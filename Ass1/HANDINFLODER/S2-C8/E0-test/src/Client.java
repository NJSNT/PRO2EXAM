import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client
{
  public static void main(String[] args)
  {
    try{
      Socket socket= new Socket("192.168.1.100",2910);

      ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
      outToServer.writeObject("Hi");
      //outToServer.writeBoolean(true);


      ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
      String o = (String) inFromServer.readObject();

      System.out.println(o);
    }
    catch (UnknownHostException e)
    {
      throw new RuntimeException(e);
    }
    catch (ClassNotFoundException e)
    {
      throw new RuntimeException(e);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
}
