package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        WMLoggingLibrary WMlogger = WMLoggingLibrary.builder()
                .logPath("logs")
                .infoFileName("info.log")
                .warnFileName("warn.log")
                .debugFileName("debug.log")
                .infoFileSize(5, WMLoggingLibrary.SizeUnit.MB)
                .warnFileSize(10, WMLoggingLibrary.SizeUnit.MB)
                .debugFileSize(2, WMLoggingLibrary.SizeUnit.MB)
                .build();

        HashMap<String, String> context = new HashMap<>();
        context.put("user","alice");
        context.put("userID","123");

        Logger logger = LogManager.getLogger(Main.class);
        for (String key : context.keySet()) {
            ThreadContext.put(key, context.get(key));
        }
        logger.info("Connecting to the DB....");
        logger.debug("Connecting to the DB123....");
        logger.warn("Something went wrong while connecting to the DB");

    }
}



