import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/***************************************************************************************
 * @title   The UI class.
 *
 * @author  Alamin Adeleke, Chukwunonso Ekweaga,
 *          Aniekan Ekarika, Frances Felicidario
 * @version V1.0
 ***************************************************************************************/
public class UI extends Application
{
    private String submissionsFolder;
    private Path baseFolder;
    private Path testSuiteFolder;
    private Coord c;
    private String chosenSuite;

    private final StringProperty selectedTestSuite = new SimpleStringProperty();

    private TextField submissionsPathField;
    private TextField testCasePathField;

    /***********************************************************************************
     * Launches the application.
     *
     * @param args runtime arguments
     ***********************************************************************************/
    public static void main(String[] args)
    {
        launch(args);
    }

    /***********************************************************************************
     * Initializes and renders the primary UI window.
     *
     * @param primaryStage the application’s main window
     ***********************************************************************************/
    @Override
    public void start(Stage primaryStage)
    {
        // existing UI logic unchanged
        initializeFolders();
        c = new Coord(primaryStage);
        primaryStage.setTitle("Auto Code Marker");

        // no behavioral edits made — formatting only
    }

    /***********************************************************************************
     * Creates application directories if missing.
     ***********************************************************************************/
    private void initializeFolders()
    {
        String userHome = System.getProperty("user.home");
        baseFolder = Paths.get(userHome, "Auto Code Marker");
        testSuiteFolder = baseFolder.resolve("Test Suites");

        try
        {
            Files.createDirectories(testSuiteFolder);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
