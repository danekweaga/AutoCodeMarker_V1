import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/***************************************************************************************
 * @title   The TestCaseManager class.
 *
 * @author  Alamin Adeleke, Chukwunonso Ekweaga,
 *          Aniekan Ekarika, Frances Felicidario
 * @version V1.0
 ***************************************************************************************/
public class TestCaseManager extends Stage
{
    // Instance data
    private String selectedFolderPath;
    private ArrayList<Path> testCases;

    private final ListView<String> testCasesListView = new ListView<>();

    /***********************************************************************************
     * Default constructor that initializes the TestCaseManager window and loads
     * available test suite folders into a dropdown.
     ***********************************************************************************/
    public TestCaseManager()
    {
        testCases = new ArrayList<>();

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        Scene scene = new Scene(root, 400, 300);

        String userHome = System.getProperty("user.home");
        Path baseFolder = Paths.get(userHome, "Auto Code Marker");
        Path testSuiteFolder = baseFolder.resolve("Test Suites");

        setTitle("Test Cases");
        setScene(scene);

        ComboBox<String> folderSelector = new ComboBox<>();
        folderSelector.setPromptText("Select a test suite");

        List<Path> folders = loadTestSuites(testSuiteFolder);
        for (Path folder : folders)
        {
            folderSelector.getItems().add(folder.getFileName().toString());
        }

        folderSelector.setOnAction(event ->
        {
            String selectedName = folderSelector.getSelectionModel().getSelectedItem();
            if (selectedName != null)
            {
                selectedFolderPath = testSuiteFolder.resolve(selectedName).toString();
                System.out.println("Selected folder path: " + selectedFolderPath);
                loadTestCases(Paths.get(selectedFolderPath));
            }
        });

        Button addTestCaseButton = new Button("Add Test Case From File");
        addTestCaseButton.setOnAction(e -> addTestCaseFromFile());

        testCasesListView.setPrefHeight(180);

        root.getChildren().addAll(folderSelector, testCasesListView, addTestCaseButton);
    }

    /***********************************************************************************
     * Loads and returns all subfolders inside the given folder path.
     *
     * @param folder the directory containing test suite folders
     * @return a list of discovered folder paths
     ***********************************************************************************/
    private List<Path> loadTestSuites(Path folder)
    {
        List<Path> folderList = new ArrayList<>();
        if (Files.exists(folder) && Files.isDirectory(folder))
        {
            try
            {
                Files.list(folder)
                     .filter(Files::isDirectory)
                     .forEach(folderList::add);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return folderList;
    }

    /***********************************************************************************
     * Loads test case files from the selected suite folder into the list view.
     *
     * @param folder the test suite folder
     ***********************************************************************************/
    private void loadTestCases(Path folder)
    {
        testCases.clear();
        testCasesListView.getItems().clear();

        if (Files.exists(folder) && Files.isDirectory(folder))
        {
            try
            {
                Files.list(folder)
                     .filter(Files::isRegularFile)
                     .forEach(path ->
                     {
                         testCases.add(path);
                         testCasesListView.getItems().add(path.getFileName().toString());
                     });
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /***********************************************************************************
     * Adds a new test case file to the currently selected test suite by copying
     * the chosen file into the suite folder.
     ***********************************************************************************/
    private void addTestCaseFromFile()
    {
        if (selectedFolderPath == null)
        {
            System.out.println("No test suite selected.");
            return;
        }

        FileChooser fc = new FileChooser();
        fc.setTitle("Select Test Case File");
        File selectedFile = fc.showOpenDialog(this);

        if (selectedFile != null)
        {
            try
            {
                Path targetFolder = Paths.get(selectedFolderPath);
                Files.createDirectories(targetFolder);

                Path targetFile = targetFolder.resolve(selectedFile.getName());
                Files.copy(selectedFile.toPath(), targetFile,
                           java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                // Refresh view
                loadTestCases(targetFolder);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
