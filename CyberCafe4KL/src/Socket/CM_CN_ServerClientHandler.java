package Socket; // Đổi package thành Socket

import Controller.DBConnection; // Vẫn import từ Controller.DBConnection
import java.io.*;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Map;
import java.util.function.Consumer;

public class CM_CN_ServerClientHandler implements Runnable {

    private Socket clientSocket;
    private PrintWriter writer;
    private BufferedReader reader;
    private Map<Integer, CM_CN_ServerClientHandler> onlineClientsHandlers; 
    private Consumer<CM_CN_OnlineClientInfo> addClientCallback; 
    private Consumer<Integer> removeClientCallback; 
    private Consumer<String[]> newMessageCallback; 

    private int clientIDAccount;
    private int clientIDComputer;
    private String clientNameAccount;
    private String clientComputerName;
    private int clientLogAccessID; 

    public CM_CN_ServerClientHandler(Socket socket, 
                                     Map<Integer, CM_CN_ServerClientHandler> onlineClientsHandlers,
                                     Consumer<CM_CN_OnlineClientInfo> addClientCallback,
                                     Consumer<Integer> removeClientCallback,
                                     Consumer<String[]> onNewMessage) {
        this.clientSocket = socket;
        this.onlineClientsHandlers = onlineClientsHandlers;
        this.addClientCallback = addClientCallback;
        this.removeClientCallback = removeClientCallback;
        this.newMessageCallback = onNewMessage;
    }

    @Override
    public void run() {
        try {
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                processClientMessage(line);
            }
        } catch (IOException e) {
            System.err.println("Lỗi đọc/ghi từ client " + (clientNameAccount != null ? clientNameAccount : clientSocket.getInetAddress().getHostAddress()) + ": " + e.getMessage());
        } finally {
            disconnectClient(); 
        }
    }

    private void processClientMessage(String message) {
        String[] parts = message.split("\\|", -1); 

        if (parts.length > 0) {
            String messageType = parts[0];
            
            switch (messageType) {
                case CM_CN_MessageTypes.CLIENT_ONLINE:
                    if (parts.length >= 6) { 
                        try {
                            clientIDAccount = Integer.parseInt(parts[1]);
                            clientIDComputer = Integer.parseInt(parts[2]);
                            clientNameAccount = parts[3];
                            clientComputerName = parts[4];
                            clientLogAccessID = Integer.parseInt(parts[5]); 

                            onlineClientsHandlers.put(clientIDComputer, this); 
                            
                            if (addClientCallback != null) {
                                addClientCallback.accept(new CM_CN_OnlineClientInfo(clientIDAccount, clientIDComputer, clientNameAccount, clientComputerName, clientLogAccessID));
                            }

                            System.out.println("Client " + clientNameAccount + " (Máy: " + clientComputerName + ", IDLog: " + clientLogAccessID + ") đã ONLINE.");

                        } catch (NumberFormatException e) {
                            System.err.println("Lỗi phân tích số khi nhận CLIENT_ONLINE: " + message);
                        }
                    }
                    break;

                case CM_CN_MessageTypes.CLIENT_OFFLINE:
                    System.out.println("Client " + clientNameAccount + " (Máy: " + clientComputerName + ") đã OFFLINE.");
                    break; 

                case CM_CN_MessageTypes.CHAT_MESSAGE:
                    if (parts.length >= 7) { 
                        try {
                            int senderAccID = Integer.parseInt(parts[1]);
                            String senderName = parts[3];
                            String senderCompName = parts[4];
                            int currentLogAccessID = Integer.parseInt(parts[5]); 
                            String content = parts[6];

                            System.out.println("Tin nhắn từ " + senderName + " (Log: " + currentLogAccessID + "): " + content);

                            CM_CN_ChatManager.saveChatMessage(senderAccID, CM_CN_ChatManager.ADMIN_ACCOUNT_ID, content);

                            if (newMessageCallback != null) {
                                newMessageCallback.accept(new String[]{senderCompName, content, LocalTime.now().withNano(0).toString().substring(0, 5)});
                            }

                        } catch (NumberFormatException e) {
                            System.err.println("Lỗi phân tích số khi nhận CHAT_MESSAGE: " + message);
                        }
                    }
                    break;
                case CM_CN_MessageTypes.PONG:
                    System.out.println("Nhận PONG từ " + clientNameAccount + " lúc " + parts[2]); 
                    break;
                default:
                    System.out.println("Server nhận tin nhắn không rõ loại: " + message);
            }
        }
    }

    public void sendAdminChatMessage(String messageContent) {
        if (writer != null) {
            String time = LocalTime.now().withNano(0).toString().substring(0, 5);
            writer.println(CM_CN_MessageTypes.ADMIN_MESSAGE + "|Admin|" + messageContent + "|" + time);
            writer.flush();
            System.out.println("Server đã gửi tin nhắn đến client " + clientComputerName + ": " + messageContent);
        }
    }

    private void disconnectClient() {
        if (clientIDComputer != 0 && onlineClientsHandlers.containsKey(clientIDComputer)) { 
            onlineClientsHandlers.remove(clientIDComputer); 
            System.out.println("Client " + (clientNameAccount != null ? clientNameAccount : clientIDComputer) + " (ID: " + clientIDComputer + ") đã bị xóa khỏi danh sách online.");
            
            if (removeClientCallback != null) {
                removeClientCallback.accept(clientIDComputer);
            }
        }
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Lỗi đóng kết nối client " + (clientNameAccount != null ? clientNameAccount : clientIDComputer) + ": " + e.getMessage());
        }
    }

    public int getClientIDAccount() { return clientIDAccount; }
    public int getClientIDComputer() { return clientIDComputer; }
    public String getClientNameAccount() { return clientNameAccount; }
    public String getClientComputerName() { return clientComputerName; }
    public int getLogAccessID() { return clientLogAccessID; } 

    public boolean isConnected() {
        return clientSocket != null && clientSocket.isConnected() && !clientSocket.isClosed();
    }
}