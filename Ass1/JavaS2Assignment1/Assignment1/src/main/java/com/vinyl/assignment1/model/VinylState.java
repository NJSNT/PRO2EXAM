package com.vinyl.assignment1.model;

// 状态模式接口
public interface VinylState {
    boolean reserve(Vinyl vinyl, String userId);
    boolean borrow(Vinyl vinyl, String userId);
    void returnVinyl(Vinyl vinyl);
    String getStateName();
}
