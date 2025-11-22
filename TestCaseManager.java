/**
 * Write a description of class TestCase here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestCaseManager extends Stage {
    private String selectedFolderPath; 
    private ArrayList<TestCase> testCases;

    public TestCaseManager() {
        VBox root = new VBox(10);
        Scene scene = new Scene(root, 400, 200);

        // Base folder
        String userHome = System.getProperty("user.home");
        Path baseFolder = Paths.get(userHome, "Auto Code Marker");
        Path testSuiteFolder = baseFolder.resolve("Test Suites");

        setTitle("Test Cases");
        setScene(scene);

        // Create the dropdown
        ComboBox<String> folderSelector = new ComboBox<>();
        folderSelector.setPromptText("Select a test suite");

        // Load folders into dropdown
        List<Path> folders = loadTestSuites(testSuiteFolder);
        for (Path folder : folders) {
            folderSelector.getItems().add(folder.getFileName().toString());
        }

        // Handle selection
        folderSelector.setOnAction(event -> {
            String selectedName = folderSelector.getSelectionModel().getSelectedItem();
            if (selectedName != null) {
                selectedFolderPath = testSuiteFolder.resolve(selectedName).toString();
                System.out.println("Selected folder path: " + selectedFolderPath);

                // Call your method with the selected folder Path
                loadTestSuites(Paths.get(selectedFolderPath));
            }
        });

        root.getChildren().add(folderSelector);
    }

    /**
     * Loads all folders inside a given path
     */
    private List<Path> loadTestSuites(Path folder) {
        List<Path> folderList = new ArrayList<>();
        if (Files.exists(folder) && Files.isDirectory(folder)) {
            try {
                Files.list(folder)
                        .filter(Files::isDirectory)
                        .forEach(folderList::add);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return folderList;
    }
}
