package jw.demo.enums;

import com.google.gson.JsonObject;
import jw.demo.utils.DocumentUtil;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


@Getter
public enum ApiEndpoint {

    AUTH(ApiMethod.POST, "authorize", null);

    @Getter
    private final ApiMethod method;
    private final String endpoint;
    private JsonObject payload;

    private final Logger LOG = LogManager.getLogger(ApiEndpoint.class);

    ApiEndpoint(ApiMethod method, String endpoint, String payloadPath) {
        this.method = method;
        this.endpoint = endpoint;
        if (StringUtils.isNotBlank(payloadPath)) {
            try {
                payload = DocumentUtil.getJsonObjectFromFile(payloadPath);
            } catch (IOException e) {
                LOG.error("Failed to get payload from file for endpoint: [{}]", endpoint));
            }
        }
    }
}
