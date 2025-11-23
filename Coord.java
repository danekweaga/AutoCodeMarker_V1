import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;

/***************************************************************************************
 * @title   The Coord class.
 *
 * Handles test execution and UI coordination.
 *
 ***************************************************************************************/
public class Coord
{
    private final Stage owner;

    private TestSuiteManager suiteManager;
    private TestCaseManager caseManager;
    private OutputViewer outputView;
    private Stage loadingStage;

    public Coord(Stage owner) {
        this.owner = owner;
    }

    /**
     * Runs tests on submissions given a submission folder and test suite folder.
     */
    public void runTests(String submissionFolder, String testSuiteFolderName) {
        showLoadingDialog();

        new Thread(() -> {
            ArrayList<Output> outputs = new ArrayList<>();
            TestSuite testSuite = new TestSuite();

            try {
                String userHome = System.getProperty("user.home");
                Path baseFolder = Paths.get(userHome, "Auto Code Marker");
                Path testSuiteFolder = baseFolder.resolve("Test Suites").resolve(testSuiteFolderName);

                if (!Files.exists(testSuiteFolder) || !Files.isDirectory(testSuiteFolder)) {
                    throw new IOException("Test Suite folder not found: " + testSuiteFolder);
                }

                // Load test cases from .txt files
                int index = 0;
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(testSuiteFolder, "*.txt")) {
                    for (Path file : stream) {
                        List<String> lines = Files.readAllLines(file);
                        String input = lines.size() > 0 ? lines.get(0) : "";
                        String output = lines.size() > 1 ? lines.get(1) : "";
                        testSuite.add(new TestCase(file.getFileName().toString(), input, output, index++));
                    }
                }

                // Get submissions
                File submissionsDir = new File(submissionFolder);
                if (!submissionsDir.exists() || !submissionsDir.isDirectory()) {
                    throw new IOException("Submission folder not found: " + submissionFolder);
                }

                File[] submissionFolders = submissionsDir.listFiles(File::isDirectory);
                if (submissionFolders == null) submissionFolders = new File[0];

                for (File folder : submissionFolders) {
                    File[] javaFiles = folder.listFiles(f -> f.isFile() && f.getName().endsWith(".java"));
                    if (javaFiles == null || javaFiles.length == 0) continue;

                    String fileName = javaFiles[0].getName();
                    Submission submission = new Submission(folder.getName(), folder.getAbsolutePath(), fileName);
                    Output output = new Output(submission.name);

                    // Compile
                    String compileError = compileCode(submission);
                    if (compileError != null) {
                        for (TestCase tc : testSuite.testSuite) {
                            output.addResult(new Result(tc.getName(), "FAIL - Compilation Error: " + compileError));
                        }
                        outputs.add(output);
                        continue;
                    }

                    // Run test cases
                    for (TestCase tc : testSuite.testSuite) {
                        String resultText;
                        try {
                            // Split inputs by spaces to pass as command-line arguments
                            String[] args = tc.getInput().split("\\s+");
                            String programOutput = runCode(submission, args);

                            String expected = tc.getOutput() != null ? tc.getOutput().trim() : "";
                            String actual = programOutput != null ? programOutput.trim() : "";

                            resultText = expected.equals(actual) ? "PASS" :
                                    "FAIL - Expected: '" + expected + "', Got: '" + actual + "'";
                        } catch (Exception e) {
                            resultText = "FAIL - Runtime Error: " + e.getMessage();
                        }
                        output.addResult(new Result(tc.getName(), resultText));
                    }

                    outputs.add(output);
                }

            } catch (Exception e) {
                Platform.runLater(() -> {
                    hideLoadingDialog();
                    showErrorDialog("Test Execution Error", "An error occurred while running tests: " + e.getMessage());
                });
                return;
            }

            // Show output viewer and hide modal
            Platform.runLater(() -> {
                hideLoadingDialog(); // Ensure loading modal is gone
                if (outputView != null && outputView.isShowing()) {
                    outputView.close();
                }
                outputView = new OutputViewer(outputs);
                outputView.initOwner(owner);
                outputView.initModality(Modality.NONE);
                outputView.setOnCloseRequest(e -> {
                    outputView = null;
                    caseManager = null;
                });
                outputView.show();
            });

        }).start();
    }

    private void showLoadingDialog() {
        Platform.runLater(() -> {
            loadingStage = new Stage();
            loadingStage.initOwner(owner);
            loadingStage.initModality(Modality.WINDOW_MODAL);
            loadingStage.setTitle("Running Tests...");

            ProgressBar progressBar = new ProgressBar();
            progressBar.setProgress(-1);

            Label label = new Label("Running tests, please wait...");

            VBox content = new VBox(10);
            content.getChildren().addAll(label, progressBar);
            content.setStyle("-fx-padding: 20; -fx-alignment: center;");

            Scene scene = new Scene(content, 300, 100);
            loadingStage.setScene(scene);
            loadingStage.setResizable(false);
            loadingStage.show();
        });
    }

    private void hideLoadingDialog() {
        Platform.runLater(() -> {
            if (loadingStage != null) {
                loadingStage.close();
                loadingStage = null;
            }
        });
    }

    private void showErrorDialog(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(owner);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private String compileCode(Submission submission) {
        try {
            ProcessBuilder pb = new ProcessBuilder("javac", submission.fileName);
            pb.directory(new File(submission.path));
            Process process = pb.start();

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder errorOutput = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode != 0 || errorOutput.length() > 0) {
                return errorOutput.toString().trim();
            }
        } catch (Exception e) {
            return "Compilation process error: " + e.getMessage();
        }
        return null;
    }

    /**
     * Runs the compiled code with command-line arguments.
     */
    private String runCode(Submission submission, String[] args) throws Exception {
        String className = submission.fileName.replace(".java", "");
        ArrayList<String> command = new ArrayList<>();
        command.add("java");
        command.add(className);
        for (String arg : args) command.add(arg);

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(submission.path));
        pb.redirectErrorStream(true);

        Process process = pb.start();

        StringBuilder outputBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (outputBuilder.length() > 0) outputBuilder.append("\n");
                outputBuilder.append(line);
            }
        }

        boolean finished = process.waitFor(10, java.util.concurrent.TimeUnit.SECONDS);
        if (!finished) {
            process.destroyForcibly();
            throw new RuntimeException("Program execution timed out (10 seconds)");
        }

        int exitCode = process.exitValue();
        if (exitCode != 0) {
            throw new RuntimeException("Program exited with error code " + exitCode + ". Output: " + outputBuilder.toString());
        }

        return outputBuilder.toString().trim();
    }

    public void manageTestSuites() {
        if (suiteManager == null || !suiteManager.isShowing()) {
            suiteManager = new TestSuiteManager();
            suiteManager.initOwner(owner);
            suiteManager.initModality(Modality.NONE);
            suiteManager.setOnCloseRequest(e -> suiteManager = null);
            suiteManager.show();
        } else {
            suiteManager.toFront();
        }
    }

    public void manageTestCases() {
        if (caseManager == null || !caseManager.isShowing()) {
            caseManager = new TestCaseManager();
            caseManager.initOwner(owner);
            caseManager.initModality(Modality.NONE);
            caseManager.setOnCloseRequest(e -> caseManager = null);
            caseManager.show();
        } else {
            caseManager.toFront();
        }
    }
}
