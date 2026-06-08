package com.vinyl.assignment1.model;

// 已预约状态 (Reserved)
public class ReservedState implements VinylState {
    
    @Override
    public boolean reserve(Vinyl vinyl, String userId) {
        // 拒绝覆盖: 已持有预约锁
        return false;
    }

    @Override
    public boolean borrow(Vinyl vinyl, String userId) {
        // 鉴权: 仅预约持有人可执行出借动作
        if (vinyl.isReserved() && vinyl.getReservingUserId().equals(userId)) {
            vinyl.setReservingUserId(null); // 释放预约锁
            vinyl.setBorrowingUserId(userId);
            vinyl.setState(new BorrowedState());
            return true;
        }
        return false;
    }

    @Override
    public void returnVinyl(Vinyl vinyl) {
        // 无效操作 (未流通无法归还)
    }

    @Override
    public String getStateName() {
        return "Reserved";
    }
}
