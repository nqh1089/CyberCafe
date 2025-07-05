package Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cilent {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 3333);
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            Scanner sc = new Scanner(System.in);

            // Thread nhận dữ liệu từ server
            Thread receiveThread = new Thread(() -> {
                try {
                    while (true) {
                        String response = dataInputStream.readUTF();
                        System.out.println("Admin: " + response);
                    }
                } catch (IOException e) {
                    System.out.println("🛑 Server đã ngắt kết nối.");
                }
            });
            receiveThread.start();

            // Thread gửi dữ liệu
            while (true) {
                String input = sc.nextLine();
                dataOutputStream.writeUTF(input);
                dataOutputStream.flush();
                if (input.equals("!end")) {
                    System.out.println("🛑 Admin đã ngắt kết nối.");
                    socket.close(); // Đảm bảo đóng socket để readerThread cũng kết thúc
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
