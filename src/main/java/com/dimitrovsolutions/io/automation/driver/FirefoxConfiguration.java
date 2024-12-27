package com.dimitrovsolutions.io.automation.driver;

import com.dimitrovsolutions.util.LoggerUtil;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.dimitrovsolutions.util.LoggerUtil.hasNoHandlers;
import static com.dimitrovsolutions.util.LoggerUtil.initLogger;

/**
 * Predefined firefox browser configuration for selenium
 */
public class FirefoxConfiguration implements Configurable {

    private static final String FIREFOX_EXE_PATH = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    private static final String FIREFOX_PROFILE_NAME = "Placeholder profile";
    private static final String FIREFOX_PROFILE_SRC = "C:\\Users\\batba\\AppData\\Local\\Mozilla\\Firefox\\Profiles\\";
    private static final String FIREFOX_PROFILE_PATH = FIREFOX_PROFILE_SRC + FIREFOX_PROFILE_NAME;

    private final FirefoxOptions firefoxOptions;

    private static final Logger logger = Logger.getLogger(FirefoxConfiguration.class.getName());
    private static final String DRIVER_CONFIGURATIONS_LOGGER_FILE_NAME = "/driver_configurations.log";

    public FirefoxConfiguration() {
        if (hasNoHandlers(logger)) {
            initLogger(logger, Level.ALL, DRIVER_CONFIGURATIONS_LOGGER_FILE_NAME);
        }

        logger.log(Level.INFO, "Firefox Configurations logger initialized");

        firefoxOptions = new FirefoxOptions();
        FirefoxProfile firefoxProfile = null;
        try {
            firefoxProfile = new FirefoxProfile(new File(FIREFOX_PROFILE_PATH));
            firefoxOptions.setProfile(firefoxProfile);
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Firefox profile failed to initialize");
        }

        firefoxOptions.setBinary(FIREFOX_EXE_PATH);
    }

    public FirefoxConfiguration(String profilePath, String binaryPath) {
        if (hasNoHandlers(logger)) {
            initLogger(logger, Level.ALL, DRIVER_CONFIGURATIONS_LOGGER_FILE_NAME);
        }

        logger.log(Level.INFO, "Firefox Configurations logger initialized");

        firefoxOptions = new FirefoxOptions();
        FirefoxProfile firefoxProfile = null;
        try {
            firefoxProfile = new FirefoxProfile(new File(profilePath));
            firefoxOptions.setProfile(firefoxProfile);
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Firefox profile failed to initialize");
        }

        firefoxOptions.setBinary(binaryPath);
    }

    /**
     * Creates new Firefox browser webdriver from FirefoxOptions(Opens new browser tab)
     * using geckodriver.exe for selenium to execute on
     */
    @Override
    public FirefoxDriver getDriver() {
        logger.log(Level.INFO, "Chrome driver initialized, and shutting down");
        LoggerUtil.tearDown(logger);
        return new FirefoxDriver(firefoxOptions);
    }
}