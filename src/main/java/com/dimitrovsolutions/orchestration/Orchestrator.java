package com.dimitrovsolutions.orchestration;

import com.dimitrovsolutions.io.Destructor;
import com.dimitrovsolutions.io.automation.SeleniumFacade;
import com.dimitrovsolutions.context.Context;
import com.dimitrovsolutions.io.html.DocumentScraper;
import com.dimitrovsolutions.io.files.JobFileSystemHandler;
import com.dimitrovsolutions.model.NavigationConfig;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * The class responsible for the complete flow of the application
 */
public class Orchestrator implements Destructor {

    public static final String BROWSER = "chrome";

    private static volatile Orchestrator orchestrator;

    private final Context context;

    private static final FileHandler fileHandler;
    private static final Logger logger = Logger.getLogger(Orchestrator.class.getName());
    private static final String LOG_DIRECTORY = "src/main/resources/logs/orchestrator.log";

    static {
        try {
            fileHandler = new FileHandler(LOG_DIRECTORY, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fileHandler.setFormatter(new SimpleFormatter());
    }

    public static synchronized void start(NavigationConfig navigationConfig) {
        if (orchestrator == null) {
            orchestrator = new Orchestrator(navigationConfig);
            orchestrator.tearDown();
        }
    }

    private Orchestrator(NavigationConfig navigationConfig) {
        this.context = new Context();

        try {
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            logger.log(Level.INFO, "Application started at: " + LocalDateTime.now());

            sendHttpRequestParseResponseScrapeDataIntoJobsCache(navigationConfig.scrapePageUrl());

            if (context.hasNewJobs()) {
                context.setSeleniumFacade(new SeleniumFacade(navigationConfig));
                context.initWebDriver(BROWSER);
                context.runSelenium();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            logger.log(Level.INFO, "Application closing without errors: " + LocalDateTime.now());
            tearDownFileHandlers();
        }
    }

    /**
     * Builds Http requests, sends it, parses the response body and scrapes the new jobs listings
     * into jobsCache.
     */
    private void sendHttpRequestParseResponseScrapeDataIntoJobsCache(String url) {
        HttpRequest request = targetedPageForWebScrapping(url);

        HttpResponse<String> response = sendRequestReturnHttpResponse(request);

        String stringRepresentationOfDocBody = context.parseDocBody(response);

        context.setDocument(Jsoup.parse(stringRepresentationOfDocBody));

        print("BODY " + stringRepresentationOfDocBody);

        DocumentScraper.traverseDocumentAndAddNewJobsToJobsCache(context);
    }

    /**
     * Build httpRequest for the provided url
     */
    private HttpRequest targetedPageForWebScrapping(String url) {
        return context.getClient().getRequest(url);
    }

    /**
     * send async request and on receiving response returns HttpResponse for the scrapper to handle
     */
    private HttpResponse<String> sendRequestReturnHttpResponse(HttpRequest request) {
        return context.getClient().send(request);
    }

    /**
     * Print String representation of response body into the command line.
     */
    private static void print(String htmlResponseBody) {
        System.out.println(htmlResponseBody);
    }

    /**
     * Teardown used in finally block of Orchestrator.
     */
    public void tearDownFileHandlers() {
        context.tearDown();
        DocumentScraper.tearDown();
        JobFileSystemHandler.tearDown();
    }

    /**
     * Tear down log related file handler and log event.
     */

    public synchronized void tearDown() {
        logger.log(Level.INFO, "Application logger tear down at: " + LocalDateTime.now());

        if (fileHandler != null) {
            fileHandler.close();
        }

        if (orchestrator != null) {
            orchestrator = null;
        }
    }
}