public class Result {
       // Instance data
       private String testCaseName;
       private String result;

       // No-arg constructor
       public Result() { }

       // Convenience constructor
       public Result(String testCaseName, String result) 
       {
              this.testCaseName = testCaseName;
              this.result = result;
       }

       // Getters and setters
       public String getTestCaseName()
       {
              return testCaseName;
       }

       public void setTestCaseName(String testCaseName) 
       {
              this.testCaseName = testCaseName;
       }

       public String getResult() {
              return result;
       }

       public void setResult(String result) 
       {
              this.result = result;
       }

       @Override
       public String toString() 
       {
              return "Result{testCaseName='" + testCaseName + "', result='" + result + "'}";
       }

}