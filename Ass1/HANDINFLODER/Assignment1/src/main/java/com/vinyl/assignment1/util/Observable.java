package com.vinyl.assignment1.util;

// 被观察者接口 (Observable Pattern)
public interface Observable {
    void addObserver(Observer o);
    void removeObserver(Observer o);
}
