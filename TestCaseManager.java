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
/***************************************************************************************
 * @title   The TestCaseManager class.
 *
 * @author  Alamin Adeleke, Chukwunonso Ekweaga,
 *          Aniekan Ekarika, Frances Felicidario
 * @version V1.0
 ***************************************************************************************/
public class TestCaseManager extends Stage
{
    private String selectedFolderPath;
    private ArrayList<TestCase> testCases;

    /***********************************************************************************
     * Default constructor that initializes the TestCaseManager window and loads
     * available test suite folders into a dropdown.
     ***********************************************************************************/
    public TestCaseManager()
    {
        VBox root = new VBox(10);
        Scene scene = new Scene(root, 400, 200);

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

                loadTestSuites(Paths.get(selectedFolderPath));
            }
        });

        root.getChildren().add(folderSelector);
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
}
