import java.util.ArrayList;
/**
 * Test Suite containing multiple TestCase objects. This stores and manages set of test cases belonging to this suite.
 *
 * @author Frances Felicidario, Alamin Adeleke, Aniekan Ekarika, Chukwunonso Ekweaga
 * @version 11-22-25
 */
public class TestSuite 
{
    public ArrayList<TestCase> testSuite;
    public TestSuite()
    {
        testSuite = new ArrayList<TestCase>();
    }
    
    public void add(TestCase tc)
    {
        testSuite.add(tc);
    }
}

