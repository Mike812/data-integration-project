package utils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Class with utility functions to load / write files from / to the file system.
 */
public class InputOutputUtils {

    private static String pathToFirstNamesFile = "text/first_names.txt";
    private static String pathToLastNamesFile = "text/last_names.txt";
    private static String pathToUsStatesFile = "text/us_states.txt";
    private static String pathToCompanyNamesFile = "text/company_names.txt";

    public static String getCurrentTimestamp(String format){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

        return formatter.format(now);
    }

    /**
     * Create timestamps in the past relative to the number of events that should be created
     * @param format
     * @param plusHours
     * @param numberOfEvents
     * @return
     */
    public static String getTimestampForSampleData(String format, int plusHours, int numberOfEvents){
        int minusTwoYears = 365 * 2;
        int minusDays = 0;
        for(int i=10000; i<numberOfEvents;i+=10000){
            minusDays += minusTwoYears;
        }
        LocalDateTime now = LocalDateTime.now().minusDays(minusDays).plusHours(plusHours);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

        return formatter.format(now);
    }

    public static List<String> getLinesFromFile(String pathToFile) {
        List<String> lines = new ArrayList<>();
        try{
            InputStream inputStream = InputOutputUtils.class.getClassLoader().getResourceAsStream(pathToFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null){
                lines.add(line);
            }
        } catch (IOException e){
            e.printStackTrace();
        }


        return lines;
    }

    public static List<String> getFirstNamesFromFile(){
        return InputOutputUtils.getLinesFromFile(pathToFirstNamesFile);
    }

    public static List<String> getLastNamesFromFile(){
        return InputOutputUtils.getLinesFromFile(pathToLastNamesFile);
    }

    public static List<String> getUsStatesInfoFromFile(){
        return InputOutputUtils.getLinesFromFile(pathToUsStatesFile);
    }

    public static List<String> getUsStateTwoLetterCodesFromFile(){
        List<String> lines = getUsStatesInfoFromFile();
        List<String> twoLetterCodes = new ArrayList<>();
        for(String line : lines){
            String[] splittedLine = line.split("\\t");
            twoLetterCodes.add(splittedLine[splittedLine.length-1]);
        }

        return twoLetterCodes;
    }

    public static List<String> getCompanyNamesFromFile(){
        return getLinesFromFile(pathToCompanyNamesFile);
    }

}
