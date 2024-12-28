package com.dimitrovsolutions.model;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Record with the path taken by the automated application from the landing page
 * to the final page of the jobs listings where the scrapping kicks in.
 */
public class NavigationConfig {
    private final String scrapePageUrl;
    private final Queue<String> route;

    public NavigationConfig(String scrapePageUrl) {
        this.scrapePageUrl = scrapePageUrl;
        route = new ArrayDeque<>();
        fillRoute();
    }

    private void fillRoute() {
        String devBgIndexUrl = scrapePageUrl.substring(0, 15);
        route.add(devBgIndexUrl);
        int nextUpperBond = 15;
        while (true) {
            nextUpperBond = scrapePageUrl.indexOf("/", nextUpperBond + 1);
            if (nextUpperBond == -1) {
                break;
            }

            route.add(scrapePageUrl.substring(0, nextUpperBond + 1));
        }

        route.add(scrapePageUrl);
        route.forEach(System.out::println);
    }

    public Queue<String> route() {
        return route;
    }

    public String scrapePageUrl() {
        return scrapePageUrl;
    }
}