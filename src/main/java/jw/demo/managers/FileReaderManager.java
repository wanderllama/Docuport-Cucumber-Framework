package jw.demo.managers;

import jw.demo.config.ConfigReader;

/**
 * Manages Resource File Readers
 */
public class FileReaderManager {

    private static FileReaderManager fileReaderManager = new FileReaderManager();
    private static ConfigReader configReader = new ConfigReader();

    // ValidationDataReader can be used for
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
