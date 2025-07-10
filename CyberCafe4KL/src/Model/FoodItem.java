package Model;

public class FoodItem {
    private int maSP;
    private String tenSP;
    private String hinhAnh;
    private double gia;
    private String loai;
    private int trangThai; // 1 = còn bán, 0 = ngừng bán

    public FoodItem() {}

    public FoodItem(int maSP, String tenSP, String hinhAnh, double gia, String loai, int trangThai) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.hinhAnh = hinhAnh;
        this.gia = gia;
        this.loai = loai;
        this.trangThai = trangThai;
    }

    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}
