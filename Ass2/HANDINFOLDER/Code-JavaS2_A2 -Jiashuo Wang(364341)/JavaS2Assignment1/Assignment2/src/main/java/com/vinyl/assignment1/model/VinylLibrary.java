package com.vinyl.assignment1.model;

import java.util.ArrayList;
import java.util.List;

import com.vinyl.assignment1.util.Observable;
import com.vinyl.assignment1.util.Observer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VinylLibrary implements Observable {
    // 采用同步集合保证高并发读写环境下的线程安全性
    private final ObservableList<Vinyl> vinyls;
    private final List<Observer> observers;

    public VinylLibrary() {
        this.vinyls = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
        this.observers = new ArrayList<>();
    }

    public synchronized void addVinyl(Vinyl vinyl) {
        if (vinyl != null && !vinyls.contains(vinyl)) {
            vinyls.add(vinyl);
            notifyObservers(vinyl);
        }
    }

    // 数据初始化便利方法
    public synchronized void addVinyl(String title, String artist, int year) {
        Vinyl newVinyl = new Vinyl(title, artist, year);
        vinyls.add(newVinyl);
        notifyObservers(newVinyl);
    }

    public synchronized void removeVinyl(Vinyl vinyl) {
        if (vinyl != null && vinyls.contains(vinyl)) {
            vinyls.remove(vinyl);
            notifyObservers(vinyl);
        }
    }

    public ObservableList<Vinyl> getVinyls() {
        return vinyls;
    }

    // --- 核心业务逻辑 (Core Domain Logic) ---

    // 预约流程
    public synchronized void reserveVinyl(Vinyl vinyl, String userId) {
        if (vinyl != null) {
            boolean success = vinyl.reserve(userId);
            if (success) {
                notifyObservers(vinyl);
            }
        }
    }

    // 借阅流程
    public synchronized void borrowVinyl(Vinyl vinyl, String userId) {
        if (vinyl != null) {
            boolean success = vinyl.borrow(userId);
            if (success) {
                notifyObservers(vinyl);
            }
        }
    }

    // 归还流程
    public synchronized void returnVinyl(Vinyl vinyl, String userId) {
        if (vinyl != null) {
            // 鉴权: 仅当前持有人可执行归还操作
            if (userId != null && userId.equals(vinyl.getBorrowingUserId())) {
                vinyl.returnVinyl();
                
                // 若该物品已处于销毁标记状态且无人排队等待, 则执行软删除到硬删除的流转
                if (vinyl.isMarkedForRemoval() && !vinyl.isReserved()) {
                    vinyls.remove(vinyl);
                }
                
                notifyObservers(vinyl);
            }
        }
    }

    // 标记销毁
    public synchronized void markVinylForRemoval(Vinyl vinyl) {
        if (vinyl != null) {
            if (vinyl.isBorrowed() || vinyl.isReserved()) {
                // 若仍在流通周期内, 仅作标记以延迟销毁
                vinyl.setMarkedForRemoval(true);
                notifyObservers(vinyl);
            } else {
                // 空闲状态直接进行硬删除
                vinyls.remove(vinyl);
                notifyObservers(vinyl);
            }
        }
    }

    // --- 发布-订阅模式 (Observer Pattern) ---

    @Override
    public synchronized void addObserver(Observer o) {
        if (o != null && !observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public synchronized void removeObserver(Observer o) {
        observers.remove(o);
    }

    private synchronized void notifyObservers(Vinyl vinyl) {
        for (Observer o : observers) {
            o.update(vinyl);
        }
    }
}
