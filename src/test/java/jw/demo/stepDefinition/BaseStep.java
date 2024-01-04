package jw.demo.stepDefinition;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.cucumber.java.DefaultDataTableCellTransformer;
import io.cucumber.java.DefaultDataTableEntryTransformer;
import io.cucumber.java.DefaultParameterTransformer;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import jw.demo.constants.Constants;
import jw.demo.enums.Wait;
import jw.demo.enums.WebDriverRunLocation;
import jw.demo.managers.DriverManager;
import jw.demo.managers.FileReaderManager;
import jw.demo.pages.POM;
import jw.demo.utils.DocumentUtil;
import jw.demo.utils.HttpDownloadUtility;
import jw.demo.utils.TestContext;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.awaitility.core.ConditionTimeoutException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;

//@SuppressWarnings("Convert2Lambda")
public class BaseStep extends AbstractTestNGCucumberTests {

    // jdbc template can be used alongside spring repositories for DB testing
    // can use @Autowired annotation or use CDI
//    protected JdbcTemplate jdbcTemplate;
//
//    public BaseStep(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
    protected static final String ENV_PASS = FileReaderManager.getInstance().getConfigReader().getEnvPasswd();
    protected static final String BASE_URL = FileReaderManager.getInstance().getConfigReader().getBaseUrl();
    private static final Logger LOG = LogManager.getLogger(BaseStep.class);
    protected final String attachmentTableCommon = "";
    protected final String attachmentTableDataRows = attachmentTableCommon;
    protected static final String BAD_PAGE_SOURCE = "<html><head></head><body></body></html>";
    protected static final int TIME_OUT_QUICK = 1;
    protected static final int TIME_OUT_SHORT = 2;
    protected static final int TIME_OUT_FOR_SHORT_PAGELOAD = 5;
    protected static final int TIME_OUT_DD_SECONDS = 1;
    protected static final int TIME_OUT_SECONDS = 10;
    protected static final int TIME_OUT_FOR_PAGELOAD = 10;
    protected static final int TIME_OUT_SECONDS_UPLOAD = 10;
    protected static final int TIME_OUT_SECONDS_DOWNLOAD = 40;
    protected static final int TIMEOUT_FOR_REOPEN = 60;
    protected static final int TIMEOUT_COMPLETE_TASK = 120;
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
    protected static final String EST = "EST";
    protected static final String UTC = "UTC";
    private final ObjectMapper objectMapper = new ObjectMapper();
    protected POM pom = new POM();

    // Locators
    @FindBy
    protected static final By LOADER_ICON = By.xpath("");
    @FindBy
    protected static final By errorModalMessage = By.xpath("");
    @FindBy
    protected static final By DELETE_BUTTON = By.xpath("");

    // Methods
    protected static String getCurrentDate(String zone) {
        return DateTime.now().withZone(DateTimeZone.forID(zone)).toString();
    }

    protected static WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    public static void waitForDownload(String fileName, File downloadDir, int timeOutInSeconds) {
        String filePath = FilenameUtils.normalize(downloadDir.getPath() + File.separator + fileName);
        File file = new File(filePath);
        LOG.info("Waiting for download to exist in {}", file.getAbsolutePath());
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSeconds),
                Duration.ofMillis(POLL_WAIT_MS));
        ExpectedCondition<Boolean> fileDownloaded = new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                if (file.exists()) {
                    LOG.info("File '{}' detected. Done waiting.", fileName);
                    return true;
                }
                LOG.info("File '{}' not detected. Sleeping...", fileName);
                return false;
            }
        };
        wait.until(fileDownloaded);
    }

    /**
     * An expectation for checking an element is visible and enabled such that you
     * can click it.
     *
     * @param by            - locator used to find the element
     * @param index         - index of the element to be clicked (useful when
     *                      locator returns multiple elements)
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     * @returns The result is returned or false is returned if the time-out passes
     */
    public static boolean isClickable(By by, int index, int timeOutInSecs) {
        LOG.info("Checking if Element {} is clickable", by);
        try {
            (new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs)))
                    .until(ExpectedConditions.elementToBeClickable(getDriver().findElements(by).get(index)));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean getTextContainsToVerify(WebElement element, String text) {
        return getTextContainsToVerify(element, text, TIME_OUT_SECONDS);
    }

    public static boolean getTextContainsToVerify(WebElement element, String text, int timeOutInSecs) {
        (new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs)))
                .until(ExpectedConditions.visibilityOfAllElements(element));
        return element.getText().contains(text);
    }

    public static boolean getTextContainsToVerify(By by, String text) {
        return getTextContainsToVerify(by, text, TIME_OUT_SECONDS);
    }

    public static boolean getTextContainsToVerify(By by, String text, int timeOutInSecs) {
        (new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs)))
                .until(ExpectedConditions.visibilityOfElementLocated(by));
        return getDriver().findElement(by).getText().contains(text);
    }

    public static boolean getAllTextContainsToVerify(By by, String text, int timeOutInSecs) {
        (new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs)))
                .until(ExpectedConditions.visibilityOfElementLocated(by));
        List<String> allText = getDriver().findElements(by).stream().map(WebElement::getText)
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

    public static boolean getTextContainsToVerify(By by, int index, String text, int timeOutInSecs) {
        (new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs)))
                .until(ExpectedConditions.visibilityOfElementLocated(by));
        return (getDriver().findElements(by).get(index)).getText().contains(text);
    }

    @DefaultParameterTransformer
    @DefaultDataTableEntryTransformer
    @DefaultDataTableCellTransformer
    public Object transformer(Object fromValue, Type toValueType) {
        return objectMapper.convertValue(fromValue, objectMapper.constructType(toValueType));
    }

    protected JsonObject getScenarioData() {
        return TestContext.getScenarioCtx().getScenarioData();
    }

    protected String getCookie(String cookieName) {
        new WebDriverWait(getDriver(), Wait.REGULAR.waitTime())
                .until(new ExpectedCondition<Cookie>() {
                    public Cookie apply(WebDriver driver) {
                        return driver.manage().getCookieNamed(cookieName);
                    }
                });
        return getDriver().manage().getCookieNamed(cookieName).getValue();
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page
     * and visible. Visibility means that the element is not only displayed but also
     * has a height and width that is greater than 0.
     * <p>
     * A default timeout of 10 seconds is provided, and a
     * TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     *
     * @param by - locator used to find the element
     */
    public void waitForElementToBeVisible(By by) {
        waitForElementToBeVisible(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that an element is either invisible or not
     * present on the DOM.
     * <p>
     * A default timeout of 10 seconds is provided, and a
     * TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     *
     * @param by - locator used to find the element
     */
    public void waitForElementToBeInvisible(By by) {
        waitForElementToBeInvisible(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page.
     * This does not necessarily mean that the element is visible.
     * <p>
     * A default timeout of 10 seconds is provided, and a
     * TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     *
     * @param by - locator used to find the element
     */
    public void waitForElementToBePresent(By by) {
        waitForElementToBePresent(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking an element is visible and enabled such that you
     * can click it.
     * <p>
     * A default timeout of 10 seconds is provided, and a
     * TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     *
     * @param by - locator used to find the element
     */
    public void waitForElementToBeClickable(By by) {
        waitForElementToBeClickable(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page
     * and visible. Visibility means that the element is not only displayed but also
     * has a height and width that is greater than 0.
     * <p>
     * A TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     *
     * @param by            - locator used to find the element
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     */
    public void waitForElementToBeVisible(By by, int timeOutInSecs) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs))
                .until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    /**
     * An expectation for checking that an element is either invisible or not
     * present on the DOM.
     * <p>
     * A TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     *
     * @param by            - locator used to find the element
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     */
    public void waitForElementToBeInvisible(By by, int timeOutInSecs) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs))
                .until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page.
     * This does not necessarily mean that the element is visible.
     * <p>
     * A TimeoutException/ConditionTimeoutException will be thrown once the time out
     * is passed.
     * </p>
     *
     * @param by            - locator used to find the element
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     */
    public void waitForElementToBePresent(By by, int timeOutInSecs) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs))
                .until(ExpectedConditions.presenceOfElementLocated(by));
    }

    /**
     * An expectation for checking an element is visible and enabled such that you
     * can click it.
     * <p>
     * A TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     *
     * @param by            - locator used to find the element
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     */
    public void waitForElementToBeClickable(By by, int timeOutInSecs) {
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
     * </p>
     *
     * @param element - element the WebElement
     */
    public void waitForElementToBeVisible(WebElement element) {
        waitForElementToBeVisible(element, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that an element is either invisible or not
     * present on the DOM.
     * <p>
     * A default timeout of 10 seconds is provided and a
     * TimeoutException/ConditionTimeoutException will be thrown once the time out
     * is passed.
     * </p>
     *
     * @param element - element the WebElement
     */
    public void waitForElementToBeInvisible(WebElement element) {
        waitForElementToBeInvisible(element, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking an element is visible and enabled such that you
     * can click it.
     * <p>
     * A default timeout of 10 seconds and then provided and a
     * TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     * </p>
     *
     * @param element - element the WebElement
     */
    public void waitForElementToBeClickable(WebElement element) {
        waitForElementToBeClickable(element, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page
     * and visible. Visibility means that the element is not only displayed but also
     * has a height and width that is greater than 0.
     * <p>
     * A TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     * </p>
     *
     * @param element       - element the WebElement
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     */
    public void waitForElementToBeVisible(WebElement element, int timeOutInSecs) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs))
                .until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * An expectation for checking that an element is either invisible or not
     * present on the DOM.
     * <p>
     * A TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     *
     * @param element       - element the WebElement
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     */
    public void waitForElementToBeInvisible(WebElement element, int timeOutInSecs) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs))
                .until(ExpectedConditions.invisibilityOf(element));
    }

    /**
     * An expectation for checking an element is visible and enabled such that you
     * can click it.
     * <p>
     * A TimeoutException/ConditionTimeoutException will be thrown once the time-out
     * is passed.
     * </p>
     *
     * @param element       - element the WebElement
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     */
    public void waitForElementToBeClickable(WebElement element, int timeOutInSecs) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForElementToContainAttribute(WebElement element, String attribute, String attributeValue) {
        waitForElementToContainAttribute(element, attribute, attributeValue, TIME_OUT_SECONDS);
    }

    public void waitForElementToContainAttribute(WebElement element, String attribute, String attributeValue,
                                                 int timeOutInSecs) {
        ExpectedCondition<Boolean> attributeContains = new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    waitForElementToBeVisible(element, timeOutInSecs);
                    return element.getAttribute(attribute).contains(attributeValue);
                } catch (StaleElementReferenceException e) {
                    return false;
                }
            }
        };
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs), Duration.ofMillis(POLL_WAIT_MS))
                .until(attributeContains);
    }

    public Boolean parentDivElementIsClicked(By subDivLocator) {
        try {
            WebElement parentDivElement = (WebElement) ((JavascriptExecutor) getDriver())
                    .executeScript("return arguments[0].parentNode;", getDriver().findElement(subDivLocator));
            waitForElementToContainAttribute(parentDivElement, "class", "checked");
            LOG.info("Parent Div is clicked as expected for [{}]", subDivLocator);
            return true;
        } catch (TimeoutException | ConditionTimeoutException | NoSuchElementException e) {
            LOG.error("Parent Div is not clicked as expected for [{}]", subDivLocator);
            return false;
        }
    }

    public void scrollIntoView(By by) {
        scrollIntoView(by, TIME_OUT_SECONDS);
    }

    public void scrollIntoView(By by, Duration duration) {
        scrollIntoView(by, (int) duration.getSeconds());
    }

    public void scrollIntoView(By by, int timeOutInSecs) {
        waitForElementToBeVisible(by, timeOutInSecs);
        getJSExecutor().executeScript(
                "arguments[0].scrollIntoView({behavior: 'auto', block: 'center', inline: 'nearest'});",
                getDriver().findElement(by));
    }

    public void scrollIntoView(WebElement element) {
        scrollIntoView(element, TIME_OUT_SECONDS);
    }

    public void scrollIntoView(WebElement element, int timeOutInSecs) {
        waitForElementToBeVisible(element, timeOutInSecs);
        getJSExecutor().executeScript(
                "arguments[0].scrollIntoView({behavior: 'auto', block: 'center', inline: 'nearest'});", element);
    }

    public void scrollIntoViewByText(String text) {
        scrollIntoViewByText(text, TIME_OUT_SECONDS);
    }

    public void scrollIntoViewByText(String text, int timeOutInSecs) {
        List<WebElement> multipleGenericTextLocators = getElements(By.xpath("//*[contains(text(),'" + text + "')]"));
        multipleGenericTextLocators.forEach(currentWebElement -> {
            if (isVisible(currentWebElement)) {
                scrollIntoView(currentWebElement, timeOutInSecs);
            }
        });
    }

    public void validateAttachmentDetails(JsonArray attachmentList, List<String> attachmentTableRowsText) {
        attachmentList.forEach((JsonElement attachment) -> {
            JsonObject docObj = attachment.getAsJsonObject();
            for (String attachmentTableRowText : attachmentTableRowsText) {
                validateAttachmentDetail(docObj, attachmentTableRowText);
            }
        });
    }

    public void validateAttachmentDetail(JsonObject docObj, String attachmentTableRowText) {
        if (attachmentTableRowText.contains(docObj.get(DocumentUtil.FILE_NAME).getAsString())) {
            String fileName = docObj.get(DocumentUtil.FILE_NAME).getAsString();
            String currentDateUtc = getCurrentDate(EST);
            String currentDateEastern = getCurrentDate(UTC);
            Assert.assertTrue(attachmentTableRowText.contains(fileName),
                    "File name did not match! actual: " + attachmentTableRowText + " | expected: "
                            + docObj.get(DocumentUtil.FILE_NAME).getAsString() + "\nCurrent Row Data: "
                            + attachmentTableRowText);
            scrollIntoViewByText(fileName);
            if (!attachmentTableRowText.contains(currentDateEastern)) {
                Assert.assertTrue(attachmentTableRowText.contains(currentDateUtc),
                        "Attachment table doesn't contain or contains different dates that the expected: Eastern: ["
                                + currentDateEastern + "] || " + DateTimeZone.UTC + ": [" + currentDateUtc + "]\nCurrent Row Data: "
                                + attachmentTableRowText);
            }
            if (docObj.has(DocumentUtil.UPLOADED_BY)) {
                String uploadedBy = docObj.get(DocumentUtil.UPLOADED_BY).getAsString();
                LOG.info("actual text: {} -- expected text: {}", attachmentTableRowText, uploadedBy);
                Assert.assertTrue(attachmentTableRowText.contains(uploadedBy),
                        "'Uploaded by' expected to be " + uploadedBy + "\nCurrent Row Data: " + attachmentTableRowText);
            }
            if (docObj.has(DocumentUtil.FILE_SIZE)) {
                String fileSize = docObj.get(DocumentUtil.FILE_SIZE).getAsString();
                Assert.assertTrue(attachmentTableRowText.contains(fileSize),
                        "File size expected to be " + fileSize + "\nCurrent Row Data: " + attachmentTableRowText);
            }
        }
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page
     * and visible. Visibility means that the element is not only displayed but also
     * has a height and width that is greater than 0.
     *
     * @param by - locator used to find the element
     * @returns The result is returned or false is returned if 10 seconds to pass
     */
    public boolean isVisible(By by) {
        return isVisible(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page
     * and visible. Visibility means that the element is not only displayed but also
     * has a height and width that is greater than 0.
     *
     * @param by            - locator used to find the element
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     * @returns The result is returned or false is returned if the time-out passes
     */
    public boolean isVisible(By by, int timeOutInSecs) {
        try {
            waitForElementToBeVisible(by, timeOutInSecs);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page
     * and visible. Visibility means that the element is not only displayed but also
     * has a height and width that is greater than 0.
     *
     * @param element - element the WebElement
     * @returns The result is returned or false is returned if 10 seconds to pass
     */
    public boolean isVisible(WebElement element) {
        return isVisible(element, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page
     * and visible. Visibility means that the element is not only displayed but also
     * has a height and width that is greater than 0.
     *
     * @param element       - element the WebElement
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     * @returns The result is returned or false is returned if the time-out passes
     */
    public boolean isVisible(WebElement element, int timeOutInSecs) {
        try {
            waitForElementToBeVisible(element, timeOutInSecs);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * An expectation for checking that an element is either invisible or not
     * present on the DOM.
     *
     * @param by - locator used to find the element
     * @returns The result is returned or false is returned if 10 seconds to pass
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isInvisible(By by) {
        return isInvisible(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that an element is either invisible or not
     * present on the DOM.
     *
     * @param by            - locator used to find the element
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     * @returns The result is returned or false is returned if the time-out passes
     */
    public boolean isInvisible(By by, int timeOutInSecs) {
        try {
            waitForElementToBeInvisible(by, timeOutInSecs);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * An expectation for checking that an element is either invisible or not
     * present on the DOM.
     *
     * @param element - element the WebElement
     * @returns The result is returned or false is returned if 10 seconds to pass
     */
    public boolean isInvisible(WebElement element) {
        return isInvisible(element, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that an element is either invisible or not
     * present on the DOM.
     *
     * @param element       - element the WebElement
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     * @returns The result is returned or false is returned if the time-out passes
     */
    public boolean isInvisible(WebElement element, int timeOutInSecs) {
        try {
            waitForElementToBeInvisible(element, timeOutInSecs);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page
     * is visible and isDisplayed
     * <p>
     * isVisible returns the result of driver.findElement(by), but isDisplayed goes
     * further and checks for getDriver().findElement(by).isDisplayed()
     * </p>
     *
     * @param by - locator used to find the element
     * @returns The result is returned or false is returned if 10 seconds to pass
     */
    public boolean isDisplayed(By by) {
        return isDisplayed(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page
     * is visible and isDisplayed
     * <p>
     * isVisible returns the result of driver.findElement(by), but isDisplayed goes
     * further and checks for getDriver().findElement(by).isDisplayed()
     *
     * @param by            - locator used to find the element
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     * @returns The result is returned or false is returned if the time-out passes
     */
    public boolean isDisplayed(By by, int timeOutInSecs) {
        try {
            waitForElementToBeVisible(by, timeOutInSecs);
            return getDriver().findElement(by).isDisplayed();
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page.
     * This does not necessarily mean that the element is visible.
     *
     * @param by - locator used to find the element
     * @returns The result is returned or false is returned if 10 seconds to pass
     */
    public boolean isPresent(By by) {
        return isPresent(by, TIME_OUT_SECONDS);
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page.
     * This does not necessarily mean that the element is visible.
     *
     * @param by            - locator used to find the element
     * @param timeOutInSecs - time limit to wait before throwing an Exception
     * @returns The result is returned or false is returned if the time-out passes
     */
    public boolean isPresent(By by, int timeOutInSecs) {
        try {
            waitForElementToBePresent(by, timeOutInSecs);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public WebElement getElement(By by) {
        return getElement(by, TIME_OUT_SECONDS);
    }

    public WebElement getElement(By by, int timeOutInSecs) {
        waitForElementToBePresent(by, timeOutInSecs);
        return getDriver().findElement(by);
    }

    public List<WebElement> getElements(By by) {
        return getElements(by, TIME_OUT_SECONDS);
    }

    public List<WebElement> getElements(By by, int timeOutInSecs) {
        waitForElementToBePresent(by, timeOutInSecs);
        return getDriver().findElements(by);
    }

    public void clear(By by) {
        clear(by, TIME_OUT_SECONDS);
    }

    public void clear(By by, int timeOutInSecs) {
        waitForElementToBeVisible(by, timeOutInSecs);
        getDriver().findElement(by).clear();
    }

    public void sendKeys(By by, String input) {
        sendKeys(by, input, TIME_OUT_SECONDS);
    }

    public void sendKeys(By by, String input, int timeOutInSecs) {
        scrollIntoView(by, timeOutInSecs);
        getDriver().findElement(by).clear();
        getDriver().findElement(by).sendKeys(input);
    }

    public boolean doElementsExist(By by) {
        return !getDriver().findElements(by).isEmpty();
    }

    public boolean isMessageDisplayed(String message) {
        return isDisplayed(By.xpath("//*[contains(.,'" + message + "')]"), TIME_OUT_SECONDS);
    }

    public void selectByText(By by, String text) {
        selectByText(by, text, TIME_OUT_SECONDS);
    }

    public void selectByText(By by, String text, int timeOutInSecs) {
        waitForElementToBeVisible(by, timeOutInSecs);
        (new Select(getDriver().findElement(by))).selectByVisibleText(text);
    }

    public String getTextIfVisible(By by) {
        if (isVisible(by)) {
            return getText(by, TIME_OUT_SECONDS);
        } else {
            return "";
        }
    }

    public String getText(By by) {
        return getText(by, TIME_OUT_SECONDS);
    }

    public String getText(By by, int timeoutInSecs) {
        try {
            waitForElementToBeVisible(by, timeoutInSecs);
            LOG.info("Getting text for element {}", by);
            return getDriver().findElement(by).getText();
        } catch (UnhandledAlertException e) {
            getDriver().navigate().refresh();
            waitForLoaderIconToDisappear();
            waitForElementToBeVisible(by, timeoutInSecs);
            LOG.info("Getting text for element {}", by);
            return getDriver().findElement(by).getText();
        }
    }

    public void waitForLoaderIconToDisappear(int timeOut) {
        acceptAlertForOutdatedBrowser();
        ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<>() {
            public Boolean apply(WebDriver driver) {
                Boolean pageLoadComplete = ((JavascriptExecutor) driver).executeScript("return document.readyState")
                        .equals("complete");
                if (pageLoadComplete)
                    LOG.info(
                            "document.readyState is complete! Waiting for the Loader Icon to be cleared in the UI.....");
                else
                    LOG.warn("document.readyState not complete; waiting.....");
                return pageLoadComplete;
            }
        };
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut)).until(pageLoadCondition);
        } catch (Exception e) {
            Assert.fail("Timed out waiting for document.readyState to be complete");
        }
        try {
            waitForElementToBeInvisible(LOADER_ICON, timeOut);
        } catch (Exception e) {
            Assert.fail("Timed out waiting for the Loader Icon to Disappear [" + LOADER_ICON + "]");
        }
        acceptAlertForOutdatedBrowser();
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

    public String getText(By by, int index, int timeoutInSecs) {
        waitForElementToBeVisible(by, timeoutInSecs);
        return getDriver().findElements(by).get(index).getText();
    }

    public String getText(WebElement we) {
        return getText(we, TIME_OUT_SECONDS);
    }

    public String getText(WebElement we, int timeOutInSecs) {
        waitForElementToBeVisible(we, timeOutInSecs);
        return we.getText();
    }

    public List<String> getTexts(By by) {
        return getTexts(by, TIME_OUT_SECONDS);
    }

    public List<String> getTexts(By by, int timeOutInSeconds) {
        List<WebElement> elements = getElements(by);
        List<String> texts = new ArrayList<>();
        for (WebElement element : elements) {
            texts.add(getText(element));
        }
        return texts;
    }

    public Integer getElementCount(By by) {
        return getDriver().findElements(by).size();
    }

    public void waitUntilElementsPopulated(By by, int timeOut, int pollAwaitSeconds, int size) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut));
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return driver.findElements(by).size() > size;
            }
        });
    }

    public void waitUntilElementEnabled(final By by, int maxTime, int pollInterval) {
        LOG.info("Waiting for element to be enabled: {}", by.toString());
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(maxTime),
                Duration.ofMillis(pollInterval));
        ExpectedCondition<Boolean> elementToBeEnabled = new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    waitForElementToBePresent(by, maxTime);
                    return getDriver().findElement(by).isEnabled();
                } catch (StaleElementReferenceException e) {
                    LOG.warn("Element stale. Retrying in {} seconds", pollInterval / 1000);
                } catch (NoSuchElementException e) {
                    LOG.info("No such element. Retrying in {} seconds", pollInterval / 1000);
                }
                return Boolean.FALSE;
            }
        };
        wait.until(elementToBeEnabled);
    }

    public void waitUntilElementTextUpdated(final By by, String expectedText, int maxTime, int pollInterval) {
        LOG.info("Waiting for element to be enabled: {}", by.toString());
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(maxTime),
                Duration.ofMillis(pollInterval));
        ExpectedCondition<Boolean> elementTextToBeUpdated = new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    waitForElementToBeVisible(by, maxTime);
                    return getText(by).equals(expectedText);
                } catch (StaleElementReferenceException e) {
                    LOG.warn("Element stale. Retrying in {} seconds", pollInterval / 1000);
                } catch (NoSuchElementException e) {
                    LOG.warn("No such element. Retrying in {} seconds", pollInterval / 1000);
                }
                return Boolean.FALSE;
            }
        };
        wait.until(elementTextToBeUpdated);
    }

    /**
     * Enhanced click, waits for WebElement to be visible, click, and then wait for
     * document readystate is complete. Refreshes if errorModal is detected
     *
     * @param by      - A By element, usually By.xpath(xpathString)
     * @param timeOut - Time limit method will retry the click until
     */
    public void click(By by, int timeOut) {

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut),
                Duration.ofMillis(POLL_WAIT_MS));
        ExpectedCondition<Boolean> clickedButton = new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                LOG.info("Attempting to Click {}", by.toString());
                try {
                    if (!isInvisible(errorModalMessage)) {
                        getDriver().navigate().refresh();
                        waitForLoaderIconToDisappear();
                    }
                    scrollIntoView(by);
                    WebElement element = driver.findElement(by);
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
                } catch (ElementNotInteractableException e) {
                    LOG.warn("Element Not Intractable. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                }
                return Boolean.FALSE;
            }
        };
        wait.until(clickedButton);
    }

    /**
     * Enhanced click, waits for WebElement to be visible, click, and then wait for
     * document readystate is complete. Refreshes if errorModal is detected
     *
     * @param element - A WebElement
     * @param timeOut - Time limit method will retry the click until
     */
    public void click(WebElement element, int timeOut) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut),
                Duration.ofMillis(POLL_WAIT_MS));
        ExpectedCondition<Boolean> clickedButton = new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                LOG.info("Attempting to Click {}", element.toString());
                try {
                    if (!isInvisible(errorModalMessage)) {
                        getDriver().navigate().refresh();
                        waitForLoaderIconToDisappear();
                    }
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
                } catch (ElementNotInteractableException e) {
                    LOG.warn("Element Not Intractable. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                }
                return Boolean.FALSE;
            }
        };
        wait.until(clickedButton);
    }

    /**
     * Enhanced click, waits for WebElement to be visible, click, abd then wait for
     * document readystate is complete. Refreshes if errorModal is detected. Default
     * timeout of 30 seconds will be provided
     *
     * @param by - A By element, usually By.xpath(xpathString)
     */
    public void click(By by) {
        click(by, TIME_OUT_FOR_PAGE_LOAD);
    }

    /**
     * Enhanced click, waits for WebElement to be visible, click, and then wait for
     * document readystate is complete. Refreshes if errorModal is detected. Default
     * timeout of 30 seconds will be provided
     *
     * @param element - A WebElement
     */
    public void click(WebElement element) {
        click(element, TIME_OUT_FOR_PAGE_LOAD);
    }

    public void refreshIfLocatorNotVisible(By by) {
        refreshIfLocatorNotVisible(by, TIME_OUT_FOR_PAGE_LOAD);
    }

    public void refreshIfLocatorNotVisible(By by, int timeOutInSecs) {
        if (!isVisible(by, timeOutInSecs)) {
            getDriver().navigate().refresh();
        }
    }

    public JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) getDriver();
    }

    protected void waitUntilEnabled(By by, int maxTime, int pollInterval) {
        waitForElementToBeVisible(by, maxTime);
        WebElement element = getDriver().findElement(by);
        waitUntilEnabled(element, maxTime, pollInterval);
    }

    protected void waitUntilEnabled(WebElement element, long maxTime, long pollInterval) {
        await(element.toString() + " is not enabled").atMost(maxTime, TimeUnit.SECONDS).with()
                .pollDelay(POLL_NO_INIT_DELAY, TimeUnit.SECONDS).pollInterval(pollInterval, TimeUnit.SECONDS)
                .until(element::isEnabled);
    }

//    public void expandReviewPanelSection(String sectionName) {
//        By expandLocator = By.cssSelector("a[title^='Expand " + sectionName + "']");
//        By collapseLocator = By.cssSelector("a[title^='Collapse " + sectionName + "']");
//
//        pom.getSideReviewPanelPage().showReviewPanel();
//        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(TIMEOUT_COMPLETE_TASK),
//                Duration.ofMillis(LONGEST_POLL_WAIT_MS));
//        ExpectedCondition<Boolean> expandedSection = new ExpectedCondition<Boolean>() {
//            @Override
//            public Boolean apply(WebDriver driver) {
//                if (isDisplayed(expandLocator)) {
//                    LOG.info("{} section is collapsed, expanding", sectionName);
//                    mouseOver(expandLocator);
//                    click(expandLocator);
//                } else if (isDisplayed(collapseLocator)) {
//                    LOG.info("{} section is already expanded", sectionName);
//                } else {
//                    LOG.info("{} section expander/collapser not displaying. Refreshing page.....", sectionName);
//                    getDriver().navigate().refresh();
//                    waitForLoaderIconToDisappear();
//                    clickIfDisplayed(expandLocator);
//                }
//                return isDisplayed(collapseLocator);
//            }
//        };
//        wait.until(expandedSection);
//    }

    /**
     * Enhanced isEnabled check
     *
     * @param by
     * @param timeOut
     */
    public boolean isEnabled(By by, int timeOut) {
        try {
            waitForElementToBeVisible(by, timeOut);
            return getDriver().findElement(by).isEnabled();
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean isEnabled(By by) {
        return isEnabled(by, TIME_OUT_SECONDS);
    }

    protected void mouseOver(By by) {
        mouseOver(by, Duration.ofSeconds(TIME_OUT_SECONDS));
    }

    protected void mouseOver(By by, Duration duration) {

        WebDriverWait wait = new WebDriverWait(getDriver(), duration,
                Duration.ofMillis(POLL_WAIT_MS));
        ExpectedCondition<Boolean> mousedOver = new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                LOG.info("Attempting to Mouse Over {}", by.toString());
                try {
                    scrollIntoView(by, duration);
                    WebElement element = getElement(by, (int) duration.getSeconds());
                    Actions action = new Actions(driver);
                    action.moveToElement(element).build().perform();
                    return true;
                } catch (TimeoutException e) {
                    LOG.info("Timed out, could not mouse over. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                } catch (StaleElementReferenceException e) {
                    LOG.info("Element stale. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                } catch (NoSuchElementException e) {
                    LOG.info("No such element. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                }
                return false;
            }
        };
        wait.until(mousedOver);
    }

    public void downloadDocument(JsonObject docObj, String authorization) {
        String fileName = docObj.get("fileName").getAsString();
        File downloadDir = DriverManager.getDownloadDir();
        String linkAttribute = getElement(By.linkText(fileName), TIME_OUT_SECONDS).getAttribute("href");
        LOG.info("Link attribute - {}", linkAttribute);

        if (isDisplayed(By.linkText(fileName), TIME_OUT_SECONDS_DOWNLOAD)) {
            if (FileReaderManager.getInstance().getConfigReader().getWebDriverRunLocation() == WebDriverRunLocation.REMOTE) {
                LOG.info("Downloading '{}' through HTTP download util", fileName);
                HttpDownloadUtility.downloadFile(getDriver(), linkAttribute, downloadDir, authorization);
            } else {
                LOG.info("Clicking on the file to download - " + fileName);
                click(By.linkText(fileName));
            }
        }

        waitForDownload(fileName, downloadDir, TIME_OUT_SECONDS_DOWNLOAD);
    }

    public void deleteDocument(JsonObject deleteObj, String attachmentTableDataRows) {
        String fileName = deleteObj.get("deleteAttachment").getAsJsonObject().get("fileName").getAsString();
        LOG.info("Deleting the document {}", fileName);
        final int origAttachmentCount = getElements(By.xpath(attachmentTableDataRows)).size();
        click(By.xpath("//a[contains(@id,'" + fileName + "') and @class='remove-attachment']"));
        ddClick(DELETE_BUTTON);
        ExpectedCondition<Boolean> attachmentsTableReady = new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                int attachmentsSize = getElements(By.xpath(attachmentTableDataRows)).size();
                if (attachmentsSize == origAttachmentCount) {
                    ddClick(DELETE_BUTTON);
                }
                return attachmentsSize == (origAttachmentCount - 1);
            }
        };
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(TIMEOUT_COMPLETE_TASK),
                Duration.ofSeconds(TIME_OUT_FOR_PAGE_LOAD));
        wait.until(attachmentsTableReady);
        Assert.assertEquals((origAttachmentCount - 1), getElements(By.xpath(attachmentTableDataRows)).size(), "Expected attachment count to be " + (origAttachmentCount - 1));
        List<String> rows = getElements(By.xpath(attachmentTableDataRows)).stream().map(WebElement::getText)
                .collect(Collectors.toList());
        validateAttachmentDetails(deleteObj.getAsJsonArray("documentList"), rows);
    }

    public void expand(String sectionName) {
        By expandLocator = By.cssSelector("a[title^='Expand " + sectionName + "']");
        By collapseLocator = By.cssSelector("a[title^='Collapse " + sectionName + "']");

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(TIMEOUT_COMPLETE_TASK),
                Duration.ofMillis(LONGEST_POLL_WAIT_MS));
        ExpectedCondition<Boolean> expandedSection = new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                if (isDisplayed(expandLocator)) {
                    LOG.info("{} section is collapsed, expanding", sectionName);
                    mouseOver(expandLocator);
                    click(expandLocator);
                } else if (isDisplayed(collapseLocator)) {
                    LOG.info("{} section is already expanded", sectionName);
                }
                return isDisplayed(collapseLocator);
            }
        };
        wait.until(expandedSection);
    }

    public void collapse(String sectionName) {
        By expandLocator = By.cssSelector("a[title^='Expand " + sectionName + "']");
        By collapseLocator = By.cssSelector("a[title^='Collapse " + sectionName + "']");

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(TIMEOUT_COMPLETE_TASK),
                Duration.ofMillis(LONGEST_POLL_WAIT_MS));
        ExpectedCondition<Boolean> collapsedSection = new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                if (isDisplayed(collapseLocator)) {
                    LOG.info("{} section is expanded, collapsing", sectionName);
                    mouseOver(collapseLocator);
                    click(collapseLocator);
                } else if (isDisplayed(expandLocator)) {
                    LOG.info("{} section is already collapsed", sectionName);
                }
                return isDisplayed(expandLocator);
            }
        };
        wait.until(collapsedSection);
    }

    public void collapseReviewPanelSection(String sectionName) {
        By expandLocator = By.cssSelector("");
        By collapseLocator = By.cssSelector("");

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(TIMEOUT_COMPLETE_TASK),
                Duration.ofMillis(LONGEST_POLL_WAIT_MS));
        ExpectedCondition<Boolean> collapsedSection = new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                if (isDisplayed(collapseLocator)) {
                    LOG.info("{} section is expanded, collapsing", sectionName);
                    mouseOver(collapseLocator);
                    click(collapseLocator);
                } else if (isDisplayed(expandLocator)) {
                    LOG.info("{} section is already collapsed", sectionName);
                } else {
                    LOG.info("{} section including expand/collapse behavior not displaying. Refreshing page.....", sectionName);
                    getDriver().navigate().refresh();
                    waitForLoaderIconToDisappear();
                    clickIfDisplayed(collapseLocator);
                }
                return isDisplayed(expandLocator);
            }
        };
        wait.until(collapsedSection);
    }

    public void clickIfDisplayed(By by, int timeOut) {
        if (isDisplayed(by, timeOut)) {
            click(by, timeOut);
        }
    }

    public void clickIfDisplayed(By by) {
        clickIfDisplayed(by, TIME_OUT_SECONDS);
    }

    public void waitForLoaderIconToDisappear() {
        waitForLoaderIconToDisappear(TIMEOUT_COMPLETE_TASK);
    }

    public Boolean isReviewPanelSectionVisible(String sectionName, int timeOutInSecs) {
        return isVisible(By.xpath("//div[@class='contents']//a[contains(@title,'" + sectionName + "')]"));
    }

    public void searchBy(By searchBy, By searchResult, String searchType, String input) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(LONGER_TIME_OUT_SECONDS),
                Duration.ofMillis(LONGEST_POLL_WAIT_MS));
        ExpectedCondition<Boolean> foundSearchOutcome = new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                LOG.info("Searching for {} ID [{}]", searchType, input);
                try {
                    driver.findElement(searchBy)
                            .sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE) + input + Keys.TAB);
                    new WebDriverWait(getDriver(), Duration.ofSeconds(TIME_OUT_SECONDS))
                            .until(ExpectedConditions.numberOfElementsToBeMoreThan(searchResult, 0));
                    LOG.info("[{}] found [{}]", input, driver.findElements(searchResult).size());
                    return true;
                } catch (TimeoutException e) {
                    LOG.info("[{}] Search result {} did not return any results.  Retrying...", input, searchResult);
                } catch (NoSuchElementException e) {
                    LOG.info("[{}] Search by {} is not found yet.  Retrying...", input, searchBy);
                } catch (StaleElementReferenceException e) {
                    LOG.info("[{}] Search by {} is stale.  Retrying...", input, searchBy);
                } catch (ElementClickInterceptedException e) {
                    LOG.warn("Element Click Intercepted. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                } catch (ElementNotInteractableException e) {
                    LOG.warn("Element Not Intractable. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                }
                return false;
            }
        };
        wait.until(foundSearchOutcome);
    }

    public void uploadDocument(JsonArray docList, By uploadButton) {
        docList.forEach(doc -> {
            String fileName = doc.getAsJsonObject().get("fileName").getAsString();
            URL dataFileURL = this.getClass().getClassLoader().getResource("data/documents/" + fileName);
            LOG.info("Attaching a document " + dataFileURL);
            waitForElementToBePresent(uploadButton, TIME_OUT_SECONDS_DOWNLOAD);
            File file = new File(Objects.requireNonNull(dataFileURL).getFile());
            WebElement upload = getDriver().findElement(uploadButton);
            LOG.info("absolute path - " + file.getAbsolutePath());
            upload.sendKeys(file.getAbsolutePath());
        });
    }

    public void attachDocument(JsonArray docList, String attachmentTableAnchor, By attachDocumentBtn) {
        attachDocumentToSubsection(attachmentTableAnchor, docList, attachDocumentBtn);
        LOG.info("Validating the uploaded documents...");
        getDriver().findElements(attachDocumentBtn);
        List<String> rows = getElements(By.xpath(attachmentTableDataRows), TIME_OUT_SECONDS).stream()
                .map(WebElement::getText).collect(Collectors.toList());
        validateAttachmentDetails(docList, rows);
    }

    public void attachDocumentToSubsection(String attachmentTableAnchor, JsonArray docList, By attachDocumentBtn) {
        final String attachmentTable = attachmentTableAnchor + attachmentTableCommon;
        final AtomicInteger indexStash = new AtomicInteger();
        docList.forEach(doc -> {
            final int attachmentIndex = indexStash.getAndIncrement() + 1;
            String fileName = doc.getAsJsonObject().get("fileName").getAsString();
            URL dataFileURL = this.getClass().getClassLoader().getResource("data/documents/" + fileName);
            LOG.info("Attaching a document " + dataFileURL);

            DocumentUtil.uploadFile(Objects.requireNonNull(dataFileURL), attachDocumentBtn, getDriver());
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(TIMEOUT_COMPLETE_TASK));
            By attachmentRows = By.xpath(attachmentTable + "/tbody/tr");
            wait.until(ExpectedConditions.numberOfElementsToBe(attachmentRows, attachmentIndex));
            By attachmentCols = By.xpath(attachmentTable + "/tbody/tr[" + attachmentIndex + "]/td["
                    + getAttachmentTableColumnIndex(attachmentTable, "Date uploaded") + "]");
            wait.until(ExpectedConditions.textToBePresentInElement(getDriver().findElement(attachmentCols), getCurrentDate(EST)));

            sendKeys(
                    By.xpath(attachmentTable + "//textarea[contains(@id,'"
                            + doc.getAsJsonObject().get("fileName").getAsString() + "')]"),
                    doc.getAsJsonObject().get("description").getAsString(), TIME_OUT_SECONDS);
        });
    }

    public String getAttachmentTableColumnIndex(String attachmentTable, String input) {
        By by = By.xpath(attachmentTable + "/thead//th[.='" + input + "']/preceding-sibling::th");
        int index = getDriver().findElements(by).size() + 1;
        return Integer.toString(index);
    }

    public String getAttachmentTableColumnIndex(String input) {
        By by = By.xpath(attachmentTableCommon + "/thead//th[.='" + input + "']/preceding-sibling::th");
        int index = getDriver().findElements(by).size() + 1;
        return Integer.toString(index);
    }

    public String getHeaderIndex(By headers, String headerTitle) {
        List<String> headerList = getElements(headers, TIME_OUT_SECONDS)
                .stream().map(WebElement::getText).toList();
        int index = headerList.indexOf(headerTitle) + 1;
        return Integer.toString(index);
    }

    public void waitUntilSelectOptionsPopulated(final By by) {
        LOG.info("Waiting for options to be populated in {}", by.toString());
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(TIME_OUT_AWAIT_SECONDS),
                Duration.ofMillis(POLL_WAIT_MS));
        ExpectedCondition<Boolean> optionsPopulated = new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    Select select = new Select(driver.findElement(by));
                    return (select.getOptions().size() > 1);
                } catch (NoSuchElementException e) {
                    LOG.info("No such element. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                }
                return false;
            }
        };
        wait.until(optionsPopulated);
    }

    public void verifyTableElements(JsonArray arrayList, By element) {
        List<WebElement> rowHeader = getElements(element, TIME_OUT_SECONDS);
        List<String> stringArray = rowHeader.stream().map(WebElement::getText).toList();
        LOG.info(stringArray.toString());
        for (int i = 0; i < arrayList.size(); i++)
            Assert.assertTrue((stringArray.toString()).contains(arrayList.get(i).getAsString()),
                    "Actual: " + stringArray + " Expected: [" + arrayList.get(i).getAsString() + "]");
    }

    public boolean dateFormatBoolean(String dateInput) {
        String[] dateStrArray = dateInput.split(" ");
        LOG.info("This is the First Array..........." + dateStrArray[0]);
        String monthSubStr = dateStrArray[0].substring(0, dateStrArray[0].indexOf('/'));
        int monthInt = Integer.parseInt(monthSubStr);
        String dateSubStr = dateStrArray[0].substring((dateStrArray[0].indexOf('/') + 1),
                dateStrArray[0].lastIndexOf('/'));
        int dateInt = Integer.parseInt(dateSubStr);
        String yearSubStr = dateStrArray[0].substring((dateStrArray[0].lastIndexOf('/') + 1));
        int yearInt = Integer.parseInt(yearSubStr);
        return (monthInt >= 1 && monthInt <= 12) && (dateInt >= 1 && dateInt <= 31)
                && (yearInt >= 1900 && yearInt <= 2050);
    }

    public boolean timeFormatBoolean(String timeInput) {
        String[] dateStrArray = timeInput.split(" ");
        String hoursSubStr = dateStrArray[1].substring(0, dateStrArray[1].indexOf(':'));
        String amOrPmSubStr = dateStrArray[2];
        int numHours = Integer.parseInt(hoursSubStr);
        return (numHours > 0 && numHours < 13) && (amOrPmSubStr.equals("am") || amOrPmSubStr.equals("pm"));
    }

    public String getAttribute(By by, String attribute) {
        return getAttribute(by, TIME_OUT_SECONDS, attribute);
    }

    public String getAttribute(By by, int timeOutInSecs, String attribute) {
        waitForElementToBeVisible(by, timeOutInSecs);
        return getDriver().findElement(by).getAttribute(attribute);
    }

    public String getAttribute(WebElement element, String attribute) {
        return getAttribute(element, TIME_OUT_SECONDS, attribute);
    }

    public String getAttribute(WebElement element, int timeOutInSecs, String attribute) {
        waitForElementToBeVisible(element, timeOutInSecs);
        return element.getAttribute(attribute);
    }

    public void verifyCardFields(JsonArray arrayList, By element) {
        String card = getText(element, TIME_OUT_FOR_PAGE_LOAD);
        LOG.info("Card contains: " + card);
        for (int i = 0; i < arrayList.size(); i++) {
            Assert.assertTrue(card.contains(arrayList.get(i).getAsString()),
                    "The card details do not match with expected data - " + arrayList.get(i).getAsString());
        }
    }

    public void verifyScoreOrComments(String value, String expectedValue) {
        String reviewerName = getScenarioData().get("reviewer1").getAsString();
        switch (value) {
            case "Score":
                By scoreElement = By.xpath("//div[text()='" + reviewerName
                        + "']/following-sibling::div//strong[contains(text(),'" + expectedValue + "')]");
                List<WebElement> rowHeaderScore = getElements(scoreElement, TIME_OUT_SECONDS);
                List<String> stringArrayScore = rowHeaderScore.stream().map(WebElement::getText)
                        .toList();
                LOG.info(stringArrayScore.toString());
                for (String string : stringArrayScore) {
                    Assert.assertEquals(expectedValue, string);
                }
                break;
            case "Comments":
                By commentElement = By.xpath("//div[text()='" + reviewerName
                        + "']/following-sibling::div//strong[contains(text(),'" + expectedValue + "')]");
                List<WebElement> rowHeaderComment = getElements(commentElement, TIME_OUT_SECONDS);
                List<String> stringArrayComment = rowHeaderComment.stream().map(WebElement::getText)
                        .toList();
                LOG.info(stringArrayComment.toString());
                for (String s : stringArrayComment) {
                    Assert.assertEquals(expectedValue, s);
                }
        }

    }

    public void verifyText(By element, String text) {
        String actualText = getText(element, TIME_OUT_SECONDS);
        Assert.assertEquals(actualText, text);
    }

    public void addText(By by, String text) {
        click(by);
        getDriver().findElement(by).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE) + text + Keys.TAB);
    }

    public void refreshIfbyNotDisplayed(By by) {
        if (!isDisplayed(by, TIME_OUT_FOR_PAGE_LOAD)) {
            LOG.info("Refreshing the page");
            getDriver().navigate().refresh();
        }
    }

    public String getVersionId() {
        return getVersionId(Constants.CURRENT_TASK_VERSION_ID);
    }

    public String getVersionId(WebDriver driver) {
        return getVersionId(driver, Constants.CURRENT_TASK_VERSION_ID);
    }

    public String getVersionId(WebDriver driver, String script) {
        try {
            String versionId = ((JavascriptExecutor) driver).executeScript(script).toString();
            if (StringUtils.equals(versionId, "undefined")) {
                return null;
            }
            return versionId;
        } catch (JavascriptException | NullPointerException e) {
            LOG.info("Returning a null version ID because object does not exist yet for [{}]", script);
            return null;
        }
    }


    public String getVersionId(String script) {
        return getVersionId(getDriver(), script);
    }

    public Boolean waitForVersionIdUpdate(String origVersionId, int timeOutInSecs) {
        return waitForVersionIdUpdate(origVersionId, timeOutInSecs, Constants.CURRENT_TASK_VERSION_ID);
    }

    public Boolean waitForVersionIdUpdate(String origVersionId, int timeOutInSecs, String script) {
        LOG.info("Waiting for versionId Update [{}]", script);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs),
                Duration.ofMillis(POLL_WAIT_MS));
        ExpectedCondition<Boolean> versionIdUpdated = new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                String currVersionId = getVersionId(script);
                if (StringUtils.equals(origVersionId, currVersionId)) {
                    LOG.info("currVersionId: {}, waiting... [{}]", currVersionId, script);
                    return false;
                }
                LOG.info("currVersionId: {}, continuing... [{}]", currVersionId, script);
                return true;
            }
        };
        try {
            wait.until(versionIdUpdated);
            return true;
        } catch (Exception e) {
            LOG.warn("Waited {} seconds for versionId Update, continuing... [{}]", timeOutInSecs, script);
            return false;
        }
    }

    public Boolean waitForAdditionalVersionIdUpdate() {
        return waitForAdditionalVersionIdUpdate(TIME_OUT_DD_SECONDS, Constants.CURRENT_TASK_VERSION_ID);
    }

    public Boolean waitForAdditionalVersionIdUpdate(int timeOutInSecs) {
        return waitForAdditionalVersionIdUpdate(timeOutInSecs, Constants.CURRENT_TASK_VERSION_ID);
    }

    public Boolean waitForAdditionalVersionIdUpdate(String script) {
        return waitForAdditionalVersionIdUpdate(TIME_OUT_DD_SECONDS, script);
    }

    public Boolean waitForAdditionalVersionIdUpdate(int timeOutInSecs, String script) {
        String origVersionId = getVersionId(script);
        LOG.info("origVersionId: {} [{}]", origVersionId, script);
        Boolean waitResult = waitForVersionIdUpdate(origVersionId, timeOutInSecs, script);
        if (Boolean.FALSE.equals(waitResult))
            LOG.warn("Additional version update check has failed [{}]", script);
        return waitResult;
    }

    public void selectByValue(By by, String value) {
        selectByValue(by, value, TIME_OUT_SECONDS);
    }

    public void selectByValue(By by, String value, int timeOutInSecs) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSecs),
                Duration.ofMillis(POLL_WAIT_MS));
        ExpectedCondition<Boolean> optionSelected = new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                LOG.info("Attempting to Select by Value {} {}", by.toString(), value);
                try {
                    scrollIntoView(by);
                    (new Select(getDriver().findElement(by))).selectByValue(value);
                    return true;
                } catch (TimeoutException e) {
                    LOG.info("Timed out, could not selectByValue. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                } catch (StaleElementReferenceException e) {
                    LOG.info("Element stale. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                } catch (NoSuchElementException e) {
                    LOG.info("No such element. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                } catch (ElementClickInterceptedException e) {
                    LOG.warn("Element Click Intercepted. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                } catch (ElementNotInteractableException e) {
                    LOG.warn("Element Not Intractable. Retrying in {} seconds", POLL_WAIT_MS / 1000);
                }
                return false;
            }
        };
        wait.until(optionSelected);
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
        String origVersionId = getVersionId(script);
        LOG.info("origVersionId: {} [{}]", origVersionId, script);
        click(by);
        if (!waitForVersionIdUpdate(origVersionId, timeOutInSecs, script))
            LOG.warn("Version update check has failed for {} [{}]", by, script);
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
        String origVersionId = getVersionId(script);
        LOG.info("origVersionId: {} [{}]", origVersionId, script);
        click(elem);
        if (!waitForVersionIdUpdate(origVersionId, timeOutInSecs, script))
            LOG.warn("DynamicData version update check has failed for {} [{}]", elem, script);
    }

//    public void ddSendKeysTab(By by, String input) {
//        ddSendKeysTab(by, input, TIME_OUT_DD_SECONDS, Constants.CURRENT_TASK_VERSION_ID);
//    }
//
//    public void ddSendKeysTab(By by, String input, String script) {
//        ddSendKeysTab(by, input, TIME_OUT_DD_SECONDS, script);
//    }
//
//    public void ddSendKeysTab(By by, String input, int timeOutInSecs) {
//        ddSendKeysTab(by, input, timeOutInSecs, Constants.CURRENT_TASK_VERSION_ID);
//    }
//
//    public void ddSendKeysTab(By by, String input, int timeOutInSecs, String script) {
//        scrollIntoView(by, timeOutInSecs);
//        LOG.info("Clearing and ddSending Keys+Tab to Element {} with values {} [{}]", by, input, script);
//        String origVersionId = getVersionId(script);
//        LOG.info("origVersionId: {} [{}]", origVersionId, script);
//        sendKeysTab(by, input);
//        if (!waitForVersionIdUpdate(origVersionId, timeOutInSecs, script))
//            LOG.warn("DynamicData version update check has failed for {} [{}]", by, script);
//    }
//
//    public void ddSendKeysTab(WebElement element, String input) {
//        ddSendKeysTab(element, input, TIME_OUT_DD_SECONDS, Constants.CURRENT_TASK_VERSION_ID);
//    }
//
//    public void ddSendKeysTab(WebElement element, String input, String script) {
//        ddSendKeysTab(element, input, TIME_OUT_DD_SECONDS, script);
//    }
//
//    public void ddSendKeysTab(WebElement element, String input, int timeOutInSecs) {
//        ddSendKeysTab(element, input, timeOutInSecs, Constants.CURRENT_TASK_VERSION_ID);
//    }
//
//    public void ddSendKeysTab(WebElement element, String input, int timeOutInSecs, String script) {
//        scrollIntoView(element, timeOutInSecs);
//        LOG.info("Clearing and ddSending Keys+Tab to Element {} with values {} [{}]", element, input, script);
//        String origVersionId = getVersionId(script);
//        LOG.info("origVersionId: {} [{}]", origVersionId, script);
//        element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE) + input + Keys.TAB);
//        if (!waitForVersionIdUpdate(origVersionId, timeOutInSecs, script))
//            LOG.warn("DynamicData version update check has failed for {} [{}]", element, script);
//    }

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
        String origVersionId = getVersionId(script);
        selectByText(by, text);
        if (!waitForVersionIdUpdate(origVersionId, timeOutInSecs, script))
            LOG.warn("DynamicData version update check has failed for {} [{}]", by, script);
    }

    public void ddSelectByValue(By by, String text, String script) {
        ddSelectByValue(by, text, TIME_OUT_DD_SECONDS, script);
    }

    public void ddSelectByValue(By by, String text, int timeOutInSecs, String script) {
        String origVersionId = getVersionId(script);
        selectByValue(by, text);
        if (!waitForVersionIdUpdate(origVersionId, timeOutInSecs, script))
            LOG.warn("DynamicData version update check has failed for {} [{}]", by, script);
    }

    // TODO requires implementation
//    public void ddAttachDocument(JsonArray docList, String attachmentTableAnchor, By attachDocumentBtn, String script) {
//        methodToAttachUsingJs(attachmentTableAnchor, docList, attachDocumentBtn, script);
//        LOG.info("Validating the uploaded documents...");
//        getDriver().findElements(attachDocumentBtn);
//        List<String> rows = getElements(By.xpath(attachmentTableDataRows), TIME_OUT_SECONDS).stream()
//                .map(WebElement::getText).collect(Collectors.toList());
//        validateAttachmentDetails(docList, rows);
//    }
}
