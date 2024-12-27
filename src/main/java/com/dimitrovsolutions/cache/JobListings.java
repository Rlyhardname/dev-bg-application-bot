package com.dimitrovsolutions.cache;

import com.dimitrovsolutions.io.Destructor;
import com.dimitrovsolutions.io.files.JobFileSystemHandler;
import com.dimitrovsolutions.model.Job;
import com.dimitrovsolutions.util.LoggerUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.dimitrovsolutions.util.LoggerUtil.hasNoHandlers;
import static com.dimitrovsolutions.util.LoggerUtil.initLogger;

/**
 * Responsible for saving web scrapped job entries to file system.
 */
public class JobListings extends Cache implements Destructor {

    public static final Logger logger = Logger.getLogger(JobListings.class.getName());
    private static final String JOBS_LISTINGS_LOG_FILE_NAME = "/job_listings.log";

    public JobListings(Map<Integer, Job> cache) {
        super(cache);
        if (hasNoHandlers(logger)) {
            initLogger(logger, Level.ALL, JOBS_LISTINGS_LOG_FILE_NAME);
        }

        logger.log(Level.INFO, "Initializing JobListings.. ");
    }

    public JobListings() {
        super(new HashMap<>());
        if (hasNoHandlers(logger)) {
            initLogger(logger, Level.ALL, JOBS_LISTINGS_LOG_FILE_NAME);
        }

        logger.log(Level.INFO, "Initializing JobListings.. ");
    }

    public void saveEntry(int id, Job job) {
        JobFileSystemHandler.saveJobToFile(id, job);
    }

    @Override
    public boolean containsJobId(int jobId) {
        return getCache().containsKey(jobId);
    }

    @Override
    public void tearDown() {
        logger.log(Level.INFO, "Tearing down JobListings.. ");
        LoggerUtil.tearDown(logger);
    }
}