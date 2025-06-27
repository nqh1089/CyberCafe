CREATE DATABASE CYBERCAFE4KL
GO
USE CYBERCAFE4KL
GO

CREATE TABLE Account
(
	IDAccount INT PRIMARY KEY,
	NameAccount VARCHAR(50) UNIQUE NOT NULL,
	PWAccount VARCHAR(50) NOT NULL,
	RoleAccount VARCHAR(50) DEFAULT 'CLIENT' NOT NULL,
	-- CLIENT / ADMIN / BOSS
	CCCD VARCHAR(12) UNIQUE NOT NULL,
	-- Nếu 1 ngày đẹp trời nào đó, nhà nước tăng số cccd lên 13 ký tự thì cũng không sao cả, ALTER TABLE cân hết 
	PhoneNumber VARCHAR(10) UNIQUE NOT NULL,
	CurrentMoney INT NOT NULL CHECK (CurrentMoney >= 0) DEFAULT 0,
	-- Số tiền hiện tại của tài khoản, mặc định là 0
	AccountStatus BIT DEFAULT 1 NOT NULL,
	-- 1: True (Tài Khoản Còn Hoạt Động) - 0: False (Tài Khoản Ngưng Hoạt Động)
	Created_at DATETIME DEFAULT GETDATE() NOT NULL
);
GO

CREATE PROC SP_AddAcount
	@IDAccount INT,
	@NameAccount VARCHAR(50),
	@PWAccount VARCHAR(50),
	@RoleAccount VARCHAR(50),
	@CCCD VARCHAR(12),
	@PhoneNumber VARCHAR(10),
	@CurrentMoney INT,
	@AccountStatus BIT
AS
BEGIN
	IF (@IDAccount IS NULL OR @NameAccount IS NULL OR @PWAccount IS NULL OR @RoleAccount IS NULL OR @CCCD IS NULL OR @PhoneNumber IS NULL OR @CurrentMoney IS NULL OR @AccountStatus IS NULL)
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
			(IDAccount, NameAccount, PWAccount, RoleAccount, CCCD, PhoneNumber, CurrentMoney, AccountStatus)
		VALUES
			(@IDAccount, @NameAccount, @PWAccount, @RoleAccount, @CCCD, @PhoneNumber, @CurrentMoney, @AccountStatus)
	END
END
GO
EXEC SP_AddAcount 1, N'mkhoixyz', N'1234', N'BOSS', N'030208003266', N'0382526800', 999999999, 1;
EXEC SP_AddAcount 2, N'catzpat', N'1234', N'ADMIN', N'012345678999', N'0123456789', 999999999, 1;
EXEC SP_AddAcount 3, N'nqh1089', N'1234', N'ADMIN', N'012345678998', N'0987654321', 999999999, 1;
EXEC SP_AddAcount 4, N'bnah07', N'1234', N'ADMIN', N'012345678997', N'0987654322', 999999999, 1;
GO

CREATE VIEW V_Account
AS
	SELECT
		IDAccount,
		NameAccount,
		RoleAccount,
		CCCD,
		PhoneNumber,
		CurrentMoney,
		AccountStatus,
		FORMAT(Created_at, 'dd/MM/yyyy HH:mm:ss') AS Created_at
	FROM Account;
GO
-- Ca làm của nhân viên
CREATE TABLE Shift
(
	IDShift INT PRIMARY KEY,
	IDAccount INT FOREIGN KEY REFERENCES Account(IDAccount),
	StartShift DATETIME,
	EndShift DATETIME
);
GO

CREATE TABLE Computer
(
	IDComputer INT PRIMARY KEY,
	NameComputer VARCHAR(50) UNIQUE NOT NULL,
	PricePerMinute MONEY NOT NULL CHECK(PricePerMinute >= 0) DEFAULT 200,
	-- Mặc định mỗi phút là 200 đồng
	ComputerStatus BIT DEFAULT 1 NOT NULL,
	-- 1: Available ||  0: In Use / Maintenance
	Note NVARCHAR(255)
);
GO
CREATE VIEW V_ComputerStatus
AS
	SELECT
		IDComputer,
		NameComputer,
		ComputerStatus,
		CASE 
		WHEN ComputerStatus = 1 THEN N'Available'
		ELSE N'In Use / Maintenance' -- Đang sử dụng /Bảo trì
	END AS ComputerStatusText,
		Note
	FROM Computer;
GO
-- UPDATE Computer SET PricePerMinute = 1500 WHERE IDComputer = 1;
-- UPDATE Computer SET PricePerMinute = 1000 WHERE IDComputer = 2;

CREATE TABLE ComputerUsage
(
	IDUsage INT PRIMARY KEY,
	IDAccount INT FOREIGN KEY REFERENCES Account(IDAccount),
	IDComputer INT FOREIGN KEY REFERENCES Computer(IDComputer),
	StartTime DATETIME DEFAULT GETDATE(),
	EndTime DATETIME,
	TotalTime AS DATEDIFF(MINUTE, StartTime, EndTime),
	Cost MONEY
);
GO

CREATE TABLE FoodDrink
(
	IDFood INT PRIMARY KEY,
	NameFood NVARCHAR(100) NOT NULL,
	Price MONEY NOT NULL CHECK (Price >= 0) DEFAULT 0,
	Category NVARCHAR(50),
	-- Đồ ăn, Đồ uống, Combo
	Available BIT DEFAULT 1
);
GO

CREATE TABLE OrderFood
(
	IDOrder INT PRIMARY KEY,
	IDAccount INT FOREIGN KEY REFERENCES Account(IDAccount),
	OrderTime DATETIME DEFAULT GETDATE()
);

CREATE TABLE OrderDetail
(
	IDOrderDetail INT PRIMARY KEY IDENTITY,
	IDOrder INT FOREIGN KEY REFERENCES OrderFood(IDOrder),
	IDFood INT FOREIGN KEY REFERENCES FoodDrink(IDFood),
	Quantity INT,
	TotalPrice MONEY NOT NULL CHECK (TotalPrice >= 0) DEFAULT 0
	-- Cột này sẽ được tính khi thêm dữ liệu
);
-- INSERT INTO OrderDetail (IDOrder, IDFood, Quantity, TotalPrice) VALUES (@IDOrder, @IDFood, @Quantity, @Quantity * @Price);
GO

CREATE PROC SP_AddOrderWithDetails
	@IDOrder INT,
	@IDAccount INT,
	@IDFood INT,
	@Quantity INT
AS
BEGIN
	DECLARE @Price MONEY, @Total MONEY;

	-- Lấy giá món ăn
	SELECT @Price = Price
	FROM FoodDrink
	WHERE IDFood = @IDFood;
	SET @Total = @Price * @Quantity;

	-- Kiểm tra đủ tiền
	DECLARE @CurrentMoney INT;
	SELECT @CurrentMoney = CurrentMoney
	FROM Account
	WHERE IDAccount = @IDAccount;

	IF @CurrentMoney < @Total
    BEGIN
		PRINT N'Không đủ tiền để đặt món';
		RETURN;
	END

	-- Trừ tiền
	UPDATE Account SET CurrentMoney = CurrentMoney - @Total WHERE IDAccount = @IDAccount;

	-- Thêm đơn nếu chưa có
	IF NOT EXISTS (SELECT *
	FROM OrderFood
	WHERE IDOrder = @IDOrder)
    BEGIN
		INSERT INTO OrderFood
			(IDOrder, IDAccount)
		VALUES
			(@IDOrder, @IDAccount);
	END

	-- Thêm chi tiết món
	INSERT INTO OrderDetail
		(IDOrder, IDFood, Quantity, TotalPrice)
	VALUES
		(@IDOrder, @IDFood, @Quantity, @Total);
	PRINT N'Đặt món thành công';
END;
GO

CREATE OR ALTER PROC SP_CalculateCostByUsage
	@IDUsage INT
AS
BEGIN
	DECLARE @IDAccount INT, @IDComputer INT;
	DECLARE @TotalMinutes INT, @TotalCost MONEY, @CurrentMoney INT, @PricePerMinute MONEY;

	-- Cập nhật thời gian kết thúc
	UPDATE ComputerUsage
    SET EndTime = GETDATE()
    WHERE IDUsage = @IDUsage;

	-- Lấy thông tin cần thiết
	SELECT
		@IDAccount = IDAccount,
		@IDComputer = IDComputer,
		@TotalMinutes = DATEDIFF(MINUTE, StartTime, EndTime)
	FROM ComputerUsage
	WHERE IDUsage = @IDUsage;

	-- Lấy giá theo từng máy
	SELECT @PricePerMinute = PricePerMinute
	FROM Computer
	WHERE IDComputer = @IDComputer;

	SET @TotalCost = @TotalMinutes * @PricePerMinute;

	-- Lấy số dư hiện tại
	SELECT @CurrentMoney = CurrentMoney
	FROM Account
	WHERE IDAccount = @IDAccount;

	-- Kiểm tra đủ tiền
	IF @CurrentMoney < @TotalCost
    BEGIN
		PRINT N'Không đủ tiền để thanh toán thời gian sử dụng.';
		RETURN;
	END

	-- Cập nhật chi phí và trừ tiền
	UPDATE ComputerUsage
    SET Cost = @TotalCost
    WHERE IDUsage = @IDUsage;

	UPDATE Account
    SET CurrentMoney = CurrentMoney - @TotalCost
    WHERE IDAccount = @IDAccount;

	PRINT N'Tính tiền thành công.';
END;
GO
