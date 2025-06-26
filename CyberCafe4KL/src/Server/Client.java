package Server;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;

public class Client {

    private static final String SERVER_IP = "26.228.105.146"; // Đổi thành IP máy chủ Radmin VPN (Đổi thành IP của máy chạy Sever)
    private static final int SERVER_PORT = 12345;

    private static final int COMPUTER_ID = 2;
    private static final String COMPUTER_NAME = "PC02";
    private static final String STATUS = "active";

    public static void main(String[] args) {
        while (true) {
            try (
                Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                System.out.println("ĐÃ KẾT NỐI TỚI SERVER " + SERVER_IP + ":" + SERVER_PORT);

                while (true) {
                    String data = String.format("ID:%d|NAME:%s|STATUS:%s|TIME:%s",
                            COMPUTER_ID,
                            COMPUTER_NAME,
                            STATUS,
                            LocalDateTime.now()
                    );
                    out.println(data);
                    System.out.println("ĐÃ GỬI: " + data);

                    try {
                        Thread.sleep(5000); // Gửi mỗi 5 giây
                    } catch (InterruptedException ie) {
                        System.err.println("THREAD BỊ GIÁN ĐOẠN");
                    }
                }

            } catch (IOException e) {
                System.err.println("MẤT KẾT NỐI: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                e.printStackTrace();
                System.err.println("THỬ LẠI SAU 5 GIÂY...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}