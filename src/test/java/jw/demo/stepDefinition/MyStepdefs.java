package jw.demo.stepDefinition;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
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

}
