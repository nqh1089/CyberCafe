package ViewC.Code;

import java.net.*;
import java.sql.*;
import java.util.Enumeration;
import Controller.DBConnection;

public class CN_XacDinhMayClient {

    // Lấy IP Radmin VPN của máy
    public static String getRadminIP() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();

                if (!ni.isUp() || ni.isLoopback()) continue;

                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    String ip = addr.getHostAddress();

                    // IPv4 & IP Radmin VPN (26.x.x.x)
                    if (ip.startsWith("26.") && addr instanceof Inet4Address) {
                        return ip;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy IP Radmin: " + e.getMessage());
        }

        return null;
    }

    // Xác định máy tương ứng với IP Radmin
    public static String getTenMayClient() {
        String ipClient = getRadminIP();
        if (ipClient == null) {
            return "Không tìm thấy IP Radmin";
        }

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                return "Không thể kết nối CSDL";
            }

            String sql = "SELECT NameComputer FROM Computer WHERE IPRadmin = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ipClient);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("NameComputer");
            } else {
                return "Không tìm thấy máy tương ứng với IP: " + ipClient;
            }

        } catch (SQLException e) {
            return "Lỗi khi truy vấn CSDL: " + e.getMessage();
        }
    }

    // Test nhanh
    public static void main(String[] args) {
        String tenMay = getTenMayClient();
        System.out.println("Máy client hiện tại: " + tenMay);
    }
}
