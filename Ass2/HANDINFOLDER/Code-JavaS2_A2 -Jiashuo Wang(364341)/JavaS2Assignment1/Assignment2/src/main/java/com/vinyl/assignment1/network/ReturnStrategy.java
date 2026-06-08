package com.vinyl.assignment1.network;

import com.vinyl.assignment1.model.Vinyl;
import com.vinyl.assignment1.model.VinylLibrary;

public class ReturnStrategy implements MessageStrategy {
    @Override
    public NetworkMessage execute(NetworkMessage request, VinylLibrary library, String clientIp) {
        String username = request.getUsername();
        if (request.getVinyl() == null || request.getVinyl().getTitle() == null) {
            return NetworkMessage.createError("Invalid request: No vinyl title provided.", username);
        }
        
        String title = request.getVinyl().getTitle();
        
        if (username == null || username.trim().isEmpty()) {
            return NetworkMessage.createError("Invalid request: No username provided.", username);
        }

        synchronized (library) {
            Vinyl vinyl = library.getVinyls().stream()
                    .filter(v -> v.getTitle().equalsIgnoreCase(title))
                    .findFirst()
                    .orElse(null);

            if (vinyl == null) {
                return NetworkMessage.createError("Vinyl not found in server library: " + title, username);
            }

            if (!vinyl.isBorrowed()) {
                return NetworkMessage.createError("Vinyl '" + title + "' is not currently borrowed.", username);
            }

            if (!username.equalsIgnoreCase(vinyl.getBorrowingUserId())) {
                return NetworkMessage.createError("Unauthorized return: Only the borrower (" + vinyl.getBorrowingUserId() + ") can return this vinyl.", username);
            }

            library.returnVinyl(vinyl, username);

            if (!vinyl.isBorrowed()) {
                return null; // Handled by server broadcast
            } else {
                return NetworkMessage.createError("Failed to return vinyl '" + title + "'. Check current state.", username);
            }
        }
    }
}
