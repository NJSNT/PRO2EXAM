package com.vinyl.assignment1.network;

import com.vinyl.assignment1.model.Vinyl;
import com.vinyl.assignment1.model.VinylDTO;
import com.vinyl.assignment1.model.VinylLibrary;
import com.vinyl.assignment1.util.Observer;
import com.vinyl.assignment1.util.ServerLogger;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final VinylLibrary library;
    private final String ipAddress;
    private final Gson gson;
    private BufferedReader in;
    private PrintWriter out;
    private boolean running;

    public ClientHandler(Socket socket, VinylLibrary library) {
        this.socket = socket;
        this.library = library;
        this.ipAddress = socket.getInetAddress().getHostAddress();
        this.gson = new Gson();
    }

    @Override
    public void run() {
        ServerLogger.getInstance().log(ipAddress, "Client connected.");
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.running = true;

            // Send initial state to the client immediately upon connection
            sendCurrentState();

            String line;
            while (running && (line = in.readLine()) != null) {
                ServerLogger.getInstance().log(ipAddress, "Received: " + line);
                try {
                    NetworkMessage request = gson.fromJson(line, NetworkMessage.class);
                    if (request != null && request.getType() != null) {
                        MessageStrategy strategy = MessageStrategyFactory.getStrategy(request.getType());
                        if (strategy != null) {
                            NetworkMessage response = strategy.execute(request, library, ipAddress);
                            if (response != null) {
                                send(response);
                            }
                        } else {
                            send(NetworkMessage.createError("Unknown or unsupported action type."));
                        }
                    }
                } catch (Exception ex) {
                    ServerLogger.getInstance().log(ipAddress, "Error processing command: " + ex.getMessage());
                    send(NetworkMessage.createError("Server processing error: " + ex.getMessage()));
                }
            }
        } catch (IOException e) {
            ServerLogger.getInstance().log(ipAddress, "Connection issue: " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    private void sendCurrentState() {
        try {
            List<VinylDTO> dtos = library.getVinyls().stream()
                    .map(Vinyl::toDTO)
                    .collect(Collectors.toList());
            send(NetworkMessage.createUpdateAll(dtos));
        } catch (Exception e) {
            ServerLogger.getInstance().log(ipAddress, "Failed to send initial inventory: " + e.getMessage());
        }
    }

    public synchronized void send(NetworkMessage message) {
        if (out != null) {
            String json = gson.toJson(message);
            out.println(json);
        }
    }

    private void cleanup() {
        running = false;
        library.removeObserver((Observer) this);
        ServerLogger.getInstance().log(ipAddress, "Client disconnected.");
        try {
            if (socket != null) socket.close();
            if (in != null) in.close();
            if (out != null) out.close();
        } catch (IOException e) {
            ServerLogger.getInstance().log(ipAddress, "Failed to close socket: " + e.getMessage());
        }
    }
}
