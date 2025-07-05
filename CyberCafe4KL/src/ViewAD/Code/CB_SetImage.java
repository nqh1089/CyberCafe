package ViewAD.Code;

import java.awt.*;
import javax.swing.*;

public class CB_SetImage {

    public static void SetPanelBackgroundTLQL(JPanel panel) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set kích thước đúng với ảnh
                int width = 318;
                int height = 150;
                panel.setPreferredSize(new Dimension(width, height));
                panel.setSize(width, height);  // ép kích thước

                // Load ảnh từ đường dẫn tuyệt đối
                ImageIcon icon = new ImageIcon("J:/SU25/BL2/img/imgTLQL.jpg");
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
