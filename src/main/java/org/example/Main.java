package org.example;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        LogLib logLib = new LogLib();


        // Set the log path directly in your code
        LogLib.setLogPath("C:/Users/mario/Documents/Internship Cardinal Health/Logs/log");


        LogLib.setDebugLogFileName("custom-debug.log");
        LogLib.setInfoLogFileName("custom-info.log");
        LogLib.setWarnLogFileName("custom-warn.log");


        // Define tags and values
        HashMap<String, String> context = new HashMap<>();
        context.put("host", "db01");
        context.put("env", "production");

        LogLib.logWarn("Database connection failed", context);
        LogLib.logInfo("Database connection failed", context);
        LogLib.logDebug("Database connection failed", context);

        System.out.println("Current log path: " + LogLib.getLogPath());

    }
}



