package Socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.function.Consumer;

public class NC_ChatClient {
    private static final String SERVER_IP = "26.150.90.74"; // Địa chỉ IP của Server (Admin)
    private static final int SERVER_PORT = 1902;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private int idComputer;
    private String nameComputer;
    private int currentLoggedInAccountId = 0; // ID của tài khoản đang đăng nhập (0 nếu chưa)
    private String currentLoggedInAccountName = null; // Tên tài khoản đang đăng nhập (null nếu chưa)

    private Consumer<NC_Message> messageReceiver; // Callback để nhận tin nhắn từ Admin
    private Consumer<String> statusUpdater; // Callback để cập nhật trạng thái kết nối

    public NC_ChatClient(int idComputer, String nameComputer) {
        this.idComputer = idComputer;
        this.nameComputer = nameComputer;
    }

    public void setMessageReceiver(Consumer<NC_Message> messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    public void setStatusUpdater(Consumer<String> statusUpdater) {
        this.statusUpdater = statusUpdater;
    }

    public void connect() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            updateStatus("Đã kết nối tới Server.");

            // Gửi tin nhắn CLIENT_CONNECT đầu tiên với ID và tên máy tính
            NC_Message initialMessage = new NC_Message(NC_Message.NC_MessageType.CLIENT_CONNECT, idComputer, nameComputer, 0, null);
            sendMessage(initialMessage);

            // Bắt đầu lắng nghe tin nhắn từ server trong một luồng riêng
            new Thread(this::listenForMessages).start();

        } catch (IOException e) {
            updateStatus("Lỗi kết nối Server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void listenForMessages() {
        try {
            while (socket.isConnected()) {
                NC_Message message = (NC_Message) in.readObject();
                if (message != null) {
                    // Xử lý tin nhắn đến từ server (thường là từ Admin)
                    if (messageReceiver != null) {
                        messageReceiver.accept(message);
                    }
                }
            }
        } catch (IOException e) {
            updateStatus("Mất kết nối với Server: " + e.getMessage());
            disconnect("Mất kết nối.");
        } catch (ClassNotFoundException e) {
            System.err.println("NC_ChatClient: Lỗi ClassNotFoundException khi nhận tin nhắn: " + e.getMessage());
        } finally {
            disconnect("Ngắt kết nối listener.");
        }
    }

    public void sendMessage(NC_Message message) {
        try {
            if (out != null && socket.isConnected()) {
                out.writeObject(message);
                out.flush();
            } else {
                updateStatus("Không thể gửi tin nhắn: Không kết nối với server.");
            }
        } catch (IOException e) {
            updateStatus("Lỗi gửi tin nhắn: " + e.getMessage());
            e.printStackTrace();
            disconnect("Lỗi gửi tin nhắn.");
        }
    }

    public void disconnect(String reason) {
        try {
            if (socket != null && !socket.isClosed()) {
                // Gửi thông báo ngắt kết nối cho server (nếu client tự đóng)
                if (currentLoggedInAccountId != 0) {
                    sendMessage(new NC_Message(NC_Message.NC_MessageType.CLIENT_LOGOUT, idComputer, nameComputer, currentLoggedInAccountId, currentLoggedInAccountName));
                    currentLoggedInAccountId = 0;
                    currentLoggedInAccountName = null;
                }
                sendMessage(new NC_Message(NC_Message.NC_MessageType.CLIENT_DISCONNECT, idComputer, nameComputer, 0, null));
                
                in.close();
                out.close();
                socket.close();
                updateStatus("Đã ngắt kết nối Server. Lý do: " + reason);
            }
        } catch (IOException e) {
            System.err.println("NC_ChatClient: Lỗi đóng kết nối: " + e.getMessage());
        }
    }

    // Cập nhật trạng thái đăng nhập của tài khoản
    public void loginAccount(int accountId, String accountName) {
        this.currentLoggedInAccountId = accountId;
        this.currentLoggedInAccountName = accountName;
        // Gửi thông báo CLIENT_LOGIN đến server
        sendMessage(new NC_Message(NC_Message.NC_MessageType.CLIENT_LOGIN, idComputer, nameComputer, accountId, accountName));
        updateStatus("Đã đăng nhập tài khoản: " + accountName);
    }

    // Cập nhật trạng thái đăng xuất của tài khoản
    public void logoutAccount() {
        if (currentLoggedInAccountId != 0) {
            // Gửi thông báo CLIENT_LOGOUT đến server
            sendMessage(new NC_Message(NC_Message.NC_MessageType.CLIENT_LOGOUT, idComputer, nameComputer, currentLoggedInAccountId, currentLoggedInAccountName));
            updateStatus("Đã đăng xuất tài khoản: " + currentLoggedInAccountName);
            this.currentLoggedInAccountId = 0;
            this.currentLoggedInAccountName = null;
        }
    }

    private void updateStatus(String status) {
        if (statusUpdater != null) {
            statusUpdater.accept("Client Status: " + status);
        }
        System.out.println(status);
    }

    public int getIdComputer() {
        return idComputer;
    }

    public String getNameComputer() {
        return nameComputer;
    }

    public int getCurrentLoggedInAccountId() {
        return currentLoggedInAccountId;
    }

    public String getCurrentLoggedInAccountName() {
        return currentLoggedInAccountName;
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}