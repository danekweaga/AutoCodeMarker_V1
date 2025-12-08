import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/***************************************************************************************
 * @title   OutputV2FileLoader
 *
 * @brief   Modern-style stage for importing and comparing result files.
 ***************************************************************************************/
public class OutputV2FileLoader extends Stage
{
    private final Stage owner;

    public OutputV2FileLoader(Stage owner)
    {
        setResizable(false);
        this.owner = owner;
        initOwner(owner);
        setTitle("Results");

        // Title
        Label title = new Label("Results Viewer");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label subtitle = new Label("Import or compare result files");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #666666;");

        VBox header = new VBox(4, title, subtitle);

        // Buttons (large, card-style)
        Button importButton = createPrimaryButton("Import Result");
        importButton.setOnAction(e -> handleImportSingle());

        Button compareButton = createSecondaryButton("Compare Results");
        compareButton.setOnAction(e -> handleCompare());

        VBox actionsBox = new VBox(14, importButton, compareButton);
        actionsBox.setAlignment(Pos.CENTER_LEFT);

        // Card container
        VBox card = new VBox(22, header, actionsBox);
        card.setPadding(new Insets(24));
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #dddddd;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;"
        );

        VBox root = new VBox(card);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f4f6f8;");

        Scene scene = new Scene(root, 380, 260);
        setScene(scene);
    }

    /* ======================= UI helpers ======================= */

    private Button createPrimaryButton(String text)
    {
        Button btn = new Button(text);
        btn.setPrefHeight(44);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle(
            "-fx-background-color: #2f80ed;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;"
        );
        return btn;
    }

    private Button createSecondaryButton(String text)
    {
        Button btn = new Button(text);
        btn.setPrefHeight(44);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle(
            "-fx-background-color: #e9ecef;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 10;"
        );
        return btn;
    }

    /* ======================= Logic (unchanged) ======================= */

    private void handleImportSingle()
    {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Result File (.txt)");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        File file = chooser.showOpenDialog(this);
        if (file == null) return;

        try {
            OutputV2 output = loadOutputV2FromFile(file);
            if (output == null) {
                showError("Invalid File", "The selected file is not a valid result file.");
                return;
            }

            ArrayList<OutputV2> list = new ArrayList<>();
            list.add(output);

            OutputViewerV2 viewer = new OutputViewerV2(list);
            viewer.initOwner(this);
            viewer.show();

        } catch (Exception e) {
            showError("Error", e.getMessage());
        }
    }

    private void handleCompare()
    {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Two Result Files (.txt)");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        List<File> files = chooser.showOpenMultipleDialog(this);
        if (files == null || files.size() < 2) {
            showError("Selection Error", "Please select two result files.");
            return;
        }

        try {
            OutputV2 outA = loadOutputV2FromFile(files.get(0));
            OutputV2 outB = loadOutputV2FromFile(files.get(1));

            if (outA == null || outB == null) {
                showError("Invalid File", "One or more selected files were invalid.");
                return;
            }

            Stage compareStage = new Stage();
            compareStage.initOwner(this);
            compareStage.setTitle("Results Comparison");

            HBox root = new HBox(20,
                    createResultTile("Result 1", outA),
                    createResultTile("Result 2", outB)
            );
            root.setPadding(new Insets(24));
            root.setAlignment(Pos.CENTER);
            root.setStyle("-fx-background-color: #f4f6f8;");

            compareStage.setScene(new Scene(root, 560, 280));
            compareStage.show();

        } catch (Exception e) {
            showError("Error", e.getMessage());
        }
    }

    /* ========== Helpers (unchanged logic) ========== */

    private OutputV2 loadOutputV2FromFile(File file) throws Exception
    {
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            Object obj = ois.readObject();
            if (obj instanceof OutputV2) {
                return (OutputV2) obj;
            }
        }
        return null;
    }

    private VBox createResultTile(String title, OutputV2 output)
    {
        VBox tile = new VBox(10);
        tile.setPadding(new Insets(16));
        tile.setPrefWidth(240);
        tile.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #dddddd;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;"
        );

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label name = new Label(output.getSubmissionName());

        Label sub1 = new Label("Submission 1: " + calculateSuccessRate(output.getResult1()));
        Label sub2 = new Label(
                output.hasSecondSubmission()
                        ? "Submission 2: " + calculateSuccessRate(output.getResult2())
                        : "Submission 2: Not available"
        );

        tile.getChildren().addAll(titleLabel, name, sub1, sub2);
        return tile;
    }

    private String calculateSuccessRate(ArrayList<Result> results)
    {
        if (results == null || results.isEmpty()) return "0/0";

        int total = 0, pass = 0;
        for (Result r : results) {
            if (r != null && r.getResult() != null) {
                total++;
                if ("PASS".equalsIgnoreCase(r.getResult().trim())) {
                    pass++;
                }
            }
        }
        return pass + "/" + total;
    }

    private void showError(String title, String message)
    {
        javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.initOwner(this);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
