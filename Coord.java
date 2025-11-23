import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.Stage;
import javafx.stage.Modality;
/***************************************************************************************
 * @title   The Coord class.
 *
 * @author  Alamin Adeleke, Chukwunonso Ekweaga,
 *          Aniekan Ekarika, Frances Felicidario
 * @version V1.0
 ***************************************************************************************/
public class Coord
{
    private final Stage owner;

    private TestSuiteManager suiteManager;
    private TestCaseManager caseManager;

    /***********************************************************************************
     * Constructor to initialize the coordinator with the main application window.
     *
     * @param owner the primary Stage owning all sub-windows
     ***********************************************************************************/
    public Coord(Stage owner)
    {
        this.owner = owner;
    }

    /***********************************************************************************
     * Method to trigger execution of test cases using provided folder paths.
     *
     * @param submissionFolder the folder containing student submissions
     * @param testSuiteFolder  the folder containing the selected test suite
         ***********************************************************************************/
    
    public void runTests(String submissionFolder, String testSuiteFolderName) {
        ArrayList<Output> outputs = new ArrayList<>();
        TestSuite testSuite = new TestSuite();
    
        try {
            // Get Test Suite Folder
            String userHome = System.getProperty("user.home");
            Path baseFolder = Paths.get(userHome, "Auto Code Marker");
            Path testSuiteFolder = baseFolder.resolve("Test Suites").resolve(testSuiteFolderName);
    
            if (!Files.exists(testSuiteFolder) || !Files.isDirectory(testSuiteFolder)) {
                throw new IOException("Test Suite folder not found: " + testSuiteFolder);
            }
    
            // Read .txt files and create TestCase objects
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
                // Get the only .java file inside this submission folder
                File[] javaFiles = folder.listFiles(f -> f.isFile() && f.getName().endsWith(".java"));
                if (javaFiles == null || javaFiles.length == 0) continue;
    
                String fileName = javaFiles[0].getName();
                Submission submission = new Submission(folder.getName(), folder.getAbsolutePath(), fileName);
    
                Output output = new Output(submission.name);
    
                // Compile submission
                String compileError = compileCode(submission);
                if (compileError != null) {
                    // compilation failed → fail all test cases
                    for (TestCase tc : testSuite.testSuite) {
                        output.addResult(new Result(tc.getName(), "FAIL - Compilation Error: " + compileError));
                    }
                    outputs.add(output);
                    continue;
                }
    
                // Run each test case
                for (TestCase tc : testSuite.testSuite) {
                    String resultText;
                    try {
                        String programOutput = runCode(submission, tc.getInput());
    
                        if (!programOutput.trim().equals(tc.getOutput().trim())) {
                            resultText = "FAIL - Expected: " + tc.getOutput() + ", Got: " + programOutput;
                        } else {
                            resultText = "PASS";
                        }
                    } catch (Exception e) {
                        resultText = "FAIL - Runtime Error: " + e.getMessage();
                    }
    
                    output.addResult(new Result(tc.getName(), resultText));
                }
    
                outputs.add(output);
            }
    
        } catch (Exception e) {
            // Don’t pretend it didn’t fail
            throw new RuntimeException("Error occurred while running tests: " + e.getMessage(), e);
        }
    
        OutputViewer outputView = new OutputViewer(outputs);
        outputView.initOwner(owner);
        outputView.initModality(Modality.NONE);
        outputView.setOnCloseRequest(e -> caseManager = null);
        outputView.show();        
    }
    
    /**
     * Compiles the submission code. Return null if successful, or the error if compilation failed.
     */
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
    
            process.waitFor();
    
            if (errorOutput.length() > 0) return errorOutput.toString().trim();
        } catch (Exception e) {
            return e.getMessage();
        }
        return null;
    }
    
    /**
     * Runs the compiled code with a given input. Returns program output as String.
     */
    private String runCode(Submission submission, String input) throws Exception {
        // Assuming the class name is the Java file name without .java
        String className = submission.fileName.replace(".java", "");
    
        ProcessBuilder pb = new ProcessBuilder("java", className);
        pb.directory(new File(submission.path));
        Process process = pb.start();
    
        // Write input
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        writer.write(input);
        writer.newLine();
        writer.flush();
        writer.close();
    
        // Read output
        BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = outputReader.readLine()) != null) {
            result.append(line).append("\n");
        }
    
        process.waitFor();
        return result.toString().trim();
    }


    /***********************************************************************************
     * Opens or brings forward the Test Suite Manager window.
     ***********************************************************************************/
    public void manageTestSuites()
    {
        if (suiteManager == null || !suiteManager.isShowing())
        {
            suiteManager = new TestSuiteManager();
            suiteManager.initOwner(owner);
            suiteManager.initModality(Modality.NONE);
            suiteManager.setOnCloseRequest(e -> suiteManager = null);
            suiteManager.show();
        }
        else
        {
            suiteManager.toFront();
        }
    }

    /***********************************************************************************
     * Opens or brings forward the Test Case Manager window.
     ***********************************************************************************/
    public void manageTestCases()
    {
        if (caseManager == null || !caseManager.isShowing())
        {
            caseManager = new TestCaseManager();
            caseManager.initOwner(owner);
            caseManager.initModality(Modality.NONE);
            caseManager.setOnCloseRequest(e -> caseManager = null);
            caseManager.show();
        }
        else
        {
            caseManager.toFront();
        }
    }
}
