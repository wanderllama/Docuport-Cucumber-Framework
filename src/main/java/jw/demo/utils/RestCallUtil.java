package jw.demo.utils;

import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import jw.demo.constants.Constants;
import jw.demo.managers.ApiManager;
import jw.demo.managers.FileReaderManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.Map;

public class RestCallUtil {

    public static final String BASE_URI = FileReaderManager.getInstance().getConfigReader().getBaseUrl();
    public static final String ENV_PASSWD = FileReaderManager.getInstance().getConfigReader().getEnvPasswd();
    private static final Logger LOG = LogManager.getLogger(RestCallUtil.class);
    public RestCallUtil() {
        throw new IllegalStateException("Class is not designed for object creation");
    }

    public static Response getCall(String authorization, String apiUrl) {
        JsonObject grantObj = new JsonObject();
        LOG.info("making GET request to endpoint: {}", apiUrl);

        Response response = RestAssured.given().spec(ApiManager.getRequestSpec())
                .header(Constants.AUTHORIZATION, authorization).body(grantObj.getAsJsonObject().toString())
                .when().get(BASE_URI + apiUrl);
        response.then().spec(ApiManager.getResponseSpec());
        return response;
    }

    @SuppressWarnings({"Java8MapForEach", "CodeBlock2Expr"})
    public static void validateResponseElements(ResponseBodyExtractionOptions response, Map<String, String> responseElements) {
        String elementValue;
        JsonPath jsonPath = response.jsonPath();

        try {
            for (Map.Entry<String, String> field : responseElements.entrySet()) {
                elementValue = jsonPath.getString(field.getKey());
                LOG.info("element value - {}", elementValue);
                Assert.assertTrue(elementValue.contains(field.getValue()),
                        "expected element was not found - " + field.getValue());
            }
        } catch (IllegalStateException e) {
            LOG.debug(e);
            responseElements.entrySet().forEach(entry -> {
                Assert.assertEquals(entry.getValue(), jsonPath.get(entry.getKey()),
                        "expected element was not found - " + entry.getValue());
            });
        }
    }
}