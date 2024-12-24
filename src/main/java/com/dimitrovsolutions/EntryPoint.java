package com.dimitrovsolutions;

import com.dimitrovsolutions.model.NavigationConfig;
import com.dimitrovsolutions.orchestration.Orchestrator;

import java.util.ArrayDeque;
import java.util.List;

/**
 * Start application from this file.
 */
public class EntryPoint {
    public static void main(String[] args) {
        Orchestrator.start(new NavigationConfig(
                "https://dev.bg/company/jobs/java/?_seniority=intern",
                "https://dev.bg", new ArrayDeque<>(List.of(
                "https://dev.bg/company/jobs/",
                "https://dev.bg/company/jobs/java/",
                "https://dev.bg/company/jobs/java/?_seniority=intern"))
        ));
    }
}