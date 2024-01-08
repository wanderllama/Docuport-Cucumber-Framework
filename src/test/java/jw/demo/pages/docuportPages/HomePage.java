package jw.demo.pages.docuportPages;

import io.cucumber.core.exception.CucumberException;
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
    private final By loginErrorMessage = By.xpath("//i[text()='Username or password is incorrect']");

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

    public void userClicksBtn(String btn) {
        expectLoaderIconToDisappear();
        switch (btn) {
            case "login"    -> loggingIn(logInBtn);
            case "continue" -> clickContinueAfterLogin();
            case "settings" -> click(userSettings);
            case "log out", "profile" -> clickUserSettingsOption(btn);
            default -> throw new CucumberException(btn + " is an invalid button type");
        }
        log.info(String.format("successfully clicked the %s button", btn));
    }

    private void clickContinueAfterLogin() {
        if (checkIfDisplayed(continueBtn, Wait.TEN.seconds())) {
            click(continueBtn, Wait.TEN.seconds());
        }
    }

    private void loggingIn(By logInBtn) {
        simpleClick(logInBtn);
        int i = doElementsExist(loginErrorMessage) ? 6 : 0;
        while (i-- > 0) {
            userEntersEmail();
            userEntersPassword();
            i = doElementsExist(loginErrorMessage) ? i - 1 : 0;
            simpleClick(logInBtn);
        }
        if (i == 0) {
            Assert.fail("timeout");
        }
    }

    public void userOnCorrectHomePage() {
        String[] batchGroupNum = TestContext.getGroup()
                .replaceAll("[a-z A-Z]", "").split("");

        String expectedUser = "Batch" + batchGroupNum[0] + " Group" + batchGroupNum[1];
        String actualUser = getText(userProfileText);

        Assert.assertEquals(actualUser, expectedUser,
                String.format("\nactual user: %s\nexpected user: %s\n", actualUser, expectedUser));
    }
}