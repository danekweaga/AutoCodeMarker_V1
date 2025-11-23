 

import javafx.application.Application;
import javafx.stage.Stage;
import java.util.ArrayList;
/***************************************************************************************
 * @title   The OutputViewerTest class.
 *
 * @author  Alamin Adeleke, Chuckwunonso Ekweaga,
 *          Aniekan Ekarika, Frances Felicidario
 * @version V1.0
 ***************************************************************************************/
public class OutputViewerTest extends Application
{
    /***********************************************************************************
     * Launches the JavaFX test application.
     *
     * @param args runtime arguments
     ***********************************************************************************/
    public static void main(String[] args)
    {
        launch(args);
    }

    /***********************************************************************************
     * Creates multiple sample Outputs with extensive dummy Result data and opens the OutputViewer.
     *
     * @param primaryStage the primary stage provided by JavaFX (unused as owner here)
     ***********************************************************************************/
    @Override
    public void start(Stage primaryStage)
    {
        // Create ArrayList of Output objects for testing
        ArrayList<Output> outputs = new ArrayList<>();

        // Build first dummy Output object with MANY results to demonstrate scrolling
        Output output1 = new Output();
        output1.setSubmissionName("Student1_AlgorithmSolution.java");
        output1.addResult(new Result("Test Case 1: Basic Input Validation", "PASS"));
        output1.addResult(new Result("Test Case 2: Empty Input Handling", "PASS"));
        output1.addResult(new Result("Test Case 3: Large Number Input", "FAIL"));
        output1.addResult(new Result("Test Case 4: Negative Number Input", "PASS"));
        output1.addResult(new Result("Test Case 5: Zero Value Input", "PASS"));
        output1.addResult(new Result("Test Case 6: Maximum Boundary Test", "FAIL"));
        output1.addResult(new Result("Test Case 7: Minimum Boundary Test", "PASS"));
        output1.addResult(new Result("Test Case 8: Floating Point Precision", "PASS"));
        output1.addResult(new Result("Test Case 9: String Input Rejection", "PASS"));
        output1.addResult(new Result("Test Case 10: Null Input Handling", "FAIL"));
        output1.addResult(new Result("Test Case 11: Array Sorting", "PASS"));
        output1.addResult(new Result("Test Case 12: Search Algorithm", "PASS"));
        output1.addResult(new Result("Test Case 13: Memory Efficiency", "FAIL"));
        output1.addResult(new Result("Test Case 14: Time Complexity", "PASS"));
        output1.addResult(new Result("Test Case 15: Space Complexity", "PASS"));
        output1.addResult(new Result("Test Case 16: Recursive Function", "FAIL"));
        output1.addResult(new Result("Test Case 17: Iterative Solution", "PASS"));
        output1.addResult(new Result("Test Case 18: Edge Case - Single Element", "PASS"));
        output1.addResult(new Result("Test Case 19: Edge Case - Two Elements", "PASS"));
        output1.addResult(new Result("Test Case 20: Large Dataset (10k elements)", "FAIL"));
        output1.addResult(new Result("Test Case 21: Duplicate Values", "PASS"));
        output1.addResult(new Result("Test Case 22: Sorted Input", "PASS"));
        output1.addResult(new Result("Test Case 23: Reverse Sorted Input", "PASS"));
        output1.addResult(new Result("Test Case 24: Random Input", "PASS"));
        output1.addResult(new Result("Test Case 25: Performance Under Load", "FAIL"));
        outputs.add(output1);

        // Build second dummy Output object with comprehensive test suite
        Output output2 = new Output();
        output2.setSubmissionName("Student2_DataStructure.java");
        output2.addResult(new Result("Unit Test 1: Constructor Initialization", "PASS"));
        output2.addResult(new Result("Unit Test 2: Add Element", "PASS"));
        output2.addResult(new Result("Unit Test 3: Remove Element", "PASS"));
        output2.addResult(new Result("Unit Test 4: Find Element", "PASS"));
        output2.addResult(new Result("Unit Test 5: Empty Collection", "PASS"));
        output2.addResult(new Result("Unit Test 6: Single Element", "PASS"));
        output2.addResult(new Result("Unit Test 7: Multiple Elements", "PASS"));
        output2.addResult(new Result("Unit Test 8: Duplicate Elements", "FAIL"));
        output2.addResult(new Result("Unit Test 9: Memory Leak Check", "PASS"));
        output2.addResult(new Result("Unit Test 10: Iterator Functionality", "PASS"));
        output2.addResult(new Result("Unit Test 11: Clear Method", "PASS"));
        output2.addResult(new Result("Unit Test 12: Size Method", "PASS"));
        output2.addResult(new Result("Unit Test 13: Contains Method", "PASS"));
        output2.addResult(new Result("Unit Test 14: Index Out of Bounds", "FAIL"));
        output2.addResult(new Result("Unit Test 15: Null Element Handling", "PASS"));
        output2.addResult(new Result("Integration Test 1: With Database", "PASS"));
        output2.addResult(new Result("Integration Test 2: With UI Component", "FAIL"));
        output2.addResult(new Result("Integration Test 3: With Network Module", "PASS"));
        output2.addResult(new Result("Performance Test 1: 1000 operations", "PASS"));
        output2.addResult(new Result("Performance Test 2: 10000 operations", "FAIL"));
        output2.addResult(new Result("Performance Test 3: Memory Usage", "PASS"));
        outputs.add(output2);

        // Build third dummy Output object with various test types
        Output output3 = new Output();
        output3.setSubmissionName("Student3_WebApplication.java");
        output3.addResult(new Result("Authentication: Login Valid Credentials", "PASS"));
        output3.addResult(new Result("Authentication: Login Invalid Credentials", "PASS"));
        output3.addResult(new Result("Authentication: Password Encryption", "PASS"));
        output3.addResult(new Result("Authentication: Session Management", "FAIL"));
        output3.addResult(new Result("Database: Connection Pool", "PASS"));
        output3.addResult(new Result("Database: CRUD Operations", "PASS"));
        output3.addResult(new Result("Database: Transaction Rollback", "PASS"));
        output3.addResult(new Result("Database: SQL Injection Prevention", "PASS"));
        output3.addResult(new Result("API: GET Request", "PASS"));
        output3.addResult(new Result("API: POST Request", "PASS"));
        output3.addResult(new Result("API: PUT Request", "PASS"));
        output3.addResult(new Result("API: DELETE Request", "FAIL"));
        output3.addResult(new Result("API: Error Handling", "PASS"));
        output3.addResult(new Result("API: Rate Limiting", "PASS"));
        output3.addResult(new Result("UI: Page Load Time", "PASS"));
        output3.addResult(new Result("UI: Responsive Design", "FAIL"));
        output3.addResult(new Result("UI: Cross-browser Compatibility", "PASS"));
        output3.addResult(new Result("Security: XSS Prevention", "PASS"));
        output3.addResult(new Result("Security: CSRF Protection", "PASS"));
        output3.addResult(new Result("Security: Data Validation", "PASS"));
        output3.addResult(new Result("Performance: Load Testing", "FAIL"));
        output3.addResult(new Result("Performance: Stress Testing", "PASS"));
        output3.addResult(new Result("Performance: Endurance Testing", "PASS"));
        outputs.add(output3);

        // Build fourth dummy Output object with mixed results
        Output output4 = new Output();
        output4.setSubmissionName("Student4_MachineLearning.py");
        output4.addResult(new Result("Data Preprocessing: Missing Values", "PASS"));
        output4.addResult(new Result("Data Preprocessing: Normalization", "PASS"));
        output4.addResult(new Result("Data Preprocessing: Feature Scaling", "PASS"));
        output4.addResult(new Result("Model Training: Convergence", "PASS"));
        output4.addResult(new Result("Model Training: Overfitting Check", "FAIL"));
        output4.addResult(new Result("Model Training: Underfitting Check", "PASS"));
        output4.addResult(new Result("Model Evaluation: Accuracy Score", "PASS"));
        output4.addResult(new Result("Model Evaluation: Precision", "PASS"));
        output4.addResult(new Result("Model Evaluation: Recall", "PASS"));
        output4.addResult(new Result("Model Evaluation: F1 Score", "PASS"));
        output4.addResult(new Result("Model Evaluation: ROC Curve", "FAIL"));
        output4.addResult(new Result("Cross Validation: K-Fold", "PASS"));
        output4.addResult(new Result("Cross Validation: Stratified", "PASS"));
        output4.addResult(new Result("Hyperparameter Tuning: Grid Search", "PASS"));
        output4.addResult(new Result("Hyperparameter Tuning: Random Search", "PASS"));
        output4.addResult(new Result("Feature Importance: Analysis", "PASS"));
        output4.addResult(new Result("Prediction: Batch Processing", "PASS"));
        output4.addResult(new Result("Prediction: Real-time", "FAIL"));
        output4.addResult(new Result("Deployment: Model Serialization", "PASS"));
        output4.addResult(new Result("Deployment: API Integration", "PASS"));
        outputs.add(output4);

        // Build fifth dummy Output object - perfect score
        Output output5 = new Output();
        output5.setSubmissionName("Student5_PerfectSolution.java");
        output5.addResult(new Result("Basic Functionality Test 1", "PASS"));
        output5.addResult(new Result("Basic Functionality Test 2", "PASS"));
        output5.addResult(new Result("Basic Functionality Test 3", "PASS"));
        output5.addResult(new Result("Edge Case Test 1", "PASS"));
        output5.addResult(new Result("Edge Case Test 2", "PASS"));
        output5.addResult(new Result("Edge Case Test 3", "PASS"));
        output5.addResult(new Result("Performance Test 1", "PASS"));
        output5.addResult(new Result("Performance Test 2", "PASS"));
        output5.addResult(new Result("Performance Test 3", "PASS"));
        output5.addResult(new Result("Memory Test 1", "PASS"));
        output5.addResult(new Result("Memory Test 2", "PASS"));
        output5.addResult(new Result("Integration Test 1", "PASS"));
        output5.addResult(new Result("Integration Test 2", "PASS"));
        output5.addResult(new Result("Security Test 1", "PASS"));
        output5.addResult(new Result("Security Test 2", "PASS"));
        output5.addResult(new Result("Final Comprehensive Test", "PASS"));
        outputs.add(output5);

        // Build sixth dummy Output object - mostly failing
        Output output6 = new Output();
        output6.setSubmissionName("Student6_NeedsWork.cpp");
        output6.addResult(new Result("Compilation Test", "PASS"));
        output6.addResult(new Result("Basic Test 1", "FAIL"));
        output6.addResult(new Result("Basic Test 2", "FAIL"));
        output6.addResult(new Result("Basic Test 3", "FAIL"));
        output6.addResult(new Result("Input Validation", "FAIL"));
        output6.addResult(new Result("Output Format", "PASS"));
        output6.addResult(new Result("Memory Management", "FAIL"));
        output6.addResult(new Result("Exception Handling", "FAIL"));
        output6.addResult(new Result("Boundary Conditions", "FAIL"));
        output6.addResult(new Result("Performance", "FAIL"));
        output6.addResult(new Result("Code Quality", "FAIL"));
        output6.addResult(new Result("Documentation", "PASS"));
        outputs.add(output6);

        // Show the OutputViewer window with the ArrayList of outputs
        OutputViewer viewer = new OutputViewer(outputs);
        viewer.setTitle("Auto Code Marker - Test Results Viewer");
        viewer.show();
    }
}