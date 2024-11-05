package com.dimitrovsolutions.model;

import java.util.ArrayDeque;

public record NavigationConfig(String landingPage, ArrayDeque<String> route) {
}

