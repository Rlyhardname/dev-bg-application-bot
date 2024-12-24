package com.dimitrovsolutions.io.automation.driver;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class DriverConfigurationsTest {

    @Test
    void firefoxConfiguration_returnsInstanceOfFirefoxConfiguration() {
        // Given
        FirefoxConfiguration expectedConfiguration = new FirefoxConfiguration();

        // When
        var actualConfiguration = DriverConfigurations.firefoxConfiguration();

        // Then
        assertEquals(expectedConfiguration.getClass(), actualConfiguration.getClass());
        expectedConfiguration.getDriver().quit();
        actualConfiguration.getDriver().quit();
    }

    @Test
    void chromeConfiguration_returnsInstanceOfChromeConfiguration() {
        // Given
        ChromeConfigurations expectedConfiguration = new ChromeConfigurations();

        // When
        var actualConfiguration = DriverConfigurations.chromeConfiguration();

        // Then
        assertEquals(expectedConfiguration.getClass(), actualConfiguration.getClass());
        expectedConfiguration.getDriver().quit();
        actualConfiguration.getDriver().quit();
    }
}