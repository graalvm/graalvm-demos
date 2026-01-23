/*
 * Copyright (c) 2024, 2026, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggerRunTimeInit {
    public static void main(String[] args) throws IOException {
        LogManager.getLogManager().readConfiguration(LoggerRunTimeInit.class.getResourceAsStream("/logging.properties"));
        Logger logger = Logger.getLogger(LoggerRunTimeInit.class.getName());
        logger.log(Level.WARNING, "Danger, Will Robinson!");
    }
}