package com.dimitrovsolutions.io.automation.driver;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class ChromeConfigurationTest {

    @Test
    void ChromeConfigurations_setsChromeDriverPathProperty() {
        // Given
        String key = "webdriver.chrome.driver";
        String webDriverExpectedPath = Paths.get("src", "main", "resources", "drivers", "chromedriver.exe")
                .toAbsolutePath().toString();

        // When
        ChromeConfigurations driver = new ChromeConfigurations();

        // Then
        String webDriverActualPath = System.getProperty(key);
        assertEquals(webDriverExpectedPath, webDriverActualPath);
        driver.getDriver().quit();
    }

    @Test
    void getDriver_opensNewBrowserSessionAndReturnsChromeDriver() {
        // Given
        var obj = DriverConfigurations.chromeConfiguration();
        WebDriver driver = null;
        // When
        try {
            driver = obj.getDriver();

            // Then
            assertEquals(ChromeDriver.class, obj.getDriver().getClass());

        } finally {
            assert driver != null;
            obj.getDriver().quit();
            driver.quit();
        }
    }
}