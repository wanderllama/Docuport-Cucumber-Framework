package jw.demo.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.cucumber.messages.types.Attachment;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lombok.Data;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ScenarioContext {

    // TODO replace demo data
    private String userName;
    private int orgId;
    private String strOrgId;
    private String orgName;
    private String token;
    private String authorization;
    private String submittedUserId;
    private String searchTerm;
    private String reviewId;
    private int numberOfFileDownloaded;
    private int numberOfFileUploaded;
    private Map<String, Attachment> attachments = new HashMap<>();
    private JsonArray emails;

    // Scenario data from data.json or Datamap
    private JsonObject scenarioData;
    private Map<String, String> dataMap = new HashMap<>();
    private Map<String, String> fileMap = new HashMap<>();

    // Organization Data
    private List<JsonObject> organizations;

    // Payloads
    private Object payload;
    private JsonObject workflowPayload;
    private JsonObject workflowConfiguration;

    // API Responses
    private Response response;
    private ValidatableResponse jsonResponse;

    private SoftAssert softAssert;

    public void putToFileMap(String name, String path) {
        Map<String, String> mutableMap = new HashMap<>(fileMap);
        mutableMap.put(name, path);
        this.fileMap = mutableMap;
    }

    public void putToAttachmentMap(String name, Attachment attachment) {
        Map<String, Attachment> mutableMap = new HashMap<>(attachments);
        mutableMap.put(name, attachment);
        this.attachments = mutableMap;
    }
}

