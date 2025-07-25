package ViewAD.Code;

import java.awt.Color;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class CN_SetupTable {

    // Set bảng với tiêu đề truyền vào
    public static void SetTable(JTable table, JScrollPane scrollPane, String[] columnNames) {
        Color nenToi = new Color(30, 30, 47);
        Color chuTrang = Color.WHITE;
        Color titleDen = Color.BLACK;

        // Set model
        DefaultTableModel model = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);

        // Màu bảng
        table.setBackground(nenToi);
        table.setForeground(chuTrang);
        table.setGridColor(new Color(70, 70, 90));

        table.setSelectionBackground(new Color(100, 149, 237));
        table.setSelectionForeground(Color.WHITE);

        table.setCellSelectionEnabled(false);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Căn giữa tiêu đề
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getTableHeader().setForeground(titleDen);

        // ScrollPane nền tối
        scrollPane.getViewport().setBackground(nenToi);
        scrollPane.setBackground(nenToi);

        // Format tiền cho các cột nếu tên cột là "Đơn giá" hoặc "Thành tiền"
        for (int i = 0; i < columnNames.length; i++) {
            String tenCot = columnNames[i].trim().toLowerCase();
            if (tenCot.equals("đơn giá") || tenCot.equals("thành tiền")) {
                table.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
                    @Override
                    protected void setValue(Object value) {
                        try {
                            if (value != null && !value.toString().isEmpty()) {
                                int soTien = Integer.parseInt(value.toString().replaceAll("[^0-9]", ""));
                                setText(formatTien(soTien));
                            } else {
                                setText("0");
                            }
                        } catch (Exception e) {
                            setText(value.toString());
                        }
                    }
                });
            }
        }
    }

    private static String formatTien(int soTien) {
        return String.format("%,d", soTien).replace(',', ' ');
    }
}
