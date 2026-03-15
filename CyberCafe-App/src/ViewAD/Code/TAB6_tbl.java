package ViewAD.Code;

import Controller.DBConnection;
import java.awt.Color;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class TAB6_tbl {

    // Set cột và màu cho 3 bảng
    public static void SetupAllTables(JTable tblSP, JScrollPane scrollSP,
            JTable tblNV, JScrollPane scrollNV,
            JTable tblMay, JScrollPane scrollMay) {

        String[] colsTopSP = {"ID", "Tên SP", "Tổng số lượng", "Doanh thu"};
        String[] colsTopNV = {"ID", "Tên", "Tổng số HD", "Doanh thu"};
        String[] colsTopMay = {"ID", "Số lượt bật", "Giờ hoạt động", "Doanh thu"};

        Color nenSang = new Color(242, 242, 242);
        Color chuDen = Color.BLACK;

        // Set model + style
        CN_SetupTable.SetTable(tblSP, scrollSP, colsTopSP);
        CN_SetupTable.SetTable(tblNV, scrollNV, colsTopNV);
        CN_SetupTable.SetTable(tblMay, scrollMay, colsTopMay);

        // === SP
        scrollSP.getViewport().setBackground(nenSang);
        scrollSP.setBackground(nenSang);
        tblSP.setBackground(nenSang);
        tblSP.setForeground(chuDen);

        // === NV
        scrollNV.getViewport().setBackground(nenSang);
        scrollNV.setBackground(nenSang);
        tblNV.setBackground(nenSang);
        tblNV.setForeground(chuDen);

        // === Máy
        scrollMay.getViewport().setBackground(nenSang);
        scrollMay.setBackground(nenSang);
        tblMay.setBackground(nenSang);
        tblMay.setForeground(chuDen);
    }

    public static void LoadTopSanPham(JTable table, int thang) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        String sql = """
            SELECT FD.IDFood, FD.NameFood, 
                   SUM(OD.Quantity) AS SoLuong, 
                   SUM(OD.TotalPrice) AS DoanhThu
            FROM OrderDetail OD
            JOIN FoodDrink FD ON OD.IDFood = FD.IDFood
            JOIN OrderFood OFD ON OD.IDOrder = OFD.IDOrder
            WHERE MONTH(OFD.OrderTime) = ?
            GROUP BY FD.IDFood, FD.NameFood
            ORDER BY SoLuong DESC
        """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ResultSet rs = ps.executeQuery();

            DecimalFormat formatter = getFormatter();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("IDFood"),
                    rs.getString("NameFood"),
                    rs.getInt("SoLuong"),
                    formatter.format(rs.getDouble("DoanhThu"))
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void LoadTopNhanVien(JTable table, int thang) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        String sql = """
            SELECT A.IDAccount, A.NameAccount,
                   COUNT(DISTINCT OFD.IDOrder) AS SoHD,
                   SUM(OD.TotalPrice) AS DoanhThu
            FROM Account A
            JOIN OrderFood OFD ON A.IDAccount = OFD.IDAccount
            JOIN OrderDetail OD ON OFD.IDOrder = OD.IDOrder
            WHERE A.RoleAccount IN ('ADMIN', 'BOSS')
              AND MONTH(OFD.OrderTime) = ?
            GROUP BY A.IDAccount, A.NameAccount
            ORDER BY DoanhThu DESC
        """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ResultSet rs = ps.executeQuery();

            DecimalFormat formatter = getFormatter();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("IDAccount"),
                    rs.getString("NameAccount"),
                    rs.getInt("SoHD"),
                    formatter.format(rs.getDouble("DoanhThu"))
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void LoadTopMay(JTable table, int thang) {
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

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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

                if (!rs.wasNull()) {
                    int logID = rs.getInt("IDLog");
                    may.soLuot++;
                }

                if (start != null) {
                    Timestamp usedEnd = (end != null) ? end : now;
                    long phut = (usedEnd.getTime() - start.getTime()) / (1000 * 60);
                    may.tongPhut += phut;
                    may.tongTien += (cost > 0) ? cost : (phut * gia);
                } else if (may.logStart == null && logStart != null) {
                    may.logStart = logStart;
                }

                map.put(id, may);
            }

            for (MayThongKe may : map.values()) {
                if (may.tongPhut == 0 && may.logStart != null) {
                    long phut = (now.getTime() - may.logStart.getTime()) / (1000 * 60);
                    may.tongPhut += phut;
                    may.tongTien += phut * may.gia;
                }
            }

            DecimalFormat formatter = getFormatter();

            List<Map.Entry<Integer, MayThongKe>> list = new ArrayList<>(map.entrySet());
            list.sort((a, b) -> Long.compare(b.getValue().tongPhut, a.getValue().tongPhut));

            for (Map.Entry<Integer, MayThongKe> entry : list) {
                int id = entry.getKey();
                MayThongKe may = entry.getValue();

                String doanhThuStr = formatter.format((long) may.tongTien);
                String phutStr = may.tongPhut + " phút";

                model.addRow(new Object[]{id, may.soLuot, phutStr, doanhThuStr});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static DecimalFormat getFormatter() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        return new DecimalFormat("#,###", symbols);
    }
}
