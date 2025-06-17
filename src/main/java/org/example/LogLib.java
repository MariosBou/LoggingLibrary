package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import java.util.HashMap;
import org.apache.logging.log4j.core.LoggerContext;
import java.io.File;
import java.util.Properties;

public class LogLib {
    private static String log_path;
    private static final Logger logger = LogManager.getLogger(LogLib.class);
    static { initializeLogPath(); }
    public enum SizeUnit {KB, MB, GB}


    private static void initializeLogPath() {
        //Check system property first
        String systemPath = System.getProperty("LOG_PATH");
        if (systemPath != null) {
            validateAndSetPath(systemPath);
            return;
        }

        //Check configuration file
        try {
            Properties props = new Properties();
            props.load(LogLib.class.getResourceAsStream("/loglib.properties"));
            String configPath = props.getProperty("log.path");
            if (configPath != null) {
                validateAndSetPath(configPath);
                return;
            }
        } catch (Exception e) {
            logger.debug("No loglib.properties file found, using default path");
        }

        //Default location
        String defaultPath = System.getProperty("user.home") + File.separator + "logs";
        validateAndSetPath(defaultPath);
    }

    private static void validateAndSetPath(String path) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Log path must not be null or empty.");
        }

        File dir = new File(path);

        // Try to create directory if it does not exist
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new IllegalArgumentException("Log path does not exist and could not be created: " + path);
            }
        }

        // Check that it is a directory and writable
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("The provided log path is not a directory: " + path);
        }
        if (!dir.canWrite()) {
            throw new IllegalArgumentException("The provided log path is not writable: " + path);
        }

        log_path = path;
    }

    /**
     * Changes the desired path for log directory
     * @param path The new log directory path
     * @throws IllegalArgumentException if path is invalid
     */
    public static void setLogPath(String path) {
        validateAndSetPath(path);
        System.setProperty("LOG_PATH", path);

        // Reload Log4j2 configuration to apply the new path
        reloadLoggerConfig();
    }

    public static String getLogPath() {
        return log_path;
    }












    /**
     * Sets the file name for DEBUG log file.
     * @param fileName The new file name (e.g., "debug-custom.log")
     */
    public static void setDebugLogFileName(String fileName) {
        if (fileName != null && !fileName.trim().isEmpty()) {
            System.setProperty("DEBUG_LOG_NAME", fileName);
            reloadLoggerConfig();
        }
    }

    /**
     * Sets the file name for INFO log file.
     * @param fileName The new file name (e.g., "info-custom.log")
     */
    public static void setInfoLogFileName(String fileName) {
        if (fileName != null && !fileName.trim().isEmpty()) {
            System.setProperty("INFO_LOG_NAME", fileName);
            reloadLoggerConfig();
        }
    }

    /**
     * Sets the file name for WARN/ERROR log file.
     * @param fileName The new file name (e.g., "warn-custom.log")
     */
    public static void setWarnLogFileName(String fileName) {
        if (fileName != null && !fileName.trim().isEmpty()) {
            System.setProperty("WARN_LOG_NAME", fileName);
            reloadLoggerConfig();
        }
    }

    /**
     * Reloads the Log4j2 context to apply configuration changes.
     */
    private static void reloadLoggerConfig() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        context.reconfigure();
    }





































    //This stills need to test scenarios and what will happen if I give it 3 mb or something

    /**
     * Sets the maximum log file size before rotation.
     * This must be called before any logging occurs to take effect.
     * @param size The maximum file size (e.g., "5MB", "10MB", "1GB")
     * @throws IllegalArgumentException if the size is null or empty
     */
    public static void setLogFileSize(int size, SizeUnit unit) {
        if (size <= 0) {
            throw new IllegalArgumentException("Log size must be a positive integer.");
        }

        if (unit == null) {
            throw new IllegalArgumentException("Size unit must not be null.");
        }

        String formattedSize = size + unit.name();
        System.setProperty("LOG_SIZE", formattedSize);

        // Reload Log4j2 configuration to apply the new size
        reloadLoggerConfig();
    }




























        public static void logDebug(String message, HashMap<String, String> contextData) {
            applyContext(contextData);
            logger.debug(message);
        }

        public static void logInfo(String message, HashMap<String, String> contextData) {
            applyContext(contextData);
            logger.info(message);
        }

        public static void logWarn(String message, HashMap<String, String> contextData) {
            applyContext(contextData);
            logger.warn(message);
        }

        public static void logError(String message, HashMap<String, String> contextData) {
            applyContext(contextData);
            logger.error(message);
        }

        private static void applyContext(HashMap<String, String> contextData) {
            ThreadContext.clearMap(); // Clear previous context

            if (contextData != null) {
                for (String key : contextData.keySet()) {
                    ThreadContext.put(key, contextData.get(key));
                }
            }
        }
}
