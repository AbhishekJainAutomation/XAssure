package com.xassure.framework.driver;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import com.xassure.reporting.logger.LogStatus;
import com.xassure.reporting.logger.Reporting;
import com.xassure.reporting.utilities.PropertiesFileHandler;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;

@SuppressWarnings("rawtypes")
public class MobileControlsLibrary implements Controls {
	AppiumDriver<MobileElement> driver;

	private static String errorMsg = null;
	String timeLocatorJson = null;
	JSONObject jsonObj;
	JSONArray jsonArray;

	private boolean locatorTimeFlag = Boolean
			.parseBoolean(new PropertiesFileHandler().readProperty("setupConfig", "captureLocatorTime"));

	Duration pollingDuration = Duration.of(250, ChronoUnit.MILLIS);
	Duration timeOutDuration = Duration.of(2, ChronoUnit.SECONDS);

	@Inject
	public MobileControlsLibrary(AppiumDriver<MobileElement> driver) {
		this.driver = driver;
		Reporting.setDriver(driver);
	}

	@Override
	public WebDriver getDriver() {
		return driver;
	}

	@Override
	public void launchApplication(String url) {
		driver.get(url);
		String currentUrl = driver.getCurrentUrl();
		if (currentUrl.toLowerCase().contains(url.toLowerCase())) {
			Reporting.getLogger().log(LogStatus.PASS, "URL '" + url + "' is successfuly launched");
		} else if (currentUrl.isEmpty() && url.isEmpty() == false) {
			Reporting.getLogger().log(LogStatus.PASS, "Failed to launch URL '" + url + "'");
		}

	}

	@Override
	public boolean click(String elementName, String property) {
		boolean bFlag = false;
		List<WebElement> elements = getWebElementList(property, elementName);
		try {
			for (WebElement element : elements) {
				WebElement ele = verifyElementClickable(element);
				if (ele != null) {
					highlighElement(ele);
					ele.click();
					bFlag = true;
					break;
				}
			}
			if (!bFlag) {
				Reporting.getLogger().log(LogStatus.FAIL, "Failed to click on Element '" + elementName + "'.");
			} else {
				Reporting.getLogger().log(LogStatus.PASS, "Click on Element '" + elementName + "'.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Reporting.getLogger().log(LogStatus.FAIL,
					"Exception occured while clicking on element '" + elementName + "'", e);
		}
		return bFlag;
	}

	@Override
	public boolean clickUsingActionBuilder(String elementName, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean clickUsingJavaScriptExecutor(String elementName, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void enterText(String elementName, String property, String textToEnter) {
		boolean bFlag = false;
		try {
			List<WebElement> elements = getWebElementList(property, elementName);
			for (WebElement element : elements) {

				if (visibilityofWebelement(element)) {
					element.clear();
					highlighElement(element);
					element.sendKeys(textToEnter);
					bFlag = true;
					break;
				}
			}
			if (!bFlag) {
				Reporting.getLogger().log(LogStatus.FAIL,
						"Text '" + textToEnter + "' not entered successfuly into '" + elementName);

			} else {
				Reporting.getLogger().log(LogStatus.PASS,
						"Text '" + textToEnter + "' entered successfuly into '" + elementName);

			}

		} catch (TimeoutException t) {
			Reporting.getLogger().log(LogStatus.FAIL,
					"Timeout Exception occured while entering text : '" + textToEnter + "' into '" + elementName);

		} catch (Exception e) {
			Reporting.getLogger().log(LogStatus.FAIL,
					"Exception occured while entering text : '" + textToEnter + "' into '" + elementName);
		}

	}

	@Override
	public void enterTextUsingJavascriptExecutor(String elementName, String property, String textToEnter) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean clearTextBox(String elementName, String porperty) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTextBoxEditable(String elementName, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JavascriptExecutor getJavaScriptExecutor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void executeJavaScript(String javaScript) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectDropdownWithIndex(String elementNaem, String property, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean selectDropdownWithVisibleText(String elementName, String property, String visibleText) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getDropdownSelectedVisibleText(String elementName, String property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getDropdownOptions(String elementName, String property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean doubleClick(String elementName, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rightClick(String elementName, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pressEnter(String elementName, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getText(String elementName, String property) {
		boolean bFlag = false;
		String text = null;
		try {
			List<WebElement> elements = getWebElementList(property, elementName);

			for (WebElement element : elements) {

				text = element.getText();
				if (text != null) {
					bFlag = true;

					break;
				}
			}
			if (!bFlag) {
				Reporting.getLogger().log(LogStatus.FAIL, "Unable to get text from element " + elementName);

			}
		} catch (TimeoutException t) {
			Reporting.getLogger().log(LogStatus.FAIL, "Timeout Exception in fetching text from element " + elementName,
					(Exception) t);
		} catch (Exception e) {
			Reporting.getLogger().log(LogStatus.FAIL, "Exception in fetching text from element " + elementName, e);
		}

		return text;
	}

	@Override
	public String getTextFromTextboxOrDropbox(String elementName, String property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValueOfAttribute(String elementName, String property, String attribute) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void quitDriver() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isTextPresent(String elementName, String property, String text) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<WebElement> getWebElementsList(String elementName, String property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getTextFromWebElementList(String elementName, String property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPageTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isElementExists(String elementName, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean switchAndAcceptAlert() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean switchAndCancelAlert() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String switchAndGetTextFromAlert() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean waitUntilElementIsVisible(String elementName, String property, long timeOutInSeconds) {
		boolean bFlag = false;
		try {
			List<WebElement> elements = getWebElementList(elementName, property, timeOutInSeconds);

			for (WebElement element : elements) {
				highlighElement(element);
				Thread.sleep(300);
				if (existenceofWebelement(element, timeOutInSeconds)) {

					bFlag = true;

					break;
				}
			}
		} catch (TimeoutException t) {
			Reporting.getLogger().log(LogStatus.FAIL, "Timeout Exception while locating '" + elementName,
					(Exception) t);
		} catch (Exception e) {
			Reporting.getLogger().log(LogStatus.FAIL, "Exception while locating '" + elementName, e);
		}
		return bFlag;
	}

	@Override
	public boolean waitUntilElementIsInVisible(String elementName, String property) {
		boolean bFlag = false;
		try {
			if (verifyElementInvisibility(property, this.timeOutDuration.getSeconds())) {

				bFlag = true;
			} else {

				Reporting.getLogger().log(LogStatus.FAIL,
						"Element '" + elementName + "' did not get invisible in specified time");
			}

		} catch (TimeoutException t) {
			Reporting.getLogger().log(LogStatus.FAIL, "Timeout Exception while locating Element " + elementName,
					(Exception) t);
		} catch (Exception e) {
			Reporting.getLogger().log(LogStatus.FAIL, "Exception while locating Element " + elementName, e);
		}
		return bFlag;
	}

	public boolean waitUntilElementIsVisible(String elementName, String property, boolean toLog) {
		boolean bFlag = false;
		try {
			List<WebElement> elements = getWebElementList(elementName, property, this.timeOutDuration.getSeconds());

			for (WebElement element : elements) {
				highlighElement(element);
				Thread.sleep(300L);
				if (existenceofWebelement(element, this.timeOutDuration.getSeconds())) {

					bFlag = true;

					break;
				}
			}
		} catch (TimeoutException t) {
			if (toLog)
				Reporting.getLogger().log(LogStatus.FAIL, "Timeout Exception while locating '" + elementName,
						(Exception) t);
		} catch (Exception e) {
			if (toLog)
				Reporting.getLogger().log(LogStatus.FAIL, "Exception while locating '" + elementName, e);
		}
		return bFlag;
	}

	@Override

	public boolean waitUntilElementIsVisible(String elementName, String property) {
		boolean bFlag = false;
		try {
			List<WebElement> elements = getWebElementList(elementName, property, this.timeOutDuration.getSeconds());

			for (WebElement element : elements) {
				highlighElement(element);
				Thread.sleep(300L);
				if (existenceofWebelement(element, this.timeOutDuration.getSeconds())) {

					bFlag = true;

					break;
				}
			}
		} catch (TimeoutException t) {
			Reporting.getLogger().log(LogStatus.FAIL, "Timeout Exception while locating '" + elementName,
					(Exception) t);
		} catch (Exception e) {
			Reporting.getLogger().log(LogStatus.FAIL, "Exception while locating '" + elementName, e);
		}
		return bFlag;
	}

	@Override
	public boolean waitUntilElementIsInVisible(String elementName, String property, long timeOut) {
		boolean bFlag = false;
		try {
			if (verifyElementInvisibility(property, timeOut)) {

				bFlag = true;
			} else {

				Reporting.getLogger().log(LogStatus.FAIL,
						"Element '" + elementName + "' did not get invisible in specified time");
			}

		} catch (TimeoutException t) {
			Reporting.getLogger().log(LogStatus.FAIL, "Timeout Exception while locating Element " + elementName,
					(Exception) t);
		} catch (Exception e) {
			Reporting.getLogger().log(LogStatus.FAIL, "Exception while locating Element " + elementName, e);
		}
		return bFlag;
	}

	@Override
	public boolean waitUntilElementToBeClickable(String elementName, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean areElementsPresent(String elementName, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCurrentWindowHandle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getAllWindowHandles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean switchToWindow(String previousWindowHandel) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void switchToNewWindow(int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeWindow(int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPreviousPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseHover(String elementName, String property) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean mouseHoverClickChild(String parentproperty, String childproperty) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPageSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllCookies() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCurrentUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void switchToIframe() {
		// TODO Auto-generated method stub

	}

	@Override
	public void switchToIframeById(String iFrameId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void switchToIframeByIndex(int frameIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean switchToIframe(String elementName, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deselectIframe() {
		// TODO Auto-generated method stub

	}

	@Override
	public void waitFor(int enterMiliSeconds) {
		// TODO Auto-generated method stub

	}

	@Override
	public void waitForPageLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeCurrentWindow() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean verifyTitle(String title) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCssValue(String elementName, String property, String cssAttribute) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExceptionDetails(Exception e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String setPageLoad(String pageName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void highlighElement(WebElement element) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * verify whether the element is clickable:-
	 */
	private WebElement verifyElementClickable(WebElement element) {

		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), 40);
			return wait.until(ExpectedConditions.elementToBeClickable(element));
		} catch (Exception e) {

			throw e;
			// Reporting.getLogger().log(LogType.FAIL, "Exception occured while
			// verifying element is clickable", e,
			// getDriver());
		}

	}

	private boolean visibilityofWebelement(WebElement element) {

		try {
			FluentWait<WebElement> _waitForElement = new FluentWait<WebElement>(element);
			_waitForElement.pollingEvery(pollingDuration);
			_waitForElement.withTimeout(timeOutDuration);
			_waitForElement.ignoring(NoSuchElementException.class);
			_waitForElement.ignoring(StaleElementReferenceException.class);
			_waitForElement.ignoring(ElementNotVisibleException.class);

			Function<WebElement, Boolean> elementVisibility = new Function<WebElement, Boolean>() {

				public Boolean apply(WebElement element) {
					// TODO Auto-generated method stub

					return element.isDisplayed();
				}

			};

			return _waitForElement.until(elementVisibility);

		} catch (Exception e) {
			Reporting.getLogger().log(LogStatus.FAIL, "Exception occured while verifying the visibility of an element ",
					e);

		}

		return false;
	}

	@SuppressWarnings("unchecked")
	private WebElement findElement(String locator, String locatorValue, FluentWait<WebDriver> driverWait) {

		WebElement element = null;
		final String locVal = locatorValue;

		try {
			Stopwatch stopwatch = null;
			if (locator.equalsIgnoreCase("id")) {
				if (locatorTimeFlag) {
					stopwatch = Stopwatch.createStarted();
				}
				try {
					Function<WebDriver, WebElement> waitForElement = new Function<WebDriver, WebElement>() {
						public WebElement apply(WebDriver driverWait) {
							return getDriver().findElement(By.id(locVal));
						}

					};
					element = driverWait.until(waitForElement);
					if (locatorTimeFlag) {
						stopwatch.stop();
						jsonObj.put("Locator", "id");
						jsonObj.put("isWorking", "Yes");
						jsonObj.put("time", stopwatch.elapsed(TimeUnit.MILLISECONDS));
					}
				} catch (Exception e) {
					if (locatorTimeFlag) {
						stopwatch.stop();
						jsonObj.put("Locator", "id");
						jsonObj.put("isWorking", "No");
						jsonObj.put("time", stopwatch.elapsed(TimeUnit.MICROSECONDS));
					}
					throw e;
				}
			}
			if (locator.equalsIgnoreCase("xpath")) {
				if (locatorTimeFlag) {
					stopwatch = Stopwatch.createStarted();
				}
				try {
					Function<WebDriver, WebElement> waitForElement = new Function<WebDriver, WebElement>() {

						public WebElement apply(WebDriver driverWait) {
							// TODO Auto-generated method stub
							return getDriver().findElement(By.xpath(locVal));
						}

					};
					element = driverWait.until(waitForElement);
					if (locatorTimeFlag) {
						stopwatch.stop();
						jsonObj.put("Locator", "xpath");
						jsonObj.put("isWorking", "Yes");
						jsonObj.put("time", stopwatch.elapsed(TimeUnit.MICROSECONDS));
					}
				} catch (

				Exception e) {
					if (locatorTimeFlag) {
						stopwatch.stop();
						jsonObj.put("Locator", "xpath");
						jsonObj.put("isWorking", "No");
						jsonObj.put("time", stopwatch.elapsed(TimeUnit.MICROSECONDS));
					}
					throw e;
				}
			}
			if (locator.equalsIgnoreCase("className")) {
				if (locatorTimeFlag) {
					stopwatch = Stopwatch.createStarted();
				}
				try {
					Function<WebDriver, WebElement> waitForElement = new Function<WebDriver, WebElement>() {
						public WebElement apply(WebDriver driverWait) {
							// TODO Auto-generated method stub
							return getDriver().findElement(By.className(locVal));
						}

					};
					element = driverWait.until(waitForElement);
					if (locatorTimeFlag) {
						stopwatch.stop();
						jsonObj.put("Locator", "className");
						jsonObj.put("isWorking", "Yes");
						jsonObj.put("time", stopwatch.elapsed(TimeUnit.MICROSECONDS));
					}
				} catch (Exception e) {
					if (locatorTimeFlag) {
						stopwatch.stop();
						jsonObj.put("Locator", "className");
						jsonObj.put("isWorking", "No");
						jsonObj.put("time", stopwatch.elapsed(TimeUnit.MICROSECONDS));
					}
					throw e;
				}
			}
			if (locator.equalsIgnoreCase("name")) {
				if (locatorTimeFlag) {
					stopwatch = Stopwatch.createStarted();
				}
				try {
					Function<WebDriver, WebElement> waitForElement = new Function<WebDriver, WebElement>() {
						public WebElement apply(WebDriver driverWait) {
							// TODO Auto-generated method stub
							return getDriver().findElement(By.name(locVal));
						}

					};
					element = driverWait.until(waitForElement);
					if (locatorTimeFlag) {
						stopwatch.stop();
						jsonObj.put("Locator", "name");
						jsonObj.put("isWorking", "Yes");
						jsonObj.put("time", stopwatch.elapsed(TimeUnit.MICROSECONDS));
					}
				} catch (Exception e) {
					if (locatorTimeFlag) {
						stopwatch.stop();
						jsonObj.put("Locator", "name");
						jsonObj.put("isWorking", "No");
						jsonObj.put("time", stopwatch.elapsed(TimeUnit.MICROSECONDS));
					}
					throw e;
				}
			}
			if (locator.equalsIgnoreCase("tagName")) {
				if (locatorTimeFlag) {
					stopwatch = Stopwatch.createStarted();
				}
				try {
					Function<WebDriver, WebElement> waitForElement = new Function<WebDriver, WebElement>() {
						public WebElement apply(WebDriver driverWait) {
							// TODO Auto-generated method stub
							return getDriver().findElement(By.tagName(locVal));
						}

					};
					element = driverWait.until(waitForElement);
					if (locatorTimeFlag) {
						stopwatch.stop();
						jsonObj.put("Locator", "tagName");
						jsonObj.put("isWorking", "Yes");
						jsonObj.put("time", stopwatch.elapsed(TimeUnit.MICROSECONDS));
					}
				} catch (Exception e) {
					if (locatorTimeFlag) {
						stopwatch.stop();
						jsonObj.put("Locator", "tagName");
						jsonObj.put("isWorking", "No");
						jsonObj.put("time", stopwatch.elapsed(TimeUnit.MICROSECONDS));
					}
					throw e;
				}
			}
			if (locator.equalsIgnoreCase("css")) {
				if (locatorTimeFlag) {
					stopwatch = Stopwatch.createStarted();
				}
				try {
					Function<WebDriver, WebElement> waitForElement = new Function<WebDriver, WebElement>() {
						public WebElement apply(WebDriver driverWait) {
							// TODO Auto-generated method stub
							return getDriver().findElement(By.cssSelector(locVal));
						}

					};
					element = driverWait.until(waitForElement);
					if (locatorTimeFlag) {
						stopwatch.stop();
						jsonObj.put("Locator", "css");
						jsonObj.put("isWorking", "Yes");
						jsonObj.put("time", stopwatch.elapsed(TimeUnit.MICROSECONDS));
					}
				} catch (Exception e) {
					if (locatorTimeFlag) {
						stopwatch.stop();
						jsonObj.put("Locator", "css");
						jsonObj.put("isWorking", "No");
						jsonObj.put("time", stopwatch.elapsed(TimeUnit.MICROSECONDS));
					}
					throw e;
				}
			}

			if (locator.equalsIgnoreCase("linkText")) {
				if (locatorTimeFlag) {
					stopwatch = Stopwatch.createStarted();
				}
				try {
					Function<WebDriver, WebElement> waitForElement = new Function<WebDriver, WebElement>() {
						public WebElement apply(WebDriver driverWait) {
							// TODO Auto-generated method stub
							return getDriver().findElement(By.linkText(locVal));
						}

					};
					element = driverWait.until(waitForElement);
					if (locatorTimeFlag) {
						stopwatch.stop();
						jsonObj.put("Locator", "linkText");
						jsonObj.put("isWorking", "Yes");
						jsonObj.put("time", stopwatch.elapsed(TimeUnit.MICROSECONDS));
					}
				} catch (Exception e) {
					if (locatorTimeFlag) {
						stopwatch.stop();
						jsonObj.put("Locator", "linkText");
						jsonObj.put("isWorking", "No");
						jsonObj.put("time", stopwatch.elapsed(TimeUnit.MICROSECONDS));
					}
					throw e;
				}
			}
			jsonObj.put("value", locVal);
			return element;

		} catch (Exception e) {
			jsonObj.put("value", locVal);
			Reporting.getLogger().log(LogStatus.INFO, " Locator " + locator + ":" + locatorValue + " not found :", e);
			throw e;

		}

		// return null;
	}

	/**
	 * return webelement of locator:-
	 */
	private WebElement getWebelement(String locator, String locatorValue) {

		try {
			FluentWait<WebDriver> driverWait = new FluentWait<WebDriver>(getDriver());
			driverWait.pollingEvery(pollingDuration);
			driverWait.withTimeout(timeOutDuration);
			driverWait.ignoring(NoSuchElementException.class);
			driverWait.ignoring(StaleElementReferenceException.class);
			driverWait.ignoring(ElementNotVisibleException.class);

			return findElement(locator, locatorValue, driverWait);

		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}

	}

	private WebElement getWebelement(String locator, String locatorValue, long timeOutInSeconds) {

		FluentWait<WebDriver> driverWait = new FluentWait<WebDriver>(getDriver());
		driverWait.pollingEvery(pollingDuration);
		Duration timeout = Duration.of(timeOutInSeconds, ChronoUnit.SECONDS);
		driverWait.withTimeout(timeout);
		driverWait.ignoring(NoSuchElementException.class);
		driverWait.ignoring(StaleElementReferenceException.class);
		driverWait.ignoring(ElementNotVisibleException.class);

		return findElement(locator, locatorValue, driverWait);
	}

	@SuppressWarnings("unchecked")
	private List<WebElement> getWebElementList(String property, String elementName) {
		errorMsg = null;
		boolean bFlag = false;
		boolean locatorFlag = false;

		jsonArray = new JSONArray();
		Map<String, List<String>> locatorsMapValues = getLocatorsMap(property);
		List<WebElement> webElements = new ArrayList<>();
		try {
			for (String locator : locatorsMapValues.keySet()) {

				if (!locatorTimeFlag) {
					if (bFlag)
						break;
				}
				List<String> locatorVal = locatorsMapValues.get(locator);
				for (String locValue : locatorVal) {
					jsonObj = new JSONObject();
					errorMsg = errorMsg + " Locator Type : " + locator + " and Locator Value : " + locValue;
					WebElement element = getWebelement(locator, locValue);
					if (element != null) {
						webElements.add(element);
						bFlag = true;
						if (locatorTimeFlag) {
							if (!locatorFlag) {
								jsonObj.put("isUsed", "Yes");
								locatorFlag = true;
							} else {
								jsonObj.put("isUsed", "No");
							}
						}
					} else {
						if (locatorTimeFlag) {
							jsonObj.put("isUsed", "No");
						}
					}
					jsonArray.add(jsonObj);
				}
				errorMsg = errorMsg + " not found ";

			}
			if (locatorTimeFlag) {
				Reporting.getLogger().log(jsonArray.toString(), elementName);
			}
			return webElements;
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	private List<WebElement> getWebElementList(String elementName, String properties, long timeOutInSeconds) {
		jsonArray = new JSONArray();
		boolean locatorFlag = false;
		// TODO Auto-generated method stub
		// Map<String, String> descriptionMap = getDescription(elementName,
		// stepDescription);
		try {
			Map<String, List<String>> locatorsMapValues = getLocatorsMap(properties);

			List<WebElement> webElements = new ArrayList<WebElement>();
			for (String locator : locatorsMapValues.keySet()) {

				List<String> locatorVal = locatorsMapValues.get(locator);
				for (String locValue : locatorVal) {
					jsonObj = new JSONObject();
					errorMsg = errorMsg + "Locator Type : " + locator + " and locator Value : " + locValue;
					WebElement element = getWebelement(locator, locValue, timeOutInSeconds);
					if (element != null) {
						webElements.add(element);
						if (locatorTimeFlag) {
							if (!locatorFlag) {
								jsonObj.put("isUsed", "Yes");
								locatorFlag = true;
							} else {
								jsonObj.put("isUsed", "No");
							}
						}
					} else {
						if (locatorTimeFlag) {
							jsonObj.put("isUsed", "No");
						}
					}
					jsonArray.add(jsonObj);
				}
				errorMsg = errorMsg + " not found ";

			}
			if (locatorTimeFlag) {
				Reporting.getLogger().log(jsonArray.toString(), elementName);
			}
			return webElements;

		} catch (TimeoutException t) {
			return null;
		} catch (Exception e) {
			throw e;

		}

	}

	private Map<String, List<String>> getLocatorsMap(String properties) {
		Map<String, List<String>> locatorsMapValues = new HashMap<String, List<String>>();
		try {

			// Split the different locators based upon Pipe symbols:-
			String[] locatorValues = properties.split("\\|");

			for (String locator : locatorValues) {

				String locatorName = locator.split("~")[0].trim();
				String locatorValue = locator.split("~")[1].trim();
				if (locatorsMapValues.containsKey(locatorName)) {
					List<String> locatorNameProperties = locatorsMapValues.get(locatorName);
					locatorNameProperties.add(locatorValue);
				} else {
					List<String> locatorVal = new ArrayList<>();
					locatorVal.add(locatorValue);
					locatorsMapValues.put(locatorName, locatorVal);
				}

			}

		} catch (Exception e) {
			System.out.println(e);
			Reporting.getLogger().log(LogStatus.FAIL, "Exception occured while getting locators from properties", e);
		}

		return locatorsMapValues;
	}

	private List<WebElement> getWebElementsOfElement(String locator, String locatorValue) {

		List<WebElement> elements = null;
		final String locVal = locatorValue;
		FluentWait<WebDriver> driverWait = new FluentWait<WebDriver>(getDriver());
		driverWait.pollingEvery(pollingDuration);
		driverWait.withTimeout(timeOutDuration);
		driverWait.ignoring(NoSuchElementException.class);
		driverWait.ignoring(StaleElementReferenceException.class);

		try {
			if (locator.equalsIgnoreCase("id")) {

				Function<WebDriver, List<WebElement>> waitForElement = new Function<WebDriver, List<WebElement>>() {
					public List<WebElement> apply(WebDriver driverWait) {
						// TODO Auto-generated method stub
						return getDriver().findElements(By.id(locVal));
					}

				};
				elements = driverWait.until(waitForElement);
			}
			if (locator.equalsIgnoreCase("xpath")) {

				Function<WebDriver, List<WebElement>> waitForElement = new Function<WebDriver, List<WebElement>>() {

					public List<WebElement> apply(WebDriver driverWait) {
						// TODO Auto-generated method stub
						return getDriver().findElements(By.xpath(locVal));
					}

				};
				elements = driverWait.until(waitForElement);

			}
			if (locator.equalsIgnoreCase("className")) {

				Function<WebDriver, List<WebElement>> waitForElement = new Function<WebDriver, List<WebElement>>() {
					public List<WebElement> apply(WebDriver driverWait) {
						// TODO Auto-generated method stub
						return getDriver().findElements(By.className(locVal));
					}

				};
				elements = driverWait.until(waitForElement);

			}
			if (locator.equalsIgnoreCase("name")) {
				Function<WebDriver, List<WebElement>> waitForElement = new Function<WebDriver, List<WebElement>>() {
					public List<WebElement> apply(WebDriver driverWait) {
						// TODO Auto-generated method stub
						return getDriver().findElements(By.name(locVal));
					}

				};
				elements = driverWait.until(waitForElement);
			}
			if (locator.equalsIgnoreCase("tagName")) {

				Function<WebDriver, List<WebElement>> waitForElement = new Function<WebDriver, List<WebElement>>() {
					public List<WebElement> apply(WebDriver driverWait) {
						// TODO Auto-generated method stub
						return getDriver().findElements(By.tagName(locVal));
					}

				};
				elements = driverWait.until(waitForElement);

			}
			if (locator.equalsIgnoreCase("css")) {

				Function<WebDriver, List<WebElement>> waitForElement = new Function<WebDriver, List<WebElement>>() {
					public List<WebElement> apply(WebDriver driverWait) {
						// TODO Auto-generated method stub
						return getDriver().findElements(By.cssSelector(locVal));
					}

				};
				elements = driverWait.until(waitForElement);

			}

			if (locator.equalsIgnoreCase("linkText")) {

				Function<WebDriver, List<WebElement>> waitForElement = new Function<WebDriver, List<WebElement>>() {
					public List<WebElement> apply(WebDriver driverWait) {
						// TODO Auto-generated method stub
						return getDriver().findElements(By.linkText(locVal));
					}

				};
				elements = driverWait.until(waitForElement);

			}

			return elements;

		} catch (Exception e) {

			Reporting.getLogger().log(LogStatus.INFO, " Locator " + locator + ":" + locatorValue + " not found :");
		}

		return null;
	}

	@Override
	public boolean switchContext(String context) {
		boolean bFlag = false;
		try {
			for (String contexts : driver.getContextHandles()) {
				if (contexts.contains(context)) {
					driver.context(contexts);
					bFlag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Reporting.getLogger().log(LogStatus.FAIL, "Exception occured while switching context to " + context);
		}
		return bFlag;
	}

	@Override
	public boolean tap(String elementName, String property) {
		boolean bFlag = false;
		List<WebElement> elements = getWebElementList(property, elementName);
		try {
			for (WebElement element : elements) {
				WebElement ele = verifyElementClickable(element);
				if (ele != null) {

					TouchAction action = new TouchAction(driver);
					action.tap(new TapOptions().withElement(new ElementOption().withElement(ele))).perform();
					bFlag = true;
					break;

				}
			}
			if (!bFlag) {
				Reporting.getLogger().log(LogStatus.FAIL, "Failed to tap on Element '" + elementName + "'.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Reporting.getLogger().log(LogStatus.FAIL,
					"Exception occured while performing tap action on element '" + elementName + "'", e);
		}
		return bFlag;
	}

	@Override
	public void tap(int xAxis, int yAxis) {
		try {
			TouchAction action = new TouchAction(driver);
			action.tap(PointOption.point(xAxis, yAxis)).perform();

		} catch (Exception e) {
			e.printStackTrace();
			Reporting.getLogger().log(LogStatus.FAIL, "Exception occurred while performing tap on cordinates " + xAxis
					+ " on x axis and " + yAxis + " on y axis", e);
		}

	}

	@Override
	public boolean doubleTap(String elementName, String property) {
		boolean bFlag = false;
		List<WebElement> elements = getWebElementList(property, elementName);
		try {
			for (WebElement element : elements) {
				WebElement ele = verifyElementClickable(element);
				if (ele != null) {

					TouchAction action = new TouchAction(driver);
					action.tap(new TapOptions().withTapsCount(2).withElement(new ElementOption().withElement(ele)))
							.perform();

					bFlag = true;
					break;

				}
			}
			if (!bFlag) {
				Reporting.getLogger().log(LogStatus.FAIL, "Failed to tap on Element '" + elementName + "'.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Reporting.getLogger().log(LogStatus.FAIL,
					"Exception occured while performing tap action on element '" + elementName + "'", e);
		}
		return bFlag;
	}

	@Override
	public void doubleTap(int xAxis, int yAxis) {
		try {
			TouchAction action = new TouchAction(driver);
			action.tap(new TapOptions().withTapsCount(2).withPosition(PointOption.point(xAxis, yAxis))).perform();
		} catch (Exception e) {
			e.printStackTrace();
			Reporting.getLogger().log(LogStatus.FAIL, "Exception occurred while performing tap on cordinates " + xAxis
					+ " on x axis and " + yAxis + " on y axis", e);
		}
	}

	@Override
	public boolean scroll(String fromElement, String toElement) {
		return false;
	}

	@Override
	public void scroll(int fromXCordinate, int fromYCordinate, int toXCordinate, int toYCordinate) {
		try {
			TouchAction action = new TouchAction(driver);
			action.longPress(PointOption.point(fromXCordinate, fromYCordinate))
					.moveTo(PointOption.point(toXCordinate, toYCordinate)).release().perform();
		} catch (Exception e) {
			e.printStackTrace();
			Reporting.getLogger().log(LogStatus.FAIL, "Exception occurred while performing scroll from ("
					+ fromXCordinate + "," + fromYCordinate + ") to " + " (" + toXCordinate + "," + toYCordinate + ")",
					e);
		}
	}

	@Override
	public boolean zoom(String elementName, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean performMultiTouch(List<TouchAction> actionList) {
		try {

			MultiTouchAction actions = new MultiTouchAction(driver);
			for (TouchAction action : actionList) {
				actions.add(action);
			}
			actions.perform();
		} catch (Exception e) {
			e.printStackTrace();
			Reporting.getLogger().log(LogStatus.FAIL, "Exception occurred while performing multi actions", e);
		}
		return false;
	}

	private boolean existenceofWebelement(WebElement element, long timeOutInSeconds) {
		try {
			FluentWait<WebElement> _waitForElement = new FluentWait<WebElement>(element);
			_waitForElement.pollingEvery(this.pollingDuration);
			Duration timeout = Duration.of(timeOutInSeconds, ChronoUnit.SECONDS);
			_waitForElement.withTimeout(timeout);
			_waitForElement.ignoring(NoSuchElementException.class);
			_waitForElement.ignoring(StaleElementReferenceException.class);
			_waitForElement.ignoring(ElementNotVisibleException.class);

			Function<WebElement, Boolean> elementVisibility = new Function<WebElement, Boolean>() {

				public Boolean apply(WebElement element) {
					return Boolean.valueOf(element.isEnabled());
				}
			};

			WebDriverWait wait = new WebDriverWait(getDriver(), 10L);
			wait.until(ExpectedConditions.visibilityOf(element));

			return ((Boolean) _waitForElement.until(elementVisibility)).booleanValue();
		} catch (Exception exception) {

			return false;
		}
	}

	private boolean verifyElementInvisibility(String properties, long timeOutInSeconds) {
		boolean elem = false;

		try {
			Map<String, String> locatorsMap = new HashMap<>();

			String[] locatorValues = properties.split("\\|");

			for (String locator : locatorValues) {
				locatorsMap.put(locator.split("~")[0].trim(), locator.split("~")[1].trim());
			}

			for (String locator : locatorsMap.keySet()) {
				WebDriverWait wait = new WebDriverWait(getDriver(), timeOutInSeconds);

				if (locator.equalsIgnoreCase("xpath")) {

					elem = ((Boolean) wait
							.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locatorsMap.get(locator)))))
									.booleanValue();
					continue;
				}
				if (locator.equalsIgnoreCase("classname")) {
					elem = ((Boolean) wait.until(
							ExpectedConditions.invisibilityOfElementLocated(By.className(locatorsMap.get(locator)))))
									.booleanValue();
					continue;
				}
				if (locator.equalsIgnoreCase("name")) {

					elem = ((Boolean) wait
							.until(ExpectedConditions.invisibilityOfElementLocated(By.name(locatorsMap.get(locator)))))
									.booleanValue();
					continue;
				}
				if (locator.equalsIgnoreCase("id")) {
					elem = ((Boolean) wait
							.until(ExpectedConditions.invisibilityOfElementLocated(By.id(locatorsMap.get(locator)))))
									.booleanValue();
					continue;
				}
				if (locator.equalsIgnoreCase("css")) {
					elem = ((Boolean) wait.until(
							ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(locatorsMap.get(locator)))))
									.booleanValue();
					continue;
				}
				if (locator.equalsIgnoreCase("tagname")) {
					elem = ((Boolean) wait.until(
							ExpectedConditions.invisibilityOfElementLocated(By.tagName(locatorsMap.get(locator)))))
									.booleanValue();
					continue;
				}
				if (locator.equalsIgnoreCase("linkText")) {
					elem = ((Boolean) wait.until(
							ExpectedConditions.invisibilityOfElementLocated(By.linkText(locatorsMap.get(locator)))))
									.booleanValue();
				}
			}

		} catch (Exception exception) {
		}

		return elem;
	}
}
