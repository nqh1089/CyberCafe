package Socket;

import Controller.DBConnection;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NC_ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private NC_ChatServer server; // Tham chiếu đến server
    private int computerId = -1; // ID của máy tính Client này
    private String computerName = "Unknown Computer"; // Tên của máy tính Client này
    private int currentLoggedInAccountId = 0; // ID của tài khoản đang đăng nhập trên máy này (0 nếu chưa đăng nhập)
    private String currentLoggedInAccountName = null; // Tên tài khoản đang đăng nhập

    public NC_ClientHandler(Socket clientSocket, NC_ChatServer server) {
        this.clientSocket = clientSocket;
        this.server = server;
        try {
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.err.println("NC_ClientHandler: Lỗi tạo luồng I/O: " + e.getMessage());
            e.printStackTrace();
            disconnectClient("Lỗi nội bộ server.");
        }
    }

    @Override
    public void run() {
        try {
            // Bước 1: Nhận thông tin kết nối ban đầu từ client (IDComputer, NameComputer)
            NC_Message initialMessage = (NC_Message) in.readObject();
            if (initialMessage.getType() == NC_Message.NC_MessageType.CLIENT_CONNECT) {
                this.computerId = initialMessage.getComputerId();
                this.computerName = initialMessage.getComputerName();
                server.registerConnectedComputer(computerId, computerName, this);
                System.out.println("NC_ClientHandler: Client " + computerName + " (ID: " + computerId + ") đã xác định.");
                // Gửi phản hồi rằng server đã sẵn sàng (tùy chọn)
                sendNCMessage(new NC_Message(NC_Message.NC_MessageType.ADMIN_READY, "Server đã kết nối."));
            } else {
                System.err.println("NC_ClientHandler: Tin nhắn đầu tiên không phải CLIENT_CONNECT. Ngắt kết nối.");
                disconnectClient("Tin nhắn khởi tạo không hợp lệ.");
                return;
            }

            // Vòng lặp nhận tin nhắn từ client
            while (clientSocket.isConnected()) {
                NC_Message message = (NC_Message) in.readObject();
                if (message != null) {
                    processIncomingMessage(message);
                }
            }
        } catch (IOException e) {
            System.out.println("NC_ClientHandler: Client " + computerName + " (ID: " + computerId + ") đã ngắt kết nối đột ngột: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("NC_ClientHandler: Lỗi ClassNotFoundException: " + e.getMessage());
        } finally {
            disconnectClient("Client đã ngắt kết nối.");
        }
    }

    private void processIncomingMessage(NC_Message message) {
        switch (message.getType()) {
            case CHAT:
                // Tin nhắn chat từ client gửi đến Admin
                System.out.println("NC_ClientHandler (" + computerName + "): Nhận tin nhắn CHAT từ Client " + message.getSenderId() + " đến Admin " + message.getReceiverId() + ": " + message.getContent());
                server.processClientMessage(message); // Chuyển tiếp đến server để xử lý và gửi đến Admin UI
                break;
            case CLIENT_LOGIN:
                // Client báo hiệu đã đăng nhập tài khoản
                this.currentLoggedInAccountId = message.getSenderId(); // ID tài khoản là senderId trong tin nhắn LOGIN
                this.currentLoggedInAccountName = message.getAccountName();
                System.out.println("NC_ClientHandler (" + computerName + "): Client đăng nhập: IDAccount=" + currentLoggedInAccountId + ", NameAccount=" + currentLoggedInAccountName);
                server.registerLoggedInAccount(computerId, currentLoggedInAccountId, currentLoggedInAccountName); // Báo server cập nhật trạng thái
                break;
            case CLIENT_LOGOUT:
                // Client báo hiệu đã đăng xuất tài khoản
                System.out.println("NC_ClientHandler (" + computerName + "): Client đăng xuất: IDAccount=" + currentLoggedInAccountId);
                server.unregisterLoggedInAccount(computerId, currentLoggedInAccountId); // Báo server cập nhật trạng thái
                this.currentLoggedInAccountId = 0; // Đặt lại về 0
                this.currentLoggedInAccountName = null;
                break;
            case REQUEST_HISTORY:
                // Client yêu cầu lịch sử chat (tùy chọn)
                // Bạn có thể implement logic lấy lịch sử từ DB và gửi lại cho client ở đây
                System.out.println("NC_ClientHandler (" + computerName + "): Client yêu cầu lịch sử chat (chưa implement).");
                break;
            default:
                System.out.println("NC_ClientHandler (" + computerName + "): Nhận loại tin nhắn không xác định: " + message.getType());
                break;
        }
    }

    public void sendNCMessage(NC_Message message) {
        try {
            if (out != null) {
                out.writeObject(message);
                out.flush();
            }
        } catch (IOException e) {
            System.err.println("NC_ClientHandler: Lỗi gửi tin nhắn đến Client " + computerName + " (ID: " + computerId + "): " + e.getMessage());
            // Nếu gửi thất bại, có thể client đã ngắt kết nối
            disconnectClient("Lỗi gửi tin nhắn.");
        }
    }

    public void disconnectClient(String reason) {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
            // Chỉ unregister nếu computerId đã được xác định
            if (computerId != -1) {
                server.unregisterConnectedComputer(computerId, computerName);
                if (currentLoggedInAccountId != 0) {
                    server.unregisterLoggedInAccount(computerId, currentLoggedInAccountId);
                }
            }
            System.out.println("NC_ClientHandler: Client " + computerName + " (ID: " + computerId + ") đã ngắt kết nối. Lý do: " + reason);
        } catch (IOException e) {
            System.err.println("NC_ClientHandler: Lỗi đóng kết nối cho Client " + computerName + " (ID: " + computerId + "): " + e.getMessage());
        }
    }

    public int getComputerId() {
        return computerId;
    }

    public String getComputerName() {
        return computerName;
    }

    public int getCurrentLoggedInAccountId() {
        return currentLoggedInAccountId;
    }
}