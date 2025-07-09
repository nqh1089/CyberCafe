USE CyberCafe4KL
GO

INSERT INTO Computer
	(IDComputer, NameComputer, PricePerMinute, CPU, RAM, GPU, Monitor, IPRadmin, ComputerStatus)
VALUES
	(1, 'MÁY 01', 200, 'Intel Xeon W-3400', 'DDR5', 'RTX 6000 Ada', '500Hz', '26.150.90.74', 1), -- nqh
	(2, 'MÁY 02', 200, 'Intel Xeon W-3400', 'DDR5', 'RTX 6000 Ada', '500Hz', '26.144.233.207', 1), -- minhkhoi
	(3, 'MÁY 03', 200, 'Intel Xeon W-3400', 'DDR5', 'RTX 6000 Ada', '500Hz', '26.228.105.146', 1), -- namkhanh
	(4, 'MÁY 04', 200, 'Intel Xeon W-3400', 'DDR5', 'RTX 6000 Ada', '500Hz', NULL, 1), -- baohan
	(5, 'MÁY 05', 200, 'Intel Xeon W-3400', 'DDR5', 'RTX 6000 Ada', '500Hz', NULL, 0),
	(6, 'MÁY 06', 200, 'Intel Xeon W-3400', 'DDR5', 'RTX 6000 Ada', '500Hz', NULL, 1),
	(7, 'MÁY 07', 200, 'Intel Xeon W-3400', 'DDR5', 'RTX 6000 Ada', '500Hz', NULL, 1),
	(8, 'MÁY 08', 200, 'Intel Xeon W-3400', 'DDR5', 'RTX 6000 Ada', '500Hz', NULL, 0),
	(9, 'MÁY 09', 200, 'Intel Xeon W-3400', 'DDR5', 'RTX 6000 Ada', '500Hz', NULL, 1),
	(10, 'MÁY 10', 200, 'Intel Xeon W-3400', 'DDR5', 'RTX 6000 Ada', '500Hz', NULL, 0);
	-- 0 = Đang hoạt động
	-- 1 = Máy trống
GO

SELECT *
FROM Computer


INSERT INTO FoodDrink (NameFood, ImageFood, Price, Category, Available) VALUES
-- ĐỒ UỐNG
(N'Sting Tài nộc quá nớn', 'dSting.jpg', 12000, N'Đồ uống', 1),
(N'Coca-Cola', 'dCoca.jpg', 10000, N'Đồ uống', 1),
(N'Trà xanh hạ hỏa', 'd0do.jpg', 9000, N'Đồ uống', 1),
(N'Giống Coca nhưng là Pépsi', 'dPepsi.jpg', 10000, N'Đồ uống', 1),
(N'Nước tinh khiết', 'dNuocLoc.jpg', 8000, N'Đồ uống', 1),

-- ĐỒ ĂN
(N'Mì tôm 2 trứng', 'fM2T.jpg', 12000, N'Đồ ăn', 1),
(N'Mì đặc biệt', 'fMDB.jpg', 10000, N'Đồ ăn', 1),
(N'Mì kay', 'fMK.jpg', 9000, N'Đồ ăn', 1),

-- GÓI NẠP THẺ
(N'Gói nạp 50.000 VNĐ', '50.jpg', 50000, N'Gói nạp', 1),
(N'Gói nạp 100.000 VNĐ', '100.jpg', 100000, N'Gói nạp', 1),
(N'Gói nạp 200.000 VNĐ', '200.jpg', 200000, N'Gói nạp', 1),
(N'Gói nạp 500.000 VNĐ', '500.jpg', 500000, N'Gói nạp', 1);
GO

SELECT *
FROM FoodDrink


-- 1. Tạo tài khoản test cho máy 02
IF NOT EXISTS (SELECT * FROM Account WHERE IDAccount = 200)
BEGIN
    INSERT INTO Account (IDAccount, NameAccount, PWAccount, RoleAccount, CCCD, PhoneNumber, Email, Gender, OnlineStatus, AccountStatus)
    VALUES (200, 'testmay02', '1234', 'CLIENT', '999999999999', '0999999999', 'test@may02.com', N'Nam', 1, 1);
END

-- 2. Log đang dùng máy 02
INSERT INTO LogAccess (IDComputer, ThoiGianBatDau, IDAccount)
VALUES (5, DATEADD(MINUTE, -20, GETDATE()), 200); -- dùng từ 20 phút trước

-- 3. Tạo đơn hàng
IF NOT EXISTS (SELECT * FROM OrderFood WHERE IDOrder = 1)
BEGIN
    INSERT INTO OrderFood (IDOrder, IDAccount)
    VALUES (1, 200);
END

-- 4. Lấy ID món Sting & Mì đặc biệt
DECLARE @idSting INT = (SELECT TOP 1 IDFood FROM FoodDrink WHERE NameFood LIKE N'Sting%');
DECLARE @idMiDB INT = (SELECT TOP 1 IDFood FROM FoodDrink WHERE NameFood = N'Mì đặc biệt');

-- 5. Thêm chi tiết đơn hàng
INSERT INTO OrderDetail (IDOrder, IDFood, Quantity, TotalPrice)
VALUES 
(1, @idSting, 1, 12000),
(1, @idMiDB, 2, 20000);



SELECT * FROM Account
WHERE NameAccount = '2' AND PWAccount = 'khach123' AND RoleAccount = 'CLIENT' AND AccountStatus = 1;



-- Check mã hđ
SELECT * FROM OrderFood WHERE IDOrder = 7;

-- Chi tiết sản phẩm trong hóa đơn
SELECT OD.*, FD.NameFood
FROM OrderDetail OD
JOIN FoodDrink FD ON OD.IDFood = FD.IDFood
WHERE IDOrder = 7;


