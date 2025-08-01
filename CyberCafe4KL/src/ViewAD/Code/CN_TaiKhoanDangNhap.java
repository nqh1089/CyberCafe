package ViewAD.Code;

import java.sql.Timestamp;

public class CN_TaiKhoanDangNhap {

    private static String tenTaiKhoan = "";
    private static Timestamp thoiGianDangNhap;
    private static int idTaiKhoan = -1; // ✅ Thêm biến lưu ID tài khoản

    // Gọi sau khi đăng nhập thành công
    public static void setTenTaiKhoan(String ten) {
        tenTaiKhoan = ten;
    }

    public static String getTenTaiKhoan() {
        return tenTaiKhoan;
    }

    public static void setThoiGianDangNhap(Timestamp time) {
        thoiGianDangNhap = time;
    }

    public static Timestamp getThoiGianDangNhap() {
        return thoiGianDangNhap;
    }

    // ✅ Thêm getter/setter cho ID tài khoản
    public static void setIDTaiKhoan(int id) {
        idTaiKhoan = id;
    }

    public static int getIDTaiKhoan() {
        return idTaiKhoan;
    }
}
