package com.dimitrovsolutions.cache;

import com.dimitrovsolutions.io.files.JobFileSystemHandler;
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

    public void saveEntry(int id, Job job) {
        JobFileSystemHandler.saveJobToFile(id, job);
    }

    @Override
    public boolean containsJobId(int jobId) {
        return getCache().containsKey(jobId);
    }
}