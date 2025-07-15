package ViewC.Code;

import Controller.DBConnection;

import java.net.*;
import java.sql.*;
import java.util.Enumeration;

public class CN_LayTenMayTheoIP {

    // Hàm gán thông tin máy vào biến toàn cục
    public static void ganThongTinMay() {
        try {
            String ip = layIPRadmin(); // Lấy IP Radmin VPN

            if (ip == null) {
                System.out.println("KHÔNG TÌM THẤY IP RADMIN VPN");
                return;
            }

            Connection conn = DBConnection.getConnection();
            if (conn == null) {
                System.out.println("KHÔNG KẾT NỐI ĐƯỢC CSDL");
                return;
            }

            String sql = "SELECT IDComputer, NameComputer FROM Computer WHERE IPRadmin = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ip);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                CN_BienToanCuc.IDComputer = rs.getInt("IDComputer");
                CN_BienToanCuc.TenMay = rs.getString("NameComputer");
                System.out.println("MÁY HIỆN TẠI: " + CN_BienToanCuc.TenMay + " (IP: " + ip + ")");
            } else {
                System.out.println("KHÔNG TÌM THẤY MÁY CHO IP: " + ip);
            }

        } catch (Exception e) {
            System.out.println("Lỗi CN_LayTenMayTheoIP: " + e.getMessage());
        }
    }

    // ✅ Hàm public để lấy IP từ class khác
    public static String getIPRadminHienTai() {
        return layIPRadmin();
    }

    // ✅ Hàm nội bộ: duyệt NetworkInterface để lấy IP bắt đầu bằng 26.
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
            System.out.println("Lỗi layIPRadmin: " + e.getMessage());
        }

        return null;
    }
}
