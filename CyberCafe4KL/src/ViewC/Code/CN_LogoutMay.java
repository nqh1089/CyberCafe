package ViewC.Code;

import Controller.DBConnection;
import ViewAD.View.AD_ChangePW;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.SwingUtilities;
import java.sql.Timestamp;
import java.sql.ResultSet;


import ViewC.View.C1_GiaoDienCho;
import ViewC.View.C2_Menu;
import ViewC.View.C2_Chat;
import static ViewC.View.C2_Chat.instance;
import ViewC.View.C2_Order;

public class CN_LogoutMay {

    public static AD_ChangePW instance;

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

                // 1. Cập nhật trạng thái máy
                String sqlUpdateComputer = "UPDATE Computer SET ComputerStatus = 1 WHERE IDComputer = ?";
                PreparedStatement psComputer = conn.prepareStatement(sqlUpdateComputer);
                psComputer.setInt(1, idComputer);
                psComputer.executeUpdate();

                // 2. Cập nhật trạng thái tài khoản
                if (idAccount != -1) {
                    String sqlUpdateAccount = "UPDATE Account SET OnlineStatus = 0 WHERE IDAccount = ?";
                    PreparedStatement psAccount = conn.prepareStatement(sqlUpdateAccount);
                    psAccount.setInt(1, idAccount);
                    psAccount.executeUpdate();
                }

                // 3. Chốt ComputerUsage
                String sqlUsage = "UPDATE ComputerUsage SET EndTime = GETDATE() "
                        + "WHERE IDComputer = ? AND IDAccount = ? AND EndTime IS NULL";
                PreparedStatement psUsage = conn.prepareStatement(sqlUsage);
                psUsage.setInt(1, idComputer);
                psUsage.setInt(2, idAccount);
                psUsage.executeUpdate();
                psUsage.close();

// 4. Tính Cost và cập nhật vào ComputerUsage + trừ Balance
                String sqlGetCost = """
SELECT TOP 1 CU.IDUsage, CU.StartTime, DATEDIFF(MINUTE, CU.StartTime, CU.EndTime) * C.PricePerMinute AS Cost
FROM ComputerUsage CU JOIN Computer C ON CU.IDComputer = C.IDComputer
WHERE CU.IDComputer = ? AND CU.IDAccount = ? AND CU.EndTime IS NOT NULL
ORDER BY CU.EndTime DESC
""";
                PreparedStatement psCost = conn.prepareStatement(sqlGetCost);
                psCost.setInt(1, idComputer);
                psCost.setInt(2, idAccount);
                ResultSet rsCost = psCost.executeQuery();

                if (rsCost.next()) {
                    int idUsage = rsCost.getInt("IDUsage");
                    double cost = rsCost.getDouble("Cost");
                    Timestamp startTime = rsCost.getTimestamp("StartTime");

                    // Update lại Cost cho ComputerUsage
                    String sqlUpdateCU = "UPDATE ComputerUsage SET Cost = ? WHERE IDUsage = ?";
                    PreparedStatement psUpdateCU = conn.prepareStatement(sqlUpdateCU);
                    psUpdateCU.setDouble(1, cost);
                    psUpdateCU.setInt(2, idUsage);
                    psUpdateCU.executeUpdate();
                    psUpdateCU.close();

                    // Trừ tiền Balance
                    String sqlTruBalance = "UPDATE Account SET Balance = Balance - ? WHERE IDAccount = ?";
                    PreparedStatement psTruBalance = conn.prepareStatement(sqlTruBalance);
                    psTruBalance.setDouble(1, cost);
                    psTruBalance.setInt(2, idAccount);
                    psTruBalance.executeUpdate();
                    psTruBalance.close();

                    // ✅ Tính tổng tiền dịch vụ
                    double tongTienDichVu = C2_ChiPhiDichVu.layTongTienOrderClient(CN_BienToanCuc.TenMay, startTime);
                    double tongHoaDon = cost + tongTienDichVu;

                    // ✅ Lấy ID admin hoặc boss đang trực
                    int idAdminTruc = -1;
                    String sqlAdmin = """
        SELECT TOP 1 IDAccount FROM Account
        WHERE (RoleAccount = 'ADMIN' OR RoleAccount = 'BOSS')
        AND OnlineStatus = 1 AND AccountStatus = 1
    """;
                    PreparedStatement psAdmin = conn.prepareStatement(sqlAdmin);
                    ResultSet rsAdmin = psAdmin.executeQuery();
                    if (rsAdmin.next()) {
                        idAdminTruc = rsAdmin.getInt("IDAccount");
                        System.out.println("[Lấy admin trực] IDAccount: " + idAdminTruc);
                    } else {
                        System.out.println("[Lấy admin trực] Không tìm thấy ai đang trực.");
                    }
                    rsAdmin.close();
                    psAdmin.close();

                    // ✅ Lưu vào bảng Invoice (nếu có admin trực)
                    if (idAdminTruc != -1) {
                        String sqlInsertInvoice = "INSERT INTO Invoice (IDAccount, TotalAmount, Status) VALUES (?, ?, N'Paid')";
                        PreparedStatement psInvoice = conn.prepareStatement(sqlInsertInvoice);
                        psInvoice.setInt(1, idAdminTruc);
                        psInvoice.setDouble(2, tongHoaDon);
                        psInvoice.executeUpdate();
                        psInvoice.close();

                        System.out.println("Đã tính phí: " + cost + " đ → Trừ vào Balance");
                        System.out.println("Đã lưu hóa đơn tổng cộng: " + tongHoaDon + " đ (NV trực ID: " + idAdminTruc + ")");
                    } else {
                        System.out.println("Không lưu được hóa đơn vì không có admin trực.");
                    }
                }
                rsCost.close();
                psCost.close();

                // 5. Chốt LogAccess
                String sqlLog = "UPDATE LogAccess SET ThoiGianKetThuc = GETDATE() WHERE IDComputer = ? AND IDAccount = ? AND ThoiGianKetThuc IS NULL";
                PreparedStatement psLog = conn.prepareStatement(sqlLog);
                psLog.setInt(1, idComputer);
                psLog.setInt(2, idAccount);
                psLog.executeUpdate();

                conn.close();
            }

            // ✅ Thêm phần xử lý logout + disconnect cho C2_Chat
            if (C2_Chat.instance != null && C2_Chat.instance.chatClient != null) {
                if (C2_Chat.instance.chatClient.isConnected()) {
                    C2_Chat.instance.chatClient.logoutAccount();
                    C2_Chat.instance.chatClient.disconnect("Đăng xuất máy");
                }
                C2_Chat.instance.dispose();
            }

            // 6. Reset biến toàn cục
            CN_BienToanCuc.IDAccount = -1;
            CN_BienToanCuc.IDComputer = -1;
            CN_BienToanCuc.TenMay = "";
            CN_BienToanCuc.TenTaiKhoan = "";

            // 7. Đóng tất cả form đang mở
            closeAllClientForms();

            // 8. Mở lại giao diện chờ
            new C1_GiaoDienCho().setVisible(true);

            System.out.println("Đăng xuất thành công → Máy Trống");

        } catch (Exception e) {
            System.out.println("Lỗi khi đăng xuất: " + e.getMessage());
        }
    }

    public static void closeAllClientForms() {
        try {
            if (C2_Menu.instance != null) {
                if (C2_Menu.instance.timer != null) {
                    C2_Menu.instance.timer.stop();
                    System.out.println("Đã stop Timer trong C2_Menu");
                }
                C2_Menu.instance.dispose();
                C2_Menu.instance = null;
            }

            if (C2_Chat.instance != null) {
                C2_Chat.instance.dispose();
                C2_Chat.instance = null;
            }

            if (C2_Order.instance != null) {
                C2_Order.instance.dispose();
                C2_Order.instance = null;
            }

            if (AD_ChangePW.instance != null) {
                AD_ChangePW.instance.dispose();
                AD_ChangePW.instance = null;
            }

        } catch (Exception e) {
            System.out.println("Lỗi khi đóng form: " + e.getMessage());
        }
    }
}
