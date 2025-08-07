package ViewC.Code;

import Controller.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class C2_ChiPhiDichVu {

    public static double layTongTienOrderClient(String tenMay, Timestamp thoiGianBatDau) {
        double tongTien = 0;

        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                SELECT SUM(od.TotalPrice)
                FROM OrderFood o
                JOIN OrderDetail od ON o.IDOrder = od.IDOrder
                WHERE o.Note = ? AND o.OrderTime >= ?
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, tenMay);
            stmt.setTimestamp(2, thoiGianBatDau);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tongTien = rs.getDouble(1);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
        }

        return tongTien;
    }
}
