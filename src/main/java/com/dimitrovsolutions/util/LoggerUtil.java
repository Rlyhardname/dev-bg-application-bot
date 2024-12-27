package com.dimitrovsolutions.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static com.dimitrovsolutions.config.DirectoryConfig.LOGGER_PATH;
import static com.dimitrovsolutions.config.DirectoryConfig.WORKING_DIRECTORY;

/**
 * Common logger functionality wrapper
 */
public class LoggerUtil {

    /**
     * Create new Logger with File handler based on arguments
     */
    public static void initLogger(Logger logger, Level severityLevel, String fileName) {
        helpInit(logger, severityLevel, fileName);
    }

    private static void helpInit(Logger logger, Level severityLevel, String fileName) {
        try {
            FileHandler fileHandler = new FileHandler(WORKING_DIRECTORY + LOGGER_PATH + fileName, true);
            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);
            logger.setLevel(severityLevel);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Tear down file handler and remove instance from logger
     */
    public static void tearDown(Logger logger) {
        if (hasHandlers(logger)) {
            FileHandler handler = (FileHandler) logger.getHandlers()[0];
            handler.close();
            logger.removeHandler(handler);
        }
    }

    public static boolean hasNoHandlers(Logger logger) {
        return logger.getHandlers().length == 0;
    }

    private static boolean hasHandlers(Logger logger) {
        return logger.getHandlers().length > 0;
    }
}