package com.dimitrovsolutions.io.automation.driver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeConfigurations implements Configurable {
    ChromeOptions chromeOptions;

    public ChromeConfigurations() {
        setProperties();

        chromeOptions = new ChromeOptions();
        chromeOptions.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
    }

    @Override
    public void setProperties() {
        System.setProperty("webdriver.chrome.driver", "E:\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
    }

    @Override
    public ChromeDriver getDriver() {
        return new ChromeDriver(chromeOptions);
    }


}
