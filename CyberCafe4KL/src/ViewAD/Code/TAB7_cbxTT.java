package ViewAD.Code;

import Controller.DBConnection;
import java.sql.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class TAB7_cbxTT {

    // Khởi tạo ComboBox lọc trạng thái
    public static void CapNhatComboBox(JComboBox<String> cbxTT) {
        cbxTT.removeAllItems();
        cbxTT.addItem("Tất cả");
        cbxTT.addItem("Đang hoạt động");
        cbxTT.addItem("Ngừng hoạt động");
    }

    // Lọc dữ liệu bảng tblNV dựa trên từ khóa và trạng thái tài khoản
    public static void LocTheoTrangThai(JTable tblNV, JTextField txtTimKiem, JComboBox<String> cbxTT) {
        String keyword = txtTimKiem.getText().trim();
        String trangThai = cbxTT.getSelectedItem().toString();

        DefaultTableModel model = (DefaultTableModel) tblNV.getModel();
        model.setRowCount(0);  // Xóa dữ liệu cũ

        StringBuilder sql = new StringBuilder("SELECT IDAccount, NameAccount, RoleAccount, CCCD, PhoneNumber, Email, Gender, AccountStatus " +
                                              "FROM Account WHERE RoleAccount IN ('ADMIN', 'BOSS')");

        if (!keyword.isEmpty()) {
            sql.append(" AND NameAccount LIKE ?");
        }

        if (!trangThai.equals("Tất cả")) {
            sql.append(" AND AccountStatus = ?");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int i = 1;

            if (!keyword.isEmpty()) {
                ps.setString(i++, "%" + keyword + "%");
            }

            if (!trangThai.equals("Tất cả")) {
                boolean status = trangThai.equals("Đang hoạt động");
                ps.setBoolean(i++, status);
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

            // Không cho sửa bảng và chỉ cho chọn một dòng
            tblNV.setDefaultEditor(Object.class, null);
            tblNV.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            tblNV.setRowSelectionAllowed(true);
            tblNV.setColumnSelectionAllowed(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
