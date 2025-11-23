 

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
    private ArrayList<Output> outputs;
    private int currentOutputIndex;

    private VBox resultsBox;
    private Label submissionNameLabel;
    private Label outputCounterLabel;
    private Button prevOutputButton;
    private Button nextOutputButton;
    private ScrollPane scrollPane;

    /***********************************************************************************
     * Constructor that initializes the viewer for a given list of outputs.
     *
     * @param outputs the ArrayList of Output objects containing submission names and test results
     ***********************************************************************************/
    public OutputViewer(ArrayList<Output> outputs)
    {
        this.outputs = outputs;
        currentOutputIndex = 0;
        initializeUI();
        updateDisplay();
    }

    /***********************************************************************************
     * Builds the JavaFX scene, layout, and controls for the Output Manager window.
     ***********************************************************************************/
    private void initializeUI()
    {
        setTitle("Output Manager");

        BorderPane root = new BorderPane();

        // Center area with scrollable results - ALL results displayed at once
        resultsBox = new VBox(10);
        resultsBox.setPadding(new Insets(15));
        
        scrollPane = new ScrollPane(resultsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(350); // Increased height to show more results
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setCenter(scrollPane);

        // Bottom bar with submission name, output counter, and navigation buttons
        submissionNameLabel = new Label();
        submissionNameLabel.setFont(Font.font("Consolas", 14));

        outputCounterLabel = new Label("Output 0 of 0");
        outputCounterLabel.setFont(Font.font("Consolas", 14));

        // Output navigation buttons (between different outputs)
        prevOutputButton = new Button("<< Previous Output");
        nextOutputButton = new Button("Next Output >>");

        prevOutputButton.setOnAction(e ->
        {
            if (currentOutputIndex > 0)
            {
                currentOutputIndex--;
                updateDisplay();
            }
        });

        nextOutputButton.setOnAction(e ->
        {
            if (hasNextOutput())
            {
                currentOutputIndex++;
                updateDisplay();
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
                outputCounterLabel,
                prevOutputButton,
                nextOutputButton
        );

        root.setBottom(bottomBar);

        Scene scene = new Scene(root, 600, 500); // Increased height for better viewing
        this.setScene(scene);
    }

    /***********************************************************************************
     * Updates the entire display including current output content and navigation.
     ***********************************************************************************/
    private void updateDisplay()
    {
        updateResults();
        updateNavigation();
    }

    /***********************************************************************************
     * Updates the results display with all test cases for the current output.
     ***********************************************************************************/
    private void updateResults()
    {
        resultsBox.getChildren().clear();

        if (outputs == null || outputs.isEmpty()) {
            // Show empty state
            submissionNameLabel.setText("No outputs available");
            outputCounterLabel.setText("Output 0 of 0");
            
            // Show message for empty state
            Label emptyLabel = new Label("No test results to display");
            emptyLabel.setFont(Font.font("Consolas", 16));
            emptyLabel.setTextFill(Color.GRAY);
            resultsBox.getChildren().add(emptyLabel);
            return;
        }

        Output currentOutput = outputs.get(currentOutputIndex);
        ArrayList<Result> results =
                (currentOutput != null && currentOutput.getResults() != null)
                        ? currentOutput.getResults()
                        : new ArrayList<>();

        // Display ALL results at once in the scrollable area
        for (Result result : results)
        {
            resultsBox.getChildren().add(createResultRow(result));
        }

        // If no results, show empty message
        if (results.isEmpty()) {
            Label noResultsLabel = new Label("No test cases for this submission");
            noResultsLabel.setFont(Font.font("Consolas", 14));
            noResultsLabel.setTextFill(Color.GRAY);
            resultsBox.getChildren().add(noResultsLabel);
        }

        // Update submission name and counter
        String submissionNameText =
                (currentOutput != null && currentOutput.getSubmissionName() != null)
                        ? currentOutput.getSubmissionName()
                        : "Submission Name";
        submissionNameLabel.setText(submissionNameText);
        outputCounterLabel.setText("Output " + (currentOutputIndex + 1) + " of " + outputs.size());
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
        
        // Color code results: GREEN for PASS, RED for FAIL, gray for others
        if ("PASS".equalsIgnoreCase(resultText)) {
            resultLabel.setTextFill(Color.GREEN);
        } else if ("FAIL".equalsIgnoreCase(resultText)) {
            resultLabel.setTextFill(Color.RED);
        } else {
            resultLabel.setTextFill(Color.DIMGRAY);
        }

        VBox box = new VBox(4, nameLabel, resultLabel);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: #E0E0E0; -fx-border-width: 1;");
        box.setMaxWidth(Double.MAX_VALUE);

        return box;
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