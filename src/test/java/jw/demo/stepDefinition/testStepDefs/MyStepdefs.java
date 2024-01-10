package jw.demo.stepDefinition.testStepDefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import jw.demo.utils.TestContext;

public class MyStepdefs {


    @Given("test runs")
    public void testRuns() {
        System.out.println(TestContext.getScenario().getName());
    }

    //    @ParameterType("skip | pass | fail}")
    @And("^test (skip|pass|fail)$")
    public void test(String result) {
        switch (result) {
            case "skip" -> System.out.println("skip");
            case "fail" -> System.out.println("fail");
            case "pass" -> System.out.println("pass");
        }
    }

//    @Given("get token test")
//    public void getTokenTest() {
//
//    }

    @Then("token as jsonobject in test context")
    public void tokenAsJsonobjectInTestContext() {
    }

    @Given("^the (.*) logs in to docuport$")
    public void theUserLogsInToDocuport() {
    }
}
