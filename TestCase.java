/**
 * Write a description of class TestCase here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class TestCase
{
    String input;
    String expectedOutput;
    
    public TestCase(String input, String expectedOutput)
    {
        this.input = input;
        this.expectedOutput = expectedOutput;
    }
    
    public String getInput()
    {
        return input;
    }
    
    public String getExpectedOutput()
    {
        return expectedOutput;
    }
    
    public void setInput(String input)
    {
        this.input = input;
    }
    
    public void setExpectedOutput(String expectedOutput)
    {
        this.expectedOutput = expectedOutput;
    }
}