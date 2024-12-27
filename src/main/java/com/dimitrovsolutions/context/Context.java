package com.dimitrovsolutions.context;

import com.dimitrovsolutions.cache.AlreadyAppliedJobs;
import com.dimitrovsolutions.cache.Cache;
import com.dimitrovsolutions.cache.JobListings;
import com.dimitrovsolutions.io.Destructor;
import com.dimitrovsolutions.io.automation.SeleniumFacade;
import com.dimitrovsolutions.io.network.HttpClientFacade;
import com.dimitrovsolutions.model.Job;
import com.dimitrovsolutions.util.LoggerUtil;
import org.jsoup.nodes.Document;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.dimitrovsolutions.util.LoggerUtil.hasNoHandlers;
import static com.dimitrovsolutions.util.LoggerUtil.initLogger;

/**
 * Used to hold application modules, and pass them around as needed via arguments or by
 * passing the Context itself as a dependency
 */
public class Context implements Destructor {

    private static final String TEAR_DOWN = "Context Logger tear down at: ";

    private static final Logger logger = Logger.getLogger(Context.class.getName());
    private static final String CONTEXT_FILE_NAME = "/context.log";

    private final HttpClientFacade client = new HttpClientFacade();

    private final AlreadyAppliedJobs alreadyAppliedCache = new AlreadyAppliedJobs();
    private final JobListings jobListings = new JobListings();

    private SeleniumFacade seleniumFacade;

    private Document document;

    public Context() {
        if (hasNoHandlers(logger)) {
            initLogger(logger, Level.ALL, CONTEXT_FILE_NAME);
        }

        logger.log(Level.INFO, "Context logger starting at: " + LocalDateTime.now());
    }

    public void setSeleniumFacade(SeleniumFacade seleniumFacade) {
        this.seleniumFacade = seleniumFacade;
    }

    /**
     * Wraps HttpResponse object and returns String response of it's body.
     */
    public String parseDocBody(HttpResponse<String> response) {
        return response.body();
    }

    /**
     * Sets the Document used by JSoup for scraping and parsing.
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    /**
     * Gets the Document used by JSoup for scraping and parsing.
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Gets the current HttpClientFacade object held by the Context
     */
    public HttpClientFacade getClient() {
        return client;
    }

    /**
     * Gets in memory Cache Object with All jobs that haven't been applied to yet.
     */
    public Cache getJobsCache() {
        return jobListings;
    }

    public boolean hasNewJobs() {
        return getJobsCache().size() > 0;
    }

    public void tryAddJobToJobsCache(int jobId, Job job) {
        jobListings.putIfAbsent(jobId, job);
    }

    /**
     * Gets preloaded on start Cache Object with all previously applied to jobs.
     */
    public AlreadyAppliedJobs getAlreadyAppliedCache() {
        return alreadyAppliedCache;
    }

    /**
     * Invokes Selenium wrapper to execute job applications from JobsCache
     */
    public void runSelenium() throws InterruptedException {
        seleniumFacade.runScript(client, jobListings);
    }

    /**
     * Close down selenium session and quit webdriver.
     */
    public void closeSelenium() {
        if (seleniumFacade != null) {
            seleniumFacade.quitDriver();
        }
    }

    /**
     * Initializes webdriver which opens up a new browser window
     */
    public void initWebDriver(String browser) {
        seleniumFacade.initDriver(browser);
    }

    /**
     * Teardown method called in finally block in Orchestrator
     */
    @Override
    public void tearDown() {
        if (seleniumFacade != null) {
            seleniumFacade.tearDown();
        }

        jobListings.tearDown();
        alreadyAppliedCache.tearDown();
        client.tearDown();

        logger.log(Level.INFO, TEAR_DOWN + LocalDateTime.now());
        LoggerUtil.tearDown(logger);
    }
}