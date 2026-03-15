package ViewC.Code;

import Controller.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class C2_SoDuKhaDung {

    /**
     * Lấy số dư khả dụng của tài khoản (tính theo IDAccount)
     *
     * @param idAccount ID của tài khoản
     * @return số dư khả dụng, mặc định 0 nếu lỗi hoặc không tìm thấy
     */
    public static double getSoDuKhaDung(int idAccount) {
        double soDu = 0.0;
        double chiPhiGio = 0.0;
        int idMay = CN_BienToanCuc.IDComputer;

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                return 0.0;
            }

            // 1. Lấy số dư
            String sql = "SELECT Balance FROM Account WHERE IDAccount = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idAccount);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        soDu = rs.getDouble("Balance");
                    }
                }
            }

            // 2. Lấy thời gian sử dụng (phút)
            long usedMin = C2_ThoiGianSuDung.getThoiGianSuDungPhut(idMay, idAccount);

            // 3. Tính chi phí giờ
            chiPhiGio = C2_ChiPhiGio.getChiPhiGio(idMay, usedMin);

            // 4. Trừ chi phí giờ để ra số dư khả dụng
            soDu -= chiPhiGio;

            if (soDu < 0) {
                soDu = 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return soDu;
    }
}
