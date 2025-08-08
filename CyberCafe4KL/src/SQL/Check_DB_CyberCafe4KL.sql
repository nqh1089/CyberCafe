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
SELECT * FROM Computer

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



SELECT * FROM Account
WHERE (RoleAccount = 'ADMIN' OR RoleAccount = 'BOSS')
  AND OnlineStatus = 1
  AND AccountStatus = 1



SELECT TOP 10 * FROM OrderFood ORDER BY OrderTime DESC

SELECT C.NameComputer, O.OrderTime
FROM OrderFood O
JOIN Account A ON O.IDAccount = A.IDAccount
JOIN LogAccess L ON A.IDAccount = L.IDAccount
JOIN Computer C ON C.IDComputer = L.IDComputer
WHERE O.OrderTime BETWEEN L.ThoiGianBatDau AND ISNULL(L.ThoiGianKetThuc, GETDATE())
ORDER BY O.OrderTime DESC







SELECT * FROM OrderFood ORDER BY OrderTime DESC

-- Hiển thị danh sách các hóa đơn do admin ID = 1 được client gửi lên
SELECT O.IDOrder, A.NameAccount, O.OrderTime
FROM OrderFood O
JOIN Account A ON O.IDAccount = A.IDAccount
WHERE O.IDAccount = 1
ORDER BY O.OrderTime DESC;

-- Check đc
DECLARE @now DATETIME = GETDATE();

SELECT O.IDOrder, A.NameAccount, O.OrderTime
FROM OrderFood O
JOIN Account A ON O.IDAccount = A.IDAccount
WHERE O.IDAccount = 1
  AND O.OrderTime >= DATEADD(MINUTE, -15, @now)
ORDER BY O.OrderTime DESC;




SELECT * FROM OrderFood ORDER BY OrderTime DESC

SELECT SUM(od.TotalPrice)
FROM OrderFood o
JOIN OrderDetail od ON o.IDOrder = od.IDOrder
WHERE o.IDAccount = 1 AND o.OrderTime >= '2025-08-08 03:16:13'




SELECT * FROM Account

SELECT TOP 1 IDLog, IDComputer, ThoiGianBatDau, ThoiGianKetThuc, IDAccount
FROM LogAccess
ORDER BY IDLog DESC

SELECT 
    la.IDLog,
    la.IDComputer,
    la.IDAccount,
    la.ThoiGianBatDau,
    la.ThoiGianKetThuc,
    ISNULL(SUM(od.TotalPrice), 0) AS TongChiPhiDichVu
FROM LogAccess la
LEFT JOIN OrderFood ofd ON 
    ofd.Note = CONCAT(N'MÁY ', la.IDComputer)
    AND ofd.OrderTime BETWEEN la.ThoiGianBatDau AND la.ThoiGianKetThuc
LEFT JOIN OrderDetail od ON od.IDOrder = ofd.IDOrder
GROUP BY 
    la.IDLog,
    la.IDComputer,
    la.IDAccount,
    la.ThoiGianBatDau,
    la.ThoiGianKetThuc
ORDER BY la.IDLog DESC;



SELECT * FROM OrderFood
WHERE Note = N'MÁY 1';

