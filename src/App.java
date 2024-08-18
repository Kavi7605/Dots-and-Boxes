import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        /*// Load background image
        Image backgroundImage = new Image("file:background.jpg"); // Adjust the path to your image file
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true);
        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
        Background background = new Background(backgroundImg); */   
        
        // Create buttons
        Button newGameButton = new Button("New Game");
        Button continueButton = new Button("Continue");
        Button exitButton = new Button("Exit");
        
        // Set button styles
        newGameButton.setStyle("-fx-font-size: 20px; -fx-pref-width: 200px; -fx-pref-height: 60px;");
        continueButton.setStyle("-fx-font-size: 20px; -fx-pref-width: 200px; -fx-pref-height: 60px;");
        exitButton.setStyle("-fx-font-size: 20px; -fx-pref-width: 200px; -fx-pref-height: 60px;");
        
        // Arrange buttons in a layout
        TilePane buttonPane = new TilePane();
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.setHgap(10);
        buttonPane.setVgap(10);
        buttonPane.getChildren().addAll(newGameButton, continueButton, exitButton);
        
        // Create root pane and add background and button pane
        StackPane root = new StackPane();
        //root.setBackground(background);
        root.getChildren().add(buttonPane);

        // Create scene and set it on the stage
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("Fullscreen Window");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
