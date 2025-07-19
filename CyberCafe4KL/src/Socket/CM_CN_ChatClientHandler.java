package Socket;

import ViewC.Code.CN_BienToanCuc; // Vẫn import từ ViewC.Code
import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class CM_CN_ChatClientHandler {

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private String serverIp;
    private int serverPort;
    private Consumer<String[]> messageConsumer; 
    private boolean isConnected = false;
    private ExecutorService executorService;

    public CM_CN_ChatClientHandler(String serverIp, int serverPort, Consumer<String[]> messageConsumer) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.messageConsumer = messageConsumer;
        this.executorService = Executors.newSingleThreadExecutor();
        connect();
    }

    private void connect() {
        executorService.submit(() -> {
            try {
                socket = new Socket(serverIp, serverPort);
                writer = new PrintWriter(socket.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                isConnected = true;
                System.out.println("Client đã kết nối tới Server Chat.");

                sendInitialConnectInfo();

                String line;
                while (isConnected && (line = reader.readLine()) != null) {
                    processIncomingMessage(line);
                }
            } catch (IOException e) {
                if (isConnected) { 
                    System.err.println("Lỗi kết nối hoặc đọc dữ liệu từ server: " + e.getMessage());
                }
                disconnect(); 
            }
        });
    }

    private void sendInitialConnectInfo() {
        String initialMessage = CM_CN_MessageTypes.CLIENT_ONLINE + "|" +
                                CN_BienToanCuc.IDAccount + "|" +
                                CN_BienToanCuc.IDComputer + "|" +
                                CN_BienToanCuc.TenTaiKhoan + "|" +
                                CN_BienToanCuc.TenMay + "|" +
                                CN_BienToanCuc.LogAccessID; 
        sendMessage(initialMessage);
    }

    private void processIncomingMessage(String message) {
        String[] parts = message.split("\\|", 4);
        if (parts.length == 4) {
            String messageType = parts[0];
            String senderName = parts[1];
            String content = parts[2];
            String timestamp = parts[3];

            switch (messageType) {
                case CM_CN_MessageTypes.CHAT_MESSAGE:
                case CM_CN_MessageTypes.ADMIN_MESSAGE:
                    messageConsumer.accept(new String[]{senderName, content, timestamp});
                    break;
                case CM_CN_MessageTypes.PING:
                    sendMessage(CM_CN_MessageTypes.PONG + "|" + CN_BienToanCuc.TenTaiKhoan + "|" + LocalTime.now().withNano(0).toString().substring(0, 5));
                    break;
                default:
                    System.out.println("Tin nhắn không rõ loại từ server: " + message);
            }
        } else {
            System.out.println("Tin nhắn không đúng định dạng từ server: " + message);
        }
    }

    public void sendChatMessage(String messageContent) {
        if (isConnected && writer != null) {
            String formattedMessage = CM_CN_MessageTypes.CHAT_MESSAGE + "|" +
                                      CN_BienToanCuc.IDAccount + "|" +
                                      CN_BienToanCuc.IDComputer + "|" +
                                      CN_BienToanCuc.TenTaiKhoan + "|" +
                                      CN_BienToanCuc.TenMay + "|" +
                                      CN_BienToanCuc.LogAccessID + "|" +
                                      messageContent;
            sendMessage(formattedMessage);
        } else {
            System.err.println("Không thể gửi tin nhắn: Client chưa kết nối hoặc đã ngắt kết nối.");
        }
    }

    private void sendMessage(String message) {
        if (writer != null) {
            writer.println(message);
            writer.flush();
        }
    }

    public void disconnect() {
        if (isConnected) {
            isConnected = false; 
            try {
                if (writer != null) {
                    writer.println(CM_CN_MessageTypes.CLIENT_OFFLINE + "|" +
                                   CN_BienToanCuc.IDAccount + "|" +
                                   CN_BienToanCuc.IDComputer + "|" +
                                   CN_BienToanCuc.TenTaiKhoan + "|" +
                                   CN_BienToanCuc.TenMay + "|" +
                                   CN_BienToanCuc.LogAccessID);
                    writer.flush();
                }
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
                if (reader != null) reader.close();
                if (writer != null) writer.close();
                executorService.shutdownNow();
                System.out.println("Client đã ngắt kết nối.");
            } catch (IOException e) {
                System.err.println("Lỗi khi đóng kết nối client: " + e.getMessage());
            }
        }
    }

    public boolean isConnected() {
        return isConnected;
    }
}