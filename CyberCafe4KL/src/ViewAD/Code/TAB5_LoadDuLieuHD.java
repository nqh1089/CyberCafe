package ViewAD.Code;

import Controller.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class TAB5_LoadDuLieuHD {

    public static void LoadHoaDonTheoThang(int thang, JTable table, JLabel lblSoHD, JLabel lblTongTien) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear bảng

        int tongSoHoaDon = 0;
        int tongTienThuDuoc = 0;

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT ofd.IDOrder, ofd.IDAccount, ofd.OrderTime, "
                    + "SUM(od.Quantity) AS TongSL, "
                    + "SUM(od.Quantity * fd.Price) AS TongTien "
                    + "FROM OrderFood ofd "
                    + "JOIN OrderDetail od ON ofd.IDOrder = od.IDOrder "
                    + "JOIN FoodDrink fd ON od.IDFood = fd.IDFood "
                    + "WHERE MONTH(ofd.OrderTime) = ? AND YEAR(ofd.OrderTime) = ? "
                    + "GROUP BY ofd.IDOrder, ofd.IDAccount, ofd.OrderTime";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, thang);
            ps.setInt(2, java.time.LocalDate.now().getYear());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idHD = rs.getInt("IDOrder");
                int tongSL = rs.getInt("TongSL");
                int tongTien = rs.getInt("TongTien");
                int idNV = rs.getInt("IDAccount");
                String ngayTao = rs.getTimestamp("OrderTime").toLocalDateTime()
                        .format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

                tongSoHoaDon++;
                tongTienThuDuoc += tongTien;

                model.addRow(new Object[]{
                    "HD" + idHD,
                    tongSL,
                    DinhDangTien(tongTien),
                    "NV" + idNV,
                    ngayTao // Thêm dòng này
                });
            }

        } catch (Exception e) {
            System.out.println("Lỗi khi load danh sách hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }

        lblSoHD.setText(String.valueOf(tongSoHoaDon));
        lblTongTien.setText(DinhDangTien(tongTienThuDuoc)); // + " đ");
    }

    private static String DinhDangTien(int so) {
        return String.format("%,d", so).replace(",", " ");
    }
}
