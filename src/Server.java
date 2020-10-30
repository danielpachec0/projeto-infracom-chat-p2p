import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 3002;
        String client1Ip, client2Ip, clientPortS1, clientPortS2;
        int client1Port, client2Port;
        try{
            ServerSocket tmpSocket = new ServerSocket(port);
            //
            System.out.println("Waiting for client");
            Socket socket1 = tmpSocket.accept();
            client1Port = socket1.getPort();
            client1Ip = socket1.getInetAddress().getHostAddress();
            System.out.println("Connection received from " + client1Ip + " at port: " + client1Port);
            //
            System.out.println("Waiting for client");
            Socket socket2 = tmpSocket.accept();
            client2Port = socket2.getPort();
            client2Ip = socket2.getInetAddress().getHostAddress();
            System.out.println("Connection received from " + client2Ip + " at port: " + client2Port);
            //
            clientPortS1 = Integer.toString(client1Port);
            clientPortS2 = Integer.toString(client2Port);
            DataOutputStream saida = new DataOutputStream(socket1.getOutputStream());
            saida.write((clientPortS2 + " " + clientPortS1).getBytes());
            socket1.close();
            saida = new DataOutputStream(socket2.getOutputStream());
            saida.write((clientPortS1 + " " + clientPortS2).getBytes());
            socket2.close();
        }catch (BindException e){
            System.out.println("Address in use");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
