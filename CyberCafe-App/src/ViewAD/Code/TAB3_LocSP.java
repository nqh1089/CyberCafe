package ViewAD.Code;

import java.util.List;
import java.util.stream.Collectors;
import Model.FoodItem;

public class TAB3_LocSP {

    // Lọc theo loại sản phẩm
    public static List<FoodItem> LocTheoLoai(List<FoodItem> ds, String loai) {
        if (loai.equalsIgnoreCase("TẤT CẢ")) {
            return ds;
        }

        return ds.stream()
                .filter(p -> p.getLoai().equalsIgnoreCase(loai))
                .collect(Collectors.toList());
    }

    // Lọc theo trạng thái
    public static List<FoodItem> LocTheoTrangThai(List<FoodItem> ds, String trangThai) {
        if (trangThai.equalsIgnoreCase("TẤT CẢ")) {
            return ds;
        }

        if (trangThai.equalsIgnoreCase("ĐANG BÁN")) {
            return ds.stream()
                    .filter(p -> p.getTrangThai() == 1)
                    .collect(Collectors.toList());
        }

        if (trangThai.equalsIgnoreCase("NGỪNG BÁN")) {
            return ds.stream()
                    .filter(p -> p.getTrangThai() == 0)
                    .collect(Collectors.toList());
        }

        return ds;
    }

    // Kết hợp lọc loại + trạng thái
    public static List<FoodItem> LocKetHop(List<FoodItem> ds, String loai, String trangThai) {
        List<FoodItem> locTheoLoai = LocTheoLoai(ds, loai);
        return LocTheoTrangThai(locTheoLoai, trangThai);
    }
}
