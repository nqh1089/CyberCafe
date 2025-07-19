package ViewC.Code;

import Controller.DBConnection;
import Socket.CM_CN_LoginSessionManager; // Import lớp mới

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.net.*;
import java.sql.*;
import java.util.Enumeration;

public class CN_LoginMay {

    public static boolean loginMay(String username, String password) {
        Connection conn = null; // Khai báo conn ở đây để có thể đóng trong finally

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
                rsAcc.close();
                psAcc.close();
                return false;
            }

            int idAcc = rsAcc.getInt("IDAccount");
            String tenTK = rsAcc.getString("NameAccount");
            rsAcc.close();
            psAcc.close();

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
                rsComp.close();
                psComp.close();
                return false;
            }

            int idComputer = rsComp.getInt("IDComputer");
            String tenMay = rsComp.getString("NameComputer");
            rsComp.close();
            psComp.close();

            // 4. Ghi log vào LogAccess và lấy LogAccessID (Gọi từ lớp mới trong package Socket)
            // Đóng kết nối hiện tại trước khi gọi hàm mới để tránh vấn đề quản lý kết nối
            // Hoặc truyền kết nối vào hàm nếu muốn dùng lại kết nối này.
            // Để đơn giản, tôi sẽ đóng kết nối ở đây và để hàm mới tự mở/đóng kết nối riêng.
            // Điều này có thể không tối ưu về hiệu suất nhưng an toàn hơn về quản lý kết nối.
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.err.println("Lỗi đóng kết nối trước khi tạo LogAccess: " + ex.getMessage());
            }

            // Gọi hàm từ CM_CN_LoginSessionManager để tạo LogAccess và lấy ID
            int logAccessID = CM_CN_LoginSessionManager.createAndGetLogAccessID(idComputer, idAcc);
            if (logAccessID == -1) {
                System.out.println("Không thể tạo LogAccess ID.");
                return false;
            }
            // LogAccessID đã được cập nhật vào CN_BienToanCuc bên trong CM_CN_LoginSessionManager

            // Tiếp tục với các bước còn lại, cần mở lại kết nối nếu cần
            conn = DBConnection.getConnection(); // Mở lại kết nối sau khi tạo LogAccess
            if (conn == null) {
                System.out.println("Không thể kết nối CSDL sau khi tạo LogAccess.");
                return false;
            }

            // 5. Ghi log vào ComputerUsage
            String sqlUsage = "INSERT INTO ComputerUsage (IDComputer, StartTime, IDAccount) VALUES (?, GETDATE(), ?)";
            PreparedStatement psUsage = conn.prepareStatement(sqlUsage);
            psUsage.setInt(1, idComputer);
            psUsage.setInt(2, idAcc);
            psUsage.executeUpdate();
            psUsage.close();

            // 6. Cập nhật trạng thái máy (0 = đang hoạt động)
            String sqlUpdate = "UPDATE Computer SET ComputerStatus = 0 WHERE IDComputer = ?";
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setInt(1, idComputer);
            psUpdate.executeUpdate();
            psUpdate.close();

            // 7. Gán vào biến toàn cục
            CN_BienToanCuc.IDAccount = idAcc;
            CN_BienToanCuc.TenTaiKhoan = tenTK;
            CN_BienToanCuc.IDComputer = idComputer;
            CN_BienToanCuc.TenMay = tenMay;

            System.out.println("Đăng nhập thành công: " + tenTK + " - " + tenMay + " (LogAccessID: " + CN_BienToanCuc.LogAccessID + ")");
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