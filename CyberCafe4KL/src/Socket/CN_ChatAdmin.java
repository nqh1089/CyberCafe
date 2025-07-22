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
    private static int ncAdminAccountId = 2; // THAY THẾ BẰNG ID ADMIN THỰC TẾ (ví dụ: từ màn hình đăng nhập)
    private static String ncAdminAccountName = "ADMIN"; // THAY THẾ BẰNG TÊN ADMIN THỰC TẾ

    /**
     * Set thông tin tài khoản Admin sau khi đăng nhập thành công.
     * @param id ID Account của Admin.
     * @param name Tên tài khoản của Admin.
     */
    public static void SetNCAdminInfo(int id, String name) {
        ncAdminAccountId = id;
        ncAdminAccountName = name;
        if (ncChatServer != null) {
            ncChatServer.setAdminAccountId(id); // Cập nhật ID Admin cho server nếu đã khởi tạo
        }
    }

    /**
     * Lấy ID Account của Admin.
     * @return ID Account của Admin.
     */
    public static int GetNCAdminAccountId() {
        return ncAdminAccountId;
    }

    /**
     * Lấy tên tài khoản Admin. Nếu chưa được set, sẽ cố gắng load từ DB.
     * @return Tên tài khoản Admin.
     */
    public static String GetNCAdminAccountName() {
        // Chỉ tải từ DB nếu tên Admin chưa được set hoặc vẫn là giá trị mặc định
        if (ncAdminAccountName == null || ncAdminAccountName.equals("ADMIN") || ncAdminAccountName.isEmpty()) {
            String name = null;
            String sql = "SELECT NameAccount FROM Account WHERE IDAccount = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
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

    /**
     * Hiển thị giao diện chat cho Admin.
     * @param pnlMain JPanel chính để thêm các thành phần chat vào.
     */
    public static void HienThiNCGiaoDien(JPanel pnlMain) {
        pnlMain.removeAll();
        pnlMain.setLayout(new BorderLayout(4, 4));
        pnlMain.setPreferredSize(new Dimension(650, 450));

        // Khởi tạo server chat nếu chưa có
        if (ncChatServer == null) {
            ncChatServer = new NC_ChatServer(); // Tạo instance của server
            ncChatServer.setAdminAccountId(GetNCAdminAccountId()); // Set ID Admin cho server

            // Đặt callbacks để server có thể thông báo về UI
            ncChatServer.setMessageProcessor(CN_ChatAdmin::processIncomingMessage);
            ncChatServer.setStatusListener(status -> System.out.println("Server Status: " + status)); // In ra console
            ncChatServer.setClientStatusUpdater((computerId, computerName, accountId, accountName, isOnline) -> {
                CapNCNhatTrangThaiMay(computerId, computerName, accountId, accountName, isOnline);
                if (isOnline && accountId != null && accountId != 0) {
                    ncMapLoggedInClientAccounts.put(computerId, accountId); // Cập nhật ID tài khoản đang đăng nhập
                } else {
                    ncMapLoggedInClientAccounts.remove(computerId); // Xóa khỏi map khi offline hoặc không có tài khoản
                }
            });
            new Thread(() -> ncChatServer.startServer()).start(); // Chạy server trong một luồng riêng
        }

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

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT IDComputer, NameComputer, ComputerStatus FROM Computer ORDER BY IDComputer ASC");
             ResultSet rs = ps.executeQuery()) { // Đảm bảo ResultSet được đóng

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
                NC_Message lastMsg = LayNCTinNhanGanNhat(GetNCAdminAccountId(), idMay);
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
                                String oldAccountName = (ncChatServer != null && oldAccountId != 0) ? ncChatServer.LayNCTenTaiKhoanTuID(oldAccountId) : null;
                                CapNCNhatTrangThaiMay(oldIdComputer, ncMapComputerNames.get(oldIdComputer), oldAccountId, oldAccountName, ncChatServer.IsNCComputerOnline(oldIdComputer));
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

    /**
     * Phương thức tĩnh để đóng Server Chat một cách an toàn.
     * Được gọi từ bên ngoài (ví dụ: AD_Chat) khi ứng dụng Admin đóng.
     */
    public static void DongServerChat() {
        if (ncChatServer != null) {
            ncChatServer.stopServer(); // Sử dụng phương thức stopServer của NC_ChatServer
            ncChatServer = null; // Đặt về null để cho phép khởi tạo lại nếu cần
        }
    }

    /**
     * Hiển thị khung chat cho một máy cụ thể.
     * @param pnlMain JPanel chính.
     * @param idComputer ID của máy tính.
     * @param tenMay Tên máy tính.
     * @param idClientAccount ID Account của Client đang đăng nhập (hoặc 0 nếu không có).
     */
    private static void HienThiNCKhungChat(JPanel pnlMain, int idComputer, String tenMay, Integer idClientAccount) {
        ncPnlChatContent.removeAll();
        ncPnlChatContent.setLayout(new BorderLayout()); // Đảm bảo layout được thiết lập lại

        ncLblTenMayChatHeader = new JLabel(tenMay, SwingConstants.CENTER);
        // Lấy tên tài khoản hiện tại từ map đã được cập nhật bởi server
        String currentClientAccountName = ncMapLoggedInClientAccounts.containsKey(idComputer) && idClientAccount != 0 ? ncChatServer.LayNCTenTaiKhoanTuID(idClientAccount) : null;

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
                    GuiNCTinNhanTuAdmin(idComputer, tenMay);
                }
            }
        });

        JButton btnSend = new JButton("GỬI");
        btnSend.setBackground(Color.CYAN);
        btnSend.setForeground(Color.BLACK);
        btnSend.setFocusPainted(false);
        btnSend.addActionListener(e -> GuiNCTinNhanTuAdmin(idComputer, tenMay));

        pnlSend.add(ncTxtSendMessage, BorderLayout.CENTER);
        pnlSend.add(btnSend, BorderLayout.EAST);
        ncPnlChatContent.add(pnlSend, BorderLayout.SOUTH);

        // Tải lịch sử chat khi mở khung chat
        TaiNCLichSuChat(idComputer, idClientAccount);

        pnlMain.revalidate();
        pnlMain.repaint();

        SwingUtilities.invokeLater(ncTxtSendMessage::requestFocusInWindow);
    }

    /**
     * Gửi tin nhắn từ Admin đến Client.
     * @param idComputer ID của máy Client đích.
     * @param tenMay Tên máy của Client đích.
     */
    private static void GuiNCTinNhanTuAdmin(int idComputer, String tenMay) {
        String msgContent = ncTxtSendMessage.getText().trim();
        if (msgContent.isEmpty()) {
            return;
        }

        // Kiểm tra xem server chat có đang chạy không
        if (ncChatServer == null || !ncChatServer.isServerRunning()) {
            HienThiNCThongBaoLoi("Hệ thống chat chưa khởi động hoặc đã dừng.");
            return;
        }

        // Lấy thông tin hiện tại của client từ map cục bộ (đã được cập nhật từ server)
        Integer targetClientAccountId = ncMapLoggedInClientAccounts.getOrDefault(idComputer, 0);

        if (!ncChatServer.IsNCComputerOnline(idComputer)) {
            HienThiNCThongBaoLoi("Máy " + tenMay + " hiện không online. Không thể gửi tin.");
            return;
        }
        if (targetClientAccountId == null || targetClientAccountId == 0) {
            HienThiNCThongBaoLoi("Máy " + tenMay + " online nhưng chưa có tài khoản đăng nhập. Không thể gửi tin.");
            return;
        }

        Date now = new Date();
        // Tạo tin nhắn với idSender là ncAdminAccountId và idReceiver là targetClientAccountId
        NC_Message message = new NC_Message(NC_Message.NC_MessageType.CHAT,
                                            GetNCAdminAccountId(), targetClientAccountId,
                                            GetNCAdminAccountName(), msgContent, now);

        // Gửi tin nhắn qua server
        ncChatServer.guiTinNhanDenClient(idComputer, message);

        // Hiển thị tin nhắn của Admin lên khung chat của Admin
        CapNCNhatGiaoDienTinNhan(idComputer, GetNCAdminAccountName(), msgContent, now, new Color(51, 255, 255)); // Màu xanh cyan

        ncTxtSendMessage.setText(""); // Xóa nội dung đã gửi
    }

    /**
     * Xử lý tin nhắn đến từ server (do client gửi hoặc server gửi).
     * Phương thức này được gọi bởi NC_ChatServer thông qua callback.
     * @param message Tin nhắn đến.
     */
    private static void processIncomingMessage(NC_Message message) {
        if (message == null) return;

        SwingUtilities.invokeLater(() -> {
            switch (message.getType()) {
                case CHAT:
                case CHAT_HISTORY_ITEM: // Xử lý các mục lịch sử chat tương tự tin nhắn chat
                    // Tin nhắn này đến từ client, người nhận là Admin
                    if (message.getReceiverId() == GetNCAdminAccountId()) {
                        int senderComputerId = ncChatServer.GetNCComputerIdFromClientAccountId(message.getSenderId());
                        String clientAccountName = ncChatServer.LayNCTenTaiKhoanTuID(message.getSenderId());

                        if (ncCurrentSelectedComputerId == senderComputerId) {
                            // Nếu tin nhắn từ client đang được chọn, cập nhật hiển thị chat
                            CapNCNhatGiaoDienTinNhan(senderComputerId, clientAccountName, message.getContent(), message.getSentAt(), new Color(153, 255, 0)); // Màu xanh lá
                        } else {
                            // Nếu tin nhắn từ client KHÔNG đang được chọn, chỉ cập nhật trạng thái "Tin nhắn mới"
                            String senderComputerName = ncMapComputerNames.get(senderComputerId); // Lấy tên máy từ map
                            CapNCNhatTrangThaiTinNhanMoi(senderComputerId, senderComputerName, clientAccountName);
                        }
                        // Dù là tin nhắn chat hay tin nhắn lịch sử, đều cập nhật thời gian cuối cùng của máy
                        CapNCNhatThoiGianTinNhanMoiNhat(senderComputerId, message.getSentAt());
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

    /**
     * Cập nhật giao diện khung chat với tin nhắn mới.
     * @param idComputer ID của máy liên quan (để kiểm tra có phải máy đang chọn không).
     * @param senderDisplayName Tên hiển thị của người gửi (Admin/Tên tài khoản Client).
     * @param content Nội dung tin nhắn.
     * @param sentAt Thời điểm gửi.
     * @param color Màu của tin nhắn.
     */
    public static void CapNCNhatGiaoDienTinNhan(int idComputer, String senderDisplayName, String content, Date sentAt, Color color) {
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
        CapNCNhatThoiGianTinNhanMoiNhat(idComputer, sentAt);
    }

    /**
     * Cập nhật trạng thái Online/Offline và tên tài khoản trong danh sách máy.
     * Phương thức này được gọi từ NC_ChatServer thông qua ClientStatusUpdater callback.
     * @param idComputer ID của máy tính.
     * @param computerName Tên máy tính.
     * @param accountId ID của tài khoản đang đăng nhập (0 nếu không có).
     * @param accountName Tên tài khoản đang đăng nhập (null nếu không có).
     * @param isOnline Trạng thái online chat.
     */
    public static void CapNCNhatTrangThaiMay(int idComputer, String computerName, Integer accountId, String accountName, boolean isOnline) {
        SwingUtilities.invokeLater(() -> {
            JPanel pnlMay = ncMapPnlMay.get(idComputer);
            JLabel lblTrangThai = ncMapLblTrangThai.get(idComputer);
            JLabel lblTenMay = null;

            if (pnlMay != null) {
                // Lấy JLabel hiển thị tên máy (lblTenMay)
                if (pnlMay.getComponentCount() > 0 && pnlMay.getComponent(0) instanceof JPanel) {
                    JPanel pnlTop = (JPanel) pnlMay.getComponent(0);
                    if (pnlTop.getComponentCount() > 0 && pnlTop.getComponent(0) instanceof JLabel) {
                           lblTenMay = (JLabel) pnlTop.getComponent(0);
                    }
                }
            }

            if (lblTrangThai != null) {
                if (isOnline) {
                    // Cập nhật trạng thái trong map quản lý tài khoản đang đăng nhập
                    if (accountId != null && accountId != 0) {
                        ncMapLoggedInClientAccounts.put(idComputer, accountId);
                    } else {
                        ncMapLoggedInClientAccounts.remove(idComputer); // Máy online nhưng không có tài khoản
                    }

                    // Kiểm tra xem lblTrangThai có đang hiển thị "Tin nhắn mới" không, nếu có thì giữ nguyên màu
                    if (!lblTrangThai.getForeground().equals(new Color(255, 165, 0))) { // Màu cam
                        lblTrangThai.setText(accountName != null && !accountName.isEmpty() && accountId != 0 ? "(" + accountName + ")" : "Online");
                        lblTrangThai.setForeground(Color.GREEN);
                    }
                } else {
                    lblTrangThai.setText("Offline");
                    lblTrangThai.setForeground(Color.RED);
                    ncMapLoggedInClientAccounts.remove(idComputer); // Xóa khỏi map khi offline
                }
            }
            // Cập nhật tên máy kèm tài khoản trên lblTenMay
            if (lblTenMay != null) {
                lblTenMay.setText(computerName + (accountName != null && !accountName.isEmpty() && accountId != 0 ? " (" + accountName + ")" : ""));
            }

            // Cập nhật tiêu đề khung chat nếu máy đang được chọn
            if (ncCurrentSelectedComputerId == idComputer && ncLblTenMayChatHeader != null) {
                ncLblTenMayChatHeader.setText(computerName + (accountName != null && !accountName.isEmpty() && accountId != 0 ? " (" + accountName + ")" : ""));
            }
        });
    }

    /**
     * Cập nhật trạng thái "Tin nhắn mới" trong danh sách máy nếu Admin không đang chat với máy đó.
     * @param idComputer ID của máy tính.
     * @param computerName Tên máy tính (có thể null nếu không cần hiển thị tên).
     * @param accountName Tên tài khoản (nếu có).
     */
    public static void CapNCNhatTrangThaiTinNhanMoi(int idComputer, String computerName, String accountName) {
        SwingUtilities.invokeLater(() -> {
            // Chỉ cập nhật nếu Admin không đang chat với máy đó
            if (ncCurrentSelectedComputerId != idComputer) {
                JLabel lblTrangThai = ncMapLblTrangThai.get(idComputer);
                if (lblTrangThai != null) {
                    lblTrangThai.setText("Tin nhắn mới");
                    lblTrangThai.setForeground(new Color(255, 165, 0)); // Màu cam
                }
                // Có thể làm cho panel nhấp nháy hoặc nổi bật hơn nếu muốn
            }
        });
    }

    /**
     * Hiển thị thông báo lỗi (JOptionPane).
     * @param message Nội dung thông báo.
     */
    public static void HienThiNCThongBaoLoi(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, message, "Lỗi Chat", JOptionPane.ERROR_MESSAGE);
        });
    }

    /**
     * Tải lịch sử chat từ DB và hiển thị lên JTextPane.
     * @param idComputer ID của máy tính.
     * @param idClientAccount ID Account của Client đang đăng nhập (hoặc 0).
     */
    private static void TaiNCLichSuChat(int idComputer, Integer idClientAccount) {
        if (ncTxtChatDisplay == null) return;

        ncTxtChatDisplay.setText(""); // Xóa nội dung cũ

        // Lấy ID Account của Client đang chat từ map cục bộ (cập nhật nhất)
        Integer actualClientAccountId = ncMapLoggedInClientAccounts.getOrDefault(idComputer, 0);

        if (actualClientAccountId == null || actualClientAccountId == 0) {
            // Nếu không có tài khoản đăng nhập hiện tại, không thể tải lịch sử chat cụ thể
            AppendNCMessageToChatDisplay("Hệ thống", "Máy này hiện không có tài khoản đăng nhập để xem lịch sử chat.", new Color(150, 150, 150), new Date());
            return;
        }

        // Tải lịch sử trực tiếp từ DB
        List<NC_Message> history = LayNCLichSuChatTuDB(GetNCAdminAccountId(), actualClientAccountId);

        if (history.isEmpty()) {
            AppendNCMessageToChatDisplay("Hệ thống", "Chưa có tin nhắn nào.", new Color(150, 150, 150), new Date());
        } else {
            for (NC_Message msg : history) {
                Color textColor = (msg.getSenderId() == GetNCAdminAccountId()) ? new Color(51, 255, 255) : new Color(153, 255, 0);
                String senderDisplayName = (msg.getSenderId() == GetNCAdminAccountId()) ? "Admin" : ncChatServer.LayNCTenTaiKhoanTuID(msg.getSenderId());
                if (senderDisplayName == null || senderDisplayName.isEmpty()) {
                    senderDisplayName = "Client (" + msg.getSenderId() + ")"; // Fallback
                }
                AppendNCMessageToChatDisplay(senderDisplayName, msg.getContent(), textColor, msg.getSentAt());
            }
        }
    }

    /**
     * Lấy lịch sử chat từ database giữa Admin và một Client Account cụ thể.
     * @param adminId ID của tài khoản Admin.
     * @param clientAccountId ID của tài khoản Client.
     * @return Danh sách các tin nhắn NC_Message.
     */
    private static List<NC_Message> LayNCLichSuChatTuDB(int adminId, int clientAccountId) {
        List<NC_Message> chatHistory = new java.util.ArrayList<>();
        String sql = "SELECT SenderID, ReceiverID, Content, SentAt " +
                     "FROM Message " +
                     "WHERE (SenderID = ? AND ReceiverID = ?) OR (SenderID = ? AND ReceiverID = ?) " +
                     "ORDER BY SentAt ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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

    /**
     * Helper method để thêm tin nhắn vào JTextPane với định dạng.
     * @param senderDisplayName Tên người gửi hiển thị.
     * @param content Nội dung tin nhắn.
     * @param color Màu text.
     * @param sentAt Thời gian gửi.
     */
    private static void AppendNCMessageToChatDisplay(String senderDisplayName, String content, Color color, Date sentAt) {
        StyledDocument doc = ncTxtChatDisplay.getStyledDocument();
        SimpleAttributeSet sas = new SimpleAttributeSet();
        StyleConstants.setForeground(sas, color);

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

    /**
     * Hàm lấy tin nhắn gần nhất để hiển thị thời gian trong danh sách máy.
     * Lấy tin nhắn mới nhất giữa Admin và BẤT KỲ tài khoản nào đã TỪNG đăng nhập trên máy đó trong bảng Message.
     * Điều này quan trọng để hiển thị lịch sử gần nhất dù tài khoản hiện tại trên máy có thể khác.
     * @param adminId ID Admin.
     * @param computerId ID Máy tính.
     * @return NC_Message gần nhất hoặc null.
     */
    private static NC_Message LayNCTinNhanGanNhat(int adminId, int computerId) {
        NC_Message lastMsg = null;
        String sql = "SELECT m.SenderID, m.ReceiverID, m.Content, m.SentAt " +
                     "FROM Message m " +
                     "WHERE (m.SenderID = ? AND m.ReceiverID IN (SELECT DISTINCT IDAccount FROM LogAccess WHERE IDComputer = ?)) " +
                     "OR (m.ReceiverID = ? AND m.SenderID IN (SELECT DISTINCT IDAccount FROM LogAccess WHERE IDComputer = ?)) " +
                     "ORDER BY m.SentAt DESC " +
                     "OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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
            System.err.println("CN_ChatAdmin: Lỗi tổng quát khi lấy tin nhắn gần nhất: " + e.getMessage());
            e.printStackTrace();
        }
        return lastMsg;
    }


    /**
     * Cập nhật thời gian tin nhắn mới nhất trong danh sách máy.
     * @param idComputer ID của máy tính.
     * @param latestTime Thời gian của tin nhắn mới nhất.
     */
    public static void CapNCNhatThoiGianTinNhanMoiNhat(int idComputer, Date latestTime) {
        SwingUtilities.invokeLater(() -> {
            JLabel lblTime = ncMapLblTime.get(idComputer);
            if (lblTime != null) {
                lblTime.setText(new SimpleDateFormat("HH:mm").format(latestTime));
            }
        });
    }

    /**
     * Hàm hỗ trợ lấy tên máy từ ID (được gọi từ Server hoặc các nơi khác).
     * @param idComputer ID của máy tính.
     * @return Tên máy tính.
     */
    public String GetNCTenMayTuID(int idComputer) {
        return ncMapComputerNames.get(idComputer);
    }
}