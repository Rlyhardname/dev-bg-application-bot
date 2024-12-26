package com.dimitrovsolutions.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * <pre>
 * Responsible for setting up needed directories for the application to work.
 * Start the application once to create all directories, and then manually add
 *
 * cookie.txt
 * cache.txt
 * chromedriver.exe for chrome or geckodriver.exe firefox </pre>
 */
public class DirectoryConfig {

    public static final String WORKING_DIRECTORY = System.getProperty("user.dir");
    public static final String LOGGER_PATH = "/logs";

    /**
     * Run the application once so a directory gets created.<pre>
     * Create "cookie.txt" file in directory and put on new rolls
     *     1. Cookie type
     *     2. Cookie value
     *     3. path, usually just "/"
     * </pre>
     */
    public static final String COOKIES_PATH = "/cookies";

    /**
     * Run the application once so a directory gets created.<pre>
     * Add chromedriver.exe for chrome browser
     * Add geckodriver.exe for firefox browser
     * </pre>
     */
    public static final String DRIVERS_PATH = "/drivers";

    /**
     * Run the application once so a directory gets created.<pre>
     * Create cache.txt in directory.
     * /pre>
     */
    public static final String CACHE_PATH = "/cache";

    public static void initConfig() {
        try {
            Files.createDirectories(Paths.get(WORKING_DIRECTORY, LOGGER_PATH));
            Files.createDirectories(Paths.get(WORKING_DIRECTORY, COOKIES_PATH));
            Files.createDirectories(Paths.get(WORKING_DIRECTORY, DRIVERS_PATH));
            Files.createDirectories(Paths.get(WORKING_DIRECTORY, CACHE_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}