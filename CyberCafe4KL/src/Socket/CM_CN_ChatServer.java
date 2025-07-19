package Socket; // Đổi package thành Socket

import Controller.DBConnection; // Vẫn import từ Controller.DBConnection
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class CM_CN_ChatServer {

    private static final int PORT = 1902;
    private static ServerSocket serverSocket; 
    private static ExecutorService pool; 

    private static Map<Integer, CM_CN_ServerClientHandler> onlineClientHandlers = Collections.synchronizedMap(new HashMap<>());
    
    private static Consumer<CM_CN_OnlineClientInfo> addClientCallback; 
    private static Consumer<Integer> removeClientCallback; 
    private static Consumer<String[]> newMessageCallback; 

    public static void startServer(Consumer<CM_CN_OnlineClientInfo> addClientCb, 
                                   Consumer<Integer> removeClientCb, 
                                   Consumer<String[]> newMessageCb) {
        if (serverSocket != null && !serverSocket.isClosed()) {
            System.out.println("Server Chat đã chạy rồi.");
            return;
        }

        addClientCallback = addClientCb; 
        removeClientCallback = removeClientCb; 
        newMessageCallback = newMessageCb; 

        pool = Executors.newFixedThreadPool(100); 
        
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Chat Server đang lắng nghe trên cổng " + PORT);

            new Thread(() -> {
                while (!serverSocket.isClosed()) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("Client đã kết nối từ: " + clientSocket.getInetAddress().getHostAddress());
                        CM_CN_ServerClientHandler handler = new CM_CN_ServerClientHandler(
                            clientSocket, 
                            onlineClientHandlers, 
                            addClientCallback, 
                            removeClientCallback, 
                            newMessageCallback 
                        );
                        pool.execute(handler); 
                    } catch (IOException e) {
                        if (!serverSocket.isClosed()) {
                            System.err.println("Lỗi khi chấp nhận kết nối client: " + e.getMessage());
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            System.err.println("Lỗi khởi động Server Chat: " + e.getMessage());
            stopServer(); 
        }
    }

    public static void stopServer() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("Server Chat đã dừng.");
            } catch (IOException e) {
                System.err.println("Lỗi khi đóng Server Socket: " + e.getMessage());
            }
        }
        if (pool != null && !pool.isShutdown()) {
            pool.shutdownNow(); 
            System.out.println("ExecutorService pool đã tắt.");
        }
        for (Integer computerId : new HashMap<>(onlineClientHandlers).keySet()) { 
            if (removeClientCallback != null) { 
                removeClientCallback.accept(computerId);
            }
        }
        onlineClientHandlers.clear();
    }
    
    public static CM_CN_ServerClientHandler getClientHandler(int computerId) {
        return onlineClientHandlers.get(computerId);
    }
    
    public static void sendAdminMessageToClient(int targetComputerID, String messageContent) {
        CM_CN_ServerClientHandler handler = getClientHandler(targetComputerID);
        if (handler != null && handler.isConnected()) {
            handler.sendAdminChatMessage(messageContent);
            CM_CN_ChatManager.saveChatMessage(CM_CN_ChatManager.ADMIN_ACCOUNT_ID, handler.getClientIDAccount(), messageContent);
        } else {
            System.out.println("Máy tính ID " + targetComputerID + " không online hoặc không tìm thấy.");
        }
    }

    public static void main(String[] args) {
        startServer(
            (info) -> System.out.println("Test: Client Online -> " + info.getNameComputer()),
            (id) -> System.out.println("Test: Client Offline -> ID: " + id),
            (msg) -> System.out.println("Test: New message from " + msg[0] + ": " + msg[1])
        );

        try {
            Thread.sleep(Long.MAX_VALUE); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Server main thread interrupted.");
        }
    }
}