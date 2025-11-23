package app.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/ltm?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&sessionVariables=sql_mode=STRICT_TRANS_TABLES";
    private static final String USER = "root"; // change as needed
    private static final String PASSWORD = ""; // change as needed

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
    }
}