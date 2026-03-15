package Socket;

import Controller.DBConnection; // Đảm bảo lớp này tồn tại và hoạt động
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import java.time.LocalDate;
import java.time.ZoneId;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class CN_ChatAdmin {

    private static JPanel ncPnlDangChon = null; // Panel máy đang được chọn trong danh sách
    public static NC_ChatServer ncChatServer; // Thay đổi từ private sang public để AD_Chat có thể truy cập
    private static int ncCurrentSelectedComputerId = -1; // ID của máy đang được chọn để chat
    private static Integer ncCurrentSelectedClientAccountId = 0; // ID Account của client đang được chọn (0 nếu không có)
    private static JTextPane ncTxtChatDisplay; // Thay JTextArea bằng JTextPane để đổi màu
    private static JTextField ncTxtSendMessage;
    private static JPanel ncPnlChatContent; // Panel chứa khung chat và input
    private static JLabel ncLblTenMayChatHeader; // Label hiển thị tên máy đang chat

    // Map để lưu trữ các JPanel của từng máy trong danh sách, key là IDComputer
    private static final Map<Integer, JPanel> ncMapPnlMay = new HashMap<>();
    // Map để lưu trữ các JLabel lblTrangThai của từng máy, key là IDComputer
    private static final Map<Integer, JLabel> ncMapLblTrangThai = new HashMap<>();
    // Map để lưu trữ các JLabel lblTime của từng máy, key là IDComputer
    private static final Map<Integer, JLabel> ncMapLblTime = new HashMap<>();
    // Map để lưu trữ tên máy tính theo ID, tiện cho việc truy xuất (từ DB)
    private static final Map<Integer, String> ncMapComputerNames = new HashMap<>();
    // Map để lưu trữ ID Account của client đang đăng nhập trên mỗi máy
    // Đồng bộ với dữ liệu trong NC_ChatServer.loggedInAccountsOnComputers
    private static final Map<Integer, Integer> ncMapLoggedInClientAccounts = new HashMap<>();

    // ID và tên tài khoản của Admin đang đăng nhập
    private static int ncAdminAccountId = 1; // THAY THẾ BẰNG ID ADMIN THỰC TẾ (ví dụ: từ màn hình đăng nhập)
    private static String ncAdminAccountName = "Admin"; // THAY THẾ BẰNG TÊN ADMIN THỰC TẾ

    // Set thông tin tài khoản Admin sau khi đăng nhập thành công.
    public static void SetAdminInfo(int id, String name) {
        ncAdminAccountId = id;
        ncAdminAccountName = name;
        if (ncChatServer != null) {
            ncChatServer.setAdminAccountId(id); // Cập nhật ID Admin cho server nếu đã khởi tạo
        }
    }

    // Lấy ID Account của Admin.
    public static int GetAdminAccountId() {
        return ncAdminAccountId;
    }

    // Lấy tên tài khoản Admin. Nếu chưa được set, sẽ cố gắng load từ DB.
    public static String GetAdminAccountName() {
        // Chỉ tải từ DB nếu tên Admin chưa được set hoặc vẫn là giá trị mặc định
        if (ncAdminAccountName == null || ncAdminAccountName.equals("ADMIN") || ncAdminAccountName.isEmpty()) {
            String name = null;
            String sql = "SELECT NameAccount FROM Account WHERE IDAccount = ?";
            try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, ncAdminAccountId);
                try (ResultSet rs = ps.executeQuery()) { // Đảm bảo ResultSet được đóng
                    if (rs.next()) {
                        name = rs.getString("NameAccount");
                    }
                }
            } catch (SQLException e) {
                System.err.println("CN_ChatAdmin: Lỗi khi lấy tên Admin từ DB: " + e.getMessage());
                e.printStackTrace();
            }
            if (name != null && !name.isEmpty()) {
                ncAdminAccountName = name;
            } else {
                // Fallback nếu không tìm thấy trong DB
                ncAdminAccountName = "Admin_" + ncAdminAccountId;
            }
        }
        return ncAdminAccountName;
    }

    // Hiển thị giao diện chat cho Admin.
    public static void HienThiGiaoDien(JPanel pnlMain) {
        pnlMain.removeAll();
        pnlMain.setLayout(new BorderLayout(4, 4));
        pnlMain.setPreferredSize(new Dimension(650, 450));

        // Chỉ kiểm tra nếu server có sẵn từ nút đăng nhập
        if (ncChatServer == null) {
            System.err.println("Server Chat chưa khởi động! Vui lòng đăng nhập Admin trước.");
            JOptionPane.showMessageDialog(null, "Hệ thống Chat chưa được khởi động. Vui lòng đăng nhập Admin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return; // Không làm tiếp nếu không có server
        }

        // Nếu có, gán lại callbacks nếu cần (OK)
        ncChatServer.setAdminAccountId(GetAdminAccountId());
        ncChatServer.setMessageProcessor(CN_ChatAdmin::processIncomingMessage);
        ncChatServer.setStatusListener(status -> System.out.println("Server Status: " + status));
        ncChatServer.setClientStatusUpdater((computerId, computerName, accountId, accountName, isOnline) -> {
            CapNhatTrangThaiMay(computerId, computerName, accountId, accountName, isOnline);
            if (isOnline && accountId != null && accountId != 0) {
                ncMapLoggedInClientAccounts.put(computerId, accountId);
            } else {
                ncMapLoggedInClientAccounts.remove(computerId);
            }
        });

        // === Panel DANH SÁCH MÁY ===
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

        // === Load máy từ DB ===
        ncMapPnlMay.clear(); // Xóa dữ liệu cũ nếu hiển thị lại
        ncMapLblTrangThai.clear();
        ncMapLblTime.clear();
        ncMapComputerNames.clear();
        ncMapLoggedInClientAccounts.clear(); // Xóa trạng thái đăng nhập cũ

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT IDComputer, NameComputer, ComputerStatus FROM Computer ORDER BY IDComputer ASC"); ResultSet rs = ps.executeQuery()) { // Đảm bảo ResultSet được đóng

            while (rs.next()) {
                int idMay = rs.getInt("IDComputer");
                String tenMay = rs.getString("NameComputer");
                int computerStatusDB = rs.getInt("ComputerStatus"); // 0: Đang sử dụng | 1: Đang trống | 2: Bảo trì

                ncMapComputerNames.put(idMay, tenMay); // Lưu tên máy vào map

                JPanel pnlMay = new JPanel(new BorderLayout());
                pnlMay.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
                pnlMay.setBackground(new Color(30, 30, 47));
                pnlMay.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                JLabel lblTenMay = new JLabel(tenMay);
                lblTenMay.setForeground(Color.WHITE);
                lblTenMay.setFont(new Font("Segoe UI", Font.BOLD, 14));

                JLabel lblTrangThai = new JLabel("");
                lblTrangThai.setForeground(Color.RED); // Mặc định là Offline
                lblTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                lblTrangThai.setHorizontalAlignment(SwingConstants.RIGHT);
                ncMapLblTrangThai.put(idMay, lblTrangThai); // Lưu để cập nhật

                JLabel lblTime = new JLabel("HH:mm"); // Sẽ cập nhật thời gian tin nhắn mới nhất
                lblTime.setForeground(Color.LIGHT_GRAY);
                lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                lblTime.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
                ncMapLblTime.put(idMay, lblTime); // Lưu để cập nhật

                JPanel pnlTop = new JPanel(new BorderLayout());
                pnlTop.setOpaque(false);
                pnlTop.add(lblTenMay, BorderLayout.WEST);
                pnlTop.add(lblTrangThai, BorderLayout.EAST);

                JPanel pnlBottom = new JPanel(new BorderLayout());
                pnlBottom.setOpaque(false);
                pnlBottom.add(lblTime, BorderLayout.EAST);

                pnlMay.add(pnlTop, BorderLayout.CENTER);
                pnlMay.add(pnlBottom, BorderLayout.SOUTH);

                pnlMay.putClientProperty("IDComputer", idMay);
                pnlMay.putClientProperty("NameComputer", tenMay);

                // Cập nhật trạng thái ban đầu dựa vào DB ComputerStatus
                // Trạng thái online/offline thực tế sẽ được cập nhật từ NC_ChatServer
                String initialStatusText = "";
                Color initialStatusColor = Color.RED; // Mặc định là Offline
                if (computerStatusDB == 0) { // Đang sử dụng
                    initialStatusText = "Đang sử dụng";
                    initialStatusColor = Color.ORANGE;
                } else if (computerStatusDB == 1) { // Đang trống
                    initialStatusText = "Đang trống";
                    initialStatusColor = Color.GRAY;
                } else if (computerStatusDB == 2) { // Bảo trì
                    initialStatusText = "Bảo trì";
                    initialStatusColor = Color.YELLOW;
                }
                lblTrangThai.setText(initialStatusText);
                lblTrangThai.setForeground(initialStatusColor);

                // Load thời gian tin nhắn cuối cùng (nếu có)
                NC_Message lastMsg = LayTinNhanGanNhat(GetAdminAccountId(), idMay);
                if (lastMsg != null) {
                    lblTime.setText(new SimpleDateFormat("HH:mm").format(lastMsg.getSentAt()));
                }

                pnlMay.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (ncPnlDangChon != null) {
                            ncPnlDangChon.setBackground(new Color(30, 30, 47));
                            // Đảm bảo lblTrangThai của pnlDangChon cũ trở về màu bình thường nếu nó là "Tin nhắn mới"
                            int oldIdComputer = (int) ncPnlDangChon.getClientProperty("IDComputer");
                            JLabel oldLblTrangThai = ncMapLblTrangThai.get(oldIdComputer);
                            if (oldLblTrangThai != null && oldLblTrangThai.getForeground().equals(new Color(255, 165, 0))) { // Màu cam
                                // Gọi lại hàm cập nhật trạng thái thực tế sau khi click vào
                                Integer oldAccountId = ncMapLoggedInClientAccounts.getOrDefault(oldIdComputer, 0);
                                String oldAccountName = (ncChatServer != null && oldAccountId != 0) ? ncChatServer.LayTenTaiKhoanTuID(oldAccountId) : null;
                                CapNhatTrangThaiMay(oldIdComputer, ncMapComputerNames.get(oldIdComputer), oldAccountId, oldAccountName, ncChatServer.IsComputerOnline(oldIdComputer));
                            }
                        }
                        pnlMay.setBackground(new Color(60, 60, 90));
                        ncPnlDangChon = pnlMay;

                        int clickedIdComputer = (int) pnlMay.getClientProperty("IDComputer");
                        String clickedTenMay = (String) pnlMay.getClientProperty("NameComputer");

                        // Cập nhật ID máy đang chọn
                        ncCurrentSelectedComputerId = clickedIdComputer;
                        // Lấy ID Account của client đang chọn từ map đã được cập nhật bởi server
                        ncCurrentSelectedClientAccountId = ncMapLoggedInClientAccounts.getOrDefault(clickedIdComputer, 0);

                        HienThiNCKhungChat(pnlMain, clickedIdComputer, clickedTenMay, ncCurrentSelectedClientAccountId);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (pnlMay != ncPnlDangChon) {
                            pnlMay.setBackground(new Color(50, 50, 70));
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (pnlMay != ncPnlDangChon) {
                            pnlMay.setBackground(new Color(30, 30, 47));
                        }
                    }
                });

                pnlDanhSachMay.add(pnlMay);
                pnlDanhSachMay.add(Box.createVerticalStrut(6));
                ncMapPnlMay.put(idMay, pnlMay);
            }

        } catch (SQLException e) {
            System.err.println("CN_ChatAdmin: Lỗi SQL khi tải danh sách máy từ DB: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("CN_ChatAdmin: Lỗi tổng quát khi tải danh sách máy: " + e.getMessage());
            e.printStackTrace();
        }

        // === Panel CHAT mặc định ===
        ncPnlChatContent = new JPanel(new BorderLayout());
        ncPnlChatContent.setBackground(new Color(30, 30, 47));
        ncPnlChatContent.setPreferredSize(new Dimension(380, 450));

        JLabel lblMacDinh = new JLabel("Chọn máy để bắt đầu chat", SwingConstants.CENTER);
        lblMacDinh.setForeground(Color.CYAN);
        lblMacDinh.setFont(new Font("Segoe UI", Font.BOLD, 18));
        ncPnlChatContent.add(lblMacDinh, BorderLayout.CENTER);

        pnlMain.add(scrollPane, BorderLayout.WEST);
        pnlMain.add(ncPnlChatContent, BorderLayout.CENTER);

        pnlMain.revalidate();
        pnlMain.repaint();
    }

    // Phương thức tĩnh để đóng Server Chat một cách an toàn. Được gọi từ bên ngoài (ví dụ: AD_Chat) khi ứng dụng Admin đóng.
    public static void DongServerChat() {
        if (ncChatServer != null) {
            ncChatServer.stopServer(); // Sử dụng phương thức stopServer của NC_ChatServer
            ncChatServer = null; // Đặt về null để cho phép khởi tạo lại nếu cần
        }
    }

    // Hiển thị khung chat cho một máy cụ thể.
    private static void HienThiNCKhungChat(JPanel pnlMain, int idComputer, String tenMay, Integer idClientAccount) {
        ncPnlChatContent.removeAll();
        ncPnlChatContent.setLayout(new BorderLayout()); // Đảm bảo layout được thiết lập lại

        ncLblTenMayChatHeader = new JLabel(tenMay, SwingConstants.CENTER);
        // Lấy tên tài khoản hiện tại từ map đã được cập nhật bởi server
        String currentClientAccountName = ncMapLoggedInClientAccounts.containsKey(idComputer) && idClientAccount != 0 ? ncChatServer.LayTenTaiKhoanTuID(idClientAccount) : null;

        if (currentClientAccountName != null && !currentClientAccountName.isEmpty() && idClientAccount != null && idClientAccount != 0) {
            ncLblTenMayChatHeader.setText(tenMay + " (" + currentClientAccountName + ")");
        } else {
            ncLblTenMayChatHeader.setText(tenMay); // Chỉ hiển thị tên máy nếu không có tài khoản
        }
        ncLblTenMayChatHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        ncLblTenMayChatHeader.setForeground(Color.CYAN);
        ncLblTenMayChatHeader.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        ncPnlChatContent.add(ncLblTenMayChatHeader, BorderLayout.NORTH);

        ncTxtChatDisplay = new JTextPane();
        ncTxtChatDisplay.setEditable(false);
        ncTxtChatDisplay.setBackground(new Color(30, 30, 47));
        ncTxtChatDisplay.setForeground(Color.WHITE);
        ncTxtChatDisplay.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollChat = new JScrollPane(ncTxtChatDisplay);
        scrollChat.setBorder(null); // Loại bỏ border mặc định của JScrollPane
        ncPnlChatContent.add(scrollChat, BorderLayout.CENTER);

        JPanel pnlSend = new JPanel(new BorderLayout());
        pnlSend.setBackground(new Color(30, 30, 47));
        pnlSend.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ncTxtSendMessage = new JTextField();
        ncTxtSendMessage.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ncTxtSendMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    GuiTinNhanTuAdmin(idComputer, tenMay);
                }
            }
        });

        JButton btnSend = new JButton("GỬI");
        btnSend.setBackground(Color.CYAN);
        btnSend.setForeground(Color.BLACK);
        btnSend.setFocusPainted(false);
        btnSend.addActionListener(e -> GuiTinNhanTuAdmin(idComputer, tenMay));

        pnlSend.add(ncTxtSendMessage, BorderLayout.CENTER);
        pnlSend.add(btnSend, BorderLayout.EAST);
        ncPnlChatContent.add(pnlSend, BorderLayout.SOUTH);

        // Tải lịch sử chat khi mở khung chat
        TaiLichSuChatCoHeader(idComputer, idClientAccount);

        pnlMain.revalidate();
        pnlMain.repaint();

        SwingUtilities.invokeLater(ncTxtSendMessage::requestFocusInWindow);
    }

    // Gửi tin nhắn từ Admin đến Client
    private static void GuiTinNhanTuAdmin(int idComputer, String tenMay) {
        String msgContent = ncTxtSendMessage.getText().trim();
        if (msgContent.isEmpty()) {
            return;
        }

        // Kiểm tra xem server chat có đang chạy không
        if (ncChatServer == null || !ncChatServer.isServerRunning()) {
            HienThiThongBaoLoi("Hệ thống chat chưa khởi động hoặc đã dừng.");
            return;
        }

        // Lấy thông tin hiện tại của client từ map cục bộ (đã được cập nhật từ server)
        Integer targetClientAccountId = ncMapLoggedInClientAccounts.getOrDefault(idComputer, 0);

        if (!ncChatServer.IsComputerOnline(idComputer)) {
            HienThiThongBaoLoi(tenMay + " hiện không online. Không thể gửi tin.");
            return;
        }
        if (targetClientAccountId == null || targetClientAccountId == 0) {
            HienThiThongBaoLoi(tenMay + " online nhưng chưa có tài khoản đăng nhập. Không thể gửi tin.");
            return;
        }

        Date now = new Date();
        // Tạo tin nhắn với idSender là ncAdminAccountId và idReceiver là targetClientAccountId
        NC_Message message = new NC_Message(NC_Message.NC_MessageType.CHAT,
                GetAdminAccountId(), targetClientAccountId,
                GetAdminAccountName(), msgContent, now);

        // Gửi tin nhắn qua server
        ncChatServer.guiTinNhanDenClient(idComputer, message);

        // Hiển thị tin nhắn của Admin lên khung chat của Admin
        CapNhatGiaoDienTinNhan(idComputer, GetAdminAccountName(), msgContent, now, new Color(51, 255, 255)); // Màu xanh cyan

        ncTxtSendMessage.setText(""); // Xóa nội dung đã gửi
    }

    // Xử lý tin nhắn đến từ server (do client gửi hoặc server gửi)
    private static void processIncomingMessage(NC_Message message) {
        if (message == null) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            switch (message.getType()) {
                case CHAT:
                case CHAT_HISTORY_ITEM: // Xử lý các mục lịch sử chat tương tự tin nhắn chat
                    // Tin nhắn này đến từ client, người nhận là Admin
                    if (message.getReceiverId() == GetAdminAccountId()) {
                        int senderComputerId = ncChatServer.GetNCComputerIdFromClientAccountId(message.getSenderId());
                        String clientAccountName = ncChatServer.LayTenTaiKhoanTuID(message.getSenderId());

                        if (ncCurrentSelectedComputerId == senderComputerId) {
                            // Nếu tin nhắn từ client đang được chọn, cập nhật hiển thị chat
                            CapNhatGiaoDienTinNhan(senderComputerId, clientAccountName, message.getContent(), message.getSentAt(), new Color(153, 255, 0)); // Màu xanh lá
                        } else {
                            // Nếu tin nhắn từ client KHÔNG đang được chọn, chỉ cập nhật trạng thái "Tin nhắn mới"
                            String senderComputerName = ncMapComputerNames.get(senderComputerId); // Lấy tên máy từ map
                            CapNhatTrangThaiTinNhanMoi(senderComputerId, senderComputerName, clientAccountName);
                        }
                        // Dù là tin nhắn chat hay tin nhắn lịch sử, đều cập nhật thời gian cuối cùng của máy
                        CapNhatThoiGianTinNhanMoiNhat(senderComputerId, message.getSentAt());
                    }
                    break;
                case CLIENT_CONNECT:
                case CLIENT_LOGIN:
                case CLIENT_LOGOUT:
                case CLIENT_DISCONNECT:
                    // Các loại tin nhắn trạng thái này đã được xử lý bởi ClientStatusUpdater callback
                    // Không cần xử lý lại ở đây, trừ khi bạn muốn in log riêng
                    System.out.println("CN_ChatAdmin: Nhận sự kiện trạng thái từ server: " + message.getType() + " - Máy: " + message.getComputerName() + " (ID: " + message.getComputerId() + ")");
                    break;
                default:
                    System.out.println("CN_ChatAdmin: Nhận loại tin nhắn không xác định từ server: " + message.getType());
                    break;
            }
        });
    }

    // Cập nhật giao diện khung chat với tin nhắn mới.
    public static void CapNhatGiaoDienTinNhan(int idComputer, String senderDisplayName, String content, Date sentAt, Color color) {
        // Chỉ cập nhật khung chat nếu nó đang hiển thị cho máy tương ứng
        if (ncTxtChatDisplay != null && ncCurrentSelectedComputerId == idComputer) {
            SwingUtilities.invokeLater(() -> {
                StyledDocument doc = ncTxtChatDisplay.getStyledDocument();
                SimpleAttributeSet sas = new SimpleAttributeSet();
                StyleConstants.setForeground(sas, color);

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String timeString = sdf.format(sentAt);

                String formattedMessage = senderDisplayName + " [" + timeString + "]: " + content + "\n";

                try {
                    doc.insertString(doc.getLength(), formattedMessage, sas);
                    // Tự động cuộn xuống cuối
                    ncTxtChatDisplay.setCaretPosition(doc.getLength());
                } catch (Exception e) {
                    System.err.println("CN_ChatAdmin: Lỗi khi thêm tin nhắn vào JTextPane: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }
        // Cập nhật thời gian tin nhắn mới nhất trong danh sách máy (luôn cập nhật)
        CapNhatThoiGianTinNhanMoiNhat(idComputer, sentAt);
    }

    // Cập nhật trạng thái Online/Offline và tên tài khoản trong danh sách máy. Gọi từ NC_ChatServer thông qua ClientStatusUpdater.
    public static void CapNhatTrangThaiMay(int idComputer, String computerName, Integer accountId, String accountName, boolean isOnline) {
        SwingUtilities.invokeLater(() -> {
            JPanel pnlMay = ncMapPnlMay.get(idComputer);
            JLabel lblTrangThai = ncMapLblTrangThai.get(idComputer);
            JLabel lblTenMay = null;

            if (pnlMay != null) {
                if (pnlMay.getComponentCount() > 0 && pnlMay.getComponent(0) instanceof JPanel) {
                    JPanel pnlTop = (JPanel) pnlMay.getComponent(0);
                    if (pnlTop.getComponentCount() > 0 && pnlTop.getComponent(0) instanceof JLabel) {
                        lblTenMay = (JLabel) pnlTop.getComponent(0);
                    }
                }
            }

            if (lblTrangThai != null) {
                if (isOnline) {
                    // Cập nhật map account đang đăng nhập
                    if (accountId != null && accountId != 0) {
                        ncMapLoggedInClientAccounts.put(idComputer, accountId);
                    } else {
                        ncMapLoggedInClientAccounts.remove(idComputer);
                    }

                    // Nếu Admin đang chat máy này thì reset về "Online"
                    if (ncCurrentSelectedComputerId == idComputer) {
                        lblTrangThai.setText("Online");
                        lblTrangThai.setForeground(Color.GREEN);
                    } else {
                        // Nếu chưa phải "Tin nhắn mới" thì set Online
                        if (!"Tin nhắn mới".equals(lblTrangThai.getText())) {
                            lblTrangThai.setText("Online");
                            lblTrangThai.setForeground(Color.GREEN);
                        }
                    }

                } else {
                    lblTrangThai.setText("Offline");
                    lblTrangThai.setForeground(Color.RED);
                    ncMapLoggedInClientAccounts.remove(idComputer);
                }
            }

            // Tên máy kèm tên tài khoản (nếu có)
            if (lblTenMay != null) {
                lblTenMay.setText(
                        computerName
                        + (accountName != null && !accountName.isEmpty() && accountId != null && accountId != 0
                        ? " (" + accountName + ")" : "")
                );
            }

            // Nếu đang chat máy này thì update header luôn
            if (ncCurrentSelectedComputerId == idComputer && ncLblTenMayChatHeader != null) {
                ncLblTenMayChatHeader.setText(
                        computerName
                        + (accountName != null && !accountName.isEmpty() && accountId != null && accountId != 0
                        ? " (" + accountName + ")" : "")
                );
            }
        });
    }

    // Cập nhật trạng thái "Tin nhắn mới" nếu Admin chưa mở chat. Nếu Admin đang mở thì auto reset "Online".
    public static void CapNhatTrangThaiTinNhanMoi(int idComputer, String computerName, String accountName) {
        SwingUtilities.invokeLater(() -> {
            JLabel lblTrangThai = ncMapLblTrangThai.get(idComputer);
            if (lblTrangThai != null) {
                if (ncCurrentSelectedComputerId == idComputer) {
                    // Đang mở sẵn -> reset Online
                    lblTrangThai.setText("Online");
                    lblTrangThai.setForeground(Color.GREEN);
                } else {
                    lblTrangThai.setText("Tin nhắn mới");
                    lblTrangThai.setForeground(new Color(255, 165, 0)); // Màu cam
                }
            }
        });
    }

    // Hiển thị thông báo lỗi (JOptionPane).
    public static void HienThiThongBaoLoi(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, message, "Lỗi Chat", JOptionPane.ERROR_MESSAGE);
        });
    }

    // Lấy lịch sử chat từ database giữa Admin và một Client Account cụ thể.
    private static List<NC_Message> LayLichSuChatTuDB(int adminId, int clientAccountId) {
        List<NC_Message> chatHistory = new java.util.ArrayList<>();
        String sql = "SELECT SenderID, ReceiverID, Content, SentAt "
                + "FROM Message "
                + "WHERE (SenderID = ? AND ReceiverID = ?) OR (SenderID = ? AND ReceiverID = ?) "
                + "ORDER BY SentAt ASC";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, adminId);
            ps.setInt(2, clientAccountId);
            ps.setInt(3, clientAccountId);
            ps.setInt(4, adminId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int senderId = rs.getInt("SenderID");
                    int receiverId = rs.getInt("ReceiverID");
                    String content = rs.getNString("Content");
                    Date sentAt = new Date(rs.getTimestamp("SentAt").getTime());

                    NC_Message msg = new NC_Message(NC_Message.NC_MessageType.CHAT_HISTORY_ITEM, senderId, receiverId, "", content, sentAt);
                    chatHistory.add(msg);
                }
            }
        } catch (SQLException e) {
            System.err.println("CN_ChatAdmin: Lỗi SQL khi tải lịch sử chat từ DB: " + e.getMessage());
            e.printStackTrace();
        }
        return chatHistory;
    }

    // Tải lịch sử chat từ DB, Có Header ngày (sử dụng ComputerUsage).
    private static void TaiLichSuChatCoHeader(int idComputer, Integer idClientAccount) {
        if (ncTxtChatDisplay == null) {
            return;
        }

        ncTxtChatDisplay.setText(""); // Clear cũ

        Integer actualClientAccountId = ncMapLoggedInClientAccounts.getOrDefault(idComputer, 0);
        if (actualClientAccountId == null || actualClientAccountId == 0) {
            AppendMessageToChatDisplay("Hệ thống", "Máy này hiện không có tài khoản đăng nhập để xem lịch sử chat.", new Color(150, 150, 150), new Date());
            return;
        }

        List<NC_Message> history = LayLichSuChatTuDB(GetAdminAccountId(), actualClientAccountId);

        if (history.isEmpty()) {
            AppendMessageToChatDisplay("Hệ thống", "Chưa có tin nhắn nào.", new Color(150, 150, 150), new Date());
        } else {
            LocalDate currentDate = null;

            for (NC_Message msg : history) {
                LocalDate msgDate = msg.getSentAt().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

                if (!msgDate.equals(currentDate)) {
                    currentDate = msgDate;

                    String header = String.format(
                            "|------ %1$tH:%1$tM - %1$td/%1$tm/%1$tY ------|",
                            msg.getSentAt()
                    );
                    AppendMessageToChatDisplay("HỆ THỐNG", header, Color.LIGHT_GRAY, msg.getSentAt());
                }

                Color textColor = (msg.getSenderId() == GetAdminAccountId())
                        ? new Color(51, 255, 255)
                        : new Color(153, 255, 0);

                String senderDisplayName = (msg.getSenderId() == GetAdminAccountId())
                        ? "Admin"
                        : ncChatServer.LayTenTaiKhoanTuID(msg.getSenderId());

                if (senderDisplayName == null || senderDisplayName.isEmpty()) {
                    senderDisplayName = "Client (" + msg.getSenderId() + ")";
                }

                AppendMessageToChatDisplay(senderDisplayName, msg.getContent(), textColor, msg.getSentAt());
            }
        }
    }

    // Lấy danh sách phiên từ ComputerUsage
    private static List<SessionInfo> LayPhienTuComputerUsage(int accountId, int computerId) {
        List<SessionInfo> sessions = new java.util.ArrayList<>();
        String sql = "SELECT StartTime, EndTime FROM ComputerUsage WHERE IDAccount = ? AND IDComputer = ? ORDER BY StartTime ASC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ps.setInt(2, computerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date start = new Date(rs.getTimestamp("StartTime").getTime());
                    java.sql.Timestamp endTs = rs.getTimestamp("EndTime");
                    Date end = (endTs != null) ? new Date(endTs.getTime()) : null;
                    sessions.add(new SessionInfo(start, end));
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy phiên từ ComputerUsage: " + e.getMessage());
        }
        return sessions;
    }

    // Lấy danh sách phiên LogAccess của user trên máy.
    private static List<SessionInfo> LayPhienTuDB(int accountId, int computerId) {
        List<SessionInfo> sessions = new java.util.ArrayList<>();
        String sql = "SELECT StartTime, EndTime FROM LogAccess WHERE IDAccount = ? AND IDComputer = ? ORDER BY StartTime ASC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ps.setInt(2, computerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date start = new Date(rs.getTimestamp("StartTime").getTime());
                    Timestamp endTs = rs.getTimestamp("EndTime");
                    Date end = endTs != null ? new Date(endTs.getTime()) : null;
                    sessions.add(new SessionInfo(start, end));
                }
            }
        } catch (SQLException e) {
            System.err.println("CN_ChatAdmin: Lỗi khi tải phiên LogAccess: " + e.getMessage());
        }
        return sessions;
    }

    // Lớp SessionInfo để lưu StartTime & EndTime phiên.
    private static class SessionInfo {

        Date start;
        Date end;

        SessionInfo(Date start, Date end) {
            this.start = start;
            this.end = end;
        }
    }

    // Helper method để thêm tin nhắn vào JTextPane với định dạng.
    private static void AppendMessageToChatDisplay(String senderDisplayName, String content, Color color, Date sentAt) {
        StyledDocument doc = ncTxtChatDisplay.getStyledDocument();
        SimpleAttributeSet sas = new SimpleAttributeSet();
        StyleConstants.setForeground(sas, color);

        if (senderDisplayName.equalsIgnoreCase("HỆ THỐNG") && content.trim().startsWith("|------")) {
            // Header: font size, căn giữa
            StyleConstants.setFontSize(sas, 10);
            StyleConstants.setAlignment(sas, StyleConstants.ALIGN_CENTER);

            try {
                doc.insertString(doc.getLength(), content + "\n", sas);
                // Set alignment
                doc.setParagraphAttributes(doc.getLength() - content.length(), content.length(), sas, false);
                ncTxtChatDisplay.setCaretPosition(doc.getLength());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            // Tin nhắn thường
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String timeString = sdf.format(sentAt);

            String formattedMessage = senderDisplayName + " [" + timeString + "]: " + content + "\n";

            try {
                doc.insertString(doc.getLength(), formattedMessage, sas);
                ncTxtChatDisplay.setCaretPosition(doc.getLength()); // Cuộn xuống cuối
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Hàm lấy tin nhắn gần nhất để hiển thị thời gian trong danh sách máy. Lấy
     * tin nhắn mới nhất giữa Admin và BẤT KỲ tài khoản nào đã TỪNG đăng nhập
     * trên máy đó trong bảng Message.
     */
    private static NC_Message LayTinNhanGanNhat(int adminId, int computerId) {
        NC_Message lastMsg = null;
        String sql = "SELECT m.SenderID, m.ReceiverID, m.Content, m.SentAt "
                + "FROM Message m "
                + "WHERE (m.SenderID = ? AND m.ReceiverID IN (SELECT DISTINCT IDAccount FROM LogAccess WHERE IDComputer = ?)) "
                + "OR (m.ReceiverID = ? AND m.SenderID IN (SELECT DISTINCT IDAccount FROM LogAccess WHERE IDComputer = ?)) "
                + "ORDER BY m.SentAt DESC "
                + "OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, adminId);
            ps.setInt(2, computerId);
            ps.setInt(3, adminId);
            ps.setInt(4, computerId);

            try (ResultSet rs = ps.executeQuery()) { // Đảm bảo ResultSet được đóng
                if (rs.next()) {
                    int senderId = rs.getInt("SenderID");
                    int receiverId = rs.getInt("ReceiverID");
                    String content = rs.getNString("Content");
                    Date sentAt = new Date(rs.getTimestamp("SentAt").getTime());
                    lastMsg = new NC_Message(NC_Message.NC_MessageType.CHAT, senderId, receiverId, "", content, sentAt); // Type là CHAT, senderName sẽ được lấy sau
                }
            }
        } catch (SQLException e) {
            System.err.println("CN_ChatAdmin: Lỗi SQL khi lấy tin nhắn gần nhất từ DB cho máy " + computerId + ": " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("CN_ChatAdmin: Lỗi khi lấy tin nhắn gần nhất: " + e.getMessage());
            e.printStackTrace();
        }
        return lastMsg;
    }

    // Cập nhật thời gian tin nhắn mới nhất trong danh sách máy.
    public static void CapNhatThoiGianTinNhanMoiNhat(int idComputer, Date latestTime) {
        SwingUtilities.invokeLater(() -> {
            JLabel lblTime = ncMapLblTime.get(idComputer);
            if (lblTime != null) {
                lblTime.setText(new SimpleDateFormat("HH:mm").format(latestTime));
            }
        });
    }

    // Hàm hỗ trợ lấy tên máy từ ID (được gọi từ Server hoặc các nơi khác).
    public String GetTenMayTuID(int idComputer) {
        return ncMapComputerNames.get(idComputer);
    }
}
