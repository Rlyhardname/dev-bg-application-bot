package com.dimitrovsolutions.io.files;

import com.dimitrovsolutions.model.Job;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Util responsible for loading jobs from text file to alreadyApplied cache and saving
 * new jobs not already applied to from the jobs cache to the file system.
 */
public class JobFileSystemHandler {

    private static final String CACHE_DIRECTORY = "src/main/resources/cache/cache.txt";
    private static final String PERSISTENCE_FAILURE = "Application write to file failed - err: %s";
    private static final String LOAD_ERROR = "Cache load failure - err: %s";

    private static final FileHandler fileHandler;
    private static final Logger logger = Logger.getLogger(JobFileSystemHandler.class.getName());
    private static final String LOG_DIRECTORY = "src/main/resources/logs/job_loader.log";

    static {
        try {
            fileHandler = new FileHandler(LOG_DIRECTORY, true);
            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads jobs from filesystem to alreadyAppliedCache at startup up usually.
     */
    public static void loadJobs(Map<Integer, Job> alreadyAppliedCache) {
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
    }

    /**
     * Saves most recently applied jobs from jobsCache to fileSystem.
     */
    public static void saveJobToFile(int id, Job job) {
        try (BufferedWriter bw = Files.newBufferedWriter(Path.of(CACHE_DIRECTORY), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            String line = String.join("|", String.valueOf(id), job.title(), job.url(), String.valueOf(job.localDate()));
            line += System.lineSeparator();

            bw.write(line);

        } catch (IOException e) {
            logger.log(Level.SEVERE, PERSISTENCE_FAILURE);
            throw new RuntimeException(e);
        }
    }

    /**
     * Used in finally method of Orchestrator
     */
    public static void tearDown() {
        if (fileHandler != null) {
            fileHandler.close();
        }
    }
}