package jw.demo.enums;

import com.google.gson.JsonObject;
import jw.demo.utils.DocumentUtil;
import jw.demo.utils.LogException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;


@Getter
public enum ApiEndpoint {

    AUTH(ApiMethod.POST, "authorize", null);

    @Getter
    private final ApiMethod method;
    private final String endpoint;
    private JsonObject payload;

    ApiEndpoint(ApiMethod method, String endpoint, String payloadPath) {
        this.method = method;
        this.endpoint = endpoint;
        if (StringUtils.isNotBlank(payloadPath)) {
            try {
                payload = DocumentUtil.getJsonObjectFromFile(payloadPath);
            } catch (IOException e) {
                LogException.errorMessage(ApiEndpoint.class, e);
            }
        }
    }
}
