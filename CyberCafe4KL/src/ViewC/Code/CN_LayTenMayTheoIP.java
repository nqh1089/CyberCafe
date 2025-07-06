package ViewC.Code;

import Controller.DBConnection;
import java.net.InetAddress;
import java.sql.*;

public class CN_LayTenMayTheoIP {

    public static void ganThongTinMay() {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT IDComputer, NameComputer FROM Computer WHERE IPRadmin = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ip);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                CN_BienToanCuc.IDComputer = rs.getInt("IDComputer");
                CN_BienToanCuc.TenMay = rs.getString("NameComputer");
            } else {
                System.out.println("KHONG TIM THAY MAY CHO IP: " + ip);
            }

        } catch (Exception e) {
            System.out.println("Loi CN_LayTenMayTheoIP: " + e.getMessage());
        }
    }
}
