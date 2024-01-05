package jw.demo.Listeners;

import jw.demo.utils.TestContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.ArrayList;
import java.util.List;

public class RetrySetup implements IRetryAnalyzer {

    private static final Logger LOG = LogManager.getLogger(RetrySetup.class);
    private static final int maxRetryCount = getMaxRetryCount();
    private static List<String> scenariosFailedNames = new ArrayList<>();

    private static int getMaxRetryCount() throws NullPointerException {
        int maxRetry;
        try {
            maxRetry = Integer.parseInt(System.getProperty("maxRetryCount"));
            System.out.println("\nsetting maxRetryCount to [" + maxRetry + "]\n");
        } catch (NullPointerException | IllegalStateException e) {
            System.out.println("\nmaxRetryCount not set, defaulting to [0]\n");
            maxRetry = 0;
        }
        return maxRetry;
    }

    ResultStatus getResultStatusName(int status) {
        return (switch (status) {
            case 1 -> ResultStatus.SUCCESS;
            case 2 -> ResultStatus.FAILURE;
            case 3 -> ResultStatus.SKIP;
            default -> throw new Error("invalid status type detected");
        });
    }

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {
            final String currentScenarioName = TestContext.getScenario().getName();
            final int numScenarioPreviouslyAppears = getNumScenarioPreviouslyAppears(currentScenarioName);
            if (numScenarioPreviouslyAppears < maxRetryCount) {
                LOG.info("retrying scenario [{}], retryCount = {}, maxRetry = {}", currentScenarioName,
                        numScenarioPreviouslyAppears, maxRetryCount);
                iTestResult.setStatus(iTestResult.SKIP);
                return Boolean.TRUE;
            } else {
                LOG.info("max retry reached for scenario [{}], retryCount = {}, maxRetry = {}", currentScenarioName,
                        numScenarioPreviouslyAppears, maxRetryCount);
                iTestResult.setStatus(ITestResult.FAILURE);
            }
        } else {
            iTestResult.setStatus(ITestResult.SUCCESS);
        }
        return Boolean.FALSE;
    }

    private int getNumScenarioPreviouslyAppears(String currentScenarioName) {
        StringBuilder failedScenarioNameFormatted = new StringBuilder("\n\nfailed scenarios list:");
        int numScenarioPreviouslyAppears = 0;
        List<String> previouslyFailedScenarios = new ArrayList<>(scenariosFailedNames);
        for (String previouslyFailedScenario : previouslyFailedScenarios) {
            failedScenarioNameFormatted.append("\n -").append(previouslyFailedScenario);
            if (previouslyFailedScenario.equals(currentScenarioName)) {
                numScenarioPreviouslyAppears++;
            }
        }
        scenariosFailedNames.add(currentScenarioName);
        failedScenarioNameFormatted.append("\n -").append(currentScenarioName);
        LOG.warn("{}\n", failedScenarioNameFormatted.toString());
        return numScenarioPreviouslyAppears;
    }

    private enum ResultStatus {
        SUCCESS, FAILURE, SKIP;
    }
}
