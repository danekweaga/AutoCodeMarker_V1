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
import java.util.HashMap;
import java.util.Map;

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
    private OutputViewerV2 outputView;
    private ResultManager resultManager;
    private Stage loadingStage;

    public Coord(Stage owner) {
        this.owner = owner;
    }

    public void runTests(String firstSubmissionFolder, String secondSubmissionFolder, String testSuiteFolderName) {
        showLoadingDialog();
    
        new Thread(() -> {
            ArrayList<OutputV2> outputs = new ArrayList<>();
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
    
                // Get first submissions
                File firstSubmissionsDir = new File(firstSubmissionFolder);
                if (!firstSubmissionsDir.exists() || !firstSubmissionsDir.isDirectory()) {
                    throw new IOException("First submission folder not found: " + firstSubmissionFolder);
                }
    
                File[] firstSubmissionFolders = firstSubmissionsDir.listFiles(File::isDirectory);
                if (firstSubmissionFolders == null) firstSubmissionFolders = new File[0];
    
                // Get second submissions if provided
                File secondSubmissionsDir = null;
                File[] secondSubmissionFolders = new File[0];
                boolean hasSecondFolder = secondSubmissionFolder != null && !secondSubmissionFolder.isEmpty();
                
                if (hasSecondFolder) {
                    secondSubmissionsDir = new File(secondSubmissionFolder);
                    if (!secondSubmissionsDir.exists() || !secondSubmissionsDir.isDirectory()) {
                        throw new IOException("Second submission folder not found: " + secondSubmissionFolder);
                    }
                    secondSubmissionFolders = secondSubmissionsDir.listFiles(File::isDirectory);
                    if (secondSubmissionFolders == null) secondSubmissionFolders = new File[0];
                }
    
                // Create a map for quick lookup of second submissions by folder name
                Map<String, File> secondSubmissionMap = new HashMap<>();
                for (File folder : secondSubmissionFolders) {
                    secondSubmissionMap.put(folder.getName(), folder);
                }
    
                // Process first submissions
                for (File firstFolder : firstSubmissionFolders) {
                    File[] firstJavaFiles = firstFolder.listFiles(f -> f.isFile() && f.getName().endsWith(".java"));
                    if (firstJavaFiles == null || firstJavaFiles.length == 0) continue;
    
                    String firstFileName = firstJavaFiles[0].getName();
                    String folderName = firstFolder.getName();
                    
                    // Create OutputV2 for this submission
                    OutputV2 outputV2 = new OutputV2(folderName);
                    
                    // Process first submission
                    Submission firstSubmission = new Submission(folderName, firstFolder.getAbsolutePath(), firstFileName);
                    String compileError = compileCode(firstSubmission);
                    
                    if (compileError != null) {
                        // Compilation failed for first submission
                        for (TestCase tc : testSuite.testSuite) {
                            outputV2.addResult1(new Result(tc.getName(), "FAIL - Compilation Error: " + compileError));
                        }
                    } else {
                        // Run test cases for first submission
                        for (TestCase tc : testSuite.testSuite) {
                            String resultText = runTestOnSubmission(firstSubmission, tc);
                            outputV2.addResult1(new Result(tc.getName(), resultText));
                        }
                    }
    
                    // Check if there's a matching second submission
                    File secondFolder = secondSubmissionMap.get(folderName);
                    if (secondFolder != null) {
                        File[] secondJavaFiles = secondFolder.listFiles(f -> f.isFile() && f.getName().endsWith(".java"));
                        if (secondJavaFiles != null && secondJavaFiles.length > 0) {
                            String secondFileName = secondJavaFiles[0].getName();
                            Submission secondSubmission = new Submission(folderName, secondFolder.getAbsolutePath(), secondFileName);
                            
                            String secondCompileError = compileCode(secondSubmission);
                            
                            if (secondCompileError != null) {
                                // Compilation failed for second submission
                                for (TestCase tc : testSuite.testSuite) {
                                    outputV2.addResult2(new Result(tc.getName(), "FAIL - Compilation Error: " + secondCompileError));
                                }
                            } else {
                                // Run test cases for second submission
                                for (TestCase tc : testSuite.testSuite) {
                                    String resultText = runTestOnSubmission(secondSubmission, tc);
                                    outputV2.addResult2(new Result(tc.getName(), resultText));
                                }
                            }
                            outputV2.setHasSecondSubmission(true);
                            
                            // Remove from map to mark as processed
                            secondSubmissionMap.remove(folderName);
                        }
                    }
    
                    outputs.add(outputV2);
                }
    
                // Handle second submissions that don't have a matching first submission
                for (Map.Entry<String, File> entry : secondSubmissionMap.entrySet()) {
                    String folderName = entry.getKey();
                    File secondFolder = entry.getValue();
                    
                    // This is a second-only submission
                    File[] secondJavaFiles = secondFolder.listFiles(f -> f.isFile() && f.getName().endsWith(".java"));
                    if (secondJavaFiles == null || secondJavaFiles.length == 0) continue;
                    
                    String secondFileName = secondJavaFiles[0].getName();
                    OutputV2 outputV2 = new OutputV2(folderName);
                    
                    // Add empty results for first submission
                    for (TestCase tc : testSuite.testSuite) {
                        outputV2.addResult1(new Result(tc.getName(), "No first submission found"));
                    }
                    
                    // Process second submission
                    Submission secondSubmission = new Submission(folderName, secondFolder.getAbsolutePath(), secondFileName);
                    String secondCompileError = compileCode(secondSubmission);
                    
                    if (secondCompileError != null) {
                        for (TestCase tc : testSuite.testSuite) {
                            outputV2.addResult2(new Result(tc.getName(), "FAIL - Compilation Error: " + secondCompileError));
                        }
                    } else {
                        for (TestCase tc : testSuite.testSuite) {
                            String resultText = runTestOnSubmission(secondSubmission, tc);
                            outputV2.addResult2(new Result(tc.getName(), resultText));
                        }
                    }
                    outputV2.setHasSecondSubmission(true);
                    outputs.add(outputV2);
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
                // Use OutputViewerV2 instead of OutputViewer
                outputView = new OutputViewerV2(outputs);
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
    
    // Helper method to run a single test case on a submission
    private String runTestOnSubmission(Submission submission, TestCase tc) {
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
        return resultText;
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
    
    public void manageResults() {
        if (resultManager == null || !resultManager.isShowing()) {
            resultManager = new ResultManager();
            resultManager.initOwner(owner);
            resultManager.initModality(Modality.NONE);
            resultManager.setOnCloseRequest(e -> resultManager = null);
            resultManager.show();
        } else {
            resultManager.toFront();
        }
    }
}