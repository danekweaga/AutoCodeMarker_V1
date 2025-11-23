 

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

    private ArrayList<Output> outputs; // Changed from single Output to ArrayList
    private int currentOutputIndex; // Tracks which output we're viewing
    private int currentPageIndex;

    private VBox resultsBox;
    private Label submissionNameLabel;
    private Label pageLabel;
    private Button prevOutputButton; // For navigating between outputs
    private Button nextOutputButton; // For navigating between outputs
    private Button prevPageButton; // For navigating pages within current output
    private Button nextPageButton; // For navigating pages within current output
    private ScrollPane scrollPane; // Added scroll pane

    /***********************************************************************************
     * Default constructor creating an empty OutputViewer window.
     ***********************************************************************************/
    public OutputViewer()
    {
        this(new ArrayList<>()); // Initialize with empty list
    }
    
    /***********************************************************************************
     * Constructor that initializes the viewer for a given list of outputs.
     *
     * @param outputs the ArrayList of Output objects containing submission names and test results
     ***********************************************************************************/
    public OutputViewer(ArrayList<Output> outputs)
    {
        this.outputs = outputs;
        currentOutputIndex = 0;
        currentPageIndex = 0;
        initializeUI();
        updateDisplay();
    }

    /***********************************************************************************
     * Sets the Output data to display and refreshes the view.
     *
     * @param outputs the ArrayList of Outputs to show in this viewer
     ***********************************************************************************/
    public void setOutputs(ArrayList<Output> outputs)
    {
        this.outputs = outputs;
        currentOutputIndex = 0;
        currentPageIndex = 0;
        updateDisplay();
    }

    /***********************************************************************************
     * Adds a single Output to the list and refreshes the view.
     *
     * @param output the Output to add
     ***********************************************************************************/
    public void addOutput(Output output)
    {
        if (outputs == null) {
            outputs = new ArrayList<>();
        }
        outputs.add(output);
        updateDisplay();
    }

    /***********************************************************************************
     * Builds the JavaFX scene, layout, and controls for the Output Manager window.
     ***********************************************************************************/
    private void initializeUI()
    {
        setTitle("Output Manager");

        BorderPane root = new BorderPane();

        // Center area with scrollable results
        resultsBox = new VBox(10);
        resultsBox.setPadding(new Insets(15));
        
        scrollPane = new ScrollPane(resultsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(250);
        root.setCenter(scrollPane);

        // Bottom bar with submission name, page info, and navigation buttons
        submissionNameLabel = new Label();
        submissionNameLabel.setFont(Font.font("Consolas", 14));

        pageLabel = new Label("Output 0 of 0 | Page 0 of 0");
        pageLabel.setFont(Font.font("Consolas", 14));

        // Output navigation buttons (between different outputs)
        prevOutputButton = new Button("<< Output");
        nextOutputButton = new Button("Output >>");
        
        // Page navigation buttons (within current output)
        prevPageButton = new Button("< Page");
        nextPageButton = new Button("Page >");

        prevOutputButton.setOnAction(e ->
        {
            if (currentOutputIndex > 0)
            {
                currentOutputIndex--;
                currentPageIndex = 0;
                updateDisplay();
            }
        });

        nextOutputButton.setOnAction(e ->
        {
            if (hasNextOutput())
            {
                currentOutputIndex++;
                currentPageIndex = 0;
                updateDisplay();
            }
        });

        prevPageButton.setOnAction(e ->
        {
            if (currentPageIndex > 0)
            {
                currentPageIndex--;
                updatePage();
            }
        });

        nextPageButton.setOnAction(e ->
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
                prevOutputButton,
                nextOutputButton,
                prevPageButton,
                nextPageButton
        );

        root.setBottom(bottomBar);

        Scene scene = new Scene(root, 700, 400); // Increased width for more buttons
        setScene(scene);
    }

    /***********************************************************************************
     * Updates the entire display including output navigation and current page.
     ***********************************************************************************/
    private void updateDisplay()
    {
        updatePage();
        updateNavigation();
    }

    /***********************************************************************************
     * Updates the visible page: testcase/result boxes, submission label,
     * page indicator, and page navigation button states.
     ***********************************************************************************/
    private void updatePage()
    {
        resultsBox.getChildren().clear();

        if (outputs == null || outputs.isEmpty()) {
            // Show empty state
            submissionNameLabel.setText("No outputs available");
            pageLabel.setText("Output 0 of 0 | Page 0 of 0");
            prevPageButton.setDisable(true);
            nextPageButton.setDisable(true);
            
            // Add empty rows
            for (int i = 0; i < RESULTS_PER_PAGE; i++) {
                resultsBox.getChildren().add(createEmptyRow());
            }
            return;
        }

        Output currentOutput = outputs.get(currentOutputIndex);
        ArrayList<Result> results =
                (currentOutput != null && currentOutput.getResults() != null)
                        ? currentOutput.getResults()
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
                (currentOutput != null && currentOutput.getSubmissionName() != null)
                        ? currentOutput.getSubmissionName()
                        : "Submission Name";
        submissionNameLabel.setText(submissionNameText);

        // Page indicator X of Y (pages) and output indicator
        if (totalPages == 0)
        {
            pageLabel.setText("Output " + (currentOutputIndex + 1) + " of " + outputs.size() + " | Page 0 of 0");
        }
        else
        {
            pageLabel.setText("Output " + (currentOutputIndex + 1) + " of " + outputs.size() + 
                             " | Page " + (currentPageIndex + 1) + " of " + totalPages);
        }

        // Enable/disable page navigation arrows
        prevPageButton.setDisable(currentPageIndex <= 0);
        nextPageButton.setDisable(!hasNextPage());
    }

    /***********************************************************************************
     * Updates the output navigation button states.
     ***********************************************************************************/
    private void updateNavigation()
    {
        if (outputs == null || outputs.isEmpty()) {
            prevOutputButton.setDisable(true);
            nextOutputButton.setDisable(true);
            return;
        }

        prevOutputButton.setDisable(currentOutputIndex <= 0);
        nextOutputButton.setDisable(!hasNextOutput());
    }

    /***********************************************************************************
     * Creates a visual row for a single testcase result.
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
     * Determines whether there is another page of results after the current one
     * within the current output.
     *
     * @return true if another page exists; false otherwise
     ***********************************************************************************/
    private boolean hasNextPage()
    {
        if (outputs == null || outputs.isEmpty() || currentOutputIndex >= outputs.size()) {
            return false;
        }

        Output currentOutput = outputs.get(currentOutputIndex);
        if (currentOutput == null || currentOutput.getResults() == null) {
            return false;
        }

        int totalResults = currentOutput.getResults().size();
        int totalPages = (totalResults == 0)
                ? 0
                : ((totalResults - 1) / RESULTS_PER_PAGE) + 1;

        return currentPageIndex < totalPages - 1;
    }

    /***********************************************************************************
     * Determines whether there is another output after the current one.
     *
     * @return true if another output exists; false otherwise
     ***********************************************************************************/
    private boolean hasNextOutput()
    {
        if (outputs == null || outputs.isEmpty()) {
            return false;
        }

        return currentOutputIndex < outputs.size() - 1;
    }
}