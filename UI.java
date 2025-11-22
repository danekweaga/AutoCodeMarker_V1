import javafx.application.Application;
import javafx.stage.Stage;

/***************************************************************************************
 * Write a description of class UI here.
 *
 * @author Alamin Adeleke, Chuckwunonso Ekweaga, Aniekan Ekarika, Frances Felicidario
 * @version 1
 ***************************************************************************************/
public class UI extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) 
    {
        
    }
    
    // ----------------- Empty Handlers (to implement later) -----------------

    private void manageTestCases() {
        // TODO: implement Manage TestCases logic
        System.out.println("Manage TestCases clicked.");
    }

    private void manageTestSuite() {
        // TODO: implement Manage TestSuite logic
        System.out.println("Manage TestSuite clicked.");
    }

    private void runTestCases() {
        // TODO: implement Run Test Cases logic
        System.out.println("Run Test Cases clicked.");
    }

}