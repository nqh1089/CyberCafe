package Socket;

import java.io.*;
import java.net.*;
import java.time.LocalTime;

public class ChatClient {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    private String nameAccount;
    private String tenHienThi; // Máy X
    private TriConsumer onMessageReceived;

    public ChatClient(String ipAdmin, int port, String nameAccount, String tenHienThi, TriConsumer callback) {
        this.nameAccount = nameAccount;
        this.tenHienThi = tenHienThi;
        this.onMessageReceived = callback;

        try {
            socket = new Socket(ipAdmin, port);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // ✅ Gửi thông tin định danh (2 dòng)
            writer.write(nameAccount);
            writer.newLine();
            writer.write(tenHienThi);
            writer.newLine();
            writer.flush();

            // ✅ Bắt đầu lắng nghe phản hồi
            new Thread(this::nhanTinNhan).start();
        } catch (IOException e) {
            System.out.println("Không thể kết nối tới Admin.");
        }
    }

    public void guiTinNhan(String noiDung) {
        try {
            String time = LocalTime.now().withNano(0).toString().substring(0, 5);
            String goiTin = "CHAT|" + nameAccount + "|" + noiDung + "|" + time;

            writer.write(goiTin);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println("Gửi tin lỗi.");
        }
    }

    private void nhanTinNhan() {
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("CHAT|")) {
                    String[] parts = line.split("\\|", 4);
                    if (parts.length == 4 && onMessageReceived != null) {
                        onMessageReceived.accept("Admin", parts[2], parts[3]); // luôn hiển thị tên Admin
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Mất kết nối tới server.");
        }
    }

    public interface TriConsumer {
        void accept(String nguoiGui, String noiDung, String thoiGian);
    }

    public String getTenHienThi() {
        return tenHienThi;
    }

    public String getNameAccount() {
        return nameAccount;
    }
}
