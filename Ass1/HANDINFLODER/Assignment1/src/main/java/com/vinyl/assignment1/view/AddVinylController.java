package com.vinyl.assignment1.view;

import com.vinyl.assignment1.viewmodel.AddVinylViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// 添加唱片视图控制器 (AddVinylController)
public class AddVinylController {

    private AddVinylViewModel viewModel;
    private Stage stage; // 持有当前窗体引用以执行生命周期控制 (如关闭)

    @FXML private TextField titleField;
    @FXML private TextField artistField;
    @FXML private TextField yearField;
    @FXML private Label errorLabel;
    
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    // 上下文依赖注入 (DI) 和组件初始化
    public void setViewModel(AddVinylViewModel viewModel, Stage stage) {
        this.viewModel = viewModel;
        this.stage = stage;
        
        // --- 双向数据绑定 (Bidirectional Binding) ---
        // UI 对本文框的更改与 ViewModel 属性实时同步映射
        titleField.textProperty().bindBidirectional(viewModel.titleProperty());
        artistField.textProperty().bindBidirectional(viewModel.artistProperty());
        yearField.textProperty().bindBidirectional(viewModel.releaseYearProperty());

        // 错误提示仅需要自 ViewModel 到 UI 视图的单向绑定监听 (Unidirectional Binding)
        errorLabel.textProperty().bind(viewModel.errorLabelProperty());
    }

    @FXML
    public void onSaveClicked() {
        // 请求触发验证通过后的存盘保存动作
        if (viewModel.addVinyl()) {
            // 当存盘流转完毕，执行当前弹窗退出挂起
            stage.close();
        }
    }

    @FXML
    public void onCancelClicked() {
        // 请求强制抛弃数据上下文并销毁当前弹窗
        stage.close();
    }
}
