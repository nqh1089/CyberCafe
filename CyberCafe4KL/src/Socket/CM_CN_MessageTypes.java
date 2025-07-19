package Socket;

public class CM_CN_MessageTypes {
    public static final String CHAT_MESSAGE = "CHAT_MESSAGE"; // Client gửi đến Admin, Admin gửi đến Client
    public static final String CLIENT_ONLINE = "CLIENT_ONLINE"; // Client thông báo online
    public static final String CLIENT_OFFLINE = "CLIENT_OFFLINE"; // Client thông báo offline
    public static final String ADMIN_MESSAGE = "ADMIN_MESSAGE"; // Admin gửi đến Client
    public static final String PING = "PING"; // Server gửi để kiểm tra
    public static final String PONG = "PONG"; // Client phản hồi PING
    
    // Server có thể gửi thêm các loại tin nhắn khác nếu cần:
    // public static final String UPDATE_STATUS = "UPDATE_STATUS"; 
    // public static final String REQUEST_HISTORY = "REQUEST_HISTORY"; 
}