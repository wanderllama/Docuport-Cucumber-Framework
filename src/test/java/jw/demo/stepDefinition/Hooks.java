package jw.demo.stepDefinition;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Hooks extends BaseStep {

    private static final Logger LOG = LogManager.getLogger(Hooks.class);
    private static final String DEBUG = System.getProperty("debug");

    @Before(order = 1)
    public static void beforeScenario(Scenario scenario) {

    }

    @After(order = 1)
    public static void afterScenario(Scenario scenario) {

    }
}
