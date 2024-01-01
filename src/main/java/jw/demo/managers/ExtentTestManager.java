package jw.demo.managers;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {

    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static ExtentReports extentReports = ExtentManager.getExtentReports();

    private static ExtentTest getTest() {
        return test.get();
    }

    public static void endTest() {
        extentReports.flush();
        test.remove();
    }

    public static ExtentTest startTest(String testName) {
        test.set(extentReports.createTest(testName));
        return test.get();
    }
}
