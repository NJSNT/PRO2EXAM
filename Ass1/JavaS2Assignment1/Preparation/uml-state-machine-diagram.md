# UML State Machine Diagram (Vinyl States)

这个图专门用来描述一个 Vinyl（唱片）在它整个生命周期中，是如何因为用户的不同操作（Reserve, Borrow, Return, Mark for Removal）而在不同状态（State）之间来回切换的。

你可以把下面的代码复制到 [Mermaid Live Editor](https://mermaid.live) 中查看渲染出的状态机图，然后参考这个图在 Astah 中画出来交作业。

```mermaid
stateDiagram-v2
    [*] --> Available : Create pre-filled Vinyl

    state Available {
        %% 空闲状态
    }
    
    state Reserved {
        %% 被预约状态
    }
    
    state Borrowed {
        %% 被借出状态
    }
    
    state BorrowedAndReserved {
        %% 既被借出又被预约状态
    }

    %% Available 状态的流转
    Available --> Reserved : reserve(userId)
    Available --> Borrowed : borrow(userId)

    %% Reserved 状态的流转
    Reserved --> Borrowed : borrow(userId) [By reserver]

    %% Borrowed 状态的流转
    Borrowed --> BorrowedAndReserved : reserve(userId) [Not reserved]
    Borrowed --> Available : returnVinyl() [Not reserved]
    Borrowed --> Reserved : returnVinyl() [Was reserved]

    %% BorrowedAndReserved 状态的流转
    BorrowedAndReserved --> Reserved : returnVinyl()

    %% Mark for removal (软删除逻辑)
    %% 只有在 Available 且没有被预约时才能彻底被移除
    Available --> [*] : markVinylForRemoval() / returnVinyl() [Marked & Not reserved]
```
