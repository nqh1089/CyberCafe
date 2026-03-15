package ViewC.Code;

import java.awt.*;
import javax.swing.*;

public class C2_SetImage {

    public static void SetPanelBackgroundTLQL(JPanel panel) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set kích thước đúng với ảnh
                int width = 330;
                int height = 150;
                panel.setPreferredSize(new Dimension(width, height));
                panel.setSize(width, height);  // ép kích thước

                // Ví dụ: tên file đã lấy từ DB
                String imgName = "imgTLQL.jpg"; // SELECT DuongDan FROM Assets_Anh WHERE TenAnh = 'TLQL'
                String path = "src/Assets/img/" + imgName;

                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                JLabel lbl = new JLabel(new ImageIcon(img));

                lbl.setBounds(0, 0, width, height);
                panel.removeAll();
                panel.setLayout(null);
                panel.add(lbl);
                panel.repaint();
                panel.revalidate();

                System.out.println("Gan anh thanh cong");
            } catch (Exception e) {
                System.out.println("Loi khi gan anh" + e.getMessage());
            }
        });
    }
}
