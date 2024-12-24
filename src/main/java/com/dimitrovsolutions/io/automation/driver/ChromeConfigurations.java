package com.dimitrovsolutions.io.automation.driver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.Paths;

/**
 * Predefined Chrome browser configuration for SeleniumFacade script.
 */
public class ChromeConfigurations implements Configurable {

    private static String CHROME_EXE_PATH= "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";

    private final ChromeOptions chromeOptions;

    public ChromeConfigurations() {
        setProperties();

        chromeOptions = new ChromeOptions();
        chromeOptions.setBinary(CHROME_EXE_PATH);
    }

    /**
     * Set webdriver for chrome to system property, used by ChromeOptions object.
     */
    @Override
    public void setProperties() {
        String driverPath = Paths.get("src", "main", "resources", "drivers", "chromedriver.exe").toAbsolutePath().toString();
        System.setProperty("webdriver.chrome.driver", driverPath);
    }

    /**
     * Creates new Chrome browser webdriver from ChromeOptions(Opens new browser tab)
     * using chromeDriver.exe for selenium to execute on.
     */
    @Override
    public ChromeDriver getDriver() {
        return new ChromeDriver(chromeOptions);
    }
}