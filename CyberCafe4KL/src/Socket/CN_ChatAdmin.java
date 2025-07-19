package Socket;

import Controller.DBConnection;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class CN_ChatAdmin {

    private static JPanel pnlDangChon = null;

    // === Đây là biến static sẽ lưu trữ tham chiếu đến pnlMain từ AD_Chat ===
    private static JPanel mainContainerPanelReference; // Dòng này ĐÚNG

    // === Static Maps để quản lý trạng thái UI ===
    private static Map<Integer, JPanel> clientPanelMap = new HashMap<>();
    private static Map<Integer, JLabel> clientStatusLabelMap = new HashMap<>();
    private static Map<Integer, JLabel> clientTimeLabelMap = new HashMap<>();
    private static Map<Integer, JTextArea> chatHistoryAreas = new HashMap<>();
    private static int selectedClientComputerId = -1;
    private static JTextField currentTxtSend;
    private static JTextArea currentTxtChat;
    private static JLabel currentLblChattingWith;
    private static JPanel currentChatPanelDisplay;

    // Biến này KHÔNG ĐƯỢC CÓ Ở ĐÂY nếu bạn muốn nó là static hoặc được truyền vào
    // private javax.swing.JPanel pnlMain; // Dòng này GÂY LỖI, HÃY XÓA NÓ ĐI!

    public static java.util.function.Consumer<String> onSendMessageFromUI;

    public static void HienThiGiaoDien(JPanel pnlMainParameter) { // Đổi tên tham số để tránh nhầm lẫn
        // === LƯU THAM CHIẾU VÀO BIẾN STATIC ===
        CN_ChatAdmin.mainContainerPanelReference = pnlMainParameter; // Gán tham số vào biến static

        pnlMainParameter.removeAll();
        pnlMainParameter.setLayout(new BorderLayout(4, 4));
        pnlMainParameter.setPreferredSize(new Dimension(650, 450));

        JPanel pnlDanhSachMay = new JPanel();
        pnlDanhSachMay.setBackground(new Color(44, 44, 62));
        pnlDanhSachMay.setLayout(new BoxLayout(pnlDanhSachMay, BoxLayout.Y_AXIS));
        pnlDanhSachMay.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(pnlDanhSachMay);
        scrollPane.setPreferredSize(new Dimension(235, 0));
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

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT IDComputer, NameComputer FROM Computer WHERE ComputerStatus IN (0,1,2)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idComputer = rs.getInt("IDComputer");
                String tenMay = rs.getString("NameComputer");
                addClientInternal(pnlDanhSachMay, idComputer, tenMay, "Chưa online", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        currentChatPanelDisplay = new JPanel(new BorderLayout());
        currentChatPanelDisplay.setBackground(new Color(30, 30, 47));
        currentChatPanelDisplay.setPreferredSize(new Dimension(380, 450));

        JLabel lblMacDinh = new JLabel("Chọn máy để bắt đầu chat", SwingConstants.CENTER);
        lblMacDinh.setForeground(Color.CYAN);
        lblMacDinh.setFont(new Font("Segoe UI", Font.BOLD, 18));
        currentChatPanelDisplay.add(lblMacDinh, BorderLayout.CENTER);

        pnlMainParameter.add(scrollPane, BorderLayout.WEST);
        pnlMainParameter.add(currentChatPanelDisplay, BorderLayout.CENTER);

        pnlMainParameter.revalidate();
        pnlMainParameter.repaint();
    }
    
    private static void addClientInternal(JPanel pnlDanhSachMay, int idComputer, String tenMay, String statusText, String accountName) {
        JPanel pnlMay = new JPanel(new BorderLayout());
        pnlMay.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        pnlMay.setBackground(new Color(30, 30, 47));
        pnlMay.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel lblTenMay = new JLabel(tenMay + (accountName.isEmpty() ? "" : " (" + accountName + ")"));
        lblTenMay.setForeground(Color.WHITE);
        lblTenMay.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel lblTrangThai = new JLabel(statusText);
        lblTrangThai.setForeground(Color.CYAN);
        lblTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTrangThai.setHorizontalAlignment(SwingConstants.RIGHT);
        clientStatusLabelMap.put(idComputer, lblTrangThai);

        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setOpaque(false);
        pnlTop.add(lblTenMay, BorderLayout.WEST);
        pnlTop.add(lblTrangThai, BorderLayout.EAST);

        JLabel lblTime = new JLabel("");
        lblTime.setForeground(Color.LIGHT_GRAY);
        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTime.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
        clientTimeLabelMap.put(idComputer, lblTime);

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

                selectedClientComputerId = idComputer;
                // Gọi HienThiKhungChat với biến static mainContainerPanelReference
                HienThiKhungChat(mainContainerPanelReference, tenMay + (accountName.isEmpty() ? "" : " (" + accountName + ")"));

                lblTrangThai.setText("");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (pnlMay.getBackground().getRGB() != new Color(60, 60, 90).getRGB()) {
                    pnlMay.setBackground(new Color(50, 50, 70));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (pnlMay.getBackground().getRGB() != new Color(60, 60, 90).getRGB()) {
                    pnlMay.setBackground(new Color(30, 30, 47));
                }
            }
        });

        pnlDanhSachMay.add(pnlMay);
        pnlDanhSachMay.add(Box.createVerticalStrut(6));
        pnlDanhSachMay.revalidate();
        pnlDanhSachMay.repaint();
        
        clientPanelMap.put(idComputer, pnlMay);
        JTextArea newChatArea = new JTextArea();
        newChatArea.setEditable(false);
        newChatArea.setBackground(new Color(30, 30, 47));
        newChatArea.setForeground(Color.WHITE);
        newChatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        newChatArea.setLineWrap(true);
        newChatArea.setWrapStyleWord(true);
        chatHistoryAreas.put(idComputer, newChatArea);
    }

    private static void HienThiKhungChat(JPanel pnlMainParameter, String tenMayHienThi) { // Đổi tên tham số
        JPanel pnlChat = new JPanel(new BorderLayout());
        pnlChat.setBackground(new Color(30, 30, 47));
        pnlChat.setPreferredSize(new Dimension(380, 450));

        currentLblChattingWith = new JLabel(tenMayHienThi, SwingConstants.CENTER);
        currentLblChattingWith.setFont(new Font("Segoe UI", Font.BOLD, 18));
        currentLblChattingWith.setForeground(Color.CYAN);
        currentLblChattingWith.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlChat.add(currentLblChattingWith, BorderLayout.NORTH);

        currentTxtChat = chatHistoryAreas.get(selectedClientComputerId);
        if (currentTxtChat == null) {
            currentTxtChat = new JTextArea();
            currentTxtChat.setEditable(false);
            currentTxtChat.setBackground(new Color(30, 30, 47));
            currentTxtChat.setForeground(Color.WHITE);
            currentTxtChat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            currentTxtChat.setLineWrap(true);
            currentTxtChat.setWrapStyleWord(true);
            chatHistoryAreas.put(selectedClientComputerId, currentTxtChat);
        }
        JScrollPane scrollChat = new JScrollPane(currentTxtChat);
        scrollChat.setBorder(null);
        pnlChat.add(scrollChat, BorderLayout.CENTER);

        JPanel pnlSend = new JPanel(new BorderLayout());
        pnlSend.setBackground(new Color(30, 30, 47));
        pnlSend.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        currentTxtSend = new JTextField();
        currentTxtSend.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnSend = new JButton("GỬI");
        btnSend.setBackground(Color.CYAN);
        btnSend.setForeground(Color.BLACK);
        btnSend.setFocusPainted(false);

        pnlSend.add(currentTxtSend, BorderLayout.CENTER);
        pnlSend.add(btnSend, BorderLayout.EAST);
        pnlChat.add(pnlSend, BorderLayout.SOUTH);

        // Replace chat panel
        pnlMainParameter.remove(currentChatPanelDisplay);
        pnlMainParameter.add(pnlChat, BorderLayout.CENTER);
        currentChatPanelDisplay = pnlChat;
        
        pnlMainParameter.revalidate();
        pnlMainParameter.repaint();

        SwingUtilities.invokeLater(currentTxtSend::requestFocusInWindow);

        btnSend.addActionListener(e -> {
            String msg = currentTxtSend.getText().trim();
            if (!msg.isEmpty()) {
                if (onSendMessageFromUI != null) {
                    onSendMessageFromUI.accept(msg);
                }
                appendMessageToChatArea(currentTxtChat, "Bạn (Admin)", msg, LocalTime.now().withNano(0).toString().substring(0, 5));
                currentTxtSend.setText("");
            }
        });
        currentTxtSend.addActionListener(btnSend.getActionListeners()[0]);
    }
    
    public static void addOrUpdateClient(CM_CN_OnlineClientInfo clientInfo) {
        SwingUtilities.invokeLater(() -> {
            // Kiểm tra mainContainerPanelReference có null không trước khi sử dụng
            if (mainContainerPanelReference == null) {
                System.err.println("Lỗi: mainContainerPanelReference trong CN_ChatAdmin chưa được khởi tạo!");
                return;
            }

            JPanel existingPanel = clientPanelMap.get(clientInfo.getIdComputer());
            if (existingPanel != null) {
                JLabel lblTenMay = (JLabel)((JPanel)existingPanel.getComponent(0)).getComponent(0);
                lblTenMay.setText(clientInfo.getNameComputer() + " (" + clientInfo.getNameAccount() + ")");
                JLabel statusLabel = clientStatusLabelMap.get(clientInfo.getIdComputer());
                if(statusLabel != null) {
                    statusLabel.setText("Đang online");
                    statusLabel.setForeground(Color.GREEN);
                }
                System.out.println("AD_Chat UI: Đã cập nhật client online " + clientInfo.getNameComputer());
            } else {
                JPanel pnlDanhSachMay = (JPanel)((JViewport)((JScrollPane)mainContainerPanelReference.getComponent(0)).getViewport()).getView();
                addClientInternal(pnlDanhSachMay, clientInfo.getIdComputer(), clientInfo.getNameComputer(), "Đang online", clientInfo.getNameAccount());
                System.out.println("AD_Chat UI: Đã thêm client online " + clientInfo.getNameComputer());
            }
        });
    }

    public static void removeClient(int computerId) {
        SwingUtilities.invokeLater(() -> {
            // Kiểm tra mainContainerPanelReference có null không trước khi sử dụng
            if (mainContainerPanelReference == null) {
                System.err.println("Lỗi: mainContainerPanelReference trong CN_ChatAdmin chưa được khởi tạo!");
                return;
            }
            
            JPanel pnlMayToRemove = clientPanelMap.get(computerId);
            if (pnlMayToRemove != null) {
                JPanel pnlDanhSachMay = (JPanel)((JViewport)((JScrollPane)mainContainerPanelReference.getComponent(0)).getViewport()).getView();
                pnlDanhSachMay.remove(pnlMayToRemove);
                pnlDanhSachMay.revalidate();
                pnlDanhSachMay.repaint();
            }
            clientPanelMap.remove(computerId);
            clientStatusLabelMap.remove(computerId);
            clientTimeLabelMap.remove(computerId);
            chatHistoryAreas.remove(computerId);
            
            if (selectedClientComputerId == computerId) {
                selectedClientComputerId = -1;
                if (currentLblChattingWith != null) {
                    currentLblChattingWith.setText("Chọn máy để bắt đầu chat");
                }
                JTextArea defaultChatArea = new JTextArea();
                defaultChatArea.setEditable(false);
                defaultChatArea.setBackground(new Color(30, 30, 47));
                defaultChatArea.setForeground(Color.LIGHT_GRAY);
                defaultChatArea.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                defaultChatArea.setText("\n\n\tChào mừng bạn đến với Admin Chat!\n\tVui lòng chọn một máy từ danh sách.");
                defaultChatArea.setLineWrap(true);
                defaultChatArea.setWrapStyleWord(true);
                
                if (currentChatPanelDisplay != null && currentChatPanelDisplay.getComponentCount() > 1 && currentChatPanelDisplay.getComponent(1) instanceof JScrollPane) {
                     JScrollPane currentScrollPane = (JScrollPane) currentChatPanelDisplay.getComponent(1);
                     currentScrollPane.setViewportView(defaultChatArea);
                     currentScrollPane.revalidate();
                     currentScrollPane.repaint();
                }
               
                if (currentTxtSend != null) {
                    currentTxtSend.setEnabled(false);
                    currentTxtSend.setText("");
                }
                if (currentChatPanelDisplay != null && currentChatPanelDisplay.getComponentCount() > 2 && currentChatPanelDisplay.getComponent(2) instanceof JPanel) {
                    JPanel pnlSend = (JPanel) currentChatPanelDisplay.getComponent(2);
                    if (pnlSend.getComponentCount() > 1 && pnlSend.getComponent(1) instanceof JButton) {
                        JButton currentBtnSend = (JButton) pnlSend.getComponent(1);
                        currentBtnSend.setEnabled(false);
                    }
                }
            }
            System.out.println("AD_Chat UI: Đã xóa client ID " + computerId);
        });
    }

    public static void displayNewMessage(String senderComputerName, String content, String time) {
        SwingUtilities.invokeLater(() -> {
            int senderComputerId = -1;
            for (Map.Entry<Integer, JPanel> entry : clientPanelMap.entrySet()) {
                JLabel lblTenMay = (JLabel)((JPanel)entry.getValue().getComponent(0)).getComponent(0);
                if (lblTenMay.getText().startsWith(senderComputerName + " (") || lblTenMay.getText().equals(senderComputerName)) {
                    senderComputerId = entry.getKey();
                    break;
                }
            }

            if (senderComputerId != -1) {
                JTextArea chatAreaForClient = chatHistoryAreas.get(senderComputerId);
                if (chatAreaForClient == null) {
                    chatAreaForClient = new JTextArea();
                    chatAreaForClient.setEditable(false);
                    chatHistoryAreas.put(senderComputerId, chatAreaForClient);
                }
                
                appendMessageToChatArea(chatAreaForClient, senderComputerName, content, time);
                
                JLabel statusLabel = clientStatusLabelMap.get(senderComputerId);
                if (statusLabel != null && selectedClientComputerId != senderComputerId) {
                    statusLabel.setText("Tin nhắn mới");
                    statusLabel.setForeground(Color.ORANGE);
                }
                JLabel timeLabel = clientTimeLabelMap.get(senderComputerId);
                if (timeLabel != null) {
                    timeLabel.setText(time);
                }

                if (selectedClientComputerId == senderComputerId && currentTxtChat != null) {
                    currentTxtChat.setCaretPosition(currentTxtChat.getDocument().getLength());
                }
            }
        });
    }
    
    private static void appendMessageToChatArea(JTextArea chatArea, String sender, String content, String time) {
        chatArea.append(sender + " [" + time + "]: " + content + "\n");
    }

    public static int getSelectedClientComputerId() {
        return selectedClientComputerId;
    }
}