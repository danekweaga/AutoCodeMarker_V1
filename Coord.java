 

import javafx.stage.Stage;
import javafx.stage.Modality;
/***************************************************************************************
 * @title   The Coord class.
 *
 * @author  Alamin Adeleke, Chukwunonso Ekweaga,
 *          Aniekan Ekarika, Frances Felicidario
 * @version V1.0
 ***************************************************************************************/
public class Coord
{
    private final Stage owner;

    private TestSuiteManager suiteManager;
    private TestCaseManager caseManager;

    /***********************************************************************************
     * Constructor to initialize the coordinator with the main application window.
     *
     * @param owner the primary Stage owning all sub-windows
     ***********************************************************************************/
    public Coord(Stage owner)
    {
        this.owner = owner;
    }

    /***********************************************************************************
     * Method to trigger execution of test cases using provided folder paths.
     *
     * @param submissionFolder the folder containing student submissions
     * @param testSuiteFolder  the folder containing the selected test suite
     ***********************************************************************************/
    public void runTests(/*String submissionFolder, String testSuiteFolder*/)
    {
        System.out.println("Run test button clicked");
    }

    /***********************************************************************************
     * Opens or brings forward the Test Suite Manager window.
     ***********************************************************************************/
    public void manageTestSuites()
    {
        if (suiteManager == null || !suiteManager.isShowing())
        {
            suiteManager = new TestSuiteManager();
            suiteManager.initOwner(owner);
            suiteManager.initModality(Modality.NONE);
            suiteManager.setOnCloseRequest(e -> suiteManager = null);
            suiteManager.show();
        }
        else
        {
            suiteManager.toFront();
        }
    }

    /***********************************************************************************
     * Opens or brings forward the Test Case Manager window.
     ***********************************************************************************/
    public void manageTestCases()
    {
        if (caseManager == null || !caseManager.isShowing())
        {
            caseManager = new TestCaseManager();
            caseManager.initOwner(owner);
            caseManager.initModality(Modality.NONE);
            caseManager.setOnCloseRequest(e -> caseManager = null);
            caseManager.show();
        }
        else
        {
            caseManager.toFront();
        }
    }
}
