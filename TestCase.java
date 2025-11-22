/***************************************************************************************
 * @title   The TestCase class.
 *
 * @author  Alamin Adeleke, Chukwunonso Ekweaga,
 *          Aniekan Ekarika, Frances Felicidario
 * @version Final Version
 ***************************************************************************************/

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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

    /***********************************************************************************
     * Compares the expected output with the provided actual output (trimmed).
     *
     * @param actual the actual output
     * @return true if they match; false otherwise
     ***********************************************************************************/
    public boolean matches(String actual)
    {
        return expectedOutput != null
               && actual != null
               && expectedOutput.trim().equals(actual.trim());
    }

    /***********************************************************************************
     * Factory method to build a TestCase from a simple text file.
     *
     * Assumed format:
     *   line 1: test case name
     *   line 2: input
     *   line 3: expected output
     *
     * @param filePath the path to the test case file
     * @return a new TestCase object
     * @throws IOException if reading the file fails
     ***********************************************************************************/
    public static TestCase fromFile(Path filePath) throws IOException
    {
        List<String> lines = Files.readAllLines(filePath);

        String fileName =
            filePath.getFileName() != null ? filePath.getFileName().toString() : "TestCase";

        String nameLine =
            (lines.size() > 0 && !lines.get(0).trim().isEmpty())
                ? lines.get(0).trim()
                : fileName;

        String inputLine =
            (lines.size() > 1) ? lines.get(1) : "";

        String expectedLine =
            (lines.size() > 2) ? lines.get(2) : "";

        return new TestCase(nameLine, inputLine, expectedLine);
    }
}
