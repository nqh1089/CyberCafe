package ViewAD.Code;

import Model.Products;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TAB2_CardSP {

    public static JPanel taoCard(Products sp, JTable tableOrder,
                                 JTextField jTextField1, JTextField jTextField2, JTextField jTextField3) {

        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setPreferredSize(new Dimension(280, 130));
        card.setBackground(new Color(240, 240, 240));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // === ẢNH SẢN PHẨM ===
        JLabel lblImg = new JLabel();
        lblImg.setHorizontalAlignment(SwingConstants.CENTER);
        lblImg.setVerticalAlignment(SwingConstants.CENTER);
        lblImg.setPreferredSize(new Dimension(130, 130));

        try {
            ImageIcon icon = new ImageIcon("src/Assets/Products/" + sp.getHinhAnh());
            Image img = icon.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblImg.setText("No image");
            lblImg.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            lblImg.setForeground(Color.GRAY);
        }

        JPanel imgPanel = new JPanel(new BorderLayout());
        imgPanel.setPreferredSize(new Dimension(130, 130));
        imgPanel.setBackground(card.getBackground());
        imgPanel.add(lblImg, BorderLayout.CENTER);
        card.add(imgPanel, BorderLayout.WEST);

        // === THÔNG TIN SP ===
        JLabel lblTen = new JLabel(sp.getTenSP());
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTen.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblGia = new JLabel(formatTien(sp.getGia()));
        lblGia.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblGia.setForeground(Color.DARK_GRAY);
        lblGia.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSL = new JLabel("0");
        lblSL.setPreferredSize(new Dimension(30, 25));
        lblSL.setHorizontalAlignment(SwingConstants.CENTER);
        lblSL.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSL.setName("lblSoLuong");

        JButton btnMinus = new JButton("-");
        JButton btnPlus = new JButton("+");

        // === XỬ LÝ CỘNG TRỪ ===
        btnPlus.addActionListener(e -> {
            int sl = Integer.parseInt(lblSL.getText()) + 1;
            lblSL.setText(String.valueOf(sl));
            capNhatBang(sp, sl, tableOrder);
            capNhatTongTien(tableOrder, jTextField1, jTextField2, jTextField3);
        });

        btnMinus.addActionListener(e -> {
            int sl = Integer.parseInt(lblSL.getText());
            if (sl > 0) {
                sl--;
                lblSL.setText(String.valueOf(sl));
                capNhatBang(sp, sl, tableOrder);
                capNhatTongTien(tableOrder, jTextField1, jTextField2, jTextField3);
            }
        });

        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        qtyPanel.setBackground(card.getBackground());
        qtyPanel.add(btnMinus);
        qtyPanel.add(lblSL);
        qtyPanel.add(btnPlus);
        qtyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(card.getBackground());

        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(lblTen);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(lblGia);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(qtyPanel);
        infoPanel.add(Box.createVerticalGlue());

        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    private static void capNhatBang(Products sp, int soLuongMoi, JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        boolean daCo = false;

        for (int i = 0; i < model.getRowCount(); i++) {
            if (sp.getTenSP().equals(model.getValueAt(i, 0))) {
                daCo = true;

                if (soLuongMoi == 0) {
                    model.removeRow(i);
                } else {
                    model.setValueAt(sp.getGia(), i, 1);
                    model.setValueAt(soLuongMoi, i, 2);
                    model.setValueAt(sp.getGia() * soLuongMoi, i, 3);
                }
                break;
            }
        }

        if (!daCo && soLuongMoi > 0) {
            model.addRow(new Object[]{
                    sp.getTenSP(),
                    sp.getGia(),
                    soLuongMoi,
                    sp.getGia() * soLuongMoi
            });
        }
    }

    private static void capNhatTongTien(JTable table, JTextField tfTong, JTextField tfGiam, JTextField tfThanhToan) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int tong = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            Object val = model.getValueAt(i, 3);
            if (val != null) {
                try {
                    tong += Integer.parseInt(val.toString().replaceAll("\\D", ""));
                } catch (Exception ignored) {}
            }
        }

        tfTong.setText(formatTien(tong));
        tfGiam.setText("0");
        tfThanhToan.setText(formatTien(tong));
    }

    private static String formatTien(int soTien) {
        return String.format("%,d", soTien).replace(',', '.');
    }
}
