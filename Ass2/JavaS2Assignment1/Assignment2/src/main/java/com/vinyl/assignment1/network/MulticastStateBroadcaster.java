package com.vinyl.assignment1.network;

import com.google.gson.Gson;
import com.vinyl.assignment1.model.Vinyl;
import com.vinyl.assignment1.model.VinylDTO;
import com.vinyl.assignment1.model.VinylLibrary;
import com.vinyl.assignment1.util.Observer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class MulticastStateBroadcaster implements Observer {
    private static final String MULTICAST_ADDRESS = "230.0.0.1";
    private static final int MULTICAST_PORT = 4446;
    private final MulticastSocket multicastSocket;
    private final InetAddress multicastGroup;
    private final Gson gson;
    private final VinylLibrary library;

    public MulticastStateBroadcaster(VinylLibrary library) {
        this.library = library;
        try {
            this.multicastGroup = InetAddress.getByName(MULTICAST_ADDRESS);
            this.multicastSocket = new MulticastSocket();
            this.gson = new Gson();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize multicast broadcaster", e);
        }
    }

    @Override
    public void update(Vinyl vinyl) {
        List<VinylDTO> dtos = library.getVinyls().stream()
                .map(Vinyl::toDTO)
                .collect(Collectors.toList());
        broadcastState(dtos);
    }

    private void broadcastState(List<VinylDTO> vinylList) {
        NetworkMessage updateMessage = NetworkMessage.createUpdateAll(vinylList);
        String json = gson.toJson(updateMessage);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, multicastGroup, MULTICAST_PORT);
        try {
            multicastSocket.send(packet);
        } catch (IOException e) {
            System.err.println("Failed to multicast state update: " + e.getMessage());
        }
    }

    public void close() {
        if (multicastSocket != null) {
            multicastSocket.close();
        }
    }
}
