import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

public class App extends Application {  
  private Stage primaryStage;
  private Scene mainMenuScene, gameBoardScene;  
  private static final int WINDOW_WIDTH = 800;
  private static final int WINDOW_HEIGHT = 600;
  private static final int GRID_SIZE = 5;
  private static final int SPACING = 50;
  private static final int LINE_THICKNESS = 5;
  private static final int DOT_RADIUS = 10;

  Line[][] vLines, hLines;
  Rectangle[][] boxes;

  private Player player1 = new Player("Player 1", Color.RED);
  private Player player2 = new Player("Player 2", Color.BLUE);
  private Player currentPlayer = player1;

  private Map<Line, Boolean> lineDrawn = new HashMap<>();
  //private Map<StackPane, Player> boxOwner1 = new HashMap<>();
  private Map<Rectangle, Player> boxOwner = new HashMap<>();

  private Label player1ScoreLabel = new Label();
  private Label player2ScoreLabel = new Label();

  public static void main(String[] args) {
      launch(args);
  }

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
  private Scene createGameBoardScene() {
    hLines = new Line[GRID_SIZE][GRID_SIZE-1];
    vLines = new Line[GRID_SIZE-1][GRID_SIZE];

    GridPane gridPane = new GridPane();
    gridPane.setPadding(new Insets(LINE_THICKNESS+50, 0, 0, LINE_THICKNESS+50));

    int row, col;
    for (row = 0; row < GRID_SIZE; row++) {
      for (col = 0; col < GRID_SIZE; col++) {
        AnchorPane stack = new AnchorPane();

        if(col < GRID_SIZE-1) {
          Line lineUP = new Line(LINE_THICKNESS*2, LINE_THICKNESS, SPACING-LINE_THICKNESS, LINE_THICKNESS);
          styleLine(lineUP);
          stack.getChildren().add(lineUP);
          lineDrawn.put(lineUP, false);
          hLines[row][col] = lineUP;
        }

        if(row < GRID_SIZE-1) {
          Line lineLeft = new Line(LINE_THICKNESS, LINE_THICKNESS*2, LINE_THICKNESS, SPACING-LINE_THICKNESS);
          styleLine(lineLeft);
          stack.getChildren().add(lineLeft);
          lineDrawn.put(lineLeft, false);
          vLines[row][col] = lineLeft;
        }

        Circle dot = new Circle(LINE_THICKNESS, LINE_THICKNESS, DOT_RADIUS);
        dot.setFill(Color.BLACK);
        stack.getChildren().add(dot);
        
        gridPane.add(stack, col, row);
      }
    }

    // Create a border pane to hold the grid and scores
    BorderPane borderPane = new BorderPane();
    borderPane.setCenter(gridPane);

    // Create a grid pane for the score labels
    GridPane scoreGrid = new GridPane();
    scoreGrid.add(new Label("Player 1: "), 0, 0);
    scoreGrid.add(player1ScoreLabel, 1, 0);
    scoreGrid.add(new Label("Player 2: "), 0, 1);
    scoreGrid.add(player2ScoreLabel, 1, 1);

    borderPane.setTop(scoreGrid);

    updateScores();
    return new Scene(borderPane, WINDOW_WIDTH, WINDOW_HEIGHT);
  }
  

  private void styleLine(Line line) {
    line.setStroke(Color.TRANSPARENT);
    //line.setStroke(Color.BLACK);
    line.setStrokeLineCap(StrokeLineCap.BUTT);
    line.setStrokeWidth(LINE_THICKNESS*2);
    //line.setStroke(lineUnmarkedColor);

    line.setOnMouseEntered(event -> {
      if (!lineDrawn.get(line)) {
        line.setStroke(Color.LIGHTGRAY);
        System.out.println("Mouse Entered");
      }
    });

    line.setOnMouseExited(event -> {
      if (!lineDrawn.get(line)) {
        line.setStroke(Color.TRANSPARENT);
        System.out.println("Mouse Exited");
      }
    });

    line.setOnMouseClicked(event -> {
      if (lineDrawn.get(line)) {
        return; // Skip if line is already filled
      }
      line.setStroke(currentPlayer.getColor());
      lineDrawn.put(line, true);
      System.out.println("Mouse Clicked");

      boolean boxCompleted = checkForCompletedBox(line);
      if (!boxCompleted) {
        // Switch turn if no box was completed
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
      }
      updateScores();
    });
  }

  private boolean checkForCompletedBox(Line line) {
    boolean boxCompleted = false;
    
    // Check for boxes around the line
    for (Rectangle stack : boxOwner.keySet()) {
      if (isBoxComplete(stack)) {
        boxOwner.put(stack, currentPlayer);
        currentPlayer.increaseScore(1);
        boxCompleted = true;
        System.out.println("Box Completed");
      }
    }
    return boxCompleted;
  }

  private boolean isBoxComplete(Rectangle stack) {
    int row = GridPane.getRowIndex(stack);
    int col = GridPane.getColumnIndex(stack);

    boolean top = isLineDrawn(row - 1, col);
    boolean bottom = isLineDrawn(row + 1, col);
    boolean left = isLineDrawn(row, col - 1);
    boolean right = isLineDrawn(row, col + 1);

    return top && bottom && left && right;
  }

  private boolean isLineDrawn(int row, int col) {
    // Check if the line at the given position is drawn
    for (Line line : lineDrawn.keySet()) {
      int lineRow = GridPane.getRowIndex(line);
      int lineCol = GridPane.getColumnIndex(line);
      if (row == lineRow && col == lineCol && lineDrawn.get(line)) {
          return true;
      }
    }
    return false;
  }

  private void updateScores() {
    player1ScoreLabel.setText(String.valueOf(player1.getScore()));
    player2ScoreLabel.setText(String.valueOf(player2.getScore()));
  }
}