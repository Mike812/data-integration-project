package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TestDatabaseConnection {

    // volatile variables provide visibility among threads
    private static volatile TestDatabaseConnection testDatabaseInstance;
    private static volatile Connection postgresConnection;
    private static final String DATABASE = "test_company";

    private TestDatabaseConnection() {
        try{
            Properties properties = PostgreSqlUtils.getPostgresProps();
            String jdbcUrl = properties.getProperty("url") + DATABASE;
            postgresConnection = DriverManager.getConnection(jdbcUrl, properties.getProperty("user.name"), properties.getProperty("password"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getPostgresConnection() {
        if(postgresConnection == null){
            testDatabaseInstance = new TestDatabaseConnection();
        }

        return postgresConnection;
    }

    public void closeConnection() throws SQLException {
        postgresConnection.close();
        postgresConnection = null;
    }

    public static String getDatabaseName(){
        return DATABASE;
    }
}
