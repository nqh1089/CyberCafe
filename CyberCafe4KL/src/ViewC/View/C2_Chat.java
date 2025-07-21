package ViewC.View;

import Socket.NC_ChatClient;
import Socket.NC_Message;
import ViewC.Code.CN_BienToanCuc;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

public class C2_Chat extends javax.swing.JFrame {

    // Khai báo NC_ChatClient
    private NC_ChatClient chatClient;
    // Khai báo JTextPane để hiển thị tin nhắn
    private JTextPane chatDisplayPane;
    private StyledDocument doc;

    public C2_Chat() {
        initComponents();
        setTitle("CyberCafe4KL_Client - Máy: " + CN_BienToanCuc.TenMay); // Đặt tiêu đề rõ ràng hơn
        setResizable(false);
        // Thay đổi DefaultCloseOperation để xử lý ngắt kết nối khi đóng cửa sổ
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        // Gán icon gửi
        String iconPath = "E:/SU25/BL2/PRO230_DATN/CyberCafe4KL/CyberCafe4KL/src/Assets/Client/Send.png";
        ImageIcon icon = new ImageIcon(iconPath);
        Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        lblSend.setIcon(new ImageIcon(img));
        lblSend.setText("");

        // --- Khởi tạo và tích hợp JTextPane vào pnlZoneMessage (phần này vẫn nằm trong constructor) ---
        chatDisplayPane = new JTextPane();
        chatDisplayPane.setEditable(false); // Không cho phép chỉnh sửa trực tiếp
        doc = chatDisplayPane.getStyledDocument(); // Lấy StyledDocument để định dạng văn bản

        // Tạo một JScrollPane để cuộn nội dung chatDisplayPane
        JScrollPane scrollPane = new JScrollPane(chatDisplayPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Đảm bảo pnlZoneMessage sử dụng BorderLayout để JScrollPane lấp đầy
        pnlZoneMessage.setLayout(new BorderLayout());
        pnlZoneMessage.add(scrollPane, BorderLayout.CENTER);
        // --- Kết thúc tích hợp JTextPane ---

        // --- ĐẶT MÀU NỀN CHO KHU VỰC HIỂN THỊ TIN NHẮN TẠI ĐÂY (NGOÀI initComponents) ---
        // Đặt màu nền cho chính JTextPane
        chatDisplayPane.setBackground(new java.awt.Color(30, 30, 47));
        // Đặt màu nền cho viewport của JScrollPane (phần nền mà nội dung chat sẽ hiển thị)
        scrollPane.getViewport().setBackground(new java.awt.Color(30, 30, 47));
        // Đặt màu nền cho pnlZoneMessage (mặc dù JScrollPane sẽ che phần lớn nó, nhưng tốt cho sự nhất quán)
        pnlZoneMessage.setBackground(new java.awt.Color(30, 30, 47));
        // --- KẾT THÚC PHẦN ĐẶT MÀU NỀN ---


        // Khởi tạo và kết nối NC_ChatClient
        // Đảm bảo IDComputer và TenMay đã được thiết lập từ CN_BienToanCuc
        if (CN_BienToanCuc.IDComputer == -1 || CN_BienToanCuc.TenMay.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Thông tin máy tính chưa được thiết lập. Vui lòng kiểm tra CN_BienToanCuc.", "Lỗi khởi tạo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        chatClient = new NC_ChatClient(CN_BienToanCuc.IDComputer, CN_BienToanCuc.TenMay);

        // Đặt callback để nhận tin nhắn từ Admin
        chatClient.setMessageReceiver(this::displayMessage);

        // Đặt callback để cập nhật trạng thái (tùy chọn, có thể hiển thị trong console hoặc một label nhỏ)
        chatClient.setStatusUpdater(this::updateConnectionStatus);

        chatClient.connect(); // Kết nối tới server

        // Thêm Listener cho nút gửi (lblSend)
        lblSend.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sendMessage();
            }
        });

        // Thêm Listener cho trường nhập văn bản (txtText) khi nhấn Enter
        txtText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Thêm WindowListener để xử lý ngắt kết nối khi đóng cửa sổ
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (chatClient != null && chatClient.isConnected()) {
                    // Nếu tài khoản đang đăng nhập, gửi logout trước khi disconnect
                    if (CN_BienToanCuc.IDAccount != -1) {
                        chatClient.logoutAccount();
                    }
                    // Gửi thông báo CLIENT_DISCONNECT và đóng kết nối
                    chatClient.disconnect("Cửa sổ chat Client đóng.");
                }
            }
        });

        SwingUtilities.invokeLater(() -> {
            try {
                // Đợi một chút để kết nối ổn định (không phải cách lý tưởng cho ứng dụng lớn)
                Thread.sleep(1000);
                if (chatClient.isConnected() && CN_BienToanCuc.IDAccount != -1) {
                    chatClient.loginAccount(CN_BienToanCuc.IDAccount, CN_BienToanCuc.TenTaiKhoan);
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        });
    }

    // Phương thức hiển thị tin nhắn trên JTextPane
    private void displayMessage(NC_Message message) {
        SwingUtilities.invokeLater(() -> {
            SimpleAttributeSet style = new SimpleAttributeSet();
            String textToDisplay;

            // Kiểm tra loại tin nhắn và ID người gửi/người nhận
            if (message.getType() == NC_Message.NC_MessageType.CHAT) {
                if (message.getSenderId() == CN_BienToanCuc.IDAccount) {
                    // Tin nhắn gửi đi (từ client này)
                    StyleConstants.setForeground(style, new Color(51, 255, 255)); // Xanh lam
                    StyleConstants.setAlignment(style, StyleConstants.ALIGN_RIGHT);
                    textToDisplay = "Bạn (" + message.getSenderName() + ") : " + message.getContent() + "\n";
                } else if (message.getSenderId() == 0 && message.getSenderName().equals("Admin")) {
                    // Tin nhắn từ Admin (senderId = 0, senderName = "Admin" theo logic của bạn)
                    StyleConstants.setForeground(style, new Color(153, 255, 0)); // Xanh lá cây (Admin)
                    StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
                    textToDisplay = message.getSenderName() + ": " + message.getContent() + "\n";
                } else {
                    // Tin nhắn từ các nguồn khác (nếu có, ví dụ hệ thống)
                    StyleConstants.setForeground(style, Color.WHITE); // Mặc định trắng
                    StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
                    textToDisplay = message.getSenderName() + ": " + message.getContent() + "\n";
                }
            } else {
                // Các loại tin nhắn trạng thái hoặc hệ thống
                StyleConstants.setForeground(style, Color.YELLOW); // Màu vàng cho thông báo hệ thống
                StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
                textToDisplay = "[" + message.getType() + "] " + message.getContent() + "\n";
            }

            try {
                doc.insertString(doc.getLength(), textToDisplay, style);
                // Cuộn xuống cuối
                chatDisplayPane.setCaretPosition(doc.getLength());
            } catch (BadLocationException e) {
                System.err.println("Lỗi chèn văn bản vào JTextPane: " + e.getMessage());
            }
        });
    }

    // Phương thức gửi tin nhắn
    private void sendMessage() {
        String content = txtText.getText().trim();
        if (!content.isEmpty()) {
            if (chatClient != null && chatClient.isConnected()) {
                // ID của Admin (server) là 0 hoặc một ID cụ thể. Giả sử là 0 cho Admin.
                // SenderId là ID của tài khoản đang đăng nhập trên Client
                int senderId = CN_BienToanCuc.IDAccount;
                String senderName = CN_BienToanCuc.TenTaiKhoan;
                int receiverId = 0; // Gửi tới Admin (receiverId của Admin)

                NC_Message chatMessage = new NC_Message(
                        NC_Message.NC_MessageType.CHAT,
                        senderId,
                        receiverId,
                        senderName,
                        content,
                        new Date()
                );
                chatClient.sendMessage(chatMessage);
                // Hiển thị tin nhắn của chính mình ngay lập tức
                displayMessage(chatMessage);
                txtText.setText(""); // Xóa nội dung đã gửi
            } else {
                displayMessage(new NC_Message(NC_Message.NC_MessageType.ADMIN_READY, "Bạn chưa kết nối với Server hoặc đang ngắt kết nối."));
            }
        }
    }

    // Phương thức cập nhật trạng thái kết nối (có thể hiển thị trong console hoặc một JLabel nhỏ)
    private void updateConnectionStatus(String status) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Client Connection Status: " + status);
            // Nếu bạn có một JLabel để hiển thị trạng thái, bạn có thể cập nhật nó ở đây
            // Ví dụ: lblStatus.setText(status);
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        pnlMain2 = new javax.swing.JPanel();
        pnlRegister = new javax.swing.JPanel();
        lblTittle = new javax.swing.JLabel();
        txtText = new javax.swing.JTextField();
        pnlZoneMessage = new javax.swing.JPanel();
        lblSend = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlMain.setBackground(new java.awt.Color(51, 51, 255));

        pnlMain2.setBackground(new java.awt.Color(153, 255, 153));

        pnlRegister.setBackground(new java.awt.Color(30, 30, 47));

        lblTittle.setBackground(new java.awt.Color(255, 255, 255));
        lblTittle.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblTittle.setForeground(new java.awt.Color(255, 255, 255));
        lblTittle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTittle.setText("CHAT");

        pnlZoneMessage.setBackground(new java.awt.Color(30, 30, 47));
        pnlZoneMessage.setForeground(new java.awt.Color(30, 30, 47));

        javax.swing.GroupLayout pnlZoneMessageLayout = new javax.swing.GroupLayout(pnlZoneMessage);
        pnlZoneMessage.setLayout(pnlZoneMessageLayout);
        pnlZoneMessageLayout.setHorizontalGroup(
            pnlZoneMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlZoneMessageLayout.setVerticalGroup(
            pnlZoneMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );

        lblSend.setForeground(new java.awt.Color(153, 255, 255));
        lblSend.setText("Send");

        javax.swing.GroupLayout pnlRegisterLayout = new javax.swing.GroupLayout(pnlRegister);
        pnlRegister.setLayout(pnlRegisterLayout);
        pnlRegisterLayout.setHorizontalGroup(
            pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRegisterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTittle, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(pnlRegisterLayout.createSequentialGroup()
                .addComponent(txtText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSend)
                .addGap(13, 13, 13))
            .addComponent(pnlZoneMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlRegisterLayout.setVerticalGroup(
            pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRegisterLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblTittle, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(pnlZoneMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1)
                .addGroup(pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtText, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSend)))
        );

        javax.swing.GroupLayout pnlMain2Layout = new javax.swing.GroupLayout(pnlMain2);
        pnlMain2.setLayout(pnlMain2Layout);
        pnlMain2Layout.setHorizontalGroup(
            pnlMain2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMain2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlRegister, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        pnlMain2Layout.setVerticalGroup(
            pnlMain2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMain2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlRegister, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlMain2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlMain2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new C2_Chat().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblSend;
    private javax.swing.JLabel lblTittle;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlMain2;
    private javax.swing.JPanel pnlRegister;
    private javax.swing.JPanel pnlZoneMessage;
    private javax.swing.JTextField txtText;
    // End of variables declaration//GEN-END:variables
}
