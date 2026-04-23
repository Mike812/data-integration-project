package company;

import utils.InputOutputUtils;

import java.util.List;

public class DataFactory {

    private static volatile DataFactory instance;
    private List<String> firstNames;
    private List<String> lastNames;
    private List<String> stateNames;
    private List<String> stateCodes;
    private List<String> companyNames;

    private DataFactory(){
        List<String> usStatesInfo = InputOutputUtils.getUsStatesInfoFromFile();
        this.firstNames = InputOutputUtils.getFirstNamesFromFile();
        this.lastNames = InputOutputUtils.getLastNamesFromFile();
        this.stateNames = InputOutputUtils.getUsStateNames(usStatesInfo);
        this.stateCodes = InputOutputUtils.getUsStateTwoLetterCodes(usStatesInfo);
        this.companyNames = InputOutputUtils.getCompanyNamesFromFile();
    }

    public static DataFactory getInstance(){
        if(instance == null){
            instance = new DataFactory();
        }
        return instance;
    }

    public List<String> getFirstNames(){
        return firstNames;
    }

    public List<String> getLastNames(){
        return lastNames;
    }

    public List<String> getStateNames(){
        return stateNames;
    }

    public List<String> getStateCodes(){
        return stateCodes;
    }

    public List<String> getCompanyNames(){
        return companyNames;
    }
}
