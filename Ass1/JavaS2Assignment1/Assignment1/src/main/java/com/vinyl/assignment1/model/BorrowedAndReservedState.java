package com.vinyl.assignment1.model;

// 已出借且被预约状态 (Borrowed & Reserved)
public class BorrowedAndReservedState implements VinylState {

    @Override
    public boolean reserve(Vinyl vinyl, String userId) {
        // 拒绝覆盖: 已持有预约锁
        return false;
    }

    @Override
    public boolean borrow(Vinyl vinyl, String userId) {
        // 拒绝流转: 当前已被出借
        return false;
    }

    @Override
    public void returnVinyl(Vinyl vinyl) {
        vinyl.setBorrowingUserId(null);
        // 回滚至排队状态
        vinyl.setState(new ReservedState());
    }

    @Override
    public String getStateName() {
        return "Borrowed & Reserved";
    }
}
