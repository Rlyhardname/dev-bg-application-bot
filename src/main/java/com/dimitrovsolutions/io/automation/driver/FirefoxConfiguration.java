package com.dimitrovsolutions.io.automation.driver;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.File;

public class FirefoxConfiguration implements Configurable {
    private final FirefoxProfile firefoxProfile;
    private final FirefoxOptions firefoxOptions;

    public FirefoxConfiguration() {
        setProperties();

        String FIREFOX_PROFILE_SRC = "C:\\Users\\batba\\AppData\\Local\\Mozilla\\Firefox\\Profiles\\profile_name";
        firefoxProfile = new FirefoxProfile(new File(FIREFOX_PROFILE_SRC));
        firefoxOptions = new FirefoxOptions();

        String BINARY_SRC = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
        firefoxOptions.setBinary(BINARY_SRC);
        firefoxOptions.setProfile(firefoxProfile);
    }

    public FirefoxConfiguration(String profilePath, String binaryPath) {
        setProperties();
        firefoxProfile = new FirefoxProfile(new File(profilePath));
        firefoxOptions = new FirefoxOptions();

        firefoxOptions.setBinary(binaryPath);
        firefoxOptions.setProfile(firefoxProfile);
    }

    @Override
    public void setProperties() {
        System.setProperty("webdriver.gecko.driver", "E:\\geckodriver-v0.35.0-win32\\geckodriver.exe");
    }

    @Override
    public FirefoxDriver getDriver() {
        return new FirefoxDriver(firefoxOptions);
    }


}
