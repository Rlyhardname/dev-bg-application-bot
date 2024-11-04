package com.dimitrovsolutions.cache;

import com.dimitrovsolutions.model.Job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cache {
    private final Map<Integer, Job> cache;

    public Cache(Map<Integer, Job> cache) {
        if (cache == null) {
            throw new NullPointerException();
        }

        this.cache = cache;
    }

    public Cache() {
        cache = new HashMap<>();
    }

    public Map<Integer, Job> getCache() {
        return cache;
    }

    public List<Job> fetchJobsInAscendingOrder(String title) {
        return cache.values()
                .stream()
                .filter((x) -> x.title().equals(title))
                .sorted((a, b) -> b.localDate().compareTo(a.localDate()))
                .toList();
    }

    public List<Job> fetchJobsInDescendingOrder(String title) {
        return cache.values()
                .stream()
                .filter((x) -> x.title().equals(title))
                .sorted((a, b) -> b.localDate().compareTo(a.localDate()))
                .toList();
    }

    public void printCache() {
        cache.forEach((k, v) -> System.out.println("Jobs ID: " + k + " " + v));
    }
}
