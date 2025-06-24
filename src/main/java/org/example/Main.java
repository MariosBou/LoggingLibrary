package org.example;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        WMLoggingLibrary logger = WMLoggingLibrary.builder()
                .logPath("logs")
                .infoFileName("info.log")
                .warnFileName("warn.log")
                .debugFileName("debug.log")
                .loggerName("Database")
                .infoFileSize(5, WMLoggingLibrary.SizeUnit.MB)
                .warnFileSize(10, WMLoggingLibrary.SizeUnit.MB)
                .debugFileSize(2, WMLoggingLibrary.SizeUnit.MB)
                .build();

        HashMap<String, String> context = new HashMap<>();
        context.put("user", "student123");

        logger.logInfo("Application started", context);
        logger.logDebug("This is a debug message", context);
        logger.logWarn("Warning issued","Empty Product", context);
        logger.logError("An error occurred","Network Disconnect", context);

    }
}
