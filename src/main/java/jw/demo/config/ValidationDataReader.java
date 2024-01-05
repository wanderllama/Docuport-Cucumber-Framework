package jw.demo.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * Can be used to deserialize data from json files to Map<String, JsonNode>
 *
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
            Resource[] resources = resolver.getResources("classpath:src/test/resources/validations/*.json");
            for (Resource resource : resources) {
                InputStream input = resource.getInputStream();
                Map<String, JsonNode> jsonMap = new ObjectMapper().readValue(input,
                        new TypeReference<>() {
                        });
                pages.putAll(jsonMap);
            }
        } catch (IOException e) {
            LOG.error("failed to load resources for ValidationReader");
            e.printStackTrace();
        }
        LOG.info("Validation Data loaded");
    }
}
