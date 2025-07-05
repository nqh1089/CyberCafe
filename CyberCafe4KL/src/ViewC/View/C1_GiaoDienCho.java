package ViewC.View;

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

        // Panel nền có GIF
        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setLayout(null);
        this.setContentPane(bgPanel);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int W = screen.width;
        int H = screen.height;

        // Label máy + thời gian (góc phải trên)
        lblMay = new JLabel("MÁY 01, ĐANG LẤY THỜI GIAN...");
        lblMay.setFont(new Font("Monospaced", Font.PLAIN, 18));
        lblMay.setForeground(Color.CYAN);
        lblMay.setSize(400, 30);
        lblMay.setHorizontalAlignment(SwingConstants.RIGHT);
        lblMay.setLocation(W - lblMay.getWidth() - 30, 20);
        bgPanel.add(lblMay);
        CapNhatThoiGian();

        // Panel login
        panelLogin = new JPanel(null);
        panelLogin.setBounds(W / 2 - 200, H / 2 - 150, 400, 300);
        panelLogin.setBackground(new Color(30, 30, 47));
        panelLogin.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 3, true)); // Viền neon
        panelLogin.setVisible(false);
        bgPanel.add(panelLogin);

        KhoiTaoLogin(panelLogin);

        // Click màn hình → hiện login
        bgPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HienThiLogin();
            }
        });

        // Full màn hình
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
        String tenMay = "MÁY 01"; // Có thể lấy từ DB

        dongHo = new Timer(1000, e -> {
            String timeStr = sdf.format(new Date());
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
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUser.setForeground(Color.WHITE);
        lblUser.setBounds(100, 60, 200, 20);
        panelLogin.add(lblUser);

        JTextField txtUser = new JTextField();
        txtUser.setBounds(100, 80, 200, 30);
        panelLogin.add(txtUser);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
            String user = txtUser.getText();
            String pass = new String(txtPass.getPassword());

            if (user.equals("admin") && pass.equals("123")) {
                if (timer15s != null) {
                    timer15s.stop();
                }
                panelLogin.setVisible(false);
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
                // TODO: mở giao diện chính
            } else {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu");
            }
        });

        // Nếu gõ phím → reset 15s
        KeyAdapter resetTimer = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                Reset15sTimer();
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

        timer15s = new Timer(15000, e -> {
            panelLogin.setVisible(false);
        });

        timer15s.setRepeats(false);
        timer15s.start();
    }

    private void Reset15sTimer() {
        if (timer15s != null && timer15s.isRunning()) {
            timer15s.restart();
        }
    }

    // Panel nền dùng ảnh GIF
    class BackgroundPanel extends JPanel {

        private final ImageIcon gifIcon;

        public BackgroundPanel() {
            gifIcon = new ImageIcon("J:/SU25/BL2/img/0701.gif");
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); 
            if (gifIcon != null) {
                Graphics2D g2d = (Graphics2D) g;

                // Bật khử răng cưa, làm mượt khi phóng to
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
        java.awt.EventQueue.invokeLater(() -> {
            new C1_GiaoDienCho(); // KHÔNG cần gọi setVisible nữa
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
