package ViewC.Code;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Controller.DBConnection;

public class C2_ThoiGianConLai {

    public static long getThoiGianConLaiPhut(int idAccount, int idComputer) {
        long conLai = 0;

        try {
            double soDuKhaDung = C2_SoDuKhaDung.getSoDuKhaDung(idAccount);
            double pricePerMin = C2_ChiPhiGio.getGiaTheoMay(idComputer);

            if (pricePerMin > 0) {
                conLai = (long) (soDuKhaDung / pricePerMin);
            }

        } catch (Exception e) {
            System.out.println("Lỗi C2_ThoiGianConLai: " + e.getMessage());
        }

        return conLai;
    }
}
