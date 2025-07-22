package Socket;

import Controller.DBConnection;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class NC_ChatServer implements Runnable {

    private static final int PORT = 1902;
    private ServerSocket serverSocket;
    private boolean isRunning;

    private final List<NC_ClientHandler> clientHandlers = new CopyOnWriteArrayList<>();
    private final Map<Integer, NC_ClientHandler> connectedComputers = Collections.synchronizedMap(new HashMap<>());
    private final Map<Integer, Integer> loggedInAccountsOnComputers = Collections.synchronizedMap(new HashMap<>());
    private final Map<Integer, String> accountNames = Collections.synchronizedMap(new HashMap<>());

    private int adminAccountId;

    private Consumer<NC_Message> messageProcessor;
    private Consumer<String> statusListener;
    private ClientStatusUpdater clientStatusUpdater;

    public interface ClientStatusUpdater {

        void update(int computerId, String computerName, Integer accountId, String accountName, boolean isOnline);
    }

    public NC_ChatServer() {
        loadAccountNamesFromDB();
    }

    public void setAdminAccountId(int adminAccountId) {
        this.adminAccountId = adminAccountId;
    }

    public void setMessageProcessor(Consumer<NC_Message> messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    public void setStatusListener(Consumer<String> statusListener) {
        this.statusListener = statusListener;
    }

    public void setClientStatusUpdater(ClientStatusUpdater clientStatusUpdater) {
        this.clientStatusUpdater = clientStatusUpdater;
    }

    public String LayNCTenTaiKhoanTuID(int accountId) {
        return accountNames.get(accountId);
    }

    public boolean IsNCComputerOnline(int computerId) {
        return connectedComputers.containsKey(computerId);
    }

    public boolean IsNCAccountLoggedInOnComputer(int computerId) {
        return loggedInAccountsOnComputers.containsKey(computerId) && loggedInAccountsOnComputers.get(computerId) != 0;
    }

    public Integer GetNCIdAccountDangNhapTuIdComputer(int computerId) {
        return loggedInAccountsOnComputers.getOrDefault(computerId, 0);
    }

    public int GetNCComputerIdFromClientAccountId(int accountId) {
        for (Map.Entry<Integer, Integer> entry : loggedInAccountsOnComputers.entrySet()) {
            if (entry.getValue().equals(accountId)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    private void loadAccountNamesFromDB() {
        String sql = "SELECT IDAccount, NameAccount FROM Account";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                accountNames.put(rs.getInt("IDAccount"), rs.getString("NameAccount"));
            }
            if (statusListener != null) {
                statusListener.accept("Đã tải " + accountNames.size() + " tài khoản từ DB.");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tải tên tài khoản: " + e.getMessage());
            if (statusListener != null) {
                statusListener.accept("Lỗi tải tên tài khoản: " + e.getMessage());
            }
        }
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            isRunning = true;
            System.out.println("Server đang chạy tại cổng " + PORT);
            if (statusListener != null) {
                statusListener.accept("Server đang chạy tại cổng " + PORT);
            }

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client mới kết nối: " + clientSocket.getInetAddress());
                if (statusListener != null) {
                    statusListener.accept("Client mới kết nối từ " + clientSocket.getInetAddress());
                }

                NC_ClientHandler handler = new NC_ClientHandler(clientSocket, this);
                new Thread(handler).start();
            }

        } catch (IOException e) {
            if (isRunning) {
                System.err.println("Lỗi Server: " + e.getMessage());
                if (statusListener != null) {
                    statusListener.accept("Lỗi Server: " + e.getMessage());
                }
            }
        } finally {
            stopServer();
        }
    }

    @Override
    public void run() {
        startServer();
    }

    public void stopServer() {
        isRunning = false;
        try {
            for (NC_ClientHandler handler : clientHandlers) {
                handler.disconnectClient("Server đóng.");
            }
            clientHandlers.clear();
            connectedComputers.clear();
            loggedInAccountsOnComputers.clear();
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("Server đã dừng.");
            if (statusListener != null) {
                statusListener.accept("Server đã dừng.");
            }
        } catch (IOException e) {
            System.err.println("Lỗi dừng Server: " + e.getMessage());
            if (statusListener != null) {
                statusListener.accept("Lỗi dừng Server: " + e.getMessage());
            }
        }
    }

    public boolean isServerRunning() {
        return isRunning;
    }

    public void registerConnectedComputer(int computerId, String computerName, NC_ClientHandler handler) {
        connectedComputers.put(computerId, handler);
        clientHandlers.add(handler);
        System.out.println("Máy " + computerName + " (ID: " + computerId + ") đã kết nối.");
        if (clientStatusUpdater != null) {
            clientStatusUpdater.update(computerId, computerName, 0, null, true);
        }
    }

    public void unregisterConnectedComputer(int computerId, String computerName) {
        NC_ClientHandler handler = connectedComputers.remove(computerId);
        if (handler != null) {
            clientHandlers.remove(handler);
        }
        loggedInAccountsOnComputers.remove(computerId);
        System.out.println("Máy " + computerName + " (ID: " + computerId + ") đã ngắt kết nối.");
        if (clientStatusUpdater != null) {
            clientStatusUpdater.update(computerId, computerName, 0, null, false);
        }
    }

    public void registerLoggedInAccount(int computerId, int accountId, String accountName) {
        loggedInAccountsOnComputers.put(computerId, accountId);
        accountNames.put(accountId, accountName);
        System.out.println("Tài khoản " + accountName + " (ID: " + accountId + ") đăng nhập trên máy " + computerId);
        if (clientStatusUpdater != null) {
            String computerName = getComputerNameById(computerId);
            clientStatusUpdater.update(computerId, computerName, accountId, accountName, true);
        }
    }

    public void unregisterLoggedInAccount(int computerId, int accountId) {
        loggedInAccountsOnComputers.remove(computerId);
        System.out.println("Tài khoản ID: " + accountId + " đăng xuất khỏi máy " + computerId);
        if (clientStatusUpdater != null) {
            String computerName = getComputerNameById(computerId);
            clientStatusUpdater.update(computerId, computerName, 0, null, true);
        }
    }

    private String getComputerNameById(int computerId) {
        NC_ClientHandler handler = connectedComputers.get(computerId);
        return (handler != null) ? handler.getComputerName() : "Máy " + computerId;
    }

    public void guiTinNhanDenClient(int targetComputerId, NC_Message message) {
        try {
            LuuNCTinNhanVaoDB(message);
            NC_ClientHandler handler = connectedComputers.get(targetComputerId);
            if (handler != null && loggedInAccountsOnComputers.containsKey(targetComputerId)
                    && loggedInAccountsOnComputers.get(targetComputerId).equals(message.getReceiverId())) {
                handler.sendNCMessage(message);
                System.out.println("Đã gửi tin nhắn từ Admin đến máy " + targetComputerId);
            } else {
                System.out.println("Không thể gửi: Máy không online hoặc tài khoản không đăng nhập.");
                if (messageProcessor != null) {
                    messageProcessor.accept(new NC_Message(
                            NC_Message.NC_MessageType.CHAT,
                            adminAccountId,
                            message.getReceiverId(),
                            "Hệ thống",
                            "Không thể gửi tin nhắn: Máy không online hoặc tài khoản không đăng nhập.",
                            new Date()
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lưu hoặc gửi tin: " + e.getMessage());
        }
    }

    public void processClientMessage(NC_Message message) {
        if (message == null) {
            System.err.println("Nhận tin rỗng.");
            return;
        }
        try {
            // ✅ 1. Lưu DB
            LuuNCTinNhanVaoDB(message);

            // ✅ 2. Gửi sang Admin UI
            if (message.getReceiverId() == adminAccountId && messageProcessor != null) {
                messageProcessor.accept(message);
                System.out.println("Đã chuyển tiếp tin nhắn Client → Admin.");
            } else {
                System.out.println("Tin không gửi cho Admin hoặc messageProcessor chưa set.");
            }

            // ✅ 3. Gửi phản hồi về chính Client (máy gửi)
            int computerId = GetNCComputerIdFromClientAccountId(message.getSenderId());
            NC_ClientHandler handler = connectedComputers.get(computerId);
            if (handler != null) {
                handler.sendNCMessage(message);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi lưu tin nhắn Client: " + e.getMessage());
        }
    }

    private void LuuNCTinNhanVaoDB(NC_Message message) throws SQLException {
        String sql = "INSERT INTO Message (SenderID, ReceiverID, Content, SentAt) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, message.getSenderId());
            ps.setInt(2, message.getReceiverId());
            ps.setNString(3, message.getContent());
            ps.setTimestamp(4, new Timestamp(message.getSentAt().getTime()));
            ps.executeUpdate();
            System.out.println("Đã lưu tin: " + message.getContent());
        }
    }
}
