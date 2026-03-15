# ☕ CyberCafe – Phần mềm quản lý kinh doanh quán Net và Cà Phê

<p align="center">
  <img src="CyberCafe-App/src/Assets/img/GiaoDienCho.gif" width="750">
</p>

<p align="center">
<b>Integrated Cyber Cafe Management System</b><br>
Quản lý • Dịch vụ • Thống kê • Chat Real-time
</p>

<p align="center">

<img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white"/>
<img src="https://img.shields.io/badge/SQL%20Server-CC2927?style=for-the-badge&logo=microsoftsqlserver&logoColor=white"/>
<img src="https://img.shields.io/badge/Architecture-MVC-blue?style=for-the-badge"/>
<img src="https://img.shields.io/badge/Realtime-Socket-green?style=for-the-badge"/>

</p>

---

# 🧠 Overview

**CyberCafe** là hệ thống quản lý vận hành dành cho mô hình kinh doanh kết hợp giữa **Tiệm Net** và **Quán Cà Phê**, được phát triển dưới dạng **Desktop Application** bằng **Java**.

Hệ thống giúp quản lý toàn bộ hoạt động của quán thông qua một nền tảng tập trung, bao gồm:

- Quản lý máy trạm
- Quản lý phiên sử dụng
- Order đồ ăn & thức uống
- Quản lý hóa đơn thanh toán
- Thống kê doanh thu
- Chat hỗ trợ real-time giữa khách và quản lý

Ứng dụng được xây dựng theo kiến trúc **MVC kết hợp Client–Server và Socket Communication**, đảm bảo khả năng **mở rộng, realtime interaction và quản lý dữ liệu hiệu quả**.

---

# 📊 System Scope

CyberCafe được thiết kế cho các mô hình kinh doanh:

- Cyber Game / Internet Cafe
- Quán Net kết hợp dịch vụ đồ ăn & thức uống
- Hệ thống quản lý máy trạm tập trung

Phạm vi hệ thống tập trung vào:

- Quản lý máy trạm và phiên sử dụng
- Quản lý dịch vụ & sản phẩm
- Quản lý hóa đơn và thanh toán
- Thống kê và báo cáo doanh thu
- Hỗ trợ khách hàng realtime

---

# 🎯 Problem & Solution

## ❌ Vấn đề thực tế

Trong nhiều quán net nhỏ và vừa, việc quản lý thường gặp các vấn đề:

- Quản lý máy và thời gian sử dụng thủ công
- Order đồ ăn qua lời nói dễ nhầm lẫn
- Khó theo dõi trạng thái máy theo thời gian thực
- Không có hệ thống hỗ trợ khách hàng trực tiếp

## ✅ Giải pháp

CyberCafe cung cấp một giải pháp quản lý tích hợp:

- Quản lý tập trung toàn bộ hệ thống
- Đồng bộ dữ liệu giữa các máy trạm
- Order trực tiếp từ máy khách
- Chat hỗ trợ realtime
- Thống kê và báo cáo tự động

---

# 🖥️ Môi trường sử dụng hệ thống

CyberCafe hoạt động trong **hai môi trường sử dụng chính** tương ứng với hai nhóm người dùng.

## 🧑‍💼 Hệ thống quản lý tại quầy (Admin / Boss)

Ứng dụng chạy trên máy tính tại quầy, nơi quản lý điều hành toàn bộ hoạt động:

- Theo dõi trạng thái toàn bộ máy trạm
- Quản lý thời gian sử dụng
- Nhận và xử lý order
- Quản lý hóa đơn thanh toán
- Xem thống kê doanh thu
- Chat hỗ trợ khách hàng

Đây là **trung tâm điều khiển của toàn bộ hệ thống**.

## 💻 Hệ thống khách hàng tại máy trạm (Client)

Ứng dụng Client được cài trên các máy dành cho khách:

- Đăng nhập sử dụng máy
- Theo dõi thời gian sử dụng
- Order đồ ăn & thức uống
- Gửi tin nhắn hỗ trợ
- Sử dụng dịch vụ quán

Các máy Client kết nối Server thông qua **Socket** để đồng bộ dữ liệu realtime.

---

# 🚀 Core Features

## 🖥️ Quản lý máy trạm

- Theo dõi trạng thái realtime
- Active / Idle / Maintenance
- Nhận diện máy theo IP
- Tính giờ tự động

## 🛒 Order dịch vụ & sản phẩm

- Order trực tiếp từ client
- Đồng bộ realtime về Admin
- Tự động thêm vào hóa đơn

## 🧾 Quản lý hóa đơn

- Tạo hóa đơn tự động
- Gộp giờ chơi + sản phẩm
- Lưu lịch sử giao dịch

## 📊 Thống kê & báo cáo

- Doanh thu theo tháng
- Thống kê lượt khách
- Phân tích hoạt động hệ thống

## 👤 Phân quyền người dùng

| Role      | Quyền               |
| --------- | ------------------- |
| 👑 Boss   | Toàn quyền hệ thống |
| 🤵 Admin  | Quản lý vận hành    |
| 💻 Client | Sử dụng dịch vụ     |

## 💬 Real-time Chat System

- Client ↔ Admin communication
- Java Socket networking
- Multi-client handling

---

# 🧩 Management Modules

Hệ thống gồm các module quản lý:

## 🎮 Đặt máy

- Khởi tạo phiên sử dụng
- Gán khách vào máy
- Theo dõi thời gian chơi
- Tính tiền tự động

## 🛒 Order

- Nhận order từ client
- Quản lý trạng thái đơn
- Đồng bộ hóa đơn

## 🍔 Sản phẩm

- Quản lý menu đồ ăn & nước uống
- Cập nhật giá sản phẩm
- Hiển thị cho client

## 🖥️ Máy tính

- Quản lý danh sách máy
- Theo dõi trạng thái
- Bảo trì / khóa máy

## 🧾 Hóa đơn

- Quản lý thanh toán
- Xem lịch sử giao dịch
- Chi tiết hóa đơn

## 📊 Thống kê

- Báo cáo doanh thu
- Phân tích hoạt động máy

## 👨‍💼 Nhân viên

- Quản lý tài khoản nhân viên
- Phân quyền truy cập

---

# 🧱 System Architecture

```
+------------------+
|   Client PCs     |
| (Net Machines)   |
+---------+--------+
          |
          | Socket Communication
          |
+---------v--------+
|   Server System  |
|  Admin Control   |
+---------+--------+
          |
          | JDBC
          |
+---------v--------+
|   SQL Server DB  |
+------------------+
```

---

## Kiến trúc code

```
CyberCafe-App
│
├── Model
├── Controller
├── ViewAD
├── ViewC
├── Server
├── Socket
├── SQL
└── Assets
```

---

# 🛠 Technology Stack

| Category     | Technology           |
| ------------ | -------------------- |
| Language     | Java                 |
| UI Framework | Java Swing           |
| Database     | Microsoft SQL Server |
| Networking   | Java Socket          |
| Architecture | MVC                  |
| Platform     | Windows Desktop      |

---

# ⚡ Real-time Communication Flow

```
Client Login
↓
Socket Connection
↓
Server Handler Thread
↓
Message Processing
↓
Admin Interface Update
```

---

# 📂 Project Structure

```
CyberCafe/
│
├── CyberCafe-App/
│   ├── Controller/
│   ├── Model/
│   ├── Server/
│   ├── Socket/
│   ├── ViewAD/
│   ├── ViewC/
│   ├── SQL/
│   └── Assets/
│
├── Documents/
│   ├── CyberCafe_SRS
│   ├── Ideas triển khai dự án.txt
│   ├── Quy ước.txt
│   ├── Activity/
│   └── USE CASE/
│
└── README.md
```

---

# 📌 My Role as Business Analyst

Trong dự án CyberCafe, tôi đảm nhận vai trò **Business Analyst & Team Leader**, chịu trách nhiệm:

- Phân tích bài toán vận hành Cyber Cafe
- Thu thập và xác định **System Requirements**
- Xây dựng **Use Case Diagram và Activity Diagram**
- Thiết kế luồng hoạt động của hệ thống
- Là cầu nối giữa **yêu cầu nghiệp vụ và triển khai kỹ thuật**
- Hỗ trợ kiểm thử và đảm bảo hệ thống đáp ứng yêu cầu ban đầu

---

# ⭐ Highlights

- Desktop enterprise-style application
- MVC architecture implementation
- Real-time socket communication
- Multi-role access control
- Client–Server design
- Integrated cyber cafe management

---

# 👨‍💻 Author

<p align="center">
<b>Nguyễn Quang Huy</b> <br>
Business Analyst • Team Leader • System Analyst • Tester  
</p>

<p align="center" style="font-size:12px; color:gray;">
Dự án được thực hiện cùng các thành viên trong nhóm phát triển CyberCafe.
</p>

---

<p align="center">
✨ Transforming business problems into system solutions ✨<br>
<sub>Business Analysis • System Thinking • Practical Implementation</sub>
</p>
