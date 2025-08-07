package ViewC.Code;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Controller.DBConnection;

public class C2_ChiPhiGio {

    // Hàm lấy đơn giá theo máy
    public static double getGiaTheoMay(int idComputer) {
        double pricePerMin = 0;

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT PricePerMinute FROM Computer WHERE IDComputer = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idComputer);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pricePerMin = rs.getDouble("PricePerMinute");
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            System.out.println("Lỗi getGiaTheoMay: " + e.getMessage());
        }

        return pricePerMin;
    }

    // Hàm tính chi phí thực tế
    public static double getChiPhiGio(int idComputer, long soPhut) {
        double pricePerMin = getGiaTheoMay(idComputer);
        return soPhut * pricePerMin;
    }
}
