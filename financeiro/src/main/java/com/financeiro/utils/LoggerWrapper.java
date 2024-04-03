package com.financeiro.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerWrapper.class);
    
    public static void logInfo(String message) {
        LOGGER.info(message);
    }
    public static void logWarn(String message) {
        LOGGER.warn(message);
    }
    public static void logError(String message, Throwable throwable) {
        LOGGER.error(message, throwable);
    }
}
