package jw.demo.utils;

import jw.demo.enums.WaitTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.String.format;

@SuppressWarnings("CallToPrintStackTrace")
public class LogException {

    public LogException() {
        throw new IllegalStateException("Class is not designed for object creation");
    }

    public static void errorMessage(Logger log, Exception e) {
        ArrayList<String> lines = new ArrayList<>();
        log.error(prettyError("\nError: ", lines));
        e.printStackTrace();
    }

    @SuppressWarnings("rawtypes")
    public static void errorMessage(Class caller, Exception e) {
        ArrayList<String> lines = new ArrayList<>();
        LogManager.getLogger(caller).error(prettyError("\nError: ", lines));
        e.printStackTrace();
    }

    public static void errorMessage(Logger log, String customMessage, Exception e) {
        e.printStackTrace();
        ArrayList<String> lines = new ArrayList<>();
        log.error(prettyError("\nError: " + customMessage, lines));
    }

    public static void errorMessage(Logger log, Exception e, By element) {
        e.printStackTrace();
        ArrayList<String> lines = new ArrayList<>();
        log.error(prettyError(format("\nError: Locator Used: %s", element), lines));
    }

    public static void errorMessage(Logger log, String customMsg, Exception e, By element) {
        e.printStackTrace();
        ArrayList<String> lines = new ArrayList<>();
        log.error(prettyError("\nError: " + customMsg + format("\nLocator Used: %s", element), lines));
    }

    public static void errorMessage(Logger log, Exception e, WaitTime given, By element) {
        e.printStackTrace();
        ArrayList<String> lines = new ArrayList<>();
        log.error(prettyError(format("\nError: Thrown after %s seconds\nLocator Used: %s", given.amountOfSeconds(), element), lines));
    }

    public static void errorMessage(Logger log, String customMsg, Exception e, WaitTime given, By element) {
        e.printStackTrace();
        ArrayList<String> lines = new ArrayList<>();
        log.error(prettyError("\nError: " + customMsg + format("\nthrown after %s seconds\nLocator Used: %s", given.amountOfSeconds(), element), lines));
    }

    // not the best but working
    private static String prettyError(String str, ArrayList<String> lines) {
        ArrayList<String> list = new ArrayList<>(List.of(str.split("\n")));
        list.sort(Comparator.comparingInt(String::length));
        int starLength = list.get(list.size() - 1).length() + 4;
        if (starLength % 2 != 0) {
            String tmp = list.get(list.size() - 1).replaceFirst(":", "");
            list.remove(list.get(list.size() - 1));
            list.add(tmp);
            starLength++;
        }
        String star = "*".repeat(starLength);
        StringBuilder msg = new StringBuilder("\n" + star + "*\n*");
        for (String parts : list) {
            int spaces = (starLength - parts.length()) / 2;
            msg.append(" ".repeat(spaces)).append(parts).append(" ".repeat(spaces)).append("*\n*");
        }
        return msg.append(star).append("\n").toString();
    }

    public static void errorMessage(Logger log, String customMessage, Error e) {
        e.printStackTrace();
        ArrayList<String> lines = new ArrayList<>();
        log.error(prettyError("Error: " + customMessage, lines));
    }
}
