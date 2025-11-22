/***************************************************************************************
 * @title   The Coord class.
 *
 * @author  Alamin Adeleke, Chukwunonso Ekweaga,
 *          Aniekan Ekarika, Frances Felicidario
 * @version V1.1
 ***************************************************************************************/
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Coord
{
    // Instance data
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
    public void runTests(String submissionFolder, String testSuiteFolder)
    {
        System.out.println("Run test button clicked");

        if (submissionFolder == null || submissionFolder.isEmpty()
            || testSuiteFolder == null || testSuiteFolder.isEmpty())
        {
            showError("Please select a submissions folder and a test suite first.");
            return;
        }

        File submissionsRoot = new File(submissionFolder);
        File suiteFolderFile = new File(testSuiteFolder);

        try
        {
            // Load programs from submissions root
            ListOfPrograms programList = new ListOfPrograms();
            programList.loadFromRootFolder(submissionsRoot);

            // Load single test suite from test suite folder
            ListOfTestSuites suiteList = new ListOfTestSuites();
            suiteList.loadSingleSuiteFromFolder(suiteFolderFile);
            TestSuite suite = suiteList.getSingleSuite();

            List<Program> programs = programList.getPrograms();
            if (programs.isEmpty())
            {
                showError("No valid submissions found under the selected folder.");
                return;
            }

            // For Version 1: open an OutputViewer window for each program
            for (Program program : programs)
            {
                // Execute test suite on this program
                suite.executeOnProgram(program);

                // Build Output object from the updated test cases
                Output output = new Output();
                output.setSubmissionName(program.getName());

                for (TestCase tc : suite.getTestCases())
                {
                    String status = tc.isPassed() ? "PASS" : "FAIL";

                    Result result = new Result(
                        tc.getName(),
                        status,
                        tc.getInput(),
                        tc.getExpectedOutput(),
                        tc.getActualOutput()
                    );

                    output.addResult(result);
                }

                // Show result window for this submission
                OutputViewer viewer = new OutputViewer(output);
                viewer.initOwner(owner);
                viewer.initModality(Modality.NONE);
                viewer.show();
            }
        }
        catch (IOException | InterruptedException ex)
        {
            ex.printStackTrace();
            showError("Error while running tests: " + ex.getMessage());
        }
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

    /***********************************************************************************
     * Shows an error alert with the given message.
     *
     * @param message the message to display
     ***********************************************************************************/
    private void showError(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.initOwner(owner);
        alert.showAndWait();
    }
}
