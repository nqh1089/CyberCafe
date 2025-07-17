package Socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try {
            System.out.println("Đang đợi client kết nối...");
            ServerSocket serverSocket = new ServerSocket(3333);
            Socket socket = serverSocket.accept();
            System.out.println("Client đã kết nối!");

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            Scanner sc = new Scanner(System.in);

            // Thread nhận dữ liệu từ client
            Thread receiveThread = new Thread(() -> {
                try {
                    while (true) {
                        String msg = dis.readUTF();
                        if (msg.equals("!end")) {
                            System.out.println("Client đã thoát.");
                            socket.close();
                            break;
                        }
                        System.out.println("Client: " + msg);
                    }
                } catch (IOException e) {
                    System.out.println("Kết nối bị đóng.");
                }
            });
            receiveThread.start();

            // Gửi dữ liệu từ server tới client
            while (true) {
                String reply = sc.nextLine();
                dos.writeUTF(reply);
                dos.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
