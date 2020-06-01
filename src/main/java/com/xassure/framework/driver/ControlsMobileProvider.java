package com.xassure.framework.driver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Provider;
import com.xassure.reporting.utilities.PropertiesFileHandler;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class ControlsMobileProvider implements Provider<Controls> {

	public static Map<String, Controls> mobileDriverMap;
	public static String deviceType;
	public static String platform;

	private static String serverIpAddress;
	private static int serverPort;
	private static String androidHome;
	private static String appiumJsPath;
	private static String nodeJsPath;
	private static String deviceName;
	private static String deviceId;

	private static String platformVersion;
	private static String appPackage;
	private static String appActivity;
	private static String appBuildPath;
	private static String appBuildName;
	private static String mobileBrowser;
	private static int appiumCommandTimeout;
	boolean isMobileConfigSet = false;
	boolean isAppiumConfigset = false;

	public static AppiumDriverLocalService service = null;

	AppiumServiceBuilder builder = null;
	PropertiesFileHandler _propHandler = new PropertiesFileHandler();

	@Override
	public Controls get() {
		AppiumDriver<MobileElement> driver;
		String threadId = Thread.currentThread().getId() + "";
		System.out.println("Value of thread id in driver class " + threadId);

		if (service == null) {
			setAppiumConfig();
			startAppium(deviceType);
			System.out.println("Appium servier started successfullty - " + deviceType);
		} else if (service.isRunning() == false) {
			startAppium(deviceType);
			System.out.println("Appium servier started successfullty with devicetype - " + deviceType);
		}

		if (mobileDriverMap == null) {
			setMobileConfig();
			mobileDriverMap = new HashMap<>();
			driver = createDriver(deviceType);
			mobileDriverMap.put(threadId, new MobileControlsLibrary(driver));
		} else {

			if (!mobileDriverMap.containsKey(threadId)) {
				driver = createDriver(deviceType);
				mobileDriverMap.put(threadId, new MobileControlsLibrary(driver));
			} else {
				if (mobileDriverMap.get(threadId).getDriver().toString().contains("null")
						|| EnvironmentSetup.quitBrowser == true) {
					driver = createDriver(deviceType);
					mobileDriverMap.put(threadId, new MobileControlsLibrary(driver));
				}
			}
		}
		return mobileDriverMap.get(threadId);
	}

	private void setAppiumConfig() {
		if (isAppiumConfigset == false) {
			serverIpAddress = _propHandler.readProperty("mobileConfig", "appiumIpAddress");
			serverPort = Integer.parseInt(_propHandler.readProperty("mobileConfig", "appiumPort"));
			androidHome = _propHandler.readProperty("mobileConfig", "androidHome");
			appiumJsPath = _propHandler.readProperty("mobileConfig", "appiumJsPath");
			appiumCommandTimeout = Integer.parseInt(_propHandler.readProperty("mobileConfig", "appiumCmdTimeOut"));
			isAppiumConfigset = true;
		}
	}

	private void setMobileConfig() {
		if (isMobileConfigSet == false) {
			deviceName = _propHandler.readProperty("mobileConfig", "deviceName");
			deviceId = _propHandler.readProperty("mobileConfig", "deviceId");
			platformVersion = _propHandler.readProperty("mobileConfig", "platformVersion");
			appPackage = _propHandler.readProperty("mobileConfig", "appPackage");
			appActivity = _propHandler.readProperty("mobileConfig", "appActivity");
			appBuildPath = _propHandler.readProperty("mobileConfig", "appDirectory");
			appBuildName = _propHandler.readProperty("mobileConfig", "appName");
			mobileBrowser = _propHandler.readProperty("mobileConfig", "browser");
			isMobileConfigSet = true;
		}
	}

	private void startAppium(String deviceType) {

		DesiredCapabilities appiumCapabilities;
		// Set Capabilities
		appiumCapabilities = new DesiredCapabilities();
		try {
			appiumCapabilities.setCapability("noReset", "false");
			appiumCapabilities.setCapability("newCommandTimeout", appiumCommandTimeout);

			// Build the Appium service
			builder = new AppiumServiceBuilder();
			builder.withIPAddress(serverIpAddress);
			builder.usingPort(serverPort);
			builder.withCapabilities(appiumCapabilities);
			builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
			builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");
			builder.withAppiumJS(new File(appiumJsPath));
			if (deviceType.equalsIgnoreCase("android"))
				builder.withEnvironment(ImmutableMap.of("ANDROID_HOME", androidHome));
			// Start the server with the builder
			service = AppiumDriverLocalService.buildService(builder);
			service.start();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static void stopAppium() {
		if (service.isRunning()) {
			service.stop();
			Runtime rt = Runtime.getRuntime();
			try {
				Process proc = rt.exec("ping localhost");
				proc.destroy();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void removeApp() {
		getDriver().removeApp(appPackage);
	}

	private AppiumDriver<MobileElement> createDriver(String deviceType) {
		AppiumDriver<MobileElement> driver = null;
		try {
			DesiredCapabilities caps = new DesiredCapabilities();
			File appdir = new File(appBuildPath);
			File app = new File(appdir, appBuildName);

			caps.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
			// 2e3d8c7ac11079f9249afd56bd06638587a69a30
			caps.setCapability(MobileCapabilityType.UDID, deviceId);
			caps.setCapability(MobileCapabilityType.PLATFORM_NAME, deviceType);
			caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);

			if (platform.toLowerCase().contains("app")) {
				System.out.println("Insider mobile app creation >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				caps.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
				System.out.println(app.getAbsolutePath());
				if (!appActivity.isEmpty()) {
					caps.setCapability("appActivity", appActivity);
					System.out.println(appPackage + appActivity + " set up app activity>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				}
			} else if (platform.toLowerCase().contains("browser")) {
				if (deviceType.equalsIgnoreCase("android")) {

					// Set up the capability of chromedriverExecutable to trigger chrome
					// Chrome in mobile is triggered with the help of chromedriver which is chosen on the basis of
					// OS of execution machine
					if (System.getProperty("os.name").toLowerCase().contains("windows")) {
						caps.setCapability("chromedriverExecutable",
								"src/main/resources/chromedriver.exe");
					} else if (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
						caps.setCapability("chromedriverExecutable",
								"src/main/resources/chromedriver");
					}
					caps.setCapability("browserName", "Chrome");
					caps.setCapability("showChromedriverLog", "true");

				} else if (deviceType.equalsIgnoreCase("ios")) {
					caps.setCapability("browserName", "Safari");

				}
			}
			caps.setCapability(MobileCapabilityType.NO_RESET, "true");
			if (deviceType.equalsIgnoreCase("android")) {
				if (!appPackage.isEmpty())
//					caps.setCapability("appActivity", appActivity);
//					caps.setCapability("appPackage", appPackage);
				System.out.println(appPackage + appActivity + "set up app package>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				driver = new AndroidDriver<MobileElement>(
						new URL("http://" + serverIpAddress + ":" + serverPort + "/wd/hub"), caps);
				System.out.println("Driver >>>>>>>>>>>>>>" + driver);
			} else if (deviceType.equalsIgnoreCase("ios")) {
				caps.setCapability("bundleId", appPackage);
				driver = new IOSDriver<MobileElement>(
						new URL("http://" + serverIpAddress + ":" + serverPort + "/wd/hub"), caps);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return driver;
	}

	@SuppressWarnings("unchecked")
	public static AppiumDriver<MobileElement> getDriver() {
		String threadId = Thread.currentThread().getId() + "";
		return (AppiumDriver<MobileElement>) mobileDriverMap.get(threadId).getDriver();
	}

}
