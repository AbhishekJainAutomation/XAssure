package com.xassure.framework.driver;

import java.util.HashMap;
import java.util.Map;

import com.xassure.reporting.utilities.PropertiesFileHandler;
import org.openqa.selenium.WebDriver;

import com.google.inject.Provider;
import com.xassure.framework.webDriverFactory.GetChromeDriver;
import com.xassure.framework.webDriverFactory.GetFirefoxDriver;

public class ControlsWebProvider implements Provider<Controls> {
	public static Map<String, Controls> webDriverMap;
	private static WebDriver driver;
	public static String browser;
	public static String headLessExecution;
	PropertiesFileHandler _propHandler = new PropertiesFileHandler();

	@Override
	public Controls get() {
		String threadId = Thread.currentThread().getId() + "";
		System.out.println("Value of thread id in driver class " + threadId);

		headLessExecution = _propHandler.readProperty("setupConfig", "headLessExecution");
		if (webDriverMap == null) {
			webDriverMap = new HashMap<>();
			WebDriver driver = selectDriver(browser, headLessExecution);
			webDriverMap.put(threadId, new WebControlsLibrary(driver));

		} else if (!webDriverMap.containsKey(threadId)) {
			WebDriver driver = selectDriver(browser, headLessExecution);
			webDriverMap.put(threadId, new WebControlsLibrary(driver));
		} else if ((webDriverMap.get(threadId)).getDriver().toString().contains("null")) {
			WebDriver driver = selectDriver(browser, headLessExecution);
			webDriverMap.put(threadId, new WebControlsLibrary(driver));
		}

		return webDriverMap.get(threadId);
	}

	public static WebDriver getDriver() {
		String threadId = Thread.currentThread().getId() + "";
		return webDriverMap.get(threadId).getDriver();
	}

	private WebDriver selectDriver(String browser, String headLessExecution) {
		WebDriver newDriver = null;
		switch (browser.toUpperCase()) {
		case "CHROME":
			newDriver = GetChromeDriver.getChromeDriver(headLessExecution);
			break;

		case "IE":
			newDriver = null;
			break;

		case "FIREFOX":
			newDriver = GetFirefoxDriver.getFirefoxDriver(headLessExecution);
			break;

		case "SAFARI":
			newDriver = null;
			break;
		}

		return newDriver;
	}
}
