package com.vinyl.assignment1.network;

import com.vinyl.assignment1.model.VinylLibrary;

public interface  MessageStrategy {
    NetworkMessage execute(NetworkMessage request, VinylLibrary library, String clientIp);
}
