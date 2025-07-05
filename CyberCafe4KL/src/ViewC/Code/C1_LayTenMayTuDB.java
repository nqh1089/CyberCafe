package ViewC.Code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class C1_LayTenMayTuDB {

    public String LayTenMayTuDB(int idMay) {
        String name = "KHÔNG RÕ";
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ten_db", "user", "pass"
            );
            String sql = "SELECT NameComputer FROM Computer WHERE IDComputer = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idMay);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                name = rs.getString("NameComputer");
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }
}
