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
 * @brief   Window (extends Stage) that lets the user:
 *          - Import a single .txt file containing a serialized OutputV2 object
 *            and open it in OutputViewerV2.
 *          - Import two .txt files, deserialize OutputV2 for each,
 *            compute success rates (PASS/total) for result1 and result2,
 *            and display the comparison side by side in two tiles.
 ***************************************************************************************/
public class OutputV2FileLoader extends Stage
{
    private final Stage owner;

    public OutputV2FileLoader(Stage owner)
    {
        this.owner = owner;
        initOwner(owner);
        setTitle("Result Options");

        VBox root = new VBox(12);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.CENTER_LEFT);

        // Button 1: Import single result and open OutputViewerV2
        Button importResultButton = new Button("Import Result");
        importResultButton.setMaxWidth(Double.MAX_VALUE);
        importResultButton.setOnAction(e -> handleImportSingle());

        // Button 2: Compare two results and show success rates
        Button compareResultButton = new Button("Compare Results");
        compareResultButton.setMaxWidth(Double.MAX_VALUE);
        compareResultButton.setOnAction(e -> handleCompare());

        root.getChildren().addAll(importResultButton, compareResultButton);

        Scene scene = new Scene(root, 320, 150);
        setScene(scene);
    }

    /***********************************************************************************
     * Import one .txt file, deserialize OutputV2, and show in OutputViewerV2.
     ***********************************************************************************/
    private void handleImportSingle()
    {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Result File (.txt)");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        File file = chooser.showOpenDialog(this);
        if (file == null) {
            return; // user cancelled
        }

        try {
            OutputV2 output = loadOutputV2FromFile(file);
            if (output == null) {
                showError("Invalid File", "The selected file does not contain a valid OutputV2 object.");
                return;
            }

            ArrayList<OutputV2> list = new ArrayList<>();
            list.add(output);

            OutputViewerV2 viewer = new OutputViewerV2(list);
            viewer.initOwner(this);
            viewer.show();

        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Error", "Could not load result file:\n" + ex.getMessage());
        }
    }

    /***********************************************************************************
     * Compare two .txt files, deserialize OutputV2 objects, compute success rates
     * for result1 and result2 for both, and display them side by side.
     ***********************************************************************************/
    private void handleCompare()
    {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Two Result Files (.txt)");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        List<File> files = chooser.showOpenMultipleDialog(this);
        if (files == null || files.size() < 2) {
            showError("Selection Error", "Please select at least two result files.");
            return;
        }

        File fileA = files.get(0);
        File fileB = files.get(1);

        OutputV2 outA;
        OutputV2 outB;
        try {
            outA = loadOutputV2FromFile(fileA);
            outB = loadOutputV2FromFile(fileB);
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Error", "Could not load one or both result files:\n" + ex.getMessage());
            return;
        }

        if (outA == null || outB == null) {
            showError("Invalid File", "One or both files did not contain a valid OutputV2 object.");
            return;
        }

        // Build comparison window
        Stage compareStage = new Stage();
        compareStage.initOwner(this);
        compareStage.setTitle("Results Comparison");

        HBox root = new HBox(20);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.CENTER);

        VBox tile1 = createResultTile("Result 1", outA);
        VBox tile2 = createResultTile("Result 2", outB);

        root.getChildren().addAll(tile1, tile2);

        Scene scene = new Scene(root, 520, 260);
        compareStage.setScene(scene);
        compareStage.show();
    }

    /***********************************************************************************
     * Deserialize a single OutputV2 object from a .txt file.
     * Assumes the file was created with ObjectOutputStream.writeObject(outputV2).
     ***********************************************************************************/
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

    /***********************************************************************************
     * Creates one comparison tile:
     * - name (Result #1 or Result #2)
     * - Submission 1: success rate (PASS/total)
     * - Submission 2: success rate or "Not available" if hasSecondSubmission == false
     ***********************************************************************************/
    private VBox createResultTile(String title, OutputV2 output)
    {
        VBox tile = new VBox(8);
        tile.setPadding(new Insets(12));
        tile.setAlignment(Pos.TOP_LEFT);
        tile.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #cccccc;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;"
        );

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label nameLabel = new Label("Submission name: " + output.getSubmissionName());

        String firstRate = calculateSuccessRate(output.getResult1());

        String secondRate;
        if (output.hasSecondSubmission() && output.getResult2() != null && !output.getResult2().isEmpty()) {
            secondRate = calculateSuccessRate(output.getResult2());
        } else {
            secondRate = "Not available";
        }

        Label firstLabel = new Label("Submission 1: " + firstRate);
        Label secondLabel = new Label("Submission 2: " + secondRate);

        tile.getChildren().addAll(titleLabel, nameLabel, firstLabel, secondLabel);
        return tile;
    }

    /***********************************************************************************
     * Calculates success rate as "pass/total".
     * - Only "PASS" (case-insensitive) counts as success.
     * - Anything else ("FAIL", "DID NOT COMPILE", "DID NOT RUN", etc.) counts as failure.
     * - If the list is null or empty → "0/0".
     ***********************************************************************************/
    private String calculateSuccessRate(ArrayList<Result> results)
    {
        if (results == null || results.isEmpty()) {
            return "0/0";
        }

        int total = 0;
        int pass  = 0;

        for (Result r : results) {
            if (r == null || r.getResult() == null) {
                continue;
            }
            total++;
            String res = r.getResult().trim().toUpperCase();
            if ("PASS".equals(res)) {
                pass++;
            }
            // Any other value: FAIL / DID NOT COMPILE / DID NOT RUN → not counted as success.
        }

        if (total == 0) {
            return "0/0";
        }

        return pass + "/" + total;
    }

    private void showError(String title, String message)
    {
        Stage ownerStage = (owner != null) ? owner : this;
        javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.initOwner(ownerStage);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
