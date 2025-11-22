import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.layout.Priority;
import java.util.ArrayList;
import java.util.List;
/***************************************************************************************
 * @title   The OutputViewer class.
 *
 * @author  Alamin Adeleke, Chuckwunonso Ekweaga,
 *          Aniekan Ekarika, Frances Felicidario
 * @version V1.0
 ***************************************************************************************/
public class OutputViewer extends Stage
{
    // Store multiple submissions' outputs; Next/Previous navigates submissions
    private List<Output> outputs = new ArrayList<>();
    private int currentIndex = 0;

    private VBox resultsBox;
    private Label submissionNameLabel;
    private Label pageLabel;
    private Button prevButton;
    private Button nextButton;
    private ScrollPane resultsScroll;

    /***********************************************************************************
     * Default constructor creating an empty OutputViewer window.
     ***********************************************************************************/
    public OutputViewer()
    {
        this((Output) null);
    }

    /***********************************************************************************
     * Constructor that initializes the viewer for a given submission output.
     *
     * @param output the Output object containing the submission name and test results
     ***********************************************************************************/
    public OutputViewer(Output output)
    {
        if (output != null)
        {
            this.outputs.add(output);
            this.currentIndex = 0;
        }
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
        this.outputs.clear();
        if (output != null) this.outputs.add(output);
        this.currentIndex = 0;
        updatePage();
    }

    /**
     * Replace the current list of outputs (submissions) and show the first one.
     */
    public void setOutputs(List<Output> outputs)
    {
        this.outputs = outputs == null ? new ArrayList<>() : outputs;
        this.currentIndex = 0;
        updatePage();
    }

    /***********************************************************************************
     * Builds the JavaFX scene, layout, and controls for the Output Manager window.
     ***********************************************************************************/
    private void initializeUI()
    {
        setTitle("Output Manager");

        BorderPane root = new BorderPane();

    // Center area: scrollable vertical list of results
    resultsBox = new VBox(10);
    resultsBox.setPadding(new Insets(15));
    resultsScroll = new ScrollPane(resultsBox);
    resultsScroll.setFitToWidth(true);
    root.setCenter(resultsScroll);

        // Bottom bar with submission name, page info, and arrows
        submissionNameLabel = new Label();
        submissionNameLabel.setFont(Font.font("Consolas", 14));

        pageLabel = new Label("0 of 0");
        pageLabel.setFont(Font.font("Consolas", 14));

        prevButton = new Button("<");
        nextButton = new Button(">");

        prevButton.setOnAction(e ->
        {
            if (currentIndex > 0)
            {
                currentIndex--;
                updatePage();
            }
        });

        nextButton.setOnAction(e ->
        {
            if (currentIndex < outputs.size() - 1)
            {
                currentIndex++;
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

        resultsBox.getChildren().clear();

        Output output = (outputs.isEmpty() ? null : outputs.get(currentIndex));
        List<Result> results = (output != null && output.getResults() != null)
                ? output.getResults()
                : new ArrayList<>();

        // Add all results to the scrollable list
        for (Result r : results)
        {
            resultsBox.getChildren().add(createResultRow(r));
        }

        // Submission name at bottom-left
        String submissionNameText = (output != null && output.getSubmissionName() != null)
                ? output.getSubmissionName()
                : "(no submission)";
        submissionNameLabel.setText(submissionNameText);

        // Page indicator: submission index of total submissions
        if (outputs.isEmpty())
        {
            pageLabel.setText("0 of 0 submissions");
        }
        else
        {
            pageLabel.setText((currentIndex + 1) + " of " + outputs.size() + " submissions");
        }

        // Enable/disable arrows
        prevButton.setDisable(currentIndex <= 0 || outputs.isEmpty());
        nextButton.setDisable(outputs.isEmpty() || currentIndex >= outputs.size() - 1);
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
