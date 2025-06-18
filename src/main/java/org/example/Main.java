package org.example;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        LogLib logLib = new LogLib();


        // Set the log path directly in your code
        logLib.setLogPath("C:/Users/mario/Documents/Internship Cardinal Health/Logs/log");
        logLib.setInfoFileSize(15, LogLib.SizeUnit.KB);
        logLib.setWarnFileSize(16, LogLib.SizeUnit.KB);
        logLib.setDebugFileName("custom-debug.log");
        logLib.setInfoFileName("custom-info.log");
        logLib.setWarnFileName("custom-warn.log");


        // Define tags and values
        HashMap<String, String> context = new HashMap<>();
        context.put("host", "db01");
        context.put("env", "production");

        logLib.logWarn("Database connection failed", context);
        logLib.logInfo("Database connection failed", context);
        logLib.logDebug("Database connection failed", context);

    }
}



