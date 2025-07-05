package ViewAD.Code;

import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class TAB1_Slidebar {

    public static void SetLabelIcon(JLabel label, String iconName, String text) {
        String path = "/Assets/Icons/DM/" + iconName;
        URL url = TAB1_Slidebar.class.getResource(path);

        if (url == null) {
            System.err.println("Không tìm thấy icon: " + path);
            return;
        }

        ImageIcon icon = new ImageIcon(url);
        Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(img));
        label.setText(text);
        label.setHorizontalTextPosition(SwingConstants.RIGHT);
        label.setVerticalTextPosition(SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(180, 255, 255));
    }
}
