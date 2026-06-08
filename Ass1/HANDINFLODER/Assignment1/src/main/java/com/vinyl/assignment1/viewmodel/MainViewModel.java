package com.vinyl.assignment1.viewmodel;

import com.vinyl.assignment1.model.Vinyl;
import com.vinyl.assignment1.model.VinylLibrary;
import com.vinyl.assignment1.model.Session;
import com.vinyl.assignment1.util.Observer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;

// 主视图模型, 承上启下连接 View 与 Model
public class MainViewModel implements Observer {

    private final VinylLibrary library;
    private final ObservableList<Vinyl> vinylList;

    public MainViewModel(VinylLibrary library) {
        this.library = library;
        this.vinylList = FXCollections.observableArrayList();
        
        // 注册观察者以监听数据变更
        this.library.addObserver(this);
        updateVinylList();
    }

    public ObservableList<Vinyl> getVinylList() {
        return vinylList;
    }

    public VinylLibrary getLibrary() {
        return library;
    }

    @Override
    public void update(Vinyl vinyl) {
        // 确保 UI 刷新逻辑在推迟到 JavaFX 主线程 (Application Thread) 中执行
        Platform.runLater(() -> {
            updateVinylList();
        });
    }

    private void updateVinylList() {
        // 增量更新逻辑: 仅在数据集内容发生实质改变时重新渲染拉取，从而保留用户在 UI 上的操作焦点
        if (vinylList.size() != library.getVinyls().size() || !vinylList.containsAll(library.getVinyls())) {
            vinylList.setAll(library.getVinyls());
        }
    }

    // --- 业务操作指派机制 (Delegation) ---
    public void reserveVinyl(Vinyl vinyl) {
        String currentUserId = Session.getInstance().getUsername();
        library.reserveVinyl(vinyl, currentUserId);
    }
    
    public void borrowVinyl(Vinyl vinyl) {
        String currentUserId = Session.getInstance().getUsername();
        library.borrowVinyl(vinyl, currentUserId);
    }
    
    public void returnVinyl(Vinyl vinyl) {
        String currentUserId = Session.getInstance().getUsername();
        library.returnVinyl(vinyl, currentUserId);
    }
    
    public void markForRemoval(Vinyl vinyl) {
        library.markVinylForRemoval(vinyl);
    }
}
