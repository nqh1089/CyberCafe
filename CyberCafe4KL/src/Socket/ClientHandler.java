package Socket;

import Controller.DBConnection;
import ViewAD.View.AD_Chat;

import java.io.*;
import java.net.*;
import java.sql.*;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    private String nameAccount = "Unknown";  // Dùng để lưu DB
    private String tenHienThi = "Unknown";   // Dùng để hiển thị lên giao diện

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Lỗi khi tạo reader/writer cho client.");
        }
    }

    @Override
    public void run() {
        try {
            // B1: Nhận NameAccount và tên hiển thị từ client
            nameAccount = reader.readLine();     // Dòng 1: NameAccount
            tenHienThi = reader.readLine();      // Dòng 2: Máy X

            ChatServer.registerClient(nameAccount, tenHienThi, this);
            System.out.println("Đã kết nối: " + tenHienThi + " (" + nameAccount + ")");

            // B2: Lắng nghe các tin nhắn chat
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("CHAT|")) {
                    String[] parts = line.split("\\|", 4);
                    if (parts.length == 4) {
                        String sender = parts[1];     // nameAccount
                        String content = parts[2];
                        String time = parts[3];

                        // Cập nhật máy gửi gần nhất
                        ChatServer.lastClientSentMessage = sender;

                        // Hiển thị lên giao diện Admin
                        if (AD_Chat.instance != null) {
                            AD_Chat.instance.hienThiTinNhan(tenHienThi, content, time); // Hiển thị: Máy X
                        }

                        // Lưu DB với nameAccount
                        saveMessageToDB(getIDFromName(sender), getAdminID(), content);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Client " + tenHienThi + " đã ngắt kết nối.");
        }
    }

    public void sendMessage(String goiTin) {
        try {
            writer.write(goiTin);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println("Lỗi gửi tin đến " + tenHienThi);
        }
    }

    public void saveMessageToDB(int senderID, int receiverID, String content) {
        if (senderID == -1 || receiverID == -1) {
            System.out.println("Không thể lưu tin nhắn. senderID=" + senderID + ", receiverID=" + receiverID);
            return;
        }

        String sql = "INSERT INTO Message (SenderID, ReceiverID, Content) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, senderID);
            ps.setInt(2, receiverID);
            ps.setString(3, content);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Lỗi lưu tin nhắn: " + e.getMessage());
        }
    }

    // ================= SUPPORT =================

    public static int getIDFromName(String nameAcc) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT IDAccount FROM Account WHERE NameAccount = ?")) {
            ps.setString(1, nameAcc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("IDAccount");
        } catch (Exception e) {
            System.out.println("Không tìm thấy Client với NameAccount = '" + nameAcc + "'");
        }
        return -1;
    }

    public static int getAdminID() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT TOP 1 IDAccount FROM Account WHERE RoleAccount = 'ADMIN'")) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("IDAccount");
        } catch (Exception e) {
            System.out.println("Không tìm thấy Admin.");
        }
        return -1;
    }

    public String getNameAccount() {
        return nameAccount;
    }

    public String getTenHienThi() {
        return tenHienThi;
    }

    public void setTenHienThi(String tenHienThi) {
        this.tenHienThi = tenHienThi;
    }
}
