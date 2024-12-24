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

public class Orchestrator implements Destructor {
    private static Orchestrator orchestrator;
    private final Context context;
    private final FileHandler fileHandler;
    private static final Logger logger = Logger.getLogger(Orchestrator.class.getName());
    private static final String LOG_DIRECTORY = "src/main/resources/logs/orchestrator.log";

    public static Context start(NavigationConfig navigationConfig) {
        if (orchestrator == null) {
            orchestrator = new Orchestrator(navigationConfig);
        }

        return orchestrator.getContext();
    }

    private Orchestrator(NavigationConfig navigationConfig) {
        this.context = new Context(new SeleniumFacade(navigationConfig, "chrome"));

        try {
            fileHandler = new FileHandler(LOG_DIRECTORY, true);
            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            logger.log(Level.INFO, "Application started at: " + LocalDateTime.now());

            collectApplicationUrls(navigationConfig.scrapePageUrl());

            context.runSelenium();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            logger.log(Level.INFO, "Application closing without errors: " + LocalDateTime.now());
            tearDownFileHandlers();
        }

    }

    private void collectApplicationUrls(String url) {
        HttpRequest request = context.getClient().getRequest(url);

        HttpResponse<String> response = context.getClient().send(request);

        String body = context.parseDocBody(response);
        context.setDocument(Jsoup.parse(body));

        print("BODY " + body);

        DocumentScraper.traverseDocumentAndAddNewJobsToJobsCache(context);
    }

    private static void print(String htmlResponseBody) {
        System.out.println(htmlResponseBody);
    }

    public void tearDownFileHandlers() {
        tearDown();

        context.tearDown();
        DocumentScraper.tearDown();
        JobFileSystemHandler.tearDown();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void tearDown() {
        logger.log(Level.INFO, "Application logger tear down at: " + LocalDateTime.now());

        if (fileHandler != null) {
            fileHandler.close();
        }

        if (orchestrator != null) {
            orchestrator = null;
        }

    }
}