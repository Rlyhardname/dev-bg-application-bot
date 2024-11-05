package com.dimitrovsolutions.io.automation;

import com.dimitrovsolutions.io.Destructor;
import com.dimitrovsolutions.context.Context;
import com.dimitrovsolutions.io.automation.driver.DriverConfigurations;
import com.dimitrovsolutions.io.files.JobLoader;
import com.dimitrovsolutions.model.Job;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class SeleniumFacade implements Destructor {
    private final WebDriver driver;
    private final Context context;
    private static final Logger logger = Logger.getLogger(SeleniumFacade.class.getName());
    private static FileHandler fileHandler;
    private static String id;
    private static String value;
    private static String path;

    static {
        try (BufferedReader br = Files.newBufferedReader(Path.of("src/main/resources/cookies/cookie.txt").toAbsolutePath())) {
            String LOG_DIRECTORY = "src/main/resources/logs/selenium.log";

            fileHandler = new FileHandler(LOG_DIRECTORY, true);
            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);

            List<String> cookieValues = new ArrayList<>();
            String line = "";
            do {
                line = br.readLine();
                cookieValues.add(line);
            } while (line != null);

            id = cookieValues.get(0);
            value = cookieValues.get(1);
            path = cookieValues.get(2);

            logger.log(Level.INFO, "Selenium started at: " + LocalDateTime.now());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Startup error " + e.getMessage());
        }
    }

    public SeleniumFacade(Context context, String browser) {
        this.context = context;
        driver = initDriver(browser);
    }

    private WebDriver initDriver(String browser) {
        return switch (browser.toLowerCase()) {
            case "firefox" -> DriverConfigurations.firefoxConfiguration().getDriver();
            case "chrome" -> DriverConfigurations.chromeConfiguration().getDriver();
            default -> throw new IllegalStateException("Unexpected value: " + browser.toLowerCase());
        };
    }

    public void runScript() throws InterruptedException {
        configureDriver();

        navigateToLandingPage();

        acceptCookies();

        navigateToJobs();

        try {
            for (Map.Entry<Integer, Job> entry : context.getJobsCache().getCache().entrySet()) {
                Job job = openJobListing(entry);

                clickFirstApplicationButton();

                clickDropDown();

                clickComboBox();

                clickSecondApplicationButton();

                if (isPageUrlSame(job.url())) {
                    logger.log(Level.INFO, "NEW JOB " + entry.getKey() + " " + job);

                    logPageContentAfterSendingApplication();
                    // Should save if application doesn't fail
                    JobLoader.saveJobToFile(entry.getKey(), job);
                    Thread.sleep(6 * 1000 * 60);
                } else {
                    logger.log(Level.INFO, "Application complete for job" + entry.getKey() + " " + job);
                    Thread.sleep(6 * 1000 * 60);
                }

            }
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "Interrupted exception throw");
            throw new RuntimeException(e);
        } finally {
            driver.quit();
        }
    }

    private void configureDriver() {
        driver.manage().window().maximize();
    }

    private void navigateToLandingPage() throws InterruptedException {
        driver.get(context.getNavigationConfig().route().remove());
        addSessionCookie();
        Thread.sleep(1500);
    }

    private void addSessionCookie() {
        try {
            driver.manage().addCookie(new Cookie(id, value, path));
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, "Login cookies didn't load... Exit application");
            throw new IllegalArgumentException();
        }
    }

    private void navigateToJobs() throws InterruptedException {
        String route;
        while ((route = context.getNavigationConfig().route().poll()) != null) {
            driver.get(route);
            Thread.sleep(1000);
        }

    }

    private void acceptCookies() throws InterruptedException {
        WebElement element = driver.findElement(By.className("cmplz-accept"));
        if (element != null) {
            element.click();
        }

        Thread.sleep(1500);
    }

    private Job openJobListing(Map.Entry<Integer, Job> entry) throws InterruptedException {
        Job job = entry.getValue();
        driver.get(job.url());
        Thread.sleep(1000);

        return job;
    }

    private void clickFirstApplicationButton() throws InterruptedException {
        WebElement apply = driver.findElement(By.className("single-job-apply-button"));
        apply.click();
        Thread.sleep(1000);
    }

    private void clickDropDown() {
        WebElement clickDropDown = driver.findElement(By.className("upload-from-profile"));
        clickDropDown.click();
    }

    private void clickComboBox() {
        WebElement checkBox = driver.findElement(By.className("wpcf7-user_files"));
        checkBox.click();
    }

    private void clickSecondApplicationButton() {
        List<WebElement> listOfApply = driver.findElements(By.className("wpcf7-submit"));
        // Button for sending final application
        WebElement finalApply = listOfApply.get(1);
        finalApply.click();
    }

    private boolean isPageUrlSame(String url) {
        return url.equals(driver.getCurrentUrl());
    }

    private void logPageContentAfterSendingApplication() {
        HttpRequest request = context.getClient().getRequest(driver.getCurrentUrl());
        HttpResponse<String> response = context.getClient().send(request);
        String body = response.body();
        logger.log(Level.CONFIG, body);
    }

    @Override
    public void tearDown() {
        logger.log(Level.INFO, "Selenium logger teardown at: " + LocalDateTime.now());

        if (fileHandler != null) {
            fileHandler.close();
        }

    }
}
