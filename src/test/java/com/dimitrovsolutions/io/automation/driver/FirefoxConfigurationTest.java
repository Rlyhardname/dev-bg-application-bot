package com.dimitrovsolutions.io.automation.driver;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class FirefoxConfigurationTest {

    @Test
    void FirefoxConfiguration_setsFirefoxDriverPathProperty() {
        // Given
        String key = "webdriver.gecko.driver";
        String webDriverExpectedPath = Paths.get("src", "main", "resources", "drivers", "geckodriver.exe")
                .toAbsolutePath().toString();

        // When
        FirefoxConfiguration driver = new FirefoxConfiguration();

        // Then
        String webDriverActualPath = System.getProperty(key);
        assertEquals(webDriverExpectedPath, webDriverActualPath);
        driver.getDriver().quit();
    }

    @Test
    void getDriver_opensNewBrowserSessionAndReturnsChromeDriver() {
        // Given
        var obj = DriverConfigurations.firefoxConfiguration();
        WebDriver driver = null;
        // When
        try {
            driver = obj.getDriver();

            // Then
            assertEquals(FirefoxDriver.class, obj.getDriver().getClass());

        } finally {
            assert driver != null;
            obj.getDriver().quit();
            driver.quit();
        }
    }
}