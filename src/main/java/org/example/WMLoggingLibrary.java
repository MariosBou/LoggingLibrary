package org.example;

import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.LoggerContext;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;

public class WMLoggingLibrary {

    public enum SizeUnit {
        KB("KB"), MB("MB"), GB("GB");

        private final String unit;

        SizeUnit(String unit) {
            this.unit = unit;
        }

        public String getUnit() {
            return unit;
        }
    }

    private final Logger logger;

    // Private constructor; only Builder can create an instance
    private WMLoggingLibrary(Logger logger) {
        this.logger = logger;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String logPath;
        private String infoFileName;
        private String debugFileName;
        private String warnFileName;
        private String warnFileSize;
        private String infoFileSize;
        private String debugFileSize;
        private String loggerName = WMLoggingLibrary.class.getName();

        public Builder logPath(String path) {
            Path logDirPath = Paths.get(path);

            try {
                Files.createDirectories(logDirPath);

                if (!Files.isDirectory(logDirPath)) {
                    throw new IllegalArgumentException("The provided path is not a directory: " + path);
                }

                if (!Files.isWritable(logDirPath)) {
                    throw new IllegalArgumentException("The provided log path is not writable: " + path);
                }

                this.logPath = path;
            } catch (IOException e) {
                throw new RuntimeException("Failed to access or create log path: " + path, e);
            }

            return this;
        }

        public Builder infoFileName(String name) {
            this.infoFileName = name;
            return this;
        }

        public Builder debugFileName(String name) {
            this.debugFileName = name;
            return this;
        }

        public Builder warnFileName(String name) {
            this.warnFileName = name;
            return this;
        }

        public Builder debugFileSize(int size, SizeUnit unit) {
            if (size <= 0) throw new IllegalArgumentException("File size must be positive");
            this.debugFileSize = size + unit.getUnit();
            return this;
        }

        public Builder infoFileSize(int size, SizeUnit unit) {
            if (size <= 0) throw new IllegalArgumentException("File size must be positive");
            this.infoFileSize = size + unit.getUnit();
            return this;
        }

        public Builder warnFileSize(int size, SizeUnit unit) {
            if (size <= 0) throw new IllegalArgumentException("File size must be positive");
            this.warnFileSize = size + unit.getUnit();
            return this;
        }

        public Builder loggerName(String name) {
            this.loggerName = name;
            return this;
        }

        public WMLoggingLibrary build() {
            if (logPath != null) System.setProperty("LOG_PATH", logPath);
            if (infoFileName != null) System.setProperty("INFO_LOG_NAME", infoFileName);
            if (warnFileName != null) System.setProperty("WARN_LOG_NAME", warnFileName);
            if (debugFileName != null) System.setProperty("DEBUG_LOG_NAME", debugFileName);
            if (warnFileSize != null) System.setProperty("WARN_LOG_SIZE", warnFileSize);
            if (infoFileSize != null) System.setProperty("INFO_LOG_SIZE", infoFileSize);
            if (debugFileSize != null) System.setProperty("DEBUG_LOG_SIZE", debugFileSize);

            Path configPath = Paths.get("src", "main", "resources", "log4j2.xml");
            if (!Files.exists(configPath)) {
                try {
                    String xml = generateLog4j2();
                    Files.write(configPath, xml.getBytes());
                    System.setProperty("log4j.configurationFile", configPath.toUri().toString());
                } catch (IOException e) {
                    throw new RuntimeException("Could not create log4j2.xml", e);
                }
            }


            // Reload configuration
            ((LoggerContext) LogManager.getContext(false)).reconfigure();

            // Create the logger
            Logger logger = LogManager.getLogger(loggerName);
            return new WMLoggingLibrary(logger);
        }
    }

    private static String generateLog4j2() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Configuration status=\"DEBUG\">\n" +
                "    <Properties>\n" +
                "        <Property name=\"LOG_PATTERN\">%d{yyyy-MM-dd'T'HH:mm:ss.SSSZ} %X %C [%p]: %m%n</Property>\n" +
                "        <Property name=\"APP_LOG_ROOT\">${sys:LOG_PATH:-logs}</Property>\n" +
                "        <Property name=\"DEBUG_LOG_SIZE\">${sys:DEBUG_LOG_SIZE:-3MB}</Property>\n" +
                "        <Property name=\"INFO_LOG_SIZE\">${sys:INFO_LOG_SIZE:-3MB}</Property>\n" +
                "        <Property name=\"WARN_LOG_SIZE\">${sys:WARN_LOG_SIZE:-3MB}</Property>\n" +
                "        <Property name=\"DEBUG_LOG_NAME\">${sys:DEBUG_LOG_NAME:-debug.log}</Property>\n" +
                "        <Property name=\"INFO_LOG_NAME\">${sys:INFO_LOG_NAME:-info.log}</Property>\n" +
                "        <Property name=\"WARN_LOG_NAME\">${sys:WARN_LOG_NAME:-warn.log}</Property>\n" +
                "    </Properties>\n" +
                "    <Appenders>\n" +
                "        <RollingFile name=\"DebugLog\" fileName=\"${APP_LOG_ROOT}/${DEBUG_LOG_NAME}\" filePattern=\"${APP_LOG_ROOT}/${DEBUG_LOG_NAME}.%i\" append=\"true\">\n" +
                "            <LevelRangeFilter minLevel=\"DEBUG\" maxLevel=\"DEBUG\" onMatch=\"ACCEPT\" onMismatch=\"DENY\"/>\n" +
                "            <PatternLayout pattern=\"${LOG_PATTERN}\"/>\n" +
                "            <Policies><SizeBasedTriggeringPolicy size=\"${DEBUG_LOG_SIZE}\"/></Policies>\n" +
                "            <DefaultRolloverStrategy fileIndex=\"min\" max=\"5\"/>\n" +
                "        </RollingFile>\n" +
                "        <RollingFile name=\"InfoLog\" fileName=\"${APP_LOG_ROOT}/${INFO_LOG_NAME}\" filePattern=\"${APP_LOG_ROOT}/${INFO_LOG_NAME}.%i\" append=\"true\">\n" +
                "            <LevelRangeFilter minLevel=\"INFO\" maxLevel=\"INFO\" onMatch=\"ACCEPT\" onMismatch=\"DENY\"/>\n" +
                "            <PatternLayout pattern=\"${LOG_PATTERN}\"/>\n" +
                "            <Policies><SizeBasedTriggeringPolicy size=\"${INFO_LOG_SIZE}\"/></Policies>\n" +
                "            <DefaultRolloverStrategy fileIndex=\"min\" max=\"5\"/>\n" +
                "        </RollingFile>\n" +
                "        <RollingFile name=\"WarnLog\" fileName=\"${APP_LOG_ROOT}/${WARN_LOG_NAME}\" filePattern=\"${APP_LOG_ROOT}/${WARN_LOG_NAME}.%i\" append=\"true\">\n" +
                "            <LevelRangeFilter minLevel=\"ERROR\" maxLevel=\"WARN\" onMatch=\"ACCEPT\" onMismatch=\"DENY\"/>\n" +
                "            <PatternLayout pattern=\"${LOG_PATTERN}\"/>\n" +
                "            <Policies><SizeBasedTriggeringPolicy size=\"${WARN_LOG_SIZE}\"/></Policies>\n" +
                "            <DefaultRolloverStrategy fileIndex=\"min\" max=\"5\"/>\n" +
                "        </RollingFile>\n" +
                "    </Appenders>\n" +
                "    <Loggers>\n" +
                "        <Root level=\"TRACE\">\n" +
                "            <AppenderRef ref=\"DebugLog\"/>\n" +
                "            <AppenderRef ref=\"InfoLog\"/>\n" +
                "            <AppenderRef ref=\"WarnLog\"/>\n" +
                "        </Root>\n" +
                "    </Loggers>\n" +
                "</Configuration>";
    }



    public void logDebug(String message, HashMap<String, String> contextData) {
        applyContext(contextData);
        logger.debug(message);
    }

    public void logInfo(String message, HashMap<String, String> contextData) {
        applyContext(contextData);
        logger.info(message);
    }

    public void logWarn(String message, String event, HashMap<String, String> contextData) {
        HashMap<String, String> warnContext = new HashMap<>();

        if (contextData != null) {
            warnContext.putAll(contextData);
        }

        warnContext.put("Event", event);

        applyContext(warnContext);
        logger.warn(message);
    }

    public void logError(String message, String event, HashMap<String, String> contextData) {
        HashMap<String, String> errorContext = new HashMap<>();

        if (contextData != null) {
            errorContext.putAll(contextData);
        }

        errorContext.put("Event", event);

        applyContext(errorContext);
        logger.error(message);
    }


    private static void applyContext(HashMap<String, String> contextData) {
        ThreadContext.clearMap();
        if (contextData != null) {
            for (String key : contextData.keySet()) {

                String value = contextData.get(key);

                if (key == null || key.trim().isEmpty()) {
                    throw new IllegalArgumentException("Context key cannot be null or empty");
                }
                if (value == null || value.trim().isEmpty()) {
                    throw new IllegalArgumentException("Context value for key '" + key + "' cannot be null or empty");
                }

                try {
                    ThreadContext.put(key, value);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Failed to add context key: " + key, e);
                }
            }
        }
    }

    private static void applyContext(String contextData) {
        ThreadContext.clearMap();
        if (contextData != null && !contextData.trim().isEmpty()) {
            ThreadContext.put("Event", contextData);
        }
    }
}
