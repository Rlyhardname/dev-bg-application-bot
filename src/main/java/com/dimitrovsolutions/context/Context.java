package com.dimitrovsolutions.context;

import com.dimitrovsolutions.cache.Cache;
import com.dimitrovsolutions.cache.LoaderCache;
import com.dimitrovsolutions.cache.PersistenceCache;
import com.dimitrovsolutions.io.Destructor;
import com.dimitrovsolutions.io.network.HttpClientFacade;
import com.dimitrovsolutions.model.NavigationConfig;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Context implements Destructor {
    private static final Logger logger = Logger.getLogger(Context.class.getName());
    private final FileHandler fileHandler;
    private final HttpClientFacade client = new HttpClientFacade();
    private final NavigationConfig navigationConfig;
    private final Cache alreadyAppliedCache = new LoaderCache();
    private final Cache jobsCache = new PersistenceCache();
    private Document document;

    public Context(NavigationConfig navigationConfig) {
        this.navigationConfig = navigationConfig;
        try {
            fileHandler = new FileHandler("src/main/resources/logs/context.log", true);
            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            logger.log(Level.INFO, "Context logger starting at: " + LocalDateTime.now());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Already send applications file didn't load");
            throw new RuntimeException(e);
        }
    }

    public String parseDocBody(HttpResponse<String> response) {
        return response.body();
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public HttpClientFacade getClient() {
        return client;
    }

    public Cache getJobsCache() {
        return jobsCache;
    }

    public Cache getAlreadyAppliedCache() {
        return alreadyAppliedCache;
    }

    public Document getDocument() {
        return document;
    }

    public NavigationConfig getNavigationConfig() {
        return navigationConfig;
    }

    @Override
    public void tearDown() {
        logger.log(Level.INFO, "Context Logger tear down at: " + LocalDateTime.now());

        if (fileHandler != null) {
            fileHandler.close();
        }

    }
}
