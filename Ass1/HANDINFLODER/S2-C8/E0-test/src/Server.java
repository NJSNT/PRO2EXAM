import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
  public static void main(String[] args) {
    // 打印提示信息，表示服务端准备启动
    System.out.println("Starting server...");

    try {
      // 1. 初始化：创建服务端套接字并绑定到 2910 端口，监听该端口的连接请求
      ServerSocket welcomeSocket = new ServerSocket(2910);

      // 2. 核心循环：无限循环保证服务器一直运行，处理完一个请求后继续等待下一个
      while (true) {

        // 3. 监听连接：程序在此处阻塞（暂停），直到有客户端成功连接
        // 连接成功后，返回一个新的 socket 对象，专门用于与该客户端进行数据传输
        Socket socket = welcomeSocket.accept();
        System.out.println("Client connected");

        // 4. 接收准备：获取输入流并包装为 ObjectInputStream，以便直接读取对象
        ObjectInputStream inFromClient = new ObjectInputStream(socket.getInputStream());

        // 5. 读取数据：程序在此阻塞，直到客户端发送数据过来
        // 读取到数据后，将其强制类型转换为 String
        String o = (String)inFromClient.readObject();
        System.out.println("Received: " + o);

        // 6. 业务处理：将客户端发来的字符串转换为大写字母
        String answer = o.toUpperCase();

        // 7. 发送准备：获取输出流并包装为 ObjectOutputStream，用于向客户端返回对象
        ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());

        // 8. 返回结果：将转换为大写的字符串发送给客户端
        // 执行完毕后，循环回到开头的 accept()，等待下一个客户端连接
        outToClient.writeObject(answer);
      }

      // 9. 异常处理：捕获可能发生的网络通信错误（IOException）或收到未知对象类型（ClassNotFoundException）
    } catch (IOException | ClassNotFoundException e) {
      // socket stuff went wrong (在此处理连接中断或错误情况)
    }
  }

}
