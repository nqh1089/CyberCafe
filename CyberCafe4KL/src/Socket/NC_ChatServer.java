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

    // Danh sách các client handler đang kết nối
    private final List<NC_ClientHandler> clientHandlers = new CopyOnWriteArrayList<>();

    // Map để lưu trữ IDComputer và NC_ClientHandler tương ứng
    private final Map<Integer, NC_ClientHandler> connectedComputers = Collections.synchronizedMap(new HashMap<>());
    // Map để lưu trữ IDComputer và IDAccount đang đăng nhập trên máy đó (nếu có)
    private final Map<Integer, Integer> loggedInAccountsOnComputers = Collections.synchronizedMap(new HashMap<>());
    // Map để lưu trữ IDAccount và Tên tài khoản (được tải từ DB)
    private final Map<Integer, String> accountNames = Collections.synchronizedMap(new HashMap<>());

    // BỔ SUNG: Map để lưu trữ trạng thái có tin nhắn mới cho Admin từ máy tính cụ thể
    private final Map<Integer, Boolean> hasNewMessages = Collections.synchronizedMap(new HashMap<>());

    // Admin Account ID, được set từ CN_ChatAdmin
    private int adminAccountId;

    // Callbacks để giao tiếp với CN_ChatAdmin (Admin UI)
    private Consumer<NC_Message> messageProcessor; // Xử lý tin nhắn đến từ client (hiển thị lên Admin UI)
    private Consumer<String> statusListener; // Nghe trạng thái server (hiển thị log server)
    private ClientStatusUpdater clientStatusUpdater; // Cập nhật trạng thái client cho Admin UI

    // Interface cho callback cập nhật trạng thái client
    public interface ClientStatusUpdater {
        // Đã thêm tham số hasNewMessage
        void update(int computerId, String computerName, Integer accountId, String accountName, boolean isOnline, boolean hasNewMessage);
    }

    public NC_ChatServer() {
        // Constructor, nạp tên tài khoản từ DB khi khởi tạo server
        loadAccountNamesFromDB();
    }

    // Setter cho adminAccountId
    public void setAdminAccountId(int adminAccountId) {
        this.adminAccountId = adminAccountId;
    }

    // Setters cho các callbacks từ CN_ChatAdmin
    public void setMessageProcessor(Consumer<NC_Message> messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    public void setStatusListener(Consumer<String> statusListener) {
        this.statusListener = statusListener;
    }

    public void setClientStatusUpdater(ClientStatusUpdater clientStatusUpdater) {
        this.clientStatusUpdater = clientStatusUpdater;
    }

    // Phương thức để CN_ChatAdmin lấy tên tài khoản từ ID
    public String LayNCTenTaiKhoanTuID(int accountId) {
        return accountNames.get(accountId);
    }

    // Phương thức để CN_ChatAdmin kiểm tra trạng thái online của máy
    public boolean IsNCComputerOnline(int computerId) {
        return connectedComputers.containsKey(computerId);
    }

    // Phương thức để CN_ChatAdmin kiểm tra trạng thái đăng nhập của tài khoản trên máy
    public boolean IsNCAccountLoggedInOnComputer(int computerId) {
        return loggedInAccountsOnComputers.containsKey(computerId) && loggedInAccountsOnComputers.get(computerId) != 0;
    }

    // PHẦN BỔ SUNG: Lấy ID Account đang đăng nhập trên một máy tính cụ thể
    public Integer getLoggedInAccountIdOnComputer(int computerId) {
        return loggedInAccountsOnComputers.getOrDefault(computerId, 0);
    }

    // Phương thức để CN_ChatAdmin lấy ID Account đang đăng nhập từ IDComputer
    public Integer GetNCIdAccountDangNhapTuIdComputer(int computerId) {
        return loggedInAccountsOnComputers.getOrDefault(computerId, 0); // Trả về 0 nếu không có
    }

    // Phương thức để CN_ChatAdmin lấy ID Computer từ ID Account (nếu tài khoản đó đang đăng nhập)
    public int GetNCComputerIdFromClientAccountId(int accountId) {
        for (Map.Entry<Integer, Integer> entry : loggedInAccountsOnComputers.entrySet()) {
            if (entry.getValue().equals(accountId)) {
                return entry.getKey();
            }
        }
        return -1; // Không tìm thấy
    }

    // Tải tất cả tên tài khoản từ DB vào Map
    private void loadAccountNamesFromDB() {
        String sql = "SELECT IDAccount, NameAccount FROM Account";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                accountNames.put(rs.getInt("IDAccount"), rs.getString("NameAccount"));
            }
            if (statusListener != null) {
                statusListener.accept("Đã tải " + accountNames.size() + " tên tài khoản từ DB.");
            }
        } catch (SQLException e) {
            System.err.println("NC_ChatServer: Lỗi khi tải tên tài khoản từ DB: " + e.getMessage());
            e.printStackTrace();
            if (statusListener != null) {
                statusListener.accept("Lỗi tải tên tài khoản: " + e.getMessage());
            }
        }
    }

    // Bắt đầu lắng nghe kết nối từ client
    public void startServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            isRunning = true;
            System.out.println("NC_ChatServer: Server đang lắng nghe trên cổng " + PORT);
            if (statusListener != null) {
                statusListener.accept("Server đang lắng nghe trên cổng " + PORT);
            }

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("NC_ChatServer: Client mới kết nối từ " + clientSocket.getInetAddress());
                if (statusListener != null) {
                    statusListener.accept("Client mới kết nối từ " + clientSocket.getInetAddress());
                }

                NC_ClientHandler handler = new NC_ClientHandler(clientSocket, this);
                // clientHandlers.add(handler); // Sẽ được thêm vào khi handler đăng ký computerId
                new Thread(handler).start();
            }
        } catch (IOException e) {
            if (isRunning) { // Chỉ in lỗi nếu server đang cố gắng chạy
                System.err.println("NC_ChatServer: Lỗi khởi động hoặc chấp nhận kết nối: " + e.getMessage());
                e.printStackTrace();
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

    // Dừng server một cách an toàn
    public void stopServer() {
        isRunning = false;
        try {
            // Ngắt kết nối tất cả client đang hoạt động
            for (NC_ClientHandler handler : clientHandlers) {
                handler.disconnectClient("Server đang đóng."); // Sẽ tự unregister
            }
            clientHandlers.clear();
            connectedComputers.clear();
            loggedInAccountsOnComputers.clear();
            hasNewMessages.clear(); // BỔ SUNG: Xóa tất cả trạng thái tin nhắn mới khi đóng server
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("NC_ChatServer: Server đã đóng.");
            if (statusListener != null) {
                statusListener.accept("Server đã đóng.");
            }
        } catch (IOException e) {
            System.err.println("NC_ChatServer: Lỗi khi đóng server: " + e.getMessage());
            e.printStackTrace();
            if (statusListener != null) {
                statusListener.accept("Lỗi đóng Server: " + e.getMessage());
            }
        }
    }

    public boolean isServerRunning() {
        return isRunning;
    }

    // Phương thức được gọi bởi NC_ClientHandler khi một máy kết nối và gửi IDComputer
    public void registerConnectedComputer(int computerId, String computerName, NC_ClientHandler handler) {
        connectedComputers.put(computerId, handler);
        clientHandlers.add(handler); // Thêm handler vào danh sách
        hasNewMessages.put(computerId, false); // BỔ SUNG: Mặc định không có tin nhắn mới khi kết nối
        System.out.println("NC_ChatServer: Máy " + computerName + " (ID: " + computerId + ") đã đăng ký kết nối.");
        if (clientStatusUpdater != null) {
            // Cập nhật trạng thái UI Admin: Máy online, chưa có tài khoản đăng nhập (accountId = 0)
            clientStatusUpdater.update(computerId, computerName, 0, null, true, false); // BỔ SUNG: Thêm false cho hasNewMessage
        }
    }

    // Phương thức được gọi bởi NC_ClientHandler khi một máy ngắt kết nối
    public void unregisterConnectedComputer(int computerId, String computerName) {
        NC_ClientHandler handler = connectedComputers.remove(computerId);
        if (handler != null) {
            clientHandlers.remove(handler); // Xóa handler khỏi danh sách
        }
        loggedInAccountsOnComputers.remove(computerId); // Đảm bảo xóa cả trạng thái đăng nhập
        hasNewMessages.remove(computerId); // BỔ SUNG: Xóa trạng thái tin nhắn mới khi máy ngắt kết nối
        System.out.println("NC_ChatServer: Máy " + computerName + " (ID: " + computerId + ") đã ngắt kết nối.");
        if (clientStatusUpdater != null) {
            clientStatusUpdater.update(computerId, computerName, 0, null, false, false); // BỔ SUNG: Trạng thái offline, không có tin nhắn mới
        }
    }

    // Phương thức được gọi bởi NC_ClientHandler khi một tài khoản đăng nhập vào máy
    public void registerLoggedInAccount(int computerId, int accountId, String accountName) {
        loggedInAccountsOnComputers.put(computerId, accountId);
        accountNames.put(accountId, accountName); // Cập nhật/thêm tên tài khoản vào map
        System.out.println("NC_ChatServer: Tài khoản " + accountName + " (ID: " + accountId + ") đăng nhập trên máy " + computerId + ".");
        if (clientStatusUpdater != null) {
            String computerName = getComputerNameById(computerId); // Lấy tên máy từ map của server
            // BỔ SUNG: Lấy trạng thái tin nhắn mới hiện tại của máy
            boolean currentHasNewMessage = hasNewMessages.getOrDefault(computerId, false);
            clientStatusUpdater.update(computerId, computerName, accountId, accountName, true, currentHasNewMessage); // BỔ SUNG: Truyền trạng thái tin nhắn mới
        }
    }

    // Phương thức được gọi bởi NC_ClientHandler khi một tài khoản đăng xuất khỏi máy
    public void unregisterLoggedInAccount(int computerId, int intaccountId) {
        loggedInAccountsOnComputers.remove(computerId);
        System.out.println("NC_ChatServer: Tài khoản ID: " + intaccountId + " đăng xuất khỏi máy " + computerId + ".");
        if (clientStatusUpdater != null) {
            String computerName = getComputerNameById(computerId); // Lấy tên máy từ map của server
            // BỔ SUNG: Lấy trạng thái tin nhắn mới hiện tại của máy
            boolean currentHasNewMessage = hasNewMessages.getOrDefault(computerId, false);
            clientStatusUpdater.update(computerId, computerName, 0, null, true, currentHasNewMessage); // BỔ SUNG: Vẫn online nhưng không có tài khoản, truyền trạng thái tin nhắn mới
        }
    }

    // Helper để lấy tên máy từ ID (trong nội bộ server)
    private String getComputerNameById(int computerId) {
        // Có thể lấy tên máy từ DB hoặc từ NC_ClientHandler đã đăng ký
        NC_ClientHandler handler = connectedComputers.get(computerId);
        return (handler != null) ? handler.getComputerName() : "Máy " + computerId;
    }


    /**
     * Gửi tin nhắn từ Server (Admin) đến một Client cụ thể.
     * Tin nhắn sẽ được lưu vào DB trước khi gửi.
     * @param targetComputerId ID của máy tính đích.
     * @param message Tin nhắn cần gửi.
     */
    public void guiTinNhanDenClient(int targetComputerId, NC_Message message) {
        try {
            // 1. Lưu tin nhắn vào DB
            LuuNCTinNhanVaoDB(message);
            // 2. Gửi tin nhắn đến client handler tương ứng
            NC_ClientHandler handler = connectedComputers.get(targetComputerId);
            
            // Kiểm tra xem máy có online và tài khoản đích có đang đăng nhập trên máy đó không
            // BỔ SUNG: Chỉ gửi nếu máy đang online VÀ có tài khoản đăng nhập
            if (handler != null && loggedInAccountsOnComputers.containsKey(targetComputerId) && loggedInAccountsOnComputers.get(targetComputerId) != 0) {
                // Kiểm tra thêm: message.getReceiverId() có khớp với tài khoản đang đăng nhập trên máy đó không
                // Nếu tin nhắn từ Admin đến một tài khoản cụ thể, thì ID người nhận phải khớp
                if (message.getReceiverId() == loggedInAccountsOnComputers.get(targetComputerId)) {
                    handler.sendNCMessage(message);
                    System.out.println("NC_ChatServer: Đã gửi tin nhắn từ Admin đến Client " + targetComputerId + " (Tài khoản: " + message.getReceiverId() + ").");
                } else {
                    System.out.println("NC_ChatServer: Tài khoản đích " + message.getReceiverId() + " không phải tài khoản đang đăng nhập trên máy " + targetComputerId + ". Tin nhắn không được gửi qua socket.");
                    if (messageProcessor != null) {
                        messageProcessor.accept(new NC_Message(NC_Message.NC_MessageType.CHAT, adminAccountId, message.getSenderId(), "Hệ thống", "Không thể gửi tin: Tài khoản đích không khớp với tài khoản đang đăng nhập trên máy.", new Date()));
                    }
                }
            } else {
                System.out.println("NC_ChatServer: Máy " + targetComputerId + " không online hoặc chưa có tài khoản đăng nhập. Tin nhắn không được gửi qua socket.");
                // Có thể xử lý thông báo lại cho Admin là không gửi được
                if (messageProcessor != null) {
                    // Gửi lại một tin nhắn CHAT dạng thông báo cho Admin (sender là Admin, receiver là Admin, content là thông báo lỗi)
                    // Hoặc gửi một loại tin nhắn mới nếu bạn có định nghĩa riêng cho thông báo hệ thống
                    messageProcessor.accept(new NC_Message(NC_Message.NC_MessageType.CHAT, adminAccountId, message.getSenderId(), "Hệ thống", "Không thể gửi tin: Máy " + getComputerNameById(targetComputerId) + " không online hoặc chưa có tài khoản đăng nhập.", new Date()));
                }
            }
        } catch (SQLException e) {
            System.err.println("NC_ChatServer: Lỗi khi lưu tin nhắn vào DB hoặc gửi tin nhắn: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Xử lý tin nhắn đến từ Client.
     * Tin nhắn sẽ được lưu vào DB và chuyển tiếp đến Admin UI.
     * @param message Tin nhắn từ client.
     */
    public void processClientMessage(NC_Message message) {
        if (message == null) {
            System.err.println("NC_ChatServer: Nhận tin nhắn rỗng từ client.");
            return;
        }

        try {
            // 1. Lưu tin nhắn vào DB
            LuuNCTinNhanVaoDB(message);

            // 2. Chuyển tiếp tin nhắn đến Admin UI (CN_ChatAdmin)
            if (message.getReceiverId() == adminAccountId && messageProcessor != null) {
                // Thêm thông tin ComputerId và ComputerName vào tin nhắn nếu cần cho Admin UI xử lý
                // NC_ChatAdmin sẽ dùng GetNCComputerIdFromClientAccountId để lấy ID máy từ senderId
                messageProcessor.accept(message);
                System.out.println("NC_ChatServer: Đã chuyển tiếp tin nhắn từ Client " + message.getSenderId() + " đến Admin.");

                // BỔ SUNG: Đánh dấu máy này có tin nhắn mới và cập nhật trạng thái cho Admin UI
                int computerIdOfSender = GetNCComputerIdFromClientAccountId(message.getSenderId());
                if (computerIdOfSender != -1) {
                    hasNewMessages.put(computerIdOfSender, true);
                    // Cập nhật trạng thái cho Admin UI ngay lập tức
                    if (clientStatusUpdater != null) {
                        String computerName = getComputerNameById(computerIdOfSender);
                        Integer accountId = loggedInAccountsOnComputers.getOrDefault(computerIdOfSender, 0);
                        String accountName = (accountId != 0) ? accountNames.get(accountId) : null;
                        boolean isOnline = connectedComputers.containsKey(computerIdOfSender); // Kiểm tra xem máy còn online không
                        clientStatusUpdater.update(computerIdOfSender, computerName, accountId, accountName, isOnline, true); // Đánh dấu là có tin nhắn mới
                    }
                }
            } else {
                System.out.println("NC_ChatServer: Tin nhắn không dành cho Admin (" + adminAccountId + ") hoặc messageProcessor chưa được đặt.");
            }

        } catch (SQLException e) {
            System.err.println("NC_ChatServer: Lỗi khi lưu tin nhắn từ client vào DB hoặc chuyển tiếp: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lưu tin nhắn vào cơ sở dữ liệu.
     * @param message Đối tượng NC_Message cần lưu.
     */
    private void LuuNCTinNhanVaoDB(NC_Message message) throws SQLException {
        String sql = "INSERT INTO Message (SenderID, ReceiverID, Content, SentAt) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, message.getSenderId());
            ps.setInt(2, message.getReceiverId());
            ps.setNString(3, message.getContent());
            ps.setTimestamp(4, new Timestamp(message.getSentAt().getTime()));
            ps.executeUpdate();
            System.out.println("NC_ChatServer: Đã lưu tin nhắn vào DB: Sender " + message.getSenderId() + ", Receiver " + message.getReceiverId() + ", Content: " + message.getContent());
        }
    }

    /**
     * BỔ SUNG:
     * Đặt lại trạng thái "có tin nhắn mới" cho một máy tính cụ thể.
     * Phương thức này sẽ được gọi từ Admin UI khi Admin đọc tin nhắn của máy đó.
     * @param computerId ID của máy tính cần xóa trạng thái tin nhắn mới.
     */
    public void clearNewMessageStatus(int computerId) {
        if (hasNewMessages.containsKey(computerId)) {
            hasNewMessages.put(computerId, false);
            System.out.println("NC_ChatServer: Đã xóa trạng thái tin nhắn mới cho máy " + computerId);
            // Cập nhật lại trạng thái cho Admin UI để nó hiển thị trạng thái bình thường
            if (clientStatusUpdater != null) {
                String computerName = getComputerNameById(computerId);
                Integer accountId = loggedInAccountsOnComputers.getOrDefault(computerId, 0);
                String accountName = (accountId != 0) ? accountNames.get(accountId) : null;
                boolean isOnline = connectedComputers.containsKey(computerId); // Kiểm tra xem máy còn online không
                clientStatusUpdater.update(computerId, computerName, accountId, accountName, isOnline, false); // Đặt hasNewMessage về false
            }
        }
    }
}