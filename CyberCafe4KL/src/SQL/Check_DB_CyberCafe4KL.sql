-- TAB1_ĐẶT MÁY: Kiểm tra máy, trạng thái, order
SELECT 
    IDComputer,
    NameComputer,
    ComputerStatus,
    CASE 
        WHEN ComputerStatus = 0 THEN N'Đang sử dụng'
        WHEN ComputerStatus = 1 THEN N'Đang trống'
        WHEN ComputerStatus = 2 THEN N'Bảo trì'
    END AS TrangThai
FROM Computer
ORDER BY IDComputer;

SELECT * FROM Account

-- Cấu hình máy
SELECT NameComputer, CPU, RAM, GPU, Monitor, PricePerMinute FROM Computer;


-- Máy có order
SELECT * FROM OrderFood WHERE IDAccount = 200; -- client test máy





-- TAB2_QUẢN LÝ DỊCH VỤ: Xem danh sách món ăn, lọc tồn kho
SELECT * FROM FoodDrink;

-- Trạng thái
SELECT 
    IDFood,
    NameFood,
    ImageFood,
    Price,
    Category,
    Available,
    CASE 
        WHEN Available = 1 THEN N'Còn bán'
        ELSE N'Đã ẩn'
    END AS TrangThai
FROM FoodDrink
ORDER BY Available DESC, NameFood;





-- TAB3_TIN NHẮN: Lấy tin nhắn gửi/nhận theo account
SELECT * FROM Message ORDER BY SentAt DESC;

-- Lấy tất cả tin nhắn giữa 2 tài khoản
SELECT * FROM Message 
WHERE (SenderID = 1 AND ReceiverID = 2) OR (SenderID = 2 AND ReceiverID = 1)
ORDER BY SentAt;






-- TAB4_QUẢN LÝ MÁY: Chi tiết cấu hình, tình trạng từng máy
SELECT * FROM Computer ORDER BY IDComputer;

-- Thống kê số lượng máy theo trạng thái
SELECT 
    ComputerStatus,
    COUNT(*) AS SoLuong,
    CASE 
        WHEN ComputerStatus = 0 THEN N'Đang sử dụng'
        WHEN ComputerStatus = 1 THEN N'Đang trống'
        WHEN ComputerStatus = 2 THEN N'Bảo trì'
    END AS TrangThai
FROM Computer
GROUP BY ComputerStatus;





-- TAB7_QUẢN LÝ NHÂN VIÊN: Tài khoản nhân viên, trạng thái, quyền
SELECT IDAccount, NameAccount, RoleAccount, Gender, AccountStatus, OnlineStatus
FROM Account
WHERE RoleAccount IN ('ADMIN', 'BOSS')
ORDER BY RoleAccount;

-- Lọc nhân viên
SELECT 
    IDAccount,
    NameAccount,
    RoleAccount,
    Gender,
    PhoneNumber,
    Email,
    AccountStatus,
    CASE 
        WHEN AccountStatus = 1 THEN N'Đang hoạt động'
        ELSE N'Đã ngừng hoạt động'
    END AS TrangThai
FROM Account
WHERE RoleAccount IN ('ADMIN', 'BOSS')
ORDER BY IDAccount ASC;

SELECT * FROM Message



SELECT * FROM Assets_Anh
