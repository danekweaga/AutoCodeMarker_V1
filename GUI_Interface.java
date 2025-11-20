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
        titleLabel.setFont(Font.font("Consolas", 38));  // Techy-looking font
        titleLabel.setTextFill(Color.web("#00BFFF"));   // Electric blue color
        titleLabel.setStyle("-fx-font-weight: bold;");  // Bold for emphasis
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(Pos.CENTER);


        // Labels for file selection
        Label submissionsLabel = new Label("Choose Submissions Folder:  ");
        TextField submissionsPathField = new TextField();
        submissionsPathField.setEditable(false);
        submissionsPathField.setPrefWidth(300);
        
        Label testCaseLabel = new Label("Choose Test Case:                  ");
        TextField testCasePathField = new TextField();
        testCasePathField.setEditable(false);
        testCasePathField.setPrefWidth(300);

        Label expectedOutputLabel = new Label("Choose Expected Output:      ");
        TextField expectedOutputPathField = new TextField();
        expectedOutputPathField.setEditable(false);
        expectedOutputPathField.setPrefWidth(300);


        // Buttons for choosing directories/files
        Button chooseSubmissionsButton = new Button("Browse");
        Button chooseTestCaseButton = new Button("Browse");
        Button chooseExpectedOutputButton = new Button("Browse");

        Rectangle down = new Rectangle(100, 40);
        down.setFill(Color.LIGHTBLUE);
        Rectangle run = new Rectangle(100, 40);
        run.setFill(Color.LIGHTGREEN);

        // Text nodes to display on top of rectangles
        Text downloadText = new Text("Download");
        downloadText.setFont(Font.font("Arial", 12)); // Set font for the text
        downloadText.setFill(Color.BLUE.brighter().brighter()); // Text color

        Text runTestCasesText = new Text("Run Test Cases");
        runTestCasesText.setFont(Font.font("Arial", 12)); // Set font for the text
        runTestCasesText.setFill(Color.GREEN); // Text color

        // Action handlers for folder/file selection
        chooseSubmissionsButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Submissions Folder");
            var folder = directoryChooser.showDialog(primaryStage);
            if (folder != null) {
                submissionsFolder = folder.getAbsolutePath();
                submissionsPathField.setText(submissionsFolder);
            }
        });


        chooseTestCaseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Test Case File");
            var file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                testCaseFile = file.getAbsolutePath();
                testCasePathField.setText(testCaseFile);
            }
        });


        chooseExpectedOutputButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Expected Output Folder");
            var folder = directoryChooser.showDialog(primaryStage);
            if (folder != null) {
                expectedOutputFolder = folder.getAbsolutePath();
                expectedOutputPathField.setText(expectedOutputFolder);
            }
        });


        // Action buttons
        Button downloadButton = new Button("Download");
        downloadButton.setOnAction(e -> runDownload());

        Button runTestCasesButton = new Button("Run Test Cases");
        runTestCasesButton.setOnAction(e -> runTestCases());

        // Layout setup
        VBox root = new VBox(22);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);

        // Each label + browse button stacked vertically
        HBox submissionsBox = new HBox(6, submissionsLabel, submissionsPathField, chooseSubmissionsButton);
        HBox testCaseBox = new HBox(6, testCaseLabel, testCasePathField, chooseTestCaseButton);
        HBox expectedOutputBox = new HBox(6, expectedOutputLabel, expectedOutputPathField, chooseExpectedOutputButton);
        
        submissionsBox.setAlignment(Pos.CENTER_LEFT);
        testCaseBox.setAlignment(Pos.CENTER_LEFT);
        expectedOutputBox.setAlignment(Pos.CENTER_LEFT);
        
        // Use StackPane to overlay text on top of rectangles
        StackPane downloadPane = new StackPane(down, downloadText);
        StackPane runPane = new StackPane(run, runTestCasesText);

        // Action buttons stay side-by-side
        HBox actionButtons = new HBox(15, downloadButton, runTestCasesButton);
        HBox base = new HBox(6, downloadPane, runPane);
        actionButtons.setAlignment(Pos.CENTER);

        // Add everything to root
        root.getChildren().addAll(titleLabel, submissionsBox, testCaseBox, expectedOutputBox, /*actionButtons,*/ base);

        Scene scene = new Scene(root, 600, 310);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // Empty methods for buttons
    private void runDownload() {
        // TODO: Add download functionality
        System.out.println("Download button clicked.");
    }

    private void runTestCases() {
        // TODO: Add test case functionality
        System.out.println("Run Test Cases button clicked.");
    }

    // Getter methods
    public String getSubmissionsFolder() {
        return submissionsFolder;
    }

    public String getTestCaseFile() {
        return testCaseFile;
    }

    public String getExpectedOutputFolder() {
        return expectedOutputFolder;
    }
}
