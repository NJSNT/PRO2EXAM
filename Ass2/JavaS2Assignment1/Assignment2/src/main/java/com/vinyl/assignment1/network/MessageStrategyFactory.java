package com.vinyl.assignment1.network;

import java.util.HashMap;
import java.util.Map;

public class MessageStrategyFactory {
    private static final Map<NetworkMessage.Type, MessageStrategy> strategies = new HashMap<>();

    static {
        strategies.put(NetworkMessage.Type.GET_ALL, new GetAllStrategy());
        strategies.put(NetworkMessage.Type.RESERVE, new ReserveStrategy());
        strategies.put(NetworkMessage.Type.BORROW, new BorrowStrategy());
        strategies.put(NetworkMessage.Type.RETURN, new ReturnStrategy());
        strategies.put(NetworkMessage.Type.REMOVE, new RemoveStrategy());
        strategies.put(NetworkMessage.Type.ADD, new AddStrategy());
    }

    public static MessageStrategy getStrategy(NetworkMessage.Type type) {
        return strategies.get(type);
    }
}
