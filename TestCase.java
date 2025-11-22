/***************************************************************************************
 * @title   The TestCase class.
 *
 * @author  Alamin Adeleke, Chuckwunonso Ekweaga,
 *          Aniekan Ekarika, Frances Felicidario
 * @version Final Version
 ***************************************************************************************/
public class TestCase
{
    // Instance data
    private String name;
    private String input;
    private String expectedOutput;

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
}
