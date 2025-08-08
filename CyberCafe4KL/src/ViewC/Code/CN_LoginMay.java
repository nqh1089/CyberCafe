package ViewC.Code;

import Controller.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class CN_LoginMay {

    // Biến lưu thông báo lỗi
    public static String lastErrorMessage = "";

    // ===== Đăng nhập máy bình thường =====
    public static boolean loginMay(String username, String password) {
        Connection conn = null;
        lastErrorMessage = "";

        try {
            conn = DBConnection.getConnection();
            if (conn == null) {
                lastErrorMessage = "Không thể kết nối CSDL.";
                return false;
            }

            // 1. Kiểm tra tên tài khoản tồn tại
            String sqlCheckUser = "SELECT IDAccount, NameAccount, PWAccount, Balance, OnlineStatus FROM Account WHERE NameAccount = ?";
            try (PreparedStatement psUser = conn.prepareStatement(sqlCheckUser)) {
                psUser.setString(1, username);
                try (ResultSet rsUser = psUser.executeQuery()) {
                    if (!rsUser.next()) {
                        lastErrorMessage = "Tên tài khoản không tồn tại.";
                        return false;
                    }

                    int idAccount = rsUser.getInt("IDAccount");
                    String tenTK = rsUser.getString("NameAccount");
                    String passFromDB = rsUser.getString("PWAccount");
                    double balance = rsUser.getDouble("Balance");
                    int onlineStatus = rsUser.getInt("OnlineStatus");

                    // 2. Kiểm tra mật khẩu
                    if (!passFromDB.equals(password)) {
                        lastErrorMessage = "Mật khẩu không đúng.";
                        return false;
                    }

                    // 3. Kiểm tra tài khoản có đang online
                    if (onlineStatus == 1) {
                        lastErrorMessage = "Tài khoản đang được sử dụng trên máy khác.";
                        return false;
                    }

                    // 4. Kiểm tra số dư
                    if (balance <= 0) {
                        lastErrorMessage = "Số dư không đủ, vui lòng nạp thêm tiền.";
                        return false;
                    }

                    // 5. Lấy IP Radmin
                    String ipMay = CN_LayTenMayTheoIP.getIPRadminHienTai();
                    if (ipMay == null || ipMay.isEmpty()) {
                        lastErrorMessage = "Không tìm thấy IP Radmin.";
                        return false;
                    }

                    // 6. Lấy thông tin máy
                    int idComputer;
                    String tenMay;
                    String sqlComp = "SELECT IDComputer, NameComputer FROM Computer WHERE IPRadmin = ?";
                    try (PreparedStatement psComp = conn.prepareStatement(sqlComp)) {
                        psComp.setString(1, ipMay);
                        try (ResultSet rsComp = psComp.executeQuery()) {
                            if (!rsComp.next()) {
                                lastErrorMessage = "Không tìm thấy máy với IP Radmin: " + ipMay;
                                return false;
                            }
                            idComputer = rsComp.getInt("IDComputer");
                            tenMay = rsComp.getString("NameComputer");
                        }
                    }

                    // 7. Tạo LogAccess
                    int logAccessID = -1;
                    String sqlLog = "INSERT INTO LogAccess (IDComputer, ThoiGianBatDau, IDAccount) VALUES (?, GETDATE(), ?)";
                    try (PreparedStatement psLog = conn.prepareStatement(sqlLog, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        psLog.setInt(1, idComputer);
                        psLog.setInt(2, idAccount);
                        psLog.executeUpdate();
                        try (ResultSet rsLog = psLog.getGeneratedKeys()) {
                            if (rsLog.next()) {
                                logAccessID = rsLog.getInt(1);
                            }
                        }
                    }
                    if (logAccessID == -1) {
                        lastErrorMessage = "Không thể tạo bản ghi LogAccess.";
                        return false;
                    }
                    CN_BienToanCuc.LogAccessID = logAccessID;

                    // 8. Ghi ComputerUsage
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    String sqlUsage = "INSERT INTO ComputerUsage (IDComputer, IDAccount, StartTime) VALUES (?, ?, ?)";
                    try (PreparedStatement psUsage = conn.prepareStatement(sqlUsage)) {
                        psUsage.setInt(1, idComputer);
                        psUsage.setInt(2, idAccount);
                        psUsage.setTimestamp(3, now);
                        psUsage.executeUpdate();
                    }

                    // 9. Update trạng thái máy
                    try (PreparedStatement psUpdate = conn.prepareStatement("UPDATE Computer SET ComputerStatus = 0 WHERE IDComputer = ?")) {
                        psUpdate.setInt(1, idComputer);
                        psUpdate.executeUpdate();
                    }

                    // 10. Set OnlineStatus = 1
                    try (PreparedStatement psOnline = conn.prepareStatement("UPDATE Account SET OnlineStatus = 1 WHERE IDAccount = ?")) {
                        psOnline.setInt(1, idAccount);
                        psOnline.executeUpdate();
                    }

                    // 11. Gán biến toàn cục
                    CN_BienToanCuc.IDAccount = idAccount;
                    CN_BienToanCuc.TenTaiKhoan = tenTK;
                    CN_BienToanCuc.IDComputer = idComputer;
                    CN_BienToanCuc.TenMay = tenMay;

                    return true;
                }
            }
        } catch (Exception e) {
            lastErrorMessage = "Lỗi loginMay: " + e.getMessage();
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception ex) {
                lastErrorMessage = "Lỗi đóng kết nối: " + ex.getMessage();
            }
        }
    }

    // ===== Đăng nhập máy treo =====
    public static boolean loginMaytreo(String username, String password) {
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            if (conn == null) {
                System.out.println("Không thể kết nối CSDL.");
                return false;
            }

            // Kiểm tra tài khoản tồn tại
            String sqlAcc = "SELECT IDAccount, NameAccount, PWAccount, Balance, OnlineStatus FROM Account WHERE NameAccount = ?";
            try (PreparedStatement psAcc = conn.prepareStatement(sqlAcc)) {
                psAcc.setString(1, username);
                try (ResultSet rsAcc = psAcc.executeQuery()) {
                    if (!rsAcc.next()) {
                        System.out.println("Tên tài khoản không tồn tại.");
                        return false;
                    }

                    int idAccount = rsAcc.getInt("IDAccount");
                    String tenTK = rsAcc.getString("NameAccount");
                    String passFromDB = rsAcc.getString("PWAccount");
                    double balance = rsAcc.getDouble("Balance");

                    if (!passFromDB.equals(password)) {
                        System.out.println("Mật khẩu không đúng.");
                        return false;
                    }
                    if (balance <= 0) {
                        System.out.println("Số dư không đủ, vui lòng nạp thêm tiền.");
                        return false;
                    }

                    // Xác định IP Radmin
                    String ipMay = CN_LayTenMayTheoIP.getIPRadminHienTai();
                    if (ipMay == null) {
                        System.out.println("Không tìm thấy IP Radmin.");
                        return false;
                    }

                    // Xác định máy
                    int idComputer;
                    String tenMay;
                    String sqlComp = "SELECT IDComputer, NameComputer FROM Computer WHERE IPRadmin = ?";
                    try (PreparedStatement psComp = conn.prepareStatement(sqlComp)) {
                        psComp.setString(1, ipMay);
                        try (ResultSet rsComp = psComp.executeQuery()) {
                            if (!rsComp.next()) {
                                System.out.println("Không tìm thấy máy với IP Radmin: " + ipMay);
                                return false;
                            }
                            idComputer = rsComp.getInt("IDComputer");
                            tenMay = rsComp.getString("NameComputer");
                        }
                    }

                    // Tạo LogAccess
                    int logAccessID = -1;
                    String sqlLog = "INSERT INTO LogAccess (IDComputer, ThoiGianBatDau, IDAccount) VALUES (?, GETDATE(), ?)";
                    try (PreparedStatement psLog = conn.prepareStatement(sqlLog, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        psLog.setInt(1, idComputer);
                        psLog.setInt(2, idAccount);
                        psLog.executeUpdate();
                        try (ResultSet rsLog = psLog.getGeneratedKeys()) {
                            if (rsLog.next()) {
                                logAccessID = rsLog.getInt(1);
                            }
                        }
                    }
                    if (logAccessID == -1) {
                        System.out.println("Không thể tạo LogAccess.");
                        return false;
                    }
                    CN_BienToanCuc.LogAccessID = logAccessID;

                    // Ghi ComputerUsage
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    String sqlUsage = "INSERT INTO ComputerUsage (IDComputer, IDAccount, StartTime) VALUES (?, ?, ?)";
                    try (PreparedStatement psUsage = conn.prepareStatement(sqlUsage)) {
                        psUsage.setInt(1, idComputer);
                        psUsage.setInt(2, idAccount);
                        psUsage.setTimestamp(3, now);
                        psUsage.executeUpdate();
                    }

                    // Update trạng thái máy
                    try (PreparedStatement psUpdate = conn.prepareStatement("UPDATE Computer SET ComputerStatus = 0 WHERE IDComputer = ?")) {
                        psUpdate.setInt(1, idComputer);
                        psUpdate.executeUpdate();
                    }

                    // Set OnlineStatus = 1
                    try (PreparedStatement psOnline = conn.prepareStatement("UPDATE Account SET OnlineStatus = 1 WHERE IDAccount = ?")) {
                        psOnline.setInt(1, idAccount);
                        psOnline.executeUpdate();
                    }

                    // Gán biến toàn cục
                    CN_BienToanCuc.IDAccount = idAccount;
                    CN_BienToanCuc.TenTaiKhoan = tenTK;
                    CN_BienToanCuc.IDComputer = idComputer;
                    CN_BienToanCuc.TenMay = tenMay;

                    System.out.println("Đăng nhập thành công: " + tenTK + " | " + tenMay + " | LogAccessID: " + logAccessID);
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi loginMaytreo: " + e.getMessage());
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
