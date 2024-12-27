package com.dimitrovsolutions.io.automation.driver;

import com.dimitrovsolutions.util.LoggerUtil;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.dimitrovsolutions.config.DirectoryConfig.WORKING_DIRECTORY;
import static com.dimitrovsolutions.util.LoggerUtil.hasNoHandlers;
import static com.dimitrovsolutions.util.LoggerUtil.initLogger;

/**
 * Predefined Chrome browser configuration for SeleniumFacade script.
 */
public class ChromeConfigurations implements Configurable {

    private static final String CHROME_EXE_PATH = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";

    private static final Logger logger = Logger.getLogger(ChromeConfigurations.class.getName());
    private static final String DRIVER_CONFIGURATIONS_LOGGER_FILE_NAME = "/driver_configurations.log";

    private final ChromeOptions chromeOptions;

    public ChromeConfigurations() {
        if (hasNoHandlers(logger)) {
            initLogger(logger, Level.ALL, DRIVER_CONFIGURATIONS_LOGGER_FILE_NAME);
        }

        logger.log(Level.INFO, "Chrome configurations initialized");
        setProperties();

        chromeOptions = new ChromeOptions();
        chromeOptions.setBinary(CHROME_EXE_PATH);
    }

    /**
     * Set webdriver for chrome to system property, used by ChromeOptions object.
     */
    @Override
    public void setProperties() {
        String driverPath = Paths.get(WORKING_DIRECTORY, "drivers", "chromedriver.exe").toAbsolutePath().toString();
        System.setProperty("webdriver.chrome.driver", driverPath);
    }

    /**
     * Creates new Chrome browser webdriver from ChromeOptions(Opens new browser tab)
     * using chromeDriver.exe for selenium to execute on.
     */
    @Override
    public ChromeDriver getDriver() {
        logger.log(Level.INFO, "Chrome driver initialized, and shutting down");
        LoggerUtil.tearDown(logger);
        return new ChromeDriver(chromeOptions);
    }
}