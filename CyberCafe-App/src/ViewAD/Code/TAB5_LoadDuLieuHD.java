package ViewAD.Code;

import Controller.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public class TAB5_LoadDuLieuHD {

    public static void LoadHoaDonTheoThang(int thang, JTable table, JLabel lblSoHD, JLabel lblTongTien) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear bảng

        int tongSoHoaDon = 0;
        int tongTienThuDuoc = 0;

        try (Connection conn = DBConnection.getConnection()) {

            // PHẦN 1: Load hóa đơn dịch vụ từ OrderFood
            String sql = """
    SELECT ofd.IDOrder, ofd.OrderTime, ofd.IDAccount, A.NameAccount,
           SUM(od.Quantity) AS TongSL,
           SUM(od.Quantity * fd.Price) AS TongTien
    FROM OrderFood ofd
    JOIN OrderDetail od ON ofd.IDOrder = od.IDOrder
    JOIN FoodDrink fd ON od.IDFood = fd.IDFood
    JOIN Account A ON ofd.IDAccount = A.IDAccount
    WHERE MONTH(ofd.OrderTime) = ? AND YEAR(ofd.OrderTime) = ?
    GROUP BY ofd.IDOrder, ofd.IDAccount, ofd.OrderTime, A.NameAccount
""";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, thang);
            ps.setInt(2, java.time.LocalDate.now().getYear());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idHD = rs.getInt("IDOrder");
                int tongSL = rs.getInt("TongSL");
                int tongTien = rs.getInt("TongTien");
                String tenNV = rs.getString("NameAccount");
                String ngayTao = rs.getTimestamp("OrderTime").toLocalDateTime()
                        .format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

                tongSoHoaDon++;
                tongTienThuDuoc += tongTien;

                model.addRow(new Object[]{
                    "HD" + idHD,
                    tongSL,
                    DinhDangTien(tongTien),
                    tenNV,
                    ngayTao
                });
            }

            rs.close();
            ps.close();

            // PHẦN 2: Load thêm hóa đơn máy từ Invoice (logout)
            String sql2 = """
            SELECT I.IDInvoice, A.NameAccount, I.CreateAt, I.TotalAmount
            FROM Invoice I
            JOIN Account A ON I.IDAccount = A.IDAccount
            WHERE MONTH(I.CreateAt) = ? AND YEAR(I.CreateAt) = ?
        """;

            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, thang);
            ps2.setInt(2, java.time.LocalDate.now().getYear());

            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {
                int idInvoice = rs2.getInt("IDInvoice");
                String tenTK = rs2.getString("NameAccount");
                Timestamp ngayTaoRaw = rs2.getTimestamp("CreateAt");
                int tongTien = rs2.getInt("TotalAmount");

                String ngayTao = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(ngayTaoRaw);

                tongSoHoaDon++;
                tongTienThuDuoc += tongTien;

                model.addRow(new Object[]{
                    "HDM" + idInvoice,
                    "-", // Không có số lượng món
                    DinhDangTien(tongTien),
                    tenTK,
                    ngayTao
                });
            }

            rs2.close();
            ps2.close();

            // Tự động sort theo cột "Ngày tạo" sau khi load xong
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

            sorter.setComparator(4, new Comparator<String>() {
                public int compare(String d1, String d2) {
                    try {
                        return sdf.parse(d2).compareTo(sdf.parse(d1)); // DESC
                    } catch (Exception e) {
                        return 0;
                    }
                }
            });

            table.setRowSorter(sorter);
            sorter.toggleSortOrder(4); // Sort cột index 4 (ngày tạo)

        } catch (Exception e) {
            System.out.println("Lỗi khi load danh sách hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }

        lblSoHD.setText(String.valueOf(tongSoHoaDon));
        lblTongTien.setText(DinhDangTien(tongTienThuDuoc));
    }

    private static String DinhDangTien(int so) {
        return String.format("%,d", so).replace(",", " ");
    }
}
