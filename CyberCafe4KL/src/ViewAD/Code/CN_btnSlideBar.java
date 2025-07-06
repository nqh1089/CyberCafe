package ViewAD.Code;

import ViewAD.View.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CN_btnSlideBar {

    public static void ganSuKienSlideBar(
        JLabel lblDM,
        JLabel lblOrder,
        JLabel lblSP,
        JLabel lblMT,
        JLabel lblHD,
        JLabel lblTKe,
        JLabel lblTKhoan,
        JLabel lblDX,
        JFrame currentFrame
    ) {

        lblDM.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                currentFrame.dispose();
                new AD_TAB1_DatMay().setVisible(true);
            }
        });

        lblOrder.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                currentFrame.dispose();
                new AD_TAB2_Order().setVisible(true);
            }
        });

        lblSP.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                currentFrame.dispose();
                new AD_TAB3_QLSP().setVisible(true); // Quản lý sản phẩm
            }
        });

        lblMT.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                currentFrame.dispose();
                new AD_TAB4_QLMT().setVisible(true); // Quản lý máy tính
            }
        });

        lblHD.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                currentFrame.dispose();
                new AD_TAB5_QLHD().setVisible(true); // Quản lý hóa đơn
            }
        });

        lblTKe.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                currentFrame.dispose();
                new AD_TAB6_ThongKe().setVisible(true);
            }
        });

        lblTKhoan.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                currentFrame.dispose();
                new AD_TAB7_QLNV().setVisible(true); // Quản lý tài khoản
            }
        });

        lblDX.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int x = JOptionPane.showConfirmDialog(currentFrame, "Bạn chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (x == JOptionPane.YES_OPTION) {
                    currentFrame.dispose();
                    new AD_C_LoginForm().setVisible(true);
                }
            }
        });
    }
}
