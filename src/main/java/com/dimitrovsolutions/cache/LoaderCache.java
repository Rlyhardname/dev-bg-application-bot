package com.dimitrovsolutions.cache;

import com.dimitrovsolutions.io.files.JobFileSystemHandler;
import com.dimitrovsolutions.model.Job;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for loading previously saved job entries from file system into
 * in memory Map
 */
public class LoaderCache extends Cache {

    public LoaderCache(Map<Integer, Job> cache) {
        super(cache);
        loadEntries();
    }

    public LoaderCache() {
        super(new HashMap<>());
        loadEntries();
    }

    public void loadEntries() {
        JobFileSystemHandler.loadJobs(getCache());
    }

    @Override
    public boolean containsJobId(int jobId) {
        return getCache().containsKey(jobId);
    }
}