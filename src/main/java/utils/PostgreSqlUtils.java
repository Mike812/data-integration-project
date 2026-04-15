package utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Class to create a connection to a postgreSql database
 */
public class PostgreSqlUtils {

    private String jdbcUrl;
    private String userName;
    private String password;

    public PostgreSqlUtils(String database) {
        try{
            Properties properties = new Properties();
            InputStream input = PostgreSqlUtils.class.getClassLoader()
                    .getResourceAsStream("properties/db-config.properties");
            properties.load(input);
            this.jdbcUrl = properties.getProperty("url") + database;
            this.userName = properties.getProperty("username");
            this.password = properties.getProperty("password");
        } catch (IOException e){
            System.out.println("Read database config from properties file failed.");
            e.printStackTrace();
        }
    }

    public Connection getPostgreSqlConnection() throws SQLException {
        return DriverManager.getConnection(this.jdbcUrl, this.userName, this.password);
    }

    public Statement getSqlStatement(Connection postrgresqlConnection) throws SQLException {
        return postrgresqlConnection.createStatement();
    }
}
