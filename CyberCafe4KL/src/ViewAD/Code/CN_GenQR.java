package ViewAD.Code;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class CN_GenQR {

    // Mặc định ngân hàng và người nhận – sửa tại đây nếu cần
    private static final String BANK_CODE = "vpbank"; // VPBank
    private static final String ACCOUNT_NUMBER = "30503081089"; // Đúng định dạng
    private static final String ACCOUNT_NAME = "NGUYEN QUANG HUY";

    /**
     * Tạo link QR động VietQR để tải ảnh thanh toán
     *
     * @param soTien Số tiền cần chuyển (VND)
     * @param noiDung Nội dung chuyển khoản (gợi ý: mã đơn hàng)
     * @return Link ảnh QR trả về từ server VietQR
     */
    public static String TaoLinkQR(double soTien, String noiDung) {
        try {
            String encodedNoiDung = URLEncoder.encode(noiDung, "UTF-8").replace("+", "%20");
            String encodedTen = URLEncoder.encode(ACCOUNT_NAME, "UTF-8").replace("+", "%20");

            return "https://img.vietqr.io/image/"
                    + BANK_CODE + "-" + ACCOUNT_NUMBER + "-qr_only.png"
                    + "?amount=" + (int) soTien
                    + "&addInfo=" + encodedNoiDung
                    + "&accountName=" + encodedTen;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
