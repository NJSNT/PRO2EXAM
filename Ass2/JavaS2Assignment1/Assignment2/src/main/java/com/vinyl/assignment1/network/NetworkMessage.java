package com.vinyl.assignment1.network;

import com.vinyl.assignment1.model.VinylDTO;
import java.util.List;

public class NetworkMessage {
    public enum Type {
        GET_ALL,      // Request all vinyls
        RESERVE,      // Request reserve
        BORROW,       // Request borrow
        RETURN,       // Request return
        REMOVE,       // Request remove
        ADD,          // Request add new
        UPDATE_ALL,   // Response / Broadcast updated vinyl list
        ERROR         // Response error details
    }

    private Type type;
    private String username;
    private String errorMsg;
    private VinylDTO vinyl;
    private List<VinylDTO> vinylList;

    public NetworkMessage() {}

    public NetworkMessage(Type type) {
        this.type = type;
    }

    public static NetworkMessage createGetAll() {
        return new NetworkMessage(Type.GET_ALL);
    }

    public static NetworkMessage createReserve(VinylDTO vinyl, String username) {
        NetworkMessage msg = new NetworkMessage(Type.RESERVE);
        msg.vinyl = vinyl;
        msg.username = username;
        return msg;
    }

    public static NetworkMessage createBorrow(VinylDTO vinyl, String username) {
        NetworkMessage msg = new NetworkMessage(Type.BORROW);
        msg.vinyl = vinyl;
        msg.username = username;
        return msg;
    }

    public static NetworkMessage createReturn(VinylDTO vinyl, String username) {
        NetworkMessage msg = new NetworkMessage(Type.RETURN);
        msg.vinyl = vinyl;
        msg.username = username;
        return msg;
    }

    public static NetworkMessage createRemove(VinylDTO vinyl) {
        NetworkMessage msg = new NetworkMessage(Type.REMOVE);
        msg.vinyl = vinyl;
        return msg;
    }

    public static NetworkMessage createAdd(VinylDTO vinyl) {
        NetworkMessage msg = new NetworkMessage(Type.ADD);
        msg.vinyl = vinyl;
        return msg;
    }

    public static NetworkMessage createUpdateAll(List<VinylDTO> vinylList) {
        NetworkMessage msg = new NetworkMessage(Type.UPDATE_ALL);
        msg.vinylList = vinylList;
        return msg;
    }

    public static NetworkMessage createError(String errorMsg) {
        return createError(errorMsg, null);
    }

    public static NetworkMessage createError(String errorMsg, String username) {
        NetworkMessage msg = new NetworkMessage(Type.ERROR);
        msg.errorMsg = errorMsg;
        msg.username = username;
        return msg;
    }

    // Getters and Setters
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public VinylDTO getVinyl() {
        return vinyl;
    }

    public void setVinyl(VinylDTO vinyl) {
        this.vinyl = vinyl;
    }

    public List<VinylDTO> getVinylList() {
        return vinylList;
    }

    public void setVinylList(List<VinylDTO> vinylList) {
        this.vinylList = vinylList;
    }
}
