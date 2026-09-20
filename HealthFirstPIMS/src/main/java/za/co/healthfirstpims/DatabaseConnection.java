package za.co.healthfirstpims;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/healthfirst_pharmacy"
            + "?useSSL=false"
            + "&allowPublicKeyRetrieval=true"
            + "&serverTimezone=Africa/Johannesburg";

    private static final String USERNAME = "root";
    private static final String PASSWORD_VARIABLE =
            "HEALTHFIRST_DB_PASSWORD";

    private DatabaseConnection() {
        // Prevent this utility class from being instantiated.
    }

    public static Connection getConnection() throws SQLException {
        String password = System.getenv(PASSWORD_VARIABLE);

        if (password == null || password.isBlank()) {
            throw new SQLException(
                    "The HEALTHFIRST_DB_PASSWORD environment variable is missing."
            );
        }

        return DriverManager.getConnection(URL, USERNAME, password);
    }

    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            System.out.println(
                    "SUCCESS: HealthFirst Pharmacy connected to MySQL."
            );
            System.out.println(
                    "Database: " + connection.getCatalog()
            );
        } catch (SQLException exception) {
            System.err.println(
                    "DATABASE CONNECTION FAILED: "
                    + exception.getMessage()
            );
        }
    }
}