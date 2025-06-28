package View.Code;

import Controller.DAO;
import java.awt.BorderLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TAB2_QR extends javax.swing.JFrame {

    private String maHD;
    private String nguoiTao;
    private int tongTienSP, giamGia, thanhToan;
    private JTable tblChiTiet;

    public TAB2_QR(String maHD, String nguoiTao, int tongTienSP, int giamGia, int thanhToan, JTable tblChiTiet) {
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.maHD = maHD;
        this.nguoiTao = nguoiTao;
        this.tongTienSP = tongTienSP;
        this.giamGia = giamGia;
        this.thanhToan = thanhToan;
        this.tblChiTiet = tblChiTiet;

        AnhQR();
    }

    private void AnhQR() {
        try {
            ImageIcon icon = new ImageIcon("/Assets/Products/QR.png");
            Image img = icon.getImage().getScaledInstance(pnlQR.getWidth(), pnlQR.getHeight(), Image.SCALE_SMOOTH);
            JLabel lblQR = new JLabel(new ImageIcon(img));
            pnlQR.removeAll();
            pnlQR.setLayout(new BorderLayout());
            pnlQR.add(lblQR, BorderLayout.CENTER);
            pnlQR.revalidate();
            pnlQR.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể hiển thị mã QR.");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        btnDone = new javax.swing.JButton();
        pnlQR = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlMain.setBackground(new java.awt.Color(255, 248, 225));
        pnlMain.setMaximumSize(new java.awt.Dimension(400, 400));
        pnlMain.setMinimumSize(new java.awt.Dimension(400, 400));
        pnlMain.setPreferredSize(new java.awt.Dimension(400, 400));
        pnlMain.setRequestFocusEnabled(false);
        pnlMain.setVerifyInputWhenFocusTarget(false);
        pnlMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnDone.setText("Hoàn tất");
        btnDone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoneActionPerformed(evt);
            }
        });
        pnlMain.add(btnDone, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 340, 110, -1));

        pnlQR.setMaximumSize(new java.awt.Dimension(340, 340));
        pnlQR.setMinimumSize(new java.awt.Dimension(340, 340));

        javax.swing.GroupLayout pnlQRLayout = new javax.swing.GroupLayout(pnlQR);
        pnlQR.setLayout(pnlQRLayout);
        pnlQRLayout.setHorizontalGroup(
            pnlQRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );
        pnlQRLayout.setVerticalGroup(
            pnlQRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );

        pnlMain.add(pnlQR, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, 290));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoneActionPerformed

    }//GEN-LAST:event_btnDoneActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDone;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlQR;
    // End of variables declaration//GEN-END:variables
}
