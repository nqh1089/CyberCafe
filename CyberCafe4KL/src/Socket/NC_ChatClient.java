package Socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class NC_ChatClient {

    private static final String SERVER_IP = "26.150.90.74"; // ✅ IP Admin cố định
    private static final int SERVER_PORT = 1902;

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private int idComputer;
    private String nameComputer;
    private int currentLoggedInAccountId = 0;
    private String currentLoggedInAccountName = null;

    private Consumer<NC_Message> messageReceiver;
    private Consumer<String> statusUpdater;

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

            NC_Message initialMessage = new NC_Message(
                NC_Message.NC_MessageType.CLIENT_CONNECT,
                idComputer, nameComputer, 0, null
            );
            sendMessage(initialMessage);

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
                if (messageReceiver != null) {
                    messageReceiver.accept(message);
                }
            }
        } catch (Exception e) {
            disconnect("Mất kết nối.");
        }
    }

    public void sendMessage(NC_Message message) {
        try {
            if (out != null) {
                out.writeObject(message);
                out.flush();
            }
        } catch (IOException e) {
            disconnect("Lỗi gửi tin nhắn.");
        }
    }

    public void disconnect(String reason) {
        try {
            if (socket != null && !socket.isClosed()) {
                if (currentLoggedInAccountId != 0) {
                    sendMessage(new NC_Message(
                        NC_Message.NC_MessageType.CLIENT_LOGOUT,
                        idComputer, nameComputer, currentLoggedInAccountId, currentLoggedInAccountName
                    ));
                }
                sendMessage(new NC_Message(
                    NC_Message.NC_MessageType.CLIENT_DISCONNECT,
                    idComputer, nameComputer, 0, null
                ));
                in.close();
                out.close();
                socket.close();
                updateStatus("Đã ngắt kết nối.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loginAccount(int accountId, String accountName) {
        this.currentLoggedInAccountId = accountId;
        this.currentLoggedInAccountName = accountName;
        sendMessage(new NC_Message(
            NC_Message.NC_MessageType.CLIENT_LOGIN,
            idComputer, nameComputer, accountId, accountName
        ));
        updateStatus("Đã đăng nhập: " + accountName);

        // ✅ Gửi yêu cầu lịch sử chat cho phiên hiện tại
        sendMessage(new NC_Message(
            NC_Message.NC_MessageType.REQUEST_HISTORY,
            idComputer, accountId, ViewC.Code.CN_BienToanCuc.LogAccessID
        ));
    }

    public void logoutAccount() {
        if (currentLoggedInAccountId != 0) {
            sendMessage(new NC_Message(
                NC_Message.NC_MessageType.CLIENT_LOGOUT,
                idComputer, nameComputer, currentLoggedInAccountId, currentLoggedInAccountName
            ));
            updateStatus("Đã đăng xuất: " + currentLoggedInAccountName);
            this.currentLoggedInAccountId = 0;
            this.currentLoggedInAccountName = null;
        }
    }

    private void updateStatus(String status) {
        if (statusUpdater != null) {
            statusUpdater.accept(status);
        }
    }

    // ✅ FIX: isConnected() đúng — dùng biến `socket` chứ không phải `Socket`!
    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}
