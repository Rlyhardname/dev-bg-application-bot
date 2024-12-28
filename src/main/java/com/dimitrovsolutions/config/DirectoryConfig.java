package com.dimitrovsolutions.config;

import java.io.File;
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

    /**
     * Drive:/dev-bg-application-bot - from IDE
     * Drive:/dev-bg-application-bot/target - from JAR
     */
    public static final String WORKING_DIRECTORY = System.getProperty("user.dir");
    public static final String LOGGER_PATH = "/logs";

    /**
     * Run the application once so a directory and cookie.txt get created.<pre>
     * Fill cookie.txt with the following three rolls
     *     1. Cookie type
     *     2. Cookie value
     *     3. path, usually just "/"
     * </pre>
     */
    public static final String COOKIES_PATH = "/cookies";
    public static final String COOKIE_TXT = "/cookie.txt";

    /**
     * Run the application once so a directory gets created.<pre>
     * Create cache.txt in directory.
     * /pre>
     */
    public static final String CACHE_PATH = "/cache";
    public static final String CACHE_TXT = "/cache.txt";

    /**
     * Create working directories and needed txt files on first application startup
     */
    static{
        try {
            Files.createDirectories(Paths.get(WORKING_DIRECTORY, LOGGER_PATH));
            Files.createDirectories(Paths.get(WORKING_DIRECTORY, COOKIES_PATH));
            Files.createDirectories(Paths.get(WORKING_DIRECTORY, CACHE_PATH));

            File cookieTxt = new File(WORKING_DIRECTORY + COOKIES_PATH + COOKIE_TXT);
            cookieTxt.createNewFile();

            File cacheTxt = new File(WORKING_DIRECTORY + CACHE_PATH + CACHE_TXT);
            cacheTxt.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}