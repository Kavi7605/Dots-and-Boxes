import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;

public class App extends Application {

    private Stage primaryStage;
    private int boardSize;
    private int numberOfPlayers;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showMainMenu();
    }

    // Main menu
    private void showMainMenu() {
        Button newGameButton = new Button("New Game");
        Button continueButton = new Button("Continue");
        Button exitButton = new Button("Exit");

        newGameButton.setStyle("-fx-font-size: 20px; -fx-pref-width: 200px; -fx-pref-height: 60px;");
        continueButton.setStyle("-fx-font-size: 20px; -fx-pref-width: 200px; -fx-pref-height: 60px;");
        exitButton.setStyle("-fx-font-size: 20px; -fx-pref-width: 200px; -fx-pref-height: 60px;");

        exitButton.setOnAction(event -> Platform.exit());
        newGameButton.setOnAction(event -> showGameSetupScreen());

        VBox menuLayout = new VBox(20);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.getChildren().addAll(newGameButton, continueButton, exitButton);

        Scene mainScene = new Scene(menuLayout, 800, 600);
        primaryStage.setScene(mainScene);
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("Main Menu");
        primaryStage.show();
    }

    // Game setup screen
    private void showGameSetupScreen() {
        BorderPane setupPane = new BorderPane();
        setupPane.setPadding(new Insets(20));

        // Back button to return to main menu
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> showMainMenu());

        VBox setupForm = new VBox(20);
        setupForm.setAlignment(Pos.CENTER);

        // Number of Players dropdown
        Label playersLabel = new Label("Number of Players:");
        ComboBox<String> playersDropdown = new ComboBox<>();
        playersDropdown.getItems().addAll("2", "3", "4");

        // Board Size dropdown
        Label boardSizeLabel = new Label("Board Size:");
        ComboBox<String> boardSizeDropdown = new ComboBox<>();
        boardSizeDropdown.getItems().addAll("Small (3x3)", "Medium (5x5)", "Large (7x7)");

        // Confirm button
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(event -> {
            // Retrieve selected values
            numberOfPlayers = Integer.parseInt(playersDropdown.getValue());
            String boardSizeSelected = boardSizeDropdown.getValue();
            
            // Set board size according to selection
            if (boardSizeSelected.equals("Small (3x3)")) {
                boardSize = 3;
            } else if (boardSizeSelected.equals("Medium (5x5)")) {
                boardSize = 5;
            } else if (boardSizeSelected.equals("Large (7x7)")) {
                boardSize = 7;
            }
            // Transition to Game screen
            showGameScreen();
        });

        setupForm.getChildren().addAll(playersLabel, playersDropdown, boardSizeLabel, boardSizeDropdown, confirmButton);
        setupPane.setTop(backButton);
        setupPane.setCenter(setupForm);

        Scene setupScene = new Scene(setupPane, 800, 600);
        primaryStage.setScene(setupScene);
        primaryStage.setFullScreen(true);
    }

    // Game screen that shows the grid of dots
    private void showGameScreen() {
        BorderPane gamePane = new BorderPane();

        // Create a grid of dots based on board size
        GridPane dotGrid = new GridPane();
        dotGrid.setAlignment(Pos.CENTER);
        dotGrid.setHgap(20);
        dotGrid.setVgap(20);

        // Add dots (represented as circles) to the grid
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Circle dot = new Circle(5);  // Dots with radius 5
                dotGrid.add(dot, col, row);
            }
        }

        // Display number of players
        Label playersInfo = new Label("Players: " + numberOfPlayers);
        playersInfo.setStyle("-fx-font-size: 20px;");

        // Back button to return to main menu
        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(event -> showMainMenu());

        VBox gameControls = new VBox(10);
        gameControls.setAlignment(Pos.TOP_CENTER);
        gameControls.getChildren().addAll(playersInfo, backButton);

        gamePane.setTop(gameControls);
        gamePane.setCenter(dotGrid);

        Scene gameScene = new Scene(gamePane, 800, 600);
        primaryStage.setScene(gameScene);
        primaryStage.setFullScreen(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
