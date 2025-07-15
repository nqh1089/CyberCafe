package ViewC.View;

import ViewC.Code.CN_LoginMay;
import ViewC.Code.CN_BienToanCuc;
import ViewC.Code.CN_LayTenMayTheoIP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class C1_GiaoDienCho extends JFrame {

    private JLabel lblMay;
    private JPanel panelLogin;
    private Timer dongHo, timer15s;

    public C1_GiaoDienCho() {
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

        bgPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HienThiLogin();
            }
        });

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
        } else {
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.setVisible(true);
        }
    }

    private void CapNhatThoiGian() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        dongHo = new Timer(1000, e -> {
            String timeStr = sdf.format(new Date());
            String tenMay = CN_BienToanCuc.TenMay.equals("") ? "MÁY ???" : CN_BienToanCuc.TenMay;
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

            boolean ketQua = CN_LoginMay.loginMay(txtUser.getText().trim(), new String(txtPass.getPassword()));

            if (ketQua) {
                if (timer15s != null) {
                    timer15s.stop();
                }

                // Reset form
                txtUser.setText("");
                txtPass.setText("");
                panelLogin.setVisible(false);

                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");

                // Mở menu client sau đăng nhập
                new C2_Menu().setVisible(true);
                this.dispose(); // Tắt giao diện chờ
            } else {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc máy không hợp lệ!");
            }
        });

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
    }

    private void HienThiLogin() {
        panelLogin.setVisible(true);
        if (timer15s != null && timer15s.isRunning()) {
            timer15s.stop();
        }
        timer15s = new Timer(15000, e -> panelLogin.setVisible(false));
        timer15s.setRepeats(false);
        timer15s.start();
    }

    class BackgroundPanel extends JPanel {

        private final ImageIcon gifIcon = new ImageIcon("E:/SU25/BL2/img/0701.gif");

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
        java.awt.EventQueue.invokeLater(() -> new C1_GiaoDienCho());
    }
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

