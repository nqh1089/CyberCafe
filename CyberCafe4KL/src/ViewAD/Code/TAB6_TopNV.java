package ViewAD.Code;

import Controller.DBConnection;
import java.sql.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class TAB6_TopNV {

    public static void LoadTable(JTable table, int thang) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        String sql = """
            SELECT A.IDAccount, A.NameAccount,
                   COUNT(DISTINCT OFD.IDOrder) AS SoHD,
                   SUM(OD.TotalPrice) AS DoanhThu
            FROM Account A
            JOIN OrderFood OFD ON A.IDAccount = OFD.IDAccount
            JOIN OrderDetail OD ON OFD.IDOrder = OD.IDOrder
            WHERE A.RoleAccount IN ('ADMIN', 'BOSS')
              AND MONTH(OFD.OrderTime) = ?
            GROUP BY A.IDAccount, A.NameAccount
            ORDER BY DoanhThu DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, thang);
            ResultSet rs = ps.executeQuery();

            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            DecimalFormat formatter = new DecimalFormat("#,###", symbols);

            while (rs.next()) {
                int id = rs.getInt("IDAccount");
                String ten = rs.getString("NameAccount");
                int soHD = rs.getInt("SoHD");
                double doanhThu = rs.getDouble("DoanhThu");

                model.addRow(new Object[]{
                    id,
                    ten,
                    soHD,
                    formatter.format((long) doanhThu)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
