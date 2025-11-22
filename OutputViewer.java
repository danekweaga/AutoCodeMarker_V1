import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.layout.Priority;
import java.util.ArrayList;
/***************************************************************************************
 * @title   The OutputViewer class.
 *
 * @author  Alamin Adeleke, Chuckwunonso Ekweaga,
 *          Aniekan Ekarika, Frances Felicidario
 * @version V1.0
 ***************************************************************************************/
public class OutputViewer extends Stage
{
    private static final int RESULTS_PER_PAGE = 4;

    private Output output;
    private int currentPageIndex;

    private VBox resultsBox;
    private Label submissionNameLabel;
    private Label pageLabel;
    private Button prevButton;
    private Button nextButton;

    /***********************************************************************************
     * Default constructor creating an empty OutputViewer window.
     ***********************************************************************************/
    public OutputViewer()
    {
        this(null);
    }

    /***********************************************************************************
     * Constructor that initializes the viewer for a given submission output.
     *
     * @param output the Output object containing the submission name and test results
     ***********************************************************************************/
    public OutputViewer(Output output)
    {
        this.output = output;
        currentPageIndex = 0;
        initializeUI();
        updatePage();
    }

    /***********************************************************************************
     * Sets the Output data to display and refreshes the view.
     *
     * @param output the Output to show in this viewer
     ***********************************************************************************/
    public void setOutput(Output output)
    {
        this.output = output;
        currentPageIndex = 0;
        updatePage();
    }

    /***********************************************************************************
     * Builds the JavaFX scene, layout, and controls for the Output Manager window.
     ***********************************************************************************/
    private void initializeUI()
    {
        setTitle("Output Manager");

        BorderPane root = new BorderPane();

        // Center area with 4 result slots
        resultsBox = new VBox(10);
        resultsBox.setPadding(new Insets(15));
        root.setCenter(resultsBox);

        // Bottom bar with submission name, page info, and arrows
        submissionNameLabel = new Label();
        submissionNameLabel.setFont(Font.font("Consolas", 14));

        pageLabel = new Label("0 of 0");
        pageLabel.setFont(Font.font("Consolas", 14));

        prevButton = new Button("<");
        nextButton = new Button(">");

        prevButton.setOnAction(e ->
        {
            if (currentPageIndex > 0)
            {
                currentPageIndex--;
                updatePage();
            }
        });

        nextButton.setOnAction(e ->
        {
            if (hasNextPage())
            {
                currentPageIndex++;
                updatePage();
            }
        });

        HBox bottomBar = new HBox(10);
        bottomBar.setPadding(new Insets(10));
        bottomBar.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        bottomBar.getChildren().addAll(
                submissionNameLabel,
                spacer,
                pageLabel,
                prevButton,
                nextButton
        );

        root.setBottom(bottomBar);

        Scene scene = new Scene(root, 600, 350);
        setScene(scene);
    }

    /***********************************************************************************
     * Updates the visible page: testcase/result boxes, submission label,
     * page indicator, and arrow button states.
     ***********************************************************************************/
    private void updatePage()
    {
        resultsBox.getChildren().clear();

        ArrayList<Result> results =
                (output != null && output.getResults() != null)
                        ? output.getResults()
                        : new ArrayList<>();

        int totalResults = results.size();
        int totalPages = (totalResults == 0)
                ? 0
                : ((totalResults - 1) / RESULTS_PER_PAGE) + 1;

        if (currentPageIndex >= totalPages && totalPages > 0)
        {
            currentPageIndex = totalPages - 1;
        }

        int startIndex = currentPageIndex * RESULTS_PER_PAGE;
        int endIndex = Math.min(startIndex + RESULTS_PER_PAGE, totalResults);

        // Fill with real results
        for (int i = startIndex; i < endIndex; i++)
        {
            Result r = results.get(i);
            resultsBox.getChildren().add(createResultRow(r));
        }

        // If fewer than 4 results on this page, add empty placeholders
        int placeholders = RESULTS_PER_PAGE - (endIndex - startIndex);
        for (int i = 0; i < placeholders; i++)
        {
            resultsBox.getChildren().add(createEmptyRow());
        }

        // Submission name at bottom-left
        String submissionNameText =
                (output != null && output.getSubmissionName() != null)
                        ? output.getSubmissionName()
                        : "Submission Name";
        submissionNameLabel.setText(submissionNameText);

        // Page indicator X of Y (pages)
        if (totalPages == 0)
        {
            pageLabel.setText("0 of 0");
        }
        else
        {
            pageLabel.setText((currentPageIndex + 1) + " of " + totalPages);
        }

        // Enable/disable arrows
        prevButton.setDisable(currentPageIndex <= 0);
        nextButton.setDisable(!hasNextPage());
    }

    /***********************************************************************************
     * Creates a visual row for a single testcase result and wires a click
     * handler to open a detail view showing input, result, and status.
     *
     * @param result the Result object to display
     * @return a VBox representing one row
     ***********************************************************************************/
    private VBox createResultRow(Result result)
    {
        String testCaseName =
                (result != null && result.getTestCaseName() != null)
                        ? result.getTestCaseName()
                        : "Testcase Name";

        String resultText =
                (result != null && result.getResult() != null)
                        ? result.getResult()
                        : "result";

        Label nameLabel = new Label(testCaseName);
        nameLabel.setFont(Font.font("Consolas", 16));

        Label resultLabel = new Label(resultText);
        resultLabel.setFont(Font.font("Consolas", 14));
        resultLabel.setTextFill(Color.DIMGRAY);

        VBox box = new VBox(4, nameLabel, resultLabel);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #D3D3D3;");

        // Click row to show detailed info
        box.setOnMouseClicked(e -> showResultDetails(result));

        return box;
    }

    /***********************************************************************************
     * Creates an empty placeholder row to keep the layout consistent
     * when there are fewer than 4 results on the current page.
     *
     * @return a VBox representing an empty row
     ***********************************************************************************/
    private VBox createEmptyRow()
    {
        Label nameLabel = new Label("Testcase Name");
        nameLabel.setFont(Font.font("Consolas", 16));
        nameLabel.setTextFill(Color.GRAY);

        Label resultLabel = new Label("result");
        resultLabel.setFont(Font.font("Consolas", 14));
        resultLabel.setTextFill(Color.LIGHTGRAY);

        VBox box = new VBox(4, nameLabel, resultLabel);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #D3D3D3;");

        return box;
    }

    /***********************************************************************************
     * Opens a small detail window showing the testcase name, input,
     * result text, and result status for the selected row.
     *
     * NOTE: Currently the model does not store input separately,
     * so the input is shown as "Not available yet". Once you extend
     * Result or connect TestCase data, you can replace that.
     *
     * @param result the Result whose details to display
     ***********************************************************************************/
    private void showResultDetails(Result result)
    {
        Stage detailStage = new Stage();
        detailStage.initOwner(this);
        detailStage.setTitle("Testcase Details");

        String testCaseName =
                (result != null && result.getTestCaseName() != null)
                        ? result.getTestCaseName()
                        : "Testcase Name";

        String resultText =
                (result != null && result.getResult() != null)
                        ? result.getResult()
                        : "result";

        VBox root = new VBox(8);
        root.setPadding(new Insets(15));

        Label nameLabel = new Label("Testcase: " + testCaseName);
        nameLabel.setFont(Font.font("Consolas", 16));

        // Placeholder until input is actually stored in your model
        Label inputLabel = new Label("Input: (not available in Result yet)");
        inputLabel.setFont(Font.font("Consolas", 14));

        Label outputLabel = new Label("Result: " + resultText);
        outputLabel.setFont(Font.font("Consolas", 14));

        Label statusLabel = new Label("Status: " + resultText);
        statusLabel.setFont(Font.font("Consolas", 14));

        root.getChildren().addAll(nameLabel, inputLabel, outputLabel, statusLabel);

        Scene scene = new Scene(root, 400, 160);
        detailStage.setScene(scene);
        detailStage.setResizable(false);
        detailStage.show();
    }

    /***********************************************************************************
     * Determines whether there is another page of results after the current one.
     *
     * @return true if another page exists; false otherwise
     ***********************************************************************************/
    private boolean hasNextPage()
    {
        if (output == null || output.getResults() == null)
        {
            return false;
        }

        int totalResults = output.getResults().size();
        int totalPages = (totalResults == 0)
                ? 0
                : ((totalResults - 1) / RESULTS_PER_PAGE) + 1;

        return currentPageIndex < totalPages - 1;
    }
}
