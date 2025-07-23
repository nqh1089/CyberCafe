package ViewC.Code;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Controller.DBConnection;

public class C2_ThoiGianConLai {

    public static long getThoiGianConLaiPhut(int idAccount, int idComputer) {
        long conLai = 0;

        try (Connection conn = DBConnection.getConnection()) {
            // Lấy số dư
            String sqlBal = "SELECT Balance FROM Account WHERE IDAccount = ?";
            PreparedStatement psBal = conn.prepareStatement(sqlBal);
            psBal.setInt(1, idAccount);
            ResultSet rsBal = psBal.executeQuery();

            double balance = 0;
            if (rsBal.next()) {
                balance = rsBal.getDouble("Balance");
            }
            rsBal.close();
            psBal.close();

            // Lấy đơn giá máy
            String sqlPrice = "SELECT PricePerMinute FROM Computer WHERE IDComputer = ?";
            PreparedStatement psPrice = conn.prepareStatement(sqlPrice);
            psPrice.setInt(1, idComputer);
            ResultSet rsPrice = psPrice.executeQuery();

            double pricePerMin = 0;
            if (rsPrice.next()) {
                pricePerMin = rsPrice.getDouble("PricePerMinute");
            }
            rsPrice.close();
            psPrice.close();

            if (pricePerMin > 0) {
                conLai = (long) (balance / pricePerMin);
            }

        } catch (Exception e) {
            System.out.println("Lỗi C2_ThoiGianConLai: " + e.getMessage());
        }

        return conLai;
    }

}
