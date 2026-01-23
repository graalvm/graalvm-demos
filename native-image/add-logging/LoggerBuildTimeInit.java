/*
 * Copyright (c) 2024, 2026, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggerBuildTimeInit {
    private static final Logger LOGGER;
    static {
        try {
            LogManager.getLogManager().readConfiguration(LoggerBuildTimeInit.class.getResourceAsStream("/logging.properties"));
        } catch (IOException | SecurityException | ExceptionInInitializerError ex) {
            Logger.getLogger(LoggerBuildTimeInit.class.getName()).log(Level.SEVERE, "Failed to read logging.properties file", ex);
        }
        LOGGER = Logger.getLogger(LoggerBuildTimeInit .class.getName());
    }

    public static void main(String[] args) throws IOException {
        LOGGER.log(Level.WARNING, "Danger, Will Robinson!");
    }
}
