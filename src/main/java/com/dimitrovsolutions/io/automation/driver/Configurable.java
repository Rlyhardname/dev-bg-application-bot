package com.dimitrovsolutions.io.automation.driver;

import org.openqa.selenium.WebDriver;

/**
 * Contracts for webdriver version, Chrome,Firefox,Chromium etc..
 */
public interface Configurable {
    void setProperties();

    WebDriver getDriver();
}
