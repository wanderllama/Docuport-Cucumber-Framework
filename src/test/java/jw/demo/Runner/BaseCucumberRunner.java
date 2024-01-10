package jw.demo.Runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import jw.demo.config.ConfigReader;
import jw.demo.managers.DriverManager;
import jw.demo.managers.ExtentManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class BaseCucumberRunner extends AbstractTestNGCucumberTests {

    private static final Logger LOG = LogManager.getLogger(BaseCucumberRunner.class);

    @BeforeSuite(alwaysRun = true)
    public void setUp() {
        LOG.warn("====== Before Suite Global Setup ======");
        ConfigReader ready = new ConfigReader();
        DriverManager.init();
        ExtentManager.init();
//        TestContext.initGlobal();
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() {
        LOG.debug("After Suite Global Setup");
    }
}
