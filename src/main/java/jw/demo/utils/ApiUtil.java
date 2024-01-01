package jw.demo.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import io.restassured.specification.RequestSpecification;
import jw.demo.constants.Constants;
import jw.demo.enums.ApiEndpoint;
import jw.demo.enums.Wait;
import jw.demo.managers.ApiManager;
import jw.demo.managers.FileReaderManager;
import jw.demo.models.Login;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ApiUtil {

    private static final Logger LOG = LogManager.getLogger(ApiUtil.class);
    private static final String baseUrl = FileReaderManager.getInstance().getConfigReader().getBaseUrl();
    private static final String envPasswd = FileReaderManager.getInstance().getConfigReader().getEnvPasswd();
    public static final String TOKENS = "tokens";
    // TODO update to reflect the frequency the token needs to be refreshed in milliseconds
    public static final long TOKEN_REFRESH_MS = 900_000;

    public static final String BEARER = "Bearer ";

    /**
     * <p>
     * Main run method wrapper to process all Rest-Assured calls based on enums
     * ApiEndpoint and ApiMethod
     * </p>
     *
     * @param authorization
     * @param apiEndpoint
     * @param payload
     * @param pathParams
     * @param queryParams
     * @return ResponseBodyExtractionOptions object to be further extracted into
     * different options
     */

    public static ResponseBodyExtractionOptions run(String authorization, ApiEndpoint apiEndpoint, Object payload,
                                                    Map<String, Object> pathParams, Map<String, Object> queryParams) {
        Map<String, Object> headers = new HashMap<>();
        LOG.info("Running {} {}", apiEndpoint.getMethod(), apiEndpoint.getEndpoint());
        if (authorization != null) {
            headers.put(Constants.AUTHORIZATION, authorization);
        }
        if (pathParams != null && !pathParams.isEmpty()) {
            LOG.info("Path Params: {}", pathParams);
        } else {
            pathParams = new HashMap<>();
        }
        if (queryParams != null && !queryParams.isEmpty()) {
            LOG.info("Query Params: {}", queryParams);
        } else {
            queryParams = new HashMap<>();
        }

        var requestSpec = RestAssured.given().spec(ApiManager.getRequestSpec().headers(headers))
                .queryParams(queryParams).pathParams(pathParams);
        var response = getResponse(apiEndpoint, requestSpec, payload);

        try {
            return response.then().spec(ApiManager.getResponseSpec()).extract();
        } catch (AssertionError e) {
            StringBuilder exceptionMessage = new StringBuilder();
            exceptionMessage.append(String.format("%s%nResponse: %s", e, response.asPrettyString()));
            if (payload != null) {
                var gson = new GsonBuilder().setPrettyPrinting().create();
                exceptionMessage.append(String.format("%n%nPayload:%n%s%n", gson.toJson(payload)));
            }
            LogException.errorMessage(LOG, exceptionMessage.toString(), e);
            throw new AssertionError();
        }
    }

    private static Response getResponse(ApiEndpoint endpoint, RequestSpecification requestSpec, Object payload) {
        TestContext.logToScenario(
                String.format("API call started: %s %s", endpoint.getMethod(), endpoint.getEndpoint()));
        if (payload != null)
            requestSpec = requestSpec.body(payload);
        var response = switch (endpoint.getMethod()) {
            case GET -> requestSpec.when().get(getTargetUrl(endpoint.getEndpoint()));
            case PUT -> requestSpec.when().put(getTargetUrl(endpoint.getEndpoint()));
            case POST -> requestSpec.when().post(getTargetUrl(endpoint.getEndpoint()));
            case PATCH -> requestSpec.when().patch(getTargetUrl(endpoint.getEndpoint()));
//            default -> throw new IllegalArgumentException("Undefined API method: " + endpoint.getMethod());
        };
        TestContext.logToScenario(
                String.format("API call completed: %s %s", endpoint.getMethod(), endpoint.getEndpoint()));
        return response;
    }

    public static ResponseBodyExtractionOptions run(ApiEndpoint endpoint, Object payload) {
        return run(TestContext.getScenarioCtx().getAuthorization(), endpoint, payload);
    }

    public static ResponseBodyExtractionOptions run(ApiEndpoint endpoint, Object payload, Map<String, Object> pathParams) {
        return run(TestContext.getScenarioCtx().getAuthorization(), endpoint, payload, pathParams);
    }

    public static ResponseBodyExtractionOptions run(ApiEndpoint endpoint, Object payload, Map<String, Object> pathParams, Map<String, Object> queryParams) {
        return run(TestContext.getScenarioCtx().getAuthorization(), endpoint, payload, pathParams, queryParams);
    }

    public static ResponseBodyExtractionOptions run(String authorization, ApiEndpoint endpoint, Object payload) {
        Map<String, Object> pathParams = new HashMap<>();
        Map<String, Object> queryParams = new HashMap<>();
        return run(authorization, endpoint, payload, pathParams, queryParams);
    }

    public static ResponseBodyExtractionOptions run(String authorization, ApiEndpoint endpoint, Object payload, Map<String, Object> pathParams) {
        Map<String, Object> queryParams = new HashMap<>();
        return run(authorization, endpoint, payload, pathParams, queryParams);
    }

    /**
     * Get Token with valid credentials
     *
     * @param userName
     * @return token used access application
     */
    // TODO implement methods
    public static String getTokens(String userName) {
        Boolean needNewToken = Boolean.TRUE;
        if (TestContext.getData().getAsJsonObject(TOKENS).has(userName) && TestContext.getData().getAsJsonObject(TOKENS)
                .getAsJsonObject(userName).has(Constants.TIMESTAMP)) {
            var tokenTimeStamp = Timestamp.valueOf(TestContext.getData().getAsJsonObject(TOKENS)
                    .getAsJsonObject(userName).get(Constants.TIMESTAMP).getAsString());
            long timeDiffInMillis = System.currentTimeMillis() - tokenTimeStamp.getTime();
            needNewToken = timeDiffInMillis > TOKEN_REFRESH_MS;
            TestContext
                    .logToScenario(String.format(
                            "tokenTimeStamp: [%s], timeDiffsMs: [%s], needNewToken for [%s]: [%b]",
                            tokenTimeStamp.toLocalDateTime().atZone(ZoneId.of(Constants.EASTERN_TIME_ZONE_ID)),
                            timeDiffInMillis, userName, needNewToken
                    ));
        }
        if (Boolean.TRUE.equals(needNewToken)) {
            try {
                Awaitility.await().atMost(Wait.EXTRA_LONG.waitTime()).with()
                        .pollInterval(Wait.forThisAmount.ofMillis(2000))
                        .pollDelay(Wait.forThisAmount.waitTime()).untilTrue(tryLoginAndSetToken(userName));
            } catch (ConditionTimeoutException e) {
                LOG.debug(e);
                loginAndSetToken(userName);
            }
        } else {
            TestContext.switchToken(userName);
        }
        return TestContext.getScenarioCtx().getToken();
    }

    private static AtomicBoolean tryLoginAndSetToken(String userName) {
        try {
            loginAndSetToken(userName);
            return new AtomicBoolean(Boolean.TRUE);
        } catch (AssertionError e) {
            LogException.errorMessage(LOG, String.format("POST login has failed for [%s]", userName), e);
        }
        throw new AssertionError();
    }

    // TODO update method
    private static void loginAndSetToken(String userName) {
        TestContext.logToScenario(String.format("POST login started for [%s]", userName));
        var loginObject = Login.builder().username(userName).passwd(System.getenv(envPasswd)).build();
        var tokenString = run(ApiEndpoint.AUTH, loginObject).as(JsonObject.class).get(Constants.TOKEN).getAsString();
        TestContext.setToken(userName, tokenString);
        TestContext.logToScenario(String.format("POST login completed for [%s]", userName));
    }


    private static String getTargetUrl(String path) {
        return baseUrl + path;
    }
}
