package com.dimitrovsolutions;

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
    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {
        scheduler.scheduleAtFixedRate(() -> {
                    Orchestrator.start(new NavigationConfig(
                            "https://dev.bg/company/jobs/java/?_seniority=intern",
                            "https://dev.bg", new ArrayDeque<>(List.of(
                            "https://dev.bg/company/jobs/",
                            "https://dev.bg/company/jobs/java/",
                            "https://dev.bg/company/jobs/java/?_seniority=intern"))
                    ));
                },
                1,
                7,
                TimeUnit.SECONDS
        );
    }
}