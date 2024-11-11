package com.dimitrovsolutions.model;

import java.util.Queue;

public record NavigationConfig(String scrapePageUrl, String landingPage, Queue<String> route) {
}

