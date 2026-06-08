package com.vinyl.assignment1.model;

// 已出借状态 (Borrowed)
public class BorrowedState implements VinylState {
    @Override
    public boolean reserve(Vinyl vinyl, String userId) {
        if (vinyl.isMarkedForRemoval()) return false;
        // 鉴权: 确保目标未处于预约状态才允许预约
        if (!vinyl.isReserved()) {
          vinyl.setReservingUserId(userId);
          vinyl.setState(new BorrowedAndReservedState());
          return true;
        } else {
          return false;
        }
    }

    @Override
    public boolean borrow(Vinyl vinyl, String userId) {
        // 拒绝流转: 当前已被出借
        return false;
    }

    @Override
    public void returnVinyl(Vinyl vinyl) {
        // 释放借阅持有人
        vinyl.setBorrowingUserId(null); 
        // 状态分发: 根据预约判定回滚节点
        if (vinyl.isReserved()) {
            vinyl.setState(new ReservedState());
        } else {
            vinyl.setState(new AvailableState());
        }
    }

    @Override
    public String getStateName() {
        return "Borrowed";
    }
}
