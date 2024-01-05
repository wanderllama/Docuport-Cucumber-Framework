package jw.demo.config;

import jw.demo.enums.WebDriverBrowser;
import jw.demo.enums.WebDriverRunLocation;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This class represents the configuration for the application.
 * It contains various properties that are set using the @Value annotation from Spring Framework.
 * The values are captured from the path defined in @Value from src/test/resources/driver.properties
 * Used to configure the execution of scenarios.
 * If a required property is not found or is empty, a RuntimeException is thrown.
 */

@SuppressWarnings("CommentedOutCode")
@Getter
@Component
public class AppConfig {

    // environment
    private static String baseUrl;
    private static String apiUri;
    private static String envPasswd;
    // driver
    private static String driverPath;
    private static String driverFileDownloadPath;
    private static String driverRemoteUrl;
    private static String headless;
    private static Long driverImplicitlyWait;
    private static WebDriverBrowser webDriverBrowser;
    private static WebDriverRunLocation webDriverRunLocation;
    // trust store
    private static String trustStore;
    private static String trustStorePasswd;
    private static String trustStoreEnabled;
    // reporting
    private static String extentConfig;
    private static String getExtentConfigName;
    // data
    private static String dataFile;
    // proxy
    private static String driverProxyUrl;
    private static String driverNoProxyUrl;
    private static String driverNoProxyList;
    ConfigReader configReader;

    @Value("${base.url:#{null}}")
    private void setBaseUrl(String baseUrl) {
        if (StringUtils.isNotBlank(baseUrl))
            AppConfig.baseUrl = baseUrl;
        else
            throw new RuntimeException("base.url is not a valid property in driver.properties file");
    }

    @Value("${api.uri:#{null}}")
    public void setApiUri(String apiUri) {
        if (StringUtils.isNotBlank(apiUri))
            AppConfig.apiUri = apiUri;
        else
            throw new RuntimeException("api.uri is not a valid property in driver.properties file");
    }

    @Value("${env.passwd:#{null}}")
    public void setEnvPasswd(String envPasswd) {
        if (StringUtils.isNotBlank(envPasswd))
            AppConfig.envPasswd = envPasswd;
        else
            throw new RuntimeException("env.passwd is not a valid property in driver.properties file");
    }

    @Value("${driver.path:#{null}}")
    public void setDriverPath(String driverPath) {
        if (StringUtils.isNotBlank(driverPath))
            AppConfig.driverPath = driverPath;
        else
            throw new RuntimeException("driver.path is not a valid property in driver.properties file");
    }

    @Value("${driver.file.download.path:#{null}}")
    public void setDriverFileDownloadPath(String driverFileDownloadPath) {
        if (StringUtils.isNotBlank(driverFileDownloadPath))
            AppConfig.driverFileDownloadPath = driverFileDownloadPath;
        else
            throw new RuntimeException("driver.file.download.path is not a valid property in driver.properties file");
    }

    @Value("${driver.remote.url:#{null}}")
    public void setDriverRemoteUrl(String driverRemoteUrl) {
        if (StringUtils.isNotBlank(driverRemoteUrl))
            AppConfig.driverRemoteUrl = driverRemoteUrl;
        else
            throw new RuntimeException("driver.remote.url is not a valid property in driver.properties file");
    }

    @Value("${headless:#{null}}")
    public void setHeadless(String headless) {
        if (StringUtils.isNotBlank(headless))
            AppConfig.headless = driverRemoteUrl;
        else
            throw new RuntimeException("headless is not a valid property in driver.properties file");
    }


    @Value("${driver.implicitlyWait:#{null}}")
    public void setDriverImplicitlyWait(String driverImplicitlyWait) {
        if (StringUtils.isNotBlank(driverImplicitlyWait))
            AppConfig.driverImplicitlyWait = Long.parseLong(driverImplicitlyWait);
        else
            throw new RuntimeException("driver.implicitlyWait is not a valid property in driver.properties file");
    }

    @Value("${webDriver.browser.type:#{null}}")
    public void setWebDriverBrowser(String webDriverBrowser) {
        switch (webDriverBrowser.trim().toLowerCase()) {
            case "chrome":
                AppConfig.webDriverBrowser = WebDriverBrowser.CHROME;
                break;
            case "firefox":
                AppConfig.webDriverBrowser = WebDriverBrowser.FIREFOX;
                break;
            case "edge":
                AppConfig.webDriverBrowser = WebDriverBrowser.EDGE;
                break;
            default:
                throw new RuntimeException(String.format("webDriver.browser.type '%s' in driver.properties is not an acceptable webDriver browser", webDriverBrowser));
        }
    }

    @Value("${webDriver.run.location:#{null}}")
    public void setWebDriverRunLocation(String webDriverRunLocation) {
        if (StringUtils.equals(webDriverRunLocation, "remote"))
            AppConfig.webDriverRunLocation = WebDriverRunLocation.REMOTE;
        else
            AppConfig.webDriverRunLocation = WebDriverRunLocation.LOCAL;
    }

    @Value("${truststore:#{null}}")
    public void setTrustStore(String trustStore) {
        if (StringUtils.isNotBlank(trustStore))
            AppConfig.trustStore = trustStore;
//        else
//            throw new RuntimeException("truststore is not a valid property in driver.properties file");
    }

    @Value("${truststore.password:#{null}}")
    public void setTrustStorePasswd(String trustStorePasswd) {
        if (StringUtils.isNotBlank(trustStorePasswd))
            AppConfig.trustStorePasswd = trustStorePasswd;
//        else
//            throw new RuntimeException("trustStore.password is not a valid property in driver.properties file");
    }

    @Value("${truststore.isEnabled:#{null}}")
    public void setTrustStoreEnabled(String trustStoreEnabled) {
        if (StringUtils.isNotBlank(trustStoreEnabled))
            AppConfig.trustStoreEnabled = trustStoreEnabled;
        AppConfig.trustStorePasswd = "false";
//        else
//            throw new RuntimeException("truststore.isEnabled is not a valid property in driver.properties file");
    }

    @Value("${extent.config:#{null}}")
    public void setExtentConfig(String extentConfig) {
        if (StringUtils.isNotBlank(extentConfig))
            AppConfig.extentConfig = extentConfig;
        else
            throw new RuntimeException("extent.config is not a valid property in driver.properties file");
    }

    @Value("${extent.config.name:#{null}}")
    public void setGetExtentConfigName(String getExtentConfigName) {
        if (StringUtils.isNotBlank(getExtentConfigName))
            AppConfig.getExtentConfigName = getExtentConfigName;
        else
            throw new RuntimeException("extent.config.name is not a valid property in driver.properties file");
    }

    @Value("${data.file:#{null}}")
    public void setDataFile(String dataFile) {
        if (StringUtils.isNotBlank(dataFile))
            AppConfig.dataFile = dataFile;
        else
            throw new RuntimeException("data.file is not a valid property in driver.properties file");
    }


    @Value("${driver.proxy.url:#{null}}")
    public void setDriverProxyUrl(String driverProxyUrl) {
        if (StringUtils.isNotBlank(driverProxyUrl))
            AppConfig.driverProxyUrl = driverProxyUrl;
//        else
//            throw new RuntimeException("driver.proxy.url is not a valid property in driver.properties file");
    }

    @Value("${driver.noProxy.url:#{null}}")
    public void setDriverNoProxyUrl(String driverNoProxyUrl) {
        if (StringUtils.isNotBlank(driverNoProxyUrl))
            AppConfig.driverNoProxyUrl = driverNoProxyUrl;
//        else
//            throw new RuntimeException("driver.noProxy.url is not a valid property in driver.properties file");
    }

    @Value("${driver.noProxy.list:#{null}}")
    public void setDriverNoProxyList(String driverNoProxyList) {
        if (StringUtils.isNotBlank(driverNoProxyList))
            AppConfig.driverNoProxyList = driverNoProxyList;
//        else
//            throw new RuntimeException("driver.noProxy.list is not a valid property in driver.properties file");
    }
}
