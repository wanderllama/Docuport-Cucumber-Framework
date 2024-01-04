package jw.demo.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import jw.demo.enums.Wait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DocumentUtil {

    private static final Logger LOG = LogManager.getLogger(DocumentUtil.class);
    public static final String FILE_NAME = "fileName";
//    private static final int TIME_OUT_SECONDS = WaitTime.EXTRA_LONG.amountOfSeconds();
    public static final String FILE_SIZE = "fileSize";
    public static final String UPLOADED_BY = "uploadedBy";
    // TODO remove examples
    public static final String FILE_PATH_ONE = "file1";
    public static final String FILE_PATH_TWO = "path/to/file2";
    private String filePath;

    public static String getFullPath(String pathName) {
        return switch (pathName) {
            case "file1" -> "path/to/file1";
            case "file2" -> "path/to/file2";
            default -> pathName;
        };
    }

    public static void uploadFile(URL dataFileURL, By uploadBtn, WebDriver driver) {
        Awaitility.await().dontCatchUncaughtExceptions().atMost(Wait.EXTRA_LONG.amountOfSeconds(), TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Wait.EXTRA_LONG.amountOfSeconds()));
        wait.until(ExpectedConditions.presenceOfElementLocated(uploadBtn));
        File file = new File(dataFileURL.getFile());

        WebElement upload = driver.findElement(uploadBtn);
        Awaitility.await().dontCatchUncaughtExceptions().atMost(Wait.EXTRA_LONG.amountOfSeconds(), TimeUnit.SECONDS);
        LOG.info("absolute path - {}", file.getAbsolutePath());
        upload.sendKeys(file.getAbsolutePath());
    }

    public static void deleteFiles(String fileName, File downloadDir) {
        FilenameFilter errorFiles = (dir, name) -> name.startsWith(fileName);

        File[] files = downloadDir.listFiles(errorFiles);
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    LOG.debug("file.delete() output is {}", file.delete());
                }
            }
        }
    }

    public static JsonObject getJsonObjectFromFile(String filePath) throws IOException {
        LOG.info("Attempting to load Json Object from file {}", filePath);
        JsonObject jsonObj = new JsonObject();
        URL dataFileURL = Thread.currentThread().getContextClassLoader().getResource(filePath);
        Path path = Paths.get(URI.create(Objects.requireNonNull(dataFileURL).toString()));

        try {
            byte[] allContent = Files.readAllBytes(path);
            String content = new String(allContent, StandardCharsets.UTF_8);
            jsonObj = new Gson().fromJson(content, JsonObject.class);
        } catch (FileNotFoundException e) {
            LOG.error(LogException.errorMessage(e));
        }
        return jsonObj;
    }

    public static JsonArray getJsonArrayFromFile(String filePath) {
        JsonArray jsonArray = new JsonArray();
        URL dataFileURL = Thread.currentThread().getContextClassLoader().getResource(filePath);
        Path path = Paths.get(URI.create(Objects.requireNonNull(dataFileURL).toString()));

        try (Reader reader = new FileReader(path.toString())) {
            jsonArray = new Gson().fromJson(reader, JsonArray.class);
        } catch (IOException | IllegalStateException | JsonSyntaxException e) {
            LOG.error(LogException.errorMessage(e));
        }
        return jsonArray;
    }

    public static File getFileFromResource(String fileName) {
        LOG.info("Loading resource file: {}", fileName);
        URL resource = Thread.currentThread().getContextClassLoader().getResource(fileName);
        return new File(Objects.requireNonNull(resource).getFile());
    }

    public String get() {
        return filePath;
    }
}
