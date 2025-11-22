/***************************************************************************************
 * @title   The Result class.
 *
 * @author  Alamin Adeleke, Chukwunonso Ekweaga,
 *          Aniekan Ekarika, Frances Felicidario
 * @version V1.1
 ***************************************************************************************/
public class Result
{
    // Instance data
    private String testCaseName;
    private String result;          // e.g., "PASS" or "FAIL"

    // Optional detail data
    private String input;
    private String expectedOutput;
    private String actualOutput;

    /***********************************************************************************
     * Default constructor for creating an empty Result object.
     ***********************************************************************************/
    public Result()
    {
    }

    /***********************************************************************************
     * Constructor to initialize a Result with its test case name and outcome.
     *
     * @param testCaseName the name of the associated test case
     * @param result       the pass/fail or output value
     ***********************************************************************************/
    public Result(String testCaseName, String result)
    {
        this.testCaseName = testCaseName;
        this.result = result;
    }

    /***********************************************************************************
     * Constructor including full detail information.
     *
     * @param testCaseName   the name of the associated test case
     * @param result         the pass/fail indicator
     * @param input          the input used for this test
     * @param expectedOutput the expected output
     * @param actualOutput   the actual output from the program
     ***********************************************************************************/
    public Result(String testCaseName,
                  String result,
                  String input,
                  String expectedOutput,
                  String actualOutput)
    {
        this.testCaseName = testCaseName;
        this.result = result;
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.actualOutput = actualOutput;
    }

    /***********************************************************************************
     * Gets the name of the test case associated with this result.
     *
     * @return the test case name
     ***********************************************************************************/
    public String getTestCaseName()
    {
        return testCaseName;
    }

    /***********************************************************************************
     * Sets the test case name for this result.
     *
     * @param testCaseName the new test case name
     ***********************************************************************************/
    public void setTestCaseName(String testCaseName)
    {
        this.testCaseName = testCaseName;
    }

    /***********************************************************************************
     * Gets the stored result value.
     *
     * @return the result value
     ***********************************************************************************/
    public String getResult()
    {
        return result;
    }

    /***********************************************************************************
     * Sets the result value.
     *
     * @param result the new result value
     ***********************************************************************************/
    public void setResult(String result)
    {
        this.result = result;
    }

    /***********************************************************************************
     * Gets the input associated with this result.
     *
     * @return the input string, or null if not tracked
     ***********************************************************************************/
    public String getInput()
    {
        return input;
    }

    /***********************************************************************************
     * Sets the input associated with this result.
     *
     * @param input the input string
     ***********************************************************************************/
    public void setInput(String input)
    {
        this.input = input;
    }

    /***********************************************************************************
     * Gets the expected output associated with this result.
     *
     * @return the expected output string, or null if not tracked
     ***********************************************************************************/
    public String getExpectedOutput()
    {
        return expectedOutput;
    }

    /***********************************************************************************
     * Sets the expected output associated with this result.
     *
     * @param expectedOutput the expected output
     ***********************************************************************************/
    public void setExpectedOutput(String expectedOutput)
    {
        this.expectedOutput = expectedOutput;
    }

    /***********************************************************************************
     * Gets the actual output associated with this result.
     *
     * @return the actual output string, or null if not tracked
     ***********************************************************************************/
    public String getActualOutput()
    {
        return actualOutput;
    }

    /***********************************************************************************
     * Sets the actual output associated with this result.
     *
     * @param actualOutput the actual output
     ***********************************************************************************/
    public void setActualOutput(String actualOutput)
    {
        this.actualOutput = actualOutput;
    }

    /***********************************************************************************
     * Returns a formatted string representation of the Result object.
     *
     * @return formatted text for debugging or display
     ***********************************************************************************/
    @Override
    public String toString()
    {
        return "Result{testCaseName='" + testCaseName
               + "', result='" + result + "'}";
    }
}
