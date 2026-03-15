package Controller;

import Model.Products;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO {

    public List<Products> getAllProducts() {
        List<Products> danhSach = new ArrayList<>();
        String sql = "SELECT IDFood, NameFood, Price, Category, ImageFood, Available FROM tableOrder";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Products sp = new Products();
                sp.setId(rs.getInt("IDFood"));
                sp.setTenSP(rs.getString("NameFood"));
                sp.setGia(rs.getInt("Price"));
                sp.setLoaiSP(rs.getString("Category"));
                sp.setHinhAnh(rs.getString("ImageFood"));
                sp.setTrangThai(rs.getInt("Available")); // 1: đang bán, 0: ngừng bán

                danhSach.add(sp);
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy sản phẩm: " + e.getMessage());
        }

        return danhSach;
    }
}
