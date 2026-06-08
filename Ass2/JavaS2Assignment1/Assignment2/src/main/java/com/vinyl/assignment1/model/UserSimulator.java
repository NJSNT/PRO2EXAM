package com.vinyl.assignment1.model;

import com.vinyl.assignment1.network.NetworkClient;
import java.util.Random;

// 实现 Runnable 以支持多线程任务执行
public class UserSimulator implements Runnable {
    
    private final NetworkClient client;
    private final String userId; // 模拟用户标识
    
    // volatile 保证主线程和子线程间该标志位的可见性，用于安全控制线程生命周期
    private volatile boolean running = false;
    private final Random random = new Random();

    public UserSimulator(NetworkClient client, String userId) {
        this.client = client;
        this.userId = userId;
    }

    public void start() {
        running = true;
        new Thread(this).start(); // 启动新线程
    }
    
    public void stop() {
        running = false;
    }
    
    // 线程核心运行逻辑
    @Override
    public void run() {
        while (running) {
            try {
                // 模拟业务操作延迟: 随机休眠 1~3 秒
                Thread.sleep(random.nextInt(2000) + 1000);
                
                // 1. 库存检查
                if (client.getVinyls().isEmpty()) {
                    continue;
                }
                
                // 2. 随机抽取目标实体
                int index = random.nextInt(client.getVinyls().size());
                Vinyl targetVinyl = client.getVinyls().get(index);
                
                // 3. 随机选择业务操作 (0~3 对应不同行为)
                int actionType = random.nextInt(4);
                
                // 4. 执行业务逻辑分发
                switch (actionType) {
                    case 0:
                        client.reserveVinyl(targetVinyl, userId);
                        System.out.println("Simulator [" + userId + "] tried to RESERVE " + targetVinyl.getTitle());
                        break;
                    case 1:
                        client.borrowVinyl(targetVinyl, userId);
                        System.out.println("Simulator [" + userId + "] tried to BORROW " + targetVinyl.getTitle());
                        break;
                    case 2:
                        client.returnVinyl(targetVinyl, userId);
                        System.out.println("Simulator [" + userId + "] tried to RETURN " + targetVinyl.getTitle());
                        break;
                    case 3:
                        client.markVinylForRemoval(targetVinyl);
                        System.out.println("Simulator [" + userId + "] tried to MARK " + targetVinyl.getTitle() + " FOR REMOVAL");
                        break;
                }

            } catch (InterruptedException e) {
                // 处理线程中断异常，安全退出循环
                running = false;
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                // 捕获多线程并发读取导致的偶发异常以维持稳定性
                System.err.println("Simulator [" + userId + "] encountered an error: " + e.getMessage());
            }
        }
    }
}
