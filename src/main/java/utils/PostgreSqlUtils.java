package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class to create a connection to a postgreSql database
 */
public class PostgreSqlUtils {

    public static Properties getPostgresProps() {
        Properties properties = new Properties();
        try{
            InputStream input = PostgreSqlUtils.class.getClassLoader()
                    .getResourceAsStream("properties/db-config.properties");
            properties.load(input);
        } catch (IOException e){
            System.out.println("Read database config from properties file failed");
            e.printStackTrace();
        }

        return properties;
    }
}
