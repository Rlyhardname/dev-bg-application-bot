package com.dimitrovsolutions.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import static com.dimitrovsolutions.config.DirectoryConfig.LOGGER_PATH;
import static com.dimitrovsolutions.config.DirectoryConfig.WORKING_DIRECTORY;
import static org.junit.jupiter.api.Assertions.*;

public class LoggerUtilTest {

    Logger logger;

    @AfterEach
    void cleanUp() {
        int length = logger.getHandlers().length;
        if (length > 0) {
            logger.removeHandler(logger.getHandlers()[0]);
        }
    }

    @Test
    void hasNoHandlers_returnsFalse() throws IOException {
        // Given
        logger = Logger.getLogger("LoggerUtilTest");
        String fileName = "context.txt";
        FileHandler fileHandler = new FileHandler(
                WORKING_DIRECTORY + LOGGER_PATH + fileName, true);
        logger.addHandler(fileHandler);

        // When
        boolean result = LoggerUtil.hasNoHandlers(logger);

        // Then
        assertFalse(result);
    }

    @Test
    void hasNoHandlers_returnsTrue() {
        // Given
        logger = Logger.getLogger("LoggerUtilTest");

        // When
        boolean result = LoggerUtil.hasNoHandlers(logger);

        // Then
        assertTrue(result);
    }

    @Test
    void tearDown_loggerDoesntHaveHandlers_doesNothing(){
        // Given
        logger = Logger.getLogger("LoggerUtilTest");

        // When
        LoggerUtil.tearDown(logger);
    }

    @Test
    void tearDown_reduceHandlerLengthFromOneToZero() throws IOException {
        // Given
        logger = Logger.getLogger("LoggerUtilTest");
        String fileName = "context.txt";
        FileHandler fileHandler = new FileHandler(
                WORKING_DIRECTORY + LOGGER_PATH + fileName, true);
        logger.addHandler(fileHandler);
        int lengthBeforeRemovingHandlers = logger.getHandlers().length;

        // When
        LoggerUtil.tearDown(logger);

        // Then
        int lengthAfterRemovingHandlers = logger.getHandlers().length;
        assertNotEquals(lengthBeforeRemovingHandlers, lengthAfterRemovingHandlers);
        assertTrue(lengthAfterRemovingHandlers < lengthBeforeRemovingHandlers);
        assertEquals(1, lengthBeforeRemovingHandlers);
        assertEquals(0, lengthAfterRemovingHandlers);
    }
}