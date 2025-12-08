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
 * @version V1.3 (corrected)
 ***************************************************************************************/
public class TestCaseManager extends Stage
{
    private final Path baseTestSuitesFolder;
    private ComboBox<String> suiteComboBox;
    private ListView<TestCase> testCaseListView;
    private ObservableList<TestCase> testCaseObservableList;
    private String currentSuiteName;

    public TestCaseManager()
    {
        String userHome = System.getProperty("user.home");
        baseTestSuitesFolder = Paths.get(userHome, "Auto Code Marker", "Test Suites");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(5));
        topBar.setAlignment(Pos.CENTER_LEFT);
        Label selectLabel = new Label("Select Test Suite");
        selectLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        suiteComboBox = new ComboBox<>();
        suiteComboBox.setPromptText("Choose a suite");
        suiteComboBox.setMinWidth(200);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        topBar.getChildren().addAll(selectLabel, spacer, suiteComboBox);
        root.setTop(topBar);

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

        loadTestSuiteNames();
        suiteComboBox.setOnAction(e -> {
            currentSuiteName = suiteComboBox.getValue();
            loadTestCasesForSuite(currentSuiteName);
        });

        createButton.setOnAction(e -> handleCreateTestCase());
        updateButton.setOnAction(e -> handleUpdateTestCase());
        deleteButton.setOnAction(e -> handleDeleteTestCase());
        saveButton.setOnAction(e -> handleSaveTestCases());

        Scene scene = new Scene(root, 700, 400);
        this.setTitle("Test Case Manager");
        this.setScene(scene);
    }

    private void loadTestSuiteNames()
    {
        try {
            List<String> names = listTestSuiteNames();
            suiteComboBox.getItems().setAll(names);
        } catch (IOException e) {
            showError("Error loading test suite names: " + e.getMessage());
        }
    }

    private void loadTestCasesForSuite(String suiteName)
    {
        testCaseObservableList.clear();
        if (suiteName == null || suiteName.isEmpty()) return;

        try {
            testCaseObservableList.addAll(loadTestCases(suiteName));
        } catch (IOException e) {
            showError("Error loading test cases: " + e.getMessage());
        }
    }

    private void handleCreateTestCase()
    {
        if (!ensureSuiteSelected()) return;

        Optional<TestCase> result = showTestCaseDialog(null);
        result.ifPresent(tc -> {
            tc.setIndex(testCaseObservableList.size() + 1);
            testCaseObservableList.add(tc);
        });
    }

    private void handleUpdateTestCase()
    {
        if (!ensureSuiteSelected()) return;

        TestCase selected = testCaseListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a test case to update.");
            return;
        }

        Optional<TestCase> result = showTestCaseDialog(selected);
        result.ifPresent(updated -> {
            selected.setName(updated.getName());
            selected.setInput(updated.getInput());
            selected.setOutput(updated.getOutput());
            testCaseListView.refresh();
        });
    }

    private void handleDeleteTestCase()
    {
        if (!ensureSuiteSelected()) return;

        TestCase selected = testCaseListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a test case to delete.");
            return;
        }

        testCaseObservableList.remove(selected);
        int index = 1;
        for (TestCase tc : testCaseObservableList) {
            tc.setIndex(index++);
        }
        testCaseListView.refresh();
    }

    private void handleSaveTestCases()
    {
        if (!ensureSuiteSelected()) return;

        try {
            saveTestCases(currentSuiteName, new ArrayList<>(testCaseObservableList));
            showInfo("Test cases saved successfully.");
        } catch (IOException e) {
            showError("Error saving test cases: " + e.getMessage());
        }
    }

    private boolean ensureSuiteSelected()
    {
        if (currentSuiteName == null || currentSuiteName.isEmpty()) {
            showError("Please select a test suite first.");
            return false;
        }
        return true;
    }

    private Optional<TestCase> showTestCaseDialog(TestCase existing)
    {
        Dialog<TestCase> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Create Test Case" : "Update Test Case");

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(existing != null ? existing.getName() : "");
        TextField inputField = new TextField(existing != null ? existing.getInput() : "");
        TextField outputField = new TextField(existing != null ? existing.getOutput() : "");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Input:"), 0, 1);
        grid.add(inputField, 1, 1);
        grid.add(new Label("Output:"), 0, 2);
        grid.add(outputField, 1, 2);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                String name = nameField.getText().trim();
                if (name.isEmpty()) return null;
                int index = (existing != null) ? existing.getIndex() : 0;
                return new TestCase(name, inputField.getText().trim(), outputField.getText().trim(), index);
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private List<String> listTestSuiteNames() throws IOException
    {
        List<String> names = new ArrayList<>();
        if (!Files.exists(baseTestSuitesFolder)) Files.createDirectories(baseTestSuitesFolder);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(baseTestSuitesFolder, Files::isDirectory)) {
            for (Path p : stream) names.add(p.getFileName().toString());
        }

        names.sort(String::compareToIgnoreCase);
        return names;
    }

    private List<TestCase> loadTestCases(String suiteName) throws IOException
    {
        List<TestCase> cases = new ArrayList<>();
        Path suiteFolder = baseTestSuitesFolder.resolve(suiteName);
        if (!Files.exists(suiteFolder) || !Files.isDirectory(suiteFolder)) return cases;

        int index = 1;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(suiteFolder, "*.txt")) {
            for (Path file : stream) {
                List<String> lines = Files.readAllLines(file);
                String input = (lines.size() > 0) ? lines.get(0) : "";
                String output = (lines.size() > 1) ? lines.get(1) : "";

                String name = file.getFileName().toString();
                int dot = name.lastIndexOf('.');
                name = (dot > 0) ? name.substring(0, dot) : name;

                cases.add(new TestCase(name, input, output, index++));
            }
        }

        return cases;
    }

    private void saveTestCases(String suiteName, List<TestCase> testCases) throws IOException
    {
        if (suiteName == null || suiteName.isEmpty()) return;

        Path suiteFolder = baseTestSuitesFolder.resolve(suiteName);
        Files.createDirectories(suiteFolder);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(suiteFolder, "*.txt")) {
            for (Path file : stream) Files.deleteIfExists(file);
        }

        for (TestCase tc : testCases) {
            String baseName = (tc.getName() != null) ? tc.getName().trim() : "tc";
            if (baseName.isEmpty()) baseName = "tc";
            String safeName = baseName.replaceAll("[\\\\/:*?\"<>|]", "_");

            Path file = suiteFolder.resolve(safeName + ".txt");
            List<String> lines = new ArrayList<>();
            lines.add(tc.getInput() != null ? tc.getInput().trim() : "");
            lines.add(tc.getOutput() != null ? tc.getOutput().trim() : "");
            Files.write(file, lines);
        }
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}
