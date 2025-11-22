/***************************************************************************************
 * @title   The Result class.
 *
 * @author  Alamin Adeleke, Chukwunonso Ekweaga,
 *          Aniekan Ekarika, Frances Felicidario
 * @version V1.0
 ***************************************************************************************/
public class Result
{
    // Instance data
    private String testCaseName;
    private String result;

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
