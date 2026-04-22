package utils;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class InputOutputUtilsTest {

    List<String> usStateLines = InputOutputUtils.getUsStatesInfoFromFile();

    @Test
    public void testGetLinesFromFile(){
        assertEquals(54, usStateLines.size());
        String[] arizonaInfo = usStateLines.get(2).split("\t");
        assertEquals("Arizona", arizonaInfo[0]);
        assertEquals("Ariz.", arizonaInfo[1]);
        assertEquals("AZ", arizonaInfo[2]);

        List<String> firstNameLines = InputOutputUtils.getFirstNamesFromFile();
        assertEquals(200, firstNameLines.size());

        List<String> lastNameLines = InputOutputUtils.getLastNamesFromFile();
        assertEquals(997, lastNameLines.size());

        List<String> companyNameLines = InputOutputUtils.getCompanyNamesFromFile();
        assertEquals(50, companyNameLines.size());
    }

    @Test
    public void testGetUsStateNames(){
        List<String> usStateNames = InputOutputUtils.getUsStateNames(usStateLines);
        assertEquals(54, usStateNames.size());
        assertEquals("Arizona", usStateNames.get(2));
    }

    @Test
    public void testGetUsStateTwoLetterCodes(){
        List<String> usStateCodes = InputOutputUtils.getUsStateTwoLetterCodes(usStateLines);
        assertEquals(54, usStateCodes.size());
        assertEquals("AZ", usStateCodes.get(2));
    }
}
