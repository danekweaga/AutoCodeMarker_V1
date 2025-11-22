/**
 * Write a description of class TestCase here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class OutputViewer extends Stage {

    public OutputViewer() {
        StackPane root = new StackPane(new Label("Child window"));
        Scene scene = new Scene(root, 300, 200);

        setTitle("test suites");
        setScene(scene);
    }
}
