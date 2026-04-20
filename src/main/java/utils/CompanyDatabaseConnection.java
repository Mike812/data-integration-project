package utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class CompanyDatabaseConnection {

    // volatile variables provide visibility among threads
    private static volatile CompanyDatabaseConnection companyDatabaseInstance;
    private static volatile Connection postgresConnection;
    private String jdbcUrl;
    private String userName;
    private String password;
    private static final String DATABASE = "company";

    private CompanyDatabaseConnection() {
        try{
            Properties properties = new Properties();
            InputStream input = PostgreSqlUtils.class.getClassLoader().getResourceAsStream("properties/db-config.properties");
            properties.load(input);
            this.jdbcUrl = properties.getProperty("url") + DATABASE;
            this.userName = properties.getProperty("user.name");
            this.password = properties.getProperty("password");
            postgresConnection = DriverManager.getConnection(jdbcUrl, userName, password);
        } catch (IOException e){
            System.out.println("Read database config from properties file failed.");
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getPostgresConnection() {
        if(postgresConnection == null){
            companyDatabaseInstance = new CompanyDatabaseConnection();
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
