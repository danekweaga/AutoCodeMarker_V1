import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.TextField;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.GridPane;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.DirectoryStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/***************************************************************************************
 * @title   The TestCaseManager class.
 *
 * @author  Frances Felicidario
 * @version V1.3
 ***************************************************************************************/
public class TestCaseManager extends Stage
{
    // Instance data
    private final Path baseTestSuitesFolder;
    private ComboBox<String> suiteComboBox;
    private ListView<TestCase> testCaseListView;
    private ObservableList<TestCase> testCaseObservableList;

    // Name of the suite currently being edited
    private String currentSuiteName;

    /***********************************************************************************
     * Constructor. Sets up the Stage and user interface.
     ***********************************************************************************/
    public TestCaseManager()
    {
        // Base folder: <UserHome>/Auto Code Marker/Test Suites
        String userHome = System.getProperty("user.home");
        baseTestSuitesFolder = Paths.get(userHome, "Auto Code Marker", "Test Suites");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top: "Select Test Suite" (left) and ComboBox (right)
        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(5));
        topBar.setAlignment(Pos.CENTER_LEFT);

        Label selectLabel = new Label("Select Test Suite");
        selectLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        suiteComboBox = new ComboBox<>();
        suiteComboBox.setPromptText("Choose a suite");
        suiteComboBox.setMinWidth(200);

        HBox.setMargin(selectLabel, new Insets(0, 0, 0, 5));
        HBox.setMargin(suiteComboBox, new Insets(0, 5, 0, 0));

        // Push comboBox to the right
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        topBar.getChildren().addAll(selectLabel, spacer, suiteComboBox);
        root.setTop(topBar);

        // Center: left ListView, right buttons
        testCaseObservableList = FXCollections.observableArrayList();
        testCaseListView = new ListView<>(testCaseObservableList);

        VBox buttonsBox = new VBox(10);
        buttonsBox.setAlignment(Pos.TOP_CENTER);

        Button createButton = new Button("Create TC");
        Button updateButton = new Button("Update TC");
        Button deleteButton = new Button("Delete");
        Button saveButton = new Button("Save");

        createButton.setMaxWidth(Double.MAX_VALUE);
        updateButton.setMaxWidth(Double.MAX_VALUE);
        deleteButton.setMaxWidth(Double.MAX_VALUE);
        saveButton.setMaxWidth(Double.MAX_VALUE);

        buttonsBox.getChildren().addAll(createButton, updateButton, deleteButton, saveButton);
        buttonsBox.setPadding(new Insets(0, 0, 0, 10));

        HBox centerBox = new HBox(10, testCaseListView, buttonsBox);
        centerBox.setPadding(new Insets(10, 0, 0, 0));
        HBox.setHgrow(testCaseListView, javafx.scene.layout.Priority.ALWAYS);

        root.setCenter(centerBox);

        // Wire up actions
        loadTestSuiteNames();

        suiteComboBox.setOnAction(e ->
        {
            String selected = suiteComboBox.getValue();
            currentSuiteName = selected;
            loadTestCasesForSuite(selected);
        });

        createButton.setOnAction(e -> handleCreateTestCase());
        updateButton.setOnAction(e -> handleUpdateTestCase());
        deleteButton.setOnAction(e -> handleDeleteTestCase());
        saveButton.setOnAction(e -> handleSaveTestCases());

        Scene scene = new Scene(root, 700, 400);
        this.setTitle("Test Case Manager");
        this.setScene(scene);
    }

    /***********************************************************************************
     * Loads the names of test suites into the ComboBox.
     ***********************************************************************************/
    private void loadTestSuiteNames()
    {
        try
        {
            List<String> names = listTestSuiteNames();
            suiteComboBox.getItems().clear();
            suiteComboBox.getItems().addAll(names);
        }
        catch (IOException e)
        {
            showError("Error loading test suite names: " + e.getMessage());
        }
    }

    /***********************************************************************************
     * Loads all test cases for the selected suite into the ListView.
     *
     * File format (per test case file):
     *   line 1: input
     *   line 2: expected output
     *
     * Filename:
     *   <test-case-name>.txt  (name comes from TestCase.getName()).
     ***********************************************************************************/
    private void loadTestCasesForSuite(String suiteName)
    {
        testCaseObservableList.clear();
        if (suiteName == null || suiteName.isEmpty())
        {
            return;
        }

        try
        {
            List<TestCase> cases = loadTestCases(suiteName);

            // Fill the observable list with a copy of what is on disk.
            // This list is the "temporary copy" the user edits.
            testCaseObservableList.addAll(cases);
        }
        catch (IOException e)
        {
            showError("Error loading test cases: " + e.getMessage());
        }
    }

    /***********************************************************************************
     * Handles the "Create TC" action: prompts user for name, input, and output,
     * and adds a new TestCase to the current suite list (temporary copy).
     ***********************************************************************************/
    private void handleCreateTestCase()
    {
        if (!ensureSuiteSelected())
        {
            return;
        }

        Optional<TestCase> result = showTestCaseDialog(null);
        if (result.isPresent())
        {
            TestCase tc = result.get();
            // Keep index only for in-memory ordering; it is *not* saved to disk.
            tc.setIndex(testCaseObservableList.size() + 1);
            testCaseObservableList.add(tc);
        }
    }

    /***********************************************************************************
     * Handles the "Update TC" action: updates the selected test case.
     ***********************************************************************************/
    private void handleUpdateTestCase()
    {
        if (!ensureSuiteSelected())
        {
            return;
        }

        TestCase selected = testCaseListView.getSelectionModel().getSelectedItem();
        if (selected == null)
        {
            showError("Please select a test case to update.");
            return;
        }

        Optional<TestCase> result = showTestCaseDialog(selected);
        if (result.isPresent())
        {
            TestCase updated = result.get();
            selected.setName(updated.getName());
            selected.setInput(updated.getInput());
            selected.setOutput(updated.getOutput());
            testCaseListView.refresh();
        }
    }

    /***********************************************************************************
     * Handles the "Delete" action: removes the selected test case from the
     * temporary working list only.
     ***********************************************************************************/
    private void handleDeleteTestCase()
    {
        if (!ensureSuiteSelected())
        {
            return;
        }

        TestCase selected = testCaseListView.getSelectionModel().getSelectedItem();
        if (selected == null)
        {
            showError("Please select a test case to delete.");
            return;
        }

        testCaseObservableList.remove(selected);

        // Re-index remaining test cases (index is only used in memory)
        int index = 1;
        for (TestCase tc : testCaseObservableList)
        {
            tc.setIndex(index++);
        }
        testCaseListView.refresh();
    }

    /***********************************************************************************
     * Handles the "Save" action.
     *
     * Conceptually:
     *  - The ListView holds a *temporary copy* of the test cases for the suite.
     *  - When the user presses "Save", that temporary copy *replaces* the original
     *    by overwriting the .txt files on disk.
     *  - If the user closes the window or switches suites without pressing "Save",
     *    the original files remain unchanged (the temporary copy is discarded).
     ***********************************************************************************/
    private void handleSaveTestCases()
    {
        if (!ensureSuiteSelected())
        {
            return;
        }

        try
        {
            // Make a defensive copy of the current in-memory list
            List<TestCase> snapshot = new ArrayList<>(testCaseObservableList);

            // This call overwrites the on-disk test case files with our working copy.
            saveTestCases(currentSuiteName, snapshot);

            showInfo("Test cases saved successfully.");
        }
        catch (IOException e)
        {
            showError("Error saving test cases: " + e.getMessage());
        }
    }

    /***********************************************************************************
     * Ensures that a test suite has been selected. Shows an error if not.
     *
     * @return true if a suite is selected; false otherwise
     ***********************************************************************************/
    private boolean ensureSuiteSelected()
    {
        if (currentSuiteName == null || currentSuiteName.isEmpty())
        {
            showError("Please select a test suite first.");
            return false;
        }
        return true;
    }

    /***********************************************************************************
     * Shows a dialog to either create a new TestCase or edit an existing one.
     *
     * @param existing the existing TestCase, or null to create a new one
     * @return an Optional containing the created/updated TestCase if confirmed
     ***********************************************************************************/
    private Optional<TestCase> showTestCaseDialog(TestCase existing)
    {
        Dialog<TestCase> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Create Test Case" : "Update Test Case");

        ButtonType okButtonType =
            new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField inputField = new TextField();
        inputField.setPromptText("Input");

        TextField outputField = new TextField();
        outputField.setPromptText("Expected Output");

        if (existing != null)
        {
            nameField.setText(existing.getName());
            inputField.setText(existing.getInput());
            outputField.setText(existing.getOutput());
        }

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Input:"), 0, 1);
        grid.add(inputField, 1, 1);
        grid.add(new Label("Output:"), 0, 2);
        grid.add(outputField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton ->
        {
            if (dialogButton == okButtonType)
            {
                String name = nameField.getText();
                String input = inputField.getText();
                String output = outputField.getText();

                if (name == null || name.trim().isEmpty())
                {
                    return null;
                }

                int index = (existing != null) ? existing.getIndex() : 0;
                return new TestCase(name, input, output, index);
            }
            return null;
        });

        return dialog.showAndWait();
    }

    /***********************************************************************************
     * Shows an error alert with the given message.
     *
     * @param msg the message to display
     ***********************************************************************************/
    private void showError(String msg)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }

    /***********************************************************************************
     * Shows an information alert with the given message.
     *
     * @param msg the message to display
     ***********************************************************************************/
    private void showInfo(String msg)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }

    /***********************************************************************************
     * Returns the base "Test Suites" folder path.
     *
     * @return the base folder path
     ***********************************************************************************/
    public Path getBaseTestSuitesFolder()
    {
        return baseTestSuitesFolder;
    }

    /***********************************************************************************
     * Lists the names of all immediate subfolders under the base "Test Suites" folder.
     *
     * @return a list of test suite folder names
     * @throws IOException if the folder cannot be read
     ***********************************************************************************/
    private List<String> listTestSuiteNames() throws IOException
    {
        List<String> names = new ArrayList<>();

        if (!Files.exists(baseTestSuitesFolder))
        {
            Files.createDirectories(baseTestSuitesFolder);
        }

        try (DirectoryStream<Path> stream =
                 Files.newDirectoryStream(baseTestSuitesFolder, Files::isDirectory))
        {
            for (Path p : stream)
            {
                names.add(p.getFileName().toString());
            }
        }

        names.sort(String::compareToIgnoreCase);
        return names;
    }

    /***********************************************************************************
     * Loads all test cases from the given test suite folder.
     *
     * File format:
     *   line 1: input
     *   line 2: expected output
     *
     * Filename:
     *   <test-case-name>.txt
     *
     * @param suiteName the name of the test suite folder
     * @return a list of TestCase objects
     * @throws IOException if files cannot be read
     ***********************************************************************************/
    private List<TestCase> loadTestCases(String suiteName) throws IOException
    {
        List<TestCase> cases = new ArrayList<>();

        if (suiteName == null || suiteName.isEmpty())
        {
            return cases;
        }

        Path suiteFolder = baseTestSuitesFolder.resolve(suiteName);

        if (!Files.exists(suiteFolder) || !Files.isDirectory(suiteFolder))
        {
            return cases;
        }

        try (DirectoryStream<Path> stream =
                 Files.newDirectoryStream(suiteFolder, "*.txt"))
        {
            int index = 1;

            for (Path file : stream)
            {
                List<String> lines = Files.readAllLines(file);

                String input =
                    (lines.size() > 0) ? lines.get(0) : "";
                String output =
                    (lines.size() > 1) ? lines.get(1) : "";

                // Name is always inferred from the filename (no name saved in file)
                String fileName = file.getFileName().toString();
                int dot = fileName.lastIndexOf('.');
                String name = (dot > 0) ? fileName.substring(0, dot) : fileName;

                cases.add(new TestCase(name, input, output, index));
                index++;
            }
        }

        return cases;
    }

    /***********************************************************************************
     * Saves the given list of test cases into the specified test suite folder.
     *
     * Behaviour:
     *  - All existing ".txt" files in the folder are removed.
     *  - New files are created as:
     *        <test-case-name>.txt
     *
     * File format:
     *   line 1: input
     *   line 2: expected output
     *
     * NOTE: Neither name nor index are written to the file.
     *
     * @param suiteName the test suite folder name
     * @param testCases the list of TestCase objects to save
     * @throws IOException if writing to disk fails
     ***********************************************************************************/
    private void saveTestCases(String suiteName, List<TestCase> testCases) throws IOException
    {
        if (suiteName == null || suiteName.isEmpty())
        {
            return;
        }

        Path suiteFolder = baseTestSuitesFolder.resolve(suiteName);
        Files.createDirectories(suiteFolder);

        // Delete existing .txt files
        try (DirectoryStream<Path> stream =
                 Files.newDirectoryStream(suiteFolder, "*.txt"))
        {
            for (Path file : stream)
            {
                Files.deleteIfExists(file);
            }
        }

        // Rewrite all files based on the current list
        for (TestCase tc : testCases)
        {
            String baseName = (tc.getName() != null) ? tc.getName().trim() : "";
            if (baseName.isEmpty())
            {
                baseName = "tc";
            }

            // Sanitize name for filesystem
            String safeName = baseName.replaceAll("[\\\\/:*?\"<>|]", "_");

            Path file = suiteFolder.resolve(safeName + ".txt");
            List<String> lines = new ArrayList<>();
            lines.add(tc.getInput() != null ? tc.getInput() : "");
            lines.add(tc.getOutput() != null ? tc.getOutput() : "");

            Files.write(file, lines);
        }
    }
}
