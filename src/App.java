import javafx.application.Application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();

        Line line1 = new Line(40, 100, 80, 100);        
        Circle circ1 = new Circle(40, 40, 30);
        circ1.setFill(Color.RED);

        Circle circ2 = new Circle(60, 40, 30);
        circ2.setFill(Color.BLUE);

        root.getChildren().add(circ1);
        root.getChildren().add(circ2);
        root.getChildren().add(line1);
        Scene scene = new Scene(root, 400, 300);

        stage.setTitle("My JavaFX Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}