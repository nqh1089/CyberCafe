package Socket;

import java.io.Serializable;
import java.util.Date;

public class NC_Message implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum NC_MessageType {
        CHAT,
        CLIENT_CONNECT,
        CLIENT_LOGIN,
        CLIENT_LOGOUT,
        CLIENT_DISCONNECT,
        ADMIN_READY,
        REQUEST_HISTORY,
        CHAT_HISTORY_ITEM
    }

    private NC_MessageType type;
    private int senderId;
    private int receiverId;
    private String senderName;
    private String content;
    private Date sentAt;

    private int computerId;
    private String computerName;
    private String accountName;

    private int logAccessId; // ✅ Thêm LogAccessID

    public NC_Message(NC_MessageType type, int senderId, int receiverId, String senderName, String content, Date sentAt) {
        this.type = type;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.content = content;
        this.sentAt = sentAt;
    }

    public NC_Message(NC_MessageType type, int computerId, String computerName, int accountId, String accountName) {
        this.type = type;
        this.computerId = computerId;
        this.computerName = computerName;
        this.senderId = accountId;
        this.accountName = accountName;
        this.sentAt = new Date();
    }

    public NC_Message(NC_MessageType type, String content) {
        this.type = type;
        this.content = content;
        this.sentAt = new Date();
        this.senderId = 0;
        this.receiverId = 0;
        this.senderName = "System";
    }

    public NC_Message(NC_MessageType type, int computerId, int accountId, int logAccessId) {
        this.type = type;
        this.computerId = computerId;
        this.senderId = accountId;
        this.logAccessId = logAccessId;
        this.sentAt = new Date();
    }

    public NC_MessageType getType() { return type; }
    public int getSenderId() { return senderId; }
    public int getReceiverId() { return receiverId; }
    public String getSenderName() { return senderName; }
    public String getContent() { return content; }
    public Date getSentAt() { return sentAt; }
    public int getComputerId() { return computerId; }
    public String getComputerName() { return computerName; }
    public String getAccountName() { return accountName; }
    public int getLogAccessId() { return logAccessId; }
}
