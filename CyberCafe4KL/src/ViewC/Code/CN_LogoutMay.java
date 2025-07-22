package ViewC.Code;

import Controller.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.SwingUtilities;

import ViewC.View.C1_GiaoDienCho;
import ViewC.View.C2_Menu;
import ViewC.View.C2_Chat;
import ViewC.View.C2_Order;
import ViewC.View.C2_ChangePW;

public class CN_LogoutMay {

    public static void logoutMay() {
        try {
            int idComputer = CN_BienToanCuc.IDComputer;
            int idAccount = CN_BienToanCuc.IDAccount;

            if (idComputer == -1) {
                System.out.println("Không xác định được IDComputer khi đăng xuất.");
                return;
            }

            Connection conn = DBConnection.getConnection();
            if (conn != null) {

                // Cập nhật trạng thái máy
                String sqlUpdateComputer = "UPDATE Computer SET ComputerStatus = 1 WHERE IDComputer = ?";
                PreparedStatement psComputer = conn.prepareStatement(sqlUpdateComputer);
                psComputer.setInt(1, idComputer);
                psComputer.executeUpdate();

                // Cập nhật trạng thái tài khoản
                if (idAccount != -1) {
                    String sqlUpdateAccount = "UPDATE Account SET OnlineStatus = 0 WHERE IDAccount = ?";
                    PreparedStatement psAccount = conn.prepareStatement(sqlUpdateAccount);
                    psAccount.setInt(1, idAccount);
                    psAccount.executeUpdate();
                }

                // Chốt ComputerUsage
                String sqlUsage = "UPDATE ComputerUsage SET EndTime = GETDATE() "
                        + "WHERE IDComputer = ? AND IDAccount = ? AND EndTime IS NULL";
                PreparedStatement psUsage = conn.prepareStatement(sqlUsage);
                psUsage.setInt(1, idComputer);
                psUsage.setInt(2, idAccount);
                psUsage.executeUpdate();
                psUsage.close();

                // Chốt LogAccess
                String sqlLog = "UPDATE LogAccess SET ThoiGianKetThuc = GETDATE() WHERE IDComputer = ? AND IDAccount = ? AND ThoiGianKetThuc IS NULL";
                PreparedStatement psLog = conn.prepareStatement(sqlLog);
                psLog.setInt(1, idComputer);
                psLog.setInt(2, idAccount);
                psLog.executeUpdate();

                conn.close();
            }

            // Reset biến toàn cục
            CN_BienToanCuc.IDAccount = -1;
            CN_BienToanCuc.IDComputer = -1;
            CN_BienToanCuc.TenMay = "";
            CN_BienToanCuc.TenTaiKhoan = "";

            // ✅ 6. Đóng tất cả form đang mở
            closeAllClientForms();

            // ✅ 7. Mở lại giao diện chờ
            new C1_GiaoDienCho().setVisible(true);

            System.out.println("Đăng xuất thành công → Máy Trống");

        } catch (Exception e) {
            System.out.println("Lỗi khi đăng xuất: " + e.getMessage());
        }
    }

    private static void closeAllClientForms() {
        try {
            // Đóng Menu
            if (C2_Menu.instance != null) {
                C2_Menu.instance.dispose();
                C2_Menu.instance = null;
            }
            // Đóng Chat
            if (C2_Chat.instance != null) {
                C2_Chat.instance.dispose();
                C2_Chat.instance = null;
            }
            // Đóng Order
            if (C2_Order.instance != null) {
                C2_Order.instance.dispose();
                C2_Order.instance = null;
            }
            // Đóng Đổi mật khẩu
            if (C2_ChangePW.instance != null) {
                C2_ChangePW.instance.dispose();
                C2_ChangePW.instance = null;
            }

        } catch (Exception e) {
            System.out.println("Lỗi khi đóng form: " + e.getMessage());
        }
    }
}
