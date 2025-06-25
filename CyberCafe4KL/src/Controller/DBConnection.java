package Controller;

import java.sql.*;

public class DBConnection {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=PolyCafe4KL;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "1234";

    public static Connection connect() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Lỗi kết nối: " + e.getMessage());
            return null;
        }
    }
}

