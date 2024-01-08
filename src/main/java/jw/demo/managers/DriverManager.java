package jw.demo.managers;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import jw.demo.enums.WebDriverBrowser;
import jw.demo.enums.WebDriverRunLocation;
import jw.demo.utils.TestContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

@SuppressWarnings("DuplicatedCode")
public final class DriverManager {

    private static final Logger LOG = LogManager.getLogger(DriverManager.class);
    private static ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
    private static ThreadLocal<File> defaultDownload = new ThreadLocal<>();
    private static AtomicInteger sequence = new AtomicInteger(0);
    private static WebDriverRunLocation webDriverRunLocation;
    private static WebDriverBrowser webDriverBrowser;
    private static String driverFileDownloadPath;
    private static String driverRemoteUrl;
    private static String driverNoProxyList;
    private static String driverProxyUrl;
    private static String driverPath;
    private static Boolean headless;
    private static int resolutionX;
    private static int resolutionY;
    private DriverManager() {
        throw new IllegalStateException("Class is not designed for object creation");
    }

    public static void init() {
        final var RESOLUTION_DIMENSIONS = 2;
        LOG.info("Initializing Driver Manager");
        webDriverBrowser = TestContext.getWebDriverBrowser();
        webDriverRunLocation = TestContext.getWebDriverRunLocation();
        LOG.info("Using driver.location as: {}", webDriverRunLocation);
        String driverResolution = FileReaderManager.getInstance().getConfigReader().getDriverResolution();
        if (StringUtils.isNotBlank(driverResolution)) {
            String[] resolutions = driverResolution.split("x");
            if (resolutions.length == RESOLUTION_DIMENSIONS) {
                LOG.info("Using driver.resolution as: {}", driverResolution);
                resolutionX = Integer.parseInt(resolutions[0]);
                resolutionY = Integer.parseInt(resolutions[1]);
            } else {
                throw new IllegalArgumentException("Invalid value assigned to driver.resolution");
            }
        }
        headless = FileReaderManager.getInstance().getConfigReader().getHeadless();
        LOG.info(String.format("is run using headless browser: %s", headless));
        if (webDriverRunLocation == WebDriverRunLocation.REMOTE) {
            driverRemoteUrl = FileReaderManager.getInstance().getConfigReader().getDriverRemoteUrl();
            LOG.info(String.format("%s is being used as the remote url", driverRemoteUrl));
        } else {
            driverPath = FileReaderManager.getInstance().getConfigReader().getDriverPath();
            if (StringUtils.isNotBlank(driverPath)) {
                LOG.info(String.format("%s is filepath to the driver used for testing", driverPath));
                var permissionFile = new File(driverPath);
                checkDriverPermissions(permissionFile);
            } else {
                LOG.info("driver.path is not specified, defaulting to WDM");
            }
        }
        if (StringUtils.isNotBlank(FileReaderManager.getInstance().getConfigReader().getDriverProxyUrl())) {
            driverProxyUrl = FileReaderManager.getInstance().getConfigReader().getDriverNoProxyUrl();
            LOG.info(String.format("%s is getting used as the proxy url", driverProxyUrl));
            if (StringUtils.isNotBlank(FileReaderManager.getInstance().getConfigReader().getDriverNoProxyList())) {
                driverNoProxyList = FileReaderManager.getInstance().getConfigReader().getDriverNoProxyList();
                LOG.info("the following are included in the driver.noProxy.list\n" + driverNoProxyList);
            }
        }
        if (StringUtils.isNotBlank(FileReaderManager.getInstance().getConfigReader().getDriverFileDownloadPath())) {
            driverFileDownloadPath = FileReaderManager.getInstance().getConfigReader().getDriverFileDownloadPath();
            LOG.info(String.format("the file destination for downloads performed by the driver is %s ", driverFileDownloadPath.trim()));
        }
        String baseUrl = FileReaderManager.getInstance().getConfigReader().getBaseUrl();
        LOG.info("the base url used for testing is: " + baseUrl);
    }


    public static void checkDriverPermissions(File permissionFile) {
        if (permissionFile.canExecute()) {
            LOG.info("{} is able to execute already", driverPath);
        } else {
            LOG.info("{} can not execute.... attempting to modify permissions", driverPath);
            Boolean success = permissionFile.setExecutable(Boolean.TRUE);
            if (Boolean.TRUE.equals(success)) {
                LOG.info("{} made executable = {}", driverPath, success);
            } else {
                throw new IllegalStateException("WebDriver is not executable. Test can not be run....");
            }
        }
    }

    public static WebDriver getDriver() {
        return webDriver.get();
    }

    public static File getDownloadDir() {
        return defaultDownload.get();
    }

    public static void createDriver() {
        if (webDriver.get() != null) {
            shutdownDriver();
        }
        defaultDownload.set(setUpDownloadLocation());
        webDriverRunLocation = TestContext.getWebDriverRunLocation();
        webDriverBrowser = TestContext.getWebDriverBrowser();
        if (webDriverRunLocation != WebDriverRunLocation.REMOTE &&
                webDriverRunLocation != WebDriverRunLocation.LOCAL) {
            LOG.error("{} is not a supported run location", webDriverRunLocation);
        } else {
            webDriver.set(
                    switch (webDriverRunLocation) {
                        case REMOTE -> createRemoteDriver();
                        case LOCAL  -> createLocalDriver();
                    }
            );
        }
        var actualCaps = ((HasCapabilities) webDriver.get()).getCapabilities();
        LOG.info("WebDriver has the following capabilities: {}", actualCaps.asMap());
        setWindowSize();
    }

    private static WebDriver createLocalDriver() {
        LOG.info("Creating local driver");
        if (webDriverBrowser == null) {
            throw new IllegalArgumentException(String.format(
                    "WebDriver Browser: %s is not supported", webDriverBrowser));
        } else {
            return switch (webDriverBrowser) {
                case FIREFOX -> getFireFoxDriver();
                case CHROME -> getChromeDriver();
                case EDGE -> getEdgeDriver();
            };
        }
    }

    private static WebDriver getEdgeDriver() {
        EdgeDriver driver;
        var options = new EdgeOptions();
        if ((StringUtils.isNotBlank(driverPath))) {
            System.setProperty("webdriver.edge.driver", driverPath);
        } else {
            LOG.info("webdriver.edge.driver is not set, defaulting to webdriver manager");
            WebDriverManager.getInstance(DriverManagerType.EDGE).setup();
        }
        setEdgeOptions(options);
        driver = new EdgeDriver(options);
        return driver;
    }

    @SuppressWarnings("UnusedReturnValue")
    private static EdgeOptions setEdgeOptions(EdgeOptions options) {
        HashMap<String, Object> prefs = new HashMap<>();
        var logPreferences = new LoggingPreferences();
        var proxy = new Proxy();
//        prefs.put("plugins.always_open_pdf_externally", Boolean.TRUE);
//        prefs.put("download.default_directory", defaultDownload.get().getPath());
//        prefs.put("download.prompt_for_download", Boolean.FALSE);

        if (StringUtils.isNotBlank(driverProxyUrl)) {
            proxy.setHttpProxy(driverProxyUrl);
            if (StringUtils.isNotBlank(driverNoProxyList)) {
                proxy.setNoProxy(driverNoProxyList.trim());
            }
            options.setProxy(proxy);
        }
        logPreferences.enable(LogType.PERFORMANCE, Level.ALL);
        logPreferences.enable(LogType.BROWSER, Level.ALL);
        options.setExperimentalOption("prefs", prefs);
        options.setCapability(EdgeOptions.LOGGING_PREFS, logPreferences);
//        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, Boolean.TRUE);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
//        options.addArguments("ignoreProtectedModeSettings");
//        options.addArguments("ignoreZoomSetting");
//        options.addArguments("takesScreenshot");
//        options.addArguments("ensureCleanSession");
        if (Boolean.TRUE.equals(headless)) {
            // using "--headless=new" causes artifacts and high CPU usage
            options.addArguments("headless");
        }
        options.addArguments("--guest");
//        options.addArguments("disable-dev-shm-usage");
//        options.setCapability("ms:inPrivate", Boolean.TRUE);
        return options;
    }

    private static void setWindowSize() {
        if (webDriverBrowser != WebDriverBrowser.FIREFOX && resolutionX != 0 && resolutionY != 0) {
            LOG.debug("Setting window resolution to [{}x{}]", resolutionX, resolutionY);
        } else {
            LOG.warn("Can not set resolution as [{}x{}]. Maximizing window instead", resolutionX, resolutionY);
        }
    }

    private static RemoteWebDriver createRemoteDriver() {
        RemoteWebDriver driver = null;
        LOG.info("Creating remote WebDriver");
        MutableCapabilities options;
        switch (webDriverBrowser) {
            case CHROME:
                options = new ChromeOptions();
                setChromeOptions((ChromeOptions) options);
                break;
            case FIREFOX:
                options = new FirefoxOptions();
                setFirefoxOptions((FirefoxOptions) options);
                break;
            case EDGE:
                options = new EdgeOptions();
                setEdgeOptions((EdgeOptions) options);
                break;
        }
        return driver;
    }

    // firefox
    private static WebDriver getFireFoxDriver() {
        FirefoxDriver driver;
        var options = new FirefoxOptions();
        if (StringUtils.isNotBlank(driverPath)) {
            System.setProperty("webdriver.gecko.driver", driverPath);
        } else {
            LOG.info("webdriver.gecko.driver is not set, defaulting to webdriver manager");
            WebDriverManager.getInstance(DriverManagerType.FIREFOX).setup();
        }
        setFirefoxOptions(options);
        driver = new FirefoxDriver(options);
        return driver;
    }

    @SuppressWarnings("UnusedReturnValue")
    private static FirefoxOptions setFirefoxOptions(FirefoxOptions options) {
        var logPreferences = new LoggingPreferences();
        var proxy = new Proxy();
        if (StringUtils.isNotBlank(driverProxyUrl)) {
            proxy.setHttpProxy(driverProxyUrl);
            if (StringUtils.isNotBlank(driverNoProxyList)) {
                proxy.setNoProxy(driverNoProxyList.trim());
            }
            options.setProxy(proxy);
        }
        logPreferences.enable(LogType.PERFORMANCE, Level.ALL);
        logPreferences.enable(LogType.BROWSER, Level.ALL);
        options.setProfile(getFirefoxProfile());
        options.merge(new ImmutableCapabilities("loggingPrefs", logPreferences));
        options.setAcceptInsecureCerts(Boolean.TRUE);
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        if (Boolean.TRUE.equals(headless)) {
            options.addArguments("-headless");
        }
        return options;
    }

    public static FirefoxProfile getFirefoxProfile() {
        final var DOWNLOAD_FOLDER_LIST_PREFERENCE = 2;
        var profile = new FirefoxProfile();
        profile.setAcceptUntrustedCertificates(Boolean.TRUE);
        profile.setAssumeUntrustedCertificateIssuer(Boolean.FALSE);
        profile.setPreference("browser.download.folderList", DOWNLOAD_FOLDER_LIST_PREFERENCE);
        profile.setPreference("browser.download.manager.showWhenStarting", Boolean.FALSE);
        profile.setPreference("browser.download.dir", defaultDownload.get().getPath());
        profile.setPreference("javascript.enabled", Boolean.TRUE);
        profile.setPreference("security.default_personal_cert", "Select Automatically");
        profile.setPreference("browser.helperApps.neverAsk.openFile",
                "text/csv,application/x-msexcel,application/pdf,application/octet-stream,application/x-gzip,"
                        + "application/zip,application/excel,application/x-excel,application/vnd.ms-excel,"
                        + "image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
                "text/csv,application/x-msexcel,application/pdf,application/octet-stream,application/x-gzip,"
                        + "application/zip,application/excel,application/x-excel,application/vnd.ms-excel,"
                        + "image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
        profile.setPreference("browser.helperApps.alwaysAsk.force", Boolean.FALSE);
        profile.setPreference("pdfjs.disabled", Boolean.TRUE);
        profile.setPreference("plugin.scan.Acrobat", "99.0");
        profile.setPreference("plugin.scan.plid.all", Boolean.FALSE);
        profile.setPreference("plugin.disable_full_page_plugin_for_types", "application/pdf");
        profile.setPreference("browser.download.manager.alertOnEXEOpen", Boolean.FALSE);
        profile.setPreference("browser.download.manager.focusWhenStarting", Boolean.FALSE);
        profile.setPreference("browser.download.manager.useWindow", Boolean.FALSE);
        profile.setPreference("browser.download.manager.showAlertOnComplete", Boolean.FALSE);
        profile.setPreference("browser.download.manager.closeWhenDone", Boolean.FALSE);
        return profile;
    }

    // CHROME
    private static WebDriver getChromeDriver() {
        ChromeDriver driver;
        var options = new ChromeOptions();
        if (StringUtils.isNotBlank(driverPath))
            System.setProperty("webdriver.chrome.driver", driverPath);
        else {
            LOG.info("webdriver.chrome.driver is not set because driver.path is invalid" +
                    ", using default WebDriverManager setup");
            WebDriverManager.getInstance(DriverManagerType.CHROME).setup();
        }
        System.setProperty("webdriver.chrome.whitelistedIps", "");
        setChromeOptions(options);
        driver = new ChromeDriver(options);
        return driver;
    }

    @SuppressWarnings("UnusedReturnValue")
    private static ChromeOptions setChromeOptions(ChromeOptions options) {
        HashMap<String, Object> prefs = new HashMap<>();
        var logPreferences = new LoggingPreferences();
        var proxy = new Proxy();
        prefs.put("plugins.always_open_pdf_externally", Boolean.TRUE);
        prefs.put("download.default_directory", defaultDownload.get().getPath());

        if (StringUtils.isNotBlank(driverProxyUrl)) {
            proxy.setHttpProxy(driverProxyUrl);
            proxy.setSslProxy(driverProxyUrl);
            if (StringUtils.isNotBlank(driverNoProxyList)) {
                proxy.setNoProxy(driverNoProxyList.trim());
            }
            options.setProxy(proxy);
        }
        logPreferences.enable(LogType.PERFORMANCE, Level.ALL);
        logPreferences.enable(LogType.BROWSER, Level.ALL);
        options.setCapability(ChromeOptions.LOGGING_PREFS, logPreferences);

        options.setCapability(ChromeOptions.LOGGING_PREFS, logPreferences);
        options.setExperimentalOption("prefs", prefs);
        options.setAcceptInsecureCerts(Boolean.TRUE);
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        System.setProperty("webdriver.chrome.whitelistedIps", "");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("start-maximized");
        options.addArguments("incognito");
        // TODO change if unsigned certs are not used to access application
        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, Boolean.TRUE);
        options.addArguments("incognito");
        if (Boolean.TRUE.equals(headless)) {
            options.addArguments("--headless=chrome");
        }
        try {
            String cmdChromeOptions = System.getProperty("chromeOptions");
            String[] listOptions = cmdChromeOptions.split(",", -1);
            for (String option : listOptions) {
                LOG.info("Adding chromeOptions [{}]", option);
                options.addArguments(option);
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            LOG.debug(e);
            LOG.info("Additional chromeOptions not set");
        }
        return options;
    }

    public static void shutdownDriver() {
        if (webDriver.get() != null) {
            LOG.info("Shutting down driver");
            if (webDriverBrowser != WebDriverBrowser.FIREFOX) {
                Set<String> windowsHandles = webDriver.get().getWindowHandles();
                if (windowsHandles.size() > 1) {
                    windowsHandles.forEach(windowHandle -> {
                        webDriver.get().switchTo().window(windowHandle);
                        webDriver.get().close();
                        LOG.info("windowHandle: [{}] closed", windowHandle);
                    });
                }
            }
            webDriver.get().quit();
            LOG.info("webDriver.get().quit() is successful");
            webDriver.remove();
        } else {
            LOG.debug("Driver has not been set. Nothing to shutdown");
        }
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString() + sequence.getAndIncrement();
    }

    public static File setUpDownloadLocation() {
        LOG.info("Setting up unique download location");
        File downloadDir;

        if (StringUtils.isNotBlank(driverFileDownloadPath)) {
            downloadDir = new File(System.getProperty("user.dir") + File.separator + "target" + File.separator
                    + driverFileDownloadPath + File.separator + generateUUID());
        } else {
            downloadDir = new File(
                    System.getProperty("user.dir") + File.separator + "target" + File.separator + generateUUID());
        }

        if (downloadDir.mkdir()) {
            LOG.info("Created download folder {}", downloadDir.getPath());
        }
        LOG.info("Using download folder {}", downloadDir.getPath());
        return downloadDir;
    }

    public static void removeDefaultDownload() {
        defaultDownload.remove();
    }

}
