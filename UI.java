import javafx.application.Application;
import javafx.stage.Stage;
import java.awt.TextField;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/***************************************************************************************
 * Write a description of class UI here.
 *
 * @author Alamin Adeleke, Chuckwunonso Ekweaga, Aniekan Ekarika, Frances Felicidario
 * @version 1
 ***************************************************************************************/
public class UI extends Application
{
    private String submissionsFolder;
    private Path baseFolder;
    private Path testSuiteFolder;

    private TextField submissionsPathField;
    private TextField testCasePathField;
    
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) 
    {
        // ---- Initialize folders ----
        initializeFolders();
    }
    
    //Method to create app folder
    private void initializeFolders() 
    {
        //get the users folder name
        String userHome = System.getProperty("user.home");
        //Create the App folder
        baseFolder = Paths.get(userHome, "Auto Code Marker");
        testSuiteFolder = baseFolder.resolve("Test Suite");

        //Make test suite subfolder
        try 
        {
            Files.createDirectories(testSuiteFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // ----------------- Empty Handlers (to implement later) -----------------

    private void manageTestCases() 
    {
        // TODO: implement Manage TestCases logic
        System.out.println("Manage TestCases clicked.");
    }

    private void manageTestSuite() 
    {
        // TODO: implement Manage TestSuite logic
        System.out.println("Manage TestSuite clicked.");
    }

    private void runTestCases() 
    {
        // TODO: implement Run Test Cases logic
        System.out.println("Run Test Cases clicked.");
    }

}