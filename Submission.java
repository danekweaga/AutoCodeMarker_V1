
/**
 * Write a description of class Submission here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Submission
{
    String name;
    String path;
    String fileName;
    
    public Submission(String name, String path, String fileName)
    {
        this.path = path;
        this.name = name;
        this.fileName = fileName;
    }
}