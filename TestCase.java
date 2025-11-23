/***************************************************************************************
 * @title   The TestCase class.
 *
 * @author  Frances Felicidario
 * @version V1.0
 ***************************************************************************************/
public class TestCase
{
    // Instance data
    private String name;
    private String input;
    private String output;
    private int index;

    /***********************************************************************************
     * Constructs a TestCase with the given values.
     *
     * @param name   the name/label of the test case
     * @param input  the input string for the program
     * @param output the expected output string
     * @param index  the index/order of this test case
     ***********************************************************************************/
    public TestCase(String name, String input, String output, int index)
    {
        this.name = name;
        this.input = input;
        this.output = output;
        this.index = index;
    }

    /***********************************************************************************
     * Gets the test case name.
     *
     * @return the name
     ***********************************************************************************/
    public String getName()
    {
        return name;
    }

    /***********************************************************************************
     * Sets the test case name.
     *
     * @param name the new name
     ***********************************************************************************/
    public void setName(String name)
    {
        this.name = name;
    }

    /***********************************************************************************
     * Gets the test case input.
     *
     * @return the input
     ***********************************************************************************/
    public String getInput()
    {
        return input;
    }

    /***********************************************************************************
     * Sets the test case input.
     *
     * @param input the new input
     ***********************************************************************************/
    public void setInput(String input)
    {
        this.input = input;
    }

    /***********************************************************************************
     * Gets the expected output.
     *
     * @return the output
     ***********************************************************************************/
    public String getOutput()
    {
        return output;
    }

    /***********************************************************************************
     * Sets the expected output.
     *
     * @param output the new output
     ***********************************************************************************/
    public void setOutput(String output)
    {
        this.output = output;
    }

    /***********************************************************************************
     * Gets the index of this test case.
     *
     * @return the index
     ***********************************************************************************/
    public int getIndex()
    {
        return index;
    }

    /***********************************************************************************
     * Sets the index of this test case.
     *
     * @param index the new index
     ***********************************************************************************/
    public void setIndex(int index)
    {
        this.index = index;
    }
}
