package com.dimitrovsolutions;

import com.dimitrovsolutions.config.DirectoryConfig;
import com.dimitrovsolutions.model.NavigationConfig;
import com.dimitrovsolutions.orchestration.Orchestrator;

import java.util.ArrayDeque;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Start application from this file.
 */
public class EntryPoint {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final int INITIAL_DELAY_IN_TIME_UNITS = 1;
    private static final int REPEAT_PERIOD_IN_TIME_UNITS = 3600;

    public static void main(String[] args) {
        DirectoryConfig.initConfig();

        scheduler.scheduleAtFixedRate(() -> {
                    Orchestrator.start(new NavigationConfig(
                            "https://dev.bg/company/jobs/java/?_seniority=intern",
                            "https://dev.bg", new ArrayDeque<>(List.of(
                            "https://dev.bg/company/jobs/",
                            "https://dev.bg/company/jobs/java/",
                            "https://dev.bg/company/jobs/java/?_seniority=intern"))
                    ));
                },
                INITIAL_DELAY_IN_TIME_UNITS,
                REPEAT_PERIOD_IN_TIME_UNITS,
                TimeUnit.SECONDS
        );
    }
}