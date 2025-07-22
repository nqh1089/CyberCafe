package Socket;

import Controller.DBConnection;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NC_ClientHandler implements Runnable {

    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private NC_ChatServer server;

    private int computerId = -1;
    private String computerName = "Unknown";
    private int currentLoggedInAccountId = 0;

    public NC_ClientHandler(Socket clientSocket, NC_ChatServer server) {
        this.clientSocket = clientSocket;
        this.server = server;
        try {
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (Exception e) {
            disconnectClient("Lỗi IO khi khởi tạo streams.");
        }
    }

    @Override
    public void run() {
        try {
            NC_Message initial = (NC_Message) in.readObject();
            if (initial.getType() == NC_Message.NC_MessageType.CLIENT_CONNECT) {
                this.computerId = initial.getComputerId();
                this.computerName = initial.getComputerName();
                server.registerConnectedComputer(computerId, computerName, this);
            } else {
                disconnectClient("Tin nhắn đầu tiên không hợp lệ.");
                return;
            }

            while (clientSocket.isConnected()) {
                NC_Message msg = (NC_Message) in.readObject();
                processIncomingMessage(msg);
            }

        } catch (Exception e) {
            disconnectClient("Client ngắt kết nối hoặc lỗi IO.");
        }
    }

    private void processIncomingMessage(NC_Message message) {
        switch (message.getType()) {
            case CHAT:
                server.processClientMessage(message);
                break;
            case CLIENT_LOGIN:
                this.currentLoggedInAccountId = message.getSenderId();
                server.registerLoggedInAccount(computerId, currentLoggedInAccountId, message.getAccountName());
                break;
            case CLIENT_LOGOUT:
                server.unregisterLoggedInAccount(computerId, currentLoggedInAccountId);
                this.currentLoggedInAccountId = 0;
                break;
            case REQUEST_HISTORY:
                handleRequestHistory(message);
                break;
            default:
                System.out.println("Nhận tin nhắn loại không xác định: " + message.getType());
        }
    }

    private void handleRequestHistory(NC_Message message) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM Message " +
                     "WHERE (SenderID = ? OR ReceiverID = ?) " +
                     "AND SentAt >= (SELECT ThoiGianBatDau FROM LogAccess WHERE IDLog = ?)"
             )) {

            ps.setInt(1, currentLoggedInAccountId);
            ps.setInt(2, currentLoggedInAccountId);
            ps.setInt(3, message.getLogAccessId());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NC_Message history = new NC_Message(
                        NC_Message.NC_MessageType.CHAT_HISTORY_ITEM,
                        rs.getInt("SenderID"),
                        rs.getInt("ReceiverID"),
                        server.LayTenTaiKhoanTuID(rs.getInt("SenderID")),
                        rs.getString("Content"),
                        rs.getTimestamp("SentAt")
                );
                sendMessage(history);
            }

        } catch (Exception e) {
            System.err.println("Lỗi xử lý REQUEST_HISTORY: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendMessage(NC_Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (Exception e) {
            disconnectClient("Lỗi gửi tin nhắn về Client");
        }
    }

    public void disconnectClient(String reason) {
        System.out.println("Đóng kết nối Client: " + reason);
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (Exception ignored) {}
    }

    /**
     * ✅ Getter để NC_ChatServer gọi lấy tên máy.
     */
    public String getComputerName() {
        return this.computerName;
    }
}
