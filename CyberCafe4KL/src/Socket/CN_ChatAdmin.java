package ViewAD.Code;

import Controller.DBConnection;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class CN_ChatAdmin {

    private static JPanel pnlDangChon = null;

    public static void HienThiGiaoDien(JPanel pnlMain) {
        pnlMain.removeAll();
        pnlMain.setLayout(new BorderLayout(4, 4)); // Khoảng cách giữa các panel
        pnlMain.setPreferredSize(new Dimension(650, 450)); // Chiều cao toàn form cố định

        // === Panel DANH SÁCH MÁY ===
        JPanel pnlDanhSachMay = new JPanel();
        pnlDanhSachMay.setBackground(new Color(44, 44, 62));
        pnlDanhSachMay.setLayout(new BoxLayout(pnlDanhSachMay, BoxLayout.Y_AXIS));
        pnlDanhSachMay.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(pnlDanhSachMay);
        scrollPane.setPreferredSize(new Dimension(235, 0)); // Chiều rộng cố định 235px
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80), 2));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JLabel lblTitle = new JLabel("DANH SÁCH MÁY");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.CYAN);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        pnlDanhSachMay.add(lblTitle);

        // === Load máy từ DB ===
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT IDComputer, NameComputer FROM Computer WHERE ComputerStatus IN (0,1,2)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String tenMay = rs.getString("NameComputer");

                JPanel pnlMay = new JPanel(new BorderLayout());
                pnlMay.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
                pnlMay.setBackground(new Color(30, 30, 47));
                pnlMay.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                JLabel lblTenMay = new JLabel(tenMay);
                lblTenMay.setForeground(Color.WHITE);
                lblTenMay.setFont(new Font("Segoe UI", Font.BOLD, 14));

                JLabel lblTrangThai = new JLabel("Tin nhắn mới");
                lblTrangThai.setForeground(Color.CYAN);
                lblTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                lblTrangThai.setHorizontalAlignment(SwingConstants.RIGHT);

                JPanel pnlTop = new JPanel(new BorderLayout());
                pnlTop.setOpaque(false);
                pnlTop.add(lblTenMay, BorderLayout.WEST);
                pnlTop.add(lblTrangThai, BorderLayout.EAST);

                JLabel lblTime = new JLabel("HH:mm");
                lblTime.setForeground(Color.LIGHT_GRAY);
                lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                lblTime.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

                JPanel pnlBottom = new JPanel(new BorderLayout());
                pnlBottom.setOpaque(false);
                pnlBottom.add(lblTime, BorderLayout.EAST);

                pnlMay.add(pnlTop, BorderLayout.CENTER);
                pnlMay.add(pnlBottom, BorderLayout.SOUTH);

                pnlMay.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (pnlDangChon != null) {
                            pnlDangChon.setBackground(new Color(30, 30, 47));
                        }
                        pnlMay.setBackground(new Color(60, 60, 90));
                        pnlDangChon = pnlMay;
                        HienThiKhungChat(pnlMain, tenMay);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (pnlMay != pnlDangChon) {
                            pnlMay.setBackground(new Color(50, 50, 70));
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (pnlMay != pnlDangChon) {
                            pnlMay.setBackground(new Color(30, 30, 47));
                        }
                    }
                });

                pnlDanhSachMay.add(pnlMay);
                pnlDanhSachMay.add(Box.createVerticalStrut(6));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // === Panel CHAT mặc định ===
        JPanel pnlChat = new JPanel(new BorderLayout());
        pnlChat.setBackground(new Color(30, 30, 47));
        pnlChat.setPreferredSize(new Dimension(380, 450));

        JLabel lblMacDinh = new JLabel("Chọn máy để bắt đầu chat", SwingConstants.CENTER);
        lblMacDinh.setForeground(Color.CYAN);
        lblMacDinh.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pnlChat.add(lblMacDinh, BorderLayout.CENTER);

        pnlMain.add(scrollPane, BorderLayout.WEST);
        pnlMain.add(pnlChat, BorderLayout.CENTER);

        pnlMain.revalidate();
        pnlMain.repaint();
    }

    private static void HienThiKhungChat(JPanel pnlMain, String tenMay) {
        JPanel pnlChat = new JPanel(new BorderLayout());
        pnlChat.setBackground(new Color(30, 30, 47));
        pnlChat.setPreferredSize(new Dimension(380, 450));

        JLabel lblTenMay = new JLabel(tenMay, SwingConstants.CENTER);
        lblTenMay.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTenMay.setForeground(Color.CYAN);
        lblTenMay.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlChat.add(lblTenMay, BorderLayout.NORTH);

        JTextArea txtChat = new JTextArea();
        txtChat.setEditable(false);
        txtChat.setBackground(new Color(30, 30, 47));
        txtChat.setForeground(Color.WHITE);
        txtChat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtChat.setLineWrap(true);
        txtChat.setWrapStyleWord(true);
        JScrollPane scrollChat = new JScrollPane(txtChat);
        scrollChat.setBorder(null);
        pnlChat.add(scrollChat, BorderLayout.CENTER);

        JPanel pnlSend = new JPanel(new BorderLayout());
        pnlSend.setBackground(new Color(30, 30, 47));
        pnlSend.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtSend = new JTextField();
        txtSend.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnSend = new JButton("GỬI");
        btnSend.setBackground(Color.CYAN);
        btnSend.setForeground(Color.BLACK);
        btnSend.setFocusPainted(false);

        pnlSend.add(txtSend, BorderLayout.CENTER);
        pnlSend.add(btnSend, BorderLayout.EAST);
        pnlChat.add(pnlSend, BorderLayout.SOUTH);

        // Replace chat panel
        pnlMain.remove(1);
        pnlMain.add(pnlChat, BorderLayout.CENTER);
        pnlMain.revalidate();
        pnlMain.repaint();

        SwingUtilities.invokeLater(txtSend::requestFocusInWindow);

        btnSend.addActionListener(e -> {
            String msg = txtSend.getText().trim();
            if (!msg.isEmpty()) {
                txtChat.append("ADMIN: " + msg + "\n");
                txtSend.setText("");
                // Xử lý gửi tin nhắn nếu có socket hoặc DB
            }
        });
    }
}
