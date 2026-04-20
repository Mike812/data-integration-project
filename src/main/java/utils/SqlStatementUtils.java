package utils;

public class SqlStatementUtils {
    public static String maxId = "max_id";

    public static String fieldSeparatorInt = ", ";
    public static String backslashString = "\'";
    public static String fieldSeparatorString = fieldSeparatorInt + backslashString;
    public static String fieldSeparatorStringInt = backslashString + fieldSeparatorInt;
    public static String fieldSeparatorTwoStrings = backslashString + fieldSeparatorInt + backslashString;

    public static String getSelectAllFromTableCmd(String table){
        return "SELECT * FROM " + table;
    }

    public static String getTruncateTableCmd(String table){
        return "TRUNCATE " + table;
    }

    public static String getDropTableCmd(String table){ return "DROP TABLE IF EXISTS " + table; }

    public static String getMaxIdFromTableCmd(String table, String idField){
        return "SELECT max(\"" + idField + "\") as " + maxId + " FROM " + table;
    }

    public static String getCreateDatabaseCmd(String database){
        return "CREATE DATABASE " + database;
    }

    public static String getDropDatabaseCmd(String database){
        return "DROP DATABASE IF EXISTS " + database;
    }
}
