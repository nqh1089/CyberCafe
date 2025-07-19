package Socket;

import Controller.DBConnection;
import ViewC.Code.CN_BienToanCuc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CM_CN_LoginSessionManager {

    /**
     * Ghi log truy cập vào bảng LogAccess và cập nhật LogAccessID vào CN_BienToanCuc.
     * Phương thức này được gọi sau khi xác định được IDComputer và IDAccount hợp lệ.
     *
     * @param idComputer ID của máy tính đang đăng nhập.
     * @param idAccount ID của tài khoản đang đăng nhập.
     * @return LogAccessID vừa được tạo, hoặc -1 nếu có lỗi.
     */
    public static int createAndGetLogAccessID(int idComputer, int idAccount) {
        Connection conn = null;
        PreparedStatement psLog = null;
        ResultSet rsLog = null;
        int logAccessID = -1;

        try {
            conn = DBConnection.getConnection();
            if (conn == null) {
                System.err.println("Không thể kết nối CSDL để tạo LogAccess.");
                return -1;
            }

            // Ghi log vào LogAccess và lấy LogAccessID
            // Sử dụng SELECT SCOPE_IDENTITY() cho SQL Server để lấy ID vừa được tạo
            String sqlLog = "INSERT INTO LogAccess (IDComputer, ThoiGianBatDau, IDAccount) VALUES (?, GETDATE(), ?); SELECT SCOPE_IDENTITY();";
            psLog = conn.prepareStatement(sqlLog);
            psLog.setInt(1, idComputer);
            psLog.setInt(2, idAccount);
            rsLog = psLog.executeQuery();

            if (rsLog.next()) {
                logAccessID = rsLog.getInt(1); // Lấy ID vừa được tạo
                CN_BienToanCuc.LogAccessID = logAccessID; // Cập nhật vào biến toàn cục
                System.out.println("LogAccessID được tạo: " + logAccessID);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo LogAccess và lấy ID: " + e.getMessage());
            logAccessID = -1; // Đảm bảo trả về -1 nếu có lỗi
        } finally {
            try {
                if (rsLog != null) rsLog.close();
                if (psLog != null) psLog.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.err.println("Lỗi đóng kết nối LogAccess: " + ex.getMessage());
            }
        }
        return logAccessID;
    }
}