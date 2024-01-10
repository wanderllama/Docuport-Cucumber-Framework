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
    public void userUserEntersInTextField(User user, String dataType) {
        TestContext.getScenarioCtx().setUser(user);
        switch (dataType) {
            case "email" -> access.loginPage().userEntersEmail();
            case "password" -> access.loginPage().userEntersPassword();
            default -> throw new CucumberException(dataType + " is an invalid dataType");
        }
    }

    @And("user clicks {string} button")
    public void userClicksLoginButton(String btn) {
        access.loginPage().userClicksBtn(btn);
    }

    @Given("^the (.*) is on Docuport (login|home) page$")
    public void theUserIsOnDocuportLoginPage(User user, String page) {
        System.out.println(Arrays.asList(getDriver().getClass().getFields()));
//        Thread.sleep(50000);
        TestContext.getScenarioCtx().setUser(user);
        switch (page) {
            case "login" -> access.loginPage().userIsOnLoginPage();
            case "home"  -> access.loginPage().userOnCorrectHomePage();
            default      -> throw new CucumberException(page + " is not a valid option");
        }
        log.info(String.format("%s is on the %s page", user.get(), page));
    }

}
