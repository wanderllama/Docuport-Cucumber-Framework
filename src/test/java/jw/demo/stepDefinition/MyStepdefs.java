package jw.demo.stepDefinition;

import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

public class MyStepdefs {


    @Given("test runs")
    public void testRuns(Scenario scenario) {
        System.out.println(scenario.getName());
    }

    @And("test {skip | pass | fail}")
    public void test(String result) {
        switch (result) {
            case "skip", "fail" -> throw new RuntimeException();
            case "pass" -> System.out.println("pass");
        }
    }

}
