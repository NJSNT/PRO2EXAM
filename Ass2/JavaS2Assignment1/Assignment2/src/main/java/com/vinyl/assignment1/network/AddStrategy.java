package com.vinyl.assignment1.network;

import com.vinyl.assignment1.model.VinylDTO;
import com.vinyl.assignment1.model.VinylLibrary;

public class AddStrategy implements MessageStrategy {
    @Override
    public NetworkMessage execute(NetworkMessage request, VinylLibrary library, String clientIp) {
        VinylDTO dto = request.getVinyl();
        if (dto == null || dto.getTitle() == null || dto.getTitle().trim().isEmpty() ||
            dto.getArtist() == null || dto.getArtist().trim().isEmpty()) {
            return NetworkMessage.createError("Invalid request: Complete vinyl details required.");
        }

        synchronized (library) {
            boolean exists = library.getVinyls().stream()
                    .anyMatch(v -> v.getTitle().equalsIgnoreCase(dto.getTitle().trim()));

            if (exists) {
                return NetworkMessage.createError("A vinyl with the title '" + dto.getTitle().trim() + "' already exists.");
            }

            library.addVinyl(dto.getTitle().trim(), dto.getArtist().trim(), dto.getReleaseYear());
            return null; // Handled by server broadcast
        }
    }
}
