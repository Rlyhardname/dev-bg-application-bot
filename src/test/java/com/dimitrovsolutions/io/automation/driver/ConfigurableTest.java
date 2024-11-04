package com.dimitrovsolutions.io.automation.driver;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurableTest {
    @Test
    void set_properties_firefox() {
        String key = "webdriver.gecko.driver";
        String expected = "E:\\geckodriver-v0.35.0-win32\\geckodriver.exe";

        var obj = new FirefoxConfiguration();
        String actual = System.getProperty(key);

        assertEquals(expected, actual);
    }

    @Test
    void set_properties_chrome() {
        String key = "webdriver.chrome.driver";
        String expected = "E:\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe";

        var obj = new ChromeConfigurations();
        String actual = System.getProperty(key);

        assertEquals(expected, actual);
    }

    @Test
    void get_driver_returns_firefox_driver() {
        var obj = DriverConfigurations.firefoxConfiguration();

        WebDriver driver = null;
        try {
            driver = obj.getDriver();
            assertEquals(driver.getClass(), FirefoxDriver.class);
        } finally {
            assert driver != null;
            driver.quit();
        }
    }


    @Test
    void get_driver_returns_chrome_driver() {
        var obj = DriverConfigurations.chromeConfiguration();

        WebDriver driver = null;
        try {
            driver = obj.getDriver();
            assertEquals(obj.getDriver().getClass(), ChromeDriver.class);
        } finally {
            assert driver != null;
            driver.quit();
        }

    }
}
