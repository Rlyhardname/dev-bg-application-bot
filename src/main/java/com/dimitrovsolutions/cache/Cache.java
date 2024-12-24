package com.dimitrovsolutions.cache;

import com.dimitrovsolutions.model.Job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base Class for LoaderCase - responsible for loading entries from file system,
 * and PersistenceCache - responsible for saving scrapped entries to file system.
 */
public abstract class Cache {

    private final Map<Integer, Job> cache;

    public Cache(Map<Integer, Job> cache) {
        if(cache == null){
            throw new IllegalArgumentException("Cache cannot be null");
        }

        this.cache = cache;
    }

    public Cache() {
        cache = new HashMap<>();
    }

    public Map<Integer, Job> getCache() {
        return cache;
    }

    /**
     * Returns list of Jobs from the first time to the Last time a job with such a title
     * and url had been posted.
     */
    public List<Job> fetchJobsInAscendingOrder(String title) {
        return cache.values()
                .stream()
                .filter((x) -> x.title().equals(title))
                .sorted((a, b) -> b.localDate().compareTo(a.localDate()))
                .toList();
    }

    /**
     * Returns list of Jobs from the last time to the first time a Job with such a title
     * and url had been posted.
     */
    public List<Job> fetchJobsInDescendingOrder(String title) {
        return cache.values()
                .stream()
                .filter((x) -> x.title().equals(title))
                .sorted((a, b) -> b.localDate().compareTo(a.localDate()))
                .toList();
    }

    public void putIfAbsent(int jobId, Job job) {
        getCache().putIfAbsent(jobId, job);
    }

    /**
     * Prints the current jobs cache on the console.
     */
    public static void printCache(Map<Integer, Job> cache) {
        cache.forEach((k, v) -> System.out.println("Jobs ID: " + k + " " + v));
    }

    abstract boolean containsJobId(int jobId);
}