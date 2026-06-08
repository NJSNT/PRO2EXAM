package com.vinyl.assignment1.view;

import com.vinyl.assignment1.model.Vinyl;
import com.vinyl.assignment1.model.Session;
import com.vinyl.assignment1.model.UserSimulator;
import com.vinyl.assignment1.viewmodel.MainViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.binding.Bindings;

// 主视图控制器 (MainViewController)
public class MainViewController {

    private MainViewModel viewModel;
    private UserSimulator simulator;

    // FXML UI 组件映射
    @FXML private Label currentUserLabel;
    @FXML private TableView<Vinyl> vinylTable;
    @FXML private TableColumn<Vinyl, String> titleColumn;
    @FXML private TableColumn<Vinyl, String> artistColumn;
    @FXML private TableColumn<Vinyl, Number> yearColumn;
    @FXML private TableColumn<Vinyl, String> stateColumn;

    @FXML private Button reserveButton;
    @FXML private Button borrowButton;
    @FXML private Button returnButton;
    @FXML private Button removeButton;
    @FXML private Button addVinylButton;
    @FXML private Button startSimulationButton;
    @FXML private Button stopSimulationButton;

    // 上下文依赖注入 (DI)
    public void setViewModel(MainViewModel viewModel) {
        this.viewModel = viewModel;
        vinylTable.setItems(viewModel.getVinylList());
    }

    public void setSimulator(UserSimulator simulator) {
        this.simulator = simulator;
    }

    // JavaFX 生命周期钩子方法：完成 FXML 加载后自动执行
    @FXML
    public void initialize() {
        // UI 数据列绑定 (Data Binding)
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        artistColumn.setCellValueFactory(cellData -> cellData.getValue().artistProperty());
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().releaseYearProperty());

        // 动态状态 Binding: 基于原状态属性更新派生字符展示
        stateColumn.setCellValueFactory(cellData -> 
            Bindings.createStringBinding(
                () -> cellData.getValue().getState().getStateName(),
                cellData.getValue().stateProperty()
            )
        );

        // 表格选择变更事件监听注册
        vinylTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // System.out.println("Selected: " + newSelection.getTitle());
            }
        });
    }

    // --- UI 行为入口映射 ---

    @FXML
    public void onReserveClicked() {
        Vinyl selectedVinyl = vinylTable.getSelectionModel().getSelectedItem();
        if (selectedVinyl != null && viewModel != null) {
            viewModel.reserveVinyl(selectedVinyl);
            vinylTable.refresh(); // 强制刷新视图确保 UI 状态一致性
        }
    }

    @FXML
    public void onBorrowClicked() {
        Vinyl selectedVinyl = vinylTable.getSelectionModel().getSelectedItem();
        if (selectedVinyl != null && viewModel != null) {
            viewModel.borrowVinyl(selectedVinyl);
            vinylTable.refresh();
        }
    }

    @FXML
    public void onReturnClicked() {
        Vinyl selectedVinyl = vinylTable.getSelectionModel().getSelectedItem();
        if (selectedVinyl != null && viewModel != null) {
            viewModel.returnVinyl(selectedVinyl);
            vinylTable.refresh();
        }
    }

    @FXML
    public void onRemoveClicked() {
        Vinyl selectedVinyl = vinylTable.getSelectionModel().getSelectedItem();
        if (selectedVinyl != null && viewModel != null) {
            viewModel.markForRemoval(selectedVinyl);
            vinylTable.refresh();
        }
    }

    @FXML
    public void onAddVinylClicked() {
        try {
            // 解析拉起子弹窗页面 (AddVinylView)
            javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/vinyl/assignment1/view/AddVinylView.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(fxmlLoader.load());
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Add New Vinyl");
            stage.setScene(scene);
            
            AddVinylController controller = fxmlLoader.getController();
            com.vinyl.assignment1.viewmodel.AddVinylViewModel addViewModel = new com.vinyl.assignment1.viewmodel.AddVinylViewModel(viewModel.getClient());
            controller.setViewModel(addViewModel, stage);
            
            // 模态阻塞调用
            stage.showAndWait();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onStartSimulationClicked() {
        if (simulator != null) {
            simulator.start();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Simulator Status");
            alert.setHeaderText(null);
            alert.setContentText("Simulator has started.");
            alert.showAndWait();
        }
    }

    @FXML
    public void onStopSimulationClicked() {
        if (simulator != null) {
            simulator.stop();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Simulator Status");
            alert.setHeaderText(null);
            alert.setContentText("Simulator has stopped.");
            alert.showAndWait();
        }
    }
}
