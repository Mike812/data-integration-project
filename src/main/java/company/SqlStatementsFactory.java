package company;

import utils.PostgreSqlUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class SqlStatementsFactory {

    public static SqlStatements getSqlStatementsObject(String database, Logger logger) throws SQLException {
        PostgreSqlUtils postgreSqlConnection = new PostgreSqlUtils(database);
        Connection connection = postgreSqlConnection.getPostgreSqlConnection();
        Statement statement = postgreSqlConnection.getSqlStatement(connection);
        SqlStatements sqlStatements = new SqlStatements(connection, statement, database, logger);

        return sqlStatements;
    }
}
