package com.vinyl.assignment1.network;

import com.vinyl.assignment1.model.VinylLibrary;
import com.vinyl.assignment1.model.VinylDTO;
import java.util.List;
import java.util.stream.Collectors;

public class GetAllStrategy implements MessageStrategy {
    @Override
    public NetworkMessage execute(NetworkMessage request, VinylLibrary library, String clientIp) {
        List<VinylDTO> dtos = library.getVinyls().stream()
                .map(v -> v.toDTO())
                .collect(Collectors.toList());
        return NetworkMessage.createUpdateAll(dtos);
    }
}
