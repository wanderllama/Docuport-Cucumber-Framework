package jw.demo.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;

public class UIConsoleLogger {

    private static final Logger LOG = LogManager.getLogger(UIConsoleLogger.class);

    public UIConsoleLogger() {
        throw new IllegalStateException("Class is not designed for object creation");
    }

    public static JsonArray extractUIConsoleLog(WebDriver driver, String logType) {
        JsonArray logs = new JsonArray();
        if (driver != null) {
            LogEntries logEntries = driver.manage().logs().get(logType);
            for (LogEntry entry : logEntries) {
                LOG.warn("{} {}", entry.getLevel(), entry.getTimestamp());
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("level", entry.getLevel().getName());
                String[] messageParts = entry.getMessage().split(" ");
                jsonObject.addProperty("url", messageParts[0]);
                jsonObject.addProperty("message", entry.getMessage());
                logs.add(jsonObject);
            }
        }
        return logs;
    }

}
