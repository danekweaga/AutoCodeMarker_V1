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
 * Write a description of class UI here.
 *
 * @author Alamin Adeleke, Frances Felicidario
 * 
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

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        // ---- Initialize folders ----
        initializeFolders();

        // ---- Initialize Coordinator ----
        c = new Coord(primaryStage);

        primaryStage.setTitle("Auto Code Marker");

        // ---- Title Label ----
        Label titleLabel = new Label("Auto Code Marker");
        titleLabel.setFont(Font.font("Consolas", 38));
        titleLabel.setTextFill(Color.web("#00BFFF")); // blue
        titleLabel.setStyle("-fx-font-weight: bold;");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(Pos.CENTER);

        // ---- Select Submission Folder ----
        Label submissionsLabel = new Label("Select Submission Folder:");
        submissionsPathField = new TextField();
        submissionsPathField.setEditable(false);
        submissionsPathField.setPrefWidth(350);

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

        HBox submissionsBox =
            new HBox(10, submissionsLabel, submissionsPathField, chooseSubmissionsButton);
        submissionsBox.setAlignment(Pos.CENTER_LEFT);

        // --- Test Suite Dropdown Section ---
        Label testSuiteLabel = new Label("Choose Test Suite");

        // ComboBox for listing test suite folders
        ComboBox<String> testSuiteDropdown = new ComboBox<>();
        testSuiteDropdown.setPromptText("Select a test suite");

        HBox testSuitBox = new HBox(10, testSuiteLabel, testSuiteDropdown);
        testSuitBox.setAlignment(Pos.CENTER_LEFT);

        // Folder where test suite names are stored
        File testSuitesDir =
            new File(System.getProperty("user.home") + "\\Auto Code Marker\\Test Suites");

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
            System.out.println("Selected Test Suite: " + selectedTestSuite.get());
        });

        // ---- Add Test Case (copy file into selected test suite) ----
        Label testCaseLabel = new Label("Add Test Case File to Selected Suite:");
        testCasePathField = new TextField();
        testCasePathField.setEditable(false);
        testCasePathField.setPrefWidth(350);

        Button addTestCaseButton = new Button("Browse File");
        addTestCaseButton.setOnAction(e ->
        {
            if (selectedTestSuite.get() == null || selectedTestSuite.get().isEmpty())
            {
                showError(primaryStage,
                          "Please select a test suite before adding a test case file.");
                return;
            }

            FileChooser fc = new FileChooser();
            fc.setTitle("Select Test Case File");
            File file = fc.showOpenDialog(primaryStage);
            if (file != null)
            {
                testCasePathField.setText(file.getAbsolutePath());

                // Copy into selected test suite folder
                String userHome = System.getProperty("user.home");
                Path destFolder = Paths.get(userHome,
                                            "Auto Code Marker",
                                            "Test Suites",
                                            selectedTestSuite.get());
                try
                {
                    Files.createDirectories(destFolder);
                    Path destFile = destFolder.resolve(file.getName());
                    Files.copy(file.toPath(),
                               destFile,
                               StandardCopyOption.REPLACE_EXISTING);
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                    showError(primaryStage,
                              "Error copying test case file: " + ex.getMessage());
                }
            }
        });

        HBox testCaseBox =
            new HBox(10, testCaseLabel, testCasePathField, addTestCaseButton);
        testCaseBox.setAlignment(Pos.CENTER_LEFT);

        // ---- Manage TestCases & Manage TestSuite (stylized rectangles) ----
        StackPane manageTestCasesButton =
            createRectButton("Manage TestCases", Color.LIGHTBLUE, this::manageTestCases);
        StackPane manageTestSuiteButton =
            createRectButton("Manage TestSuite", Color.LIGHTBLUE, this::manageTestSuite);

        HBox manageBox = new HBox(15, manageTestCasesButton, manageTestSuiteButton);
        manageBox.setAlignment(Pos.CENTER_LEFT);

        // ---- Run Test Cases button (stylized rectangle) ----
        StackPane runTestCasesButton =
            createRectButton("Run Test Cases", Color.LIGHTGREEN, this::runTestCases);

        // ---- Layout (mostly top-to-bottom) ----
        VBox root = new VBox(22);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);
        root.getChildren().addAll(
            titleLabel,
            submissionsBox,
            testSuitBox,
            testCaseBox,
            manageBox,
            runTestCasesButton
        );

        Scene scene = new Scene(root, 600, 350);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // Method to format ui buttons
    private StackPane createRectButton(String label, Color fillColor, Runnable action)
    {
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

    // Method to create app folder
    private void initializeFolders()
    {
        // get the users folder name
        String userHome = System.getProperty("user.home");
        // Create the App folder
        baseFolder = Paths.get(userHome, "Auto Code Marker");
        testSuiteFolder = baseFolder.resolve("Test Suites");

        // Make test suite subfolder
        try
        {
            Files.createDirectories(testSuiteFolder);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // ----------------- Handlers -----------------

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
        if (submissionsFolder == null || submissionsFolder.isEmpty())
        {
            showError(null, "Please select a submission folder first.");
            return;
        }

        if (selectedTestSuite.get() == null || selectedTestSuite.get().isEmpty())
        {
            showError(null, "Please select a test suite.");
            return;
        }

        String userHome = System.getProperty("user.home");
        Path suiteFolder = Paths.get(userHome,
                                     "Auto Code Marker",
                                     "Test Suites",
                                     selectedTestSuite.get());

        c.runTests(submissionsFolder, suiteFolder.toString());
    }

    /***********************************************************************************
     * Shows an error alert with the given message.
     *
     * @param owner   the owner Stage (may be null)
     * @param message the message to display
     ***********************************************************************************/
    private void showError(Stage owner, String message)
    {
        javafx.scene.control.Alert alert =
            new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR,
                message,
                javafx.scene.control.ButtonType.OK);
        if (owner != null)
        {
            alert.initOwner(owner);
        }
        alert.showAndWait();
    }
}
