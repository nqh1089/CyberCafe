package Socket; // Đổi package thành Socket

import Controller.DBConnection; // Vẫn import từ Controller.DBConnection
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;

public class CM_CN_ChatManager {

    public static final int ADMIN_ACCOUNT_ID = 1; 

    private static Consumer<CM_CN_OnlineClientInfo> addClientToUiCallback;
    private static Consumer<Integer> removeClientFromUiCallback;
    private static Consumer<String[]> newMessageToUiCallback;

    public static void initializeChatSystem(Consumer<CM_CN_OnlineClientInfo> addClientCb, 
                                           Consumer<Integer> removeClientCb, 
                                           Consumer<String[]> newMessageCb) {
        addClientToUiCallback = addClientCb;
        removeClientFromUiCallback = removeClientCb;
        newMessageToUiCallback = newMessageCb;

        CM_CN_ChatServer.startServer(addClientToUiCallback, removeClientFromUiCallback, newMessageToUiCallback);
    }

    public static void shutdownChatSystem() {
        CM_CN_ChatServer.stopServer();
    }

    public static void saveChatMessage(int senderId, int receiverId, String content) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null) {
                String sql = "INSERT INTO Message (SenderID, ReceiverID, Content, SentAt, IsRead) VALUES (?, ?, ?, GETDATE(), 0)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, senderId);
                ps.setInt(2, receiverId);
                ps.setString(3, content);
                ps.executeUpdate();
                System.out.println("Đã lưu tin nhắn vào DB: Sender=" + senderId + ", Receiver=" + receiverId + ", Content='" + content + "'");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lưu tin nhắn vào DB: " + e.getMessage());
        }
    }

    public static void sendAdminMessageToClient(int targetComputerID, String messageContent) {
        CM_CN_ChatServer.sendAdminMessageToClient(targetComputerID, messageContent);
    }
}