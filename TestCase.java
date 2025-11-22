/***************************************************************************************
 * @title   The TestCase class.
 *
 * @author  Alamin Adeleke, Chukwunonso Ekweaga,
 *          Aniekan Ekarika, Frances Felicidario
 * @version Final Version
 ***************************************************************************************/
public class TestCase
{
    // Instance data
    private String name;
    private String input;
    private String expectedOutput;

    // Execution result data
    private String actualOutput;
    private boolean passed;

    /***********************************************************************************
     * Constructor to initialize a test case with its name, input, and expected output.
     *
     * @param name            the test case label
     * @param input           the input provided to the student's program
     * @param expectedOutput  the expected result for comparison
     ***********************************************************************************/
    public TestCase(String name, String input, String expectedOutput)
    {
        this.name = name;
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    /***********************************************************************************
     * Gets the label/name of this test case.
     *
     * @return the input string
     ***********************************************************************************/
    public String getName()
    {
        return name;
    }
    
    /***********************************************************************************
     * Gets the input value for this test case.
     *
     * @return the input string
     ***********************************************************************************/
    public String getInput()
    {
        return input;
    }

    /***********************************************************************************
     * Gets the expected output for this test case.
     *
     * @return the expected output string
     ***********************************************************************************/
    public String getExpectedOutput()
    {
        return expectedOutput;
    }

    /***********************************************************************************
     * Sets the input value for this test case.
     *
     * @param input the new input string
     ***********************************************************************************/
    public void setInput(String input)
    {
        this.input = input;
    }

    /***********************************************************************************
     * Sets the expected output value for this test case.
     *
     * @param expectedOutput the new expected output string
     ***********************************************************************************/
    public void setExpectedOutput(String expectedOutput)
    {
        this.expectedOutput = expectedOutput;
    }

    /***********************************************************************************
     * Gets the actual output produced by student's program for this test case.
     *
     * @return the actual output string, or null if not yet executed
     ***********************************************************************************/
    public String getActualOuput()
    {
        return actualOutput;
    }

     /***********************************************************************************
     * Sets the actual output prouced by student's program for this test case.
     *
     * @param actualOutput the output string returned by the program
     ***********************************************************************************/
    public void setActualOutput(String actualOutput)
    {
        this.actualOutput = actualOutput;
    }

    /***********************************************************************************
     * Checks whether this test case passed based on the last execution.
     *
     * @return true if test passed; false otherwise
     ***********************************************************************************/    
    public boolean isPassed()
    {
        return passed;
    }

    /***********************************************************************************
     * Sets the pass/fail status of this test case based on the last execution.
     *
     * @param passed true if the test passed; false otherwise
     ***********************************************************************************/
    public void setPassed(boolean passed)
    {
        this.passed = passed;
    }  
}
