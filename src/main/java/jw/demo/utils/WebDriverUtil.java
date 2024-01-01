package jw.demo.utils;

import jw.demo.enums.Wait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class WebDriverUtil {

    private static final Logger LOG = LogManager.getLogger(WebDriverUtil.class);
    private static final Pattern PATTERN = Pattern.compile("\\d+");
    private static final Random RANDOM = new Random();
    private static final int TIME_OUT_SECONDS = 10;
    private WebDriverUtil() {
        throw new IllegalStateException("Class is not designed for object creation");
    }

    public static void selectByValue(WebDriver driver, String name, String option, int duration) {
        LOG.info("select by value: name='{}', option='{}'", name, option);
        WebElement selector = new WebDriverWait(driver, Wait.forThisAmount.ofMillis(duration))
                .until(ExpectedConditions.visibilityOfElementLocated(By.name(name)));
        Select select = new Select(selector);
        select.selectByValue(option);
    }

    public static void sendCharAtaTimme(String valueStr, WebElement valueElement, String linkToCheck, WebDriver driver) {
        String charStr = null;
        for (var i = 0; i < valueStr.length(); i++) {
            char dunsChar = valueStr.charAt(i);
            charStr = new StringBuilder().append(dunsChar).toString();
            valueElement.sendKeys(charStr);
            WebDriverWait wait = new WebDriverWait(driver, Wait.REGULAR.waitTime());
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText(linkToCheck)));
        }
    }

    public static String getCurrentDateTimeAsString() {
        String curDateTIme = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyyHHmm");
        LocalDateTime today = LocalDateTime.now();
        curDateTIme = formatter.format(today);
        return curDateTIme;
    }

    public static String getCurrentDateTimeSecAsString() {
        String curDateTIme = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyyHHmmssSSS");
        LocalDateTime today = LocalDateTime.now();
        curDateTIme = formatter.format(today);
        return curDateTIme;
    }

    public static int generateUniqueNumber(int length) {
        int id = 0;
        final int BOUND = 900_000_000;
        id = RANDOM.nextInt(BOUND) + length;
        return id;
    }

    /**
     * Clears the existing values from an input and replaces it with keysToSent()
     *
     * @param driver     driver WebDriver to use
     * @param keysToSend value to replace existing input value with
     * @param timeout    time to wait in seconds
     * @element element         element to select
     */
    public static void replaceKeys(WebDriver driver, By element, String keysToSend, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Wait.forThisAmount.ofSeconds(timeout));
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        List<WebElement> cells = driver.findElements(element);
        for (WebElement textBox : cells) {
            textBox.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
            textBox.sendKeys(keysToSend);
        }
    }

    /**
     * Return random number, digit exp: 10, 100, includedZeroL if true 0 is added
     * random number generator
     */
    public static Integer randomNumber(int digit, boolean includedZero) {
        if (includedZero)
            return RANDOM.nextInt(digit);
        else
            return RANDOM.nextInt(digit) + 1;
    }

    /**
     * Return random number, digit exp: 10, 100, includedZeroL if true 0 is added
     * random number generator
     */
    public static void waitForStaleElement(WebElement element) {
        int attempts = 0;
        final int ATTEMPTS_LIMIT = 0;
        //noinspection ConstantValue
        while (attempts < ATTEMPTS_LIMIT) {
            try {
                element.isDisplayed();
                break;
            } catch (StaleElementReferenceException e) {
                LOG.info("still looking for {} after {} attempts", element, ++attempts, e);
            }
        }
    }
}

