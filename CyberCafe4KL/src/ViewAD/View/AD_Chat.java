package ViewAD.View;

import Socket.CM_CN_ChatManager;
import Socket.CM_CN_OnlineClientInfo;
import Socket.CN_ChatAdmin; // Import lớp CN_ChatAdmin từ package mới của bạn

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AD_Chat extends javax.swing.JFrame {

    public AD_Chat() {
        initComponents(); // Giữ nguyên initComponents nếu bạn có các thành phần khác trên JFrame

        setTitle("CyberCafe4KL_Admin Chat");
        setResizable(false);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        // Gọi phương thức static HienThiGiaoDien của CN_ChatAdmin để vẽ lên pnlMain
        CN_ChatAdmin.HienThiGiaoDien(pnlMain);

        // Gán callback cho CN_ChatAdmin để nó có thể thông báo khi Admin gửi tin nhắn
        CN_ChatAdmin.onSendMessageFromUI = this::sendAdminMessageToSelectedClient;

        // Khởi tạo CM_CN_ChatManager và truyền các callbacks
        // CM_CN_ChatManager sẽ gọi các phương thức static của CN_ChatAdmin để cập nhật UI
        CM_CN_ChatManager.initializeChatSystem(
                CN_ChatAdmin::addOrUpdateClient, // Khi có client online, CN_ChatAdmin cập nhật UI
                CN_ChatAdmin::removeClient, // Khi client offline, CN_ChatAdmin xóa khỏi UI
                this::handleNewMessageFromClient // Khi có tin nhắn mới, AD_Chat xử lý rồi gửi cho CN_ChatAdmin
        );

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CM_CN_ChatManager.shutdownChatSystem();
            }
        });

        pack(); // Đóng gói lại frame sau khi tất cả các panel đã được thêm và vẽ
        setLocationRelativeTo(null); // Đặt frame ra giữa màn hình
    }

    // Callback xử lý tin nhắn mới từ client, được gọi bởi CM_CN_ChatManager
    private void handleNewMessageFromClient(String[] messageData) {
        String senderCompName = messageData[0]; // Tên máy gửi
        String content = messageData[1];       // Nội dung
        String time = messageData[2];          // Thời gian
        // Gọi phương thức static hiển thị tin nhắn của CN_ChatAdmin
        CN_ChatAdmin.displayNewMessage(senderCompName, content, time);
    }

    // Callback để gửi tin nhắn từ AD_Chat UI đến CM_CN_ChatManager
    private void sendAdminMessageToSelectedClient(String messageContent) {
        int targetComputerID = CN_ChatAdmin.getSelectedClientComputerId();
        if (targetComputerID != -1) {
            CM_CN_ChatManager.sendAdminMessageToClient(targetComputerID, messageContent);
        } else {
            JOptionPane.showMessageDialog(this, "Không có máy khách nào được chọn để gửi tin nhắn.", "Lỗi", JOptionPane.WARNING_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlMain.setBackground(new java.awt.Color(51, 51, 255));

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
