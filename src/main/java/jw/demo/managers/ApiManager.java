package jw.demo.managers;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import jw.demo.utils.ApiUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;

public class ApiManager {

    public ApiManager() {
        throw new IllegalStateException("Class is not designed for object creation");
    }

    private static final Logger LOG = LogManager.getLogger(ApiUtil.class);
    private static ThreadLocal<RequestSpecification> requestSpec = new ThreadLocal<>();
    private static ThreadLocal<ResponseSpecification> responseSpec = new ThreadLocal<>();
    private static final String DEBUG = System.getProperty("debug");
    private static final int OK_STATUS_CODE = 200;
    private static final int NO_RESPONSE_STATUS_CODE = 204;

    private static void initRequestSpec() {
        if (StringUtils.equals(DEBUG, "true")) {
            LOG.debug("Creating a new RequestSpecification object with logging filters on");
            requestSpec.set(new RequestSpecBuilder()
                    .setConfig(RestAssured.config().objectMapperConfig(new ObjectMapperConfig(ObjectMapperType.GSON))
                            .logConfig(RestAssured.config().getLogConfig()
                                    .enableLoggingOfRequestAndResponseIfValidationFails()))
                    .setRelaxedHTTPSValidation().setContentType(ContentType.JSON)
                    .addHeader("Accept-Encoding", "gzip, deflate, br").setUrlEncodingEnabled(true)
                    .addFilter(new RequestLoggingFilter()).addFilter(new ResponseLoggingFilter()).build());
        }
    }

    private static void intResponseSpec() {
        LOG.debug("Creating q new ResponseSpecification object");
        responseSpec.set(new ResponseSpecBuilder()
                .expectStatusCode(Matchers.anyOf(Matchers.is(OK_STATUS_CODE), Matchers.is(NO_RESPONSE_STATUS_CODE)))
                .build());
    }

    public static RequestSpecification getRequestSpec() {
        LOG.debug("Getting thread-safe RequestSpecification");
        if (requestSpec.get() == null)
            initRequestSpec();
        return requestSpec.get();
    }

    public static ResponseSpecification getResponseSpec() {
        LOG.debug("Getting thread-safe ResponseSpecification");
        if (responseSpec.get() == null)
            intResponseSpec();
        return responseSpec.get();
    }

    public static void tearDown() {
        LOG.debug("Removing ThreadLocal Objects");
        requestSpec.remove();
        responseSpec.remove();
    }
}
