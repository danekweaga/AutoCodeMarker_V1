
/**
 * Test Suite containing multiple TestCase objects. This stores and manages set of test cases belonging to this suite.
 *
 * @author Frances Felicidario, Alamin Adeleke, Aniekan Ekarika, Chukwunonso Ekweaga
 * @version 11-22-25
 */
public class TestSuite 
{
    private String name;
    private final List<TestCase> testCases = new ArrayList<>();

    public TestSuite(String name) 
    {
        this.name = name;
    }

    public void addTestCase(TestCase testCase) 
    {
        testCases.add(testCase);
    }

    public List<TestCase> getTestCases() 
    {
        return Collections.unmodifiableList(testCases);
    }

    public void executeOnProgram(Program program) throws IOException, InterruptedException 
    {
        for (TestCase tc : testCases) 
        {
            String output = program.runWithInput(tc.getInput());
            tc.setActualOutput(output);
            tc.setPassed(tc.getExpectedOutput().trim().equals(output.trim()));
        }
    }
}

