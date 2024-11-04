package com.dimitrovsolutions.io.automation.driver;

public class DriverConfigurations {
    public static FirefoxConfiguration firefoxConfiguration() {
        return new FirefoxConfiguration();
    }

    public static FirefoxConfiguration firefoxConfiguration(String profilePath, String binaryPath) {
        return new FirefoxConfiguration(profilePath, binaryPath);
    }

    public static ChromeConfigurations chromeConfiguration() {
        return new ChromeConfigurations();
    }
}
