package ViewC.Code;

import Controller.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class CN_LoginMay {

    public static boolean loginMay(String username, String password) {
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            if (conn == null) {
                System.out.println("Không thể kết nối CSDL.");
                return false;
            }

            // 1. Kiểm tra tài khoản hợp lệ + chưa online
            String sqlAcc = "SELECT IDAccount, NameAccount, Balance FROM Account WHERE NameAccount = ? AND PWAccount = ? AND OnlineStatus = 0";
            PreparedStatement psAcc = conn.prepareStatement(sqlAcc);
            psAcc.setString(1, username);
            psAcc.setString(2, password);
            ResultSet rsAcc = psAcc.executeQuery();

            if (!rsAcc.next()) {
                System.out.println("Sai tài khoản hoặc tài khoản đang được sử dụng trên máy khác.");
                rsAcc.close();
                psAcc.close();
                return false;
            }

            int idAccount = rsAcc.getInt("IDAccount");
            String tenTK = rsAcc.getString("NameAccount");
            double balance = rsAcc.getDouble("Balance");
            rsAcc.close();
            psAcc.close();

            if (balance <= 0) {
                System.out.println("Số dư không đủ, vui lòng nạp thêm tiền.");
                return false;
            }

            // 2. Xác định IP Radmin
            String ipMay = CN_LayTenMayTheoIP.getIPRadminHienTai();
            if (ipMay == null) {
                System.out.println("Không tìm thấy IP Radmin.");
                return false;
            }

            // 3. Xác định máy
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

            // 4. Tạo LogAccess
            String sqlLog = "INSERT INTO LogAccess (IDComputer, ThoiGianBatDau, IDAccount) VALUES (?, GETDATE(), ?)";
            PreparedStatement psLog = conn.prepareStatement(sqlLog, PreparedStatement.RETURN_GENERATED_KEYS);
            psLog.setInt(1, idComputer);
            psLog.setInt(2, idAccount);
            psLog.executeUpdate();

            ResultSet rsLog = psLog.getGeneratedKeys();
            int logAccessID = -1;
            if (rsLog.next()) {
                logAccessID = rsLog.getInt(1);
            }
            rsLog.close();
            psLog.close();

            if (logAccessID == -1) {
                System.out.println("Không thể tạo LogAccess.");
                return false;
            }

            CN_BienToanCuc.LogAccessID = logAccessID;

            // 5. Ghi ComputerUsage
            Timestamp now = new Timestamp(System.currentTimeMillis());
            String sqlUsage = "INSERT INTO ComputerUsage (IDComputer, IDAccount, StartTime) VALUES (?, ?, ?)";
            PreparedStatement psUsage = conn.prepareStatement(sqlUsage);
            psUsage.setInt(1, idComputer);
            psUsage.setInt(2, idAccount);
            psUsage.setTimestamp(3, now);
            psUsage.executeUpdate();
            psUsage.close();

            // 6. Update ComputerStatus
            String sqlUpdate = "UPDATE Computer SET ComputerStatus = 0 WHERE IDComputer = ?";
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setInt(1, idComputer);
            psUpdate.executeUpdate();
            psUpdate.close();

            // 7. Set OnlineStatus = 1
            String sqlOnline = "UPDATE Account SET OnlineStatus = 1 WHERE IDAccount = ?";
            PreparedStatement psOnline = conn.prepareStatement(sqlOnline);
            psOnline.setInt(1, idAccount);
            psOnline.executeUpdate();
            psOnline.close();

            // 8. Gán biến toàn cục
            CN_BienToanCuc.IDAccount = idAccount;
            CN_BienToanCuc.TenTaiKhoan = tenTK;
            CN_BienToanCuc.IDComputer = idComputer;
            CN_BienToanCuc.TenMay = tenMay;

            System.out.println("Đăng nhập thành công: " + tenTK + " | " + tenMay + " | LogAccessID: " + logAccessID);
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







//package ViewC.Code;
//
//import Controller.DBConnection;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.Timestamp;
//
//public class CN_LoginMay {
//
//    public static boolean loginMay(String username, String password) {
//        Connection conn = null;
//
//        try {
//            conn = DBConnection.getConnection();
//            if (conn == null) {
//                System.out.println("Không thể kết nối CSDL.");
//                return false;
//            }
//
//            // 1. Kiểm tra tài khoản hợp lệ
//            String sqlAcc = "SELECT IDAccount, NameAccount, Balance FROM Account WHERE NameAccount = ? AND PWAccount = ?";
//            PreparedStatement psAcc = conn.prepareStatement(sqlAcc);
//            psAcc.setString(1, username);
//            psAcc.setString(2, password);
//            ResultSet rsAcc = psAcc.executeQuery();
//
//            if (!rsAcc.next()) {
//                System.out.println("Sai tài khoản hoặc mật khẩu.");
//                rsAcc.close();
//                psAcc.close();
//                return false;
//            }
//
//            int idAccount = rsAcc.getInt("IDAccount");
//            String tenTK = rsAcc.getString("NameAccount");
//            double balance = rsAcc.getDouble("Balance"); // Lấy số dư
//            rsAcc.close();
//            psAcc.close();
//
//            // Kiểm tra số dư > 0
//            if (balance <= 0) {
//                System.out.println("Số dư tài khoản không đủ. Vui lòng nạp thêm tiền.");
//                return false;
//            }
//
//            // 2. Lấy IP Radmin VPN của máy
//            String ipMay = CN_LayTenMayTheoIP.getIPRadminHienTai();
//            if (ipMay == null) {
//                System.out.println("Không tìm thấy IP Radmin VPN.");
//                return false;
//            }
//
//            // 3. Xác định máy theo IP
//            String sqlComp = "SELECT IDComputer, NameComputer FROM Computer WHERE IPRadmin = ?";
//            PreparedStatement psComp = conn.prepareStatement(sqlComp);
//            psComp.setString(1, ipMay);
//            ResultSet rsComp = psComp.executeQuery();
//
//            if (!rsComp.next()) {
//                System.out.println("Không tìm thấy máy với IP Radmin: " + ipMay);
//                rsComp.close();
//                psComp.close();
//                return false;
//            }
//
//            int idComputer = rsComp.getInt("IDComputer");
//            String tenMay = rsComp.getString("NameComputer");
//            rsComp.close();
//            psComp.close();
//
//            // 4. Tạo LogAccess trực tiếp ở đây
//            String sqlLog = "INSERT INTO LogAccess (IDComputer, ThoiGianBatDau, IDAccount) VALUES (?, GETDATE(), ?)";
//            PreparedStatement psLog = conn.prepareStatement(sqlLog, PreparedStatement.RETURN_GENERATED_KEYS);
//            psLog.setInt(1, idComputer);
//            psLog.setInt(2, idAccount);
//            psLog.executeUpdate();
//
//            ResultSet rsLog = psLog.getGeneratedKeys();
//            int logAccessID = -1;
//            if (rsLog.next()) {
//                logAccessID = rsLog.getInt(1);
//            }
//            rsLog.close();
//            psLog.close();
//
//            if (logAccessID == -1) {
//                System.out.println("Không thể tạo LogAccess ID.");
//                return false;
//            }
//
//            CN_BienToanCuc.LogAccessID = logAccessID;
//
//            // 5. Ghi ComputerUsage
//            Timestamp now = new Timestamp(System.currentTimeMillis());
//            String sqlUsage = "INSERT INTO ComputerUsage (IDComputer, IDAccount, StartTime) VALUES (?, ?, ?)";
//            PreparedStatement psUsage = conn.prepareStatement(sqlUsage);
//            psUsage.setInt(1, idComputer);
//            psUsage.setInt(2, idAccount);
//            psUsage.setTimestamp(3, now);
//            psUsage.executeUpdate();
//            psUsage.close();
//
//            // 6. Cập nhật trạng thái máy
//            String sqlUpdate = "UPDATE Computer SET ComputerStatus = 0 WHERE IDComputer = ?";
//            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
//            psUpdate.setInt(1, idComputer);
//            psUpdate.executeUpdate();
//            psUpdate.close();
//
//            // 7. Gán biến toàn cục
//            CN_BienToanCuc.IDAccount = idAccount;
//            CN_BienToanCuc.TenTaiKhoan = tenTK;
//            CN_BienToanCuc.IDComputer = idComputer;
//            CN_BienToanCuc.TenMay = tenMay;
//
//            System.out.println("Đăng nhập thành công: " + tenTK + " - " + tenMay + " (LogAccessID: " + logAccessID + ")");
//            return true;
//
//        } catch (Exception e) {
//            System.out.println("Lỗi loginMay: " + e.getMessage());
//            return false;
//
//        } finally {
//            try {
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (Exception ex) {
//                System.out.println("Lỗi đóng kết nối: " + ex.getMessage());
//            }
//        }
//    }
//}
