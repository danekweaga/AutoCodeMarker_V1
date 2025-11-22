import java.util.ArrayList;
/***************************************************************************************
 * @title   The Output class.
 *
 * @author  Alamin Adeleke, Chuckwunonso Ekweaga,
 *          Aniekan Ekarika, Frances Felicidario
 * @version V1.0
 ***************************************************************************************/
public class Output
{
    // Instance data
    private ArrayList<Result> results;
    private String submissionName;

    /***********************************************************************************
     * Default constructor initializing storage for Result objects.
     ***********************************************************************************/
    public Output()
    {
        results = new ArrayList<>();
    }

    /***********************************************************************************
     * Adds a Result object to this Output's list of results.
     *
     * @param result the Result object to store
     ***********************************************************************************/
    public void addResult(Result result)
    {
        results.add(result);
    }

    /***********************************************************************************
     * Gets the name of the submission associated with this Output.
     *
     * @return the submission name
     ***********************************************************************************/
    public String getSubmissionName()
    {
        return submissionName;
    }

    /***********************************************************************************
     * Sets the submission name for this Output.
     *
     * @param submissionName the name to assign
     ***********************************************************************************/
    public void setSubmissionName(String submissionName)
    {
        this.submissionName = submissionName;
    }

    /***********************************************************************************
     * Gets all stored Result objects.
     *
     * @return the list of results
     ***********************************************************************************/
    public ArrayList<Result> getResults()
    {
        return results;
    }
}
