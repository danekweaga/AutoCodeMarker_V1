/***************************************************************************************
 * @title   The TestSuiteFileService class.
 *
 * @author  Frances Felicidario
 * @version V1.0
 ***************************************************************************************/
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.DirectoryStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
 
public class TestSuiteFileService
{
    // Instance data
    private final Path baseTestSuitesFolder;

    /***********************************************************************************
     * Constructs the service using the given base "Test Suites" folder.
     *
     * @param baseTestSuitesFolder the folder containing all test suite folders
     ***********************************************************************************/
    public TestSuiteFileService(Path baseTestSuitesFolder)
    {
        this.baseTestSuitesFolder = baseTestSuitesFolder;
    }

    /***********************************************************************************
     * Returns the base "Test Suites" folder path.
     *
     * @return the base folder path
     ***********************************************************************************/
    public Path getBaseTestSuitesFolder()
    {
        return baseTestSuitesFolder;
    }

    /***********************************************************************************
     * Lists the names of all immediate subfolders under the base "Test Suites" folder.
     *
     * @return a list of test suite folder names
     * @throws IOException if the folder cannot be read
     ***********************************************************************************/
    public List<String> listTestSuiteNames() throws IOException
    {
        List<String> names = new ArrayList<>();

        if (!Files.exists(baseTestSuitesFolder))
        {
            Files.createDirectories(baseTestSuitesFolder);
        }

        try (DirectoryStream<Path> stream =
                 Files.newDirectoryStream(baseTestSuitesFolder, Files::isDirectory))
        {
            for (Path p : stream)
            {
                names.add(p.getFileName().toString());
            }
        }

        names.sort(String::compareToIgnoreCase);
        return names;
    }

    /***********************************************************************************
     * Loads all test cases from the given test suite folder.
     *
     * File format:
     *   line 1: name
     *   line 2: input
     *   line 3: expected output
     *   line 4: index (optional; if missing, index is assigned sequentially)
     *
     * @param suiteName the name of the test suite folder
     * @return a list of TestCase objects
     * @throws IOException if files cannot be read
     ***********************************************************************************/
    public List<TestCase> loadTestCases(String suiteName) throws IOException
    {
        List<TestCase> cases = new ArrayList<>();

        if (suiteName == null || suiteName.isEmpty())
        {
            return cases;
        }

        Path suiteFolder = baseTestSuitesFolder.resolve(suiteName);

        if (!Files.exists(suiteFolder) || !Files.isDirectory(suiteFolder))
        {
            return cases;
        }

        try (DirectoryStream<Path> stream =
                 Files.newDirectoryStream(suiteFolder, "*.txt"))
        {
            int fallbackIndex = 1;

            for (Path file : stream)
            {
                List<String> lines = Files.readAllLines(file);

                String name =
                    (lines.size() > 0) ? lines.get(0).trim() : file.getFileName().toString();
                String input =
                    (lines.size() > 1) ? lines.get(1) : "";
                String output =
                    (lines.size() > 2) ? lines.get(2) : "";

                int index;
                if (lines.size() > 3)
                {
                    try
                    {
                        index = Integer.parseInt(lines.get(3).trim());
                    }
                    catch (NumberFormatException e)
                    {
                        index = fallbackIndex;
                    }
                }
                else
                {
                    index = fallbackIndex;
                }

                cases.add(new TestCase(name, input, output, index));
                fallbackIndex++;
            }
        }

        // Sort by index so list view order is stable
        cases.sort(Comparator.comparingInt(TestCase::getIndex));
        return cases;
    }

    /***********************************************************************************
     * Saves the given list of test cases into the specified test suite folder.
     *
     * All existing ".txt" files in the folder are removed, and new ones are created as:
     *   tc1.txt, tc2.txt, ...
     *
     * File format:
     *   line 1: name
     *   line 2: input
     *   line 3: expected output
     *   line 4: index
     *
     * @param suiteName the test suite folder name
     * @param testCases the list of TestCase objects to save
     * @throws IOException if writing to disk fails
     ***********************************************************************************/
    public void saveTestCases(String suiteName, List<TestCase> testCases) throws IOException
    {
        if (suiteName == null || suiteName.isEmpty())
        {
            return;
        }

        Path suiteFolder = baseTestSuitesFolder.resolve(suiteName);
        Files.createDirectories(suiteFolder);

        // Delete existing .txt files
        try (DirectoryStream<Path> stream =
                 Files.newDirectoryStream(suiteFolder, "*.txt"))
        {
            for (Path file : stream)
            {
                Files.deleteIfExists(file);
            }
        }

        // Rewrite all files based on the current list
        int index = 1;
        for (TestCase tc : testCases)
        {
            tc.setIndex(index);

            Path file = suiteFolder.resolve("tc" + index + ".txt");
            List<String> lines = new ArrayList<>();
            lines.add(tc.getName());
            lines.add(tc.getInput());
            lines.add(tc.getOutput());
            lines.add(Integer.toString(tc.getIndex()));

            Files.write(file, lines);
            index++;
        }
    }
}
