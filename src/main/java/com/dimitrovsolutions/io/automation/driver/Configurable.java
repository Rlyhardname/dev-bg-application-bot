package com.dimitrovsolutions.io.automation.driver;

import org.openqa.selenium.WebDriver;

public interface Configurable {
    void setProperties();

    WebDriver getDriver();
}
