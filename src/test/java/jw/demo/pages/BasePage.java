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

@SuppressWarnings({"Convert2Lambda", "CallToPrintStackTrace"})
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
    protected final String attachmentTableCommon = "";

    // TODO adjust rounding mode depending on project AC if currency is involved otherwise remove
    protected static final RoundingMode currencyRoundingType = RoundingMode.HALF_EVEN;

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

    public void saveDataToScenarioContextAndData(String data) {
        getScenarioData().addProperty(Constants.DATA, data);
        LOG.info("Grant id - [{}]", data);
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page
     * and visible. Visibility means that the element is not only displayed but also
     * has a height and width that is greater than 0.
     * <p>
     * A default timeout of 10 seconds is provided and a
     * TimeoutException/ConditionTimeoutException will be thrown once the time out
     * is passed.
     *</p>
     * @param by  locator used to find the element
     */
    public void expectElementToBeVisible(By by) {
        expectElementToBeVisible(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that an element is either invisible or not
     * present on the DOM.
     * <p>
     * A default timeout of 10 seconds is provided and a
     * TimeoutException/ConditionTimeoutException will be thrown once the time out
     * is passed.
     *</p>
     * @param by   locator used to find the element
     */
    public void expectElementToBeInvisible(By by) {
        expectElementToBeInvisible(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking an element is visible and enabled such that you
     * can click it.
     * <p>
     * A default timeout of 10 seconds is provided and a
     * TimeoutException/ConditionTimeoutException will be thrown once the time out
     * is passed.
     *
     * @param by   locator used to find the element
     */
    public void expectElementToBeClickable(By by) {
        expectElementToBeClickable(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page
     * and visible. Visibility means that the element is not only displayed but also
     * has a height and width that is greater than 0.
     * <p>
     * A TimeoutException/ConditionTimeoutException will be thrown once the time out
     * is passed.
     *
     * @param by              locator used to find the element
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     */
    public void expectElementToBeVisible(By by, int timeOutInSecs) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs))
                .until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    /**
     * An expectation for checking that an element is either invisible or not
     * present on the DOM.
     * <p>
     * A TimeoutException/ConditionTimeoutException will be thrown once the time out
     * is passed.
     *
     * @param by              locator used to find the element
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     */
    public void expectElementToBeInvisible(By by, int timeOutInSecs) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs))
                .until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    /**
     * An expectation for checking an element is visible and enabled such that you
     * can click it.
     * <p>
     * A TimeoutException/ConditionTimeoutException will be thrown once the time out
     * is passed.
     *
     * @param by              locator used to find the element
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     */
    public void expectElementToBeClickable(By by, int timeOutInSecs) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs))
                .until(ExpectedConditions.elementToBeClickable(by));
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page
     * and visible. Visibility means that the element is not only displayed but also
     * has a height and width that is greater than 0.
     * <p>
     * A default timeout of 10 seconds is provided and a
     * TimeoutException/ConditionTimeoutException will be thrown once the time out
     * is passed.
     *
     * @param element - element the WebElement
     */
    public void expectElementToBeVisible(WebElement element) {
        expectElementToBeVisible(element, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that an element is either invisible or not
     * present on the DOM.
     * <p>
     * A default timeout of 10 seconds is provided
     * TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     *
     * @param element - element the WebElement
     */
    public void expectElementToBeInvisible(WebElement element) {
        expectElementToBeInvisible(element, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking an element is visible and enabled such that you
     * can click it.
     * <p>
     * A default timeout of 10 seconds is provided and a
     * TimeoutException/ConditionTimeoutException will be thrown once the time out
     * is passed.
     *
     * @param element - element the WebElement
     */
    public void expectElementToBeClickable(WebElement element) {
        expectElementToBeClickable(element, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page
     * and visible. Visibility means that the element is not only displayed but also
     * has a height and width that is greater than 0.
     * <p>
     * A TimeoutException/ConditionTimeoutException will be thrown once the time out
     * is passed.
     *
     * @param element       - element the WebElement
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     */
    public void expectElementToBeVisible(WebElement element, int timeOutInSecs) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs))
                .until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * An expectation for checking that an element is either invisible or not
     * present on the DOM.
     * <p>
     * A TimeoutException/ConditionTimeoutException will be thrown once the time out
     * is passed.
     *
     * @param element       - element the WebElement
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     */
    public void expectElementToBeInvisible(WebElement element, int timeOutInSecs) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs))
                .until(ExpectedConditions.invisibilityOf(element));
    }

    /**
     * An expectation for checking an element is visible and enabled such that you
     * can click it.
     * <p>
     * A TimeoutException/ConditionTimeoutException will be thrown once the time out
     * is passed.
     *
     * @param element       - element the WebElement
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     */
    public void expectElementToBeClickable(WebElement element, int timeOutInSecs) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    public void expectElementToContainAttribute(WebElement element, String attribute, String attributeValue) {
        expectElementToContainAttribute(element, attribute, attributeValue, TIME_OUT_SECONDS);
    }

    public void expectElementToContainAttribute(WebElement element, String attribute, String attributeValue,
                                                int timeOutInSecs) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs), Duration.ofMillis(POLL_WAIT_MS))
                .until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver driver) {
                        try {
                            expectElementToBeVisible(element, timeOutInSecs);
                            return element.getAttribute(attribute).contains(attributeValue);
                        } catch (StaleElementReferenceException e) {
                            return Boolean.FALSE;
                        }
                    }
                });
    }

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

    public void scrollIntoView(By by) {
        scrollIntoView(by, TIME_OUT_SECONDS);
    }

    public void scrollIntoView(By by, int timeOutInSecs) {
        expectElementToBeVisible(by, timeOutInSecs);
        getJSExecutor().executeScript(Constants.SCROLL_INTO_VIEW, getDriver().findElement(by));
    }

    public void scrollIntoView(WebElement element) {
        scrollIntoView(element, TIME_OUT_SECONDS);
    }

    public void scrollIntoView(WebElement element, int timeOutInSecs) {
        expectElementToBeVisible(element, timeOutInSecs);
        getJSExecutor().executeScript(
                "arguments[0].scrollIntoView({behavior: 'auto', block: 'center', inline: 'nearest'});", element);
    }

    public void scrollIntoViewByText(String text) {
        scrollIntoViewByText(text, TIME_OUT_SECONDS);
    }

    public void scrollIntoViewByText(String text, int timeOutInSecs) {
        List<WebElement> multipleGenericTextLocators = getElements(By.xpath("//*[contains(text(),'" + text + "')]"));
        multipleGenericTextLocators.forEach(currentWebElement -> {
            if (checkIfVisible(currentWebElement)) {
                scrollIntoView(currentWebElement, timeOutInSecs);
            }
        });
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page
     * and visible. Visibility means that the element is not only displayed but also
     * has a height and width that is greater than 0.
     *
     * @param by   locator used to find the element
     * @returns The result is returned or false is returned if 10 seconds passes
     */
    public boolean checkIfVisible(By by) {
        return checkIfVisible(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page
     * and visible. Visibility means that the element is not only displayed but also
     * has a height and width that is greater than 0.
     *
     * @param by              locator used to find the element
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     * @returns The result is returned or false is returned if the time out passes
     */
    public boolean checkIfVisible(By by, int timeOutInSecs) {
        try {
            expectElementToBeVisible(by, timeOutInSecs);
            return Boolean.TRUE;
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page
     * and visible. Visibility means that the element is not only displayed but also
     * has a height and width that is greater than 0.
     *
     * @param element - element the WebElement
     * @returns The result is returned or false is returned if 10 seconds passes
     */
    public boolean checkIfVisible(WebElement element) {
        return checkIfVisible(element, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page
     * and visible. Visibility means that the element is not only displayed but also
     * has a height and width that is greater than 0.
     *
     * @param element       - element the WebElement
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     * @returns The result is returned or false is returned if the time out passes
     */
    public boolean checkIfVisible(WebElement element, int timeOutInSecs) {
        try {
            expectElementToBeVisible(element, timeOutInSecs);
            return Boolean.TRUE;
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }

    /**
     * An expectation for checking that an element is either invisible or not
     * present on the DOM.
     *
     * @param by   locator used to find the element
     * @returns The result is returned or false is returned if 10 seconds passes
     */
    public boolean checkIfInvisible(By by) {
        return checkIfInvisible(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that an element is either invisible or not
     * present on the DOM.
     *
     * @param by              locator used to find the element
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     * @returns The result is returned or false is returned if the time out passes
     */
    public boolean checkIfInvisible(By by, int timeOutInSecs) {
        try {
            expectElementToBeInvisible(by, timeOutInSecs);
            return Boolean.TRUE;
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }

    /**
     * An expectation for checking that an element is either invisible or not
     * present on the DOM.
     *
     * @param element - element the WebElement
     * @returns The result is returned or false is returned if 10 seconds passes
     */
    public boolean checkIfInvisible(WebElement element) {
        return checkIfInvisible(element, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that an element is either invisible or not
     * present on the DOM.
     *
     * @param element       - element the WebElement
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     * @returns The result is returned or false is returned if the time out passes
     */
    public boolean checkIfInvisible(WebElement element, int timeOutInSecs) {
        try {
            expectElementToBeInvisible(element, timeOutInSecs);
            return Boolean.TRUE;
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page
     * is visible and is Displayed
     * <p>
     * checkIfVisible returns the result of driver.findElement(by) but is Displayed
     * goes further and checks for getDriver().findElement(by).isDisplayed()
     *
     * @param by - element the WebElement
     * @returns The result is returned or false is returned if 10 seconds passes
     */
    public boolean checkIfDisplayed(By by) {
        return checkIfDisplayed(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page
     * is visible and is Displayed
     * <p>
     * checkIfVisible returns the result of driver.findElement(by) but is Displayed
     * goes further and checks for getDriver().findElement(by).isDisplayed()
     *
     * @param by            - element the WebElement
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     * @returns The result is returned or false is returned if the time out passes
     */
    public boolean checkIfDisplayed(By by, int timeOutInSecs) {
        try {
            expectElementToBeVisible(by, timeOutInSecs);
            return getDriver().findElement(by).isDisplayed();
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }

    /**
     * Enhanced isEnabled check
     *
     * @param by
     * @param timeOut
     */
    public boolean checkIfEnabled(By by, int timeOut) {
        try {
            expectElementToBeVisible(by, timeOut);
            return getDriver().findElement(by).isEnabled();
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }

    public boolean checkIfEnabled(By by) {
        return checkIfEnabled(by, TIME_OUT_SECONDS);
    }

    public WebElement getElement(By by) {
        return getElement(by, TIME_OUT_SECONDS);
    }

    public WebElement getElement(By by, int timeOutInSecs) {
        expectElementToBeVisible(by, timeOutInSecs);
        LOG.info("Getting Element {}", by);
        return getDriver().findElement(by);
    }

    public List<WebElement> getElements(By by) {
        return getElements(by, TIME_OUT_SECONDS);
    }

    public List<WebElement> getElements(By by, int timeOutInSecs) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs))
                .until(ExpectedConditions.presenceOfElementLocated(by));
        LOG.info("Getting Elements {}", by);
        return getDriver().findElements(by);
    }

    public void clear(By by) {
        clear(by, TIME_OUT_SECONDS);
    }

    public void clear(By by, int timeOutInSecs) {
        expectElementToBeVisible(by, timeOutInSecs);
        LOG.info("Clearing Element {}", by);
        getDriver().findElement(by).clear();
    }

    public void sendKeys(By by, String input) {
        sendKeys(by, input, TIME_OUT_SECONDS);
    }

    public void sendKeys(By by, String input, int timeOutInSecs) {
        scrollIntoView(by, timeOutInSecs);
        LOG.info("Sending Keys to Element {} with values {}", by, input);
        getDriver().findElement(by).sendKeys(input);
    }

    public void sendKeysBlur(By by, String input) {
        sendKeysBlur(by, input, TIME_OUT_SECONDS);
    }

    public void sendKeysBlur(By by, String input, int timeOutInSecs) {
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs), Duration.ofMillis(POLL_WAIT_MS))
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
            sendKeysBlur(getDriver().findElement(by), input, timeOutInSecs);
        }
    }

    public void sendKeysBlur(WebElement element, String input) {
        sendKeysBlur(element, input, TIME_OUT_SECONDS);
    }

    public void sendKeysBlur(WebElement element, String input, int timeOutInSecs) {
        scrollIntoView(element, timeOutInSecs);
        element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        element.sendKeys(input);
        getJSExecutor().executeScript(Constants.BLUR_ACTIVE_ELEMENT);
    }

    public void sendKeysEnter(By by, String input) {
        sendKeysEnter(by, input, TIME_OUT_SECONDS);
    }

    public void sendKeysEnter(By by, String input, int timeOutInSecs) {
        sendKeysEnter(getDriver().findElement(by), input, timeOutInSecs);
    }

    public void sendKeysEnter(WebElement element, String input) {
        sendKeysEnter(element, input, TIME_OUT_SECONDS);
    }

    public void sendKeysEnter(WebElement element, String input, int timeOutInSecs) {
        scrollIntoView(element, timeOutInSecs);
        element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        element.sendKeys(input);
        element.sendKeys(Keys.ENTER);
    }

    public boolean doElementsExist(By by) {
        return !getDriver().findElements(by).isEmpty();
    }

    public boolean isMessageDisplayed(String message) {
        return checkIfDisplayed(By.xpath("//*[contains(.,'" + message + "')]"), TIME_OUT_SECONDS);
    }

    public void selectByText(By by, String text) {
        selectByText(by, text, TIME_OUT_SECONDS);
    }


    public void selectByText(By by, String text, int timeOutInSecs) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs), Duration.ofMillis(POLL_WAIT_MS))
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

    public void selectByIndex(By by, Integer index) {
        selectByIndex(by, index, TIME_OUT_SECONDS);
    }

    public void selectByIndex(By by, Integer index, int timeOutInSecs) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs), Duration.ofMillis(POLL_WAIT_MS))
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

    public void selectByValue(By by, String value) {
        selectByValue(by, value, TIME_OUT_SECONDS);
    }

    public void selectByValue(By by, String value, int timeOutInSecs) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs), Duration.ofMillis(POLL_WAIT_MS))
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

    public String getTextIfVisible(By by) {
        if (checkIfVisible(by)) {
            return getText(by, TIME_OUT_SECONDS);
        } else {
            return "";
        }
    }

    public String getText(By by) {
        return getText(by, TIME_OUT_SECONDS);
    }

    public String getText(By locator, int timeoutInSecs) {
        try {
            scrollIntoView(locator, timeoutInSecs);
            LOG.info("Getting text for element {}", locator);
            return getDriver().findElement(locator).getText();
        } catch (UnhandledAlertException e) {
            getDriver().navigate().refresh();
            expectLoaderIconToDisappear();
            scrollIntoView(locator, timeoutInSecs);
            LOG.info("Getting text for element {}", locator);
            return getDriver().findElement(locator).getText();
        }
    }

    public String getText(By locator, int index, int timeoutInSecs) {
        scrollIntoView(locator, timeoutInSecs);
        return getDriver().findElements(locator).get(index).getText();
    }

    public int getElementCount(By by) {
        LOG.info("Getting Element {} Size of {}", by, getDriver().findElements(by).size());
        return getDriver().findElements(by).size();
    }

    public void expectElementsToBePopulated(By by, int timeOut, int pollAwaitSeconds) {
        expectElementsToBePopulated(by, timeOut, pollAwaitSeconds, 0);
    }

    public void expectElementsToBePopulated(By by, int timeOutInSecs, int pollAwaitSeconds, int size) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return driver.findElements(by).size() > size;
            }
        });
    }

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
     * Clicks on the element specified by the {@code By} locator if it is displayed within the specified timeout.
     *
     * @param by      The {@code By} locator used to locate the element.
     * @param timeOut The maximum time, in seconds, to wait for the element to be displayed.
     */
    public void clickIfDisplayed(By by, int timeOut) {
        if (checkIfDisplayed(by, timeOut)) {
            click(by, timeOut);
        }
    }

    /**
     * Clicks on an element identified by the given locator if it is displayed on the page.
     *
     * @param by The locator used to identify the element.
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
     * Refreshes the page if the element identified by the given locator is not visible.
     *
     * @param locator the locator used to identify the element
     */
    public void refreshIfLocatorNotVisible(By locator) {
        refreshIfLocatorNotVisible(locator, TIME_OUT_FOR_PAGE_LOAD);
    }

    /**
     * Refreshes the page if the given locator is not visible within the specified timeout period.
     *
     * @param locator The locator to check for visibility.
     * @param timeOutInSecs The timeout period in seconds.
     */
    public void refreshIfLocatorNotVisible(By locator, int timeOutInSecs) {
        if (!checkIfVisible(locator, timeOutInSecs)) {
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
     * Waits until the element identified by the given locator is enabled.
     *
     * @param by           the locator used to find the element
     * @param maxTime      the maximum time to wait for the element to be enabled, in seconds
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
     * An expectation for checking an element is visible and enabled
     * Designed to return true if a WebElement is clickable
     *
     * @param by The By object representing the element to be checked.
     * @return true if the element is clickable, false otherwise.
     */
    public boolean checkIfClickable(By by) {
        return checkIfClickable(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking an element is visible and enabled
     * Designed to return true if a WebElement is clickable
     *
     * @param by locator used to find the element
     * @param timeOutInSecs time limit to wait before throwing an Exception
     * @return boolean The result is returned or false is returned if the time-out passes
     */
    public boolean checkIfClickable(By by, int timeOutInSecs) {
        try {
            (new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs)))
                    .until(ExpectedConditions.elementToBeClickable(by));
            return Boolean.TRUE;
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }

    /**
     * An expectation for checking an element is visible and enabled
     * Designed to return true if a WebElement is clickable
     *
     * @param by locator used to find the element
     * @param index index of the element to be clicked (useful when
     * @param timeOutInSecs time limit to wait before throwing an Exception
     * @return The result is returned or false is returned if the time-out passes
     */
    public boolean checkIfClickable(By by, int index, int timeOutInSecs) {
        LOG.info("Checking if Element {} is clickable", by);
        try {
            (new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs)))
                    .until(ExpectedConditions.elementToBeClickable(getDriver().findElements(by).get(index)));
            return Boolean.TRUE;
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }

    public void switchtoCurrentWindow() {
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

    public boolean getTextContainsToVerify(WebElement locator, String text) {
        return getTextContainsToVerify(locator, text, TIME_OUT_SECONDS);
    }

    public boolean getTextContainsToVerify(WebElement locator, String text, int timeOutInSecs) {
        (new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs)))
                .until(ExpectedConditions.visibilityOfAllElements(locator));
        return locator.getText().contains(text);
    }

    public boolean getTextContainsToVerify(By locator, String text) {
        return getTextContainsToVerify(locator, text, TIME_OUT_SECONDS);
    }

    public boolean getTextContainsToVerify(By locator, String text, int timeOutInSecs) {
        (new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs)))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        return getDriver().findElement(locator).getText().contains(text);
    }

    public void expectTextToUpdateTo(By statusLocator, String expectedStatus) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(TIMEOUT_COMPLETE_TASK),
                Duration.ofSeconds(TIME_OUT_AWAIT_SECONDS)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return getTextContainsToVerify(statusLocator, expectedStatus);
            }
        });
    }

    public boolean getAllTextContainsToVerify(By locator, String text, int timeOutInSecs) {
        (new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs)))
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

    public boolean getTextContainsToVerify(By locator, int index, String text, int timeOutInSecs) {
        (new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs)))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        return getDriver().findElements(locator).get(index).getText().contains(text);
    }

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

    protected void mouseOver(By by) {
        mouseOver(by, TIME_OUT_SECONDS);
    }

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


    public String getHeaderIndex(By headers, String headerTitle) {
        List<String> listofHeaders = getElements(headers, TIME_OUT_SECONDS).stream().map(WebElement::getText)
                .toList();
        int index = listofHeaders.indexOf(headerTitle) + 1;
        return Integer.toString(index);
    }

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

    public void refreshIfLocatorNotDisplayed(By locator) {
        if (!checkIfDisplayed(locator, TIME_OUT_FOR_PAGE_LOAD)) {
            LOG.info("Refreshing the page");
            getDriver().navigate().refresh();
            new WebDriverWait(getDriver(), Duration.ofSeconds(TIME_OUT_FOR_PAGE_LOAD))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
        }
    }


    public String getVersionId() {
        return getVersionId(getDriver(), Constants.CURRENT_TASK_VERSION_ID);
    }

    public String getVersionId(String script) {
        return getVersionId(getDriver(), script);
    }

    public Boolean waitForVersionIdUpdate(String origVersionId, int timeOutInSecs) {
        return waitForVersionIdUpdate(origVersionId, timeOutInSecs, Constants.CURRENT_TASK_VERSION_ID);
    }

    public Boolean waitForVersionIdUpdate(String origVersionId, int timeOutInSecs, String script) {
        LOG.info("Waiting for versionId Update [{}]", script);
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs), Duration.ofMillis(POLL_WAIT_MS))
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
            LOG.warn("Waited {} seconds for versionId Update, continuing... [{}]", timeOutInSecs, script);
            return Boolean.FALSE;
        }
    }

    public void ddClick(By by) {
        ddClick(by, TIME_OUT_DD_SECONDS, Constants.CURRENT_TASK_VERSION_ID);
    }

    public void ddClick(By by, String script) {
        ddClick(by, TIME_OUT_DD_SECONDS, script);
    }

    public void ddClick(By by, int timeOutInSecs) {
        ddClick(by, timeOutInSecs, Constants.CURRENT_TASK_VERSION_ID);
    }

    public void ddClick(By by, int timeOutInSecs, String script) {
        String origVersionId = getVersionId(getDriver(), script);
        LOG.info("origVersionId: {} [{}]", origVersionId, script);
        click(by);
        getJSExecutor().executeScript(Constants.BLUR_ACTIVE_ELEMENT);
        if (!waitForVersionIdUpdate(origVersionId, timeOutInSecs, script))
            TestContext.logToScenario(
                    String.format("DynamicData version update check has failed for %s [%s]", by.toString(), script));
    }

    public void ddClick(WebElement elem) {
        ddClick(elem, TIME_OUT_DD_SECONDS, Constants.CURRENT_TASK_VERSION_ID);
    }

    public void ddClick(WebElement elem, String script) {
        ddClick(elem, TIME_OUT_DD_SECONDS, script);
    }

    public void ddClick(WebElement elem, int timeOutInSecs) {
        ddClick(elem, timeOutInSecs, Constants.CURRENT_TASK_VERSION_ID);
    }

    public void ddClick(WebElement elem, int timeOutInSecs, String script) {
        String origVersionId = getVersionId(getDriver(), script);
        LOG.info("origVersionId: {} [{}]", origVersionId, script);
        click(elem);
        getJSExecutor().executeScript(Constants.BLUR_ACTIVE_ELEMENT);
        if (!waitForVersionIdUpdate(origVersionId, timeOutInSecs, script))
            TestContext.logToScenario(
                    String.format("DynamicData version update check has failed for %s [%s]", elem.toString(), script));
    }

    public void ddSendKeysBlur(By by, String input) {
        ddSendKeysBlur(by, input, TIME_OUT_DD_SECONDS, Constants.CURRENT_TASK_VERSION_ID);
    }

    public void ddSendKeysBlur(By by, String input, String script) {
        ddSendKeysBlur(by, input, TIME_OUT_DD_SECONDS, script);
    }

    public void ddSendKeysBlur(By by, String input, int timeOutInSecs) {
        ddSendKeysBlur(by, input, timeOutInSecs, Constants.CURRENT_TASK_VERSION_ID);
    }

    public void ddSendKeysBlur(By by, String input, int timeOutInSecs, String script) {
        LOG.info("Clearing and ddSending Keys to Element {} with values {} [{}] and then defocusing by sending Blur",
                by, input, script);
        String origVersionId = getVersionId(getDriver(), script);
        LOG.info("origVersionId: {} [{}]", origVersionId, script);
        sendKeysBlur(by, input);
        if (!waitForVersionIdUpdate(origVersionId, timeOutInSecs, script))
            TestContext.logToScenario(
                    String.format("DynamicData version update check has failed for %s [%s]", by.toString(), script));
    }

    public void ddSendKeysBlur(WebElement element, String input) {
        ddSendKeysBlur(element, input, TIME_OUT_DD_SECONDS, Constants.CURRENT_TASK_VERSION_ID);
    }

    public void ddSendKeysBlur(WebElement element, String input, String script) {
        ddSendKeysBlur(element, input, TIME_OUT_DD_SECONDS, script);
    }

    public void ddSendKeysBlur(WebElement element, String input, int timeOutInSecs) {
        ddSendKeysBlur(element, input, timeOutInSecs, Constants.CURRENT_TASK_VERSION_ID);
    }

    public void ddSendKeysBlur(WebElement element, String input, int timeOutInSecs, String script) {
        LOG.info("Clearing and ddSending Keys to Element {} with values {} [{}] and then defocusing by sending Blur",
                element, input, script);
        String origVersionId = getVersionId(getDriver(), script);
        LOG.info("origVersionId: {} [{}]", origVersionId, script);
        sendKeysBlur(element, input, timeOutInSecs);
        if (!waitForVersionIdUpdate(origVersionId, timeOutInSecs, script))
            TestContext.logToScenario(String.format("DynamicData version update check has failed for %s [%s]",
                    element.toString(), script));
    }

    public void ddEnterDate(By dateEntryLocator, String input) {
        ddEnterDate(dateEntryLocator, input, Constants.CURRENT_TASK_VERSION_ID);
    }

    public void ddEnterDate(By dateEntryLocator, String input, String script) {
        String origVersionId = getVersionId(getDriver(), script);
        sendKeys(dateEntryLocator, input);
        getJSExecutor().executeScript(Constants.BLUR_ACTIVE_ELEMENT);
        if (!waitForVersionIdUpdate(origVersionId, TIME_OUT_DD_SECONDS, script))
            TestContext.logToScenario(String.format("DynamicData version update check has failed for %s [%s]",
                    dateEntryLocator.toString(), script));
    }

    public void ddSelectByText(By by, String text) {
        ddSelectByText(by, text, TIME_OUT_DD_SECONDS, Constants.CURRENT_TASK_VERSION_ID);
    }

    public void ddSelectByText(By by, String text, String script) {
        ddSelectByText(by, text, TIME_OUT_DD_SECONDS, script);
    }

    public void ddSelectByText(By by, String text, int timeOutInSecs) {
        ddSelectByText(by, text, timeOutInSecs, Constants.CURRENT_TASK_VERSION_ID);
    }

    public void ddSelectByText(By by, String text, int timeOutInSecs, String script) {
        String origVersionId = getVersionId(getDriver(), script);
        selectByText(by, text);
        getJSExecutor().executeScript(Constants.BLUR_ACTIVE_ELEMENT);
        if (!waitForVersionIdUpdate(origVersionId, timeOutInSecs, script))
            TestContext.logToScenario(
                    String.format("DynamicData version update check has failed for %s [%s]", by.toString(), script));
    }

    public void ddSelectByValue(By by, String text) {
        ddSelectByValue(by, text, TIME_OUT_DD_SECONDS, Constants.CURRENT_TASK_VERSION_ID);
    }

    public void ddSelectByValue(By by, String text, String script) {
        ddSelectByValue(by, text, TIME_OUT_DD_SECONDS, script);
    }

    public void ddSelectByValue(By by, String text, int timeOutInSecs) {
        ddSelectByValue(by, text, timeOutInSecs, Constants.CURRENT_TASK_VERSION_ID);
    }

    public void ddSelectByValue(By by, String text, int timeOutInSecs, String script) {
        String origVersionId = getVersionId(getDriver(), script);
        selectByValue(by, text);
        getJSExecutor().executeScript(Constants.BLUR_ACTIVE_ELEMENT);
        if (!waitForVersionIdUpdate(origVersionId, timeOutInSecs, script))
            TestContext.logToScenario(
                    String.format("DynamicData version update check has failed for %s [%s]", by.toString(), script));
    }

    public String getVersionId(WebDriver driver) {
        return getVersionId(driver, Constants.CURRENT_TASK_VERSION_ID);
    }

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

    public void expandOrTriggerToClick(By listBoxLocator, By locatorIfExpanded) {
        expandOrTriggerToReveal(listBoxLocator, locatorIfExpanded, Boolean.FALSE);
        delayClick(locatorIfExpanded, SHORT_INTERVAL);
    }

    public void expandOrTriggerToClick(By listBoxLocator, By locatorIfExpanded, Boolean refreshOption) {
        expandOrTriggerToReveal(listBoxLocator, locatorIfExpanded, refreshOption);
        delayClick(locatorIfExpanded, SHORT_INTERVAL);
    }

    public void expandOrTriggerToClick(By listBoxLocator, By locatorIfExpanded, By locatorIfExpandedSecond) {
        expandOrTriggerToReveal(listBoxLocator, locatorIfExpanded, Boolean.FALSE);
        delayClick(locatorIfExpanded, SHORT_INTERVAL);
        delayClick(locatorIfExpandedSecond, SHORT_INTERVAL);
    }

    public void expandOrTriggerToReveal(By listBoxLocator, By locatorIfExpanded) {
        expandOrTriggerToReveal(listBoxLocator, locatorIfExpanded, Boolean.FALSE);
    }

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

    public void expectLoaderIconToDisappear() {
        expectLoaderIconToDisappear(TIMEOUT_COMPLETE_TASK);
    }

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

    public LocalDate convertToLocalDate(String inputDate) {
        return OffsetDateTime.parse(inputDate).atZoneSameInstant(ZoneId.systemDefault()).toLocalDate();
    }

    public LocalDate addYearsToLocalDate(LocalDate date, long years) {
        return date.plusYears(years);
    }

    private Callable<Boolean> waitInterval(int maxInterval) {
        return new Callable<>() {
            int counter = 0;

            public Boolean call() {
                counter++;
                return (counter == maxInterval);
            }
        };
    }

    private void awaitDelay(int maxInterval) {
        try {
            Awaitility.with().pollDelay(POLL_NO_INIT_DELAY, TimeUnit.SECONDS)
                    .pollInterval(Duration.ofMillis(POLL_WAIT_QUICK_MS)).await()
                    .atMost(Duration.ofSeconds(TIME_OUT_SECONDS)).until(waitInterval(maxInterval));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delayClick(By by, int delayInterval) {
        awaitDelay(delayInterval);
        click(by, TIME_OUT_FOR_PAGE_LOAD);
    }

    public void delayClick(WebElement element, int delayInterval) {
        awaitDelay(delayInterval);
        click(element, TIME_OUT_FOR_PAGE_LOAD);
    }

    public void delayClick(By by, int timeOut, int delayInterval) {
        awaitDelay(delayInterval);
        click(by, timeOut);
    }

    public void delayClick(WebElement element, int timeOut, int delayInterval) {
        awaitDelay(delayInterval);
        click(element, timeOut);
    }

    public void delaySendKeys(By by, String input, int delayInterval) {
        delaySendKeys(by, input, TIME_OUT_SECONDS, delayInterval);
    }

    public void delaySendKeys(By by, String input, int timeOutInSecs, int delayInterval) {
        awaitDelay(delayInterval);
        LOG.info("Sending Keys to Element {} with values {}", by, input);
        WebElement element = getElement(by);
        scrollIntoView(element, timeOutInSecs);
        element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        element.sendKeys(input);
    }

    public void delaySendKeysBlur(By by, String input, int delayInterval) {
        delaySendKeysBlur(by, input, TIME_OUT_SECONDS, delayInterval);
    }

    public void delaySendKeysBlur(By by, String input, int timeOutInSecs, int delayInterval) {
        awaitDelay(delayInterval);
        sendKeysBlur(by, input, timeOutInSecs);
    }

    public void delaySendKeysBlur(WebElement element, String input, int delayInterval) {
        delaySendKeysBlur(element, input, TIME_OUT_SECONDS, delayInterval);
    }

    public void delaySendKeysBlur(WebElement element, String input, int timeOutInSecs, int delayInterval) {
        awaitDelay(delayInterval);
        sendKeysBlur(element, input, timeOutInSecs);
    }

    public void delaySelectByValue(By by, String value, int delayInterval) {
        awaitDelay(delayInterval);
        selectByValue(by, value);
    }

    public void delaySelectByText(By by, String text, int delayInterval) {
        awaitDelay(delayInterval);
        selectByText(by, text);
    }

    public void changeWindow() {
        LOG.info("LOG:..............changing second poped out window");
        String oldTab = getDriver().getWindowHandle();
        ArrayList<String> newTab = new ArrayList<>(getDriver().getWindowHandles());
        newTab.remove(oldTab);
        getDriver().switchTo().window(newTab.get(0));
    }

    public String getCurrentDate(String dateFormat, ZoneId zone) {
        return OffsetDateTime.now(zone).format(DateTimeFormatter.ofPattern(dateFormat));
    }

    public String getFutureDate(String pattern, int daysInFuture, ZoneId zone) {
        return getFutureDate(DateTimeFormatter.ofPattern(pattern), daysInFuture, zone);
    }

    public String getFutureDate(DateTimeFormatter pattern, int daysInFuture, ZoneId zone) {
        return ZonedDateTime.now(zone).plusDays(daysInFuture).format(pattern);
    }

    public String getPastDate(String pattern, int daysInPast, ZoneId zone) {
        return getPastDate(DateTimeFormatter.ofPattern(pattern), daysInPast, zone);
    }

    public String getPastDate(DateTimeFormatter pattern, int daysInPast, ZoneId zone) {
        return ZonedDateTime.now(zone).minusDays(daysInPast).format(pattern);
    }

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

    public String getAttribute(By by, String attribute) {
        return getAttribute(by, TIME_OUT_SECONDS, attribute);
    }

    public String getAttribute(By by, int timeOutInSecs, String attribute) {
        expectElementToBeVisible(by, timeOutInSecs);
        return getDriver().findElement(by).getAttribute(attribute);
    }

    public String getAttribute(WebElement element, String attribute) {
        return getAttribute(element, TIME_OUT_SECONDS, attribute);
    }

    public String getAttribute(WebElement element, int timeOutInSecs, String attribute) {
        expectElementToBeVisible(element, timeOutInSecs);
        return element.getAttribute(attribute);
    }

    public boolean hasAttribute(By by, String attribute) {
        return getAttribute(by, TIME_OUT_SECONDS, attribute) != null;
    }

    public boolean hasAttribute(By by, int timeOutInSecs, String attribute) {
        expectElementToBeVisible(by, timeOutInSecs);
        return getDriver().findElement(by).getAttribute(attribute) != null;
    }

    public boolean hasAttribute(WebElement element, String attribute) {
        return getAttribute(element, TIME_OUT_SECONDS, attribute) != null;
    }

    public boolean hasAttribute(WebElement element, int timeOutInSecs, String attribute) {
        expectElementToBeVisible(element, timeOutInSecs);
        return element.getAttribute(attribute) != null;
    }

    public double calculateAddedAmountForUsd(double num1, double num2) {
        return calculateAddedAmount(num1, num2, USD_DECIMAL_SCALE, RoundingMode.HALF_UP);
    }

    public double calculateAddedAmount(double num1, double num2, int scale, RoundingMode roundingMode) {
        BigDecimal total = new BigDecimal(num1).add(new BigDecimal(num2));
        return total.setScale(scale, roundingMode).doubleValue();
    }

    public double calculateSubtractedAmountForUsd(double num1, double num2) {
        return calculateSubtractedAmount(num1, num2, USD_DECIMAL_SCALE, RoundingMode.HALF_UP);
    }

    public double calculateSubtractedAmount(double num1, double num2, int scale, RoundingMode roundingMode) {
        BigDecimal total = new BigDecimal(num1).subtract(new BigDecimal(num2));
        return total.setScale(scale, roundingMode).doubleValue();
    }

    public double calculateSubtractedAmountMinimumZeroForUsd(double num1, double num2) {
        return calculateSubtractedAmountMinimumZero(num1, num2, USD_DECIMAL_SCALE, RoundingMode.HALF_UP);
    }

    public double calculateSubtractedAmountMinimumZero(double num1, double num2, int scale, RoundingMode roundingMode) {
        BigDecimal total = new BigDecimal(num1).subtract(new BigDecimal(num2));
        if (total.doubleValue() < 0) {
            return 0;
        } else {
            return total.setScale(scale, roundingMode).doubleValue();
        }
    }

    public double calculateMultipliedAmountForUsd(double rate, double num1) {
        return calculateMultipliedAmount(rate, num1, USD_DECIMAL_SCALE, RoundingMode.HALF_UP);
    }

    public double calculateMultipliedAmount(double rate, double num1, int scale, RoundingMode roundingMode) {
        BigDecimal total = new BigDecimal(rate).multiply(new BigDecimal(num1))
                .divide(new BigDecimal(100), currencyRoundingType).setScale(scale, roundingMode);
        return total.setScale(scale, roundingMode).doubleValue();
    }

    public double truncateDoubleNumber(double value, int places) {
        double multiplier = Math.pow(10, places);
        return Math.floor(multiplier * value) / multiplier;
    }

    public double doubleRound(Double val, int digit) {
        return new BigDecimal(val.toString()).setScale(digit, RoundingMode.HALF_UP).doubleValue();
    }

    public void verifyDropDownValues(JsonArray array, By locator) {
        Select select = new Select(getElement(locator));
        List<WebElement> dropDownValues = select.getOptions();
        for (var i = 0; i < dropDownValues.size(); i++) {
            LOG.info(dropDownValues.get(i).getText() + "......" + array.get(i).getAsString());
            Assert.assertEquals(dropDownValues.get(i).getText(), array.get(i).getAsString());
        }
    }

//    public void validateDropDownOptionSelected(By optionLocator, String dropDownOption) {
//        LOG.info("Waiting for element to be Selected: {}", optionLocator.toString());
//        WebElement element = getElement(optionLocator);
//        Assert.assertEquals(element.isSelected(), dropDownOption + "Not Selected");
//    }

    public void verifyTextDoesNotExistsOnPage(String text) {
        LOG.info("Verifying that {} does not exists", text);
        Assert.assertFalse(getDriver().getPageSource().contains(text), text + "Does Exists on the page");
    }

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

    public String getText(WebElement we) {
        return getText(we, TIME_OUT_SECONDS);
    }

    public String getText(WebElement we, int timeOutInSecs) {
        scrollIntoView(we, timeOutInSecs);
        return we.getText();
    }

    public List<String> getTexts(By by) {
        return getTexts(by, TIME_OUT_SECONDS);
    }

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
