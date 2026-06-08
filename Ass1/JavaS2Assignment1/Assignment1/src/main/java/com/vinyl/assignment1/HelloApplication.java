package com.vinyl.assignment1;

import com.vinyl.assignment1.model.VinylLibrary;
import com.vinyl.assignment1.model.UserSimulator;
import com.vinyl.assignment1.view.MainViewController;
import com.vinyl.assignment1.viewmodel.MainViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    
    // 模拟后台用户的独立线程
    private UserSimulator simulator;

    @Override
    public void start(Stage stage) throws IOException {
        // ========== 1. 构建 Model 层 ==========
        // 初始化全局库并预先添加样例数据
        VinylLibrary library = new VinylLibrary();
        library.addVinyl("Abbey Road", "The Beatles", 1969);
        library.addVinyl("The Dark Side of the Moon", "Pink Floyd", 1973);
        library.addVinyl("Thriller", "Michael Jackson", 1982);
        library.addVinyl("Random Access Memories", "Daft Punk", 2013);
        library.addVinyl("Rumours", "Fleetwood Mac", 1977);

        // 创建多线程后台模拟器
        simulator = new UserSimulator(library, "Robot-Alice");

        // ========== 2. 构建 ViewModel 层 ==========
        // 初始化核心 ViewModel 并绑定数据模型
        MainViewModel mainViewModel = new MainViewModel(library);

        // ========== 3. 加载 View 界面层 ==========
        // 解析主视图 FXML
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/vinyl/assignment1/view/MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        
        // 获取视图 Controller 并注入 ViewModel 和 模拟器
        MainViewController controller = fxmlLoader.getController();
        controller.setViewModel(mainViewModel);
        controller.setSimulator(simulator);

        // ========== 4. 启动应用 ==========
        stage.setTitle("Vinyl Library");
        stage.setScene(scene);
        stage.show();
    }

    // 监听应用关闭事件
    @Override
    public void stop() throws Exception {
        // 销毁后台模拟器线程以安全退出
        if (simulator != null) {
            simulator.stop();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}
