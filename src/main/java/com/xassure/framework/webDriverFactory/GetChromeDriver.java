package com.xassure.framework.webDriverFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class GetChromeDriver {
	static WebDriver driver;

	public static WebDriver getChromeDriver() {
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
		} else if (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
		}
		try {
			driver = (WebDriver) new ChromeDriver();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return driver;
	}
}
