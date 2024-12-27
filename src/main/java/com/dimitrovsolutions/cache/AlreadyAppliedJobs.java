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
 * Responsible for loading previously saved job entries from file system into
 * in memory Map
 */
public class AlreadyAppliedJobs extends Cache implements Destructor {

    public static final Logger logger = Logger.getLogger(AlreadyAppliedJobs.class.getName());
    private static final String ALREADY_APPLIED_JOBS_LOG_FILE_NAME = "/already_applied_jobs.log";

    public AlreadyAppliedJobs(Map<Integer, Job> cache) {
        super(cache);
        if (hasNoHandlers(logger)) {
            initLogger(logger, Level.ALL, ALREADY_APPLIED_JOBS_LOG_FILE_NAME);
        }

        logger.log(Level.INFO, "Cache initialized");
        loadEntries();
    }

    public AlreadyAppliedJobs() {
        super(new HashMap<>());
        if (hasNoHandlers(logger)) {
            initLogger(logger, Level.ALL, ALREADY_APPLIED_JOBS_LOG_FILE_NAME);
        }

        logger.log(Level.INFO, "Cache initialized");
        loadEntries();
    }

    public void loadEntries() {
        JobFileSystemHandler.loadJobs(getCache());
    }

    @Override
    public boolean containsJobId(int jobId) {
        return getCache().containsKey(jobId);
    }

    @Override
    public void tearDown() {
        logger.log(Level.INFO, "Tearing down");
        LoggerUtil.tearDown(logger);
    }
}