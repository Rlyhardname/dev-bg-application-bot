package com.dimitrovsolutions.io.automation.driver;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.File;

public class FirefoxConfiguration implements Configurable {
    private final FirefoxOptions firefoxOptions;
    private static String BINARY_SRC = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    private static String firefox_profile_name = "Placeholder profile";
    private static String FIREFOX_PROFILE_SRC = "C:\\Users\\batba\\AppData\\Local\\Mozilla\\Firefox\\Profiles\\" + firefox_profile_name;

    public FirefoxConfiguration() {
        setProperties();

        firefoxOptions = new FirefoxOptions();
        FirefoxProfile firefoxProfile = null;
        try {
            firefoxProfile = new FirefoxProfile(new File(FIREFOX_PROFILE_SRC));
            firefoxOptions.setProfile(firefoxProfile);
        } catch (RuntimeException e) {
            // log no profile, or do nothing
        }

        firefoxOptions.setBinary(BINARY_SRC);
    }

    public FirefoxConfiguration(String profilePath, String binaryPath) {
        setProperties();

        firefoxOptions = new FirefoxOptions();

        FirefoxProfile firefoxProfile = null;
        try {
            firefoxProfile = new FirefoxProfile(new File(profilePath));
            firefoxOptions.setProfile(firefoxProfile);
        } catch (RuntimeException e) {
            // log no profile, or do nothing
        }

        firefoxOptions.setBinary(binaryPath);
    }

    @Override
    public void setProperties() {
        System.setProperty("webdriver.gecko.driver", "../resources/drivers/geckodriver.exe");
    }

    @Override
    public FirefoxDriver getDriver() {
        return new FirefoxDriver(firefoxOptions);
    }
}