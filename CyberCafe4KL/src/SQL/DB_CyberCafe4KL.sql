-- USE master
-- GO
-- ALTER DATABASE CyberCafe4KL SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
-- DROP DATABASE CyberCafe4KL;
-- GO

CREATE DATABASE CyberCafe4KL
GO
USE CyberCafe4KL
GO

CREATE TABLE Account
(
	IDAccount INT PRIMARY KEY,
	NameAccount VARCHAR(50) UNIQUE NOT NULL,
	PWAccount VARCHAR(50) NOT NULL,
	RoleAccount VARCHAR(50) CHECK (RoleAccount IN ('CLIENT', 'ADMIN', 'BOSS')) DEFAULT 'CLIENT',
	-- CLIENT / ADMIN / BOSS
	CCCD VARCHAR(12) UNIQUE NOT NULL,
	-- Nếu 1 ngày đẹp trời nào đó, nhà nước tăng số cccd lên 13 ký tự thì cũng không sao cả, ALTER TABLE cân hết 
	PhoneNumber VARCHAR(10) UNIQUE NULL,
	Email VARCHAR(50) NULL,
	Gender NVARCHAR(3) NULL,
	-- 'Nam', 'Nữ'
	OnlineStatus BIT DEFAULT 0 NOT NULL,
	-- 1: Online - 0: Offline 
	AccountStatus BIT DEFAULT 1 NOT NULL,
	-- 1: True (Tài Khoản Còn Hoạt Động) - 0: False (Tài Khoản Ngưng Hoạt Động)
	Balance MONEY NOT NULL DEFAULT 0,
	-- Số dư khả dụng của tài khoản Client
	Created_at DATETIME DEFAULT GETDATE() NOT NULL
);
GO

CREATE PROC SP_AddAccount
	@IDAccount INT,
	@NameAccount VARCHAR(50),
	@PWAccount VARCHAR(50),
	@RoleAccount VARCHAR(50),
	@CCCD VARCHAR(12),
	@PhoneNumber VARCHAR(10),
	@Email VARCHAR(50),
	@Gender NVARCHAR(3),
	@OnlineStatus BIT,
	@AccountStatus BIT
AS
BEGIN
	IF (@IDAccount IS NULL OR @NameAccount IS NULL OR @PWAccount IS NULL OR @RoleAccount IS NULL OR @CCCD IS NULL OR @PhoneNumber IS NULL OR @Email IS NULL OR @Gender IS NULL OR @AccountStatus IS NULL OR @OnlineStatus IS NULL)
		PRINT N'VUI LÒNG NHẬP ĐỦ THÔNG TIN'
	ELSE IF EXISTS (SELECT *
	FROM Account
	WHERE IDAccount = @IDAccount)
    	PRINT N'IDAccount ALREADY EXISTS'
	ELSE IF EXISTS (SELECT *
	FROM Account
	WHERE NameAccount = @NameAccount)
    	PRINT N'NameAccount ALREADY EXISTS'
	ELSE IF EXISTS (SELECT *
	FROM Account
	WHERE CCCD = @CCCD)
    	PRINT N'CCCD ALREADY EXISTS'
	ELSE IF EXISTS (SELECT *
	FROM Account
	WHERE PhoneNumber = @PhoneNumber)
    	PRINT N'PhoneNumber ALREADY EXISTS'
	ELSE IF (@RoleAccount = 'BOSS' AND EXISTS (SELECT *
		FROM Account
		WHERE RoleAccount = 'BOSS'))
		PRINT N'BOSS ĐÃ CÓ CHỦ CÒN BẠN THÌ KHÔNG'
	ELSE 
		BEGIN
		INSERT INTO Account
			(IDAccount, NameAccount, PWAccount, RoleAccount, CCCD, PhoneNumber, Email, Gender, OnlineStatus, AccountStatus)
		VALUES
			(@IDAccount, @NameAccount, @PWAccount, @RoleAccount, @CCCD, @PhoneNumber, @Email, @Gender, @OnlineStatus, @AccountStatus)
	END
END
GO

EXEC SP_AddAccount 1, N'nqh1089', N'123', N'BOSS', N'012345678998', N'0987654321', 'nqh1089@cb4kl.com', N'Nam', 0, 1;
EXEC SP_AddAccount 2, N'catzpat', N'123', N'ADMIN', N'012345678999', N'0123456789', 'catzpat@cb4kl.com', N'Nam', 0, 1;
EXEC SP_AddAccount 3, N'mkhoixyz', N'123', N'ADMIN', N'030208003266', N'0382526800', 'mkhoixyz@cb4kl.com', N'Nam', 0, 1;
EXEC SP_AddAccount 4, N'bnah07', N'123', N'ADMIN', N'012345678997', N'0987654322', 'bnah07@cb4kl.com', N'Nữ', 0, 1;

EXEC SP_AddAccount 5, N'1', N'1', N'ADMIN', N'111111111111', N'1111111111', '1@cb4kl.com', N'Nam', 0, 1;

GO


CREATE PROC SP_DangKyNhanhClient
	@NameAccount VARCHAR(50),
	@PhoneNumber VARCHAR(10),
	@Gender NVARCHAR(3),
	@CCCD VARCHAR(12)
AS
BEGIN
	IF EXISTS (SELECT 1
	FROM Account
	WHERE NameAccount = @NameAccount)
		RETURN 1;
	-- trùng tài khoản

	IF EXISTS (SELECT 1
	FROM Account
	WHERE PhoneNumber = @PhoneNumber)
		RETURN 2;
	-- trùng SĐT

	IF EXISTS (SELECT 1
	FROM Account
	WHERE CCCD = @CCCD)
		RETURN 3;
	-- trùng CCCD

	DECLARE @ID INT = (SELECT ISNULL(MAX(IDAccount), 0) + 1
	FROM Account);

	INSERT INTO Account
		(IDAccount, NameAccount, PWAccount, RoleAccount, CCCD, PhoneNumber, Gender)
	VALUES
		(@ID, @NameAccount, '123', 'CLIENT', @CCCD, @PhoneNumber, @Gender);

	RETURN 0;
END

GO
CREATE VIEW V_Account
AS
	SELECT
		IDAccount, NameAccount, RoleAccount, CCCD, PhoneNumber, Email, Gender,
		OnlineStatus, AccountStatus, FORMAT(Created_at, 'dd/MM/yyyy HH:mm:ss') AS Created_at
	FROM Account;
GO
-- SELECT * FROM V_Account;

CREATE TABLE Computer
(
	IDComputer INT PRIMARY KEY,
	NameComputer VARCHAR(50) NOT NULL,
	PricePerMinute MONEY NOT NULL CHECK(PricePerMinute >= 0) DEFAULT 200,
	CPU VARCHAR(50) NULL,
	RAM VARCHAR(50) NULL,
	GPU VARCHAR(50) NULL,
	Monitor VARCHAR(50) NULL,
	IPRadmin VARCHAR(20) NULL,
	ComputerStatus INT NOT NULL DEFAULT 1
	-- 0: ĐANG SỬ DỤNG | 1: ĐANG TRỐNG | 2: BẢO TRÌ
);
GO

CREATE VIEW V_ComputerStatus
AS
	SELECT
		IDComputer,
		ComputerStatus,
		CASE 
		WHEN ComputerStatus = 0 THEN N'Đang sử dụng'
		WHEN ComputerStatus = 1 THEN N'Đang trống'
		WHEN ComputerStatus = 2 THEN N'Bảo trì'
		ELSE N'Không xác định'
	END AS ComputerStatusText
	FROM Computer;
GO

-- Tạo lại bảng ComputerUsage
CREATE TABLE ComputerUsage
(
	IDUsage INT IDENTITY(1,1) PRIMARY KEY,
	IDAccount INT FOREIGN KEY REFERENCES Account(IDAccount),
	IDComputer INT FOREIGN KEY REFERENCES Computer(IDComputer),
	StartTime DATETIME DEFAULT GETDATE(),
	EndTime DATETIME,
	TotalTime AS DATEDIFF(MINUTE, StartTime, EndTime),
	Cost MONEY
	-- Tổng tiền = phút chơi * đơn giá máy
);
GO

CREATE TABLE LogAccess
(
    IDLog INT IDENTITY(1,1) PRIMARY KEY,
    IDComputer INT NOT NULL FOREIGN KEY REFERENCES Computer(IDComputer),
    ThoiGianBatDau DATETIME NOT NULL,
    ThoiGianKetThuc DATETIME NULL,
    IDAccount INT NULL FOREIGN KEY REFERENCES Account(IDAccount)
);

CREATE TABLE FoodDrink
(
	IDFood INT IDENTITY(1,1) PRIMARY KEY,
	NameFood NVARCHAR(100) NOT NULL,
	ImageFood NVARCHAR(255) NOT NULL,
	Price MONEY NOT NULL CHECK (Price >= 0) DEFAULT 0,
	Category NVARCHAR(50),
	Available BIT DEFAULT 1
	-- 1: Còn hàng, còn bán, 0: Hết hàng, không bán nữa
);
GO

CREATE TABLE OrderFood
(
	IDOrder INT PRIMARY KEY,
	IDAccount INT FOREIGN KEY REFERENCES Account(IDAccount),
	OrderTime DATETIME DEFAULT GETDATE()
);
GO

CREATE TABLE OrderDetail
(
	IDOrderDetail INT PRIMARY KEY IDENTITY,
	IDOrder INT FOREIGN KEY REFERENCES OrderFood(IDOrder),
	IDFood INT FOREIGN KEY REFERENCES FoodDrink(IDFood),
	Quantity INT NOT NULL CHECK (Quantity > 0),
	-- Số lượng món ăn
	TotalPrice MONEY NOT NULL CHECK (TotalPrice >= 0) DEFAULT 0
);
-- INSERT INTO OrderDetail (IDOrder, IDFood, Quantity, TotalPrice) VALUES (@IDOrder, @IDFood, @Quantity, @Quantity * @Price);
GO

CREATE TABLE Message
(
	IDMessage INT PRIMARY KEY IDENTITY,
	SenderID INT NOT NULL FOREIGN KEY REFERENCES Account(IDAccount),
	-- ID người gửi (liên kết tới Account)
	ReceiverID INT NOT NULL FOREIGN KEY REFERENCES Account(IDAccount),
	-- ID người nhận (liên kết tới Account)
	Content NVARCHAR(255) NOT NULL,
	-- Nội dung tin nhắn
	SentAt DATETIME DEFAULT GETDATE() NOT NULL,
	-- Thời điểm gửi
	IsRead BIT DEFAULT 0
	-- 0: chưa đọc, 1: đã đọc
);
GO

-- Bảng Hóa Đơn
CREATE TABLE Invoice
(
	IDInvoice INT PRIMARY KEY IDENTITY,
	IDAccount INT FOREIGN KEY REFERENCES Account(IDAccount),
	CreateAt DATETIME DEFAULT GETDATE() NOT NULL,
	TotalAmount MONEY NOT NULL CHECK (TotalAmount >= 0) DEFAULT 0,
	Status NVARCHAR(50) DEFAULT N'Paid'
	-- Paid / Pending / Cancelled
);
GO

-- Bảng Chi tiết hóa đơn
CREATE TABLE InvoiceDetail
(
	IDInvoiceDetail INT PRIMARY KEY IDENTITY,
	IDInvoice INT FOREIGN KEY REFERENCES Invoice(IDInvoice),
	IDFood INT FOREIGN KEY REFERENCES FoodDrink(IDFood),
	Quantity INT NOT NULL CHECK (Quantity > 0),
	UnitPrice MONEY NOT NULL CHECK (UnitPrice >= 0),
	TotalPrice MONEY NOT NULL CHECK (TotalPrice >= 0) DEFAULT 0,
	-- Tính toán tổng giá trị của mặt hàng
	TotalAmount MONEY NOT NULL CHECK (TotalAmount >= 0) DEFAULT 0
);
GO

-- Bảng up ảnh
CREATE TABLE Assets_Anh
(
    IDAsset INT IDENTITY(1,1) PRIMARY KEY,
    TenAnh NVARCHAR(100) NOT NULL,
    DuongDan NVARCHAR(255) NOT NULL,
    MoTa NVARCHAR(255) NULL
);
GO
