// File: ViewAD/View/AD_Chat.java
package ViewAD.View;

import Socket.CN_ChatAdmin;
import ViewC.Code.CN_BienToanCuc;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities; // Để cập nhật UI an toàn

public class AD_Chat extends javax.swing.JFrame {
    // Chạy 1 form duy nhất
    private static AD_Chat instance;

    public AD_Chat() {
        initComponents();
        setTitle("CyberCafe4KL_Admin Chat");
        setResizable(false);
        
        // Đặt instance khi khởi tạo lần đầu
        instance = this;

        // k cho đóng form (Vô hiệu hoá nút X)
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        // --- Bắt đầu tích hợp CN_ChatAdmin ---
        // Set thông tin Admin trước khi hiển thị giao diện.
        CN_ChatAdmin.SetAdminInfo(1, "Admin");

        // Gọi CN_ChatAdmin để vẽ lên pnlMain
        CN_ChatAdmin.HienThiGiaoDien(pnlMain);
        // --- Kết thúc tích hợp CN_ChatAdmin ---
    }

    public static void showChat() {
        if (instance == null) {
            instance = new AD_Chat();
        }
        instance.setVisible(true);
        instance.setState(JFrame.NORMAL); // Nếu đang thu nhỏ thì bật lại
        instance.toFront(); // Đưa ra trước các cửa sổ khác
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlMain.setBackground(new java.awt.Color(30, 30, 47));

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 615, Short.MAX_VALUE)
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 436, Short.MAX_VALUE)
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
    private javax.swing.JPanel pnlMain;
    // End of variables declaration//GEN-END:variables
}
