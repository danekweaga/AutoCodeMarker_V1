
/**
 * @title    Test Suite containing multiple TestCase objects. This stores and manages set of test cases belonging to this suite.
 *
 * @author   Frances Felicidario, Alamin Adeleke, Aniekan Ekarika, Chukwunonso Ekweaga
 *
 * @version 11-22-25
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestSuite
{
    // Instance data
    private String name;
    private final List<TestCase> testCases = new ArrayList<>();

    /***********************************************************************************
     * Constructs a TestSuite with the given name.
     *
     * @param name the name of this test suite
     ***********************************************************************************/
    public TestSuite(String name)
    {
        this.name = name;
    }

    /***********************************************************************************
     * Gets the name of this TestSuite.
     *
     * @return the test suite name
     ***********************************************************************************/
    public String getName()
    {
        return name;
    }

    /***********************************************************************************
     * Sets the name of this TestSuite.
     *
     * @param name the new suite name
     ***********************************************************************************/
    public void setName(String name)
    {
        this.name = name;
    }

    /***********************************************************************************
     * Adds a TestCase to this TestSuite.
     *
     * @param testCase the TestCase to add
     ***********************************************************************************/
    public void addTestCase(TestCase testCase)
    {
        testCases.add(testCase);
    }

    /***********************************************************************************
     * Removes all TestCase objects from this TestSuite.
     ***********************************************************************************/
    public void clearTestCases()
    {
        testCases.clear();
    }

    /***********************************************************************************
     * Returns an unmodifiable view of the test cases in this suite.
     *
     * @return list of TestCase objects
     ***********************************************************************************/
    public List<TestCase> getTestCases()
    {
        return Collections.unmodifiableList(testCases);
    }

    /***********************************************************************************
     * Executes all test cases in this suite on the given Program.
     *
     * Each TestCase's actualOutput and passed fields are updated based on the
     * Program's output.
     *
     * @param program the Program to execute these test cases on
     * @throws IOException          if compilation or execution fails
     * @throws InterruptedException if the execution is interrupted
     ***********************************************************************************/
    public void executeOnProgram(Program program)
        throws IOException, InterruptedException
    {
        for (TestCase tc : testCases)
        {
            String output = program.runWithInput(tc.getInput());
            tc.setActualOutput(output);
            tc.setPassed(tc.getExpectedOutput().trim().equals(output.trim()));
        }
    }
}

