package ViewAD.Code;

import Controller.DBConnection;
import java.sql.*;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class TAB7_NhanVien {

    // Load bảng theo từ khóa và trạng thái tài khoản (statusFilter: -1 = tất cả, 1 = hoạt động, 0 = ngừng)
    public static void LoadTable(JTable table, String keyword, int statusFilter) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        String sql = "SELECT IDAccount, NameAccount, RoleAccount, CCCD, PhoneNumber, Email, Gender, AccountStatus " +
                     "FROM Account WHERE RoleAccount IN ('ADMIN', 'BOSS')";

        if (!keyword.isEmpty()) {
            sql += " AND NameAccount LIKE ?";
        }

        if (statusFilter != -1) {
            sql += " AND AccountStatus = ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int paramIndex = 1;

            if (!keyword.isEmpty()) {
                ps.setString(paramIndex++, "%" + keyword + "%");
            }

            if (statusFilter != -1) {
                ps.setBoolean(paramIndex, statusFilter == 1);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = {
                    rs.getString("IDAccount"),
                    rs.getString("NameAccount"),
                    rs.getString("RoleAccount"),
                    rs.getString("CCCD"),
                    rs.getString("PhoneNumber"),
                    rs.getString("Email"),
                    rs.getString("Gender"),
                    rs.getBoolean("AccountStatus") ? "Đang hoạt động" : "Ngừng hoạt động"
                };
                model.addRow(row);
            }

            // Cấu hình bảng
            table.setDefaultEditor(Object.class, null); // Không cho chỉnh sửa
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Chọn 1 dòng duy nhất
            table.setRowSelectionAllowed(true);
            table.setColumnSelectionAllowed(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
