# LoggingLibrary

A **lightweight and configurable Java logging library** built on top of **Log4j2**, providing flexible, dynamic, and context-aware logging features.  
This project was developed as part of an internship to explore **customizable logging architectures** for large-scale systems.

---

## Features

- **Programmatic Log4j2 Configuration** – No XML needed! Configure everything directly in Java.  
- **Dynamic Log Paths** – Change log file locations at runtime.  
- **Automatic File Rotation** – Set maximum file sizes and automatic rollovers.  
- **Context-Aware Logging** – Separate logs per module, user, or environment.  
- **Lightweight Integration** – Easy to plug into any Java project.

---

## Design Overview

The library uses a **custom LogManager** to build Log4j2 configurations dynamically.  
Developers can define:
- Log levels (INFO, DEBUG, ERROR)
- File patterns and sizes
- Contextual identifiers (for multi-module systems)

---

## Usage Example

```java
import org.example.WMLoggingLibrary;
import java.util.HashMap;

public class ExampleApp {
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
        logger.logWarn("Warning issued", "Empty Product", context);
        logger.logError("An error occurred", "Network Disconnect", context);
    }
}

