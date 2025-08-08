package Socket;

import Controller.DBConnection;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class AccountStatusManager {

    // Map lưu mối liên hệ Socket <-> IDAccount
    private static final Map<Socket, Integer> clientAccounts = new ConcurrentHashMap<>();


    public static void addClient(Socket socket, int idAccount) {
        clientAccounts.put(socket, idAccount);
        System.out.println("[AccountStatusManager] Đã gắn IDAccount " + idAccount + " với socket " + socket);
    }

  
    public static Integer getIdAccount(Socket socket) {
        return clientAccounts.get(socket);
    }

 
    public static void removeClient(Socket socket) {
        clientAccounts.remove(socket);
        System.out.println("[AccountStatusManager] Đã xóa socket " + socket);
    }

  
    public static void setAccountOffline(int idAccount) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE Account SET OnlineStatus = 0 WHERE IDAccount = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idAccount);
                ps.executeUpdate();
                System.out.println("[AccountStatusManager] Đã set OnlineStatus=0 cho IDAccount: " + idAccount);
            }
        } catch (Exception e) {
            System.out.println("[AccountStatusManager] Lỗi khi set OnlineStatus=0: " + e.getMessage());
        }
    }


    public static void lockClient(Socket socket) {
        Integer idAccount = getIdAccount(socket);
        if (idAccount != null) {
            setAccountOffline(idAccount);
            // Có thể gửi lệnh LOCK xuống client nếu cần
            // sendMessage(socket, "LOCK");
        }
    }

    public static void handleDisconnect(Socket socket) {
        Integer idAccount = getIdAccount(socket);
        if (idAccount != null) {
            setAccountOffline(idAccount);
        }
        removeClient(socket);
    }
}
