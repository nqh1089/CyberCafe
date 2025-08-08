package ViewC.Code;

// Lớp chứa các biến toàn cục được sử dụng trong phía Client.
//Các biến này lưu trữ thông tin về máy tính hiện tại và tài khoản người dùng đang đăng nhập.
public class CN_BienToanCuc {

    /**
     * ID tài khoản người dùng sau khi đăng nhập (tương ứng với IDAccount trong
     * bảng Account). Mặc định là -1 khi chưa có tài khoản nào đăng nhập.
     */
    public static int IDAccount = -1;

    /**
     * ID của máy tính đang chạy ứng dụng Client (tương ứng với IDComputer trong
     * bảng Computer). Mặc định là -1 khi ID máy chưa được xác định.
     */
    public static int IDComputer = -1;

    /**
     * Tên của máy tính đang chạy ứng dụng Client (tương ứng với NameComputer
     * trong bảng Computer). Mặc định là chuỗi rỗng khi tên máy chưa được xác
     * định.
     */
    public static String TenMay = "";

    /**
     * Tên tài khoản người dùng vừa đăng nhập (tương ứng với NameAccount trong
     * bảng Account). Mặc định là chuỗi rỗng khi chưa có tài khoản nào đăng
     * nhập.
     */
    public static String TenTaiKhoan = "";

    /**
     * ID của phiên đăng nhập hiện tại (tương ứng với LogAccess.IDLogAccess).
     * Được sử dụng để theo dõi phiên làm việc của người dùng. Mặc định là -1
     * khi chưa có phiên đăng nhập nào được tạo.
     */
    public static int LogAccessID = -1;
    public static long TongThoiGianPhut;

    // ✅ Hàm public để lấy tên máy hiện tại từ các class khác
    public static String getTenMayClient() {
        return TenMay;
    }
}
