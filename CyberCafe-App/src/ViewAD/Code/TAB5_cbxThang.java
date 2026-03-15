package ViewAD.Code;

import Controller.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class TAB5_cbxThang {

    public static void LoadThangCoHD(JComboBox<String> cbx) {
        Set<Integer> danhSachThang = new TreeSet<>();
        int namHienTai = LocalDate.now().getYear();

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(
                "SELECT OrderTime FROM OrderFood WHERE YEAR(OrderTime) = ?")) {

            ps.setInt(1, namHienTai);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("OrderTime");
                if (ts != null) {
                    LocalDate localDate = ts.toLocalDateTime().toLocalDate();
                    danhSachThang.add(localDate.getMonthValue());
                }
            }

        } catch (Exception e) {
            System.out.println("Lỗi khi load tháng có hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }

        // Đổ vào ComboBox
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (int thang : danhSachThang) {
            model.addElement("Tháng " + thang);
        }
        cbx.setModel(model);

        // Tự động chọn tháng hiện tại nếu có
        int thangHT = LocalDate.now().getMonthValue();
        if (danhSachThang.contains(thangHT)) {
            cbx.setSelectedItem("Tháng " + thangHT);
        } else if (!danhSachThang.isEmpty()) {
            cbx.setSelectedIndex(danhSachThang.size() - 1);
        }
    }

    public static int LaySoThangDangChon(JComboBox<String> cbx) {
        String text = (String) cbx.getSelectedItem();
        if (text == null) {
            return -1;
        }
        try {
            return Integer.parseInt(text.replace("Tháng", "").trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
