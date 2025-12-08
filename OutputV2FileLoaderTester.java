import javafx.application.Application;
import javafx.stage.Stage;

/***************************************************************************************
 * @title   OutputV2FileLoaderTester
 *
 * @brief   Simple JavaFX tester that opens the OutputV2FileLoader window.
 *          This simulates "button pressed" in the main UI.
 ***************************************************************************************/
public class OutputV2FileLoaderTester extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("OutputV2 File Loader Tester Host");

        // Simulate: a button was pressed in the main UI
        OutputV2FileLoader loaderWindow = new OutputV2FileLoader(primaryStage);
        loaderWindow.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
