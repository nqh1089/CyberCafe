package ViewC.Code;

import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class C2_ClientIcons {

    public static void LoadIcons(JPanel pnlCN) {
        String[] iconFiles = { "Chat.png", "Order.png", "DX.png", "DMK.png", "KM.png" };
        String[] captions = { "Chat", "Order", "Đăng xuất", "Mật khẩu", "Khóa máy" };

        pnlCN.removeAll();
        pnlCN.setLayout(new BoxLayout(pnlCN, BoxLayout.Y_AXIS));
        pnlCN.setBackground(new Color(30, 30, 47));
        pnlCN.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0)); //Khoảng cách giữa 3icon ở row1
        row1.setBackground(new Color(30, 30, 47));

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        row2.setBackground(new Color(30, 30, 47));

        JPanel row2Wrapper = new JPanel(new BorderLayout());
        row2Wrapper.setBackground(new Color(30, 30, 47));
        row2Wrapper.setMaximumSize(new Dimension(300, 40)); // Giảm chiều cao xuống
        row2Wrapper.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0)); // Đẩy sát lên hàng trên // top, left, bottom, right
        row2Wrapper.add(row2, BorderLayout.CENTER);

        for (int i = 0; i < iconFiles.length; i++) {
            JPanel itemPanel = new JPanel();
            itemPanel.setPreferredSize(new Dimension(60, 69)); //Khoảng cách giãn dòng (Giữa dòng 1 và 2) //Chỉnh 69
            itemPanel.setBackground(new Color(30, 30, 47));
            itemPanel.setLayout(new BorderLayout());

            URL url = C2_ClientIcons.class.getResource("/Assets/Client/" + iconFiles[i]);
            if (url != null) {
                ImageIcon rawIcon = new ImageIcon(url);
                Image scaled = rawIcon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaled);

                JLabel lbl = new JLabel(captions[i], icon, JLabel.CENTER);
                lbl.setHorizontalTextPosition(JLabel.CENTER);
                lbl.setVerticalTextPosition(JLabel.BOTTOM);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
                itemPanel.add(lbl, BorderLayout.CENTER);
            } else {
                System.err.println("Không tìm thấy icon: " + iconFiles[i]);
            }

            if (i < 3) {
                row1.add(itemPanel);
            } else {
                row2.add(itemPanel);
            }
        }

        pnlCN.add(row1);
        pnlCN.add(row2Wrapper);
        pnlCN.revalidate();
        pnlCN.repaint();
    }
}
