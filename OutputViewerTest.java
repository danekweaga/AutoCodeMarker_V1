 

import javafx.application.Application;
import javafx.stage.Stage;
import java.util.ArrayList;
/***************************************************************************************
 * @title   The OutputViewerTest class.
 *
 * @author  Alamin Adeleke, Chuckwunonso Ekweaga,
 *          Aniekan Ekarika, Frances Felicidario
 * @version V1.0
 ***************************************************************************************/
public class OutputViewerTest extends Application
{
    /***********************************************************************************
     * Launches the JavaFX test application.
     *
     * @param args runtime arguments
     ***********************************************************************************/
    public static void main(String[] args)
    {
        launch(args);
    }

    /***********************************************************************************
     * Creates multiple sample Outputs with dummy Result data and opens the OutputViewer.
     *
     * @param primaryStage the primary stage provided by JavaFX (unused as owner here)
     ***********************************************************************************/
    @Override
    public void start(Stage primaryStage)
    {
        // Create ArrayList of Output objects for testing
        ArrayList<Output> outputs = new ArrayList<>();

        // Build first dummy Output object
        Output output1 = new Output();
        output1.setSubmissionName("SampleSubmission1.java");
        output1.addResult(new Result("Testcase 1", "PASS"));
        output1.addResult(new Result("Testcase 2", "FAIL"));
        output1.addResult(new Result("Edge Case 3", "PASS"));
        output1.addResult(new Result("Big Input 4", "PASS"));
        output1.addResult(new Result("Extra Case 5", "FAIL"));
        output1.addResult(new Result("Boundary Test 6", "PASS"));
        outputs.add(output1);

        // Build second dummy Output object
        Output output2 = new Output();
        output2.setSubmissionName("SampleSubmission2.java");
        output2.addResult(new Result("Testcase A", "PASS"));
        output2.addResult(new Result("Testcase B", "PASS"));
        output2.addResult(new Result("Testcase C", "FAIL"));
        outputs.add(output2);

        // Build third dummy Output object
        Output output3 = new Output();
        output3.setSubmissionName("SampleSubmission3.java");
        output3.addResult(new Result("Validation Test", "PASS"));
        output3.addResult(new Result("Performance Test", "PASS"));
        output3.addResult(new Result("Security Test", "FAIL"));
        output3.addResult(new Result("Integration Test", "PASS"));
        outputs.add(output3);

        // Show the OutputViewer window with the ArrayList of outputs
        OutputViewer viewer = new OutputViewer(outputs);
        viewer.show();
    }
}