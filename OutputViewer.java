import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
/***************************************************************************************
 * @title   The OutputViewer class.
 *
 * @author  Alamin Adeleke, Chukwunonso Ekweaga,
 *          Aniekan Ekarika, Frances Felicidario
 * @version V1.0
 ***************************************************************************************/
public class OutputViewer extends Stage
{
    /***********************************************************************************
     * Default constructor that initializes and displays the OutputViewer window.
     ***********************************************************************************/
    public OutputViewer()
    {
        StackPane root = new StackPane(new Label("Child window"));
        Scene scene = new Scene(root, 300, 200);

        setTitle("test suites");
        setScene(scene);
    }
}
