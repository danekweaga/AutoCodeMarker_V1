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
import javafx.stage.FileChooser;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.util.ArrayList;

/***************************************************************************************
 * @title   The OutputViewerV2 class.
 *
 * @author  Alamin Adeleke, Chuckwunonso Ekweaga,
 *          Aniekan Ekarika, Frances Felicidario
 * @version V1.0
 ***************************************************************************************/
public class OutputViewerV2 extends Stage
{
    private ArrayList<OutputV2> outputs;
    private int currentOutputIndex;

    private VBox leftResultsBox;
    private VBox rightResultsBox;
    private Label submissionNameLabel;
    private Label outputCounterLabel;
    private Button prevOutputButton;
    private Button nextOutputButton;
    private Button downloadButton;
    private Label firstSubmissionSuccessRateLabel;
    private Label secondSubmissionSuccessRateLabel;

    /***********************************************************************************
     * Constructor that initializes the viewer for a given list of outputs.
     *
     * @param outputs the ArrayList of OutputV2 objects containing submission names and test results
     ***********************************************************************************/
    public OutputViewerV2(ArrayList<OutputV2> outputs)
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
        setTitle("Test Results Viewer");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f2f5;");

        // Top bar with submission name and navigation
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(12, 20, 12, 20));
        topBar.setStyle("-fx-background-color: #1a237e;");
        topBar.setAlignment(Pos.CENTER_LEFT);

        submissionNameLabel = new Label("Test Results Viewer");
        submissionNameLabel.setFont(Font.font("Segoe UI", 18));
        submissionNameLabel.setTextFill(Color.WHITE);

        outputCounterLabel = new Label("Output 0 of 0");
        outputCounterLabel.setFont(Font.font("Segoe UI", 14));
        outputCounterLabel.setTextFill(Color.LIGHTGRAY);

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        downloadButton = new Button("📥 Download");
        downloadButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 6 12; -fx-background-radius: 4;");
        downloadButton.setOnAction(e -> downloadCurrentOutput());

        topBar.getChildren().addAll(submissionNameLabel, topSpacer, outputCounterLabel, downloadButton);
        root.setTop(topBar);

        // Center area with side-by-side submissions
        HBox centerBox = new HBox(15);
        centerBox.setPadding(new Insets(15));
        centerBox.setAlignment(Pos.TOP_CENTER);

        // First submission panel
        VBox firstSubmissionPanel = createSubmissionPanel("First Submission");
        leftResultsBox = new VBox(5);
        leftResultsBox.setPadding(new Insets(10));
        
        ScrollPane leftScrollPane = new ScrollPane(leftResultsBox);
        leftScrollPane.setFitToWidth(true);
        leftScrollPane.setStyle("-fx-background: white; -fx-border-color: #ddd; -fx-background-radius: 4;");
        leftScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        leftScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        firstSubmissionPanel.getChildren().add(1, leftScrollPane);
        VBox.setVgrow(leftScrollPane, Priority.ALWAYS);
        
        // Second submission panel
        VBox secondSubmissionPanel = createSubmissionPanel("Second Submission");
        rightResultsBox = new VBox(5);
        rightResultsBox.setPadding(new Insets(10));
        
        ScrollPane rightScrollPane = new ScrollPane(rightResultsBox);
        rightScrollPane.setFitToWidth(true);
        rightScrollPane.setStyle("-fx-background: white; -fx-border-color: #ddd; -fx-background-radius: 4;");
        rightScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        rightScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        secondSubmissionPanel.getChildren().add(1, rightScrollPane);
        VBox.setVgrow(rightScrollPane, Priority.ALWAYS);

        centerBox.getChildren().addAll(firstSubmissionPanel, secondSubmissionPanel);
        HBox.setHgrow(firstSubmissionPanel, Priority.ALWAYS);
        HBox.setHgrow(secondSubmissionPanel, Priority.ALWAYS);

        root.setCenter(centerBox);

        // Bottom bar with success rates and navigation
        HBox bottomBar = new HBox(20);
        bottomBar.setPadding(new Insets(12, 20, 12, 20));
        bottomBar.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e0e0e0; -fx-border-width: 1 0 0 0;");
        bottomBar.setAlignment(Pos.CENTER);

        // Success rate labels
        HBox successRateBox = new HBox(30);
        successRateBox.setAlignment(Pos.CENTER_LEFT);
        
        firstSubmissionSuccessRateLabel = new Label("First: N/A");
        firstSubmissionSuccessRateLabel.setFont(Font.font("Segoe UI", 14));
        
        secondSubmissionSuccessRateLabel = new Label("Second: N/A");
        secondSubmissionSuccessRateLabel.setFont(Font.font("Segoe UI", 14));
        
        successRateBox.getChildren().addAll(firstSubmissionSuccessRateLabel, secondSubmissionSuccessRateLabel);

        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);

        // Navigation buttons
        HBox navButtons = new HBox(8);
        navButtons.setAlignment(Pos.CENTER);
        
        prevOutputButton = new Button("◀ Previous");
        prevOutputButton.setStyle("-fx-background-color: #5c6bc0; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 6 12; -fx-background-radius: 4;");
        prevOutputButton.setOnAction(e -> navigateToPrevious());
        
        nextOutputButton = new Button("Next ▶");
        nextOutputButton.setStyle("-fx-background-color: #5c6bc0; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 6 12; -fx-background-radius: 4;");
        nextOutputButton.setOnAction(e -> navigateToNext());

        navButtons.getChildren().addAll(prevOutputButton, nextOutputButton);

        bottomBar.getChildren().addAll(successRateBox, bottomSpacer, navButtons);
        root.setBottom(bottomBar);

        Scene scene = new Scene(root, 1200, 700); // Increased width for better side-by-side view
        this.setScene(scene);
    }

    /***********************************************************************************
     * Creates a submission panel with header and scrollable results.
     ***********************************************************************************/
    private VBox createSubmissionPanel(String title)
    {
        VBox panel = new VBox();
        panel.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 6; -fx-border-width: 1;");
        panel.setMaxHeight(Double.MAX_VALUE);

        // Header
        Label header = new Label(title);
        header.setFont(Font.font("Segoe UI", 16));
        header.setTextFill(Color.WHITE);
        header.setStyle("-fx-background-color: #3949ab; -fx-padding: 12; -fx-alignment: center; -fx-background-radius: 6 6 0 0;");
        header.setMaxWidth(Double.MAX_VALUE);

        // Placeholder for scroll pane (will be replaced)
        Region placeholder = new Region();
        panel.getChildren().addAll(header, placeholder);
        VBox.setVgrow(placeholder, Priority.ALWAYS);

        return panel;
    }

    /***********************************************************************************
     * Downloads the current output as a serialized file.
     ***********************************************************************************/
    private void downloadCurrentOutput()
    {
        if (outputs == null || outputs.isEmpty() || currentOutputIndex >= outputs.size()) {
            return;
        }

        OutputV2 currentOutput = outputs.get(currentOutputIndex);
        String fileName = currentOutput.getSubmissionName().replaceAll("[^a-zA-Z0-9.-]", "_") + ".result";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Test Results");
        fileChooser.setInitialFileName(fileName);
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Result Files", "*.result")
        );

        File file = fileChooser.showSaveDialog(this);
        if (file != null) {
            try (FileOutputStream fileOut = new FileOutputStream(file);
                 ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(currentOutput);
                System.out.println("Results saved to: " + file.getAbsolutePath());
            } catch (Exception e) {
                System.err.println("Error saving results: " + e.getMessage());
            }
        }
    }

    /***********************************************************************************
     * Navigates to the previous output.
     ***********************************************************************************/
    private void navigateToPrevious()
    {
        if (currentOutputIndex > 0) {
            currentOutputIndex--;
            updateDisplay();
        }
    }

    /***********************************************************************************
     * Navigates to the next output.
     ***********************************************************************************/
    private void navigateToNext()
    {
        if (hasNextOutput()) {
            currentOutputIndex++;
            updateDisplay();
        }
    }

    /***********************************************************************************
     * Updates the entire display including current output content and navigation.
     ***********************************************************************************/
    private void updateDisplay()
    {
        updateResults();
        updateNavigation();
        updateSuccessRates();
    }

    /***********************************************************************************
     * Updates the success rate labels for both submissions.
     ***********************************************************************************/
    private void updateSuccessRates()
    {
        if (outputs == null || outputs.isEmpty() || currentOutputIndex >= outputs.size()) {
            firstSubmissionSuccessRateLabel.setText("First: N/A");
            secondSubmissionSuccessRateLabel.setText("Second: N/A");
            firstSubmissionSuccessRateLabel.setTextFill(Color.GRAY);
            secondSubmissionSuccessRateLabel.setTextFill(Color.GRAY);
            return;
        }

        OutputV2 currentOutput = outputs.get(currentOutputIndex);
        
        // Calculate success rate for first submission
        String firstSubmissionRate = calculateSuccessRate(currentOutput.getResult1());
        updateSuccessRateLabel(firstSubmissionSuccessRateLabel, "First: ", firstSubmissionRate);
        
        // Calculate success rate for second submission
        if (currentOutput.hasSecondSubmission()) {
            String secondSubmissionRate = calculateSuccessRate(currentOutput.getResult2());
            updateSuccessRateLabel(secondSubmissionSuccessRateLabel, "Second: ", secondSubmissionRate);
        } else {
            secondSubmissionSuccessRateLabel.setText("Second: N/A");
            secondSubmissionSuccessRateLabel.setTextFill(Color.GRAY);
        }
    }

    /***********************************************************************************
     * Updates a success rate label with appropriate styling.
     ***********************************************************************************/
    private void updateSuccessRateLabel(Label label, String prefix, String rate)
    {
        label.setText(prefix + rate);
        
        if (rate.contains("did not compile")) {
            label.setTextFill(Color.ORANGERED);
        } else if (rate.contains("Runtime error")) {
            label.setTextFill(Color.RED);
        } else if (rate.contains("No tests")) {
            label.setTextFill(Color.GRAY);
        } else if (rate.contains("(")) { // Has success rate
            try {
                // Extract the decimal value before the parenthesis
                String ratePart = rate.substring(0, rate.indexOf('(')).trim();
                double successRate = Double.parseDouble(ratePart);
                
                if (successRate >= 0.8) { // 80% or higher
                    label.setTextFill(Color.GREEN);
                } else if (successRate >= 0.6) { // 60-79%
                    label.setTextFill(Color.ORANGE);
                } else { // Below 60%
                    label.setTextFill(Color.RED);
                }
            } catch (Exception e) {
                label.setTextFill(Color.BLACK);
            }
        } else {
            label.setTextFill(Color.BLACK);
        }
    }

    /***********************************************************************************
     * Calculates success rate for a list of results.
     ***********************************************************************************/
    private String calculateSuccessRate(ArrayList<Result> results)
    {
        if (results == null || results.isEmpty()) {
            return "No tests";
        }

        // Check for compile errors
        for (Result result : results) {
            if (result != null && result.getResult() != null && 
                result.getResult().toLowerCase().contains("compile")) {
                return "Code did not compile";
            }
        }

        // Check for runtime errors
        for (Result result : results) {
            if (result != null && result.getResult() != null && 
                result.getResult().toLowerCase().contains("runtime")) {
                return "Runtime error";
            }
        }

        // Calculate success rate
        int totalPass = 0;
        int totalTests = 0;
        
        for (Result result : results) {
            if (result != null && result.getResult() != null) {
                totalTests++;
                if ("PASS".equalsIgnoreCase(result.getResult())) {
                    totalPass++;
                }
            }
        }

        if (totalTests == 0) {
            return "0.000 (0/0)";
        }

        double successRate = (double) totalPass / totalTests;
        return String.format("%.3f (%d/%d)", successRate, totalPass, totalTests);
    }

    /***********************************************************************************
     * Updates the results display for both submissions.
     ***********************************************************************************/
    private void updateResults()
    {
        // Clear both panels
        leftResultsBox.getChildren().clear();
        rightResultsBox.getChildren().clear();

        if (outputs == null || outputs.isEmpty()) {
            submissionNameLabel.setText("Test Results Viewer");
            outputCounterLabel.setText("Output 0 of 0");
            
            Label emptyLabel = new Label("No test results to display");
            emptyLabel.setFont(Font.font("Segoe UI", 14));
            emptyLabel.setTextFill(Color.GRAY);
            leftResultsBox.getChildren().add(emptyLabel);
            return;
        }

        OutputV2 currentOutput = outputs.get(currentOutputIndex);
        
        // Display first submission results
        displayResultsInPanel(leftResultsBox, currentOutput.getResult1(), "No test cases for first submission");
        
        // Display second submission results
        if (currentOutput.hasSecondSubmission()) {
            displayResultsInPanel(rightResultsBox, currentOutput.getResult2(), "No test cases for second submission");
        } else {
            Label notAvailableLabel = new Label("No second submission available");
            notAvailableLabel.setFont(Font.font("Segoe UI", 14));
            notAvailableLabel.setTextFill(Color.GRAY);
            notAvailableLabel.setStyle("-fx-font-style: italic; -fx-padding: 20;");
            notAvailableLabel.setAlignment(Pos.CENTER);
            rightResultsBox.getChildren().add(notAvailableLabel);
        }

        // Update header information
        String submissionNameText = currentOutput.getSubmissionName() != null ? 
                                  currentOutput.getSubmissionName() : "Unnamed Submission";
        submissionNameLabel.setText("Submission: " + submissionNameText);
        outputCounterLabel.setText("Result " + (currentOutputIndex + 1) + " of " + outputs.size());
    }

    /***********************************************************************************
     * Displays results in a specific panel.
     ***********************************************************************************/
    private void displayResultsInPanel(VBox panel, ArrayList<Result> results, String emptyMessage)
    {
        if (results.isEmpty()) {
            Label noResultsLabel = new Label(emptyMessage);
            noResultsLabel.setFont(Font.font("Segoe UI", 14));
            noResultsLabel.setTextFill(Color.GRAY);
            noResultsLabel.setStyle("-fx-font-style: italic; -fx-padding: 20;");
            noResultsLabel.setAlignment(Pos.CENTER);
            panel.getChildren().add(noResultsLabel);
            return;
        }

        for (Result result : results) {
            panel.getChildren().add(createResultRow(result));
        }
    }

    /***********************************************************************************
     * Creates a visual row for a single testcase result.
     ***********************************************************************************/
    private VBox createResultRow(Result result)
    {
        String testCaseName = result != null && result.getTestCaseName() != null ? 
                            result.getTestCaseName() : "Unnamed Test";
        String resultText = result != null && result.getResult() != null ? 
                          result.getResult() : "No Result";

        Label nameLabel = new Label(testCaseName);
        nameLabel.setFont(Font.font("Segoe UI", 13));
        nameLabel.setWrapText(true);

        Label resultLabel = new Label(resultText);
        resultLabel.setFont(Font.font("Segoe UI", 12));
        
        // Color coding based on result
        if ("PASS".equalsIgnoreCase(resultText)) {
            resultLabel.setTextFill(Color.GREEN);
            resultLabel.setStyle("-fx-font-weight: bold;");
        } else if ("FAIL".equalsIgnoreCase(resultText)) {
            resultLabel.setTextFill(Color.RED);
            resultLabel.setStyle("-fx-font-weight: bold;");
        } else if (resultText.toLowerCase().contains("compile")) {
            resultLabel.setTextFill(Color.ORANGERED);
            resultLabel.setStyle("-fx-font-weight: bold;");
        } else if (resultText.toLowerCase().contains("runtime")) {
            resultLabel.setTextFill(Color.DARKRED);
            resultLabel.setStyle("-fx-font-weight: bold;");
        } else {
            resultLabel.setTextFill(Color.DIMGRAY);
        }

        VBox row = new VBox(4, nameLabel, resultLabel);
        row.setPadding(new Insets(8, 10, 8, 10));
        row.setStyle("-fx-background-color: #fafafa; -fx-border-color: #e8eaf6; -fx-border-width: 1; -fx-border-radius: 3;");
        row.setMaxWidth(Double.MAX_VALUE);
        row.setSpacing(2);

        return row;
    }

    /***********************************************************************************
     * Updates the navigation button states.
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
     * Checks if there is another output after the current one.
     ***********************************************************************************/
    private boolean hasNextOutput()
    {
        return outputs != null && !outputs.isEmpty() && currentOutputIndex < outputs.size() - 1;
    }
}