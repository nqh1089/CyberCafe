package ViewC.View;

import ViewC.Code.CN_BienToanCuc;
import ViewC.Code.CN_LayTenMayTheoIP;
import ViewC.Code.CN_LoginMay;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.SwingConstants;

import java.sql.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class C1_GiaoDienBaoTri extends javax.swing.JFrame {

    private JLabel lblMay;
    private JPanel panelLogin;
    private Timer dongHo, timer15s;

    public C1_GiaoDienBaoTri() {
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
Timer timerCheckBack = new Timer(5000, new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String tenMay = CN_BienToanCuc.TenMay;

            Connection conn = Controller.DBConnection.getConnection();

            String sql = "SELECT ComputerStatus FROM Computer WHERE NameComputer = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tenMay);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int status = rs.getInt("ComputerStatus");
                if (status != 2) {
                    ((Timer) e.getSource()).stop(); // Dừng timer
                    dispose(); // Đóng giao diện bảo trì
                    new C1_GiaoDienCho().setVisible(true); // Mở lại giao diện chờ
                }
            }

            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
});
timerCheckBack.start();

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

//    private void showFullScreenDialog(String message) {
//        JWindow dialog = new JWindow(this);
//        dialog.setLayout(new BorderLayout());
//
//        JPanel content = new JPanel(new BorderLayout());
//        content.setBackground(new Color(40, 40, 60));
//        content.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
//
//        JLabel lbl = new JLabel(message, SwingConstants.CENTER);
//        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
//        lbl.setForeground(Color.WHITE);
//        lbl.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//
//        JButton btn = new JButton("OK");
//        btn.setFocusPainted(false);
//        btn.setBackground(new Color(0, 200, 200));
//        btn.setForeground(Color.BLACK);
//        btn.addActionListener(e -> dialog.dispose());
//
//        content.add(lbl, BorderLayout.CENTER);
//        content.add(btn, BorderLayout.SOUTH);
//
//        dialog.setContentPane(content);
//        dialog.setSize(350, 150);
//        dialog.setLocationRelativeTo(this);
//        dialog.setAlwaysOnTop(true);
//        dialog.setVisible(true);
//    }

    class BackgroundPanel extends JPanel {

        private final ImageIcon gifIcon;

        public BackgroundPanel() {
            String fileName = "BaoTri.jpg";
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

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new C1_GiaoDienBaoTri().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables


}
