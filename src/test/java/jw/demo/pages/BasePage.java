package jw.demo.pages;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jw.demo.constants.Constants;
import jw.demo.managers.DriverManager;
import jw.demo.managers.FileReaderManager;
import jw.demo.utils.LogException;
import jw.demo.utils.TestContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SuppressWarnings({"Convert2Lambda", "CallToPrintStackTrace", "JavadocReference"})
public class BasePage {

    private final static Logger LOG = LogManager.getLogger(BasePage.class);
    protected POM pom = new POM();

    protected static final String BASE_URL = FileReaderManager.getInstance().getConfigReader().getBaseUrl();
    protected static final String ENV_PASS = FileReaderManager.getInstance().getConfigReader().getEnvPasswd();
    protected static final String BAD_PAGE_SOURCE = "<html><head></head><body></body></html>";
    protected static final int USD_DECIMAL_SCALE = 2;
    protected static final int NO_INIT_POLL_DELAY = 0;
    protected static final int TIME_OUT_QUICK = 1;
    protected static final int TIME_OUT_SHORT = 2;
    protected static final int TIME_OUT_QUICK_PAGELOAD = 3;
    protected static final int TIME_OUT_FOR_SHORT_PAGELOAD = 5;
    protected static final int TIME_OUT_DD_SECONDS = 6;
    protected static final int TIME_OUT_SECONDS = 10;
    protected static final int TIME_OUT_SECONDS_UPLOAD = 10;
    protected static final int TIME_OUT_SECONDS_DOWNLOAD = 40;
    protected static final int TIMEOUT_FOR_REOPEN = 60;
    protected static final int TIMEOUT_COMPLETE_TASK = 90;
    protected static final int TIMEOUT_UPLOAD_TASK = 150;
    protected static final int POLL_COMPLETE_TASK = 20;
    protected static final int TIME_OUT_FOR_PAGE_LOAD = 30;
    protected static final int TIME_OUT_AWAIT_SECONDS = 20;
    protected static final int POLL_AWAIT_SECONDS = 2;
    protected static final int TIMEOUT_AWAIT = 30;
    protected static final int POLL_AWAIT = 5;
    protected static final int TIME_OUT_SECONDS_MAX_APP_ERROR = 10;
    protected static final int POLL_INTERVAL_SECONDS = 3;
    protected static final int TIME_OUT_MIN = 2500;
    protected static final int TIME_OUT_FOR_EXTRA = 15;
    protected static final int TIME_OUT_SECONDS_WAIT = 35;
    protected static final int TIMEOUT_GET_TASK = 10;
    protected static final int POLL_GET_TASK = 2;
    protected static final int LONGER_TIME_OUT_SECONDS = 60;
    protected static final int TIME_OUT_3_MIN = 180;
    protected static final int TIME_OUT_5_MIN = 300;
    protected static final int POLL_WAIT_SHORT_MS = 1000;
    protected static final int POLL_WAIT_MS = 2000;
    protected static final int LONG_POLL_WAIT_MS = 5000;
    protected static final int LONGER_POLL_WAIT_MS = 10000;
    protected static final int LONGEST_POLL_WAIT_MS = 20000;
    protected static final int POLL_WAIT_QUICK_MS = 100;
    protected static final int SHORT_INTERVAL = 10;
    protected static final int AWAIT_100_MS = 1;
    protected static final int AWAIT_200_MS = 2;
    protected static final int AWAIT_500_MS = 5;
    protected static final int AWAIT_2_SEC = 20;
    protected static final int AWAIT_5_SEC = 50;
    protected static final int POLL_NO_INIT_DELAY = 0;

    // TODO adjust rounding mode depending on project AC if currency is involved otherwise remove
    protected static final RoundingMode roundingMode = RoundingMode.HALF_UP;

    // TODO locators are required by some methods and need to be defined for methods to function correctly
    // LOCATORS
    protected final By inactiveContinueButton = By.xpath("");
    protected final By errorModalMessage = By.xpath("");
    protected final By loaderIcon = By.xpath("");


    public static WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    public JsonObject getScenarioData() {
        return TestContext.getScenarioCtx().getScenarioData();
    }

    /**
     * Saves the given data to the scenario context.
     *
     * @param data The data to be saved.
     */
    public void saveDataToScenarioContextAndData(String data) {
        getScenarioData().addProperty(Constants.DATA, data);
        LOG.info("Grant id - [{}]", data);
    }

    /**
     * An expectation for checking that a WebElement is present on the DOM of a page
     * and visible. Visibility means that the WebElement is not only displayed but also
     * has a height and width that is greater than 0.
     * <p>
     * A default timeout of 10 seconds is provided and a
     * TimeoutException/ConditionTimeoutException will be thrown once the time out
     * is passed.
     *</p>
     *
     * @param by  locator used to find the WebElement
     */
    public void expectElementToBeVisible(By by) {
        expectElementToBeVisible(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that a WebElement is either invisible or not
     * present on the DOM.
     * <p>
     * A default timeout of 10 seconds is provided and a
     * TimeoutException/ConditionTimeoutException will be thrown once the time out
     * is passed.
     * </p>
     *
     * @param by   locator used to find the WebElement
     */
    public void expectElementToBeInvisible(By by) {
        expectElementToBeInvisible(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking a WebElement is visible and enabled such that you
     * can click it.
     * <p>
     * A default timeout of 10 seconds is provided
     * TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     * </p>
     *
     * @param by   locator used to find the WebElement
     */
    public void expectElementToBeClickable(By by) {
        expectElementToBeClickable(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that a WebElement is present on the DOM of a page
     * and visible. Visibility means that the WebElement is not only displayed but also
     * has a height and width that is greater than 0.
     * <p>
     * A TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     * </p>
     *
     * @param by              locator used to find the WebElement
     * @param timeOut   time limit to wait before throwing an Exception
     */
    public void expectElementToBeVisible(By by, int timeOut) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    /**
     * An expectation for checking that a WebElement is either invisible or not
     * present on the DOM.
     * <p>
     * A TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     * </p>
     *
     * @param by              locator used to find the WebElement
     * @param timeOut   time limit to wait before throwing an Exception
     */
    public void expectElementToBeInvisible(By by, int timeOut) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    /**
     * An expectation for checking a WebElement is visible and enabled such that you
     * can click it.
     * <p>
     * A TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     * </p>
     *
     * @param by              locator used to find the WebElement
     * @param timeOut   time limit to wait before throwing an Exception
     */
    public void expectElementToBeClickable(By by, int timeOut) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.elementToBeClickable(by));
    }

    /**
     * An expectation for checking that a WebElement is present on the DOM of a page
     * and visible. Visibility means that the WebElement is not only displayed but also
     * has a height and width that is greater than 0.
     * <p>
     * A default timeout of 10 seconds is provided
     * TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     * </p>
     *
     * @param element element the WebElement
     */
    public void expectElementToBeVisible(WebElement element) {
        expectElementToBeVisible(element, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that a WebElement is either invisible or not
     * present on the DOM.
     * <p>
     * A default timeout of 10 seconds is provided
     * TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     * </p>
     *
     * @param element element the WebElement
     */
    public void expectElementToBeInvisible(WebElement element) {
        expectElementToBeInvisible(element, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking a WebElement is visible and enabled such that you
     * can click it.
     * <p>
     * A default timeout of 10 seconds is provided
     * TimeoutException/ConditionTimeoutException will be thrown once the time-out passes
     * </p>
     *
     * @param element element the WebElement
     */
    public void expectElementToBeClickable(WebElement element) {
        expectElementToBeClickable(element, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that a WebElement is present on the DOM of a page
     * and visible. Visibility means that the WebElement is not only displayed but also
     * has a height and width that is greater than 0.
     * <p>
     * A TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     * </p>
     *
     * @param element       element the WebElement
     * @param timeOut time limit to wait before throwing an Exception
     */
    public void expectElementToBeVisible(WebElement element, int timeOut) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * An expectation for checking that a WebElement is either invisible or not
     * present on the DOM.
     * <p>
     * A TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     * </p>
     *
     * @param element       element the WebElement
     * @param timeOut time limit to wait before throwing an Exception
     */
    public void expectElementToBeInvisible(WebElement element, int timeOut) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.invisibilityOf(element));
    }

    /**
     * An expectation for checking a WebElement is visible and enabled such that you
     * can click it.
     * <p>
     * A TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     * </p>
     * @param element       - element the WebElement
     * @param timeOut - time limit to wait before throwing an Exception
     */
    public void expectElementToBeClickable(WebElement element, int timeOut) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Verifies if the given web element contains the specified attribute with the expected attribute value.
     * <p>
     * Given time-out of 10 seconds
     * </p>
     *
     * @param element        The web element to be checked.
     * @param attribute      The name of the attribute to be checked.
     * @param attributeValue The expected value of the attribute.
     */
    public void expectElementToContainAttribute(WebElement element, String attribute, String attributeValue) {
        expectElementToContainAttribute(element, attribute, attributeValue, TIME_OUT_SECONDS);
    }

    /**
     * Waits for the specified element to contain the specified attribute with the specified attribute value.
     *
     * @param element        the {@link WebElement} to check
     * @param attribute      the name of the attribute to check
     * @param attributeValue the expected attribute value
     * @param timeOut  the maximum time to wait for the attribute to contain the attribute value, in seconds
     */
    public void expectElementToContainAttribute(WebElement element, String attribute, String attributeValue,
                                                int timeOut) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(POLL_WAIT_MS))
                .until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver driver) {
                        try {
                            expectElementToBeVisible(element, timeOut);
                            return element.getAttribute(attribute).contains(attributeValue);
                        } catch (StaleElementReferenceException e) {
                            return Boolean.FALSE;
                        }
                    }
                });
    }

    /**
     * Clicks the parent div WebElement
     *
     * @param subDivLocator the locator of the sub div element
     * @return true if the parent div element is clicked, false otherwise
     */
    public Boolean parentDivElementIsClicked(By subDivLocator) {
        try {
            WebElement parentDivElement = (WebElement) getJSExecutor().executeScript("return arguments[0].parentNode;",
                    getDriver().findElement(subDivLocator));
            expectElementToContainAttribute(parentDivElement, "class", "checked");
            LOG.info("Parent Div is clicked as expected for [{}]", subDivLocator);
            return Boolean.TRUE;
        } catch (TimeoutException | ConditionTimeoutException | NoSuchElementException e) {
            LOG.error("Parent Div is not clicked as expected for [{}]", subDivLocator);
            return Boolean.FALSE;
        }
    }

    /**
     * Scrolls the element identified by the given locator into view.
     * <p>
     * Given time-out of 10 seconds
     * </p>
     *
     * @param by the locator used to identify the element to scroll into view
     */
    public void scrollIntoView(By by) {
        scrollIntoView(by, TIME_OUT_SECONDS);
    }

    /**
     * Scrolls the web page to bring the element matching the given locator into view.
     *
     * @param by            the locator to find the element to scroll into view
     * @param timeOut the maximum time in seconds to wait for the element to be visible before scrolling
     */
    public void scrollIntoView(By by, int timeOut) {
        expectElementToBeVisible(by, timeOut);
        getJSExecutor().executeScript(Constants.SCROLL_INTO_VIEW, getDriver().findElement(by));
    }

    /**
     * Scrolls the web page until an element containing the specified text is in view.
     * <p>
     * Given time-out of 10 seconds
     * </p>
     *
     * @param element the WebElement to scroll into view
     */
    public void scrollIntoView(WebElement element) {
        scrollIntoView(element, TIME_OUT_SECONDS);
    }

    /**
     * Scrolls the web page until an element containing the specified text is in view.
     *
     * @param element The {@link WebElement} to scroll into view.
     * @param timeOut The timeout in seconds for waiting for the element to be visible.
     */
    public void scrollIntoView(WebElement element, int timeOut) {
        expectElementToBeVisible(element, timeOut);
        getJSExecutor().executeScript(
                "arguments[0].scrollIntoView({behavior: 'auto', block: 'center', inline: 'nearest'});", element);
    }

    /**
     * Scrolls the web page until an element containing the specified text is in view.
     *
     * @param text the text to search for
     */
    public void scrollIntoViewByText(String text) {
        scrollIntoViewByText(text, TIME_OUT_SECONDS);
    }

    /**
     * Scrolls the web page until an element containing the specified text is in view.
     *
     * @param text          the text to be searched for in the web page
     * @param timeOut the maximum time in seconds to wait for the element to become visible
     */
    public void scrollIntoViewByText(String text, int timeOut) {
        List<WebElement> multipleGenericTextLocators = getElements(By.xpath("//*[contains(text(),'" + text + "')]"));
        multipleGenericTextLocators.forEach(currentWebElement -> {
            if (checkIfVisible(currentWebElement)) {
                scrollIntoView(currentWebElement, timeOut);
            }
        });
    }

    /**
     * An expectation for checking that a WebElement is present on the DOM of a page
     * and visible. Visibility means that the WebElement is not only displayed but also
     * has a height and width that is greater than 0.
     * <p>
     * Given time-out period of 10 seconds.
     * </p>
     *
     * @param by   locator used to find the WebElement
     * @return The result is returned or false is returned if 10 seconds pass
     */
    public boolean checkIfVisible(By by) {
        return checkIfVisible(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that a WebElement is present on the DOM of a page
     * and visible. Visibility means that the WebElement is not only displayed but also
     * has a height and width that is greater than 0.
     *
     * @param by              locator used to find the WebElement
     * @param timeOut - time limit to wait before throwing an Exception
     * @return The result is returned or false is returned if the time-out passes
     */
    public boolean checkIfVisible(By by, int timeOut) {
        try {
            expectElementToBeVisible(by, timeOut);
            return Boolean.TRUE;
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }

    /**
     * An expectation for checking that a WebElement is present on the DOM of a page
     * and visible. Visibility means that the WebElement is not only displayed but also
     * has a height and width that is greater than 0.
     * <p>
     * Given time-out period of 10 seconds.
     * </p>
     *
     * @param element   element the WebElement
     * @return The result is returned or false is returned if 10 seconds pass
     */
    public boolean checkIfVisible(WebElement element) {
        return checkIfVisible(element, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that a WebElement is present on the DOM of a page
     * and visible. Visibility means that the WebElement is not only displayed but also
     * has a height and width that is greater than 0.
     *
     * @param element         element the WebElement
     * @param timeOut   time limit to wait before throwing an Exception
     * @return The result is returned or false is returned if the time-out passes
     */
    public boolean checkIfVisible(WebElement element, int timeOut) {
        try {
            expectElementToBeVisible(element, timeOut);
            return Boolean.TRUE;
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }

    /**
     * An expectation for checking that a WebElement is either invisible or not
     * present on the DOM.
     * <p>
     * Given time-out period of 10 seconds.
     * </p>
     *
     * @param by   locator used to find the WebElement
     * @return The result is returned or false is returned if 10 seconds pass
     */
    public boolean checkIfInvisible(By by) {
        return checkIfInvisible(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that a WebElement is either invisible or not
     * present on the DOM.
     *
     * @param by              locator used to find the WebElement
     * @param timeOut   time limit to wait before throwing an Exception
     * @return The result is returned or false is returned if the time-out passes
     */
    public boolean checkIfInvisible(By by, int timeOut) {
        try {
            expectElementToBeInvisible(by, timeOut);
            return Boolean.TRUE;
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }

    /**
     * An expectation for checking that a WebElement is either invisible or not
     * present on the DOM.
     * <p>
     * Given time-out period of 10 seconds
     * </p>
     *
     * @param element element the WebElement
     * @return The result is returned or false is returned if 10 seconds pass
     */
    public boolean checkIfInvisible(WebElement element) {
        return checkIfInvisible(element, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that a WebElement is either invisible or not
     * present on the DOM.
     *
     * @param element         element the WebElement
     * @param timeOut   time limit to wait before throwing an Exception
     * @return The result is returned or false is returned if the time-out passes
     */
    public boolean checkIfInvisible(WebElement element, int timeOut) {
        try {
            expectElementToBeInvisible(element, timeOut);
            return Boolean.TRUE;
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }

    /**
     * An expectation for checking that a WebElement is present on the DOM of a page
     * is visible and is Displayed
     * <p>
     * checkIfVisible returns the result of driver.findElement(by) but is Displayed
     * goes further and checks for getDriver().findElement(by).isDisplayed()
     * </p>
     * <p>
     * Given time-out period of 10 seconds
     * </p>
     *
     * @param by element the WebElement
     * @return The result is returned or false is returned if 10 seconds pass
     */
    public boolean checkIfDisplayed(By by) {
        return checkIfDisplayed(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that a WebElement is present on the DOM of a page
     * is visible and is Displayed
     * <p>
     * checkIfVisible returns the result of driver.findElement(by) but is Displayed
     * goes further and checks for getDriver().findElement(by).isDisplayed()
     *
     * @param by              element the WebElement
     * @param timeOut   time limit to wait before throwing an Exception
     * @return The result is returned or false is returned if the time-out passes
     */
    public boolean checkIfDisplayed(By by, int timeOut) {
        try {
            expectElementToBeVisible(by, timeOut);
            return getDriver().findElement(by).isDisplayed();
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }

    /**
     * Enhanced isEnabled check
     *
     * @param by              element the WebElement
     * @param timeOut   time limit to wait before throwing an Exception
     */
    public boolean checkIfEnabled(By by, int timeOut) {
        try {
            expectElementToBeVisible(by, timeOut);
            return getDriver().findElement(by).isEnabled();
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }

    /**
     * Returns a WebElement using By locator
     * <p>
     * Given time-out period of 10 seconds given
     * </p>
     *
     * @param by The locator used to find the WebElement.
     * @return {@code true} if the WebElement is enabled, {@code false} otherwise.
     */
    public boolean checkIfEnabled(By by) {
        return checkIfEnabled(by, TIME_OUT_SECONDS);
    }

    /**
     * Returns a WebElement using By locator
     * <p>
     * Given time-out period of 10 seconds given
     * </p>
     * 
     * @param by the By locator used to identify the web element
     * @return the WebElement identified by the specified By locator
     */
    public WebElement getElement(By by) {
        return getElement(by, TIME_OUT_SECONDS);
    }

    /**
     * Returns a WebElement using By locator
     *
     * @param by            The locator used to find the WebElement.
     * @param timeOut The maximum time to wait for the WebElement to be visible, in seconds.
     * @return The WebElement found using the specified locator.
     */
    public WebElement getElement(By by, int timeOut) {
        expectElementToBeVisible(by, timeOut);
        LOG.info("Getting Element {}", by);
        return getDriver().findElement(by);
    }

    /**
     * Returns a list of WebElements matching the given locator.
     * <p>
     * Given time-out period of 10 seconds given
     * </p>
     * 
     * @param by The locator strategy to use for finding the WebElements.
     * @return A list of WebElements matching the given locator.
     */
    public List<WebElement> getElements(By by) {
        return getElements(by, TIME_OUT_SECONDS);
    }

    /**
     * Retrieves a list of web elements based on the provided locator.
     *
     * @param by the locator to find the WebElements
     * @param timeOut the maximum time in seconds to wait for the WebElements to be present
     * @return a list of web elements matching the locator
     */
    public List<WebElement> getElements(By by, int timeOut) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.presenceOfElementLocated(by));
        LOG.info("Getting Elements {}", by);
        return getDriver().findElements(by);
    }

    /**
     * Clears the text input identified by the given locator.
     * <p>
     * Given time-out period of 10 seconds given
     * </p>
     * 
     * @param by the locator used to identify the text input
     */
    public void clear(By by) {
        clear(by, TIME_OUT_SECONDS);
    }

    /**
     * Clear a WebElement identified by the given {@link By} locator, after ensuring it is visible.
     *
     * @param by The {@link By} locator used to identify the WebElement.
     * @param timeOut The timeout in seconds to wait for the WebElement to be visible.
     */
    public void clear(By by, int timeOut) {
        expectElementToBeVisible(by, timeOut);
        LOG.info("Clearing Element {}", by);
        getDriver().findElement(by).clear();
    }

    /**
     * Sends the given input to the WebElement identified using given locator
     * Uses the default timeout value.
     * <p>
     * Given time-out period of 10 seconds given
     * </p>
     * 
     * @param by the {@link By} locator to identify the WebElement
     * @param input the input to send to the WebElement
     */
    public void sendKeys(By by, String input) {
        sendKeys(by, input, TIME_OUT_SECONDS);
    }

    /**
     * Sends input to the specified WebElement identified by the given locator.
     *
     * @param by The locator representing the web element.
     * @param input The input string to be sent.
     * @param timeOut The maximum time to wait for the WebElement to become available for interaction
     */
    public void sendKeys(By by, String input, int timeOut) {
        scrollIntoView(by, timeOut);
        LOG.info("Sending Keys to Element {} with values {}", by, input);
        getDriver().findElement(by).sendKeys(input);
    }

    /**
     * Sends the specified input to the given WebElement after clearing the text from WebElement.
     * <p>
     * Uses JS executor to execute JS blur method removing keyboard focus from the WebElement
     * </p>
     * <p>
     * Given time-out period of 10 seconds given
     * </p>
     * 
     * @param by    the locator strategy to locate the web element
     * @param input the text to be sent to the web element
     */
    public void sendKeysBlur(By by, String input) {
        sendKeysBlur(by, input, TIME_OUT_SECONDS);
    }

    /**
     * Sends keys to the WebElement specified by the given locator.
     * <p>
     * Waits for a specified amount of time to wait for WebElement to become available for interaction
     * </p>
     * 
     * @param by            the locator of the WebElement
     * @param input         the string to send to the WebElement
     * @param timeOut the maximum amount of time to wait for the WebElement to become available for interaction
     */
    public void sendKeysBlur(By by, String input, int timeOut) {
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(POLL_WAIT_MS))
                    .until(new ExpectedCondition<Boolean>() {
                        @Override
                        public Boolean apply(WebDriver driver) {
                            LOG.info("Attempting to send keys [{}] to {}", input, by.toString());
                            try {
                                refreshOnErrorMessage();
                                sendKeysBlur(driver.findElement(by), input);
                                return Boolean.TRUE;
                            } catch (TimeoutException e) {
                                LOG.warn("Timed out, could not input. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                            } catch (StaleElementReferenceException e) {
                                LOG.warn("Element stale. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                            } catch (NoSuchElementException e) {
                                LOG.warn("No such element. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                            } catch (ElementClickInterceptedException e) {
                                LOG.warn("Element Click Intercepted. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                                clickIfDisplayed(inactiveContinueButton, TIME_OUT_QUICK);
                            } catch (ElementNotInteractableException e) {
                                LOG.warn("Element Not Interactable. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                                clickIfDisplayed(inactiveContinueButton, TIME_OUT_QUICK);
                            }
                            return Boolean.FALSE;
                        }
                    });
        } catch (TimeoutException e) {
            refreshOnErrorMessage();
            sendKeysBlur(getDriver().findElement(by), input, timeOut);
        }
    }

    /**
     * Sends the specified input to the given WebElement after clearing the text from WebElement.
     * <p>
     * Uses JS executor to execute JS blur method removing keyboard focus from the WebElement
     * </p>
     * @param element The WebElement to which the input should be sent.
     * @param input   The input that should be sent to the WebElement.
     */
    public void sendKeysBlur(WebElement element, String input) {
        sendKeysBlur(element, input, TIME_OUT_SECONDS);
    }

    /**
     * Sends the specified input to the given WebElement after clearing the text from WebElement.
     * <p>
     * Uses JS executor to execute JS blur method removing keyboard focus from the WebElement
     * </p>
     * @param element The WebElement to send the input to.
     * @param input The input to be sent.
     * @param timeOut The maximum time in seconds to wait for the WebElement to be available for interaction
     */
    public void sendKeysBlur(WebElement element, String input, int timeOut) {
        scrollIntoView(element, timeOut);
        element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        element.sendKeys(input);
        getJSExecutor().executeScript(Constants.BLUR_ACTIVE_ELEMENT);
    }

    /**
     * Sends the specified input string to the WebElement and simulates pressing the Enter key.
     * <p>
     * Given time-out period of 10 seconds given
     * </p>
     * @param by     the By object that represents the WebElement to send the input to
     * @param input  the input string to be sent to the WebElement
     */
    public void sendKeysEnter(By by, String input) {
        sendKeysEnter(by, input, TIME_OUT_SECONDS);
    }

    /**
     * Sends the input value to WebElement and presses the Enter key on the specified element.
     *
     * @param by              the locator strategy to find the WebElement
     * @param input           the input value to send
     * @param timeOut   the maximum time in seconds to wait for the WebElement to be present and visible
     */
    public void sendKeysEnter(By by, String input, int timeOut) {
        sendKeysEnter(getDriver().findElement(by), input, timeOut);
    }

    /**
     * Sends the specified input to the given WebElement and presses the Enter key.
     * <p>
     * Given time-out period of 10 seconds given
     * </p>
     *
     * @param element the WebElement to which the input is sent
     * @param input   the input to be sent
     */
    public void sendKeysEnter(WebElement element, String input) {
        sendKeysEnter(element, input, TIME_OUT_SECONDS);
    }

    /**
     * Sends the given input to the specified WebElement and presses the ENTER key.
     * 
     * @param element The WebElement to type into.
     * @param input The input text to be sent.
     * @param timeOut The maximum time to wait for the WebElement to be visible before sending the keys.
     */
    public void sendKeysEnter(WebElement element, String input, int timeOut) {
        scrollIntoView(element, timeOut);
        element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        element.sendKeys(input);
        element.sendKeys(Keys.ENTER);
    }

    /**
     * Checks if one or more WebElements exist based on the given locator.
     *
     * @param by the locator to identify the WebElements
     * @return true when one or more elements exist, otherwise false
     */
    public boolean doElementsExist(By by) {
        return !getDriver().findElements(by).isEmpty();
    }

    /**
     * Checks if the given message is displayed on the current page within the specified time out period.
     * <p>
     * Given time-out period of 10 seconds given
     * </p>
     * 
     * @param message the message to be checked if displayed
     * @return true if the message is displayed, false otherwise
     */
    public boolean isMessageDisplayed(String message) {
        return checkIfDisplayed(By.xpath("//*[contains(.,'" + message + "')]"), TIME_OUT_SECONDS);
    }

    /**
     * Selects an option from a drop-down list based on the visible text.
     * <p>
     * Given time-out period of 10 seconds given
     * </p>
     * @param by   the By object to locate the drop-down element
     * @param text the visible text of the option to select
     */
    public void selectByText(By by, String text) {
        selectByText(by, text, TIME_OUT_SECONDS);
    }


    /**
     * Selects an option from a drop-down list by its visible text before a given time-out period ends.
     *
     * @param by            the locator strategy to identify the drop-down element
     * @param text          the visible text of the option to select
     * @param timeOut the maximum time to wait in seconds for the drop-down WebElement to be available
     */
    public void selectByText(By by, String text, int timeOut) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(POLL_WAIT_MS))
                .until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver driver) {
                        LOG.info("Attempting to Select by Text {} {}", by, text);
                        try {
                            scrollIntoView(by);
                            (new Select(getDriver().findElement(by))).selectByVisibleText(text);
                            return Boolean.TRUE;
                        } catch (TimeoutException e) {
                            LOG.info("Timed out, could not selectByText. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                        } catch (StaleElementReferenceException e) {
                            LOG.info("Element stale. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                        } catch (NoSuchElementException e) {
                            LOG.info("No such element. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                        } catch (ElementClickInterceptedException e) {
                            LOG.warn("Element Click Intercepted. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                            clickIfDisplayed(inactiveContinueButton, TIME_OUT_QUICK);
                        } catch (ElementNotInteractableException e) {
                            LOG.warn("Element Not Interactable. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                            clickIfDisplayed(inactiveContinueButton, TIME_OUT_QUICK);
                        }
                        return Boolean.FALSE;
                    }
                });
    }

    /**
     * Selects an option from a dropdown menu by index before the end of a given time-out period.
     * <p>
     * Given time-out period of 10 seconds given
     * </p> 
     * 
     * @param by The locator strategy to identify the dropdown menu.
     * @param index The index of the option to be selected.
     */
    public void selectByIndex(By by, Integer index) {
        selectByIndex(by, index, TIME_OUT_SECONDS);
    }

    /**
     * Selects an option from a drop-down list by its index.
     *
     * @param by The locator strategy to identify the dropdown menu.
     * @param index The index of the option to be selected.
     * @param timeOut The maximum time to wait for the WebElement to be visible
     **/
    public void selectByIndex(By by, Integer index, int timeOut) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(POLL_WAIT_MS))
                .until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver driver) {
                        LOG.info("Attempting to Select by index {} {}", by, index);
                        try {
                            scrollIntoView(by);
                            (new Select(getDriver().findElement(by))).selectByIndex(index);
                            return Boolean.TRUE;
                        } catch (TimeoutException e) {
                            LOG.info("Timed out, could not selectByIndex. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                        } catch (StaleElementReferenceException e) {
                            LOG.info("Element stale. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                        } catch (NoSuchElementException e) {
                            LOG.info("No such element. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                        } catch (ElementClickInterceptedException e) {
                            LOG.warn("Element Click Intercepted. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                            clickIfDisplayed(inactiveContinueButton, TIME_OUT_QUICK);
                        } catch (ElementNotInteractableException e) {
                            LOG.warn("Element Not Interactable. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                            clickIfDisplayed(inactiveContinueButton, TIME_OUT_QUICK);
                        }
                        return Boolean.FALSE;
                    }
                });
    }

    /**
     * Selects an option from a dropdown list by its value.
     * <p>
     * Given time-out period of 10 seconds given
     * </p>
     * 
     * @param by the locator for the dropdown element.
     * @param value The value of the item to be selected.
     */
    public void selectByValue(By by, String value) {
        selectByValue(by, value, TIME_OUT_SECONDS);
    }

    /**
     * Selects an option from a dropdown by its value.
     * 
     * @param by The locator strategy to identify the dropdown.
     * @param value The value of the*/
    public void selectByValue(By by, String value, int timeOut) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(POLL_WAIT_MS))
                .until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver driver) {
                        LOG.info("Attempting to Select by Value {} {}", by, value);
                        try {
                            scrollIntoView(by);
                            (new Select(getDriver().findElement(by))).selectByValue(value);
                            return Boolean.TRUE;
                        } catch (TimeoutException e) {
                            LOG.info("Timed out, could not selectByValue. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                        } catch (StaleElementReferenceException e) {
                            LOG.info("Element stale. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                        } catch (NoSuchElementException e) {
                            LOG.info("No such element. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                        } catch (ElementClickInterceptedException e) {
                            LOG.warn("Element Click Intercepted. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                            clickIfDisplayed(inactiveContinueButton, TIME_OUT_QUICK);
                        } catch (ElementNotInteractableException e) {
                            LOG.warn("Element Not Interactable. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                            clickIfDisplayed(inactiveContinueButton, TIME_OUT_QUICK);
                        }
                        return Boolean.FALSE;
                    }
                });
    }

    /**
     * Returns the text of a WebElement identified by the given locator if it is visible on the page.
     * <p>
     *  Returns empty String if WebElement is not located before time-out
     * </p>
     * 
     * @param by the locator used to identify the WebElement
     * @return the text of the WebElement if it is visible, otherwise an empty string
     */
    public String getTextIfVisible(By by) {
        if (checkIfVisible(by)) {
            return getText(by, TIME_OUT_SECONDS);
        } else {
            return "";
        }
    }

    /**
     * Retrieves the text of a WebElement identified by the provided locator.
     * <p>
     * Time-out period of 10 seconds given
     * </p>
     * 
     * @param by the locator used to find the Web element
     * @return the text of the Web element
     */
    public String getText(By by) {
        return getText(by, TIME_OUT_SECONDS);
    }

    /**
     * Retrieves the text of a WebElement identified by the given locator.
     *
     * @param locator The locator used to identify the WebElement.
     * @param timeOut The maximum time to wait for the WebElement to be visible
     */

    public String getText(By locator, int timeOut) {
        try {
            scrollIntoView(locator, timeOut);
            LOG.info("Getting text for element {}", locator);
            return getDriver().findElement(locator).getText();
        } catch (UnhandledAlertException e) {
            getDriver().navigate().refresh();
            expectLoaderIconToDisappear();
            scrollIntoView(locator, timeOut);
            LOG.info("Getting text for element {}", locator);
            return getDriver().findElement(locator).getText();
        }
    }

    /**
     * Retrieves the text of a WebElement located using the given locator and index.
     *
     * @param locator  the locator strategy to identify the WebElement
     * @param index  the position of the WebElement in the list of matching elements
     * @param timeOut  the maximum time to wait for the WebElement to be visible (in seconds)
     * @return the text of the WebElement at the given index
     */
    public String getText(By locator, int index, int timeOut) {
        scrollIntoView(locator, timeOut);
        return getDriver().findElements(locator).get(index).getText();
    }

    /**
     * Waits for until the number of WebElements returned by a locator is equal to or greater than the defined amount.
     * 
     * @param by the locator used to find the WebElements
     * @return the count of elements matching the locator
     */
    public int getElementCount(By by) {
        LOG.info("Getting Element {} Size of {}", by, getDriver().findElements(by).size());
        return getDriver().findElements(by).size();
    }

    /**
     * Waits for until the number of WebElements returned by a locator is equal to or greater than the defined amount.
     * <p>
     * Waits for a given time limit.
     * </p>
     * @param by The locator used to find the WebElements.
     * @param timeOut The maximum time to wait for the WebElements to be populated, in seconds.
     * @param pollAwaitSeconds The number of seconds to wait between each attempt to check if the WebElements are populated.
     */
    public void expectElementsToBePopulated(By by, int timeOut, int pollAwaitSeconds) {
        expectElementsToBePopulated(by, timeOut, pollAwaitSeconds, 0);
    }

    /**
     * Waits until the number of WebElements returned by a locator is equal to or greater than the defined amount.
     * <p>
     * Waits for a given time limit, uses Fluent
     * </p>
     * @param by              the locator strategy to find the WebElements
     * @param timeOut   the maximum time to wait for the WebElements to be populated, in seconds
     * @param pollAwaitSeconds   the interval between each check for the WebElements, in seconds
     * @param size            the expected size of the WebElements list
     */
    public void expectElementsToBePopulated(By by, int timeOut, int pollAwaitSeconds, int size) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return driver.findElements(by).size() > size;
            }
        });
    }

    /**
     * Waits for the WebElement to be enabled before the end of the time-out period.
     *
     * @param by The locating mechanism used to find the WebElement.
     * @param maxTime The maximum time to wait for the WebElement to be enabled, in seconds.
     * @param pollInterval The interval between checking for the WebElement's enabled state, in milliseconds.
     */
    public void expectElementToBeEnabled(By by, int maxTime, int pollInterval) {
        LOG.info("Waiting for element to be enabled: {}", by.toString());
        new WebDriverWait(getDriver(), Duration.ofSeconds(maxTime), Duration.ofMillis(pollInterval))
                .until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver driver) {
                        try {
                            return getDriver().findElement(by).isEnabled();
                        } catch (StaleElementReferenceException e) {
                            LOG.warn("Element stale. Retrying in {} seconds", pollInterval / 1000);
                        } catch (NoSuchElementException e) {
                            LOG.warn("No such element. Retrying in {} seconds", pollInterval / 1000);
                        }
                        return Boolean.FALSE;
                    }
                });
    }

    /**
     * Waits until the WebElement becomes disabled before the end of the time-out period
     *
     * @param by The By object to locate the WebElement.
     * @param maxTime The maximum time to wait in seconds.
     * @param pollInterval The time interval in milliseconds between checking for the WebElement's state.
     */
    public void expectElementToBeDisabled(By by, int maxTime, int pollInterval) {
        LOG.info("Waiting for element to be disabled: {}", by.toString());
        new WebDriverWait(getDriver(), Duration.ofSeconds(maxTime), Duration.ofMillis(pollInterval))
                .until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver driver) {
                        try {
                            new WebDriverWait(getDriver(), Duration.ofSeconds(maxTime))
                                    .until(ExpectedConditions.presenceOfElementLocated(by));
                            return !getDriver().findElement(by).isEnabled();
                        } catch (StaleElementReferenceException e) {
                            LOG.warn("Element stale. Retrying in {} seconds", pollInterval / 1000);
                        } catch (NoSuchElementException e) {
                            LOG.warn("No such element. Retrying in {} seconds", pollInterval / 1000);
                        }
                        return Boolean.FALSE;
                    }
                });
    }

    /**
     * Waits for a WebElements text to update to the expected text value within a specified timeframe.
     *
     * @param by            The locator strategy to identify the WebElement.
     * @param expectedText  The expected text value that the WebElement should have.
     * @param maxTime       The maximum time in seconds to wait for the WebElement's text to be updated.
     * @param pollInterval  The interval in milliseconds between consecutive checks for the WebElement's text update.
     */
    public void expectElementToBeTextUpdated(final By by, String expectedText, int maxTime, int pollInterval) {
        LOG.info("Waiting for element to be enabled: {}", by.toString());
        new WebDriverWait(getDriver(), Duration.ofSeconds(maxTime), Duration.ofMillis(pollInterval))
                .until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver driver) {
                        try {
                            expectElementToBeVisible(by, maxTime);
                            return getText(by).equals(expectedText);
                        } catch (StaleElementReferenceException e) {
                            LOG.warn("Element stale. Retrying in {} seconds", pollInterval / 1000);
                        } catch (NoSuchElementException e) {
                            LOG.warn("No such element. Retrying in {} seconds", pollInterval / 1000);
                        }
                        return Boolean.FALSE;
                    }
                });
    }

    /**
     * Enhanced click, waits for WebElement to be visible, then clicks.
     * Will check document.readyState using JS executor until timeout or readyState is complete
     * Refreshes if errorModal is detected, Default timeout of 30 seconds is provided
     *
     * @param by      - A By element, usually By.xpath(xpathString)
     * @param timeOut - Time limit method will retry the click until
     */
    public void click(By by, int timeOut) {
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(POLL_WAIT_MS))
                    .until(new ExpectedCondition<Boolean>() {
                        @Override
                        public Boolean apply(WebDriver driver) {
                            LOG.info("Attempting to Click {}", by.toString());
                            try {
                                refreshOnErrorMessage();
                                scrollIntoView(by);
                                driver.findElement(by).click();
                                return Boolean.TRUE;
                            } catch (TimeoutException e) {
                                LOG.warn("Timed out, could not click. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                            } catch (StaleElementReferenceException e) {
                                LOG.warn("Element stale. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                            } catch (NoSuchElementException e) {
                                LOG.warn("No such element. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                            } catch (ElementClickInterceptedException e) {
                                LOG.warn("Element Click Intercepted. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                                clickIfDisplayed(inactiveContinueButton, TIME_OUT_QUICK);
                            } catch (ElementNotInteractableException e) {
                                LOG.warn("Element Not Interactable. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                                clickIfDisplayed(inactiveContinueButton, TIME_OUT_QUICK);
                            }
                            return Boolean.FALSE;
                        }
                    });
        } catch (TimeoutException e) {
            refreshOnErrorMessage();
            getJSExecutor().executeScript(Constants.BLUR_ACTIVE_ELEMENT);
            scrollIntoView(by, timeOut);
            getDriver().findElement(by).click();
        }
    }

    /**
     * Enhanced click, waits for WebElement to be visible, then clicks.
     * Will check document.readyState using JS executor until timeout or readyState is complete
     * Refreshes if errorModal is detected, Default timeout of 30 seconds is provided
     *
     * @param element - A WebElement
     * @param timeOut - Time limit method will retry the click until
     */
    public void click(WebElement element, int timeOut) {
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(POLL_WAIT_MS))
                    .until(new ExpectedCondition<Boolean>() {
                        @Override
                        public Boolean apply(WebDriver driver) {
                            LOG.info("Attempting to Click {}", element.toString());
                            try {
                                scrollIntoView(element);
                                element.click();
                                return Boolean.TRUE;
                            } catch (TimeoutException e) {
                                LOG.warn("Timed out, could not click. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                            } catch (StaleElementReferenceException e) {
                                LOG.warn("Element stale. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                            } catch (NoSuchElementException e) {
                                LOG.warn("No such element. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                            } catch (ElementClickInterceptedException e) {
                                LOG.warn("Element Click Intercepted. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                                clickIfDisplayed(inactiveContinueButton, TIME_OUT_QUICK);
                            } catch (ElementNotInteractableException e) {
                                LOG.warn("Element Not Interactable. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                                clickIfDisplayed(inactiveContinueButton, TIME_OUT_QUICK);
                            }
                            return Boolean.FALSE;
                        }
                    });
        } catch (TimeoutException e) {
            getJSExecutor().executeScript(Constants.BLUR_ACTIVE_ELEMENT);
            scrollIntoView(element, timeOut);
            element.click();
        }
    }

    /**
     * Enhanced click, waits for WebElement to be visible, then clicks.
     * Will check document.readyState using JS executor until timeout or readyState is complete
     * Refreshes if errorModal is detected, Default timeout of 30 seconds is provided
     *
     * @param by - A By element, usually By.xpath(xpathString)
     */
    public void click(By by) {
        click(by, TIME_OUT_FOR_PAGE_LOAD);
    }

    /**
     * Enhanced click, waits for WebElement to be visible, then clicks.
     * Will check document.readyState using JS executor until timeout or readyState is complete
     * Refreshes if errorModal is detected, Default timeout of 30 seconds is provided
     *
     * @param element - A WebElement
     */
    public void click(WebElement element) {
        click(element, TIME_OUT_FOR_PAGE_LOAD);
    }

    /**
     * Clicks on the WebElement specified by the {@code By} locator if it is displayed within the specified timeout.
     *
     * @param by      The {@code By} locator used to locate the WebElement.
     * @param timeOut The maximum time, in seconds, to wait for the WebElement to be displayed.
     */
    public void clickIfDisplayed(By by, int timeOut) {
        if (checkIfDisplayed(by, timeOut)) {
            click(by, timeOut);
        }
    }

    /**
     * Clicks on a WebElement identified by the given locator if it is displayed on the page.
     *
     * @param by The locator used to identify the WebElement.
     */
    public void clickIfDisplayed(By by) {
        clickIfDisplayed(by, TIME_OUT_FOR_PAGE_LOAD);
    }

    /**
     * Refreshes the page when the UI displays an error message
     */
    public void refreshOnErrorMessage() {
        if (!checkIfInvisible(errorModalMessage)) {
            getDriver().navigate().refresh();
            expectLoaderIconToDisappear();
        }
    }

    /**
     * Refreshes the page if the WebElement identified by the given locator is not visible.
     *
     * @param locator the locator used to identify the WebElement
     */
    public void refreshIfLocatorNotVisible(By locator) {
        refreshIfLocatorNotVisible(locator, TIME_OUT_FOR_PAGE_LOAD);
    }

    /**
     * Refreshes the page if the given WebElement is not visible within the specified timeout period.
     *
     * @param locator The locator to check for visibility.
     * @param timeOut The timeout period in seconds.
     */
    public void refreshIfLocatorNotVisible(By locator, int timeOut) {
        if (!checkIfVisible(locator, timeOut)) {
            getDriver().navigate().refresh();
        }
    }

    /**
     * Retrieves the JavaScript executor for the current driver.
     *
     * @return The JavaScript executor.
     */
    public JavascriptExecutor getJSExecutor() {
        return getJSExecutor(getDriver());
    }

    /**
     * Returns a JavascriptExecutor object for executing JavaScript in the given WebDriver.
     *
     * @param driver the WebDriver instance
     */
    public JavascriptExecutor getJSExecutor(WebDriver driver) {
        return (JavascriptExecutor) driver;
    }

    /**
     * Waits until the WebElement identified by the given locator is enabled.
     *
     * @param by           the locator used to find the WebElement
     * @param maxTime      the maximum time to wait for the WebElement to be enabled, in seconds
     * @param pollInterval the polling interval,*/
    protected void waitUntilEnabled(By by, int maxTime, int pollInterval) {
        expectElementToBeVisible(by, maxTime);
        WebElement element = getDriver().findElement(by);
        waitUntilEnabled(element, maxTime, pollInterval);
    }


    /**
     * Waits until the given WebElement is enabled or until a maximum wait time is reached.
     *
     * @param element      the WebElement to wait for
     * @param maxTime      the maximum time to wait in seconds
     * @param pollInterval the interval between checking the enabled status in seconds*/
    protected void waitUntilEnabled(WebElement element, long maxTime, long pollInterval) {
        Awaitility.await(element.toString() + " is not enabled").atMost(maxTime, TimeUnit.SECONDS).with()
                .pollDelay(POLL_NO_INIT_DELAY, TimeUnit.SECONDS).pollInterval(pollInterval, TimeUnit.SECONDS)
                .until(element::isEnabled);
    }

    /**
     * An expectation for checking a WebElement is visible and enabled
     * Designed to return true if a WebElement is clickable
     *
     * @param by The By object representing the WebElement to be checked.
     * @return true if the WebElement is clickable, false otherwise.
     */
    public boolean checkIfClickable(By by) {
        return checkIfClickable(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking a WebElement is visible and enabled
     * Designed to return true if a WebElement is clickable
     *
     * @param by locator used to find the WebElement
     * @param timeOut time limit to wait before throwing an Exception
     * @return boolean The result is returned or false is returned if the time-out passes
     */
    public boolean checkIfClickable(By by, int timeOut) {
        try {
            (new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut)))
                    .until(ExpectedConditions.elementToBeClickable(by));
            return Boolean.TRUE;
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }

    /**
     * An expectation for checking a WebElement is visible and enabled
     * Designed to return true if a WebElement is clickable
     *
     * @param by locator used to find the WebElement
     * @param index index of the WebElement to be clicked (useful when
     * @param timeOut time limit to wait before throwing an Exception
     * @return The result is returned or false is returned if the time-out passes
     */
    public boolean checkIfClickable(By by, int index, int timeOut) {
        LOG.info("Checking if Element {} is clickable", by);
        try {
            (new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut)))
                    .until(ExpectedConditions.elementToBeClickable(getDriver().findElements(by).get(index)));
            return Boolean.TRUE;
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }

    /**
     * Switches to the current window handle and accepts the alert for outdated browser.
     * <p>
     * This method iterates over all the window handles of the WebDriver and switches
     * to the window that is not the current handle. It then accepts the alert for an outdated
     * browser and breaks the loop.
     *
     * @throws NoAlertPresentException if no alert is present
     * @throws NoSuchWindowException   if the specified window handle does not exist
     */
    public void switchToCurrentWindow() {
        Set<String> handles = getDriver().getWindowHandles();
        String currentHandle = getDriver().getWindowHandle();

        for (String handle : handles) {
            if (!handle.equals(currentHandle)) {
                getDriver().switchTo().window(handle);
                acceptAlertForOutdatedBrowser();
                break;
            }
        }
    }

    /**
     * Verifies if the text is contained within the text of the given WebElement.
     *
     * @param locator the WebElement to check the text from
     * @param text the text to verify if it is contained within the web element's text
     * @return true if the web element's text contains the specified text, false otherwise
     */
    public boolean getTextContainsToVerify(WebElement locator, String text) {
        return getTextContainsToVerify(locator, text, TIME_OUT_SECONDS);
    }

    /**
     * Checks whether the text of the given WebElement contains the specified text within a given timeout period.
     *
     * @param locator       The WebElement to check for text.
     * @param text          The text to search for within the web element.
     * @param timeOut The timeout period in seconds for WebElement visibility.
     * @return {@code true} if the web element's text contains the specified text; otherwise, {@code false}.
     */
    public boolean getTextContainsToVerify(WebElement locator, String text, int timeOut) {
        (new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut)))
                .until(ExpectedConditions.visibilityOfAllElements(locator));
        return locator.getText().contains(text);
    }

    /**
     * Verifies if the given text is contained in the WebElement identified by the specified locator.
     *
     * @param locator the locator used to identify the WebElement
     * @param text the text to verify
     * @return true if the WebElement contains the specified text, false otherwise
     */
    public boolean getTextContainsToVerify(By locator, String text) {
        return getTextContainsToVerify(locator, text, TIME_OUT_SECONDS);
    }

    /**
     * Checks if the text of a WebElement contains the specified text within the given timeout period.
     *
     * @param locator the locator used to find the WebElement
     * @param text the text to search for within the WebElement's text
     * @param timeOut the maximum time to wait for the WebElement to become visible
     * @return true if the WebElement's text contains the specified text, false otherwise
     */
    public boolean getTextContainsToVerify(By locator, String text, int timeOut) {
        (new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut)))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        return getDriver().findElement(locator).getText().contains(text);
    }

    /**
     * Waits for the text in a WebElement located by the given locator to be updated to the expected text.
     * <p>
     * Time-out period of 20 seconds given
     * </p>
     * @param statusLocator the locator used to identify the web element
     * @param expectedStatus the expected text that the WebElement should be updated to
     */
    public void expectTextToUpdateTo(By statusLocator, String expectedStatus) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(TIMEOUT_COMPLETE_TASK),
                Duration.ofSeconds(TIME_OUT_AWAIT_SECONDS)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return getTextContainsToVerify(statusLocator, expectedStatus);
            }
        });
    }

    /**
     * Verifies if all text of WebElements located by the given locator contains the specified text.
     *
     * @param locator          the locator of the text elements to verify
     * @param text             the text to search for
     * @param timeOut    the maximum time in seconds to wait for the text elements to be visible
     * @return true if at least one of the text elements contains the specified text, otherwise false
     */
    public boolean getAllTextContainsToVerify(By locator, String text, int timeOut) {
        (new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut)))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        List<String> allText = getDriver().findElements(locator).stream().map(WebElement::getText)
                .collect(Collectors.toList());
        for (String actualTextItem : allText) {
            if (actualTextItem.contains(text)) {
                LOG.info("{} contains {}", allText, text);
                return Boolean.TRUE;
            }
            LOG.info("{} does not contain {}", allText, text);
        }
        return Boolean.FALSE;
    }

    /**
     * Checks whether the text of the WebElement located by locator and index matches the expected text.
     *
     * @param locator the locator used to identify the WebElement
     * @param index the index of the WebElement to check
     * @param text the text to search for
     * @param timeOut the maximum time to wait for the WebElement to be visible
     * @return true if the text at the specified index of the WebElement contains the specified text, false otherwise
     */
    public boolean getTextContainsToVerify(By locator, int index, String text, int timeOut) {
        (new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut)))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        return getDriver().findElements(locator).get(index).getText().contains(text);
    }

    /**
     * Retrieves the value of a cookie with the specified name.
     *
     * @param cookieName the name of the cookie to retrieve
     * @return the value of the cookie
     */
    protected String getCookie(String cookieName) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(LONGER_TIME_OUT_SECONDS))
                .until(new ExpectedCondition<Cookie>() {
                    @Override
                    public Cookie apply(WebDriver driver) {
                        return driver.manage().getCookieNamed(cookieName);
                    }
                });
        String cookie = getDriver().manage().getCookieNamed(cookieName).getValue();
        LOG.info("Got Cookie [{}]: {}", cookieName, cookie);
        return cookie;
    }

    /**
     * Moves the mouse cursor over a specified WebElement identified by the given locator.
     * <p>
     * This method moves the mouse cursor over the WebElement to trigger any hover or mouse over events.
     * It uses the default timeout value for waiting until the WebElement is available for interaction.
     * </p>
     *
     * @param by the locator used to find the web element.
     */
    protected void mouseOver(By by) {
        mouseOver(by, TIME_OUT_SECONDS);
    }

    /**
     * Moves the mouse over the specified WebElement identified by the given By locator.
     * <p>
     * Time-out period defined by caller
     * </p>
     *
     * @param by The By locator used to identify the WebElement to mouse over.
     * @param timeOutInSeconds The timeout value in seconds to wait for the WebElement to be moused over.
     */
    @SuppressWarnings("SameParameterValue")
    protected void mouseOver(By by, int timeOutInSeconds) {
        ExpectedCondition<Boolean> mousedOver = new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                LOG.info("Attempting to Mouse Over {}", by.toString());
                try {
                    scrollIntoView(by, timeOutInSeconds);
                    WebElement element = getElement(by, timeOutInSeconds);
                    Actions action = new Actions(driver);
                    action.moveToElement(element).build().perform();
                    return Boolean.TRUE;
                } catch (TimeoutException e) {
                    LOG.info("Timed out, could not mouse over. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                } catch (StaleElementReferenceException e) {
                    LOG.info("Element stale. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                } catch (NoSuchElementException e) {
                    LOG.info("No such element. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                }
                return Boolean.FALSE;
            }
        };
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSeconds), Duration.ofMillis(POLL_WAIT_MS))
                .until(mousedOver);
    }

    /**
     * Expands the specified section identified by the given section name.
     *
     * @param sectionName the name of the section to expand
     */
    public void expand(String sectionName) {
        By expandLocator = By.cssSelector("a[title^='Expand " + sectionName + "']");
        By collapseLocator = By.cssSelector("a[title^='Collapse " + sectionName + "']");

        new WebDriverWait(getDriver(), Duration.ofSeconds(TIMEOUT_COMPLETE_TASK),
                Duration.ofMillis(LONGEST_POLL_WAIT_MS)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                if (checkIfDisplayed(expandLocator)) {
                    LOG.info("{} section is collapsed, expanding", sectionName);
                    mouseOver(expandLocator);
                    click(expandLocator);
                } else if (checkIfDisplayed(collapseLocator)) {
                    LOG.info("{} section is already expanded", sectionName);
                }
                return checkIfDisplayed(collapseLocator);
            }
        });
    }

    /**
     * Collapses a section with the given name.
     *
     * @param sectionName the name of the section to collapse or expand
     */
    public void collapse(String sectionName) {
        By expandLocator = By.cssSelector("a[title^='Expand " + sectionName + "']");
        By collapseLocator = By.cssSelector("a[title^='Collapse " + sectionName + "']");

        new WebDriverWait(getDriver(), Duration.ofSeconds(TIMEOUT_COMPLETE_TASK),
                Duration.ofMillis(LONGEST_POLL_WAIT_MS)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                if (checkIfDisplayed(collapseLocator)) {
                    LOG.info("{} section is expanded, collapsing", sectionName);
                    mouseOver(collapseLocator);
                    click(collapseLocator);
                } else if (checkIfDisplayed(expandLocator)) {
                    LOG.info("{} section is already collapsed", sectionName);
                }
                return checkIfDisplayed(expandLocator);
            }
        });
    }

    /**
     * Searches for a WebElement using the given search criteria and waits for the search result to be displayed.
     *
     * @param searchBy The locator strategy used to find the WebElement.
     * @param searchResult The locator strategy used to find the search result elements.
     * @param searchType The type of search being performed.
     * @param input The input value used for the search.
     */
    public void searchBy(By searchBy, By searchResult, String searchType, String input) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(LONGER_TIME_OUT_SECONDS),
                Duration.ofMillis(LONGEST_POLL_WAIT_MS)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                LOG.info("Searching for {} ID [{}]", searchType, input);
                try {
                    WebElement element = driver.findElement(searchBy);
                    element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
                    element.sendKeys(input);
                    getJSExecutor().executeScript(Constants.BLUR_ACTIVE_ELEMENT);
                    new WebDriverWait(driver, Duration.ofSeconds(TIME_OUT_FOR_EXTRA))
                            .until(ExpectedConditions.numberOfElementsToBeMoreThan(searchResult, 0));
                    LOG.info("[{}] found [{}]", input, driver.findElements(searchResult).size());
                    return Boolean.TRUE;
                } catch (TimeoutException e) {
                    LOG.info("[{}] Search result {} did not return any results.  Retrying...", input,
                            searchResult);
                } catch (NoSuchElementException e) {
                    LOG.info("[{}] Search by {} is not found yet.  Retrying...", input, searchBy);
                } catch (StaleElementReferenceException e) {
                    LOG.info("[{}] Search by {} is stale.  Retrying...", input, searchBy);
                } catch (ElementClickInterceptedException e) {
                    LOG.warn("Element Click Intercepted. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                    clickIfDisplayed(inactiveContinueButton, TIME_OUT_QUICK);
                } catch (ElementNotInteractableException e) {
                    LOG.warn("Element Not Interactable. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                    clickIfDisplayed(inactiveContinueButton, TIME_OUT_QUICK);
                }
                return Boolean.FALSE;
            }
        });
    }


    /**
     * Returns the index of the specified header title 
     * <p>
     * Collect all header WebElements in list and searches list for header title and returns index as String
     * </p> 
     *
     * @param headers     the locator used to identify the list of headers.
     * @param headerTitle the title of the header to find the index of.
     * @return the index of the specified header title as a string. If the header is not found, returns "-1".
     */
    public String getHeaderIndex(By headers, String headerTitle) {
        List<String> listOfHeaders = getElements(headers, TIME_OUT_SECONDS).stream().map(WebElement::getText)
                .toList();
        int index = listOfHeaders.indexOf(headerTitle) + 1;
        return Integer.toString(index);
    }

    /**
     * Waits until the options of a select WebElement are populated.
     * <p>
     * Uses Fluent wait and has given time-out of 90 seconds and polls every 2 seconds
     * </p>
     *
     * @param by The locator used to identify the select element.
     */
    public void waitUntilSelectOptionsPopulated(final By by) {
        LOG.info("Waiting for options to be populated in {}", by.toString());
        new WebDriverWait(getDriver(), Duration.ofSeconds(TIMEOUT_COMPLETE_TASK), Duration.ofMillis(POLL_WAIT_MS))
                .until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver driver) {
                        try {
                            Select select = new Select(driver.findElement(by));
                            return (select.getOptions().size() > 1);
                        } catch (NoSuchElementException e) {
                            LOG.info("No such element. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                        }
                        return Boolean.FALSE;
                    }
                });
    }

    /**
     * Waits until the number of WebElements found by single locator matches the target count.
     * <p>
     * Uses Fluent wait and has given time-out of 20 seconds and polls every 2 seconds
     * </p>
     *
     * @param by          the locator used to find the WebElements
     * @param targetCount the expected number of elements
     */
    public void waitUntilGetElementsCount(final By by, int targetCount) {
        LOG.info("Waiting for elements {} count to be {}", by.toString(), targetCount);
        new WebDriverWait(getDriver(), Duration.ofSeconds(TIME_OUT_AWAIT_SECONDS), Duration.ofMillis(POLL_WAIT_MS))
                .until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver driver) {
                        try {
                            int currentCount = getElements(by).size();
                            return (currentCount == targetCount);
                        } catch (NoSuchElementException e) {
                            LOG.info("No such element. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                        }
                        return Boolean.FALSE;
                    }
                });
    }

    /**
     * Accepts the alert for an outdated browser. 
     * This method verifies if an alert is present and checks its message. 
     * If the message matches the expected message, the alert is accepted. 
     * If no alert is present, a debug log is generated.
     */
    public void acceptAlertForOutdatedBrowser() {
        try {
            Alert alt = getDriver().switchTo().alert();
            String actualBoxMsg = alt.getText();
            Assert.assertEquals(actualBoxMsg,
                    "Our website has detected that you are using an outdated browser that may prevent you from accessing certain features. Please update your browser to the latest version.");
            alt.accept();

        } catch (NoAlertPresentException e) {
            LOG.debug("No alert is present");
        }
    }

    /**
     * Refreshes the page if the specified WebElement is not displayed within the given timeout period.
     * <p>
     * Given time-out period of 30 seconds
     * </p>
     * @param locator the locator of the WebElement to check for visibility
     */
    public void refreshIfLocatorNotDisplayed(By locator) {
        if (!checkIfDisplayed(locator, TIME_OUT_FOR_PAGE_LOAD)) {
            LOG.info("Refreshing the page");
            getDriver().navigate().refresh();
            new WebDriverWait(getDriver(), Duration.ofSeconds(TIME_OUT_FOR_PAGE_LOAD))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
        }
    }


    /**
     * Retrieves the version ID attribute from a dynamic component using JS
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by double clicking
     * </p>
     * <p>
     * Given time-out period of 6 seconds
     * </p>
     *
     * @return the version ID
     */
    public String getVersionId() {
        return getVersionId(getDriver(), Constants.CURRENT_TASK_VERSION_ID);
    }

    /**
     * Retrieves the version ID attribute from a dynamic component using JS
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by double clicking
     * </p>
     *
     * @param script the script for which the version ID needs to be retrieved
     * @return the version ID of the script
     */
    public String getVersionId(String script) {
        return getVersionId(getDriver(), script);
    }

    /**
     * Waits for the version ID update to be completed.
     *
     * @param origVersionId   The original version ID.
     * @param timeOut   The time-out duration in seconds.
     * @return                {@code true} if the version ID update is completed within the specified time-out duration,
     *                        {@code false} otherwise.
     */
    public Boolean waitForVersionIdUpdate(String origVersionId, int timeOut) {
        return waitForVersionIdUpdate(origVersionId, timeOut, Constants.CURRENT_TASK_VERSION_ID);
    }

    /**
     * Waits for the version ID to be updated based on the provided original version ID and script.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by double clicking
     * </p>
     *
     * @param origVersionId   The original version ID before waiting for an update.
     * @param timeOut   The maximum time to wait in seconds for the version ID to be updated.
     * @param script          The script to be executed for retrieving the current version ID.
     * @return True if the version ID was successfully updated within the specified time limit, false otherwise.
     */
    public Boolean waitForVersionIdUpdate(String origVersionId, int timeOut, String script) {
        LOG.info("Waiting for versionId Update [{}]", script);
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(POLL_WAIT_MS))
                    .until(new ExpectedCondition<Boolean>() {
                        @Override
                        public Boolean apply(WebDriver driver) {
                            String currVersionId = getVersionId(driver, script);
                            if (StringUtils.equals(origVersionId, currVersionId)) {
                                LOG.info("currVersionId: {}, waiting... [{}]", currVersionId, script);
                                return Boolean.FALSE;
                            }
                            LOG.info("currVersionId: {}, continuing... [{}]", currVersionId, script);
                            return Boolean.TRUE;
                        }
                    });
            return Boolean.TRUE;
        } catch (Exception e) {
            LOG.warn("Waited {} seconds for versionId Update, continuing... [{}]", timeOut, script);
            return Boolean.FALSE;
        }
    }

    /**
     * This method performs a double click action on the given WebElement.
     * Uses the specified timeout to wait for a version ID update after the double click action.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before double click.
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement then double click.
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by double clicking
     * </p>
     * <p>
     * Given time-out period of 6 seconds
     * </p>
     *
     * @param by the locator of the dropdown WebElement to click on
     */
    public void ddClick(By by) {
        ddClick(by, TIME_OUT_DD_SECONDS, Constants.CURRENT_TASK_VERSION_ID);
    }

    /**
     * This method performs a double click action on the given WebElement.
     * Uses the specified timeout to wait for a version ID update after the double click action.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before double click.
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement then double click.
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by double clicking
     * </p>
     * <p>
     * Given time-out period of 6 seconds
     * </p>
     *
     * @param by
     * @param script
     */
    public void ddClick(By by, String script) {
        ddClick(by, TIME_OUT_DD_SECONDS, script);
    }

    /**
     * This method performs a double click action on the given WebElement.
     * Uses the specified timeout to wait for a version ID update after the double click action.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before double click.
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement then double click.
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by double clicking
     * </p>
     *
     * @param by           the locator strategy (By) used to find the WebElement
     * @param timeOut the maximum time to wait for the WebElement to be clickable
     */
    public void ddClick(By by, int timeOut) {
        ddClick(by, timeOut, Constants.CURRENT_TASK_VERSION_ID);
    }

    /**
     * This method performs a double click action on the given WebElement.
     * Uses the specified timeout to wait for a version ID update after the double click action.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before double click.
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement then double click.
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by double clicking
     * </p>
     *
     * @param by the locator strategy to find the web element
     * @param timeOut the maximum time in seconds to wait for version ID update
     * @param script the script associated with the version ID
     */
    public void ddClick(By by, int timeOut, String script) {
        String origVersionId = getVersionId(getDriver(), script);
        LOG.info("origVersionId: {} [{}]", origVersionId, script);
        click(by);
        getJSExecutor().executeScript(Constants.BLUR_ACTIVE_ELEMENT);
        if (!waitForVersionIdUpdate(origVersionId, timeOut, script))
            TestContext.logToScenario(
                    String.format("DynamicData version update check has failed for %s [%s]", by.toString(), script));
    }

    /**
     * This method performs a double click action on the given WebElement.
     * Uses the specified timeout to wait for a version ID update after the double click action.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before double click.
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement then double click.
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by double clicking
     * </p>
     * <p>
     * Given time-out period of 6 seconds
     * </p>
     *
     * @param elem the WebElement to be clicked
     */
    public void ddClick(WebElement elem) {
        ddClick(elem, TIME_OUT_DD_SECONDS, Constants.CURRENT_TASK_VERSION_ID);
    }

    /**
     * This method performs a double click action on the given WebElement.
     * Uses the specified timeout to wait for a version ID update after the double click action.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before double click.
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement then double click.
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by double clicking
     * </p>
     * <p>
     * Given time-out period of 6 seconds
     * </p>
     *
     * @param elem
     * @param script
     */
    public void ddClick(WebElement elem, String script) {
        ddClick(elem, TIME_OUT_DD_SECONDS, script);
    }

    /**
     * This method performs a double click action on the given WebElement.
     * Uses the specified timeout to wait for a version ID update after the double click action.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before double click.
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement then double click.
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by double clicking
     * </p>
     *
     * @param elem           the WebElement to click on
     * @param timeOut  the timeout in seconds for the click operation
     * @since 1.0
     */
    public void ddClick(WebElement elem, int timeOut) {
        ddClick(elem, timeOut, Constants.CURRENT_TASK_VERSION_ID);
    }

    /**
     * This method performs a double click action on the given WebElement.
     * Uses the specified timeout to wait for a version ID update after the double click action.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before double click.
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement then double click.
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by double clicking
     * </p>
     *
     * @param elem           The WebElement on which the double click action should be performed.
     * @param timeOut  The timeout in seconds to wait for a version ID update after the double click action.
     * @param script         The script to execute to obtain the original version ID.
     * @see #getVersionId(WebDriver, String)
     * @see #click(WebElement)
     * @see #getJSExecutor()
     * @see Constants#BLUR_ACTIVE_ELEMENT
     * @see #waitForVersionIdUpdate(String, int, String)
     * @see TestContext#logToScenario(String)
     */
    public void ddClick(WebElement elem, int timeOut, String script) {
        String origVersionId = getVersionId(getDriver(), script);
        LOG.info("origVersionId: {} [{}]", origVersionId, script);
        click(elem);
        getJSExecutor().executeScript(Constants.BLUR_ACTIVE_ELEMENT);
        if (!waitForVersionIdUpdate(origVersionId, timeOut, script))
            TestContext.logToScenario(
                    String.format("DynamicData version update check has failed for %s [%s]", elem.toString(), script));
    }

    /**
     * Sends keys to a given WebElement and use state change to validate typing action.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before simulating typing.
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement.
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent after simulating typing.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by simulating typing.
     * </p>
     * <p>
     * Given time-out period of 6 seconds
     * </p>
     *
     * @param by The locator used to identify the WebElement to send keys to.
     * @param input The input value to send to the WebElement.
     */
    public void ddSendKeysBlur(By by, String input) {
        ddSendKeysBlur(by, input, TIME_OUT_DD_SECONDS, Constants.CURRENT_TASK_VERSION_ID);
    }

    /**
     * Sends keys to a given WebElement and use state change to validate typing action.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before simulating typing.
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement.
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent after simulating typing.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by simulating typing.
     * </p>
     * <p>
     * Given time-out period of 6 seconds
     * </p>
     *
     * @param by the locator used to locate the WebElement
     * @param input the keys to be sent to the WebElement
     * @param script the JavaScript code to trigger the blur event on the WebElement
     */
    public void ddSendKeysBlur(By by, String input, String script) {
        ddSendKeysBlur(by, input, TIME_OUT_DD_SECONDS, script);
    }

    /**
     * Sends keys to a given WebElement and use state change to validate typing action.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before simulating typing.
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement.
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent after simulating typing.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by simulating typing.
     * </p>
     *
     * @param by The locator strategy to find the WebElement.
     * @param input The keys to be sent to the WebElement.
     * @param timeOut The timeout in seconds for waiting.
     */
    public void ddSendKeysBlur(By by, String input, int timeOut) {
        ddSendKeysBlur(by, input, timeOut, Constants.CURRENT_TASK_VERSION_ID);
    }

    /**
     * Sends keys to a given WebElement and use state change to validate typing action.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before simulating typing.
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement.
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent after simulating typing.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by simulating typing.
     * </p>
     *
     * @param by            The WebElement to send keys to.
     * @param input         The input value to send to the WebElement.
     * @param timeOut The timeout in seconds for waiting for version ID update.
     * @param script        The script to get the original version ID from.
     */
    public void ddSendKeysBlur(By by, String input, int timeOut, String script) {
        LOG.info("Clearing and ddSending Keys to Element {} with values {} [{}] and then refocusing by sending Blur",
                by, input, script);
        String origVersionId = getVersionId(getDriver(), script);
        LOG.info("origVersionId: {} [{}]", origVersionId, script);
        sendKeysBlur(by, input);
        if (!waitForVersionIdUpdate(origVersionId, timeOut, script))
            TestContext.logToScenario(
                    String.format("DynamicData version update check has failed for %s [%s]", by.toString(), script));
    }

    /**
     * Sends keys to a given WebElement and use state change to validate typing action.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before simulating typing.
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement.
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent after simulating typing.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by simulating typing.
     * </p>
     * <p>
     * Given time-out period of 6 seconds
     * </p>
     *
     * @param element the WebElement to send keys to
     * @param input the text to be entered in the element
     */
    public void ddSendKeysBlur(WebElement element, String input) {
        ddSendKeysBlur(element, input, TIME_OUT_DD_SECONDS, Constants.CURRENT_TASK_VERSION_ID);
    }

    /**
     * Sends keys to a given WebElement and use state change to validate typing action.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before simulating typing.
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement.
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent after simulating typing.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by simulating typing.
     * </p>
     * <p>
     * Given time-out period of 6 seconds
     * </p>
     *
     * @param element The WebElement to which keys need to be sent.
     * @param input The keys or text to be sent to the WebElement.
     * @param script The JavaScript code to trigger the blur event on the WebElement.
     *
     * @see  ddSendKeysBlur(WebElement element, String input, int timeout, String script)
     */
    public void ddSendKeysBlur(WebElement element, String input, String script) {
        ddSendKeysBlur(element, input, TIME_OUT_DD_SECONDS, script);
    }

    /**
     * Sends keys to a given WebElement and use state change to validate typing action.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before simulating typing.
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement.
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent after simulating typing.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by simulating typing.
     * </p>
     *
     * @param element       the WebElement to send keys to
     * @param input         the keys to send to the web element
     * @param timeOut the maximum time to wait for the blur event to be triggered
     */
    public void ddSendKeysBlur(WebElement element, String input, int timeOut) {
        ddSendKeysBlur(element, input, timeOut, Constants.CURRENT_TASK_VERSION_ID);
    }

    /**
     * Sends keys to a given WebElement and use state change to validate typing action.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before simulating typing.
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement.
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent after simulating typing.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by simulating typing.
     * </p>
     *
     * @param element       The WebElement to send keys to.
     * @param input         The input value to send to the WebElement.
     * @param timeOut The timeout in seconds for waiting for version ID update.
     * @param script        The script to get the original version ID from.
     */
    public void ddSendKeysBlur(WebElement element, String input, int timeOut, String script) {
        LOG.info("Clearing and ddSending Keys to Element {} with values {} [{}] and then remove focus by sending Blur",
                element, input, script);
        String origVersionId = getVersionId(getDriver(), script);
        LOG.info("origVersionId: {} [{}]", origVersionId, script);
        sendKeysBlur(element, input, timeOut);
        if (!waitForVersionIdUpdate(origVersionId, timeOut, script))
            TestContext.logToScenario(String.format("DynamicData version update check has failed for %s [%s]",
                    element.toString(), script));
    }

    /**
     * Enters a date in a specified input field.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before entering date.
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement then enters date.
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by entering date
     * </p>
     * <p>
     * Given time-out period of 6 seconds
     * </p>
     *
     * @param dateEntryLocator The locator of the date entry input field.
     * @param input The date value to enter.
     */
    public void ddEnterDate(By dateEntryLocator, String input) {
        ddEnterDate(dateEntryLocator, input, Constants.CURRENT_TASK_VERSION_ID);
    }

    /**
     * Enters a date in a specified input field.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before entering date.
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement then enters date.
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by entering date
     * </p>
     * <p>
     * Given time-out period of 6 seconds
     * </p>
     *
     * @param by The locator used to identify the dropdown element.
     * @param text The value of the option to be selected.
     * @param script The JavaScript script to execute.
     */
    public void ddEnterDate(By dateEntryLocator, String input, String script) {
        String origVersionId = getVersionId(getDriver(), script);
        sendKeys(dateEntryLocator, input);
        getJSExecutor().executeScript(Constants.BLUR_ACTIVE_ELEMENT);
        if (!waitForVersionIdUpdate(origVersionId, TIME_OUT_DD_SECONDS, script))
            TestContext.logToScenario(String.format("DynamicData version update check has failed for %s [%s]",
                    dateEntryLocator.toString(), script));
    }

    /**
     * Selects an option from a dropdown menu by its text.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before selectByText().
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement then execute selectByText().
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by selecting an option
     * </p>
     * <p>
     * Given time-out period of 6 seconds
     * </p>
     *
     * @param by The locator used to identify the dropdown element.
     * @param text The value of the option to be selected.
     */
    public void ddSelectByText(By by, String text) {
        ddSelectByText(by, text, TIME_OUT_DD_SECONDS, Constants.CURRENT_TASK_VERSION_ID);
    }

    /**
     * Selects an option from a dropdown menu by its text.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before selectByText().
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement then execute selectByText().
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by selecting an option
     * </p>
     * <p>
     * Given time-out period of 6 seconds
     * </p>
     *
     * @param by The locator used to identify the dropdown element.
     * @param text The value of the option to be selected.
     * @param script The JavaScript script to execute.
     */
    public void ddSelectByText(By by, String text, String script) {
        ddSelectByText(by, text, TIME_OUT_DD_SECONDS, script);
    }

    /**
     * Selects an option from a dropdown menu by its text.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before selectByText().
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement then execute selectByText().
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by selecting an option
     * </p>
     *
     * @param by The locator used to identify the dropdown element.
     * @param text The value of the option to be selected.
     * @param timeOut The maximum time to wait for the version ID update.
     */
    public void ddSelectByText(By by, String text, int timeOut) {
        ddSelectByText(by, text, timeOut, Constants.CURRENT_TASK_VERSION_ID);
    }

    /**
     * Selects an option from a dropdown menu by its text.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before selectByText().
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement then execute selectByText().
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by selecting an option
     * </p>
     *
     * @param by The locator used to identify the dropdown element.
     * @param text The value of the option to be selected.
     * @param timeOut The maximum time to wait for the version ID update.
     * @param script The JavaScript script to execute.
     */
    public void ddSelectByText(By by, String text, int timeOut, String script) {
        String origVersionId = getVersionId(getDriver(), script);
        selectByText(by, text);
        getJSExecutor().executeScript(Constants.BLUR_ACTIVE_ELEMENT);
        if (!waitForVersionIdUpdate(origVersionId, timeOut, script))
            TestContext.logToScenario(
                    String.format("DynamicData version update check has failed for %s [%s]", by.toString(), script));
    }

    /**
     * Selects an option from a dropdown menu by its value.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before selectByValue().
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement then execute selectByValue().
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by selecting an option
     * </p>
     * <p>
     * Given time-out period of 6 seconds
     * </p>
     *
     * @param by the locator strategy to identify the dropdown element
     * @param text the value of the option to be selected
     */
    public void ddSelectByValue(By by, String text) {
        ddSelectByValue(by, text, TIME_OUT_DD_SECONDS, Constants.CURRENT_TASK_VERSION_ID);
    }

    /**
     * Selects an option from a dropdown menu by its value.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before selectByValue().
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement then execute selectByValue().
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by selecting an option
     * </p>
     * <p>
     * Given time-out period of 6 seconds
     * </p>
     *
     * @param by     The locator strategy to find the drop-down element.
     * @param text   The value of the option to be selected.
     * @param script The JavaScript script to execute after selecting the option.
     */
    public void ddSelectByValue(By by, String text, String script) {
        ddSelectByValue(by, text, TIME_OUT_DD_SECONDS, script);
    }

    /**
     * Selects an option from a dropdown menu by its value.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before selectByValue().
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement then execute selectByValue().
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by selecting an option
     * </p>
     *
     * @param by The locator of the dropdown element.
     * @param text The value of the option to be selected.
     * @param timeOut The maximum time to wait for the option to be selectable, in seconds.
     */
    public void ddSelectByValue(By by, String text, int timeOut) {
        ddSelectByValue(by, text, timeOut, Constants.CURRENT_TASK_VERSION_ID);
    }

    /**
     * Selects an option from a dropdown menu by its value.
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent before selectByValue().
     * </p>
     * <p>
     * Execute JS blur method to remove keyboard focus from WebElement then execute selectByValue().
     * </p>
     * <p>
     * Execute JS script to get versionId of Redux/DynamicComponent.
     * </p>
     * <p>
     * Compare versionIds to establish state change caused by selecting an option
     * </p>
     * @param by The locator used to identify the dropdown element.
     * @param text The value of the option to be selected.
     * @param timeOut The maximum time to wait for the version ID update.
     * @param script The JavaScript script to execute.
     */
    public void ddSelectByValue(By by, String text, int timeOut, String script) {
        String origVersionId = getVersionId(getDriver(), script);
        selectByValue(by, text);
        getJSExecutor().executeScript(Constants.BLUR_ACTIVE_ELEMENT);
        if (!waitForVersionIdUpdate(origVersionId, timeOut, script))
            TestContext.logToScenario(
                    String.format("DynamicData version update check has failed for %s [%s]", by.toString(), script));
    }

    /**
     * Retrieves the version ID by executing a JavaScript script using JS executor.
     * <p>
     * versionId is an attribute of a Redux/Dynamic component that changes value depending on state change after interaction
     * </p>
     *
     * @param driver the WebDriver instance to retrieve the version ID for
     * @return the version ID as a string
     */
    public String getVersionId(WebDriver driver) {
        return getVersionId(driver, Constants.CURRENT_TASK_VERSION_ID);
    }

    /**
     * Retrieves the version ID by executing a JavaScript script using JS executor.
     * <p>
     * versionId is an attribute of a Redux/Dynamic component that changes value depending on state change after interaction
     * </p>
     *
     * @param driver The WebDriver instance to execute the JavaScript on.
     * @param script The JavaScript script to execute.
     * @return The version ID obtained from executing the script, or null if the version ID is "undefined" or if an exception occurs.
     */
    public String getVersionId(WebDriver driver, String script) {
        try {
            String versionId = getJSExecutor(driver).executeScript(script).toString();
            if (StringUtils.equals(versionId, "undefined")) {
                return null;
            }
            return versionId;
        } catch (JavascriptException | NullPointerException e) {
            LOG.info("Returning a null version ID because object does not exist yet for [{}]", script);
            return null;
        }
    }

    /**
     * Expands or triggers an action and attempts to locate WebElement before the time-out period ends.
     * <p>
     * After expandOrTriggerReveal delayClick method used on WebElement locatorIfExpanded.
     * </p>
     * <p>
     *  Given time-out period of 90 seconds
     * </p>.
     *
     * @param listBoxLocator   the locator for the list box element
     * @param locatorIfExpanded   the locator for a WebElement within the list box that indicates it is expanded
     */
    public void expandOrTriggerToClick(By listBoxLocator, By locatorIfExpanded) {
        expandOrTriggerToReveal(listBoxLocator, locatorIfExpanded, Boolean.FALSE);
        delayClick(locatorIfExpanded, SHORT_INTERVAL);
    }

    /**
     * Expands or triggers an action and attempts to locate WebElement before the time-out period ends.
     * <p>
     * After expandOrTriggerReveal delayClick method used on WebElement locatorIfExpanded.
     * </p>
     * <p>
     *  Given time-out period of 90 seconds
     * </p>.
     *
     * @param listBoxLocator   the locator for the list box element
     * @param locatorIfExpanded   the locator for a WebElement within the list box that indicates it is expanded
     * @param refreshOption   indicates whether to refresh the page before clicking the element
     */
    public void expandOrTriggerToClick(By listBoxLocator, By locatorIfExpanded, Boolean refreshOption) {
        expandOrTriggerToReveal(listBoxLocator, locatorIfExpanded, refreshOption);
        delayClick(locatorIfExpanded, SHORT_INTERVAL);
    }

    /**
     * Expands or triggers an action and attempts to locate WebElement before the time-out period ends.
     * <p>
     * After expandOrTriggerReveal delayClick method used on WebElement locatorIfExpanded then locatorIfExpandedSecond
     * </p>
     * <p>
     *  Given time-out period of 90 seconds
     * </p>.
     * @param listBoxLocator
     * @param locatorIfExpanded
     * @param locatorIfExpandedSecond
     */
    public void expandOrTriggerToClick(By listBoxLocator, By locatorIfExpanded, By locatorIfExpandedSecond) {
        expandOrTriggerToReveal(listBoxLocator, locatorIfExpanded, Boolean.FALSE);
        delayClick(locatorIfExpanded, SHORT_INTERVAL);
        delayClick(locatorIfExpandedSecond, SHORT_INTERVAL);
    }

    /**
     * Expands or triggers an action and attempts to locate WebElement before the time-out period ends.
     * <p>
     * Given time-out period of 90 seconds
     * </p>.
     *
     * @param listBoxLocator The locator of the WebElement that needs to be expanded or triggered.
     * @param locatorIfExpanded The locator of the WebElement that should be revealed when the {@code listBoxLocator} is expanded
     */
    public void expandOrTriggerToReveal(By listBoxLocator, By locatorIfExpanded) {
        expandOrTriggerToReveal(listBoxLocator, locatorIfExpanded, Boolean.FALSE);
    }

    /**
     * Expands or triggers an action and attempts to locate WebElement before the time-out period ends.
     * <p>
     * Given time-out period of 90 seconds
     * </p>.
     *
     * @param listBoxLocator      the locator of the list box element
     * @param locatorIfExpanded   the locator of the WebElement that should be visible if the list box is expanded
     * @param refreshOption       a boolean flag indicating whether to refresh the page if the WebElement is not visible
     */
    public void expandOrTriggerToReveal(By listBoxLocator, By locatorIfExpanded, Boolean refreshOption) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(TIMEOUT_COMPLETE_TASK),
                Duration.ofMillis(LONGEST_POLL_WAIT_MS)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                if (refreshOption && !checkIfVisible(listBoxLocator, TIME_OUT_SHORT)) {
                    getDriver().navigate().refresh();
                    expectLoaderIconToDisappear();
                }
                delayClick(listBoxLocator, SHORT_INTERVAL);
                Boolean locatorIfExpandedVisibility = checkIfVisible(locatorIfExpanded);
                LOG.info("Visibility is [{}] for [{}]", locatorIfExpandedVisibility,
                        locatorIfExpanded.toString());
                if (refreshOption && !locatorIfExpandedVisibility) {
                    getDriver().navigate().refresh();
                    expectLoaderIconToDisappear();
                }
                return locatorIfExpandedVisibility;
            }
        });
    }

    /**
     * Adds the specified number of days to the current calendar date.
     * <p>
     * Uses provided pattern to format and convert to String
     * </p>
     *
     * @param format the format of the date string
     * @param numberOfDays the number of days to be added to the current date
     * @return the updated calendar date as a string in the specified format
     */
    public String addCalendarDate(String format, int numberOfDays) {
        String date = new SimpleDateFormat(format).format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(Constants.UTC));
        try {
            c.setTime(sdf.parse(date));
        } catch (ParseException e) {
            LOG.info(LogException.errorMessage(e));
        }
        c.add(Calendar.DATE, numberOfDays);
        return sdf.format(c.getTime());
    }

    /**
     * Adds the specified number of days to the current calendar date and time.
     * <p>
     * Converts the TimeStamp object to UTC time zone and then formats to String
     * </p>
     *
     * @param numberOfDays the number of days to add
     * @return the updated calendar date and time as a string formatted in "MM/dd/yyyy hh:mm a" format
     */
    public String addCalendarDateTime(int numberOfDays) {
        String timeStamp = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(new Timestamp(new Date().getTime()));
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(Constants.UTC));
        try {
            c.setTimeInMillis(sdf.parse(timeStamp).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, numberOfDays);
        return sdf.format(c.getTime().getTime());
    }

    /**
     * Expects the loader icon to disappear within a certain time-out period.
     * <p>
     * Time-out period of 90 seconds is given
     * </p>
     *
     * @see #expectLoaderIconToDisappear(long)
     */
    public void expectLoaderIconToDisappear() {
        expectLoaderIconToDisappear(TIMEOUT_COMPLETE_TASK);
    }

    /**
     * Waits for the loader icon to disappear from the UI before a given time-out period ends.
     *
     * @param timeOut the maximum time to wait in seconds
     */
    public void expectLoaderIconToDisappear(int timeOut) {
        acceptAlertForOutdatedBrowser();
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut)).until((ExpectedCondition<Boolean>) driver -> {
                if (getJSExecutor().executeScript(Constants.DOCUMENT_READY_STATE)
                        .equals(Constants.COMPLETE)) {
                    LOG.info(
                            "document.readyState is complete! Waiting for the Loader Icon to be cleared in the UI.....");
                    return Boolean.TRUE;
                }
                LOG.warn("document.readyState not complete; waiting.....");
                return Boolean.FALSE;
            });
        } catch (TimeoutException e) {
            Assert.fail("Timed out waiting for document.readyState to be complete");
        }
        final String loaderIconFailMsg = String.format("Timed out waiting for the Loader Icon to Disappear [%s]",
                loaderIcon);
        if (!checkIfInvisible(loaderIcon, timeOut)) {
            try {
                scrollIntoView(loaderIcon, timeOut);
                Assert.fail(loaderIconFailMsg);
            } catch (TimeoutException e) {
                Assert.assertFalse(checkIfInvisible(loaderIcon, timeOut), loaderIconFailMsg);
            }
        }
        acceptAlertForOutdatedBrowser();
    }

    /**
     * Converts a given String representation of a date to a LocalDate object.
     *
     * @param inputDate the string representation of the date
     * @return the LocalDate representation of the given input date
     */
    public LocalDate convertToLocalDate(String inputDate) {
        return OffsetDateTime.parse(inputDate).atZoneSameInstant(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Adds the specified number of years to the given LocalDate.
     *
     * @param date The LocalDate to which the years need to be added. Must not be null.
     * @param years The number of years to add to the LocalDate. Must be a positive or negative integer.
     * @return A new LocalDate object that represents the resulting date after adding the years.
     */
    public LocalDate addYearsToLocalDate(LocalDate date, long years) {
        return date.plusYears(years);
    }

    /**
     * Creates a {@link Callable} that waits until the specified maximum interval is reached.
     *
     * @param maxInterval the maximum interval to wait for
     * @return a {@link Callable} that returns {@code true} when the maximum interval is reached,
     *      {@code false} otherwise
     */
    private Callable<Boolean> waitInterval(int maxInterval) {
        return new Callable<>() {
            int counter = 0;

            public Boolean call() {
                counter++;
                return (counter == maxInterval);
            }
        };
    }

    /**
     * Waits for a specified delay interval before continuing execution.
     *
     * @param maxInterval the maximum delay interval in milliseconds
     */
    private void awaitDelay(int maxInterval) {
        try {
            Awaitility.with().pollDelay(POLL_NO_INIT_DELAY, TimeUnit.SECONDS)
                    .pollInterval(Duration.ofMillis(POLL_WAIT_QUICK_MS)).await()
                    .atMost(Duration.ofSeconds(TIME_OUT_SECONDS)).until(waitInterval(maxInterval));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Waits for a specified period before attempting to click a WebElement before a given time-out period ends.
     * <p>
     * Time-out of 10 seconds is given to wait for the WebElement to become available for interaction
     * </p>
     *
     * @param by the locator used to identify the web element
     * @param delayInterval the delay interval in milliseconds before the click action is performed
     */
    public void delayClick(By by, int delayInterval) {
        awaitDelay(delayInterval);
        click(by, TIME_OUT_FOR_PAGE_LOAD);
    }

    /**
     * Waits for a specified period before attempting to click a WebElement before a given time-out period ends.
     * <p>
     * Time-out of 10 seconds is given to wait for the WebElement to become available for interaction
     * </p>
     *
     * @param by the locator of the WebElement to be clicked
     * @param timeOut the maximum amount of time to wait for the WebElement to be clickable, in seconds
     * @param delayInterval the duration to delay the click action before performing it, in milliseconds
     */
    public void delayClick(WebElement element, int delayInterval) {
        awaitDelay(delayInterval);
        click(element, TIME_OUT_FOR_PAGE_LOAD);
    }

    /**
     * Waits for a specified period before attempting to click a WebElement before a given time-out period ends.
     *
     * @param by the locator of the WebElement to be clicked
     * @param timeOut the maximum amount of time to wait for the WebElement to be clickable, in seconds
     * @param delayInterval the duration to delay the click action before performing it, in milliseconds
     */
    public void delayClick(By by, int timeOut, int delayInterval) {
        awaitDelay(delayInterval);
        click(by, timeOut);
    }

    /**
     * Waits for a specified period before attempting to click a WebElement before a given time-out period ends.
     * <p>
     * Time-out of 10 seconds is given to wait for the WebElement to become available for interaction
     * </p>
     *
     * @param element the WebElement to be clicked
     * @param timeOut the maximum time to wait for the element to be clickable, in milliseconds
     * @param delayInterval the interval to wait before clicking the element, in milliseconds
     */
    public void delayClick(WebElement element, int timeOut, int delayInterval) {
        awaitDelay(delayInterval);
        click(element, timeOut);
    }

    /**
     * Waits for a specified period before attempting to send keys to the WebElement before a given time-out period ends.
     * <p>
     * Time-out of 10 seconds is given to wait for the WebElement to become available for interaction
     * </p>
     *
     * @param by the locator used to identify the element
     * @param input the character sequence to be sent
     * @param delayInterval the delay in milliseconds between each character sent
     */
    public void delaySendKeys(By by, String input, int delayInterval) {
        delaySendKeys(by, input, TIME_OUT_SECONDS, delayInterval);
    }

    /**
     * Waits for a specified period before attempting to send keys to the WebElement before a given time-out period ends.
     *
     * @param by            the locator of the WebElement to send keys to
     * @param input         the text to be sent to the element
     * @param timeOut the maximum time to wait for the WebElement to be visible before sending keys
     * @param delayInterval the delay interval in milliseconds before sending keys
     */
    public void delaySendKeys(By by, String input, int timeOut, int delayInterval) {
        awaitDelay(delayInterval);
        LOG.info("Sending Keys to Element {} with values {}", by, input);
        WebElement element = getElement(by);
        scrollIntoView(element, timeOut);
        element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        element.sendKeys(input);
    }

    /**
     * Delays the sending of keys to a WebElement and triggers a blur event after a specified interval.
     * <p>
     * Uses JS executor to execute JS blur method to remove keyboard focus from WebElement
     * </p>
     * <p>
     * Time-out of 10 seconds is given to wait for the WebElement to become available for interaction
     * </p>
     *
     * @param element          The WebElement to send keys to.
     * @param input            The input (keys) to be sent.
     * @param delayInterval    The delay interval (in milliseconds) before sending the keys and triggering the blur event.
     */
    public void delaySendKeysBlur(By by, String input, int delayInterval) {
        delaySendKeysBlur(by, input, TIME_OUT_SECONDS, delayInterval);
    }

    /**
     * Delays the sending of keys to a WebElement and triggers a blur event after a specified interval.
     * <p>
     * Uses JS executor to execute JS blur method to remove keyboard focus from WebElement
     * </p>
     *
     * @param element          The WebElement to send keys to.
     * @param input            The input (keys) to be sent.
     * @param timeOut    The maximum time (in seconds) to wait for the WebElement to become available for interaction
     * @param delayInterval    The delay interval (in milliseconds) before sending the keys and triggering the blur event.
     */
    public void delaySendKeysBlur(By by, String input, int timeOut, int delayInterval) {
        awaitDelay(delayInterval);
        sendKeysBlur(by, input, timeOut);
    }

    /**
     * Delays the sending of keys to a WebElement and triggers a blur event after a specified interval.
     * <p>
     * Uses JS executor to execute JS blur method to remove keyboard focus from WebElement
     * </p>
     * <p>
     * Time-out of 10 seconds is given to wait for the WebElement to become available for interaction
     * </p>
     *
     * @param element          The WebElement to send keys to.
     * @param input            The input (keys) to be sent.
     * @param delayInterval    The delay interval (in milliseconds) before sending the keys and triggering the blur event.
     */
    public void delaySendKeysBlur(WebElement element, String input, int delayInterval) {
        delaySendKeysBlur(element, input, TIME_OUT_SECONDS, delayInterval);
    }

    /**
     * Delays the sending of keys to a WebElement and triggers a blur event after a specified interval.
     * <p>
     * Uses JS executor to execute JS blur method to remove keyboard focus from WebElement
     * </p>
     *
     * @param element          The WebElement to send keys to.
     * @param input            The input (keys) to be sent.
     * @param timeOut    The maximum time (in seconds) to wait for the WebElement to become available for interaction
     * @param delayInterval    The delay interval (in milliseconds) before sending the keys and triggering the blur event.
     */
    public void delaySendKeysBlur(WebElement element, String input, int timeOut, int delayInterval) {
        awaitDelay(delayInterval);
        sendKeysBlur(element, input, timeOut);
    }

    /**
     * Delays the selection of an option in a dropdown by its value by a specified interval.
     *
     * @param by            the locating mechanism to find the dropdown element
     * @param value         the value of the option to be selected
     * @param delayInterval the delay interval in milliseconds
     */
    public void delaySelectByValue(By by, String value, int delayInterval) {
        awaitDelay(delayInterval);
        selectByValue(by, value);
    }

    /**
     * Delays the selection of an option in a dropdown by a specified interval.
     *
     * @param by The locator strategy used to identify the dropdown element.
     * @param text The text of the option to be selected.
     * @param delayInterval The time interval, in milliseconds, to delay the selection.
     */
    public void delaySelectByText(By by, String text, int delayInterval) {
        awaitDelay(delayInterval);
        selectByText(by, text);
    }

    /**
     * Changes the current window to the second window.
     * <p>
     * This method retrieves the current window handle, then retrieves all available window handles.
     * <p>
     * After removing the current window handle from the list. The driver's focus switches windows using remaining windows handle.
     * </p>
     * <p>
     * ArrayList<String> is used due to slight performance advantage over Set when dealing with small Collections.
     * </p>
     *
     */
    public void changeWindow() {
        LOG.info("LOG:..............changing to second window");
        String oldTab = getDriver().getWindowHandle();
        ArrayList<String> newTab = new ArrayList<>(getDriver().getWindowHandles());
        newTab.remove(oldTab);
        getDriver().switchTo().window(newTab.get(0));
    }

    /**
     * Returns the current date in the specified format and time zone.
     *
     * @param dateFormat the format of the date to return
     * @param zone the time zone to use for the date
     * @return the current date in the specified format and time zone
     */
    public String getCurrentDate(String dateFormat, ZoneId zone) {
        return OffsetDateTime.now(zone).format(DateTimeFormatter.ofPattern(dateFormat));
    }

    /**
     * Returns the future date based on the given pattern, number of days in the future, and time zone.
     * <p>
     * Uses specified number of days and the given time zone to get future date.
     * </p>
     *
     * @param pattern the pattern to be used for formatting the date
     * @param daysInFuture the number of days to add to the current date
     * @param zone the time zone to be used for the future date
     * @return the formatted future date as a string
     */
    public String getFutureDate(String pattern, int daysInFuture, ZoneId zone) {
        return getFutureDate(DateTimeFormatter.ofPattern(pattern), daysInFuture, zone);
    }

    /**
     * Returns a future date as a string representation formatted according to the provided pattern,
     * <p>
     * Uses specified number of days and the given time zone to get future date.
     * </p>
     *
     * @param pattern the pattern to be used for formatting the future date (e.g. "yyyy-MM-dd")
     * @param daysInFuture the number of days in the future from the current date
     * @param zone the time zone to be used for calculating the future date
     * @return a string representation of the future date formatted according to the provided pattern
     */
    public String getFutureDate(DateTimeFormatter pattern, int daysInFuture, ZoneId zone) {
        return ZonedDateTime.now(zone).plusDays(daysInFuture).format(pattern);
    }

    /**
     * Retrieves the date in the past based on the specified pattern, number of days, and time zone.
     * <p>
     * Uses specified number of days and the given time zone to get past date.
     * </p>
     * @param pattern the pattern used to format the date
     * @param daysInPast the number of days to go back in the past
     * @param zone the time zone to use for the date calculation
     * @return the formatted date in the past as a string
     */
    public String getPastDate(String pattern, int daysInPast, ZoneId zone) {
        return getPastDate(DateTimeFormatter.ofPattern(pattern), daysInPast, zone);
    }

    /**
     * Returns a past date as a string formatted according to the provided pattern.
     * <p>
     * Uses specified number of days and the given time zone to get past date.
     * </p>
     *
     * @param pattern the date and time format pattern to be used
     * @param daysInPast the number of days in the past from the current date
     * @param zone the time zone to apply when calculating the past date
     * @return a string representation of the past date formatted according to the pattern
     */
    public String getPastDate(DateTimeFormatter pattern, int daysInPast, ZoneId zone) {
        return ZonedDateTime.now(zone).minusDays(daysInPast).format(pattern);
    }

    /**
     * Continuously refreshes the page until the WebElement becomes visible or the maximum time is reached.
     *
     * @param by the locator strategy used to identify the element
     * @param maxTime the maximum time (in seconds) to wait for the WebElement to become visible
     * @param pollInterval the time interval (in milliseconds) between each refresh attempt
     */
    public void refreshUntilElementVisible(final By by, int maxTime, int pollInterval) {
        LOG.info("Waiting for element to be enabled: {}", by.toString());
        new WebDriverWait(getDriver(), Duration.ofSeconds(maxTime), Duration.ofMillis(pollInterval))
                .until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver driver) {
                        try {
                            Boolean result = checkIfVisible(by, TIME_OUT_SHORT);
                            if (Boolean.FALSE.equals(result)) {
                                getDriver().navigate().refresh();
                                expectLoaderIconToDisappear();
                            }
                            return result;
                        } catch (NoSuchElementException e) {
                            LOG.info("No such element. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                            getDriver().navigate().refresh();
                            expectLoaderIconToDisappear();
                        }
                        return Boolean.FALSE;
                    }
                });
    }

    /**
     * Refreshes the page until the specified WebElement is visible.
     *
     * @param by The locator strategy to find the element.
     * @param navigateBy The locator strategy to navigate to the page containing the element.
     * @param maxTime The maximum time, in seconds, to wait for the WebElement to become visible.
     * @param pollInterval The time, in milliseconds, to wait between each check for the element's visibility.
     */
    public void refreshUntilElementVisible(final By by, final By navigateBy, int maxTime, int pollInterval) {
        LOG.info("Waiting for element to be enabled: {}", by.toString());
        new WebDriverWait(getDriver(), Duration.ofSeconds(maxTime), Duration.ofMillis(pollInterval))
                .until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver driver) {
                        try {
                            Boolean result = checkIfVisible(by, TIME_OUT_SHORT);
                            if (Boolean.FALSE.equals(result)) {
                                getDriver().navigate().refresh();
                                expectLoaderIconToDisappear();
                                click(navigateBy);
                            }
                            return result;
                        } catch (NoSuchElementException | TimeoutException | ConditionTimeoutException e) {
                            LOG.info("No such element. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                            getDriver().navigate().refresh();
                            expectLoaderIconToDisappear();
                        }
                        return Boolean.FALSE;
                    }
                });
    }

    /**
     * Returns the specified attribute of a WebElement within a given time-out period.
     *
     * @param by the locator used to locate the element.
     * @param attribute The name of the attribute whose value is to be retrieved.
     * @return The value of the specified attribute as a string.
     */
    public String getAttribute(By by, String attribute) {
        return getAttribute(by, TIME_OUT_SECONDS, attribute);
    }

    /**
     * Returns the specified attribute of a WebElement within a given time-out period.
     *
     * @param by the locator to locate the element
     * @param timeOut the maximum time to wait for the WebElement to be visible, in seconds
     * @param attribute the name of the attribute to retrieve
     * @return The value of the specified attribute as a string.
     */
    public String getAttribute(By by, int timeOut, String attribute) {
        expectElementToBeVisible(by, timeOut);
        return getDriver().findElement(by).getAttribute(attribute);
    }

    /**
     * Returns the specified attribute of a WebElement within a given time-out period.
     * <p>
     * Time-out period of 10 seconds is given
     * </p>
     *
     * @param element The WebElement from which to retrieve the attribute value.
     * @param attribute The name of the attribute to retrieve.
     * @return The value of the specified attribute as a string.
     */
    public String getAttribute(WebElement element, String attribute) {
        return getAttribute(element, TIME_OUT_SECONDS, attribute);
    }

    /**
     * Returns the specified attribute of a WebElement within a given time-out period.
     *
     * @param element The WebElement from which to retrieve the attribute value.
     * @param timeOut The maximum number of seconds to wait for the WebElement to be visible.
     * @param attribute The name of the attribute whose value is to be retrieved.
     * @return The value of the specified attribute as a string.
     * @throws TimeoutException If the WebElement is not visible within the specified time-out duration.
     */
    public String getAttribute(WebElement element, int timeOut, String attribute) {
        expectElementToBeVisible(element, timeOut);
        return element.getAttribute(attribute);
    }

    /**
     * Checks if a WebElement has the specified attribute.
     * <p>
     * Time-out period of 10 seconds is given
     * </p>
     * @param by       the locator strategy used to find the element
     * @param attribute the name of the attribute to check
     * @return true if the WebElement has the specified attribute, false otherwise
     */
    public boolean hasAttribute(By by, String attribute) {
        return getAttribute(by, TIME_OUT_SECONDS, attribute) != null;
    }

    /**
     * Checks if the WebElement has the specified attribute within a given time-out period.
     *
     * @param by The locator strategy to find the element.
     * @param timeOut The maximum time in seconds to wait until the WebElement is visible.
     * @param attribute The attribute to check for.
     * @return true if the WebElement has the specified attribute, false otherwise.
     */
    public boolean hasAttribute(By by, int timeOut, String attribute) {
        expectElementToBeVisible(by, timeOut);
        return getDriver().findElement(by).getAttribute(attribute) != null;
    }

    /**
     * This method checks whether the given WebElement has the specified attribute.
     * <p>
     * Time-out period of 10 seconds is given
     * </p>
     *
     * @param element The WebElement to check for the attribute.
     * @param attribute The attribute to check for.
     * @return true if the WebElement has the attribute, false otherwise.
     */
    public boolean hasAttribute(WebElement element, String attribute) {
        return getAttribute(element, TIME_OUT_SECONDS, attribute) != null;
    }

    /**
     * Checks if the given WebElement has the specified attribute within the given time-out period.
     *
     * @param element The WebElement to check the attribute for.
     * @param timeOut The time-out period in seconds within which to wait for the WebElement to be visible.
     * @param attribute The attribute to check for.
     * @return true if the WebElement has the specified attribute, false otherwise.
     */
    public boolean hasAttribute(WebElement element, int timeOut, String attribute) {
        expectElementToBeVisible(element, timeOut);
        return element.getAttribute(attribute) != null;
    }

    /**
     * Calculates the added amount between two amounts using scale and rounding mode.
     * <p>
     * Scale is 2 to match USD
     * </p>
     * <p>
     * Scale is used to express BigDecimal with a defined number of decimal places
     * </p>
     * <p>
     *      ex: setScale(1, RoundingMode.CEILING)
     *          31452678569.24321 -> 31452678569.3
     * </p>
     * <p>
     * Uses HALF_UP rounding mode defined as static final variable
     * </p>
     * @param amount1 the first number to be added
     * @param amount2 the second number to be added
     * @return the added amount for USD currency
     */
    public double calculateAddedAmountForUsd(double amount1, double amount2) {
        return calculateAddedAmount(amount1, amount2, USD_DECIMAL_SCALE, roundingMode);
    }

    /**
     * Calculates the added amount between two amounts using scale and rounding mode.
     * <p>
     * Scale is used to express BigDecimal with a defined number of decimal places
     * </p>
     * <p>
     *      ex: setScale(1, RoundingMode.CEILING)
     *          31452678569.24321 -> 31452678569.3
     * </p>
     * <p>
     * Uses HALF_UP rounding mode defined as static final variable
     * </p>
     * @param amount1       the first number
     * @param amount2       the second number
     * @param scale         the number of decimal places to round to
     * @param roundingMode  the rounding mode to be used
     * @return double       the amount after adding two numbers, rounded to the specified scale using the specified rounding mode
     */
    public double calculateAddedAmount(double amount1, double amount2, int scale, RoundingMode roundingMode) {
        BigDecimal total = new BigDecimal(amount1).add(new BigDecimal(amount2));
        return total.setScale(scale, roundingMode).doubleValue();
    }

    /**
     * Calculates the subtracted amount between two amounts using scale and rounding mode.
     * <p>
     * Scale is 2 to match USD
     * </p>
     * <p>
     * Scale is used to express BigDecimal with a defined number of decimal places
     * </p>
     * <p>
     *      ex: setScale(1, RoundingMode.CEILING)
     *          31452678569.24321 -> 31452678569.3
     * </p>
     * <p>
     * Uses HALF_UP rounding mode defined as static final variable
     * </p>
     * @param amount1 the first number
     * @param amount2 the second number
     * @return the subtracted amount
     */
    public double calculateSubtractedAmountForUsd(double amount1, double amount2) {
        return calculateSubtractedAmount(amount1, amount2, USD_DECIMAL_SCALE, roundingMode);
    }

    /**
     * Calculates the subtracted amount between two amounts using scale and rounding mode.
     * <p>
     * Scale is used to express BigDecimal with a defined number of decimal places
     * </p>
     * <p>
     *      ex: setScale(1, RoundingMode.CEILING)
     *          31452678569.24321 -> 31452678569.3
     * </p>
     * <p>
     * Uses HALF_UP rounding mode defined as static final variable
     * </p>
     * @param amount1         the first input number
     * @param amount2         the second input number
     * @param scale
     **/
    public double calculateSubtractedAmount(double amount1, double amount2, int scale, RoundingMode roundingMode) {
        BigDecimal total = new BigDecimal(amount1).subtract(new BigDecimal(amount2));
        return total.setScale(scale, roundingMode).doubleValue();
    }

    /**
     * Calculates the multiplied amount using the provided rate, amount, scale and rounding mode.
     * <p>
     * Scale is 2 to match USD
     * </p>
     * <p>
     * Scale is used to express BigDecimal with a defined number of decimal places
     * </p>
     * <p>
     *      ex: setScale(1, RoundingMode.CEILING)
     *          31452678569.24321 -> 31452678569.3
     * </p>
     * <p>
     * Uses HALF_UP rounding mode defined as static final variable
     * </p>
     * @param amount1 The first number to be subtracted
     * @param amount2 The second number to subtract from the first
     * @return The subtracted amount, rounded to the nearest USD amount and never negative
     */
    public double calculateSubtractedAmountMinimumZeroForUsd(double amount1, double amount2) {
        return calculateSubtractedAmountMinimumZero(amount1, amount2, USD_DECIMAL_SCALE, roundingMode);
    }

    /**
     * Calculates the multiplied amount using the provided rate, amount, scale and rounding mode.
     * Scale is 2 to match USD
     * Scale is used to express BigDecimal with a defined number of decimal places
     * <p>
     *      ex: setScale(1, RoundingMode.CEILING)
     *          31452678569.24321 -> 31452678569.3
     * </p>
     * <p>
     * Uses HALF_UP rounding mode defined as static final variable
     * </p>
     * @param amount1 the first number
     * @param amount2 the second number
     * @param scale the scale used for rounding the result
     * @param roundingMode the rounding mode used for rounding the result
     * @return the subtracted amount between amount1 and amount2, with a minimum of zero,
     *         rounded to the specified scale and rounding mode
     */
    public double calculateSubtractedAmountMinimumZero(double amount1, double amount2, int scale, RoundingMode roundingMode) {
        BigDecimal total = new BigDecimal(amount1).subtract(new BigDecimal(amount2));
        if (total.doubleValue() < 0) {
            return 0;
        } else {
            return total.setScale(scale, roundingMode).doubleValue();
        }
    }

    /**
     * Calculates the multiplied amount using the provided rate, amount, scale and rounding mode.
     * Scale is 2 to match USD
     * Scale is used to express BigDecimal with a defined number of decimal places
     * <p>
     *      ex: setScale(1, RoundingMode.CEILING)
     *          31452678569.24321 -> 31452678569.3
     * </p>
     * <p>
     * Uses HALF_UP rounding mode defined as static final variable
     * </p>
     *
     * @param rate to be used for multiplication
     * @param amount1 the number to be multiplied with the rate
     * @return the multiplied amount in USD
     */
    public double calculateMultipliedAmountForUsd(double rate, double amount1) {
        return calculateMultipliedAmount(rate, amount1, USD_DECIMAL_SCALE, roundingMode);
    }

    /**
     * Calculates the multiplied amount using the provided rate, amount, scale and rounding mode.
     * Scale is used to express BigDecimal with a defined number of decimal places
     * <p>
     *      ex: setScale(1, RoundingMode.CEILING)
     *          31452678569.24321 -> 31452678569.3
     * </p>
     * <p>
     * Uses HALF_UP rounding mode defined as static final variable
     * </p>
     *
     * @param rate The rate to be used for multiplication
     * @param amount1 The number to be multiplied
     * @param scale The scale of the result
     * @param roundingMode The rounding mode to be applied
     * @return The multiplied amount with the specified scale and rounding mode
     */
    public double calculateMultipliedAmount(double rate, double amount1, int scale, RoundingMode roundingMode) {
        BigDecimal total = new BigDecimal(rate).multiply(new BigDecimal(amount1))
                .divide(new BigDecimal(100), BasePage.roundingMode).setScale(scale, roundingMode);
        return total.setScale(scale, roundingMode).doubleValue();
    }

    /**
     * Truncates the decimal places of a double value.
     *
     * @param value   the double value to be truncated
     * @param places  the number of decimal places to truncate
     * @return the truncated double value
     */
    public double truncateDoubleNumber(double value, int places) {
        double multiplier = Math.pow(10, places);
        return Math.floor(multiplier * value) / multiplier;
    }

    /**
     * Rounds a Double value to the specified number of decimal places.
     * Uses HALF_UP rounding mode defined as static final variable
     *
     * @param val    the Double value to be rounded.
     * @param digit  the number of decimal places to round to.
     * @return the rounded Double value.
     */
    public double doubleRound(Double val, int digit) {
        return new BigDecimal(val.toString()).setScale(digit, roundingMode).doubleValue();
    }

    /**
     * Verifies the values of a dropdown against the given array of values.
     *
     * @param array          The array containing the expected values of the dropdown.
     * @param locator        The locator to identify the dropdown element.
     */
    public void verifyDropDownValues(JsonArray array, By locator) {
        Select select = new Select(getElement(locator));
        List<WebElement> dropDownValues = select.getOptions();
        for (var i = 0; i < dropDownValues.size(); i++) {
            LOG.info(dropDownValues.get(i).getText() + "......" + array.get(i).getAsString());
            Assert.assertEquals(dropDownValues.get(i).getText(), array.get(i).getAsString());
        }
    }
// TODO update or remove
//    public void validateDropDownOptionSelected(By optionLocator, String dropDownOption) {
//        LOG.info("Waiting for element to be Selected: {}", optionLocator.toString());
//        WebElement element = getElement(optionLocator);
//        Assert.assertEquals(element.isSelected(), dropDownOption + "Not Selected");
//    }

    /**
     * Verifies that the given text does not exist on the current page.
     *
     * @param text the text to be verified
     */
    public void verifyTextDoesNotExistsOnPage(String text) {
        LOG.info("Verifying that {} does not exists", text);
        Assert.assertFalse(getDriver().getPageSource().contains(text), text + "Does Exists on the page");
    }

    /**
     * Screens the webpage for Section 508 violations.
     * <p>
     * Section 508 refers to the requirements that federal agencies to make their electronic and information technology accessible to people with disabilities.
     * </p>
     * <p>
     * Uses the Axe java library for analyzing the webpage for violations against the WCAG 2.0-AA standard to achieve Section 508 compliance.
     * </p>
     * It logs the violations and passes found, and takes a screenshot if any violations are found.
     *
     * @throws AssertionError if any enforced violations are found
     */
    public void screenFor508Violations() {
        // As of 2018, Section 508 matches the WCAG 2.0 AA standard
        Results results = new AxeBuilder().withTags(Arrays.asList("wcag2a", "wcag2aa", "section508"))
                .analyze(getDriver());
        List<Rule> violations = results.getViolations();
        results.getPasses().forEach((Rule pass) -> LOG.debug("Evaluated and passed: [{}: {}] tags: {}", pass.getId(),
                pass.getDescription(), pass.getTags()));
        if (!violations.isEmpty()) {
            List<Rule> enforcedViolations = new ArrayList<>();
            for (Rule violation : violations) {
                if (Arrays.asList("label", "color-contrast", "duplicate-id", "duplicate-id-active", "duplicate-id-aria",
                                "aria-required-parent", "aria-valid-attr-value", "list", "image-alt")
                        .contains(violation.getId())) {
                    TestContext.getScenario()
                            .log(String.format("508 rule violation for id '%s' warning: [%s] tags: %s",
                                    violation.getId(), violation.getDescription(), violation.getTags()));
                } else {
                    LOG.error("Evaluated and failed: [{}: {}] tags: {}", violation.getId(),
                            violation.getDescription(), violation.getTags());
                    enforcedViolations.add(violation);
                }
            }
            final byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            TestContext.getScenario().attach(screenshot, "image/png", "Screenshot");
            Assert.assertEquals(enforcedViolations.size(), 0,
                    "[" + enforcedViolations.size() + "] Section 508 violations found\nEvaluated and failed: "
                            + enforcedViolations.toString().replace("Rule{id='", "\nRule{id='"));
        } else {
            LOG.info("No Section 508 violations found for {} [{}]", getDriver().getTitle(),
                    getDriver().getCurrentUrl());
        }
    }

    /**
     * Retrieves the text of the specified WebElement.
     *
     * @param element the WebElement whose text needs to be retrieved
     * @return the text of the WebElement
     */
    public String getText(WebElement element) {
        return getText(element, TIME_OUT_SECONDS);
    }

    /**
     * Retrieves the text of a given WebElement after scrolling it into view.
     * <p>
     * Uses JS executor to execute JS scrollIntoView method
     * </p>
     * @param element             the WebElement to retrieve the text from
     * @param timeOut  the maximum amount of time (in seconds) to wait for the WebElement to be scrolled into view
     * @return the text of the web element
     */
    public String getText(WebElement element, int timeOut) {
        scrollIntoView(element, timeOut);
        return element.getText();
    }

    /**
     * Returns a list of text values from the WebElements located by single locator.
     *
     * @param by the By object defining the WebElements to retrieve text values from
     * @return a list of text values
     */
    public List<String> getTexts(By by) {
        return getTexts(by, TIME_OUT_SECONDS);
    }

    /**
     * Retrieves the texts of the visible WebElements identified by single locator.
     *
     * @param by              the By locator used to identify the WebElements.
     * @param timeOutInSeconds the maximum amount of time to wait for the WebElements to become visible.
     * @return a list of String objects representing the texts of the visible WebElements.
     */
    public List<String> getTexts(By by, int timeOutInSeconds) {
        List<String> texts = new ArrayList<>();
        scrollIntoView(by, timeOutInSeconds);
        for (WebElement element : getElements(by, timeOutInSeconds)) {
            if (checkIfVisible(element, TIME_OUT_QUICK)) {
                texts.add(getText(element));
            }
        }
        return texts;
    }
}
