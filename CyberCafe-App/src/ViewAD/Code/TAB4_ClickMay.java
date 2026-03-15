package ViewAD.Code;

import Controller.DBConnection;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class TAB4_ClickMay {

    private JLabel lblTenMay, lblTrangThai, lblCPU, lblRAM, lblGPU, lblMonitor, lblGia;

    public TAB4_ClickMay(JLabel lblTenMay, JLabel lblTrangThai,
                         JLabel lblCPU, JLabel lblRAM, JLabel lblGPU,
                         JLabel lblMonitor, JLabel lblGia) {
        this.lblTenMay = lblTenMay;
        this.lblTrangThai = lblTrangThai;
        this.lblCPU = lblCPU;
        this.lblRAM = lblRAM;
        this.lblGPU = lblGPU;
        this.lblMonitor = lblMonitor;
        this.lblGia = lblGia;
    }

    public MouseAdapter getClickHandler(TAB4_LoadMayTinh.Computer may) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LoadThongTinMay(may.ten);
            }
        };
    }

    private void LoadThongTinMay(String tenMay) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Không thể kết nối CSDL");
                return;
            }

            String sql = "SELECT * FROM Computer WHERE NameComputer = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tenMay);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                lblTenMay.setText(rs.getString("NameComputer"));

                int status = rs.getInt("ComputerStatus");
                if (status == 1) {
                    lblTrangThai.setText("HOẠT ĐỘNG");
                } else if (status == 2) {
                    lblTrangThai.setText("BẢO TRÌ");
                } else {
                    lblTrangThai.setText("KHÔNG RÕ");
                }

                lblCPU.setText(rs.getString("CPU"));
                lblRAM.setText(rs.getString("RAM"));
                lblGPU.setText(rs.getString("GPU"));
                lblMonitor.setText(rs.getString("Monitor"));
                lblGia.setText((int) rs.getDouble("PricePerMinute") + " đ / phút");
            } else {
                System.out.println("Không tìm thấy thông tin máy: " + tenMay);
            }

        } catch (Exception ex) {
            System.out.println("Lỗi khi load thông tin máy: " + ex.getMessage());
        }
    }
}
