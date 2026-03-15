package ViewAD.Code;

import Controller.DBConnection;
import java.sql.*;
import javax.swing.JLabel;

public class TAB6_TongLuotTruyCap {

    public static void Load(JLabel lblTongTruyCap, int thang) {
        String sql = "SELECT COUNT(*) AS SoLuot FROM LogAccess WHERE MONTH(ThoiGianBatDau) = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, thang);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                lblTongTruyCap.setText(String.valueOf(rs.getInt("SoLuot")));
            } else {
                lblTongTruyCap.setText("0");
            }

        } catch (Exception e) {
            e.printStackTrace();
            lblTongTruyCap.setText("0");
        }
    }
}
