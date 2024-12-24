package com.dimitrovsolutions.cache;

import com.dimitrovsolutions.io.files.JobLoader;
import com.dimitrovsolutions.model.Job;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for saving web scrapped job entries to file system.
 */
public class PersistenceCache extends Cache {

    public PersistenceCache(Map<Integer, Job> cache) {
        super(cache);
    }

    public PersistenceCache() {
        super(new HashMap<>());
    }

    @Override
    public void loadEntries() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveEntry(int id, Job job) {
        JobLoader.saveJobToFile(id, job);
    }
}