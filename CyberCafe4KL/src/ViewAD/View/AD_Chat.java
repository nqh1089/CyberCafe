package ViewAD.View;

import Socket.ChatServer;
import Socket.ChatTargetManager;
import ViewAD.Code.AD_Chat_pnlSender;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalTime;

public class AD_Chat extends javax.swing.JFrame {

    public static AD_Chat instance;
    private JTextPane txtChat;

    public AD_Chat() {
        initComponents();
        instance = this;
        setTitle("CyberCafe4KL_Admin");
        this.setResizable(false);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        
//        AD_Chat_pnlSender.LoadDanhSachChat(pnlSender);
        // Nếu có máy đang được chọn để nhắn tin
        if (ChatTargetManager.hasTarget()) {
            String nameAccount = ChatTargetManager.getTargetNameAccount();

            // Tìm tên máy tương ứng với NameAccount
            try (Connection conn = Controller.DBConnection.getConnection()) {
                String sql = """
            SELECT TOP 1 C.NameComputer
            FROM LogAccess L
            JOIN Computer C ON L.IDComputer = C.IDComputer
            JOIN Account A ON L.IDAccount = A.IDAccount
            WHERE A.NameAccount = ?
            ORDER BY L.ThoiGianBatDau DESC
        """;
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nameAccount);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String tenMay = rs.getString("NameComputer");
                    lblTittle.setText("CHAT - " + tenMay.toUpperCase());
                }
            } catch (Exception e) {
                lblTittle.setText("CHAT");
            }
        } else {
            lblTittle.setText("CHAT");
        }

        // Gán icon gửi
        String iconPath = "E:/SU25/BL2/PRO230_DATN/CyberCafe4KL/CyberCafe4KL/src/Assets/Client/Send.png";
        ImageIcon icon = new ImageIcon(iconPath);
        Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        lblSend.setIcon(new ImageIcon(img));
        lblSend.setText("");

        // Tạo vùng chat bằng JTextPane để hỗ trợ đổi màu
        txtChat = new JTextPane();
        txtChat.setEditable(false);
        txtChat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtChat.setBackground(new Color(30, 30, 47)); // Nền tối
        txtChat.setForeground(Color.WHITE);          // Mặc định trắng nếu thiếu style

        JScrollPane scrollPane = new JScrollPane(txtChat);
        pnlZoneMessage.setLayout(new BorderLayout());
        pnlZoneMessage.add(scrollPane, BorderLayout.CENTER);

        // Gửi khi click icon
        lblSend.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                guiTinNhan();
            }
        });

        // Gửi khi nhấn Enter (không xử lý Shift)
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
            String time = LocalTime.now().withNano(0).toString().substring(0, 5);
            StyledDocument doc = txtChat.getStyledDocument();

            try {
                // Style tin nhắn từ Admin
                Style adminStyle = txtChat.addStyle("AdminStyle", null);
                StyleConstants.setForeground(adminStyle, new Color(153, 255, 0));
                doc.insertString(doc.getLength(), "Admin [" + time + "]: " + msg + "\n", adminStyle);

                // Gửi cho client cuối cùng đã gửi
                String target = ChatTargetManager.hasTarget()
                        ? ChatTargetManager.getTargetNameAccount()
                        : ChatServer.lastClientSentMessage;

                if (target != null) {
                    ChatServer.guiChoClient(target, msg);
                } else {
                    // Không có máy nào để gửi
                    Style sysStyle = txtChat.addStyle("SystemStyle", null);
                    StyleConstants.setForeground(sysStyle, Color.LIGHT_GRAY);
                    doc.insertString(doc.getLength(), "(Không có máy nào để gửi tin.)\n", sysStyle);
                }

                txtChat.setCaretPosition(doc.getLength()); // Tự scroll xuống dòng cuối

            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }

            txtText.setText(""); // Xoá nội dung
        }
    }

    public void hienThiTinNhan(String tenMay, String noiDung, String thoiGian) {
        StyledDocument doc = txtChat.getStyledDocument();
        try {
            Style clientStyle = txtChat.addStyle("ClientStyle", null);
            StyleConstants.setForeground(clientStyle, new Color(204, 255, 255)); // Client: cyan
            doc.insertString(doc.getLength(), tenMay + " [" + thoiGian + "]: " + noiDung + "\n", clientStyle);
            txtChat.setCaretPosition(doc.getLength()); // Scroll cuối
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public class TestChatDanhSach {

        public static void main(String[] args) {
            JFrame f = new JFrame("Test Danh sách máy");
            f.setSize(270, 700);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel pnlSender = new JPanel(null);
            pnlSender.setPreferredSize(new Dimension(245, 1000));
            pnlSender.setBackground(Color.CYAN);

            JScrollPane scroll = new JScrollPane(pnlSender);
            scroll.setBounds(0, 0, 255, 700);
            scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scroll.setBorder(null);

            f.setLayout(null);
            f.add(scroll);

            AD_Chat_pnlSender.LoadDanhSachChat(pnlSender);
            f.setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        pnlMainMessage = new javax.swing.JPanel();
        pnlRegister = new javax.swing.JPanel();
        txtText = new javax.swing.JTextField();
        pnlZoneMessage = new javax.swing.JPanel();
        lblSend = new javax.swing.JLabel();
        lblTittle = new javax.swing.JLabel();
        pnlSender = new javax.swing.JPanel();
        pnlItem1 = new javax.swing.JPanel();
        lblTime1 = new javax.swing.JLabel();
        lblMay1 = new javax.swing.JLabel();
        pnlItem2 = new javax.swing.JPanel();
        lblTime2 = new javax.swing.JLabel();
        lblMay2 = new javax.swing.JLabel();
        pnlItem3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblMay3 = new javax.swing.JLabel();
        pnlItem4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        lblMay4 = new javax.swing.JLabel();
        pnlTittle = new javax.swing.JPanel();
        lblTittle1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlMain.setBackground(new java.awt.Color(51, 51, 255));

        pnlMainMessage.setBackground(new java.awt.Color(153, 255, 153));

        pnlRegister.setBackground(new java.awt.Color(30, 30, 47));

        pnlZoneMessage.setBackground(new java.awt.Color(30, 30, 47));

        javax.swing.GroupLayout pnlZoneMessageLayout = new javax.swing.GroupLayout(pnlZoneMessage);
        pnlZoneMessage.setLayout(pnlZoneMessageLayout);
        pnlZoneMessageLayout.setHorizontalGroup(
            pnlZoneMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlZoneMessageLayout.setVerticalGroup(
            pnlZoneMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        lblSend.setForeground(new java.awt.Color(153, 255, 255));
        lblSend.setText("Send");

        lblTittle.setBackground(new java.awt.Color(255, 255, 255));
        lblTittle.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        lblTittle.setForeground(new java.awt.Color(153, 255, 255));
        lblTittle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTittle.setText("Máy X");

        javax.swing.GroupLayout pnlRegisterLayout = new javax.swing.GroupLayout(pnlRegister);
        pnlRegister.setLayout(pnlRegisterLayout);
        pnlRegisterLayout.setHorizontalGroup(
            pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRegisterLayout.createSequentialGroup()
                .addComponent(txtText, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSend)
                .addGap(13, 13, 13))
            .addComponent(pnlZoneMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlRegisterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTittle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlRegisterLayout.setVerticalGroup(
            pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRegisterLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lblTittle)
                .addGap(28, 28, 28)
                .addComponent(pnlZoneMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1)
                .addGroup(pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtText, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSend)))
        );

        javax.swing.GroupLayout pnlMainMessageLayout = new javax.swing.GroupLayout(pnlMainMessage);
        pnlMainMessage.setLayout(pnlMainMessageLayout);
        pnlMainMessageLayout.setHorizontalGroup(
            pnlMainMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainMessageLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlRegister, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        pnlMainMessageLayout.setVerticalGroup(
            pnlMainMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainMessageLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlRegister, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        pnlSender.setBackground(new java.awt.Color(153, 255, 255));

        pnlItem1.setBackground(new java.awt.Color(30, 30, 47));

        lblTime1.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblTime1.setForeground(new java.awt.Color(153, 255, 255));
        lblTime1.setText("HH:mm");

        lblMay1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblMay1.setForeground(new java.awt.Color(153, 255, 255));
        lblMay1.setText("MÁY X: Order mới");

        javax.swing.GroupLayout pnlItem1Layout = new javax.swing.GroupLayout(pnlItem1);
        pnlItem1.setLayout(pnlItem1Layout);
        pnlItem1Layout.setHorizontalGroup(
            pnlItem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlItem1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lblMay1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTime1)
                .addContainerGap())
        );
        pnlItem1Layout.setVerticalGroup(
            pnlItem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlItem1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlItem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMay1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTime1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlItem2.setBackground(new java.awt.Color(30, 30, 47));

        lblTime2.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblTime2.setForeground(new java.awt.Color(153, 255, 255));
        lblTime2.setText("HH:mm");

        lblMay2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblMay2.setForeground(new java.awt.Color(153, 255, 255));
        lblMay2.setText("MÁY X: Order mới");

        javax.swing.GroupLayout pnlItem2Layout = new javax.swing.GroupLayout(pnlItem2);
        pnlItem2.setLayout(pnlItem2Layout);
        pnlItem2Layout.setHorizontalGroup(
            pnlItem2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlItem2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lblMay2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTime2)
                .addContainerGap())
        );
        pnlItem2Layout.setVerticalGroup(
            pnlItem2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlItem2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlItem2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMay2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTime2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlItem3.setBackground(new java.awt.Color(30, 30, 47));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 255, 255));
        jLabel5.setText("HH:mm");

        lblMay3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblMay3.setForeground(new java.awt.Color(153, 255, 255));
        lblMay3.setText("MÁY X: Order mới");

        javax.swing.GroupLayout pnlItem3Layout = new javax.swing.GroupLayout(pnlItem3);
        pnlItem3.setLayout(pnlItem3Layout);
        pnlItem3Layout.setHorizontalGroup(
            pnlItem3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlItem3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lblMay3, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addContainerGap())
        );
        pnlItem3Layout.setVerticalGroup(
            pnlItem3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlItem3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlItem3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMay3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlItem4.setBackground(new java.awt.Color(30, 30, 47));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(153, 255, 255));
        jLabel9.setText("HH:mm");

        lblMay4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblMay4.setForeground(new java.awt.Color(153, 255, 255));
        lblMay4.setText("MÁY X: Order mới");

        javax.swing.GroupLayout pnlItem4Layout = new javax.swing.GroupLayout(pnlItem4);
        pnlItem4.setLayout(pnlItem4Layout);
        pnlItem4Layout.setHorizontalGroup(
            pnlItem4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlItem4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lblMay4, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addContainerGap())
        );
        pnlItem4Layout.setVerticalGroup(
            pnlItem4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlItem4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlItem4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMay4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlTittle.setBackground(new java.awt.Color(30, 30, 47));

        lblTittle1.setBackground(new java.awt.Color(255, 255, 255));
        lblTittle1.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        lblTittle1.setForeground(new java.awt.Color(153, 255, 255));
        lblTittle1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTittle1.setText("CHAT");

        javax.swing.GroupLayout pnlTittleLayout = new javax.swing.GroupLayout(pnlTittle);
        pnlTittle.setLayout(pnlTittleLayout);
        pnlTittleLayout.setHorizontalGroup(
            pnlTittleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(pnlTittleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlTittleLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblTittle1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        pnlTittleLayout.setVerticalGroup(
            pnlTittleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 84, Short.MAX_VALUE)
            .addGroup(pnlTittleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlTittleLayout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addComponent(lblTittle1)
                    .addContainerGap(31, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout pnlSenderLayout = new javax.swing.GroupLayout(pnlSender);
        pnlSender.setLayout(pnlSenderLayout);
        pnlSenderLayout.setHorizontalGroup(
            pnlSenderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSenderLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(pnlSenderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlItem1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlItem2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlItem3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlItem4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlTittle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2, 2, 2))
        );
        pnlSenderLayout.setVerticalGroup(
            pnlSenderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSenderLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlTittle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(pnlItem1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(pnlItem2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(pnlItem3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(pnlItem4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(168, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlSender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(pnlMainMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlMainMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlSender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                new AD_Chat().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblMay1;
    private javax.swing.JLabel lblMay2;
    private javax.swing.JLabel lblMay3;
    private javax.swing.JLabel lblMay4;
    private javax.swing.JLabel lblSend;
    private javax.swing.JLabel lblTime1;
    private javax.swing.JLabel lblTime2;
    private javax.swing.JLabel lblTittle;
    private javax.swing.JLabel lblTittle1;
    private javax.swing.JPanel pnlItem1;
    private javax.swing.JPanel pnlItem2;
    private javax.swing.JPanel pnlItem3;
    private javax.swing.JPanel pnlItem4;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlMainMessage;
    private javax.swing.JPanel pnlRegister;
    private javax.swing.JPanel pnlSender;
    private javax.swing.JPanel pnlTittle;
    private javax.swing.JPanel pnlZoneMessage;
    private javax.swing.JTextField txtText;
    // End of variables declaration//GEN-END:variables
}
