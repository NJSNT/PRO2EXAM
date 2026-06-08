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
        // ========== 1. 构建 Network 客户端 ==========
        com.vinyl.assignment1.network.NetworkClient client = com.vinyl.assignment1.network.NetworkClient.getInstance();
        client.start();

        // 创建多线程后台模拟器
        simulator = new UserSimulator(client, "Robot-Alice");

        // ========== 2. 构建 ViewModel 层 ==========
        // 初始化核心 ViewModel 并绑定数据模型
        MainViewModel mainViewModel = new MainViewModel(client);

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
        com.vinyl.assignment1.network.NetworkClient.getInstance().close();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}
