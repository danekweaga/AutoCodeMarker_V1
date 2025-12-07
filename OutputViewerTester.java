import java.util.ArrayList;

public class OutputViewerTester {
    static String[] testCases;
    public static void main(String[] args) {
        ArrayList<OutputV2> outputs = new ArrayList<>();

        // 20 consistent test case names
        testCases = new String[20];
        for (int i = 0; i < 20; i++) {
            testCases[i] = "TestCase" + (i + 1);
        }

        // Define 10 submissions
        String[] submissionNames = {
            "Submission_A", "Submission_B", "Submission_C", "Submission_D",
            "Submission_E", "Submission_F", "Submission_G", "Submission_H",
            "Submission_I", "Submission_J"
        };

        // Statuses for result1
        String[][] result1Statuses = {
            { "PASS","FAIL","PASS","FAIL","PASS","FAIL","PASS","FAIL","PASS","FAIL",
              "PASS","FAIL","PASS","FAIL","PASS","FAIL","PASS","FAIL","PASS","FAIL" },
            { "FAIL","FAIL","PASS","PASS","FAIL","FAIL","PASS","PASS","FAIL","FAIL",
              "PASS","PASS","FAIL","FAIL","PASS","PASS","FAIL","FAIL","PASS","PASS" },
            { "COMPILE","COMPILE","COMPILE","COMPILE","COMPILE","COMPILE","COMPILE","COMPILE",
              "COMPILE","COMPILE","COMPILE","COMPILE","COMPILE","COMPILE","COMPILE","COMPILE",
              "COMPILE","COMPILE","COMPILE","COMPILE" },
            { "RUNTIME","RUNTIME","RUNTIME","RUNTIME","RUNTIME","RUNTIME","RUNTIME","RUNTIME",
              "RUNTIME","RUNTIME","RUNTIME","RUNTIME","RUNTIME","RUNTIME","RUNTIME","RUNTIME",
              "RUNTIME","RUNTIME","RUNTIME","RUNTIME" },
            { "PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS",
              "PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS" },
            { "FAIL","PASS","FAIL","PASS","FAIL","PASS","FAIL","PASS","FAIL","PASS",
              "FAIL","PASS","FAIL","PASS","FAIL","PASS","FAIL","PASS","FAIL","PASS" },
            { "PASS","FAIL","FAIL","PASS","PASS","FAIL","FAIL","PASS","PASS","FAIL",
              "FAIL","PASS","PASS","FAIL","FAIL","PASS","PASS","FAIL","FAIL","PASS" },
            { "COMPILE","COMPILE","COMPILE","COMPILE","COMPILE","COMPILE","COMPILE","COMPILE",
              "COMPILE","COMPILE","COMPILE","COMPILE","COMPILE","COMPILE","COMPILE","COMPILE",
              "COMPILE","COMPILE","COMPILE","COMPILE" },
            { "RUNTIME","RUNTIME","PASS","PASS","RUNTIME","RUNTIME","PASS","PASS","RUNTIME","RUNTIME",
              "PASS","PASS","RUNTIME","RUNTIME","PASS","PASS","RUNTIME","RUNTIME","PASS","PASS" },
            { "PASS","PASS","FAIL","FAIL","PASS","PASS","FAIL","FAIL","PASS","PASS",
              "FAIL","FAIL","PASS","PASS","FAIL","FAIL","PASS","PASS","FAIL","FAIL" }
        };

        // Statuses for result2 (some submissions have it, some don’t)
        String[][] result2Statuses = {
            { "PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS",
              "PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS" },
            { "FAIL","FAIL","FAIL","FAIL","FAIL","FAIL","FAIL","FAIL","FAIL","FAIL",
              "FAIL","FAIL","FAIL","FAIL","FAIL","FAIL","FAIL","FAIL","FAIL","FAIL" },
            { "PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS",
              "PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS" },
            { "COMPILE","COMPILE","COMPILE","COMPILE","COMPILE","COMPILE","COMPILE","COMPILE",
              "COMPILE","COMPILE","COMPILE","COMPILE","COMPILE","COMPILE","COMPILE","COMPILE",
              "COMPILE","COMPILE","COMPILE","COMPILE" },
            null, // no result2
            { "PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS",
              "PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS" },
            { "FAIL","FAIL","PASS","PASS","FAIL","FAIL","PASS","PASS","FAIL","FAIL",
              "PASS","PASS","FAIL","FAIL","PASS","PASS","FAIL","FAIL","PASS","PASS" },
            null, // no result2
            { "PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS",
              "PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS","PASS" },
            { "FAIL","FAIL","FAIL","FAIL","FAIL","FAIL","FAIL","FAIL","FAIL","FAIL",
              "FAIL","FAIL","FAIL","FAIL","FAIL","FAIL","FAIL","FAIL","FAIL","FAIL" }
        };

        // Build outputs
        for (int i = 0; i < submissionNames.length; i++) {
            OutputV2 out = new OutputV2(submissionNames[i]);
            addResults(out, result1Statuses[i], result2Statuses[i]);
            outputs.add(out);
        }

        // Show outputs
        OutputViewerV2 outputView = new OutputViewerV2(outputs);
        outputView.show();
    }
    
    // Helper to add results
    static void addResults(OutputV2 out, String[] r1, String[] r2) {
        for (int i = 0; i < r1.length; i++) {
            out.addResult1(new Result(testCases[i], r1[i]));
        }
        if (r2 != null) {
            for (int i = 0; i < r2.length; i++) {
                out.addResult2(new Result(testCases[i], r2[i]));
            }
        }
    }
}
