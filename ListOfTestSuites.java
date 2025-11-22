/***************************************************************************************
 * @title   The ListOfTestSuites class.
 * @desc    This program only stores one suite, and loads the single suite's test cases
 *          from a test suite folder on disk.
 *
 * @author  Frances Felicidario
 * @version V1 11-22-25
 ***************************************************************************************/
import java.io.File;
import java.io.IOException;

public class ListOfTestSuites
{
    // Instance data
    private TestSuite singleSuite;

    /***********************************************************************************
     * Default constructor initializing a single, default TestSuite.
     ***********************************************************************************/
    public ListOfTestSuites()
    {
        singleSuite = new TestSuite("Default Test Suite");
    }

    /***********************************************************************************
     * Loads the single TestSuite from the given folder. Each file in the folder
     * is treated as a test case file and converted to a TestCase.
     *
     * @param suiteFolder folder containing test case files
     * @throws IOException if reading test case files fails
     ***********************************************************************************/
    public void loadSingleSuiteFromFolder(File suiteFolder) throws IOException
    {
        if (suiteFolder == null || !suiteFolder.exists() || !suiteFolder.isDirectory())
        {
            throw new IOException("Invalid test suite folder: " + suiteFolder);
        }

        singleSuite.setName(suiteFolder.getName());
        singleSuite.clearTestCases();

        File[] files = suiteFolder.listFiles(File::isFile);
        if (files == null)
        {
            return;
        }

        for (File file : files)
        {
            TestCase testCase = TestCase.fromFile(file.toPath());
            singleSuite.addTestCase(testCase);
        }
    }

    /***********************************************************************************
     * Returns the single stored TestSuite.
     *
     * @return the single TestSuite
     ***********************************************************************************/
    public TestSuite getSingleSuite()
    {
        return singleSuite;
    }
}
