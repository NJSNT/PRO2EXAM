package com.vinyl.assignment1.network;

import com.vinyl.assignment1.model.Vinyl;
import com.vinyl.assignment1.model.VinylLibrary;

public class RemoveStrategy implements MessageStrategy {
    @Override
    public NetworkMessage execute(NetworkMessage request, VinylLibrary library, String clientIp) {
        if (request.getVinyl() == null || request.getVinyl().getTitle() == null) {
            return NetworkMessage.createError("Invalid request: No vinyl title provided.");
        }
        
        String title = request.getVinyl().getTitle();

        synchronized (library) {
            Vinyl vinyl = library.getVinyls().stream()
                    .filter(v -> v.getTitle().equalsIgnoreCase(title))
                    .findFirst()
                    .orElse(null);

            if (vinyl == null) {
                return NetworkMessage.createError("Vinyl not found in server library: " + title);
            }

            library.markVinylForRemoval(vinyl);
            return null; // Handled by server broadcast
        }
    }
}
