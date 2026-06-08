package com.vinyl.assignment1.network;

import com.vinyl.assignment1.model.Vinyl;
import com.vinyl.assignment1.model.VinylDTO;
import com.vinyl.assignment1.util.Observable;
import com.vinyl.assignment1.util.Observer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class NetworkClient implements Observable {
    private static NetworkClient instance;
    private final String host;
    private final int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private MulticastSocket multicastSocket;
    private InetAddress multicastGroup;
    private static final String MULTICAST_ADDRESS = "230.0.0.1";
    private static final int MULTICAST_PORT = 4446;
    private final ObservableList<Vinyl> vinyls;
    private final List<Observer> observers;
    private final Gson gson;
    private boolean running;

    private NetworkClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.vinyls = FXCollections.observableArrayList();
        this.observers = new ArrayList<>();
        this.gson = new Gson();
    }

    public static synchronized NetworkClient getInstance() {
        if (instance == null) {
            instance = new NetworkClient("localhost", 2910);
        }
        return instance;
    }

    public void start() {
        if (running) return;
        try {
            this.socket = new Socket(host, port);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.running = true;

            startMulticastListener();

            // Spawn receiver listener thread for TCP responses
            Thread thread = new Thread(this::receiveLoop);
            thread.setDaemon(true);
            thread.start();

            // Fetch initial state using TCP; subsequent updates arrive over multicast
            requestAllVinyls();
        } catch (IOException e) {
            System.err.println("Failed to connect to Vinyl Server at " + host + ":" + port + " - " + e.getMessage());
            close();
        }
    }

    private void startMulticastListener() throws IOException {
        this.multicastGroup = InetAddress.getByName(MULTICAST_ADDRESS);
        this.multicastSocket = new MulticastSocket(MULTICAST_PORT);
        this.multicastSocket.joinGroup(multicastGroup);

        Thread multicastThread = new Thread(this::receiveMulticastLoop);
        multicastThread.setDaemon(true);
        multicastThread.start();
    }

    private void receiveMulticastLoop() {
        try {
            byte[] buffer = new byte[65536];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            while (running && multicastSocket != null && !multicastSocket.isClosed()) {
                multicastSocket.receive(packet);
                String line = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                try {
                    NetworkMessage msg = gson.fromJson(line, NetworkMessage.class);
                    if (msg != null && msg.getType() == NetworkMessage.Type.UPDATE_ALL) {
                        Platform.runLater(() -> {
                            List<Vinyl> list = new ArrayList<>();
                            for (VinylDTO dto : msg.getVinylList()) {
                                list.add(Vinyl.fromDTO(dto));
                            }
                            this.vinyls.setAll(list);
                            notifyObservers(null);
                        });
                    }
                } catch (Exception ex) {
                    System.err.println("Multicast parsing error: " + ex.getMessage());
                }
            }
        } catch (IOException e) {
            if (running) {
                System.err.println("Multicast listener disconnected: " + e.getMessage());
            }
        }
    }

    private void receiveLoop() {
        try {
            String line;
            while (running && (line = in.readLine()) != null) {
                try {
                    NetworkMessage msg = gson.fromJson(line, NetworkMessage.class);
                    if (msg != null) {
                        handleIncomingMessage(msg);
                    }
                } catch (Exception ex) {
                    System.err.println("Client parsing error: " + ex.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server stream disconnected: " + e.getMessage());
        } finally {
            close();
        }
    }

    private void handleIncomingMessage(NetworkMessage msg) {
        if (msg.getType() == NetworkMessage.Type.UPDATE_ALL) {
            Platform.runLater(() -> {
                List<Vinyl> list = new ArrayList<>();
                for (VinylDTO dto : msg.getVinylList()) {
                    list.add(Vinyl.fromDTO(dto));
                }
                this.vinyls.setAll(list);
                notifyObservers(null);
            });
        } else if (msg.getType() == NetworkMessage.Type.ERROR) {
            String errorUser = msg.getUsername();
            String currentUser = com.vinyl.assignment1.model.Session.getInstance().getUsername();
            if (errorUser == null || errorUser.equalsIgnoreCase(currentUser)) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Server Operations Error");
                    alert.setHeaderText("Action Rejected by Server");
                    alert.setContentText(msg.getErrorMsg());
                    alert.showAndWait();
                });
            } else {
                System.err.println("Background worker [" + errorUser + "] request rejected by server: " + msg.getErrorMsg());
            }
        }
    }

    public ObservableList<Vinyl> getVinyls() {
        return vinyls;
    }

    public void requestAllVinyls() {
        send(NetworkMessage.createGetAll());
    }

    public void reserveVinyl(Vinyl vinyl, String username) {
        send(NetworkMessage.createReserve(vinyl.toDTO(), username));
    }

    public void borrowVinyl(Vinyl vinyl, String username) {
        send(NetworkMessage.createBorrow(vinyl.toDTO(), username));
    }

    public void returnVinyl(Vinyl vinyl, String username) {
        send(NetworkMessage.createReturn(vinyl.toDTO(), username));
    }

    public void markVinylForRemoval(Vinyl vinyl) {
        send(NetworkMessage.createRemove(vinyl.toDTO()));
    }

    public void addVinyl(String title, String artist, int year) {
        VinylDTO dto = new VinylDTO(title, artist, year, "Available", null, null, false);
        send(NetworkMessage.createAdd(dto));
    }

    private void send(NetworkMessage msg) {
        if (out != null) {
            String json = gson.toJson(msg);
            out.println(json);
        }
    }

    @Override
    public synchronized void addObserver(Observer o) {
        if (o != null && !observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public synchronized void removeObserver(Observer o) {
        observers.remove(o);
    }

    private synchronized void notifyObservers(Vinyl vinyl) {
        for (Observer o : observers) {
            o.update(vinyl);
        }
    }

    public void close() {
        if (!running) return;
        running = false;
        try {
            if (multicastSocket != null) {
                if (multicastGroup != null) {
                    multicastSocket.leaveGroup(multicastGroup);
                }
                multicastSocket.close();
            }
            if (socket != null) socket.close();
            if (in != null) in.close();
            if (out != null) out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
