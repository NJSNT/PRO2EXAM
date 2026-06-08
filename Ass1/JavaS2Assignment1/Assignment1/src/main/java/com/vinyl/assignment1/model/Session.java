package com.vinyl.assignment1.model;

public class Session {
    // 内部静态单例实例
    private static Session instance;
    
    // 全局业务数据 (例如：当前登录用户的用户名)
    private String username;

    // 1. 私有化构造方法，防止外部实例化
    private Session() {
        // 默认初始化要求指定的固定用户名
        this.username = "Ondrej"; 
    }

    // 2. 提供全局唯一访问入口
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    // --- Getters & Setters ---
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
