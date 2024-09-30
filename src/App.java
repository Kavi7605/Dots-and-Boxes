import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Layout type
        StackPane root = new StackPane();

        // Scene with a background color
        Scene scene = new Scene(root, Color.LIGHTSKYBLUE);

        // Adding buttons
        Button playButton = new Button("Play");
        Button quitButton = new Button("Quit");

        // Set button size and style for 'New Game'
        playButton.setPrefWidth(200);
        playButton.setPrefHeight(70);
        playButton.setStyle("-fx-font-size: 22px; -fx-background-color: #4CAF50; -fx-text-fill: white;");

        // Set button size and style for 'Quit'
        quitButton.setPrefWidth(200);
        quitButton.setPrefHeight(70);
        quitButton.setStyle("-fx-font-size: 22px; -fx-background-color: #F44336; -fx-text-fill: white;");

        // Set actions for buttons
        playButton.setOnAction(e -> System.out.println("New Game clicked!"));
        quitButton.setOnAction(e -> Platform.exit());

        // Add buttons to a VBox (vertical layout)
        VBox vbox = new VBox(20); // 20 is the spacing between the buttons
        vbox.setAlignment(Pos.CENTER); // Center all buttons
        vbox.getChildren().addAll(playButton, quitButton);

        // Add VBox to the root StackPane
        root.getChildren().add(vbox);

        // Set window title
        primaryStage.setTitle("Dots and Boxes");

        // Set window size
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);

        // Adding an icon
        Image icon = new Image("icon.png");
        primaryStage.getIcons().add(icon);

        // Set the window to fullscreen
        // primaryStage.setFullScreen(true);
        
        primaryStage.setMaximized(true);

        // Set the scene to the primaryStage
        primaryStage.setScene(scene);
        
        // Show the primaryStage
        primaryStage.show();
    }

    /*private void playButtonScreen(){
        Button backButton = new Button("<");
        backButton.setOnAction(event -> start(null));
    }*/
}
