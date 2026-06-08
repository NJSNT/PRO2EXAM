package com.vinyl.assignment1.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerLogger {
    private static ServerLogger instance;
    private static final Object lock = new Object();
    private PrintWriter fileWriter;

    private ServerLogger() {
        try {
            String fileName = "server-log-" + java.time.LocalDate.now().toString() + ".txt";
            this.fileWriter = new PrintWriter(new FileWriter(fileName, true), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ServerLogger getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ServerLogger();
                }
            }
        }
        return instance;
    }

    public synchronized void log(String ipAddress, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logEntry = String.format("[%s] [IP: %s] %s", timestamp, ipAddress, message);
        System.out.println(logEntry);
        if (fileWriter != null) {
            fileWriter.println(logEntry);
        }
    }
}
