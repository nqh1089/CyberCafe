package ViewC.Code;

import Controller.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import ViewC.View.C1_GiaoDienCho;

public class CN_LogoutMay {

    public static void logoutMay() {
        try {
            int idComputer = CN_BienToanCuc.IDComputer;

            if (idComputer == -1) {
                System.out.println("Không xác định được IDComputer khi đăng xuất.");
                return;
            }

            // 1. Cập nhật trạng thái máy về TRỐNG
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                String sqlUpdate = "UPDATE Computer SET ComputerStatus = 1 WHERE IDComputer = ?";
                PreparedStatement ps = conn.prepareStatement(sqlUpdate);
                ps.setInt(1, idComputer);
                ps.executeUpdate();
                conn.close();
            }

            // 2. Xoá toàn bộ dữ liệu trong biến toàn cục
            CN_BienToanCuc.IDAccount = -1;
            CN_BienToanCuc.IDComputer = -1;
            CN_BienToanCuc.TenMay = "";
            CN_BienToanCuc.TenTaiKhoan = "";

            // 3. Mở lại giao diện chờ
            new C1_GiaoDienCho().setVisible(true);

            System.out.println("Đăng xuất thành công -> Máy trống");
        } catch (Exception e) {
            System.out.println("Lỗi khi đăng xuất: " + e.getMessage());
        }
    }
}
