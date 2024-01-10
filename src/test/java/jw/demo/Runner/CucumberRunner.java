package jw.demo.Runner;

import io.cucumber.testng.CucumberOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;


@CucumberOptions(plugin = {"pretty"
        , "json:target/cucumber-report.json"
        , "html:target/cucumber-report.html"
        , "junit:target/cucumber-reports/Cucumber.xml"
        , "jw.demo.Listeners.CucumberReportListener"
        , "timeline:target/logs"
}
        , features = {"src/test/resources/features/"}
        , glue = {"jw.demo"}
        , monochrome = false
        , dryRun = false
        , tags = "@login"
)
public class CucumberRunner extends BaseCucumberRunner {

    private static final Logger LOG = LogManager.getLogger(CucumberRunner.class);

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        LOG.debug("TestNGCucumberRunner.scenarios() has been called");
        return super.scenarios();
    }
}
