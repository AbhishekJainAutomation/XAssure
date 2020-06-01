package com.xassure.framework.driver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.apache.maven.shared.invoker.*;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.xassure.dbControls.ControlsDatabaseBinding;
import com.xassure.reporting.csvHandlers.CsvWriter;
import com.xassure.reporting.logger.Logger;
import com.xassure.reporting.logger.Reporting;
import com.xassure.reporting.testCaseDetails.TestCaseData;
import com.xassure.reporting.utilities.EmailReport;
import com.xassure.reporting.utilities.PropertiesFileHandler;

public class EnvironmentSetup {

	public static Injector injector;
	public Injector apiInjector;
	public Injector dbInjector;
	PropertiesFileHandler propFileHandle = new PropertiesFileHandler();
	Logger log = null;
	static int testCaseCount = 0;
	private String testSuitId;


	static Reporting reporter = new Reporting();
	private static boolean captureScreenShotAtEveryStep = Boolean
			.parseBoolean(new PropertiesFileHandler().readProperty("setupConfig", "screenshotAtEveryStep"));

	private static boolean dbControl = Boolean
			.parseBoolean(new PropertiesFileHandler().readProperty("setupConfig", "dbControl"));

	private static boolean apiControl = Boolean
			.parseBoolean(new PropertiesFileHandler().readProperty("setupConfig", "apiControl"));

	public static boolean quitBrowser = Boolean
			.parseBoolean(new PropertiesFileHandler().readProperty("setupConfig", "quitBowser"));

	private static boolean webAppActive = Boolean
			.parseBoolean(new PropertiesFileHandler().readProperty("webApp", "isActive"));

	@Parameters({ "releaseId", "sprintId", "testSuiteId"})
	@BeforeSuite
	public void startReporter(ITestContext context, String releaseId, String sprintId, String testSuiteId) {
		this.testSuitId = testSuiteId;
		reporter.createFolder(context.getSuite().getName(),
				captureScreenShotAtEveryStep,
				releaseId,
				sprintId,
				testSuiteId);
		if (webAppActive)
			hitRequest("/api/testSuites/testSuite/execution/" + Reporting.getRunId() + "/" + testSuiteId,
					"POST");

	}

	public void hitRequest(String urlPath, String requestType) {
		try {
			String serverIp = propFileHandle.readProperty("webApp", "serverIp");
			String serverPort = propFileHandle.readProperty("webApp", "serverPort");
			System.out.println("server port - "+serverPort);
			URL url = new URL("http://" + serverIp + ":" + serverPort + urlPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(requestType);

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				//System.out.println(output);
			}

			conn.disconnect();

		} catch (UnknownHostException e) {
			System.out.println("Host not found for XAssure Web Application");
		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	public static void main(String[] args) {
//		new EnvironmentSetup().hitRequest("/api/test-suite/execution/292929", "POST");
	}

	@Parameters({ "platform", "browser" })
	@BeforeTest
	public void beforeTest(String platform, String browser) {
		testCaseCount = testCaseCount + 1;
		if (platform.equalsIgnoreCase("web")) {
			ControlsWebProvider.browser = browser;
			injector = Guice.createInjector(new ControlsBinding());
		} else if (platform.toLowerCase().contains("mobile")) {
			ControlsMobileProvider.deviceType = browser;
			ControlsMobileProvider.platform = platform;
			injector = Guice.createInjector(new ControlsBinding());
		}
		if (apiControl) {
			apiInjector = Guice.createInjector(new ApiBinding());
		}
		if (dbControl) {
			dbInjector = Guice.createInjector(new ControlsDatabaseBinding());
		}

	}

	@Parameters({ "platform", "browser" })
	@BeforeMethod
	public void beforeMethod(String platform, String browser, Method method) {
		TestCaseData testCasedata = new TestCaseData();
		testCasedata.setBrowser(browser);
		testCasedata.setTestCaseName(method.getName());
		testCasedata.setTestCaseModule(method.getDeclaringClass().getSimpleName());
		reporter.startReporter(testCasedata);
	}

	@Parameters({ "platform", "browser" })
	@AfterMethod
	public void afterMethod(String platform, String browser) {
		if (quitBrowser) {
			if (platform.equalsIgnoreCase("web")) {
				ControlsWebProvider.getDriver().quit();

			} else if (platform.contains("mobile")) {
				ControlsMobileProvider.getDriver().quit();

			}
		}
	}

	@AfterSuite
	public void afterSuite() {
		Reporting.setExecutionFinishTime();
		// if ("mobileapp".equalsIgnoreCase(ControlsMobileProvider.platform)) {
		// ControlsMobileProvider.removeApp();
		// ControlsMobileProvider.stopAppium();
		//
		// }

		CsvWriter csvWriter = new CsvWriter();
		EmailReport email = new EmailReport();
		EmailReport.totalTestCases = testCaseCount;
		email.sendEmail();
		csvWriter.updateSetupCsv(testCaseCount);
		if (webAppActive)
			hitRequest("/api/testSuites/testSuite/execution/" + Reporting.getRunId() + "/" + testSuitId,
					"PUT");

	}
}
