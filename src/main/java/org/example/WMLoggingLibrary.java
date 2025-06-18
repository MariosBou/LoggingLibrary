package org.example;

import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.LoggerContext;
import java.io.IOException;
import java.nio.file.*;

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


    private WMLoggingLibrary() { }

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


        public Builder logPath(String path) {
            Path logDirPath = Paths.get(path);

            try {
                // Try to create the directory if it doesn't exist
                Files.createDirectories(logDirPath);

                // Check if it is a directory and writable
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
            if (size <= 0) {
                throw new IllegalArgumentException("File size must be positive");
            }
            this.debugFileSize = size + unit.getUnit();
            return this;
        }

        public Builder infoFileSize(int size, SizeUnit unit) {
            if (size <= 0) {
                throw new IllegalArgumentException("File size must be positive");
            }
            this.infoFileSize = size + unit.getUnit();
            return this;
        }

        public Builder warnFileSize(int size, SizeUnit unit) {
            if (size <= 0) {
                throw new IllegalArgumentException("File size must be positive");
            }
            this.warnFileSize = size + unit.getUnit();
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

            ((LoggerContext) LogManager.getContext(false)).reconfigure();
            return new WMLoggingLibrary();
        }
    }

    public Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}
