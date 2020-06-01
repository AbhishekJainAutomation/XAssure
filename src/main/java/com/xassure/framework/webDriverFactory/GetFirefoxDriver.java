package com.xassure.framework.webDriverFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class GetFirefoxDriver {
	static WebDriver driver;

	public static WebDriver getFirefoxDriver(String headLessExecution) {

		// Create Object of ChromeOption Class
		FirefoxOptions option = new FirefoxOptions();
		//Set the setHeadless is equal to true which will run test in Headless mode
		option.setHeadless(Boolean.parseBoolean(headLessExecution));

		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			System.setProperty("webDriver.gecko.driver", "src/main/resources/gecko.exe");
		} else if (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
			System.setProperty("webDriver.gecko.driver", "src/main/resources/gecko");
		}
		try {
			driver = (WebDriver) new FirefoxDriver(option);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return driver;
	}
}
