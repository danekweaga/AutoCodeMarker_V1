/**
 * Write a description of class TestCase here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import javafx.stage.Stage;
import javafx.stage.Modality;

public class Coord {
    private final Stage owner;

    private TestSuiteManager suiteManager;
    private TestCaseManager caseManager;

    public Coord(Stage owner) {
        this.owner = owner;
    }

    // run tests
    public void runTests() {
        System.out.println("Run test button clicked");
    }

    // manage test suites
    public void manageTestSuites() {
        if (suiteManager == null || !suiteManager.isShowing()) {
            suiteManager = new TestSuiteManager();
            suiteManager.initOwner(owner);
            suiteManager.initModality(Modality.NONE);
            suiteManager.setOnCloseRequest(e -> suiteManager = null); // reset on close
            suiteManager.show();
        } else {
            suiteManager.toFront();
        }
    }

    // manage test cases
    public void manageTestCases() {
        if (caseManager == null || !caseManager.isShowing()) {
            caseManager = new TestCaseManager();
            caseManager.initOwner(owner);
            caseManager.initModality(Modality.NONE);
            caseManager.setOnCloseRequest(e -> caseManager = null); // reset on close
            caseManager.show();
        } else {
            caseManager.toFront();
        }
    }
}
