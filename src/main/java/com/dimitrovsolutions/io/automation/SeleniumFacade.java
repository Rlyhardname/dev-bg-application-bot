package com.dimitrovsolutions.io.automation;

import com.dimitrovsolutions.cache.JobListings;
import com.dimitrovsolutions.io.Destructor;
import com.dimitrovsolutions.io.automation.driver.DriverConfigurations;
import com.dimitrovsolutions.io.network.HttpClientFacade;
import com.dimitrovsolutions.model.Job;
import com.dimitrovsolutions.model.NavigationConfig;
import com.dimitrovsolutions.util.LoggerUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.dimitrovsolutions.config.DirectoryConfig.WORKING_DIRECTORY;
import static com.dimitrovsolutions.util.LoggerUtil.hasNoHandlers;
import static com.dimitrovsolutions.util.LoggerUtil.initLogger;

/**
 * Wrapper for selenium, exposing just the functionality it will produce
 */
public class SeleniumFacade implements Destructor {

    private static final Logger logger = Logger.getLogger(SeleniumFacade.class.getName());
    public static final String SELENIUM_LOGGER_FILE_NAME = "/selenium.log";

    private static String id;
    private static String value;
    private static String path;

    private WebDriver driver;
    private final NavigationConfig navigationConfig;

    /**
     * Preloads login cookies from file into private static fields id,value and path. Sets log directory for log file.
     */
    static {
        try (BufferedReader br = Files.newBufferedReader(Path.of(
                WORKING_DIRECTORY + "/cookies/cookie.txt").toAbsolutePath())) {
            initLogger(logger, Level.ALL, SELENIUM_LOGGER_FILE_NAME);
            initCookies(br);
        } catch (
                IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private static void initCookies(BufferedReader br) {
        List<String> cookieValues = new ArrayList<>();
        String line = "";
        do {
            try {
                line = br.readLine();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Profile cookies failed to load.. " + e.getMessage());
                throw new RuntimeException(e);
            }

            cookieValues.add(line);
        } while (line != null);

        id = cookieValues.get(0);
        value = cookieValues.get(1);
        path = cookieValues.get(2);
    }

    public SeleniumFacade(NavigationConfig navigationConfig) {
        if (hasNoHandlers(logger)) {
            initLogger(logger, Level.ALL, SELENIUM_LOGGER_FILE_NAME);
        }

        logger.log(Level.INFO, "Selenium started at: " + LocalDateTime.now());
        this.navigationConfig = navigationConfig;
    }

    /**
     * Creates a webdriver based on browser chosen and opens an instance of the browser.
     */
    public void initDriver(String browser) {
        driver = switch (browser.toLowerCase()) {
            case "firefox" -> DriverConfigurations.firefoxConfiguration().getDriver();
            case "chrome" -> DriverConfigurations.chromeConfiguration().getDriver();
            default -> throw new IllegalStateException("Unexpected value: " + browser.toLowerCase());
        };
    }

    /**
     * Selenium script complete flow block.
     */
    public void runScript(HttpClientFacade client, JobListings jobListings) throws InterruptedException {
        System.out.println("enter");
        configureDriver();

        navigateToLandingPage();

        acceptCookies();

        navigateToJobs();

        try {
            for (Map.Entry<Integer, Job> entry : jobListings.getCache().entrySet()) {
                Job job = openJobListing(entry.getValue());

                clickFirstApplicationButton();

                clickDropDown();

                clickComboBox();

                clickSecondApplicationButton();

                if (isPageUrlSame(job.url())) {
                    logger.log(Level.INFO, "NEW JOB " + entry.getKey() + " " + job);

                    logPageContentAfterSendingApplication(client);
                    // Should save if application doesn't fail
                    logger.log(Level.INFO, "saving job " + entry.getKey() + " " + job);
                    jobListings.saveEntry(entry.getKey(), job);
                } else {
                    logger.log(Level.INFO, "redirected on job " + entry.getKey() + " " + job);
                }

            }
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "Interrupted exception throw");
            throw new RuntimeException(e);
        } finally {
            quitDriver();
        }
    }

    /**
     * Maximize browser window by default.
     */
    private void configureDriver() {
        driver.manage().window().maximize();
    }

    /**
     * Go to the predefined route and add session cookies, to accept website cookies and login.
     */
    private void navigateToLandingPage() throws InterruptedException {
        driver.get(navigationConfig.route().remove());
        addSessionCookie();
        Thread.sleep(1500);
    }

    /**
     * Add session/login cookies to webdriver implementation used.
     */
    private void addSessionCookie() {
        try {
            driver.manage().addCookie(new Cookie(id, value, path));
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, "Login cookies corrupted... Exit application");
            throw new IllegalArgumentException();
        }
    }

    /**
     * Move towards the end of the navigation route, pausing 1 second per click, simulating real user.
     */
    private void navigateToJobs() throws InterruptedException {
        String route;
        while ((route = navigationConfig.route().poll()) != null) {
            driver.get(route);
            Thread.sleep(1000);
        }
    }

    /**
     * Accept website cookies.
     */
    private void acceptCookies() throws InterruptedException {
        WebElement element = driver.findElement(By.className("cmplz-accept"));
        if (element != null) {
            element.click();
        }

        Thread.sleep(1500);
    }

    /**
     * Open url of job being applied to.
     */
    private Job openJobListing(Job job) throws InterruptedException {
        driver.get(job.url());
        Thread.sleep(1000);

        return job;
    }

    /**
     * Click apply button triggering application form to popup.
     */
    private void clickFirstApplicationButton() throws InterruptedException {
        WebElement apply = driver.findElement(By.className("single-job-apply-button"));
        apply.click();
        Thread.sleep(1000);
    }

    /**
     * Click dropdown with my files, triggering list of my files to show.
     */
    private void clickDropDown() {
        WebElement clickDropDown = driver.findElement(By.className("upload-from-profile"));
        clickDropDown.click();
    }

    /**
     * Choose combo box with cv file(should only have one cv file).
     */
    private void clickComboBox() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement checkBox = wait.until(
                ExpectedConditions.elementToBeClickable(
                        driver.findElement(By.className("wpcf7-user_files"))));
        checkBox.click();
    }

    /**
     * Click second application button triggering CV being sent.
     */
    private void clickSecondApplicationButton() {
        List<WebElement> listOfApply = driver.findElements(By.className("wpcf7-submit"));
        // Button for sending final application
        WebElement finalApply = listOfApply.get(1);
        finalApply.click();
    }

    /**
     * Check if we've been redirected somewhere, should stay on the same page after application
     */
    private boolean isPageUrlSame(String url) {
        return url.equals(driver.getCurrentUrl());
    }

    /**
     * Log the current page.
     */
    private void logPageContentAfterSendingApplication(HttpClientFacade client) {
        HttpRequest request = client.getRequest(driver.getCurrentUrl());
        HttpResponse<String> response = client.send(request);
        String body = response.body();
        logger.log(Level.CONFIG, body);
    }

    /**
     * Destroy the current instance of the driver, closing the browser window.
     */
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Used in finally method of Orchestrator.
     */
    @Override
    public void tearDown() {
        logger.log(Level.INFO, "Selenium logger teardown at: " + LocalDateTime.now());
        LoggerUtil.tearDown(logger);
    }
}