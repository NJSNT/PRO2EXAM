package com.vinyl.assignment1.viewmodel;

import com.vinyl.assignment1.model.Vinyl;
import com.vinyl.assignment1.model.Session;
import com.vinyl.assignment1.util.Observer;
import com.vinyl.assignment1.network.NetworkClient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;

// 主视图模型, 承上启下连接 View 与 Model
public class MainViewModel implements Observer {

    private final NetworkClient client;
    private final ObservableList<Vinyl> vinylList;

    public MainViewModel(NetworkClient client) {
        this.client = client;
        this.vinylList = FXCollections.observableArrayList();
        
        // 注册观察者以监听数据变更
        this.client.addObserver(this);
        updateVinylList();
    }

    public ObservableList<Vinyl> getVinylList() {
        return vinylList;
    }

    public NetworkClient getClient() {
        return client;
    }

    @Override
    public void update(Vinyl vinyl) {
        // 确保 UI 刷新逻辑在推迟到 JavaFX 主线程 (Application Thread) 中执行
        Platform.runLater(this::updateVinylList);
    }

    private void updateVinylList() {
        // 增量更新逻辑: 仅在数据集内容发生实质改变时重新渲染拉取，从而保留用户在 UI 上的操作焦点
        if (vinylList.size() != client.getVinyls().size() || !vinylList.containsAll(client.getVinyls())) {
            vinylList.setAll(client.getVinyls());
        }
    }

    // --- 业务操作指派机制 (Delegation) ---
    public void reserveVinyl(Vinyl vinyl) {
        String currentUserId = Session.getInstance().getUsername();
        client.reserveVinyl(vinyl, currentUserId);
    }
    
    public void borrowVinyl(Vinyl vinyl) {
        String currentUserId = Session.getInstance().getUsername();
        client.borrowVinyl(vinyl, currentUserId);
    }
    
    public void returnVinyl(Vinyl vinyl) {
        String currentUserId = Session.getInstance().getUsername();
        client.returnVinyl(vinyl, currentUserId);
    }
    
    public void markForRemoval(Vinyl vinyl) {
        client.markVinylForRemoval(vinyl);
    }
}
