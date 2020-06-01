package com.xassure.framework.webDriverFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class GetChromeDriver {
	static WebDriver driver;

	public static WebDriver getChromeDriver(String headLessExecution) {
		// Create Object of ChromeOption Class
		ChromeOptions option=new ChromeOptions();
		//Set the setHeadless is equal to true which will run test in Headless mode
		option.setHeadless(Boolean.parseBoolean(headLessExecution));
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
		} else if (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
		}
		try {
			driver = (WebDriver) new ChromeDriver(option);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return driver;
	}
}
