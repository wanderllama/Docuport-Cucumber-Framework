package jw.demo.config;

import jw.demo.enums.WebDriverBrowser;
import jw.demo.enums.WebDriverRunLocation;
import jw.demo.utils.LogException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Logger LOG = LogManager.getLogger(ConfigReader.class);
    private static final String DRIVER_PROPERTY_FILE_PATH = "src/test/resources/driver.properties";
    private static Properties properties;

    public String getProperty(String key) {
        if (properties == null) {
            loadProperties();
        }
        return properties.getProperty(key);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private void loadProperties() {
        LOG.info("Loading Configuration Properties");
        try {
            InputStream driverProp = Thread.currentThread().getContextClassLoader().getResourceAsStream(DRIVER_PROPERTY_FILE_PATH);
            properties = new Properties();
            properties.load(driverProp);
        } catch (IOException e) {
            LogException.errorMessage(LOG, "Loading driver.properties file failed in ConfigReader class", e);
            e.printStackTrace();
        }
    }

    public String getBaseUrl() {
        String baseUrl = getProperty("base.url");
        if (baseUrl == null)
            throw new RuntimeException("base.url has not be assigned a value in driver.properties");
        return baseUrl;
    }

    public String getApiUri() {
        String apiUri = getProperty("api.uri");
        if (apiUri == null)
            throw new RuntimeException("api.uri has not be assigned a value in driver.properties");
        return apiUri;
    }

    public String getEnvPasswd() {
        String envPasswd = getProperty("env.passwd");
        if (envPasswd == null)
            throw new RuntimeException("env.passwd has not be assigned a value in driver.properties");
        return envPasswd;
    }

    public String getDriverPath() {
        String driverPath = getProperty("driver.path");
        if (driverPath == null)
            throw new RuntimeException("driver.path has not be assigned a value in driver.properties");
        return driverPath;
    }

    public String getDriverFileDownloadPath() {
        String driverPath = getProperty("driver.file.download.path");
        if (driverPath == null)
            throw new RuntimeException("driver.file.download.path has not be assigned a value in driver.properties");
        return driverPath;
    }

    public String getDriverRemoteUrl() {
        String driverRemoteUrl = getProperty("driver.remote.url");
        if (driverRemoteUrl == null)
            throw new RuntimeException("driver.remote.url has not be assigned a value in driver.properties");
        return driverRemoteUrl;
    }

    public boolean getHeadless() {
        String headless = getProperty("headless");
        if (headless == null || headless.equals("false")) {
            return false;
        }
        return true;
    }

    public long getDriverImplicitlyWait() {
        String driverImplicitlyWait = getProperty("driver.implicitlyWait");
        if (driverImplicitlyWait == null)
            throw new RuntimeException("driver.implicitlyWait has not be assigned a value in driver.properties");
        return Long.parseLong(driverImplicitlyWait);
    }


    public WebDriverBrowser getWebDriverBrowser() {
        String webDriverBrowserProp = getProperty("webDriver.browser.type");
        if (webDriverBrowserProp == null)
            throw new RuntimeException("webDriver.browser.type has not be assigned a value in driver.properties");
        WebDriverBrowser webDriverBrowser = null;
        webDriverBrowser = switch (webDriverBrowserProp.trim().toLowerCase()) {
            case "chrome" -> WebDriverBrowser.CHROME;
            case "firefox" -> WebDriverBrowser.FIREFOX;
            case "edge" -> WebDriverBrowser.EDGE;
            default ->
                    throw new RuntimeException("webDriver.browser.type " + webDriverBrowserProp + " in driver.properties is not an acceptable webDriver browser");
        };
        return webDriverBrowser;
    }

    public WebDriverRunLocation getWebDriverRunLocation() {
        String webDriverRunLocationProp = getProperty("driver.remote.url");
        if (webDriverRunLocationProp == null)
            throw new RuntimeException("env.passwd has not been assigned a value in driver.properties");
        else if (webDriverRunLocationProp.trim().equalsIgnoreCase("remote"))
            return WebDriverRunLocation.REMOTE;
        else if (webDriverRunLocationProp.trim().equalsIgnoreCase("local"))
            return WebDriverRunLocation.LOCAL;
        throw new RuntimeException("env.passwd has invalid value in driver.properties");
    }

    public String getTrustStore() {
        String trustStore = getProperty("truststore");
        if (trustStore == null)
            throw new RuntimeException("truststore has not be assigned a value in driver.properties");
        return trustStore;
    }

    public String getTrustStorePasswd() {
        String trustStorePasswd = getProperty("truststore.password");
        if (trustStorePasswd == null)
            throw new RuntimeException("truststore.password has not be assigned a value in driver.properties");
        return trustStorePasswd;
    }


    public Boolean getTrustStoreEnabled() {
        String trustStoreEnabled = getProperty("trustStore.isEnabled");
        if (trustStoreEnabled == null)
            throw new RuntimeException("trustStore.isEnabled has not be assigned a value in driver.properties");
        return Boolean.parseBoolean(trustStoreEnabled);
    }

    public String getExtentConfig() {
        String extentConfig = getProperty("extent.config");
        if (extentConfig == null)
            throw new RuntimeException("extent.config has not be assigned a value in driver.properties");
        return extentConfig;
    }

    public String getGetExtentConfigName() {
        String getExtentConfigName = getProperty("extentConfig.name");
        if (getExtentConfigName == null)
            throw new RuntimeException("extentConfig.name has not be assigned a value in driver.properties");
        return getExtentConfigName;
    }

    public String getDataFile() {
        String dataFile = getProperty("data.file");
        if (dataFile == null)
            throw new RuntimeException("data.file has not be assigned a value in driver.properties");
        return dataFile;
    }

    public String getDriverProxyUrl() {
        String driverProxyUrl = getProperty("driver.proxy.url");
        if (driverProxyUrl == null)
            throw new RuntimeException("driver.proxy.url has not be assigned a value in driver.properties");
        return driverProxyUrl;
    }

    public String getDriverNoProxyUrl() {
        String driverNoProxyUrl = getProperty("driver.noProxy.url");
        if (driverNoProxyUrl == null)
            throw new RuntimeException("driver.noProxy.url has not be assigned a value in driver.properties");
        return driverNoProxyUrl;
    }

    public String getDriverNoProxyList() {
        String driverNoProxyList = getProperty("driver.noProxy.list");
        if (driverNoProxyList == null)
            throw new RuntimeException("driver.noProxy.list has not be assigned a value in driver.properties");
        return driverNoProxyList;
    }
}
