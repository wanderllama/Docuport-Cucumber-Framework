package jw.demo.Listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.gherkin.model.Given;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import jw.demo.managers.DriverManager;
import jw.demo.managers.ExtentManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.util.HashMap;
import java.util.Map;

public class CucumberReportListener implements ConcurrentEventListener {

    private static final Logger LOG = LogManager.getLogger(CucumberReportListener.class);

    private ExtentReports extentReports;
    private Map<String, ExtentTest> feature = new HashMap<>();
    private ThreadLocal<ExtentTest> scenario = new ThreadLocal<>();
    private ThreadLocal<ExtentTest> step = new ThreadLocal<>();

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, this::runStarted);
        publisher.registerHandlerFor(TestRunFinished.class, this::runFinished);
        publisher.registerHandlerFor(TestSourceRead.class, this::featureRead);
        publisher.registerHandlerFor(TestCaseStarted.class, this::scenarioStarted);
        publisher.registerHandlerFor(TestStepStarted.class, this::stepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::stepFinished);
    }

    private void runStarted(TestRunStarted started) {
        LOG.debug("cucumber run started");
        extentReports = ExtentManager.getExtentReports();
    }

    private void runFinished(TestRunFinished testRunFinished) {
        LOG.debug("cucumber run finished");
        extentReports.flush();
    }

    private void featureRead(TestSourceRead sourceRead) {
    }

    private void scenarioStarted(TestCaseStarted scenarioStarted) {
        String featureName = scenarioStarted.getTestCase().getUri().toString().split(".*/")[1];
        if (feature.get(featureName) == null)
            feature.putIfAbsent(featureName, extentReports.createTest(featureName));
        scenario.set(feature.get(featureName).createNode(scenarioStarted.getTestCase().getName()));
    }

    private void stepStarted(TestStepStarted testStepStarted) {
        String stepName;
        String keyword = "Hook :";
        if (testStepStarted.getTestStep() instanceof PickleStepTestStep steps) {
//            PickleStepTestStep steps = (PickleStepTestStep) testStepStarted.getTestStep();
            stepName = steps.getStep().getText();
            keyword = steps.getStep().getKeyword();
        } else {
            HookTestStep hook = (HookTestStep) testStepStarted.getTestStep();
            stepName = hook.getHookType().name();
        }
        step.set(scenario.get().createNode(Given.class, keyword + " " + stepName));
    }

    private void stepFinished(TestStepFinished stepFinished) {
        if (stepFinished.getResult().getStatus() == Status.FAILED && (DriverManager.getDriver() != null)) {
            String base64Screenshot = "data:image/png;base64,"
                    + ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
            step.get().fail(stepFinished.getResult().getError(),
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
        }
        LOG.debug("cucumber run started");
        extentReports = ExtentManager.getExtentReports();
    }
}
