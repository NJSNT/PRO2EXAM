# Requirements Document

## Introduction

本需求文档定义了一个黑胶唱片(Vinyl)图书馆管理系统的功能规格。该系统允许用户浏览、预订、借阅和归还黑胶唱片，同时支持多个用户并发操作。系统使用 MVVM 架构模式，并实现了观察者、状态和线程设计模式以确保系统的可维护性和正确性。

## Glossary

- **Vinyl Library System**: 黑胶唱片管理系统，提供 Vinyl 对象的管理和用户交互功能
- **Vinyl**: 黑胶唱片实体，包含标题、艺术家、发行年份和借阅状态
- **State**: Vinyl 的状态，包括可用(available)、已借阅(borrowed)、已保留(reserved)及其组合
- **Available**: Vinyl 未被借阅且无预约的状态
- **Borrowed**: Vinyl 已被借出的状态
- **Reserved**: Vinyl 被某个用户预约的状态
- **Borrow**: 用户借阅 Vinyl 的操作
- **Return**: 用户归还 Vinyl 的操作
- **Reserve**: 用户预约 Vinyl 的操作
- **Remove**: 从图书馆中移除 Vinyl 的操作，可能需要先标记为待移除
- **User ID**: 用于标识系统中的唯一用户
- **Runnable**: 一个线程类，用于模拟其他用户的随机操作
- **Observer Pattern**: 观察者设计模式，用于在 Vinyl 状态变化时通知 GUI 更新
- **State Pattern**: 状态设计模式，用于管理 Vinyl 的不同状态
- **MVVM Pattern**: 模型-视图-视图模型设计模式，用于分离业务逻辑和 UI

## Requirements

### Requirement 1: Vinyl Data Model

**User Story:** AS 图书馆管理员，我想要定义 Vinyl 的基本属性和状态，以便系统能够正确管理每个黑胶唱片的信息。

#### Acceptance Criteria

1. The Vinyl Library System SHALL store a Vinyl with a title attribute
2. The Vinyl Library System SHALL store a Vinyl with an artist attribute
3. The Vinyl Library System SHALL store a Vinyl with a release year attribute
4. The Vinyl Library System SHALL store a Vinyl with a lending state attribute
5. The Vinyl Library System SHALL provide a mechanism to retrieve Vinyl title
6. The Vinyl Library System SHALL provide a mechanism to retrieve Vinyl artist
7. The Vinyl Library System SHALL provide a mechanism to retrieve Vinyl release year
8. The Vinyl Library System SHALL provide a mechanism to retrieve Vinyl lending state

### Requirement 2: Vinyl State Management

**User Story:** AS 图书馆用户，我想要查看 Vinyl 的当前状态，以便了解哪些唱片可以借阅或预约。

#### Acceptance Criteria

1. WHEN a Vinyl is neither borrowed nor reserved, the Vinyl Library System SHALL mark the Vinyl as available
2. WHEN a Vinyl is borrowed by a user, the Vinyl Library System SHALL set the Vinyl state to borrowed
3. WHEN a Vinyl is reserved by a user, the Vinyl Library System SHALL set the Vinyl state to reserved
4. WHEN a user borrows a reserved Vinyl, the Vinyl Library System SHALL set the Vinyl state to borrowed
5. The Vinyl Library System SHALL use the State Pattern to manage Vinyl state transitions

### Requirement 3: Vinyl Reservation

**User Story:** AS 图书馆用户，我想要预约一个 Vinyl，以便确保我能够在该唱片可用时借阅它。

#### Acceptance Criteria

1. WHEN a user attempts to reserve an available Vinyl, the Vinyl Library System SHALL accept the reservation
2. WHEN a user attempts to reserve a borrowed Vinyl that has no existing reservation, the Vinyl Library System SHALL accept the reservation
3. WHEN a user attempts to reserve a Vinyl that already has a reservation, the Vinyl Library System SHALL reject the reservation
4. WHEN a Vinyl is reserved, the Vinyl Library System SHALL store the user ID of the reserving user
5. WHILE a Vinyl is reserved, the Vinyl Library System SHALL prevent other users from reserving the same Vinyl

### Requirement 4: Vinyl Borrowing

**User Story:** AS 图书馆用户，我想要借阅一个 Vinyl，以便能够欣赏音乐。

#### Acceptance Criteria

1. WHEN a user attempts to borrow an available Vinyl, the Vinyl Library System SHALL accept the borrowing request
2. WHEN a user attempts to borrow a Vinyl that is reserved by the same user, the Vinyl Library System SHALL accept the borrowing request
3. WHEN a user attempts to borrow a Vinyl that is reserved by a different user, the Vinyl Library System SHALL reject the borrowing request
4. WHEN a user successfully borrows a Vinyl, the Vinyl Library System SHALL store the user ID of the borrowing user
5. WHEN a user successfully borrows a Vinyl, the Vinyl Library System SHALL update the Vinyl state to borrowed
6. WHEN a user successfully borrows a reserved Vinyl, the Vinyl Library System SHALL clear the reservation

### Requirement 5: Vinyl Return

**User Story:** AS 图书馆用户，我想要归还一个 Vinyl，以便其他用户可以借阅它。

#### Acceptance Criteria

1. WHEN a user returns a borrowed Vinyl, the Vinyl Library System SHALL update the Vinyl state to available
2. WHEN a user returns a borrowed Vinyl, the Vinyl Library System SHALL clear the borrowing user ID
3. IF a returned Vinyl was marked for removal and has no reservation, the Vinyl Library System SHALL remove the Vinyl from the library
4. IF a returned Vinyl was marked for removal but has a reservation, the Vinyl Library System SHALL keep the Vinyl in the library

### Requirement 6: Vinyl Removal

**User Story:** AS 图书馆管理员，我想要从图书馆中移除一个 Vinyl，以便管理馆藏。

#### Acceptance Criteria

1. WHEN a user attempts to remove an available Vinyl that has no reservation, the Vinyl Library System SHALL immediately remove the Vinyl
2. WHEN a user attempts to remove a Vinyl that is borrowed, the Vinyl Library System SHALL mark the Vinyl for removal without immediately deleting it
3. WHEN a user attempts to remove a Vinyl that is reserved, the Vinyl Library System SHALL mark the Vinyl for removal without immediately deleting it
4. WHEN a Vinyl is marked for removal, the Vinyl Library System SHALL prevent new reservations on the Vinyl
5. WHEN a Vinyl is marked for removal, the Vinyl Library System SHALL allow the current reserving user to borrow the Vinyl
6. WHEN a marked Vinyl is returned with no pending reservation, the Vinyl Library System SHALL remove the Vinyl from the library
7. The Vinyl Library System SHALL provide a visual indicator in the GUI showing which Vinyls are marked for removal

### Requirement 7: User Identification

**User Story:** AS 图书馆用户，我想要在系统中拥有唯一的身份标识，以便系统能够追踪我借阅和预约的 Vinyl。

#### Acceptance Criteria

1. The Vinyl Library System SHALL assign a unique user ID to each user
2. WHEN a user performs a borrow operation, the Vinyl Library System SHALL attach the user ID to the Vinyl
3. WHEN a user performs a reserve operation, the Vinyl Library System SHALL attach the user ID to the Vinyl
4. The Vinyl Library System SHALL validate that a user can only borrow Vinyls reserved by the same user ID

### Requirement 8: GUI Display

**User Story:** AS 图书馆用户，我想要在图形界面中查看所有 Vinyl 及其信息，以便选择我想要的唱片。

#### Acceptance Criteria

1. WHEN the GUI is displayed, the Vinyl Library System SHALL show a list of all Vinyls
2. WHEN the GUI is displayed, the Vinyl Library System SHALL display the title for each Vinyl
3. WHEN the GUI is displayed, the Vinyl Library System SHALL display the artist for each Vinyl
4. WHEN the GUI is displayed, the Vinyl Library System SHALL display the release year for each Vinyl
5. WHEN the GUI is displayed, the Vinyl Library System SHALL display the lending state for each Vinyl
6. IF a Vinyl is marked for removal, the Vinyl Library System SHALL display this information in the GUI

### Requirement 9: GUI Actions

**User Story:** AS 图书馆用户，我想要选择一个 Vinyl 并执行操作，以便管理我对该 Vinyl 的使用。

#### Acceptance Criteria

1. WHEN a user selects a Vinyl in the GUI, the Vinyl Library System SHALL highlight the selected Vinyl
2. WHEN a user clicks the Reserve button on a selected Vinyl, the Vinyl Library System SHALL attempt to reserve the Vinyl for the current user
3. WHEN a user clicks the Borrow button on a selected Vinyl, the Vinyl Library System SHALL attempt to borrow the Vinyl for the current user
4. WHEN a user clicks the Return button on a selected Vinyl, the Vinyl Library System SHALL return the Vinyl
5. WHEN a user clicks the Remove button on a selected Vinyl, the Vinyl Library System SHALL attempt to mark the Vinyl for removal

### Requirement 10: GUI Auto-update

**User Story:** AS 图书馆用户，我想要 GUI 自动更新以反映其他用户的操作，以便我总是看到最新的 Vinyl 状态。

#### Acceptance Criteria

1. WHEN a Vinyl state changes in the Model, the Vinyl Library System SHALL notify all registered observers
2. WHEN an observer receives a notification of a Vinyl state change, the Vinyl Library System SHALL update the GUI display
3. The Vinyl Library System SHALL use the Observer Pattern to implement state change notifications
4. WHEN multiple users interact with the system simultaneously, the Vinyl Library System SHALL update the GUI in real-time

### Requirement 11: MVVM Architecture

**User Story:** AS 系统架构师，我想要使用 MVVM 设计模式，以便分离业务逻辑和用户界面。

#### Acceptance Criteria

1. The Vinyl Library System SHALL separate the application into Model, View, and ViewModel layers
2. The Model layer SHALL contain the Vinyl data structures and business logic
3. The View layer SHALL contain the JavaFX FXML files and UI components
4. The ViewModel layer SHALL contain the presentation logic and data binding
5. The ViewModel SHALL expose data from the Model to the View
6. The ViewModel SHALL handle user interactions from the View and call Model methods

### Requirement 12: Multi-User Simulation

**User Story:** AS 测试人员，我想要模拟多个用户的并发操作，以便验证系统的线程安全性和状态一致性。

#### Acceptance Criteria

1. The Vinyl Library System SHALL provide a Runnable class to simulate other users
2. WHEN the Runnable simulation is running, the Vinyl Library System SHALL perform random actions at random intervals
3. The random actions SHALL include reserving, borrowing, returning, and removing Vinyls
4. WHILE the simulation is running, the Vinyl Library System SHALL continue to accept user input in the GUI
5. The Vinyl Library System SHALL use Threads to implement concurrent user operations
6. WHEN multiple threads modify the same Vinyl, the Vinyl Library System SHALL maintain data consistency

### Requirement 13: Add Vinyl Window

**User Story:** AS 图书馆管理员，我想要通过图形界面添加新的 Vinyl，以便扩展图书馆馆藏。

#### Acceptance Criteria

1. WHEN a user requests to add a new Vinyl, the Vinyl Library System SHALL display a form window
2. WHEN the form is submitted with valid data, the Vinyl Library System SHALL create a new Vinyl in the available state
3. The Vinyl Library System SHALL require a title for the new Vinyl
4. The Vinyl Library System SHALL require an artist for the new Vinyl
5. The Vinyl Library System SHALL require a release year for the new Vinyl
6. WHEN a new Vinyl is added, the Vinyl Library System SHALL update the GUI list to include the new Vinyl

### Requirement 14: Session Management

**User Story:** AS 系统开发者，我想要使用共享的 Session 对象管理跨视图的信息传递，以便避免 ViewModel 之间的直接依赖。

#### Acceptance Criteria

1. The Vinyl Library System SHALL provide a Session object to store shared application state
2. WHEN a Vinyl is selected in one view, the Vinyl Library System SHALL store the selection in the Session object
3. WHEN another view accesses the Session object, the Vinyl Library System SHALL provide the current selection
4. WHEN the current user performs actions, the Vinyl Library System SHALL retrieve the user ID from the Session object
5. The ViewModel SHALL access the Session object to obtain cross-view information

### Requirement 15: Observable Collections

**User Story:** AS 系统开发者，我想要使用 JavaFX 的可观察集合，以便在数据变化时自动更新 GUI。

#### Acceptance Criteria

1. The ViewModel SHALL use ObservableList to store Vinyl data
2. WHEN a Vinyl is added to the ObservableList, the Vinyl Library System SHALL automatically update the GUI
3. WHEN a Vinyl is removed from the ObservableList, the Vinyl Library System SHALL automatically update the GUI
4. WHEN a Vinyl property in the ObservableList changes, the Vinyl Library System SHALL automatically update the GUI
