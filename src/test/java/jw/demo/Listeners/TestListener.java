package jw.demo.Listeners;

import jw.demo.managers.DriverManager;
import jw.demo.utils.TestContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

@SuppressWarnings("UnnecessaryBoxing")
public class TestListener implements ITestListener {

    private static final Logger LOG = LogManager.getLogger(TestListener.class);
    private static final double PASS_RATE = .90;

    private static String scenarioName(String scenarioName) {
        return scenarioName == null ? "" : scenarioName;
    }

    @Override
    public void onStart(ITestContext context) {
        LOG.debug("============= Test Suite Started =============");
    }

    @Override
    public void onTestStart(ITestResult result) {
        LOG.debug("=========== Starting Test Scenario ===========");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        result.getTestContext().setAttribute("success", 1 +
                Integer.valueOf(result.getTestContext().getAttribute("success").toString()));
        LOG.debug("=========== Scenario [{}] Executed Successfully ===========",
                scenarioName(TestContext.getScenario().getName()));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        result.getTestContext().setAttribute("failure", 1 +
                Integer.valueOf(result.getTestContext().getAttribute("failure").toString()));
        LOG.debug("=========== Scenario [{}] Failed Execution ===========",
                scenarioName(TestContext.getScenario().getName()));
        DriverManager.shutdownDriver();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        result.getTestContext().setAttribute("skip", 1 +
                Integer.valueOf(result.getTestContext().getAttribute("skip").toString()));
        LOG.debug("=========== Scenario: [{}] Skipped ===========",
                scenarioName(TestContext.getScenario().getName()));
    }

    public void onTestFailedButWithinSuccessPercentage(ITestContext result) {
        int passRate = (Integer.valueOf((String) result.getAttribute("success")) +
                Integer.valueOf((String) result.getAttribute("failure")) +
                Integer.valueOf((String) result.getAttribute("skip")))
                / Integer.valueOf((String) result.getAttribute("success"));
        LOG.debug("=========== Suite Passed Since Pass Rate is [{}] ===========", passRate + "%");
        LOG.debug("============= Suite Has [{}] Failed Scenarios =============",
                Integer.valueOf((String) result.getAttribute("failure")));
        LOG.debug("============= Suite Has [{}] Skipped Scenarios ============",
                Integer.valueOf((String) result.getAttribute("skip")));
    }

    @Override
    public void onFinish(ITestContext context) {
        LOG.debug("=========== Test Suite Finished ===========\n");
    }
}
