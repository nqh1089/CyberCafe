package Socket;

import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer implements Runnable {

    private ServerSocket serverSocket;
    public static Map<String, ClientHandler> clients = new HashMap<>(); // key = NameAccount
    public static String lastClientSentMessage = null; // Ghi nhớ NameAccount cuối cùng gửi tin
    public static ChatServer instance = null; // kiểm tra đã chạy chưa
    private final int port;

    public ChatServer(int port) {
        this.port = port;

        // Nếu đã chạy rồi thì không khởi tạo lại nữa
        if (instance != null) {
            System.out.println("ChatServer đã chạy trước đó.");
            return;
        }

        instance = this; // Gán instance để nhận biết đã chạy
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("ChatServer đang lắng nghe tại cổng " + port);

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    ClientHandler handler = new ClientHandler(socket);
                    new Thread(handler).start();
                } catch (IOException e) {
                    System.out.println("Lỗi khi chấp nhận client: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("Lỗi ChatServer: " + e.getMessage());
        }
    }

    // Gọi khi client kết nối xong và gửi tên tài khoản, tên hiển thị
    public static void registerClient(String nameAccount, String tenHienThi, ClientHandler handler) {
        clients.put(nameAccount, handler);
        handler.setTenHienThi(tenHienThi);
        System.out.println("Đã kết nối: " + tenHienThi + " (" + nameAccount + ")");
    }

    // Gửi tin nhắn từ Admin đến client
    public static void guiChoClient(String nameAccount, String noiDung) {
        ClientHandler handler = clients.get(nameAccount);
        if (handler != null) {
            String time = getTime();

            // Tạo chuỗi tin nhắn gửi cho client
            String goiTin = "CHAT|Admin|" + noiDung + "|" + time;
            handler.sendMessage(goiTin);

            // Lưu vào cơ sở dữ liệu
            handler.saveMessageToDB(getAdminID(), getClientID(nameAccount), noiDung);
        } else {
            System.out.println("Không tìm thấy client: " + nameAccount);
        }
    }

    // Lấy thời gian hiện tại định dạng hh:mm
    private static String getTime() {
        return java.time.LocalTime.now().withNano(0).toString().substring(0, 5);
    }

    // Lấy ID người dùng từ NameAccount
    private static int getClientID(String nameAccount) {
        return ClientHandler.getIDFromName(nameAccount);
    }

    // Lấy ID của tài khoản admin
    private static int getAdminID() {
        return ClientHandler.getAdminID();
    }
}
