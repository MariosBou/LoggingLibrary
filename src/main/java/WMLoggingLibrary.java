import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.LoggerContext;


public class WMLoggingLibrary {

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
            this.logPath = path;
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

        public Builder warnFileSize(String size) {
            this.warnFileSize = size;
            return this;
        }

        public Builder infoFileSize(String size) {
            this.infoFileSize = size;
            return this;
        }

        public Builder debugFileSize(String size) {
            this.debugFileSize = size;
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
