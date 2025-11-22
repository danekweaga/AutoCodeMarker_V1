/***************************************************************************************
 * @title   The ListOfPrograms class.
 * @desc    Given the root submissions folder, the program finds each immediate subfolder,
 * assumes the folder name is the program/class name, checks that <name>.java exists inside
 * and creates a Program object per such subfolder.
 *
 * @author  Frances Felicidario
 * @version V1.0 11-22-25
 ***************************************************************************************/
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListOfPrograms
{
    // Instance data
    private ArrayList<Program> programs;

    /***********************************************************************************
     * Default constructor initializing an empty list of programs.
     ***********************************************************************************/
    public ListOfPrograms()
    {
        programs = new ArrayList<>();
    }

    /***********************************************************************************
     * Loads Program objects from the given root submissions folder.
     *
     * Each direct subfolder is treated as a separate submission, with the folder
     * name used as the Program name and expecting a <name>.java file inside.
     *
     * @param rootFolder the folder containing one subfolder per submission
     ***********************************************************************************/
    public void loadFromRootFolder(File rootFolder)
    {
        programs.clear();

        if (rootFolder == null || !rootFolder.exists() || !rootFolder.isDirectory())
        {
            return;
        }

        File[] subFolders = rootFolder.listFiles(File::isDirectory);
        if (subFolders == null)
        {
            return;
        }

        for (File sub : subFolders)
        {
            String programName = sub.getName();
            File javaFile = new File(sub, programName + ".java");

            if (javaFile.exists())
            {
                programs.add(new Program(programName, sub));
            }
        }
    }

    /***********************************************************************************
     * Returns an unmodifiable view of all loaded programs.
     *
     * @return list of Program objects
     ***********************************************************************************/
    public List<Program> getPrograms()
    {
        return Collections.unmodifiableList(programs);
    }
}
