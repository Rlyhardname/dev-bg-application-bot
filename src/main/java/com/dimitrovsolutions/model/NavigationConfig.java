package com.dimitrovsolutions.model;

import java.util.Queue;

/**
 * Record with the path taken by the automated application from the landing page
 * to the final page of the jobs listings where the scrapping kicks in.
 */
public record NavigationConfig(String scrapePageUrl, String landingPage, Queue<String> route) {
}

