package View.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class C1_GiaoDienCho extends JFrame {

    public C1_GiaoDienCho() {
        // Phải gọi trước khi pack/hiển thị
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(new Color(30, 30, 47));

        // Label chính giữa màn hình
        JLabel lbl = new JLabel("VUI LÒNG LIÊN HỆ QUẦY ĐỂ BẬT MÁY");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setVerticalAlignment(SwingConstants.CENTER);

        this.setLayout(new BorderLayout());
        this.add(lbl, BorderLayout.CENTER);

        // Full screen (ẩn luôn taskbar nếu hệ thống hỗ trợ)
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this); // Tự động gọi setVisible(true)
        } else {
            this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Không ẩn taskbar
            this.setVisible(true);
        }
    }

    // Hàm thoát khỏi chế độ full màn hình (khi chuyển sang giao diện khác)
    public static void ThoatFullManHinh() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(null); // Trả JFrame về trạng thái bình thường
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
public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new C1_GiaoDienCho(); // KHÔNG cần gọi setVisible nữa
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
