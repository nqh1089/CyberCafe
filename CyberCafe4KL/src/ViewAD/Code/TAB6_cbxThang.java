package ViewAD.Code;

import Controller.DBConnection;
import java.sql.*;
import javax.swing.*;

public class TAB6_cbxThang {

    public static void LoadThangPhatSinh(JComboBox<String> cbxThang) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT DISTINCT MONTH(ThoiGianBatDau) FROM LogAccess\n"
                       + "UNION SELECT DISTINCT MONTH(OrderTime) FROM OrderFood\n"
                       + "UNION SELECT DISTINCT MONTH(CreateAt) FROM Invoice\n"
                       + "UNION SELECT DISTINCT MONTH(StartTime) FROM ComputerUsage\n"
                       + "ORDER BY 1";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            cbxThang.removeAllItems();
            while (rs.next()) {
                int thang = rs.getInt(1);
                cbxThang.addItem("Tháng " + thang);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getThangFromComboBox(JComboBox<String> cbxThang) {
        String selected = (String) cbxThang.getSelectedItem();
        if (selected == null) return -1;
        return Integer.parseInt(selected.replaceAll("\\D", ""));
    }
}
