import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client{

    public static int uPort, targetPort;

    public static void send(String msg, int port){
        try {
            Socket socket = new Socket("localhost", port);
            DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
            saida.write(msg.getBytes());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Runnable receive = () -> {
        try {
            ServerSocket sSocket = new ServerSocket(uPort);
            while(true){
                Socket socket = sSocket.accept();
                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String rec = bufferedReader.readLine();
                if(rec.equals("END")){
                    System.out.println(targetPort + " terminated the connection.");
                    break;
                }
                System.out.println("message from sender at" + targetPort + ": "  + rec);
                socket.close();
            }
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };



    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String address = "localhost", msg;
        try {
            Socket socket = new Socket(address, 3002);
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            msg = bufferedReader.readLine();
            String[] addresses = msg.split(" ");
            targetPort = Integer.parseInt(addresses[0]);
            uPort = Integer.parseInt(addresses[1]);
            socket.close();
        } catch (ConnectException e) {
            System.out.println("Unable to connect server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("connected to " + targetPort + ".");
        System.out.println("To end the connection send \"END\"");
        new Thread(receive).start();
        while(true){
            msg = in.nextLine();
            send(msg, targetPort);
            if (msg.equals("END")){
                System.out.println("Connection ended.");
                System.exit(0);
            }
        }
    }
}