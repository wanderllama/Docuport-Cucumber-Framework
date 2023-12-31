package jw.demo.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jw.demo.utils.LogException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages validation files for text/field validations, if needed
 *
 * @author mchoi
 */
public class ValidationDataReader {

    private static final Logger LOG = LogManager.getLogger(ValidationDataReader.class);
    @SuppressWarnings("FieldMayBeFinal")
    private static Map<String, JsonNode> pages = new HashMap<>();

    @SuppressWarnings("CallToPrintStackTrace")
    public ValidationDataReader() {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(
                Thread.currentThread().getContextClassLoader());
        LOG.info("Loading Validation Data");
        try {
            Resource[] resources = resolver.getResources("classpath:validations/**/*.json");
            for (Resource resource : resources) {
                InputStream input = resource.getInputStream();
                Map<String, JsonNode> jsonMap = new ObjectMapper().readValue(input,
                        new TypeReference<>() {
                        });
                pages.putAll(jsonMap);
            }
        } catch (IOException e) {
            LogException.errorMessage(LOG, e);
        }
        LOG.info("Validation Data loaded");
    }
}
