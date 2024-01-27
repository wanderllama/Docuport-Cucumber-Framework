package jw.demo.stepDefinition.docuportStepDefs;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import jw.demo.managers.DriverManager;
import jw.demo.utils.TestContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Hooks extends BaseStep {

    private static final Logger LOG = LogManager.getLogger(Hooks.class);
    private static final String DEBUG = System.getProperty("debug");

    @Before(order = 1)
    public static void beforeGlobal(Scenario scenario) {
        if (!TestContext.globalSetupComplete()) {
            TestContext.initGlobal();
            LOG.info("Global Setup Complete");
        }
    }

    @Before(order = 2)
    public static void beforeScenario(Scenario scenario) {
        if (DriverManager.getDriver() == null) {
            DriverManager.createDriver();
        }
        DriverManager.getDriver().manage().window().maximize();
        TestContext.initScenario(scenario);
    }

    @After(order = 1)
    public static void afterScenario(Scenario scenario) {
        TestContext.setScenarioName();
        DriverManager.shutdownDriver();
    }
}
