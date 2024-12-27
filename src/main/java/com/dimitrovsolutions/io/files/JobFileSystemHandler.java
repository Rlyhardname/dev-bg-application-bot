package com.dimitrovsolutions.io.files;

import com.dimitrovsolutions.model.Job;
import com.dimitrovsolutions.util.LoggerUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.dimitrovsolutions.config.DirectoryConfig.WORKING_DIRECTORY;
import static com.dimitrovsolutions.util.LoggerUtil.hasNoHandlers;

/**
 * Util responsible for loading jobs from text file to alreadyApplied cache and saving
 * new jobs not already applied to from the jobs cache to the file system.
 */
public class JobFileSystemHandler {

    private static final String CACHE_DIRECTORY = WORKING_DIRECTORY + "/cache/cache.txt";
    private static final String PERSISTENCE_FAILURE = "Application write to file failed - err: %s";
    private static final String LOAD_ERROR = "Cache load failure - err: %s";

    private static final Logger logger = Logger.getLogger(JobFileSystemHandler.class.getName());
    public static final String JOB_LOADER_LOGGER_FILE_NAME = "/job_loader.log";

    /**
     * Loads jobs from filesystem to alreadyAppliedCache at startup up usually.
     */
    public static void loadJobs(Map<Integer, Job> alreadyAppliedCache) {
        if (hasNoHandlers(logger)) {
            LoggerUtil.initLogger(logger, Level.ALL, JOB_LOADER_LOGGER_FILE_NAME);
            logger.log(Level.INFO, "Initializing logger.. ");
        }

        logger.log(Level.INFO, "Start Loading jobs from file system to alreadyAppliedCache.. ");
        try (BufferedReader br = Files.newBufferedReader(Path.of(CACHE_DIRECTORY))) {
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }

                String[] elements = line.split("\\|");
                alreadyAppliedCache
                        .putIfAbsent(Integer.parseInt(elements[0]), new Job(elements[1], elements[2], LocalDateTime.parse(elements[3])));
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, LOAD_ERROR);
            throw new RuntimeException(e);
        }

        logger.log(Level.INFO, "Finish loading jobs from file system to alreadyAppliedCache. ");
    }

    /**
     * Saves most recently applied jobs from jobsCache to fileSystem.
     */
    public static void saveJobToFile(int id, Job job) {
        if (hasNoHandlers(logger)) {
            LoggerUtil.initLogger(logger, Level.ALL, JOB_LOADER_LOGGER_FILE_NAME);
            logger.log(Level.INFO, "Initializing logger.. ");
        }

        logger.log(Level.INFO, "Start Saving job to file with id " + id);
        try (BufferedWriter bw = Files.newBufferedWriter(Path.of(CACHE_DIRECTORY), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            String line = String.join("|", String.valueOf(id), job.title(), job.url(), String.valueOf(job.localDate()));
            line += System.lineSeparator();

            bw.write(line);

        } catch (IOException e) {
            logger.log(Level.SEVERE, PERSISTENCE_FAILURE);
            throw new RuntimeException(e);
        }

        logger.log(Level.INFO, "Finish Saving job to file with id " + id);
    }

    /**
     * Used in finally method of Orchestrator
     */
    public static void tearDown() {
        logger.log(Level.INFO, "Start Tearing down file system.");
        LoggerUtil.tearDown(logger);
    }
}