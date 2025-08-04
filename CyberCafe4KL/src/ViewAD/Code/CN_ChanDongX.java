package ViewAD.Code;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CN_ChanDongX {

    public static void ChanDongX(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (CN_TaiKhoanDangNhap.getIDTaiKhoan() != -1) {
                    JOptionPane.showMessageDialog(frame, "Vui lòng đăng xuất trước khi thoát.");
                } else {
                    frame.dispose(); // cho tắt nếu chưa login
                }
            }
        });
    }
}
