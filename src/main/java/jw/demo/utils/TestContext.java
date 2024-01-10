package jw.demo.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.cucumber.java.Scenario;
import jw.demo.constants.Constants;
import jw.demo.enums.WebDriverBrowser;
import jw.demo.enums.WebDriverRunLocation;
import jw.demo.managers.FileReaderManager;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@SuppressWarnings("CallToPrintStackTrace")
public final class TestContext {

    private static final Logger LOG = LogManager.getLogger(TestContext.class);
    private static final Pattern SCENARIO_NAME_MATCHER = Pattern.compile(".*<-?\\d{4}>.*");
    @Getter
    private static boolean globalSetupComplete;
    @Getter
    private static String baseUrl;
    @Getter
    private static String envPasswd;
    @Getter
    private static String userPassword;
    @Getter
    private static String group;
    @Getter
    private static WebDriverBrowser webDriverBrowser;
    @Getter
    private static WebDriverRunLocation webDriverRunLocation;
    private static JsonObject globalData;
    private static ThreadLocal<Scenario> scenario = new ThreadLocal<>();
    private static ThreadLocal<ScenarioContext> scenarioCtx = new ThreadLocal<>();

    private TestContext() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Initializes the global variables and sets up the environment.
     * <p>
     * The following variables are initialized using values from 'src/test/resources/driver.properties'
     * - Sets the `baseUrl` variable to the value of 'base.url'
     * - Sets the `envPass` variable to the value 'env.passwd'
     * - Sets the `userPassword` variable to the value to envPasswd
     * - Sets the `datafile` variable to the value of `data.file`
     * </p>
     * <p>
     * The datafile path is used by DocumentUtil to retrieve scenario data saved in the json file
     * - Loads the JSON object from the datafile using getJsonObjectFromFile().
     * - Sets the `globalData` variable to the loaded JSON object.
     * - Sets the `scenarioOutcome` array used by ITestListener
     * - Sets the `globalSetupComplete` variable to true. Variable is used in Hooks @Before
     * </p>
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public static void initGlobal() {
        //noinspection ResultOfMethodCallIgnored
//        FileReaderManager.getInstance().getValidationReader();
//        if (Boolean.TRUE.equals(FileReaderManager.getInstance().getConfigReader().getTrustStoreEnabled())) {
//            System.setProperty("javax.net.ssl.trustStore",
//                    FileReaderManager.getInstance().getConfigReader().getTrustStore());
//            System.setProperty("javax.net.ssl.trustStorePassword",
//                    FileReaderManager.getInstance().getConfigReader().getTrustStorePasswd());
//        }
        group = FileReaderManager.getInstance().getConfigReader().getGroup();
        LOG.info("Using group as: {}", group);
        webDriverBrowser = FileReaderManager.getInstance().getConfigReader().getWebDriverBrowser();
        LOG.info("Using driver.browser as: {}", webDriverBrowser);
        webDriverRunLocation = FileReaderManager.getInstance().getConfigReader().getWebDriverRunLocation();
        LOG.info("Using driver.location as: {}", webDriverRunLocation);
        baseUrl = FileReaderManager.getInstance().getConfigReader().getBaseUrl();
        LOG.info("Using base.url as: {}", baseUrl);
        envPasswd = FileReaderManager.getInstance().getConfigReader().getEnvPasswd() + group.charAt(3);
        LOG.info("Using env.pass as: {}", envPasswd);
        userPassword = envPasswd;
        String datafile = System.getProperty("datafile");
        if (StringUtils.isBlank(datafile))
            datafile = FileReaderManager.getInstance().getConfigReader().getDataFile();
        LOG.info("Loading {} as input data file", datafile);
        try {
            globalData = DocumentUtil.getJsonObjectFromFile("data" + datafile);
        } catch (IOException e) {
            LOG.error("Failed to get globalData jsonObject from file [{}]", datafile);
            e.printStackTrace();
        }
        LOG.debug("Test data scenario count [{}]", globalData.size());
        globalData.add(ApiUtil.TOKENS, new JsonObject());
        globalSetupComplete = true;
    }

    /**
     * Retrieves the current Scenario object.
     *
     * @return The current Scenario object.
     */
    public static Scenario getScenario() {
        return scenario.get();
    }

    /**
     * Initializes the scenario with the given Scenario object.
     * Sets the Scenario using currentScenario object passed by cucumber.
     * ScenarioContext is initialized and scenario data added to context using getInitScenarioData().
     * Scenario names can be identified by regex pattern for extra setup before the start of scenario.
     *
     * @param currentScenario the Scenario object representing the current scenario
     */
    public static void initScenario(Scenario currentScenario) {
        scenario.set(currentScenario);
        scenarioCtx.set(new ScenarioContext());
        getScenarioCtx().setScenarioData(getInitScenarioData(getScenario().getName()));
        getScenarioCtx().setSoftAssert(new SoftAssert());

//        if (SCENARIO_NAME_MATCHER.matcher(currentScenario.getName()).matches()) {
//            // TODO check if you need to handle parameterization in scenario name
//            var variable = currentScenario.getName().substring(currentScenario.getName().indexOf('<') + 1,
//                    currentScenario.getName().indexOf('>'));
//            // TODO update to capture scenarios that require special setup based on defined scenario nomenclature
//            //  or has other parameterized values to capture and assign to ScenarioCtx
//            if (currentScenario.getName().contains("scenarioIdentifier")) {
//                // do these steps
//                var anotherVariable = currentScenario.getName().substring(currentScenario.getName().indexOf('<') + 1,
//                        currentScenario.getName().indexOf('>'));
//                getScenarioCtx().setOrgName(anotherVariable);
//                LOG.debug("extra setup complete");
//            } else {
//                LOG.debug("no extra setup required");
//            }
//        }
        LOG.info("Starting Scenario [{}]", TestContext.getScenario().getName());
    }

    /**
     * Retrieves the ScenarioContext object.
     *
     * @return scenarioCTX object representing the current scenario context
     */
    public static ScenarioContext getScenarioCtx() {
        return scenarioCtx.get();
    }

    /**
     * Retrieves the global data as a JsonObject.
     *
     * @return JsonObject containing scenario data saved in data.json file
     */
    public static JsonObject getData() {
        return globalData;
    }

    /**
     * Sets the token for a given user
     * Adds token data to the jsonObject globalData as a property (retried using get())
     * Adds token data to scenarioCtx
     *
     * @param userName username of the user for whom the token is being set
     * @param token    token to set for the user
     * @return JsonObject representing token but token is retrieved from globalData or scenario context
     */
    @SuppressWarnings("UnusedReturnValue")
    public static synchronized JsonObject setToken(String userName, String token) {
        var currentToken = new JsonObject();
        currentToken.addProperty(Constants.TOKEN, token);
        currentToken.addProperty(Constants.AUTHORIZATION, ApiUtil.BEARER + token);
        currentToken.addProperty(Constants.TIMESTAMP, new Timestamp(System.currentTimeMillis()).toString());
        getData().getAsJsonObject(ApiUtil.TOKENS).add(userName, currentToken);

        getScenarioCtx().setToken(token);
        getScenarioCtx().setAuthorization(ApiUtil.BEARER + getScenarioCtx().getToken());
        return currentToken;
    }

    /**
     * Retrieves the initial scenario data based on the scenario name.
     *
     * @param scenarioName the name of the scenario
     * @return JsonObject containing initial scenario data
     */
    public static JsonObject getInitScenarioData(String scenarioName) {
        if (!globalData.has(scenarioName)) {
            return new JsonObject();
        }
        return globalData.getAsJsonObject(scenarioName).deepCopy();
    }

    /**
     * Method used in @After Hooks to remove Scenario and Scenario Context from heap
     */
    public static void tearDown() {
        scenario.remove();
        scenarioCtx.remove();
    }

    // TODO USED FOR DEMO MARKED FOR REMOVAL

    /**
     * Sets the organization ID in the scenario context.
     *
     * @param organizationId the organization ID to set
     */
    public static void setOrganizationId(String organizationId) {
        getScenarioCtx().setStrOrgId(organizationId);
        getScenarioCtx().setOrgId(Integer.parseInt(organizationId.replace("-", "")));
    }

    /**
     * Converts a JsonArray to a list of strings.
     *
     * @param jsonArr the JsonArray to convert
     * @return ArrayList<String> converted from the JsonArray
     */
    public static ArrayList<String> getListFromJsonArray(JsonArray jsonArr) {
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        return new Gson().fromJson(jsonArr, listType);
    }

    /**
     * Logs a message to the scenario
     * The log message is formatted with the current thread name abf current time in the Eastern time zone
     * @param log message to be logged
     */
    public static void logToScenario(String log) {
        try {
            getScenario().log(String.format("%s - [%s] %s", Thread.currentThread().getName(),
                    OffsetDateTime.now(ZoneId.of(Constants.EASTERN_TIME_ZONE_ID))
                            .format(DateTimeFormatter.ofPattern(Constants.MODIFY_DATETIME)),
                    log));
        } catch (NullPointerException e) {
            LOG.error("Can not get scenario with TestContext.getScenario() in the logToscenario() lambda, so logging here\n" + log);
            e.printStackTrace();
        }
    }

    /**
     * Called by ApiUtil.getToken()
     * this method will setToken to scenarioCtx object
     *
     * @param userName username of user token is needed for
     * @return token data for user with username param as jsonObject
     */
    @SuppressWarnings("UnusedReturnValue")
    public static JsonObject switchToken(String userName) {
        JsonObject tokenForUserName = getData().getAsJsonObject(ApiUtil.TOKENS).getAsJsonObject(userName).deepCopy();
        getScenarioCtx().setToken(tokenForUserName.get(Constants.TOKEN).getAsString());
        getScenarioCtx().setAuthorization(ApiUtil.BEARER + getScenarioCtx().getToken());
        return tokenForUserName;
    }

    /**
     * returns boolean value when globalInit() finishes successfully
     * @return globalSetupComeplete
     */
    public static boolean globalSetupComplete() {
        return globalSetupComplete;
    }

    public static void setScenarioName() {
    }
}
