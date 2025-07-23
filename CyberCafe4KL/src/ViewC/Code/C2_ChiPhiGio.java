package ViewC.Code;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Controller.DBConnection;

public class C2_ChiPhiGio {

    public static double getChiPhiGio(int idComputer, long soPhut) {
        double chiPhi = 0;

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT PricePerMinute FROM Computer WHERE IDComputer = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idComputer);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double pricePerMin = rs.getDouble("PricePerMinute");
                chiPhi = soPhut * pricePerMin;
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            System.out.println("Lỗi C2_ChiPhiGio: " + e.getMessage());
        }

        return chiPhi;
    }

}
