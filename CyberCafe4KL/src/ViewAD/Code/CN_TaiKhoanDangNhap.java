package ViewAD.Code;

// Gọi tên tài khoản đăng nhập
// gọi ở các tab: CN_TaiKhoanDangNhap.getTenTaiKhoan()

public class CN_TaiKhoanDangNhap {

    private static String tenTaiKhoan = "";

    // Gọi sau khi đăng nhập thành công
    public static void setTenTaiKhoan(String ten) {
        tenTaiKhoan = ten;
    }

    // Gọi từ các tab để hiển thị tên
    public static String getTenTaiKhoan() {
        return tenTaiKhoan;
    }
}
