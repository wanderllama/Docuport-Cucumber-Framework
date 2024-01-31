package jw.demo.stepDefinition.docuportStepDefs;

import io.cucumber.core.exception.CucumberException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import jw.demo.enums.User;
import jw.demo.utils.TestContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class LoginSteps extends BaseStep {

    private static final Logger log = LogManager.getLogger(LoginSteps.class);

    @When("^user (.*) enters (email|password) in text field$")
    public void userEntersIntoTextField(User user, String dataType) {
        TestContext.getScenarioCtx().setUser(user);
        switch (dataType) {
            case "email"    -> access.homePage().userEntersEmail();
            case "password" -> access.homePage().userEntersPassword();
            default         -> throw new CucumberException(dataType + " is an invalid dataType");
        }
        log.info("successfully entered {} as email and {} as password",
                TestContext.getScenarioCtx().getUserEmail(), TestContext.getUserPassword());
    }

    @Given("^the (.*) is on Docuport (login|home) page$")
    public void theUserIsOnDocuportLoginPage(User user, String page) {
        System.out.println(Arrays.asList(getDriver().getClass().getFields()));
        TestContext.getScenarioCtx().setUser(user);
        switch (page) {
            case "login" -> access.homePage().userIsOnLoginPage();
            case "home"  -> access.homePage().userOnCorrectHomePage();
            default      -> throw new CucumberException(page + " is not a valid option");
        }
        log.info(String.format("%s is on the %s page", user.get(), page));
    }

    @And("user clicks {string} button")
    public void userClicksButton(String btn) {
        access.homePage().expectLoaderIconToDisappear();
        switch (btn) {
            case "login"    -> access.homePage().clickLogInBtn();
            case "continue" -> access.homePage().clickContinueAfterLogin();
            case "log out", "profile", "settings" -> access.homePage().clickUserSettingsOption(btn);
            default -> throw new CucumberException(btn + " is an invalid button type");
        }
        log.info(String.format("successfully clicked the %s button", btn));
//        access.homePage().userClicksBtn(btn);
    }


}
