package jw.demo.managers;

import jw.demo.config.ConfigReader;

/**
 * Manages Resource File Readers
 *
 * @author mchoi
 */
public class FileReaderManager {

    private static FileReaderManager fileReaderManager = new FileReaderManager();
    private static ConfigReader configReader = new ConfigReader();
//    private static ValidationDataReader validationReader = new ValidationDataReader();

    public static FileReaderManager getInstance() {
        return fileReaderManager;
    }

    public ConfigReader getConfigReader() {
        return configReader;
    }

//    @SuppressWarnings("UnusedReturnValue")
//    public ValidationDataReader getValidationReader() {
//        return validationReader;
//    }
}
