package ViewC.View;

import Socket.ChatClient;
import ViewC.Code.CN_BienToanCuc;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;

public class C2_Chat extends javax.swing.JFrame {

    private ChatClient clientSocket;
    private JTextPane txtChat;

    public C2_Chat() {
        initComponents();
        setTitle("CyberCafe4KL_Client");
        setResizable(false);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);


        // Gán icon gửi
        String iconPath = "E:/SU25/BL2/PRO230_DATN/CyberCafe4KL/CyberCafe4KL/src/Assets/Client/Send.png";
        ImageIcon icon = new ImageIcon(iconPath);
        Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        lblSend.setIcon(new ImageIcon(img));
        lblSend.setText("");

        // Tạo vùng chat
        txtChat = new JTextPane();
        txtChat.setEditable(false);
        txtChat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtChat.setBackground(new Color(30, 30, 47));
        txtChat.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(txtChat);
        pnlZoneMessage.setLayout(new BorderLayout());
        pnlZoneMessage.add(scrollPane, BorderLayout.CENTER);

        // Tên thật & tên hiển thị
        String nameAccount = CN_BienToanCuc.TenTaiKhoan; // dùng để gửi & lưu DB
        String tenMay = "Máy " + CN_BienToanCuc.IDComputer; // dùng để hiển thị
        clientSocket = new ChatClient("26.150.90.74", 1902, nameAccount, tenMay, this::hienThiTinNhan);

        // Gửi bằng chuột
        lblSend.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                guiTinNhan();
            }
        });

        // Gửi bằng Enter
        txtText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume(); // chặn xuống dòng
                    guiTinNhan();
                }
            }
        });
    }

    private void guiTinNhan() {
        String msg = txtText.getText().trim();
        if (!msg.isEmpty()) {
            clientSocket.guiTinNhan(msg);
            String time = LocalTime.now().withNano(0).toString().substring(0, 5);
            appendTinNhan(clientSocket.getTenHienThi(), msg, time, new Color(204, 255, 255)); // Client: Cyan
            txtText.setText("");
        }
    }

    public void hienThiTinNhan(String nguoiGui, String noiDung, String thoiGian) {
        appendTinNhan(nguoiGui, noiDung, thoiGian, new Color(153, 255, 0)); // Admin: Xanh
    }

    private void appendTinNhan(String ten, String nd, String time, Color color) {
        StyledDocument doc = txtChat.getStyledDocument();
        try {
            Style style = txtChat.addStyle("style", null);
            StyleConstants.setForeground(style, color);
            doc.insertString(doc.getLength(), ten + " [" + time + "]: " + nd + "\n", style);
            txtChat.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
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
