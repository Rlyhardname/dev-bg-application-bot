package com.dimitrovsolutions.io.automation.driver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Predefined Chrome browser configuration for SeleniumFacade script.
 */
public class ChromeConfigurations implements Configurable {
    ChromeOptions chromeOptions;

    public ChromeConfigurations() {
        setProperties();

        chromeOptions = new ChromeOptions();
        chromeOptions.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
    }

    @Override
    public void setProperties() {
        System.setProperty("webdriver.chrome.driver", "../resources/drivers/chromedriver.exe");
    }

    @Override
    public ChromeDriver getDriver() {
        return new ChromeDriver(chromeOptions);
    }
}