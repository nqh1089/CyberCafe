package ViewAD.Code;

import Controller.DBConnection;
import java.sql.*;
import java.util.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class TAB6_TopMay {

    public static void LoadTable(JTable table, int thang) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        String sql = """
            SELECT C.IDComputer, C.NameComputer, C.PricePerMinute,
                   L.IDLog, L.ThoiGianBatDau,
                   CU.StartTime, CU.EndTime, CU.Cost
            FROM Computer C
            LEFT JOIN LogAccess L ON C.IDComputer = L.IDComputer AND MONTH(L.ThoiGianBatDau) = ?
            LEFT JOIN ComputerUsage CU ON C.IDComputer = CU.IDComputer AND MONTH(CU.StartTime) = ?
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, thang);
            ps.setInt(2, thang);
            ResultSet rs = ps.executeQuery();

            class MayThongKe {
                String name;
                int soLuot = 0;
                long tongPhut = 0;
                double tongTien = 0;
                Timestamp logStart = null;
                double gia = 0;
            }

            Map<Integer, MayThongKe> map = new HashMap<>();
            Timestamp now = new Timestamp(System.currentTimeMillis());

            while (rs.next()) {
                int id = rs.getInt("IDComputer");
                String name = rs.getString("NameComputer");
                double gia = rs.getDouble("PricePerMinute");

                Timestamp start = rs.getTimestamp("StartTime");
                Timestamp end = rs.getTimestamp("EndTime");
                double cost = rs.getDouble("Cost");

                Timestamp logStart = rs.getTimestamp("ThoiGianBatDau");

                MayThongKe may = map.getOrDefault(id, new MayThongKe());
                may.name = name;
                may.gia = gia;

                // Đếm lượt bật
                int logID = rs.getInt("IDLog");
                if (!rs.wasNull()) {
                    may.soLuot++;
                }

                // Có dòng ComputerUsage
                if (start != null) {
                    Timestamp usedEnd = (end != null) ? end : now;
                    long phut = (usedEnd.getTime() - start.getTime()) / (1000 * 60);
                    may.tongPhut += phut;
                    may.tongTien += (cost > 0) ? cost : (phut * gia);
                } else {
                    // Nếu chưa có ComputerUsage → lưu thời gian bắt đầu từ LogAccess
                    if (may.logStart == null && logStart != null) {
                        may.logStart = logStart;
                    }
                }

                map.put(id, may);
            }

            // Máy chưa có ComputerUsage → tính từ logStart đến now
            for (MayThongKe may : map.values()) {
                if (may.tongPhut == 0 && may.logStart != null) {
                    long phut = (now.getTime() - may.logStart.getTime()) / (1000 * 60);
                    may.tongPhut += phut;
                    may.tongTien += phut * may.gia;
                }
            }

            // Format doanh thu kiểu "10 000"
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            DecimalFormat formatter = new DecimalFormat("#,###", symbols);

            // Sắp xếp theo tổng phút giảm dần
            List<Map.Entry<Integer, MayThongKe>> list = new ArrayList<>(map.entrySet());
            list.sort((a, b) -> Long.compare(b.getValue().tongPhut, a.getValue().tongPhut));

            for (Map.Entry<Integer, MayThongKe> entry : list) {
                int id = entry.getKey();
                MayThongKe may = entry.getValue();

                String doanhThuStr = formatter.format((long) may.tongTien);
                String phutStr = may.tongPhut + " phút";

                model.addRow(new Object[]{
                    id,
                    may.soLuot,
                    phutStr,
                    doanhThuStr
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
