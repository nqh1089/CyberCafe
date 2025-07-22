// File: ViewAD/View/AD_Chat.java
package ViewAD.View;

import Socket.CN_ChatAdmin; // Đảm bảo import đúng package
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.SwingUtilities; // Để cập nhật UI an toàn

public class AD_Chat extends javax.swing.JFrame {

    // Constructor của AD_Chat
    public AD_Chat() {
        initComponents(); // Giữ nguyên initComponents nếu bạn có các thành phần khác trên JFrame

        setTitle("CyberCafe4KL_Admin Chat");
        setResizable(false);
        // Thay đổi DefaultCloseOperation để xử lý việc đóng server một cách an toàn
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        // --- Bắt đầu tích hợp CN_ChatAdmin ---
        // Set thông tin Admin trước khi hiển thị giao diện.
        // Bạn cần thay thế giá trị này bằng ID và tên Admin thực tế sau khi đăng nhập.
        // Ví dụ: Lấy từ biến toàn cục hoặc một phương thức getter sau khi đăng nhập thành công.
        // Đây là ví dụ, bạn hãy thay đổi 1 và "AD_Test" bằng ID và tên tài khoản Admin thực tế của bạn.
        CN_ChatAdmin.SetAdminInfo(1, "Admin"); 

        // Gọi CN_ChatAdmin để vẽ lên pnlMain
        CN_ChatAdmin.HienThiGiaoDien(pnlMain);
        // --- Kết thúc tích hợp CN_ChatAdmin ---
        
        // Thêm WindowListener để đóng server khi JFrame đóng
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("AD_Chat: Cửa sổ Admin Chat đang đóng, đang tiến hành tắt server chat...");
                // Gọi phương thức tĩnh DongServerChat() từ CN_ChatAdmin
                CN_ChatAdmin.DongServerChat(); 
                System.out.println("AD_Chat: Server chat đã tắt.");
            }
        });
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
