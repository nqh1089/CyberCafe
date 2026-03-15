package ViewC.Code;

import Controller.DBConnection;
import javax.sound.sampled.*;
import java.io.File;
import java.sql.*;
import java.sql.Timestamp;

public class C_TBThoiGian {

    private static boolean warnedLowBalance = false; // 5 phút
    private static boolean warnedOneMinute = false;  // 1 phút

    // Lấy đường dẫn tuyệt đối tới file âm thanh
    private static final String LOW_BAL_SOUND = System.getProperty("user.dir") + File.separator + "img" + File.separator + "5Phut.WAV";
    private static final String ONE_MIN_SOUND = System.getProperty("user.dir") + File.separator + "img" + File.separator + "1Phut.WAV";

    public static void checkBalanceAndAct() {
        int idAcc = CN_BienToanCuc.IDAccount;
        int idComp = CN_BienToanCuc.IDComputer;
        if (idAcc <= 0 || idComp <= 0) {
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                return;
            }

            double balance = getBalance(conn, idAcc);
            long usedMin = C2_ThoiGianSuDung.getThoiGianSuDungPhut(idComp, idAcc);
            double pricePerMinute = getPricePerMinute(conn, idComp);
            double chiPhiGio = usedMin * pricePerMinute;

            Timestamp startTime = getStartTime(conn, idComp, idAcc);
            double chiPhiDichVu = 0;
            if (startTime != null) {
                chiPhiDichVu = C2_ChiPhiDichVu.layTongTienOrderClient(CN_BienToanCuc.TenMay, startTime);
            }

            double remaining = balance - chiPhiGio - chiPhiDichVu;

            if (remaining <= 0) {
                System.out.println("[Balance] Hết tiền → Logout máy");
                CN_LogoutMay.logoutMay();
                return;
            }

            // Cảnh báo 5 phút
            if (!warnedLowBalance && remaining <= 1000) {
                warnedLowBalance = true;
                playWavAsync(LOW_BAL_SOUND);
                System.out.println("[Balance] Còn ≤ 1000 → Cảnh báo âm thanh 5 phút");
            }

            // Cảnh báo 1 phút
            if (!warnedOneMinute && remaining <= 200) {
                warnedOneMinute = true;
                playWavAsync(ONE_MIN_SOUND);
                System.out.println("[Balance] Còn ≤ 200 → Cảnh báo âm thanh 1 phút");
            }

        } catch (Exception ex) {
            System.out.println("Lỗi C_TBThoiGian.checkBalanceAndAct: " + ex.getMessage());
        }
    }

    public static void resetWarnings() {
        warnedLowBalance = false;
        warnedOneMinute = false;
    }

    private static double getBalance(Connection conn, int idAccount) throws SQLException {
        String sql = "SELECT Balance FROM Account WHERE IDAccount = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAccount);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("Balance");
                }
            }
        }
        return 0;
    }

    private static double getPricePerMinute(Connection conn, int idComputer) throws SQLException {
        String sql = "SELECT PricePerMinute FROM Computer WHERE IDComputer = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idComputer);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("PricePerMinute");
                }
            }
        }
        return 200.0; // fallback
    }

    private static Timestamp getStartTime(Connection conn, int idComputer, int idAccount) throws SQLException {
        String sql = """
            SELECT TOP 1 StartTime
            FROM ComputerUsage
            WHERE IDComputer = ? AND IDAccount = ? AND EndTime IS NULL
            ORDER BY StartTime DESC
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idComputer);
            ps.setInt(2, idAccount);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getTimestamp("StartTime");
                }
            }
        }
        return null;
    }

    private static void playWavAsync(String path) {
        new Thread(() -> {
            try {
                File file = new File(path);
                if (!file.exists()) {
                    System.err.println("[Audio] Không thấy file: " + path);
                    return;
                }
                try (AudioInputStream ais = AudioSystem.getAudioInputStream(file)) {
                    Clip clip = AudioSystem.getClip();
                    clip.open(ais);
                    clip.start();
                }
            } catch (Exception e) {
                System.out.println("Lỗi phát âm thanh: " + e.getMessage());
            }
        }, "balance-alert").start();
    }
}
