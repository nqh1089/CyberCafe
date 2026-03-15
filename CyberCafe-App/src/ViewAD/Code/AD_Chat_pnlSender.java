package ViewAD.Code;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AD_Chat_pnlSender extends JPanel {

    private JPanel pnlSender;
    private JPanel pnlTittle;
    private JLabel lblTittle1;
    private JScrollPane scrollPane;

    public AD_Chat_pnlSender() {
        setLayout(null);
        setBackground(new Color(153, 255, 255));
        setPreferredSize(new Dimension(245, 700));

        pnlTittle = new JPanel();
        pnlTittle.setBackground(new Color(30, 30, 47));
        pnlTittle.setBounds(0, 0, 245, 55);
        pnlTittle.setLayout(null);

        lblTittle1 = new JLabel("CHAT", SwingConstants.CENTER);
        lblTittle1.setFont(new Font("Verdana", Font.BOLD, 18));
        lblTittle1.setForeground(new Color(153, 255, 255));
        lblTittle1.setBounds(0, 10, 245, 35);

        pnlTittle.add(lblTittle1);
        add(pnlTittle);

        pnlSender = new JPanel();
        pnlSender.setBackground(new Color(153, 255, 255));
        pnlSender.setLayout(null);

        scrollPane = new JScrollPane(pnlSender);
        scrollPane.setBounds(0, 55, 245, 645);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);

        add(scrollPane);

        LoadDanhSachChat();
    }

    private void LoadDanhSachChat() {
        pnlSender.removeAll();
        pnlSender.setPreferredSize(new Dimension(245, 0));

        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=CyberCafe4KL", "sa", "123456")) {
            String query = "SELECT NameComputer FROM Computer ORDER BY IDComputer";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            int y = 0;
            while (rs.next()) {
                String name = rs.getString("NameComputer");
                JPanel pnlItem = new JPanel();
                pnlItem.setBackground(new Color(30, 30, 47));
                pnlItem.setLayout(null);
                pnlItem.setBounds(0, y, 245, 45);

                JLabel lblTime = new JLabel(new SimpleDateFormat("HH:mm").format(new Date()));
                lblTime.setFont(new Font("Segoe UI", Font.BOLD, 10));
                lblTime.setForeground(new Color(153, 255, 255));
                lblTime.setBounds(180, 5, 60, 20);

                JLabel lblText = new JLabel(name + ": Order mới");
                lblText.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblText.setForeground(new Color(153, 255, 255));
                lblText.setBounds(19, 20, 200, 20);

                pnlItem.add(lblTime);
                pnlItem.add(lblText);

                pnlSender.add(pnlItem);
                y += 47;
            }

            pnlSender.setPreferredSize(new Dimension(245, y));
            pnlSender.revalidate();
            pnlSender.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
