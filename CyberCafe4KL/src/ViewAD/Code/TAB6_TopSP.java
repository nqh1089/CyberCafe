package ViewAD.Code;

import Controller.DBConnection;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TAB6_TopSP {

    public static void LoadTable(JTable table, int thang) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        String sql = """
            SELECT FD.IDFood, FD.NameFood, 
                   SUM(OD.Quantity) AS SoLuong, 
                   SUM(OD.TotalPrice) AS DoanhThu
            FROM OrderDetail OD
            JOIN FoodDrink FD ON OD.IDFood = FD.IDFood
            JOIN OrderFood OFD ON OD.IDOrder = OFD.IDOrder
            WHERE MONTH(OFD.OrderTime) = ?
            GROUP BY FD.IDFood, FD.NameFood
            ORDER BY SoLuong DESC
        """;

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            DecimalFormat formatter = new DecimalFormat("#,###", symbols);

            ps.setInt(1, thang);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("IDFood");
                String ten = rs.getString("NameFood");
                int soLuong = rs.getInt("SoLuong");
                double doanhThu = rs.getDouble("DoanhThu");

                String doanhThuStr = formatter.format((long) doanhThu); // format tiền

                model.addRow(new Object[]{id, ten, soLuong, doanhThuStr});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
