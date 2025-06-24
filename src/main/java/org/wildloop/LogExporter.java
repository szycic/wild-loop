package org.wildloop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

/**
 * <p>
 * LogExporter is responsible for exporting simulation logs to files.
 * It manages the creation of log files, writing events to them, and archiving
 * logs with timestamps and {@link World} IDs.
 * </p>
 * <p>
 * The logs are stored in a specified directory with a consistent naming scheme,
 * allowing for easy retrieval and analysis of simulation events.
 * </p>
 *
 * @see EventLogger
 * @see Event
 * @see EventType
 */
public class LogExporter {
    /** The directory where log files are stored. */
    private static final String LOG_DIRECTORY = "logs/";
    /** The file extension for log files. */
    private static final String LOG_FILE_EXTENSION = ".log";
    /** The name of the latest log file, which is overwritten each time a new log is opened. */
    private static final String LATEST_LOG_FILE_NAME = "latest" + LOG_FILE_EXTENSION;
    /** The {@link BufferedWriter} used to write logs to the file. */
    private static BufferedWriter logWriter;
    /** The current {@link World} ID for which the log is being written. */
    private static String currentWorldId;
    
    static {
        File logDir = new File(LOG_DIRECTORY);
        if (!logDir.exists()) {
            createLogDirectory();
        }

        Consumer<Event> logListener = event -> {
            if (logWriter != null) {
                try {
                    logWriter.write(event.toString());
                    logWriter.newLine();
                    logWriter.flush();
                } catch (IOException e) {
                    System.err.println("Failed to write to log file: " + e.getMessage());
                }
            }
        };

        EventLogger.subscribe(logListener);
    }
    
    /**
     * Creates the logs' directory if it does not exist.
     * This method is called during static initialization to ensure that
     * the logs directory is available before any logging operations.
     */
    private static void createLogDirectory() {
        try {
            Files.createDirectories(Paths.get(LOG_DIRECTORY));
        } catch (IOException e) {
            System.err.println("Failed to create logs directory: " + e.getMessage());
        }
    }
    
    /**
     * Opens a new log file for the specified world ID.
     * This method initializes the log writer and subscribes to the {@link EventLogger}
     * to write events to the log file.
     *
     * @param worldId the unique identifier for the world being logged
     */
    public static void openLog(String worldId) {
        if (logWriter != null) {
            closeLog();
        }

        currentWorldId = worldId;
        
        try {
            logWriter = Files.newBufferedWriter(
                    Paths.get(LOG_DIRECTORY, LATEST_LOG_FILE_NAME),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            System.err.println("Failed to create log file: " + e.getMessage());
        }
    }

    /**
     * Closes the current log file and archives it by calling {@link #archiveLog()}.
     */
    public static void closeLog() {
        if (logWriter != null) {
            try {
                logWriter.close();
                archiveLog();
            } catch (IOException e) {
                System.err.println("Failed to close log file: " + e.getMessage());
            }
        }
    }

    /**
     * Archives the current log file by renaming it with a timestamp and the current world ID.
     */
    private static void archiveLog() {
        if (currentWorldId != null) {
            Path latestLogPath = Paths.get(LOG_DIRECTORY, LATEST_LOG_FILE_NAME);
            if (!Files.exists(latestLogPath)) {
                return;
            }
            
            try {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "T" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
                String archiveFileName = timestamp + "_world" + currentWorldId + LOG_FILE_EXTENSION;
                Path archiveLogPath = Paths.get(LOG_DIRECTORY, archiveFileName);
                
                Files.copy(latestLogPath, archiveLogPath, StandardCopyOption.REPLACE_EXISTING);
                currentWorldId = null;
            } catch (IOException e) {
                System.err.println("Failed to archive log file: " + e.getMessage());
            }
        }
    }
}
