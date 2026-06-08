# Vinyl Library 实施计划

- [ ] 1. 设置 JavaFX 项目结构
  - 创建 Maven 或 Gradle 项目配置文件(pom.xml 或 build.gradle)
  - 添加 JavaFX 依赖和插件配置
  - 创建标准的包结构: com.vinyllibrary.model, com.vinyllibrary.viewmodel, com.vinyllibrary.view, com.vinyllibrary.util
  - 设置资源目录(src/main/resources)用于存放 FXML 文件

- [ ] 2. 实现状态模式(State Pattern)
  - [ ] 2.1 创建 VinylState 接口
    - 定义 reserve(Vinyl vinyl, String userId) 方法
    - 定义 borrow(Vinyl vinyl, String userId) 方法
    - 定义 returnVinyl(Vinyl vinyl) 方法
    - 定义 getStateName() 方法

  - [ ] 2.2 实现 AvailableState 类
    - 实现 reserve(): 设置 reservingUserId,转换状态到 ReservedState
    - 实现 borrow(): 设置 borrowingUserId,转换状态到 BorrowedState
    - 实现 returnVinyl(): 无操作
    - 实现 getStateName(): 返回 "Available"

  - [ ] 2.3 实现 BorrowedState 类
    - 实现 reserve(): 检查是否已有预约,如无则允许预约
    - 实现 borrow(): 只有预约用户可以借阅
    - 实现 returnVinyl(): 根据是否有预约转换到 AvailableState 或 ReservedState
    - 实现 getStateName(): 返回 "Borrowed"

  - [ ] 2.4 实现 ReservedState 类
    - 实现 reserve(): 拒绝新预约,返回 false
    - 实现 borrow(): 只有预约用户可以借阅
    - 实现 returnVinyl(): 无操作
    - 实现 getStateName(): 返回 "Reserved"

  - [ ] 2.5 实现 BorrowedAndReservedState 类
    - 实现 reserve(): 拒绝新预约,返回 false
    - 实现 borrow(): 只有预约用户可以借阅
    - 实现 returnVinyl(): 转换到 AvailableState
    - 实现 getStateName(): 返回 "Borrowed & Reserved"

  - [ ]* 2.6 为状态模式编写单元测试
    - 测试每个状态类的 reserve 方法
    - 测试每个状态类的 borrow 方法
    - 测试每个状态类的 returnVinyl 方法
    - 验证状态转换的正确性

- [ ] 3. 实现 Vinyl 模型类
  - [ ] 3.1 创建 Vinyl 类的基本结构和属性
    - 添加 SimpleStringProperty title
    - 添加 SimpleStringProperty artist
    - 添加 SimpleIntegerProperty releaseYear
    - 添加 ObjectProperty<VinylState> state,初始化为 AvailableState
    - 添加 StringProperty borrowingUserId
    - 添加 StringProperty reservingUserId
    - 添加 BooleanProperty markedForRemoval
    - 实现构造函数 Vinyl(String title, String artist, int year)

  - [ ] 3.2 实现 Vinyl 的状态操作方法
    - 实现 reserve(String userId): 委托给当前状态对象的 reserve 方法
    - 实现 borrow(String userId): 委托给当前状态对象的 borrow 方法
    - 实现 returnVinyl(): 委托给当前状态对象的 returnVinyl 方法
    - 实现 markForRemoval(): 设置 markedForRemoval 为 true

  - [ ] 3.3 实现 Vinyl 的状态查询方法
    - 实现 isAvailable(): 检查状态是否为 AvailableState
    - 实现 isBorrowed(): 检查 borrowingUserId 是否不为空
    - 实现 isReserved(): 检查 reservingUserId 是否不为空
    - 实现 canBorrow(String userId): 根据当前状态和用户 ID 判断
    - 实现 canReserve(String userId): 根据当前状态和用户 ID 判断

  - [ ] 3.4 实现 Vinyl 的访问器方法
    - 实现所有属性的 getter 方法
    - 实现所有 Property 的 getter 方法(titleProperty(), artistProperty()等)

  - [ ]* 3.5 为 Vinyl 类编写单元测试
    - 测试 reserve 方法的各种场景
    - 测试 borrow 方法的各种场景
    - 测试 returnVinyl 方法的各种场景
    - 测试 markForRemoval 方法
    - 验证不变量:Vinyl 状态唯一性

- [ ] 4. 实现观察者模式(Observer Pattern)
  - [ ] 4.1 创建 Observer 接口
    - 定义 update(Vinyl vinyl) 方法

  - [ ] 4.2 创建 Observable 接口或基类
    - 定义 addObserver(Observer observer) 方法
    - 定义 removeObserver(Observer observer) 方法
    - 定义 notifyObservers() 方法

- [ ] 5. 实现 VinylLibrary 管理类
  - [ ] 5.1 创建 VinylLibrary 类的基本结构
    - 添加 ObservableList<Vinyl> vinyls,使用 FXCollections.observableArrayList()
    - 添加 List<Observer> observers
    - 实现构造函数

  - [ ] 5.2 实现 VinylLibrary 的 Vinyl 管理方法
    - 实现 addVinyl(String title, String artist, int year): 创建 Vinyl 并添加到列表
    - 实现 removeVinyl(Vinyl vinyl): 从列表中移除 Vinyl
    - 实现 getVinyls(): 返回 vinyls 列表

  - [ ] 5.3 实现 VinylLibrary 的操作方法(使用 synchronized 保证线程安全)
    - 实现 reserveVinyl(Vinyl vinyl, String userId): 调用 vinyl.reserve(),通知观察者
    - 实现 borrowVinyl(Vinyl vinyl, String userId): 调用 vinyl.borrow(),通知观察者
    - 实现 returnVinyl(Vinyl vinyl, String userId): 调用 vinyl.returnVinyl(),通知观察者
    - 实现 markVinylForRemoval(Vinyl vinyl): 调用 vinyl.markForRemoval(),通知观察者

  - [ ] 5.4 实现 VinylLibrary 的观察者方法
    - 实现 addObserver(Observer observer): 添加观察者
    - 实现 removeObserver(Observer observer): 移除观察者
    - 实现 notifyObservers(): 通知所有观察者 Vinyl 状态变化

  - [ ]* 5.5 为 VinylLibrary 编写单元测试
    - 测试 addVinyl 和 removeVinyl 方法
    - 测试操作方法(reserveVinyl, borrowVinyl, returnVinyl, markVinylForRemoval)
    - 测试观察者通知机制
    - 验证不变量:预约唯一性,可观察性一致性
    - 测试线程安全性:多线程并发操作

- [ ] 6. 实现用户模拟器(UserSimulator)
  - [ ] 6.1 创建 UserSimulator 类的基本结构
    - 添加 VinylLibrary library 引用
    - 添加 String userId
    - 添加 boolean running 标志
    - 添加 Random random 对象
    - 添加 Thread simulatorThread
    - 实现构造函数 UserSimulator(VinylLibrary library, String userId)

  - [ ] 6.2 实现 UserSimulator 的核心方法
    - 实现 run(): 循环执行随机操作,使用 Thread.sleep() 控制间隔
    - 实现 start(): 启动模拟线程
    - 实现 stop(): 停止模拟线程

  - [ ] 6.3 实现 UserSimulator 的辅助方法
    - 实现 performRandomAction(): 随机选择预约、借阅、归还或移除操作
    - 实现 selectRandomVinyl(): 从 VinylLibrary 中随机选择一个 Vinyl
    - 实现 getRandomInterval(): 生成 1-5 秒的随机间隔

- [ ] 7. 实现 Session 单例类
  - [ ] 7.1 创建 Session 类的基本结构
    - 创建 static Session instance
    - 添加 StringProperty currentUserId,初始化为默认用户 "user1"
    - 添加 ObjectProperty<Vinyl> selectedVinyl
    - 实现私有构造函数

  - [ ] 7.2 实现 Session 的单例方法
    - 实现 static getInstance(): 返回单例实例,使用双重检查锁定

  - [ ] 7.3 实现 Session 的访问器方法
    - 实现 getCurrentUserId() 和 setCurrentUserId(String userId)
    - 实现 getSelectedVinyl() 和 setSelectedVinyl(Vinyl vinyl)
    - 实现 currentUserIdProperty() 和 selectedVinylProperty()

- [ ] 8. 实现 MainViewModel
  - [ ] 8.1 创建 MainViewModel 类的基本结构
    - 添加 VinylLibrary library 引用
    - 添加 Session session 引用
    - 添加 ObjectProperty<Vinyl> selectedVinyl
    - 添加 StringProperty currentUserId
    - 添加 ObservableList<Vinyl> vinylList,绑定到 library.getVinyls()
    - 添加 BooleanProperty canReserve, canBorrow, canReturn, canRemove
    - 实现构造函数 MainViewModel(VinylLibrary library, Session session)

  - [ ] 8.2 实现 MainViewModel 的数据绑定方法
    - 实现 bindToModel(VinylLibrary library): 绑定 vinylList 到 library 的 vinyls
    - 实现 onVinylSelected(Vinyl vinyl): 更新 selectedVinyl 属性
    - 实现 updateActionStates(): 根据 selectedVinyl 的状态更新 canReserve, canBorrow 等

  - [ ] 8.3 实现 MainViewModel 的命令方法
    - 实现 reserve(): 调用 library.reserveVinyl(selectedVinyl.get(), currentUserId.get())
    - 实现 borrow(): 调用 library.borrowVinyl(selectedVinyl.get(), currentUserId.get())
    - 实现 return_(): 调用 library.returnVinyl(selectedVinyl.get(), currentUserId.get())
    - 实现 remove(): 调用 library.markVinylForRemoval(selectedVinyl.get())

  - [ ] 8.4 实现 MainViewModel 的观察者通知
    - 让 MainViewModel 实现 Observer 接口
    - 实现 onVinylStateChanged(Vinyl vinyl): 更新 UI 状态

  - [ ]* 8.5 为 MainViewModel 编写单元测试
    - 测试数据绑定是否正确
    - 测试命令执行逻辑
    - 测试操作状态更新
    - 测试观察者通知

- [ ] 9. 实现 AddVinylViewModel
  - [ ] 9.1 创建 AddVinylViewModel 类的基本结构
    - 添加 VinylLibrary library 引用
    - 添加 StringProperty title
    - 添加 StringProperty artist
    - 添加 IntegerProperty releaseYear
    - 添加 BooleanProperty isValid
    - 实现构造函数 AddVinylViewModel(VinylLibrary library)

  - [ ] 9.2 实现 AddVinylViewModel 的核心方法
    - 实现 addVinyl(): 验证表单,调用 library.addVinyl(),清空表单
    - 实现 validateForm(): 检查所有字段是否不为空,releaseYear 是否为有效年份
    - 实现 clearForm(): 重置所有字段

  - [ ]* 9.3 为 AddVinylViewModel 编写单元测试
    - 测试表单验证逻辑
    - 测试 addVinyl 方法
    - 测试 clearForm 方法

- [ ] 10. 创建 MainView.fxml 布局文件
  - [ ] 10.1 设计主界面布局
    - 创建 BorderPane 作为根容器
    - 在顶部区域添加 HBox,包含:用户 ID 显示,模拟器控制按钮(启动/停止)
    - 在中心区域添加 TableView<Vinyl>,显示 Vinyl 列表
    - 在底部区域添加 HBox,包含:预约、借阅、归还、移除、添加 Vinyl 按钮
    - 添加状态栏显示操作结果

  - [ ] 10.2 配置 TableView 和 TableColumn
    - 配置 vinylTable,设置列宽,启用选择模式
    - 配置 titleColumn,绑定到 Vinyl.titleProperty()
    - 配置 artistColumn,绑定到 Vinyl.artistProperty()
    - 配置 yearColumn,绑定到 Vinyl.releaseYearProperty()
    - 配置 stateColumn,绑定到 Vinyl.stateProperty().getStateName()
    - 为 markedForRemoval 的 Vinyl 添加样式类

  - [ ] 10.3 配置按钮和事件处理
    - 配置所有按钮的 fx:id 和 onAction
    - 添加按钮禁用状态绑定到 ViewModel 的 BooleanProperty

- [ ] 11. 实现 MainViewController
  - [ ] 11.1 创建 MainViewController 类的基本结构
    - 添加 MainViewModel viewModel 引用
    - 添加 UserSimulator simulator 引用
    - 添加所有@FXML 注解的 UI 组件(vinylTable, titleColumn, artistColumn 等)
    - 添加所有@FXML 注解的按钮(reserveButton, borrowButton 等)

  - [ ] 11.2 实现 MainViewController 的初始化方法
    - 实现 initialize(): 创建 ViewModel,绑定数据到 UI,设置监听器
    - 绑定 vinylTable 的 items 到 viewModel.vinylList
    - 绑定各按钮的 disableProperty 到对应的 BooleanProperty
    - 为 vinylTable 的 selectionModel.selectedItemProperty() 添加监听器

  - [ ] 11.3 实现 MainViewController 的事件处理方法
    - 实现 onReserveClicked(): 调用 viewModel.reserve()
    - 实现 onBorrowClicked(): 调用 viewModel.borrow()
    - 实现 onReturnClicked(): 调用 viewModel.return_()
    - 实现 onRemoveClicked(): 调用 viewModel.remove()
    - 实现 onAddVinylClicked(): 打开 AddVinylView 窗口
    - 实现 onStartSimulationClicked(): 创建并启动 UserSimulator
    - 实现 onStopSimulationClicked(): 停止 UserSimulator
    - 添加错误处理:使用 Alert 显示操作结果

- [ ] 12. 创建 AddVinylView.fxml 布局文件
  - [ ] 12.1 设计添加 Vinyl 对话框布局
    - 创建 VBox 作为根容器,添加内边距和间距
    - 添加标题 Label "Add New Vinyl"
    - 添加 GridPane 包含表单字段(标题、艺术家、发行年份)
    - 添加 HBox 包含保存和取消按钮

  - [ ] 12.2 配置表单控件
    - 配置 titleField,设置 promptText
    - 配置 artistField,设置 promptText
    - 配置 yearField,设置 promptText,限制输入为数字
    - 配置 saveButton 和 cancelButton 的 fx:id 和 onAction

- [ ] 13. 实现 AddVinylController
  - [ ] 13.1 创建 AddVinylController 类的基本结构
    - 添加 AddVinylViewModel viewModel 引用
    - 添加 Stage stage 引用
    - 添加所有@FXML 注解的表单控件

  - [ ] 13.2 实现 AddVinylController 的初始化方法
    - 实现 initialize(): 创建 ViewModel,绑定数据到 UI

  - [ ] 13.3 实现 AddVinylController 的事件处理方法
    - 实现 onSaveClicked(): 调用 viewModel.addVinyl(),关闭窗口
    - 实现 onCancelClicked(): 关闭窗口
    - 实现 setStage(Stage stage): 设置窗口引用

- [ ] 14. 实现应用程序入口 Main 类
  - [ ] 14.1 创建 Main 类的基本结构
    - 继承 Application 类
    - 添加 VinylLibrary library
    - 添加 Session session

  - [ ] 14.2 实现 Main 的启动方法
    - 实现 start(Stage primaryStage): 加载 MainView.fxml,设置标题,显示窗口
    - 创建 VinylLibrary 实例并添加初始 Vinyl 数据
    - 创建 Session 实例并设置当前用户 ID
    - 传入 MainViewController 所需的参数

  - [ ] 14.3 实现 Main 的 main 方法
    - 实现 main(String[] args): 调用 launch(args)

- [ ] 15. 检查点 - 确保应用程序可以启动
  - 运行应用程序,确保可以启动并显示主界面
  - 验证表格中显示初始 Vinyl 数据
  - 验证按钮状态正确

- [ ] 16. 实现错误处理和用户反馈
  - [ ] 16.1 添加错误提示机制
    - 创建工具方法 showErrorAlert(String title, String message)
    - 创建工具方法 showInfoAlert(String title, String message)
    - 创建工具方法 showWarningAlert(String title, String message)

  - [ ] 16.2 在 ViewModel 中添加错误处理
    - 在命令方法中捕获异常
    - 使用 Platform.runLater() 显示 Alert
    - 提供友好的错误消息

  - [ ] 16.3 在 Model 中添加输入验证
    - 在 VinylLibrary 的操作方法中添加参数验证
    - 在 AddVinylViewModel 的 validateForm 中添加验证逻辑

- [ ] 17. 实现线程安全和并发控制
  - [ ] 17.1 确保 Model 层的线程安全
    - 为 VinylLibrary 的所有操作方法添加 synchronized 关键字
    - 使用 FXCollections.synchronizedObservableList() 包装 vinyls 列表

  - [ ] 17.2 确保 ViewModel 层的线程安全（防止 "Not on FX application thread" 崩溃）
    - 任何由后台线程通过 Observer 触发的 UI 更新，必须使用 Platform.runLater() 包装
    - 在观察者模式的回调方法中，严格使用 Platform.runLater() 来更新 UI 属性

  - [ ] 17.3 测试多线程场景
    - 启动多个 UserSimulator 实例
    - 验证状态转换的正确性
    - 验证 GUI 实时更新

- [ ] 18. 检查点 - 确保所有功能正常工作
  - 测试预约、借阅、归还、移除功能
  - 测试多用户模拟器
  - 验证 GUI 自动更新
  - 确保没有线程安全问题

- [ ]* 19. 编写集成测试
  - [ ]* 19.1 测试 MVVM 层之间的交互
    - 测试数据从 Model 到 View 的正确传递
    - 测试命令从 View 到 Model 的正确执行

  - [ ]* 19.2 测试观察者模式
    - 测试状态变化是否正确通知所有观察者
    - 测试 GUI 是否实时更新

  - [ ]* 19.3 测试并发场景
    - 创建多个线程同时操作同一 Vinyl
    - 验证系统正确处理并发请求
    - 验证数据一致性

- [ ]* 20. 编写 GUI 测试
  - [ ]* 20.1 设置 TestFX 测试环境
    - 添加 TestFX 依赖
    - 配置测试运行器

  - [ ]* 20.2 编写 UI 交互测试
    - 测试所有按钮功能
    - 测试表格选择和显示
    - 测试表单验证和提交

- [ ] 21. 优化和文档
  - [ ] 21.1 代码优化
    - 重构重复代码
    - 添加必要的注释
    - 遵循 Java 代码规范

  - [ ] 21.2 更新文档
    - 更新 README.md
    - 添加运行说明
    - 添加 UML 图的 Astah 导出文件(可选)
