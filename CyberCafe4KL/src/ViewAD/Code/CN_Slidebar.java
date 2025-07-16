package ViewAD.Code;

import ViewAD.View.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class CN_Slidebar {

    // Gọi 1 hàm duy nhất để vừa set icon vừa gắn sự kiện
    public static void SetSlidebar(
            JLabel lblDM, JLabel lblOrder, JLabel lblSP, JLabel lblMT,
            JLabel lblHD, JLabel lblTKe, JLabel lblTKhoan,
            JLabel lblDMK, JLabel lblDX, JLabel lblChat, JLabel lblTB,
            JFrame currentFrame
    ) {
        // Set icon
        setIcon(lblDM, "icDM.png", " ĐẶT MÁY", 32);
        setIcon(lblOrder, "icOrder.png", " ORDER", 32);
        setIcon(lblSP, "icSP.png", " SẢN PHẨM", 32);
        setIcon(lblHD, "icHD1.png", " HÓA ĐƠN", 32);
        setIcon(lblMT, "icMT.png", " MÁY TÍNH", 32);
        setIcon(lblTKe, "icTKe.png", " THỐNG KÊ", 32);
        setIcon(lblTKhoan, "icTKhoan.png", " TÀI KHOẢN", 32);
        setIcon(lblDMK, "icDMK.png", " Đổi mật khẩu", 18);
        setIcon(lblDX, "icDX.png", " Đăng xuất", 20);
        setIcon(lblChat, "icChat.png", "", 24);
        setIcon(lblTB, "icTB.png", "", 24);

        // Gán sự kiện
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
                new AD_TAB3_QLSP().setVisible(true);
            }
        });

        lblMT.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                currentFrame.dispose();
                new AD_TAB4_QLMT().setVisible(true);
            }
        });

        lblHD.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                currentFrame.dispose();
                new AD_TAB5_QLHD().setVisible(true);
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
                new AD_TAB7_QLNV().setVisible(true);
            }
        });

        lblDMK.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                currentFrame.dispose();
                new AD_ChangePW().setVisible(true);
            }
        });

        lblDX.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int x = JOptionPane.showConfirmDialog(currentFrame, "Bạn chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (x == JOptionPane.YES_OPTION) {
                    currentFrame.dispose();
                    new AD_LoginForm().setVisible(true);
                }
            }
        });
    }

    // Hàm gán icon + text + kích thước
    private static void setIcon(JLabel label, String iconName, String text, int size) {
        String path = "/Assets/Icons/DM/" + iconName;
        URL url = CN_Slidebar.class.getResource(path);
        if (url == null) {
            System.err.println("Không tìm thấy icon: " + path);
            return;
        }

        ImageIcon icon = new ImageIcon(url);
        Image img = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(img));
        label.setText(text);
        label.setHorizontalTextPosition(SwingConstants.RIGHT);
        label.setVerticalTextPosition(SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(180, 255, 255));
    }
}
