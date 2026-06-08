package com.vinyl.assignment1.model;

// 空闲状态 (Available)
public class AvailableState implements VinylState {
    @Override
    public boolean reserve(Vinyl vinyl, String userId) {
        if (vinyl.isMarkedForRemoval()) return false;
        vinyl.setReservingUserId(userId);
        vinyl.setState(new ReservedState());
        return true;
    }

    @Override
    public boolean borrow(Vinyl vinyl, String userId) {
        vinyl.setBorrowingUserId(userId);
        vinyl.setState(new BorrowedState());
        return true;
    }

    @Override
    public void returnVinyl(Vinyl vinyl) {
        // 无效操作 (Invalid transition)
    }

    @Override
    public String getStateName() {
        return "Available";
    }
}
