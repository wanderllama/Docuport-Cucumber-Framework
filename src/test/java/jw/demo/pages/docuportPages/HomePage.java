package jw.demo.pages.docuportPages;

import jw.demo.constants.Constants;
import jw.demo.enums.Wait;
import jw.demo.utils.TestContext;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

@Getter
public class HomePage extends BasePage {

    private static final Logger log = LogManager.getLogger(HomePage.class);

    //            "//label[text()='Username or email']/../..");
    private final By userNameField = By.tagName("input");
    //        "//label[text()='Password']/..//following-sibling::input");
    private final By passwordField = By.tagName("input");
    private final By login = By.xpath("//form//parent::div/h1");
    private final By loginErrorMessage = By.xpath("//span[text()='Username or password is incorrect']");

    public void userIsOnLoginPage() {
        getDriver().get(TestContext.getBaseUrl());
        expectElementToBeVisible(login);
    }

    public void userEntersPassword() {
        log.info("entering password: " + TestContext.getUserPassword());
        sendKeys(getDriver().findElement(with(userNameField)
                .below(passwordField)), TestContext.getUserPassword(), Wait.TWO.seconds());
    }

    public void userEntersEmail() {
        log.info("entering username: " + TestContext.getScenarioCtx().getUserEmail());
        sendKeysBlur(userNameField, TestContext.getScenarioCtx().getUserEmail());
    }

    public void userOnCorrectHomePage() {
        String[] batchGroupNum = TestContext.getGroup()
                .replaceAll("[a-z A-Z]", "").split("");

        String expectedUser = "Batch" + batchGroupNum[0] + " Group" + batchGroupNum[1];
        String actualUser = getText(userProfileText);

        Assert.assertEquals(actualUser, expectedUser,
                String.format("\nactual user: %s\nexpected user: %s\n", actualUser, expectedUser));
    }

    public void clickLogInBtn() {
        simpleClick(logInBtn);
        if (doElementsExist(loginErrorMessage)) {
            access.homePage().userEntersEmail();
            access.homePage().userEntersPassword();
            simpleClick(logInBtn);
        }
    }

    public void clickUserSettingsOption(String option) {
        click(userSettings);
        switch (option) {
            case "profile" -> click(profileBtn);
            case "log out" -> logOut();
            case "settings" -> click(userSettings);
//            case "accounts" -> click();
//            case "change password" -> click();
            default -> throw new RuntimeException(option + " is not a valid option in user settings dropdown");
        }
    }

    private void logOut() {
        getJSExecutor().executeScript(Constants.LOG_OUT);
        log.info("user has logged out");
    }

    public void clickContinueAfterLogin() {
        click(continueBtn, Wait.FIFTEEN.seconds());
        if (checkIfDisplayed(continueBtn)) {
            simpleClick(continueBtn);
        }
    }
}