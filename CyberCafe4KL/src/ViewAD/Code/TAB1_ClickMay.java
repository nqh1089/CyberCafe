package ViewAD.Code;

import Controller.DBConnection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TAB1_ClickMay {

    private JLabel lblTenMay, lblTrangThai, lblTimeStart, lblTimeEnd, lblTimeUsed, lblTamTinh, lblTongTien;
    private JTable tblOrder;

    public TAB1_ClickMay(JLabel lblTenMay, JLabel lblTrangThai, JLabel lblTimeStart, JLabel lblTimeEnd,
                         JLabel lblTimeUsed, JLabel lblTamTinh, JLabel lblTongTien, JTable tblOrder) {
        this.lblTenMay = lblTenMay;
        this.lblTrangThai = lblTrangThai;
        this.lblTimeStart = lblTimeStart;
        this.lblTimeEnd = lblTimeEnd;
        this.lblTimeUsed = lblTimeUsed;
        this.lblTamTinh = lblTamTinh;
        this.lblTongTien = lblTongTien;
        this.tblOrder = tblOrder;
    }

    public MouseAdapter getClickHandler(String tenMay) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadThongTinMay(tenMay);
            }
        };
    }

    public void loadThongTinMay(String tenMay) {
        lblTenMay.setText(tenMay.toUpperCase());

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return;

            int idMay = -1;
            int idAcc = -1;
            Timestamp startTime = null;

            // Lấy thông tin máy
            String sqlMay = "SELECT IDComputer, ComputerStatus, PricePerMinute FROM Computer WHERE NameComputer = ?";
            PreparedStatement ps = conn.prepareStatement(sqlMay);
            ps.setString(1, tenMay);
            ResultSet rs = ps.executeQuery();

            double pricePerMinute = 0;

            if (rs.next()) {
                idMay = rs.getInt("IDComputer");
                boolean dangDung = rs.getInt("ComputerStatus") == 0;
                pricePerMinute = rs.getDouble("PricePerMinute");
                lblTrangThai.setText(dangDung ? "Đang sử dụng" : "Trống");
            }

            // Lấy thông tin tài khoản đang dùng máy
            String sqlLog = "SELECT TOP 1 IDAccount, ThoiGianBatDau FROM LogAccess WHERE IDComputer = ? ORDER BY ThoiGianBatDau DESC";
            ps = conn.prepareStatement(sqlLog);
            ps.setInt(1, idMay);
            rs = ps.executeQuery();

            if (rs.next()) {
                idAcc = rs.getInt("IDAccount");
                startTime = rs.getTimestamp("ThoiGianBatDau");
            }

            // Hiển thị thời gian sử dụng nếu có
            if (startTime != null) {
                lblTimeStart.setText(formatTime(startTime));
                Timestamp now = new Timestamp(System.currentTimeMillis());
                lblTimeEnd.setText(formatTime(now));

                long usedMinutes = (now.getTime() - startTime.getTime()) / 60000;
                lblTimeUsed.setText(usedMinutes + " phút");

                double tamTinh = usedMinutes * pricePerMinute;
                lblTamTinh.setText(formatMoney(tamTinh));
            } else {
                lblTimeStart.setText("Chưa có");
                lblTimeEnd.setText("--:--");
                lblTimeUsed.setText("--:--");
                lblTamTinh.setText("0");
            }

            // Load bảng order nếu có
            if (idAcc != -1) {
                loadOrderSanPham(idAcc);
            } else {
                clearOrderTable();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadOrderSanPham(int idAcc) {
        DefaultTableModel model = (DefaultTableModel) tblOrder.getModel();
        model.setRowCount(0);
        double tongTien = 0;

        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                SELECT f.NameFood, f.Price, d.Quantity, d.TotalPrice
                FROM OrderFood o
                JOIN OrderDetail d ON o.IDOrder = d.IDOrder
                JOIN FoodDrink f ON d.IDFood = f.IDFood
                WHERE o.IDAccount = ?
            """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idAcc);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String ten = rs.getString("NameFood");
                double gia = rs.getDouble("Price");
                int sl = rs.getInt("Quantity");
                double thanhTien = rs.getDouble("TotalPrice");

                tongTien += thanhTien;
                model.addRow(new Object[]{
                    ten,
                    formatMoney(gia),
                    sl,
                    formatMoney(thanhTien)
                });
            }

            lblTongTien.setText(formatMoney(tongTien));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearOrderTable() {
        DefaultTableModel model = (DefaultTableModel) tblOrder.getModel();
        model.setRowCount(0);
        lblTongTien.setText("0");
    }

    public void resetThongTinMayChuaChon() {
        lblTenMay.setText("MÁY ");
        lblTrangThai.setText("--");
        lblTimeStart.setText("Chưa có");
        lblTimeEnd.setText("--:--");
        lblTimeUsed.setText("--:--");
        lblTamTinh.setText("0");
        lblTongTien.setText("0");

        DefaultTableModel model = (DefaultTableModel) tblOrder.getModel();
        model.setRowCount(0);
    }

    private String formatTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        return sdf.format(date);
    }

    private String formatMoney(double amount) {
        long value = (long) amount;
        String raw = Long.toString(value);
        StringBuilder sb = new StringBuilder();

        int count = 0;
        for (int i = raw.length() - 1; i >= 0; i--) {
            sb.insert(0, raw.charAt(i));
            count++;
            if (count == 3 && i != 0) {
                sb.insert(0, ' ');
                count = 0;
            }
        }

        return sb.toString();
    }
}
