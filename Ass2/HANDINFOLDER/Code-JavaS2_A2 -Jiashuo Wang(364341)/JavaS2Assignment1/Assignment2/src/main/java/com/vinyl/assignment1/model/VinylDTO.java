package com.vinyl.assignment1.model;

public class VinylDTO {
    private String title;
    private String artist;
    private int releaseYear;
    private String state;
    private String borrowingUserId;
    private String reservingUserId;
    private boolean markedForRemoval;

    public VinylDTO() {}

    public VinylDTO(String title, String artist, int releaseYear, String state, String borrowingUserId, String reservingUserId, boolean markedForRemoval) {
        this.title = title;
        this.artist = artist;
        this.releaseYear = releaseYear;
        this.state = state;
        this.borrowingUserId = borrowingUserId;
        this.reservingUserId = reservingUserId;
        this.markedForRemoval = markedForRemoval;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBorrowingUserId() {
        return borrowingUserId;
    }

    public void setBorrowingUserId(String borrowingUserId) {
        this.borrowingUserId = borrowingUserId;
    }

    public String getReservingUserId() {
        return reservingUserId;
    }

    public void setReservingUserId(String reservingUserId) {
        this.reservingUserId = reservingUserId;
    }

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public void setMarkedForRemoval(boolean markedForRemoval) {
        this.markedForRemoval = markedForRemoval;
    }
}
