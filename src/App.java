import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application {

    private static final int BOARD_SIZE = 5;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private Stage primaryStage;
    private Scene mainMenuScene, gameBoardScene;
    private int player1Score = 0;
    private int player2Score = 0;
    private int currentPlayer = 1;
    private int[][] lineStates = new int[BOARD_SIZE][BOARD_SIZE]; // 0 for unclaimed, 1 for Player 1, 2 for Player 2

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Dots and Boxes");
        primaryStage.setMaximized(true);

        // Create main menu scene
        mainMenuScene = createMainMenuScene();

        // Create game board scene
        gameBoardScene = createGameBoardScene();

        // Set initial scene
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }

    private Scene createMainMenuScene() {
        
        BorderPane root = new BorderPane();
        VBox center = new VBox(20);
        center.setAlignment(Pos.CENTER);

        Button playButton = new Button("Play");
        playButton.setOnAction(event -> primaryStage.setScene(gameBoardScene));

        Button quitButton = new Button("Quit");
        quitButton.setOnAction(event -> primaryStage.close());

        center.getChildren().addAll(playButton, quitButton);
        root.setCenter(center);

        return new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    Label player1ScoreLabel = new Label(String.valueOf(player1Score));
    Label player2ScoreLabel = new Label(String.valueOf(player2Score));

    private Scene createGameBoardScene() {
        BorderPane root = new BorderPane();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(5);
        grid.setVgap(5);

        Label player1NameLabel = new Label("Player 1:");
        player1NameLabel.setFont(new Font(20));
        grid.add(player1NameLabel, 0, 0);

        player1ScoreLabel.setFont(new Font(20));
        grid.add(player1ScoreLabel, 1, 0);

        Label player2NameLabel = new Label("Player 2:");
        player2NameLabel.setFont(new Font(20));
        grid.add(player2NameLabel, BOARD_SIZE + 1, 0);

        player2ScoreLabel.setFont(new Font(20));
        grid.add(player2ScoreLabel, BOARD_SIZE + 2, 0);

        // Create dots and lines
        for (int i = 0; i <= BOARD_SIZE; i++) {
            for (int j = 0; j <= BOARD_SIZE; j++) {
                Circle dot = new Circle(5);
                grid.add(dot, j, i);
                final int k = j;
                final int l = i;
                if (i < BOARD_SIZE) {
                    Line horizontalLine = new Line(0, 0, 50, 0);
                    horizontalLine.setStrokeWidth(5);
                    horizontalLine.setOnMouseClicked(event -> handleLineClick(k, l, true));
                    grid.add(horizontalLine, j, i );
                }

                if (j < BOARD_SIZE) {
                    Line verticalLine = new Line(0, 0, 0, 50);
                    verticalLine.setStrokeWidth(5);
                    verticalLine.setOnMouseClicked(event -> handleLineClick(k, l, false));
                    grid.add(verticalLine, j , i);
                }
            }
        }

        root.setCenter(grid);

        return new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    private void handleLineClick(int x, int y, boolean isHorizontal) {
        if (lineStates[x][y] !=0) {
            return; // Line is already claimed
        }

        // Claim the line for the current player
        lineStates[x][y] = currentPlayer;

        // Check if a box has been completed
        if (checkBoxCompletion(x, y, isHorizontal)) {
            updateScore(currentPlayer);
        }

        // Switch to the other player
        currentPlayer = 3 - currentPlayer;
    }

    private boolean checkBoxCompletion(int x, int y, boolean isHorizontal) {
        if (isHorizontal) {
            // Check if all four sides of the box are claimed
            return lineStates[x][y] == lineStates[x + 1][y] &&
                   lineStates[x][y] == lineStates[x][y - 1] &&
                   lineStates[x][y] == lineStates[x + 1][y - 1];
        } else {
            // Check if all four sides of the box are claimed
            return lineStates[x][y] == lineStates[x][y + 1] &&
                   lineStates[x][y] == lineStates[x - 1][y] &&
                   lineStates[x][y] == lineStates[x - 1][y + 1];
        }
    }

    private void updateScore(int player) {
        if (player == 1) {
            player1Score++;
            player1ScoreLabel.setText(String.valueOf(player1Score));
        } else {
            player2Score++;
            player2ScoreLabel.setText(String.valueOf(player2Score));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}