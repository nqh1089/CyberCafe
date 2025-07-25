package ViewC.View;

import Socket.NC_ChatClient;
import Socket.NC_Message;
import ViewC.Code.CN_BienToanCuc;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class C2_Chat extends javax.swing.JFrame {

    public NC_ChatClient chatClient;
    private JTextPane chatDisplayPane;
    private StyledDocument doc;
    public static C2_Chat instance;

    public C2_Chat() {
        initComponents();
        instance = this;
        setTitle("CyberCafe4KL_Client - Máy: " + CN_BienToanCuc.TenMay);
        setResizable(false);
        // k cho đóng form (Vô hiệu hoá nút X)
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        String iconPath = "E:/SU25/BL2/PRO230_DATN/CyberCafe4KL/CyberCafe4KL/src/Assets/Client/Send.png";
        ImageIcon icon = new ImageIcon(iconPath);
        Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        lblSend.setIcon(new ImageIcon(img));
        lblSend.setText("");

        chatDisplayPane = new JTextPane();
        chatDisplayPane.setEditable(false);
        doc = chatDisplayPane.getStyledDocument();

        JScrollPane scrollPane = new JScrollPane(chatDisplayPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        pnlZoneMessage.setLayout(new BorderLayout());
        pnlZoneMessage.add(scrollPane, BorderLayout.CENTER);

        chatDisplayPane.setBackground(new java.awt.Color(30, 30, 47));
        scrollPane.getViewport().setBackground(new java.awt.Color(30, 30, 47));
        pnlZoneMessage.setBackground(new java.awt.Color(30, 30, 47));

        if (CN_BienToanCuc.IDComputer == -1 || CN_BienToanCuc.TenMay.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Thông tin máy tính chưa được thiết lập.", "Lỗi khởi tạo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        chatClient = new NC_ChatClient(CN_BienToanCuc.IDComputer, CN_BienToanCuc.TenMay);

        chatClient.setMessageReceiver(this::displayMessage);
        chatClient.setStatusUpdater(this::updateConnectionStatus);

        chatClient.connect();

        lblSend.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sendMessage();
            }
        });

        txtText.addActionListener(e -> sendMessage());

//        this.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                if (chatClient != null && chatClient.isConnected()) {
//                    if (CN_BienToanCuc.IDAccount != -1) {
//                        chatClient.logoutAccount();
//                    }
//                    chatClient.disconnect("Cửa sổ chat Client đóng.");
//                }
//            }
//        });

        SwingUtilities.invokeLater(() -> {
            try {
                Thread.sleep(1000);
                if (chatClient.isConnected() && CN_BienToanCuc.IDAccount != -1) {
                    chatClient.loginAccount(CN_BienToanCuc.IDAccount, CN_BienToanCuc.TenTaiKhoan);
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
        public static void showChat() {
        if (instance == null) {
            instance = new C2_Chat();
        }
        instance.setVisible(true);
        instance.setState(JFrame.NORMAL); // Nếu đang thu nhỏ thì bật lại
        instance.toFront(); // Đưa ra trước các cửa sổ khác
    }

    private void displayMessage(NC_Message message) {
        SwingUtilities.invokeLater(() -> {
            SimpleAttributeSet style = new SimpleAttributeSet();
            String time = new java.text.SimpleDateFormat("HH:mm").format(message.getSentAt());
            String textToDisplay;

            if ("Admin".equals(message.getSenderName())) {
                StyleConstants.setForeground(style, new Color(153, 255, 0)); // Xanh lá
                textToDisplay = "Admin [" + time + "]: " + message.getContent() + "\n";
            } else {
                StyleConstants.setForeground(style, new Color(51, 255, 255)); // Xanh cyan
                textToDisplay = "Máy " + CN_BienToanCuc.IDComputer + " [" + time + "]: " + message.getContent() + "\n";
            }

            try {
                doc.insertString(doc.getLength(), textToDisplay, style);
                chatDisplayPane.setCaretPosition(doc.getLength());
            } catch (BadLocationException e) {
                System.err.println("Lỗi hiển thị: " + e.getMessage());
            }
        });
    }

    private void sendMessage() {
        String content = txtText.getText().trim();
        if (!content.isEmpty()) {
            if (chatClient != null && chatClient.isConnected()) {
                int senderId = CN_BienToanCuc.IDAccount;
                String senderName = CN_BienToanCuc.TenTaiKhoan;
                int receiverId = 1; // ID Admin THẬT

                NC_Message chatMessage = new NC_Message(
                        NC_Message.NC_MessageType.CHAT,
                        senderId,
                        receiverId,
                        senderName,
                        content,
                        new Date()
                );
                chatClient.sendMessage(chatMessage);
                txtText.setText("");
            } else {
                System.out.println("Chưa kết nối server.");
            }
        }
    }

    private void updateConnectionStatus(String status) {
        SwingUtilities.invokeLater(() -> System.out.println("Trạng thái: " + status));
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
