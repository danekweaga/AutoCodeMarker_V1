import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/***************************************************************************************
 * @title   The OutputViewer class.
 *
 * @version V2.0
 *
 * Shows multiple Result objects with left/right navigation
 * and a vertical scrollable list of all outputs.
 ***************************************************************************************/
public class OutputViewer extends Stage
{
    private List<Result> results = new ArrayList<>();
    private int currentIndex = 0;

    private Label submissionNameLabel;
    private Label resultLabel;

    private Button prevButton;
    private Button nextButton;

    private VBox scrollContainer;
    private ScrollPane scrollPane;

    public OutputViewer(Output output)
    {
        this.results = new ArrayList<>(output.getResults());

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        submissionNameLabel = new Label(output.getSubmissionName());
        submissionNameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Current result display
        resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size: 16px;");

        updateDisplayedResult();

        // Navigation buttons
        prevButton = new Button("<");
        nextButton = new Button(">");

        prevButton.setOnAction(e -> showPrevious());
        nextButton.setOnAction(e -> showNext());

        HBox navBar = new HBox(10, prevButton, nextButton);
        navBar.setAlignment(Pos.CENTER);

        // Scrollable list of all results
        scrollContainer = new VBox(10);
        scrollContainer.setPadding(new Insets(10));

        for (Result r : results) {
            Label item = new Label(r.getTestCaseName() + " — " + r.getOutcome());
            item.setStyle("-fx-font-size: 14px;");
            scrollContainer.getChildren().add(item);
        }

        scrollPane = new ScrollPane(scrollContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(250);

        VBox centerBox = new VBox(20, resultLabel, navBar, scrollPane);
        centerBox.setAlignment(Pos.TOP_CENTER);

        root.setTop(submissionNameLabel);
        BorderPane.setAlignment(submissionNameLabel, Pos.CENTER);

        root.setCenter(centerBox);

        Scene scene = new Scene(root, 400, 450);
        setScene(scene);
        setTitle("Output Viewer");
    }

    private void updateDisplayedResult()
    {
        if (results.isEmpty()) {
            resultLabel.setText("No results available.");
            return;
        }

        Result current = results.get(currentIndex);
        resultLabel.setText(
                current.getTestCaseName() + "\nResult: " + current.getOutcome()
        );

        prevButton.setDisable(currentIndex == 0);
        nextButton.setDisable(currentIndex == results.size() - 1);
    }

    private void showPrevious()
    {
        if (currentIndex > 0) {
            currentIndex--;
            updateDisplayedResult();
        }
    }

    private void showNext()
    {
        if (currentIndex < results.size() - 1) {
            currentIndex++;
            updateDisplayedResult();
        }
    }
}
