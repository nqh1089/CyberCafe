package Model;

public class Products {
    private int id;
    private String tenSP;
    private int gia;
    private String loaiSP;
    private String hinhAnh;
    private int trangThai;

    public Products() {}

    public Products(int id, String tenSP, int gia, String loaiSP, String hinhAnh, int trangThai) {
        this.id = id;
        this.tenSP = tenSP;
        this.gia = gia;
        this.loaiSP = loaiSP;
        this.hinhAnh = hinhAnh;
        this.trangThai = trangThai;
    }

    // ===== GET =====
    public int getId() {
        return id;
    }

    public String getTenSP() {
        return tenSP;
    }

    public int getGia() {
        return gia;
    }

    public String getLoaiSP() {
        return loaiSP;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public int getTrangThai() {
        return trangThai;
    }

    // ===== SET =====
    public void setId(int id) {
        this.id = id;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public void setLoaiSP(String loaiSP) {
        this.loaiSP = loaiSP;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}
