package Socket;

import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer {

    private ServerSocket serverSocket;
    public static Map<String, ClientHandler> clients = new HashMap<>(); // key = NameAccount
    public static String lastClientSentMessage = null; // Ghi nhớ NameAccount cuối cùng gửi tin

    public ChatServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("ChatServer đang lắng nghe tại cổng " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket);
                new Thread(handler).start();
            }

        } catch (IOException e) {
            System.out.println("Lỗi ChatServer: " + e.getMessage());
        }
    }

    /**
     * Gọi khi client kết nối xong và gửi tên tài khoản, tên hiển thị
     */
    public static void registerClient(String nameAccount, String tenHienThi, ClientHandler handler) {
        clients.put(nameAccount, handler);
        handler.setTenHienThi(tenHienThi);
        System.out.println("Đã kết nối: " + tenHienThi + " (" + nameAccount + ")");
    }

    /**
     * Gửi tin nhắn từ Admin đến client có tên tài khoản (nameAccount)
     */
    public static void guiChoClient(String nameAccount, String noiDung) {
        ClientHandler handler = clients.get(nameAccount);
        if (handler != null) {
            String time = getTime();

            // Gửi đến client, tên người gửi là "Admin"
            String goiTin = "CHAT|Admin|" + noiDung + "|" + time;
            handler.sendMessage(goiTin);

            // Lưu DB (admin → nameAccount)
            handler.saveMessageToDB(getAdminID(), getClientID(nameAccount), noiDung);

        } else {
            System.out.println("Không tìm thấy client: " + nameAccount);
        }
    }

    private static String getTime() {
        return java.time.LocalTime.now().withNano(0).toString().substring(0, 5);
    }

    // Lấy ID người dùng từ NameAccount
    private static int getClientID(String nameAccount) {
        return ClientHandler.getIDFromName(nameAccount);
    }

    private static int getAdminID() {
        return ClientHandler.getAdminID();
    }
}
