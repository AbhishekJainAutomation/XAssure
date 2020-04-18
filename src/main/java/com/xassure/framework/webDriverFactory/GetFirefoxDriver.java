package com.xassure.framework.webDriverFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class GetFirefoxDriver {
	static WebDriver driver;

	public static WebDriver getFirefoxDriver() {
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			System.setProperty("webDriver.gecko.driver", "src/main/resources/gecko.exe");
		} else if (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
			System.setProperty("webDriver.gecko.driver", "src/main/resources/gecko");
		}
		try {
			driver = (WebDriver) new FirefoxDriver();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return driver;
	}
}
