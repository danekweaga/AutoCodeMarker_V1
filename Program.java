/***************************************************************************************
 * @title   The Program class.
 * @desc    Compiles and runs single Java submissions with input support
 *
 * @author  Frances Felicidario
 * @version V1 11-22-25
 ***************************************************************************************/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Program
{
    // Instance data
    private String name;
    private File folder;

    /***********************************************************************************
     * Constructor to initialize a Program with its name and folder.
     *
     * @param name    the class name containing main
     * @param folder  the folder where the .java file is located
     ***********************************************************************************/
    public Program(String name, File folder)
    {
        this.name = name;
        this.folder = folder;
    }

    /***********************************************************************************
     * Gets the Program name (class name).
     *
     * @return the program name
     ***********************************************************************************/
    public String getName()
    {
        return name;
    }

    /***********************************************************************************
     * Gets the folder where this Program's source file resides.
     *
     * @return the folder File object
     ***********************************************************************************/
    public File getFolder()
    {
        return folder;
    }

    /***********************************************************************************
     * Compiles and runs this Program with the given input string.
     *
     * Assumes a single Java file "<name>.java" inside the folder and that this
     * file contains the main method.
     *
     * @param input  the input to send to the program's standard input
     * @return the program's standard output as a String
     * @throws IOException          if compilation or execution fails
     * @throws InterruptedException if the process is interrupted
     ***********************************************************************************/
    public String runWithInput(String input) throws IOException, InterruptedException
    {
        // Compile
        ProcessBuilder compileBuilder =
            new ProcessBuilder("javac", name + ".java");
        compileBuilder.directory(folder);
        Process compileProcess = compileBuilder.start();
        int compileExitCode = compileProcess.waitFor();

        if (compileExitCode != 0)
        {
            throw new IOException("Compilation failed for program: " + name);
        }

        // Run
        ProcessBuilder runBuilder =
            new ProcessBuilder("java", name);
        runBuilder.directory(folder);
        Process runProcess = runBuilder.start();

        // Send input
        try (BufferedWriter writer =
                 new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream())))
        {
            if (input != null && !input.isEmpty())
            {
                writer.write(input);
                writer.newLine();
            }
        }

        // Read output
        StringBuilder outputBuilder = new StringBuilder();
        try (BufferedReader reader =
                 new BufferedReader(new InputStreamReader(runProcess.getInputStream())))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                outputBuilder.append(line).append(System.lineSeparator());
            }
        }

        runProcess.waitFor();
        return outputBuilder.toString();
    }
}
