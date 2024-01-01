package jw.demo.managers;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import jw.demo.utils.LogException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public final class ExtentManager {

    private static final Logger LOG = LogManager.getLogger(ExtentManager.class);
    private static ExtentReports extentReports;
    private static String reportFileName;
    private ExtentManager() {
        throw new IllegalStateException("Class is not designed for object creation");
    }

    public static ExtentReports getExtentReports() {
        if (extentReports == null)
            extentReports = createReporter();
        return extentReports;
    }

    public static ExtentReports createReporter() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFileName);
        ExtentReports extentReports = new ExtentReports();
        try {
            sparkReporter.loadXMLConfig(FileReaderManager.getInstance().getConfigReader().getExtentConfig());
        } catch (IOException e) {
            LogException.errorMessage(LOG, e);
        }
        extentReports.attachReporter(sparkReporter);
        return extentReports;
    }

    public static void init() {
        reportFileName = FileReaderManager.getInstance().getConfigReader().getGetExtentConfigName();
    }
}
