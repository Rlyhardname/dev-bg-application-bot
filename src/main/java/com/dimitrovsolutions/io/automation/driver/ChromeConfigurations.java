package com.dimitrovsolutions.io.automation.driver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Predefined Chrome browser configuration for SeleniumFacade script.
 */
public class ChromeConfigurations implements Configurable {
    ChromeOptions chromeOptions;
    private static String CHROME_EXE_PATH= "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";

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
        System.setProperty("webdriver.chrome.driver", "../resources/drivers/chromedriver.exe");
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