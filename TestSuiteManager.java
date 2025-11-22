// ChildWindow.java
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class TestSuiteManager extends Stage {

    public TestSuiteManager() {
        StackPane root = new StackPane(new Label("Child window"));
        Scene scene = new Scene(root, 300, 200);

        setTitle("test suites");
        setScene(scene);
    }
}
