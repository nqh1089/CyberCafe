package Socket;

import java.io.Serializable;
import java.util.Date;

public class NC_Message implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum NC_MessageType {
        CHAT,               // Tin nhắn chat thông thường
        CLIENT_CONNECT,     // Client vừa kết nối đến server (gửi từ ClientHandler đến Server)
        CLIENT_LOGIN,       // Client vừa đăng nhập tài khoản (gửi từ ClientHandler đến Server)
        CLIENT_LOGOUT,      // Client vừa đăng xuất tài khoản (gửi từ ClientHandler đến Server)
        CLIENT_DISCONNECT,  // Client vừa ngắt kết nối (gửi từ ClientHandler đến Server)
        ADMIN_READY,        // Admin sẵn sàng nhận thông báo (gửi từ Admin UI đến Server)
        REQUEST_HISTORY,    // Client yêu cầu lịch sử chat (tùy chọn)
        CHAT_HISTORY_ITEM   // Một mục trong lịch sử chat (gửi từ Server/DB đến UI)
    }

    private NC_MessageType type;
    private int senderId; // ID của người gửi (AdminID hoặc AccountID của Client)
    private int receiverId; // ID của người nhận (AdminID hoặc AccountID của Client)
    private String senderName; // Tên hiển thị của người gửi (VD: "Admin", "User1")
    private String content; // Nội dung tin nhắn
    private Date sentAt; // Thời gian gửi tin nhắn

    // Thêm các trường cho thông tin máy/tài khoản khi cần
    private int computerId;
    private String computerName;
    private String accountName; // Dùng khi gửi thông tin đăng nhập/đăng xuất

    // Constructor cho tin nhắn CHAT thông thường
    public NC_Message(NC_MessageType type, int senderId, int receiverId, String senderName, String content, Date sentAt) {
        this.type = type;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.content = content;
        this.sentAt = sentAt;
    }

    // Constructor cho các loại tin nhắn trạng thái (CLIENT_CONNECT, CLIENT_LOGIN, v.v.)
    public NC_Message(NC_MessageType type, int computerId, String computerName, int accountId, String accountName) {
        this.type = type;
        this.computerId = computerId;
        this.computerName = computerName;
        this.senderId = accountId; // ID tài khoản là sender cho các sự kiện client
        this.accountName = accountName;
        this.sentAt = new Date(); // Thời gian sự kiện
        this.content = ""; // Không có nội dung chat
    }

    // Constructor cho tin nhắn hệ thống hoặc không có người gửi/nhận cụ thể
    public NC_Message(NC_MessageType type, String content) {
        this.type = type;
        this.content = content;
        this.sentAt = new Date();
        this.senderId = 0; // ID hệ thống
        this.receiverId = 0; // ID hệ thống
        this.senderName = "System";
    }

    // --- Getters ---
    public NC_MessageType getType() {
        return type;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getContent() {
        return content;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public int getComputerId() {
        return computerId;
    }

    public String getComputerName() {
        return computerName;
    }

    public String getAccountName() {
        return accountName;
    }

    @Override
    public String toString() {
        return "NC_Message{" +
                "type=" + type +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", senderName='" + senderName + '\'' +
                ", content='" + content + '\'' +
                ", sentAt=" + sentAt +
                ", computerId=" + computerId +
                ", computerName='" + computerName + '\'' +
                ", accountName='" + accountName + '\'' +
                '}';
    }
}