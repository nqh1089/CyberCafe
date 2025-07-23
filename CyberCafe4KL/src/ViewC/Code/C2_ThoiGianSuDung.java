package ViewC.Code;

import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import Controller.DBConnection;

public class C2_ThoiGianSuDung {

    public static long getThoiGianSuDungPhut(int idComputer, int idAccount) {
        long usedMin = 0;

        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                SELECT TOP 1 StartTime
                FROM ComputerUsage
                WHERE IDComputer = ? AND IDAccount = ? AND EndTime IS NULL
                ORDER BY StartTime DESC
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idComputer);
            ps.setInt(2, idAccount);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Timestamp startTime = rs.getTimestamp("StartTime");
                usedMin = Duration.between(startTime.toInstant(), Instant.now()).toMinutes();
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            System.out.println("Lỗi C2_ThoiGianSuDung: " + e.getMessage());
        }

        return usedMin;
    }
}
