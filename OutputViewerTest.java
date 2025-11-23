 

import javafx.application.Application;
import javafx.stage.Stage;
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
     * Creates a sample Output with dummy Result data and opens the OutputViewer.
     *
     * @param primaryStage the primary stage provided by JavaFX (unused as owner here)
     ***********************************************************************************/
    @Override
    public void start(Stage primaryStage)
    {
        // Build a dummy Output object for testing
        Output output = new Output();
        output.setSubmissionName("SampleSubmission.java");

        output.addResult(new Result("Testcase 1", "PASS"));
        output.addResult(new Result("Testcase 2", "FAIL"));
        output.addResult(new Result("Edge Case 3", "PASS"));
        output.addResult(new Result("Big Input 4", "PASS"));
        output.addResult(new Result("Extra Case 5", "FAIL"));

        // Show the OutputViewer window
        OutputViewer viewer = new OutputViewer(output);
        viewer.show();
    }
}
