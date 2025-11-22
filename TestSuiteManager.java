import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
/***************************************************************************************
 * @title   The TestSuiteManager class.
 *
 * @author  Alamin Adeleke, Chuckwunonso Ekweaga,
 *          Aniekan Ekarika, Frances Felicidario
 * @version V1.0
 ***************************************************************************************/
public class TestSuiteManager extends Stage
{
    /***********************************************************************************
     * Default constructor that initializes the TestSuiteManager window.
     ***********************************************************************************/
    public TestSuiteManager()
    {
        StackPane root = new StackPane(new Label("Child window"));
        Scene scene = new Scene(root, 300, 200);

        setTitle("test suites");
        setScene(scene);
    }
}
