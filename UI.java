import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
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
import java.nio.file.StandardCopyOption;

/***************************************************************************************
 * The GUI in which the User interacts with to set Folder paths, Set Test Suites and Test Cases, etc
 *
 * @author Alamin Adeleke, Chuckwunonso Ekweaga, Aniekan Ekarika, Frances Felicidario
 * @version 1
 ***************************************************************************************/
public class UI extends Application
{
    private String submissionsFolder;
    private Path baseFolder;
    private Path testSuiteFolder;
    private Coord c;
    private String chosenSuite;
    // Variable to store selected test suite name
    private final StringProperty selectedTestSuite = new SimpleStringProperty();

    private TextField submissionsPathField;
    private TextField testCasePathField;
    ComboBox<String> testSuiteDropdown;
    
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) 
    {
        // ---- Initialize folders ----
        initializeFolders();
        
        // ---- Initialize Coordinator
        c = new Coord(primaryStage);

        primaryStage.setTitle("Auto Code Marker");

        // ---- Title Label ----
        Label titleLabel = new Label("Auto Code Marker");
        titleLabel.setFont(Font.font("Consolas", 42));
        titleLabel.setTextFill(Color.web("#00BFFF")); // blue
        titleLabel.setStyle("-fx-font-weight: bold;");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(Pos.CENTER);

        // ---- Select Submission Folder ----
        Label submissionsLabel = new Label("Select Submission Folder:");
        submissionsPathField = new TextField();
        submissionsPathField.setEditable(false);
        submissionsPathField.setPrefWidth(185);

        Button chooseSubmissionsButton = new Button("Browse");
        chooseSubmissionsButton.setOnAction(e -> 
        {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Submission Folder");
            File folder = directoryChooser.showDialog(primaryStage);
            if (folder != null) 
            {
                submissionsFolder = folder.getAbsolutePath();
                submissionsPathField.setText(submissionsFolder);
            }
        });

        HBox submissionsBox = new HBox(11, submissionsLabel, submissionsPathField, chooseSubmissionsButton);
        submissionsBox.setAlignment(Pos.CENTER_LEFT);

        // --- Test Suite Dropdown Section ---

        Label testSuiteLabel = new Label("Choose Test Suite:            ");
        
        // ComboBox for listing test suite folders
        testSuiteDropdown = new ComboBox<>();
        testSuiteDropdown.setPromptText("Select a test suite");
        testSuiteDropdown.setPrefWidth(250);
        
        HBox testSuitBox = new HBox(9, testSuiteLabel, testSuiteDropdown);
        testSuitBox.setAlignment(Pos.CENTER_LEFT);
        
        // Folder where test suite names are stored
        File testSuitesDir = new File(System.getProperty("user.home") + "\\Auto Code Marker\\Test Suites");
        
        // Load folder names into dropdown
        if (testSuitesDir.exists() && testSuitesDir.isDirectory()) 
        {
            File[] folders = testSuitesDir.listFiles(File::isDirectory);
            if (folders != null) 
            {
                for (File f : folders) 
                {
                    testSuiteDropdown.getItems().add(f.getName());
                }
            }
        }
        
        
        
        testSuiteDropdown.setOnAction(e -> 
        {
            selectedTestSuite.set(testSuiteDropdown.getValue());
        });
        testSuiteDropdown.setOnShowing(e -> refreshTestSuites());
        refreshTestSuites();

        // ---- Manage TestCases & Manage TestSuite (stylized rectangles) ----
        StackPane manageTestCasesButton = createRectButton("Manage TestCases", Color.LIGHTBLUE, this::manageTestCases);
        StackPane manageTestSuiteButton = createRectButton("Manage TestSuite", Color.LIGHTBLUE, this::manageTestSuite);

        HBox manageBox = new HBox(20, manageTestCasesButton, manageTestSuiteButton);
        manageBox.setAlignment(Pos.CENTER);

        //Run Test Cases button (stylized rectangle)
        StackPane runTestCasesButton = createRectButton("Run Test Cases", Color.LIGHTGREEN, this::runTestCases);

        //Layout (mostly top-to-bottom)
        VBox root = new VBox(22);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);
        root.getChildren().addAll(
                titleLabel,
                submissionsBox,
                testSuitBox,
                manageBox,
                runTestCasesButton
                
        );

        Scene scene = new Scene(root, 450, 300);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    //Method to format ui buttons
    private StackPane createRectButton(String label, Color fillColor, Runnable action) {
        Rectangle rect = new Rectangle(160, 40);
        rect.setFill(fillColor);
        rect.setArcWidth(20);
        rect.setArcHeight(20);

        Text text = new Text(label);
        text.setFont(Font.font("Arial", 13));
        text.setFill(Color.BLACK);

        StackPane pane = new StackPane(rect, text);

        // Hover effect
        pane.setOnMouseEntered(e -> rect.setOpacity(0.7));
        pane.setOnMouseExited(e -> rect.setOpacity(1.0));

        // Click actions on both rect & text
        pane.setOnMouseClicked(e -> 
        {
            if (action != null) 
            {
                action.run();
            }
        });
        rect.setOnMouseClicked(e -> 
        {
            if (action != null) 
            {
                action.run();
            }
        });
        text.setOnMouseClicked(e -> 
        {
            if (action != null) 
            {
                action.run();
            }
        });

        return pane;
    }
    
    //Method to create app folder
    private void initializeFolders() 
    {
        //get the users folder name
        String userHome = System.getProperty("user.home");
        //Create the App folder
        baseFolder = Paths.get(userHome, "Auto Code Marker");
        testSuiteFolder = baseFolder.resolve("Test Suites");

        //Make test suite subfolder
        try 
        {
            Files.createDirectories(testSuiteFolder);
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    private void refreshTestSuites() 
    {
        File dir = new File(System.getProperty("user.home") + "\\Auto Code Marker\\Test Suites\\");
        File[] folders = dir.listFiles(File::isDirectory);
    
        testSuiteDropdown.getItems().clear();
    
        if (folders != null) {
            for (File f : folders) {
                testSuiteDropdown.getItems().add(f.getName());
            }
        }
    }

    
    // Button Methods to interact with the coordinator Class
    private void manageTestSuite() 
    {
        c.manageTestSuites();
        chosenSuite = selectedTestSuite.get();
    }
    private void manageTestCases() 
    {
        c.manageTestCases();
    }
    private void runTestCases() 
    {
        c.runTests(submissionsPathField.getText(),testSuiteDropdown.getValue());
    }
}
