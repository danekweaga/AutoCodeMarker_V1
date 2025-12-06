import java.util.ArrayList;

/***************************************************************************************
 * @title   The OutputV2 class.
 *
 * @author  Alamin Adeleke, Chukwunonso Ekweaga,
 *          Aniekan Ekarika, Frances Felicidario
 * @version V2.0
 ***************************************************************************************/
public class OutputV2
{
    // Instance data
    private ArrayList<Result> result1;
    private ArrayList<Result> result2;
    private boolean hasSecondSubmission;
    private String submissionName;

    /***********************************************************************************
     * Constructor initializing both result lists.
     *
     * @param submissionName the name associated with this OutputV2
     ***********************************************************************************/
    public OutputV2(String submissionName)
    {
        this.submissionName = submissionName;
        this.result1 = new ArrayList<>();
        this.result2 = new ArrayList<>();
        this.hasSecondSubmission = false;
    }

    /***********************************************************************************
     * Adds a Result to result1 list.
     *
     * @param result the Result object to store
     ***********************************************************************************/
    public void addResult1(Result result)
    {
        result1.add(result);
    }

    /***********************************************************************************
     * Adds a Result to result2 list and marks hasSecondSubmission as true.
     *
     * @param result the Result object to store
     ***********************************************************************************/
    public void addResult2(Result result)
    {
        result2.add(result);
        this.hasSecondSubmission = true;
    }

    /***********************************************************************************
     * Gets the name of the submission.
     *
     * @return the submission name
     ***********************************************************************************/
    public String getSubmissionName()
    {
        return submissionName;
    }

    /***********************************************************************************
     * Sets the submission name.
     *
     * @param submissionName the name to assign
     ***********************************************************************************/
    public void setSubmissionName(String submissionName)
    {
        this.submissionName = submissionName;
    }

    /***********************************************************************************
     * Checks if this OutputV2 has a second submission.
     *
     * @return true if result2 has been used, false otherwise
     ***********************************************************************************/
    public boolean hasSecondSubmission()
    {
        return hasSecondSubmission;
    }

    /***********************************************************************************
     * Manually set whether second submission exists.
     * (Useful if switching logic elsewhere)
     *
     * @param value the boolean value
     ***********************************************************************************/
    public void setHasSecondSubmission(boolean value)
    {
        this.hasSecondSubmission = value;
    }

    /***********************************************************************************
     * Gets result1 list.
     *
     * @return list of Result objects for first submission
     ***********************************************************************************/
    public ArrayList<Result> getResult1()
    {
        return result1;
    }

    /***********************************************************************************
     * Gets result2 list.
     *
     * @return list of Result objects for second submission
     ***********************************************************************************/
    public ArrayList<Result> getResult2()
    {
        return result2;
    }
}
