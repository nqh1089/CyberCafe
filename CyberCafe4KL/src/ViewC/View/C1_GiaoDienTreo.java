package ViewC.View;

import Controller.DBConnection;
import ViewC.Code.CN_LoginMay;
import ViewC.Code.CN_BienToanCuc;
import ViewC.Code.CN_LayTenMayTheoIP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class C1_GiaoDienTreo extends JFrame {

    private JLabel lblMay;
    private JPanel panelLogin;
    private Timer dongHo, timer15s;

    public C1_GiaoDienTreo() {
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String ip = CN_LayTenMayTheoIP.getIPRadminHienTai();

        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setLayout(null);
        this.setContentPane(bgPanel);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int W = screen.width;
        int H = screen.height;

        // Label hiển thị tên máy + thời gian
        lblMay = new JLabel("ĐANG XÁC ĐỊNH MÁY...");
        lblMay.setFont(new Font("Monospaced", Font.PLAIN, 18));
        lblMay.setForeground(Color.CYAN);
        lblMay.setSize(400, 30);
        lblMay.setHorizontalAlignment(SwingConstants.RIGHT);
        lblMay.setLocation(W - lblMay.getWidth() - 30, 20);
        bgPanel.add(lblMay);
        CapNhatThoiGian();

        // Panel login nổi
        panelLogin = new JPanel(null);
        panelLogin.setBounds(W / 2 - 200, H / 2 - 150, 400, 300);
        panelLogin.setBackground(new Color(30, 30, 47));
        panelLogin.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 3, true));
        panelLogin.setVisible(false);
        bgPanel.add(panelLogin);
        KhoiTaoLogin(panelLogin);

        Timer timerCheckStatus = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String tenMay = CN_BienToanCuc.TenMay; // ✅ lấy từ biến chuẩn

                    Connection conn = DBConnection.getConnection();
                    String sql = "SELECT ComputerStatus FROM Computer WHERE NameComputer = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, tenMay);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        int status = rs.getInt("ComputerStatus");
                        if (status == 2) {
                            ((Timer) e.getSource()).stop(); // Dừng chính Timer này
                            dispose(); // đóng GiaoDiệnCho
                            new C1_GiaoDienBaoTri().setVisible(true); // ✅ hiện GiaoDiệnBảoTrì
                        }
                    }

                    conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        timerCheckStatus.start();

        // Click nền để hiện login
        bgPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HienThiLogin();
            }
        });

        // Fullscreen
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
        } else {
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.setVisible(true);
        }

        // Nếu đăng xuất về lại mà mất tên máy thì khôi phục lại
        if (CN_BienToanCuc.TenMay == null || CN_BienToanCuc.TenMay.isEmpty()) {
            CN_BienToanCuc.TenMay = ViewC.Code.CN_XacDinhMayClient.getTenMayClient();
        }

    }

    private void CapNhatThoiGian() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

        dongHo = new Timer(1000, e -> {
            // Nếu tên máy đang trống hoặc null -> lấy lại từ IP
            if (CN_BienToanCuc.TenMay == null || CN_BienToanCuc.TenMay.isEmpty()) {
                CN_BienToanCuc.TenMay = ViewC.Code.CN_XacDinhMayClient.getTenMayClient();
            }

            String timeStr = sdf.format(new Date());
            String tenMay = CN_BienToanCuc.TenMay != null ? CN_BienToanCuc.TenMay : "MÁY ???";
            lblMay.setText(tenMay + ", " + timeStr);
        });

        dongHo.start();
    }

    private void KhoiTaoLogin(JPanel panelLogin) {
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(130, 20, 200, 30);
        panelLogin.add(lblTitle);

        JLabel lblUser = new JLabel("User name:");
        lblUser.setForeground(Color.WHITE);
        lblUser.setBounds(100, 60, 200, 20);
        panelLogin.add(lblUser);

        JTextField txtUser = new JTextField();
        txtUser.setBounds(100, 80, 200, 30);
        panelLogin.add(txtUser);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(Color.WHITE);
        lblPass.setBounds(100, 115, 200, 20);
        panelLogin.add(lblPass);

        JPasswordField txtPass = new JPasswordField();
        txtPass.setBounds(100, 135, 200, 30);
        panelLogin.add(txtPass);

        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setBounds(140, 190, 120, 35);
        panelLogin.add(btnLogin);

        btnLogin.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword());

            // Nếu nhập sai tài khoản so với người đang dùng trước đó
            if (!user.equalsIgnoreCase(CN_BienToanCuc.TenTaiKhoan)) {
                showFullScreenDialog("Chỉ tài khoản '" + CN_BienToanCuc.TenTaiKhoan + "' mới có thể mở khóa máy");
                resetFormLogin();
                return;
            }

            boolean ketQua = CN_LoginMay.loginMay(user, pass);

            // Luôn reset form login dù đúng hay sai
            resetFormLogin();

            if (ketQua) {
                if (timer15s != null) {
                    timer15s.stop();
                }
                panelLogin.setVisible(false);

                showFullScreenDialog("Đăng nhập thành công!");

                new C2_Menu().setVisible(true);
                C2_Chat.showChat();
                C1_GiaoDienTreo.this.dispose();
            } else {
                showFullScreenDialog("Sai tài khoản hoặc máy không hợp lệ!");
            }
        });

        // Nếu gõ phím → reset timer 15s
        KeyAdapter resetTimer = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (timer15s != null && timer15s.isRunning()) {
                    timer15s.restart();
                }
            }
        };
        txtUser.addKeyListener(resetTimer);
        txtPass.addKeyListener(resetTimer);
        panelLogin.getRootPane().setDefaultButton(btnLogin); // Gán phím enter (Enter = Login)
    }

    private void HienThiLogin() {
        panelLogin.setVisible(true);

        if (timer15s != null && timer15s.isRunning()) {
            timer15s.stop();
        }

        timer15s = new Timer(15000, e -> {
            panelLogin.setVisible(false);
            resetFormLogin();  // Reset sau khi auto ẩn
        });

        timer15s.setRepeats(false);
        timer15s.start();
    }

    private void resetFormLogin() {
        for (Component comp : panelLogin.getComponents()) {
            if (comp instanceof JTextField) {
                ((JTextField) comp).setText("");
            }
            if (comp instanceof JPasswordField) {
                ((JPasswordField) comp).setText("");
            }
        }
    }

    private void showFullScreenDialog(String message) {
        JDialog dialog = new JDialog(this, "Thông báo", true); // dùng true để modal (chắn tương tác)
        dialog.setUndecorated(true); // giống JWindow
        dialog.setLayout(new BorderLayout());

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(40, 40, 60));
        content.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));

        JLabel lbl = new JLabel(message, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lbl.setForeground(Color.WHITE);
        lbl.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btn = new JButton("OK");
        btn.setFocusPainted(false);
        btn.setBackground(new Color(0, 200, 200));
        btn.setForeground(Color.BLACK);
        btn.addActionListener(e -> dialog.dispose());

        // Gán ENTER và ESC để đóng dialog
        dialog.getRootPane().setDefaultButton(btn); // ENTER = OK
        dialog.getRootPane().registerKeyboardAction(e -> dialog.dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        content.add(lbl, BorderLayout.CENTER);
        content.add(btn, BorderLayout.SOUTH);

        dialog.setContentPane(content);
        dialog.setSize(350, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    class BackgroundPanel extends JPanel {

        private final ImageIcon gifIcon;

        public BackgroundPanel() {
            String fileName = "TreoMay.jpg"; // SELECT DuongDan FROM Assets_Anh WHERE TenAnh = 'GiaoDienTreo'
            String path = "src/Assets/img/" + fileName;
            gifIcon = new ImageIcon(path);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (gifIcon != null) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.drawImage(gifIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1536, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 864, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new C1_GiaoDienTreo());
    }
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

