import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

public class App extends Application {  
    private Stage primaryStage;
    private Scene mainMenuScene, gameBoardScene;
    private static final int GRID_SIZE = 6;
    private static final int SPACING = 50;
    private static final int LINE_THICKNESS = 5;

    private String info = "Player 1's turn";
    public Label infoLabel = new Label(info);

    private Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

    Line[][] vLines, hLines;
    Rectangle[][] boxes;

    private Player player1 = new Player("Player 1", Color.RED);
    private Player player2 = new Player("Player 2", Color.BLUE);
    private Player currentPlayer = player1;

    private Map<Line, Boolean> lineDrawn = new HashMap<>(); //Line as key and true/false for marked or not
    private Map<Rectangle, Player> boxOwner = new HashMap<>();

    private Label player1ScoreLabel = new Label();
    private Label player2ScoreLabel = new Label();

    private int counter = 36;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;      
        // Create main menu scene
        mainMenuScene = createMainMenuScene();

        // Create game board scene
        gameBoardScene = createGameBoardScene();

        primaryStage.setTitle("Dots and Boxes"); 
        primaryStage.setScene(mainMenuScene);     
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private Scene createMainMenuScene() {
        BorderPane root = new BorderPane();
        VBox center = new VBox(20);
        center.setAlignment(Pos.CENTER);

        Button playButton = new Button("Play");
        playButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-color: #008000; -fx-text-fill: white;"); 
        playButton.setPrefWidth(100); 
        playButton.setPrefHeight(50);
        playButton.setOnAction(event -> primaryStage.setScene(gameBoardScene));

        Button quitButton = new Button("Quit");
        quitButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-color: #FF0000; -fx-text-fill: white;"); 
        quitButton.setPrefWidth(100); 
        quitButton.setPrefHeight(50);
        quitButton.setOnAction(event -> primaryStage.close());

        center.getChildren().addAll(playButton, quitButton);
        root.setCenter(center);

        return new Scene(root, screenSize.getWidth(), screenSize.getHeight());
    } 

    private Scene createGameBoardScene() {

        hLines = new Line[GRID_SIZE][GRID_SIZE-1];
        vLines = new Line[GRID_SIZE-1][GRID_SIZE];
        boxes = new Rectangle[GRID_SIZE][GRID_SIZE];

        player1 = new Player("Player 1", Color.RED);
        player2 = new Player("Player 2", Color.BLUE);
        currentPlayer = player1;

        lineDrawn = new HashMap<>(); 
        boxOwner = new HashMap<>();

        player1ScoreLabel = new Label();
        player2ScoreLabel = new Label();

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(LINE_THICKNESS+50, 0, 0, LINE_THICKNESS+50));

        int row, col;
        for (row = 0; row < GRID_SIZE; row++) {
            for (col = 0; col < GRID_SIZE; col++) {
                AnchorPane stack = new AnchorPane();

                //makes square
                if(col < GRID_SIZE-1 && row < GRID_SIZE-1)
                {
                    Rectangle square = new Rectangle(0, 0, SPACING, SPACING);
                    square.setFill(((row+col)%2==0)?Color.valueOf("#d6d6d6"):Color.valueOf("#bbbbbb"));
                    square.setStrokeWidth(0);
                    stack.getChildren().add(square);
                    boxes[row][col] = square;
                }

                //makes H line
                if(col < GRID_SIZE-1) {
                    Line lineUP = new Line(LINE_THICKNESS, 0, SPACING-LINE_THICKNESS, 0);
                    stack.getChildren().add(lineUP); //added this line as children of Stack, stack is the parent
                    styleLine(lineUP, true);
                    lineDrawn.put(lineUP, false);
                    hLines[row][col] = lineUP;
                }

                //makes v line
                if(row < GRID_SIZE-1) {
                    Line lineLeft = new Line(0, LINE_THICKNESS, 0, SPACING-LINE_THICKNESS);
                    stack.getChildren().add(lineLeft);
                    styleLine(lineLeft, false);
                    lineDrawn.put(lineLeft, false);
                    vLines[row][col] = lineLeft;
                }

                //makes dot
                Circle dot = new Circle(0, 0, LINE_THICKNESS*2);
                dot.setFill(Color.BLACK);
                stack.getChildren().add(dot);

                gridPane.add(stack, col, row); //stack is added at row/col index, so im getting those values from stack parent
            }
        }

        //gpt code
        // Create a border pane to hold the grid and scores
        BorderPane borderPane = new BorderPane();

        // Create a grid pane for the score labels
        GridPane scoreGrid = new GridPane();
        Label player1Label = new Label("Player 1: ");
        Label player2Label = new Label("Player 2: ");
        Button backButton = new Button("<");

        backButton.setStyle("-fx-font-size: 24px; -fx-padding: 10px 20px; -fx-background-color: #0000FF; -fx-text-fill: white;"); 
        backButton.setPrefWidth(100); 
        backButton.setPrefHeight(50);
        backButton.setOnAction(event -> primaryStage.setScene(mainMenuScene));

        scoreGrid.add(backButton, 0, 0);

        scoreGrid.add(player1Label, 2, 1);
        scoreGrid.add(player1ScoreLabel, 3, 1);

        scoreGrid.add(player2Label, 2, 2);
        scoreGrid.add(player2ScoreLabel, 3, 2);

        scoreGrid.add(infoLabel, 2, 3);
        infoLabel.setStyle("-fx-font-size: 20px;");

        player1Label.setStyle("-fx-font-size: 20px;");
        player2Label.setStyle("-fx-font-size: 20px;");
        player1ScoreLabel.setStyle("-fx-font-size: 20px;");
        player2ScoreLabel.setStyle("-fx-font-size: 20px;");

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(gridPane);

        borderPane.setTop(scoreGrid);
        borderPane.setCenter(hBox);

        updateScores();

        return new Scene(borderPane, screenSize.getWidth(), screenSize.getHeight());
    }

    private void styleLine(Line line, boolean isLineUp) {
        line.setStroke(Color.TRANSPARENT);
        //line.setStroke(Color.BLACK);
        line.setStrokeLineCap(StrokeLineCap.BUTT);
        line.setStrokeWidth(LINE_THICKNESS*2);
        //line.setStroke(lineUnmarkedColor);

        line.setOnMouseEntered(event -> {
            if (!lineDrawn.get(line)) {
                line.setStroke(Color.LIGHTGRAY);
            }
        });

        line.setOnMouseExited(event -> {
            if (!lineDrawn.get(line)) {
                line.setStroke(Color.TRANSPARENT);
            }
        });

        line.setOnMouseClicked(event -> {
            if (lineDrawn.get(line)) {
                return; // Skip if line is already filled
            }

            line.setStroke(Color.valueOf("#212121"));

            //puts this line inside hashmap as marked true
            lineDrawn.put(line, true);

            boolean boxCompleted = checkForCompletedBox(line, isLineUp);
            if (!boxCompleted) {
                // Switch turn if no box was completed
                currentPlayer = (currentPlayer == player1) ? player2 : player1;
                infoLabel.setText(currentPlayer.getName() + "'s turn");
            }
            updateScores(); //this just refreshes the scores
            if(counter == 0){
                
            }
        });
    }

    private boolean checkForCompletedBox(Line line, boolean isLineUp) {
        boolean boxCompleted = false;

        //this two line gets the Parent AnchorPane and then the ROw/Column location on Grid pane
        int row = GridPane.getRowIndex(line.getParent());
        int col = GridPane.getColumnIndex(line.getParent());

        if(isLineUp) { //if horizontal line
            if(row > 0) { //only runs if there is a box above
                //checks the 3 lines, left, above and right side
                if (lineDrawn.get(vLines[row-1][col]) && lineDrawn.get(hLines[row-1][col]) && lineDrawn.get(vLines[row-1][col+1])) {
                    //this is my boxes array that stores all the Rectangles
                    boxes[row-1][col].setFill(currentPlayer.getColor());
                    //boxOwner is hashmap, just storing the player for the box
                    boxOwner.put(boxes[row-1][col], currentPlayer);
                    currentPlayer.increaseScore(1); //adds 1 score
                    boxCompleted = true; //this boolean is from chatgpt code, i kept it as it is
                    counter--;
                }
            }
                    //we want both checks, so no else if
            if(row < GRID_SIZE-1) { //only runs if there is a box below
                //checks the 3 lines, left , below and right side
                if (lineDrawn.get(vLines[row][col]) && lineDrawn.get(hLines[row+1][col]) && lineDrawn.get(vLines[row][col+1])) {
                    boxes[row][col].setFill(currentPlayer.getColor());
                    boxOwner.put(boxes[row][col], currentPlayer);
                    currentPlayer.increaseScore(1);
                    boxCompleted = true;
                    counter--;
                }
            }
        }

        if(!isLineUp) { //if vertical line
            if(col > 0) {
                if (lineDrawn.get(hLines[row][col-1]) && lineDrawn.get(vLines[row][col-1]) && lineDrawn.get(hLines[row+1][col-1])) {
                    boxes[row][col-1].setFill(currentPlayer.getColor());
                    boxOwner.put(boxes[row][col-1], currentPlayer);
                    currentPlayer.increaseScore(1);
                    boxCompleted = true;
                    counter--;
                }
            }
            if(col < GRID_SIZE-1) {
                if (lineDrawn.get(hLines[row][col]) && lineDrawn.get(vLines[row][col+1]) && lineDrawn.get(hLines[row+1][col])) {
                    boxes[row][col].setFill(currentPlayer.getColor());
                    boxOwner.put(boxes[row][col], currentPlayer);
                    currentPlayer.increaseScore(1);
                    boxCompleted = true;
                    counter--;
                }
            }
        }
        return boxCompleted;
    }

    private void updateScores() {
        player1ScoreLabel.setText(String.valueOf(player1.getScore()));
        player2ScoreLabel.setText(String.valueOf(player2.getScore()));
    }
}
