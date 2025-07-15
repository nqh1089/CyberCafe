package ViewC.Code;

import Controller.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CN_LoginMay {

    public static boolean loginMay(String username, String password) {
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            if (conn == null) {
                System.out.println("Không thể kết nối CSDL.");
                return false;
            }

            // 1. Kiểm tra tài khoản hợp lệ
            String sqlAcc = "SELECT IDAccount, NameAccount FROM Account WHERE NameAccount = ? AND PWAccount = ?";
            PreparedStatement psAcc = conn.prepareStatement(sqlAcc);
            psAcc.setString(1, username);
            psAcc.setString(2, password);
            ResultSet rsAcc = psAcc.executeQuery();

            if (!rsAcc.next()) {
                System.out.println("Sai tài khoản hoặc mật khẩu.");
                return false;
            }

            int idAcc = rsAcc.getInt("IDAccount");
            String tenTK = rsAcc.getString("NameAccount");

            // 2. Lấy IP Radmin VPN của máy
            String ipMay = CN_LayTenMayTheoIP.getIPRadminHienTai();
            if (ipMay == null) {
                System.out.println("Không tìm thấy IP Radmin VPN.");
                return false;
            }

            // 3. Xác định máy theo IP
            String sqlComp = "SELECT IDComputer, NameComputer FROM Computer WHERE IPRadmin = ?";
            PreparedStatement psComp = conn.prepareStatement(sqlComp);
            psComp.setString(1, ipMay);
            ResultSet rsComp = psComp.executeQuery();

            if (!rsComp.next()) {
                System.out.println("Không tìm thấy máy với IP Radmin: " + ipMay);
                return false;
            }

            int idComputer = rsComp.getInt("IDComputer");
            String tenMay = rsComp.getString("NameComputer");

            // 4. Ghi log vào LogAccess
            String sqlLog = "INSERT INTO LogAccess (IDComputer, ThoiGianBatDau, IDAccount) VALUES (?, GETDATE(), ?)";
            PreparedStatement psLog = conn.prepareStatement(sqlLog);
            psLog.setInt(1, idComputer);
            psLog.setInt(2, idAcc);
            psLog.executeUpdate();

            // 5. Ghi log vào ComputerUsage
            String sqlUsage = "INSERT INTO ComputerUsage (IDComputer, StartTime, IDAccount) VALUES (?, GETDATE(), ?)";
            PreparedStatement psUsage = conn.prepareStatement(sqlUsage);
            psUsage.setInt(1, idComputer);
            psUsage.setInt(2, idAcc);
            psUsage.executeUpdate();

            // 6. Cập nhật trạng thái máy (0 = đang hoạt động)
            String sqlUpdate = "UPDATE Computer SET ComputerStatus = 0 WHERE IDComputer = ?";
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setInt(1, idComputer);
            psUpdate.executeUpdate();

            // 7. Gán vào biến toàn cục
            CN_BienToanCuc.IDAccount = idAcc;
            CN_BienToanCuc.TenTaiKhoan = tenTK;
            CN_BienToanCuc.IDComputer = idComputer;
            CN_BienToanCuc.TenMay = tenMay;

            System.out.println("Đăng nhập thành công: " + tenTK + " - " + tenMay);
            return true;

        } catch (Exception e) {
            System.out.println("Lỗi loginMay: " + e.getMessage());
            return false;

        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception ex) {
                System.out.println("Lỗi đóng kết nối: " + ex.getMessage());
            }
        }
    }
}
