package utils;

public class SqlStatementUtils {
    public static String fieldSeparatorInt = ", ";
    public static String backslashString = "\'";
    public static String fieldSeparatorString = fieldSeparatorInt + backslashString;
    public static String fieldSeparatorStringInt = backslashString + fieldSeparatorInt;
    public static String fieldSeparatorTwoStrings = backslashString + fieldSeparatorInt + backslashString;
}
