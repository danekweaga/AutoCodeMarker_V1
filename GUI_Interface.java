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

public class GUI_Interface extends Application 
{

    private String submissionsFolder;
    private String testCaseFile;
    private String expectedOutputFolder;

    public static void main(String[] args) 
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) 
    {
        primaryStage.setTitle("↢ACM↣");

        Label titleLabel = new Label("Auto Code Marker");
        titleLabel.setFont(Font.font("Consolas", 18));  // Techy-looking font
        titleLabel.setTextFill(Color.web("#00BFFF"));   // Electric blue color
        titleLabel.setStyle("-fx-font-weight: bold;");  // Bold for emphasis
        
        
        // Labels
        Label submissionsLabel = new Label("Choose Submissions Folder:");
        Label testCaseLabel = new Label("Choose Test Case:");
        Label expectedOutputLabel = new Label("Choose Expected Output:");

        // Buttons for choosing directories/files
        Button chooseSubmissionsButton = new Button("Browse");
        Button chooseTestCaseButton = new Button("Browse");
        Button chooseExpectedOutputButton = new Button("Browse");

        // Action handlers for folder/file selection
        chooseSubmissionsButton.setOnAction(e -> 
        {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Submissions Folder");
            var folder = directoryChooser.showDialog(primaryStage);
            if (folder != null) 
            {
                submissionsFolder = folder.getAbsolutePath();
                submissionsLabel.setText("Choose Submissions Folder:\n" + submissionsFolder);
            }
        });

        chooseTestCaseButton.setOnAction(e ->
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Test Case File");
            var file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) 
            {
                testCaseFile = file.getAbsolutePath();
                testCaseLabel.setText("Choose Test Case:\n" + testCaseFile);
            }
        });

        chooseExpectedOutputButton.setOnAction(e -> 
        {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Expected Output Folder");
            var folder = directoryChooser.showDialog(primaryStage);
            if (folder != null) 
            {
                expectedOutputFolder = folder.getAbsolutePath();
                expectedOutputLabel.setText("Choose Expected Output:\n" + expectedOutputFolder);
            }
        });

        // Action buttons
        Button downloadButton = new Button("Download");
        downloadButton.setOnAction(e -> runDownload());

        Button runTestCasesButton = new Button("Run Test Cases");
        runTestCasesButton.setOnAction(e -> runTestCases());

        // Layout setup
        VBox root = new VBox(25);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);

        // Each label + browse button stacked vertically
        VBox submissionsBox = new VBox(8, submissionsLabel, chooseSubmissionsButton);
        VBox testCaseBox = new VBox(8, testCaseLabel, chooseTestCaseButton);
        VBox expectedOutputBox = new VBox(8, expectedOutputLabel, chooseExpectedOutputButton);

        // Action buttons stay side-by-side
        HBox actionButtons = new HBox(15, downloadButton, runTestCasesButton);
        actionButtons.setAlignment(Pos.CENTER);

        // Add everything to root
        root.getChildren().addAll(titleLabel, submissionsBox, testCaseBox, expectedOutputBox, actionButtons);

        Scene scene = new Scene(root, 222, 350);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // Empty methods for buttons
    private void runDownload() 
    {
        // TODO: Add download functionality
        System.out.println("Download button clicked.");
    }

    private void runTestCases() 
    {
        // TODO: Add test case functionality
        System.out.println("Run Test Cases button clicked.");
    }

    // Getter methods
    public String getSubmissionsFolder() 
    {
        return submissionsFolder;
    }

    public String getTestCaseFile() 
    {
        return testCaseFile;
    }

    public String getExpectedOutputFolder()
    {
        return expectedOutputFolder;
    }
}
