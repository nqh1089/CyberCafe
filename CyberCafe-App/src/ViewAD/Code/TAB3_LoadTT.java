package ViewAD.Code;

import Controller.DBConnection;
import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TAB3_LoadTT {

    // Đường dẫn tuyệt đối tới thư mục ảnh sản phẩm
    private static final String DUONG_DAN_ANH = "E:/SU25/BL2/CyberCafe4KL/CyberCafe4KL/src/Assets/Products/";

    // Hàm chính để load bảng có lọc
    public static void LoadData(JTable table, String loaiSP, String trangThai) {
        String[] headers = {"Mã SP", "Ảnh SP", "Tên SP", "Loại SP", "Giá SP", "Trạng thái"};
        DefaultTableModel model = new DefaultTableModel(headers, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 1 ? ImageIcon.class : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM FoodDrink WHERE 1=1";

            if (!loaiSP.equals("Tất cả")) {
                sql += " AND Category = ?";
            }
            if (!trangThai.equals("Tất cả")) {
                sql += " AND Available = ?";
            }

            PreparedStatement stmt = conn.prepareStatement(sql);

            int index = 1;
            if (!loaiSP.equals("Tất cả")) {
                stmt.setString(index++, loaiSP);
            }
            if (!trangThai.equals("Tất cả")) {
                int status = trangThai.equals("Đang bán") ? 1 : 0;
                stmt.setInt(index++, status);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("IDFood");
                String imageName = rs.getString("ImageFood");
                String name = rs.getString("NameFood");
                String category = rs.getString("Category");
                int price = (int) rs.getDouble("Price");
                int available = rs.getInt("Available");

                ImageIcon icon = LoadIcon(imageName);

                String status = available == 1 ? "Đang bán" : "Ngừng bán";
                String giaDinhDang = DinhDangGia(price);

                model.addRow(new Object[]{id, icon, name, category, giaDinhDang, status});
            }

            table.setModel(model);
            table.setRowHeight(65);
            CanGiuaCot(table);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm tìm kiếm theo tên sản phẩm (áp dụng cả lọc)
    public static void TimKiemTheoTen(JTable table, String tuKhoa, String loaiSP, String trangThai) {
        String[] headers = {"Mã SP", "Ảnh SP", "Tên SP", "Loại SP", "Giá SP", "Trạng thái"};
        DefaultTableModel model = new DefaultTableModel(headers, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 1 ? ImageIcon.class : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM FoodDrink WHERE NameFood LIKE ?";
            if (!loaiSP.equals("Tất cả")) {
                sql += " AND Category = ?";
            }
            if (!trangThai.equals("Tất cả")) {
                sql += " AND Available = ?";
            }

            PreparedStatement stmt = conn.prepareStatement(sql);
            int index = 1;
            stmt.setString(index++, "%" + tuKhoa + "%");

            if (!loaiSP.equals("Tất cả")) {
                stmt.setString(index++, loaiSP);
            }
            if (!trangThai.equals("Tất cả")) {
                int status = trangThai.equals("Đang bán") ? 1 : 0;
                stmt.setInt(index++, status);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("IDFood");
                String imageName = rs.getString("ImageFood");
                String name = rs.getString("NameFood");
                String category = rs.getString("Category");
                int price = (int) rs.getDouble("Price");
                int available = rs.getInt("Available");

                ImageIcon icon = LoadIcon(imageName);
                String status = available == 1 ? "Đang bán" : "Ngừng bán";
                String giaDinhDang = DinhDangGia(price);

                model.addRow(new Object[]{id, icon, name, category, giaDinhDang, status});
            }

            table.setModel(model);
            table.setRowHeight(65);
            CanGiuaCot(table);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Định dạng giá thành 10 000
    private static String DinhDangGia(int gia) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(gia).replace(",", " ");
    }

    // Load ảnh sản phẩm
    private static ImageIcon LoadIcon(String imageName) {
        try {
            File file = new File(DUONG_DAN_ANH + imageName);
            if (!file.exists()) {
                System.err.println("Không tìm thấy ảnh: " + imageName);
                return null;
            }

            Image img = new ImageIcon(file.getAbsolutePath()).getImage();
            Image scaled = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);

        } catch (Exception e) {
            System.err.println("Lỗi khi load ảnh: " + imageName);
            return null;
        }
    }

    // Căn giữa các cột bảng
    private static void CanGiuaCot(JTable table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof ImageIcon icon) {
                    JLabel lbl = new JLabel(icon);
                    lbl.setHorizontalAlignment(JLabel.CENTER);
                    return lbl;
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });

        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != 1) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
    }
}
