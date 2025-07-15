package ViewC.Code;

import Controller.DBConnection;
import java.net.*;
import java.sql.*;
import java.util.Enumeration;

public class CN_LayTenMayTheoIP {

    public static void ganThongTinMay() {
        try {
            String ip = layIPRadmin(); // <-- Đổi cách lấy IP

            if (ip == null) {
                System.out.println("KHONG TIM THAY IP RADMIN VPN");
                return;
            }

            Connection conn = DBConnection.getConnection();
            if (conn == null) {
                System.out.println("KHONG KET NOI DUOC CSDL");
                return;
            }

            String sql = "SELECT IDComputer, NameComputer FROM Computer WHERE IPRadmin = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ip);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                CN_BienToanCuc.IDComputer = rs.getInt("IDComputer");
                CN_BienToanCuc.TenMay = rs.getString("NameComputer");
                System.out.println("MAY HIEN TAI: " + CN_BienToanCuc.TenMay + " (IP: " + ip + ")");
            } else {
                System.out.println("KHONG TIM THAY MAY CHO IP: " + ip);
            }

        } catch (Exception e) {
            System.out.println("Loi CN_LayTenMayTheoIP: " + e.getMessage());
        }
    }

    // Hàm lấy IP Radmin VPN (26.x.x.x)
    private static String layIPRadmin() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();

                if (!ni.isUp() || ni.isLoopback()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    String ip = addr.getHostAddress();

                    if (ip.startsWith("26.") && addr instanceof Inet4Address) {
                        return ip;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Loi layIPRadmin: " + e.getMessage());
        }

        return null;
    }
}
