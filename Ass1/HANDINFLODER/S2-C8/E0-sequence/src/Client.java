import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client
{
  public static void main(String[] args)
  {
    Scanner scanner = new Scanner(System.in);
    try
    {
      Socket socket = new Socket("192.168.1.101", 2910);
      ObjectOutputStream outputStream = new ObjectOutputStream(
          socket.getOutputStream());
      outputStream.flush();
      ObjectInputStream objectInputStream = new ObjectInputStream(
          socket.getInputStream());

      String greeting = (String) objectInputStream.readObject();
      System.out.println("Server: " + greeting);

      while (true)
      {
        System.out.print(
            "Enter command ('upper case', 'lower case' or 'Stop'): ");
        String command = scanner.nextLine();

        outputStream.writeObject(command);
        if ("Stop".equalsIgnoreCase(command))
        {
          System.out.println("Ending");
          break;
        }
        if ("upper case".equalsIgnoreCase(command)
            || "lower case".equalsIgnoreCase(command))
        {
          // 3. 接收服务器询问 "Argument?"
          String askArgument = (String) objectInputStream.readObject();
          System.out.println("Server: " + askArgument);
          System.out.println("Input text to convert.");
          String text = scanner.nextLine();
          outputStream.writeObject(text);

          String receive = (String) objectInputStream.readObject();
          System.out.println("result:" + receive);
        }
        else
        {
          // 接收服务器针对错误指令的提示
          String errorResponse = (String) objectInputStream.readObject();
          System.out.println("Server: " + errorResponse);
        }
      }
      socket.close();
      scanner.close();

    }
    catch (UnknownHostException e)
    {
      throw new RuntimeException(e);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    catch (ClassNotFoundException e)
    {
      throw new RuntimeException(e);
    }
  }}