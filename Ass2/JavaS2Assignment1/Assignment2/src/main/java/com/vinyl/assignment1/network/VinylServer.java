package com.vinyl.assignment1.network;

import com.vinyl.assignment1.model.VinylLibrary;
import com.vinyl.assignment1.util.ServerLogger;
import com.vinyl.assignment1.network.MulticastStateBroadcaster;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class VinylServer {
    private static final int PORT = 2910;
    private final VinylLibrary library;

    public VinylServer() {
        this.library = new VinylLibrary();
        MulticastStateBroadcaster multicastBroadcaster = new MulticastStateBroadcaster(this.library);
        this.library.addObserver(multicastBroadcaster);
        // Load seed data into library
        library.addVinyl("Abbey Road", "The Beatles", 1969);
        library.addVinyl("The Dark Side of the Moon", "Pink Floyd", 1973);
        library.addVinyl("Thriller", "Michael Jackson", 1982);
        library.addVinyl("Random Access Memories", "Daft Punk", 2013);
        library.addVinyl("Rumours", "Fleetwood Mac", 1977);
    }

    public void start() {
        ServerLogger.getInstance().log("Server", "Starting Vinyl Server on port " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            ServerLogger.getInstance().log("Server", "Vinyl Server started successfully. Waiting for clients...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, library);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            ServerLogger.getInstance().log("Server", "Server exception: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        VinylServer server = new VinylServer();
        server.start();
    }
}
