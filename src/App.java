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
        Button newGame = new Button("New Game");
        Button continueButton = new Button("Continue");
        Button exitButton = new Button("Exit");

        // Set button size and style for 'New Game'
        newGame.setPrefWidth(150);
        newGame.setPrefHeight(50);
        newGame.setStyle("-fx-font-size: 16px; -fx-background-color: #4CAF50; -fx-text-fill: white;");

        // Set button size and style for 'Continue'
        continueButton.setPrefWidth(150);
        continueButton.setPrefHeight(50);
        continueButton.setStyle("-fx-font-size: 16px; -fx-background-color: #2196F3; -fx-text-fill: white;");

        // Set button size and style for 'Exit'
        exitButton.setPrefWidth(150);
        exitButton.setPrefHeight(50);
        exitButton.setStyle("-fx-font-size: 16px; -fx-background-color: #F44336; -fx-text-fill: white;");

        // Set actions for buttons
        newGame.setOnAction(e -> System.out.println("New Game clicked!"));
        continueButton.setOnAction(e -> System.out.println("Continue clicked!"));
        exitButton.setOnAction(e -> Platform.exit());

        // Add buttons to a VBox (vertical layout)
        VBox vbox = new VBox(20); // 20 is the spacing between the buttons
        vbox.setAlignment(Pos.CENTER); // Center all buttons
        vbox.getChildren().addAll(newGame, continueButton, exitButton);

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
        primaryStage.setFullScreen(true);

        // Set the scene to the primaryStage
        primaryStage.setScene(scene);
        
        // Show the primaryStage
        primaryStage.show();
    }

    /*private void newGameScreen(){
        Button backButton = new Button("<");
        backButton.setOnAction(event -> start(null));
        
    }*/
}
