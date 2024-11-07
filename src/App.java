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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class App extends Application {  
    private Stage primaryStage;
    private Scene mainMenuScene;
    private static final int GRID_SIZE = 6;
    private static final int SPACING = 50;
    private static final int LINE_THICKNESS = 5;

    private String info = "Player 1's turn";
    public Label infoLabel = new Label(info);

    private Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

    Line[][] vLines, hLines;
    Rectangle[][] boxes;

    private Player player1 = new Player("Player 1", Color.rgb(255, 85, 85));
    private Player player2 = new Player("Player 2", Color.rgb(85, 170, 255));
    private Player currentPlayer = player1;

    private Map<Line, Boolean> lineDrawn = new HashMap<>();
    private Map<Rectangle, Player> boxOwner = new HashMap<>();

    private Label player1ScoreLabel = new Label();
    private Label player2ScoreLabel = new Label();

    private int boxCounter;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        mainMenuScene = createMainMenuScene();
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
        styleButton(playButton, "#32CD32", "#2E8B57");
        
        Button quitButton = new Button("Quit");
        styleButton(quitButton, "#FF4500", "#CD5C5C");
        
        playButton.setOnAction(event -> primaryStage.setScene(createGameBoardScene()));
        quitButton.setOnAction(event -> primaryStage.close());

        center.getChildren().addAll(playButton, quitButton);
        root.setCenter(center);

        return new Scene(root, screenSize.getWidth(), screenSize.getHeight());
    } 

    private Scene createGameBoardScene() {
        hLines = new Line[GRID_SIZE][GRID_SIZE-1];
        vLines = new Line[GRID_SIZE-1][GRID_SIZE];
        boxes = new Rectangle[GRID_SIZE][GRID_SIZE];

        player1 = new Player("Player 1", Color.rgb(255, 85, 85));
        player2 = new Player("Player 2", Color.rgb(85, 170, 255));
        currentPlayer = player1;

        lineDrawn = new HashMap<>();
        boxOwner = new HashMap<>();

        player1ScoreLabel = new Label();
        player2ScoreLabel = new Label();
        infoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        infoLabel.setTextFill(Color.DARKSLATEBLUE);

        boxCounter = (GRID_SIZE-1)*(GRID_SIZE-1);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(LINE_THICKNESS+50, 0, 0, LINE_THICKNESS+50));

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                AnchorPane stack = new AnchorPane();
                if(col < GRID_SIZE-1 && row < GRID_SIZE-1) {
                    Rectangle square = new Rectangle(0, 0, SPACING, SPACING);
                    square.setFill(((row+col)%2==0)?Color.valueOf("#E6E6FA"):Color.valueOf("#D8BFD8"));
                    square.setStrokeWidth(0);
                    stack.getChildren().add(square);
                    boxes[row][col] = square;
                }

                if(col < GRID_SIZE-1) {
                    Line lineUP = new Line(LINE_THICKNESS, 0, SPACING-LINE_THICKNESS, 0);
                    styleLine(lineUP, true);
                    lineDrawn.put(lineUP, false);
                    hLines[row][col] = lineUP;
                    stack.getChildren().add(lineUP);
                }

                if(row < GRID_SIZE-1) {
                    Line lineLeft = new Line(0, LINE_THICKNESS, 0, SPACING-LINE_THICKNESS);
                    styleLine(lineLeft, false);
                    lineDrawn.put(lineLeft, false);
                    vLines[row][col] = lineLeft;
                    stack.getChildren().add(lineLeft);
                }

                Circle dot = new Circle(0, 0, LINE_THICKNESS*2);
                dot.setFill(Color.DARKSLATEBLUE);
                stack.getChildren().add(dot);

                gridPane.add(stack, col, row);
            }
        }

        BorderPane borderPane = new BorderPane();
        GridPane scoreGrid = new GridPane();

        Label player1Label = new Label("Player 1: ");
        Label player2Label = new Label("Player 2: ");
        
        Button backButton = new Button("<");
        styleButton(backButton, "#1E90FF", "#4682B4");
        
        backButton.setOnAction(event -> primaryStage.setScene(mainMenuScene));

        scoreGrid.add(backButton, 0, 0);
        scoreGrid.add(player1Label, 2, 1);
        scoreGrid.add(player1ScoreLabel, 3, 1);
        scoreGrid.add(player2Label, 2, 2);
        scoreGrid.add(player2ScoreLabel, 3, 2);
        scoreGrid.add(infoLabel, 2, 3);

        setLabelStyle(player1Label, player1ScoreLabel, player2Label, player2ScoreLabel);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(gridPane);

        borderPane.setTop(scoreGrid);
        borderPane.setCenter(hBox);

        updateScores();

        return new Scene(borderPane, screenSize.getWidth(), screenSize.getHeight());
    }

    private void styleButton(Button button, String color, String hoverColor) {
        button.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 10;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: white; -fx-background-radius: 10;-fx-font-size: 16px; -fx-padding: 10px 20px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 10;-fx-font-size: 16px; -fx-padding: 10px 20px;"));
        button.setEffect(new DropShadow());
    }

    private void styleLine(Line line, boolean isLineUp) {
        line.setStroke(Color.TRANSPARENT);
        line.setStrokeLineCap(StrokeLineCap.BUTT);
        line.setStrokeWidth(LINE_THICKNESS*2);

        line.setOnMouseEntered(event -> {
            if (!lineDrawn.get(line)) {
                line.setStroke(currentPlayer.getColor());
            }
        });

        line.setOnMouseExited(event -> {
            if (!lineDrawn.get(line)) {
                line.setStroke(Color.TRANSPARENT);
            }
        });

        line.setOnMouseClicked(event -> {
            if (lineDrawn.get(line)) {
                return;
            }

            line.setStroke(Color.valueOf("#696969"));
            lineDrawn.put(line, true);

            int boxCompleted = checkForCompletedBox(line, isLineUp);
            if (boxCompleted==0) {
                currentPlayer = (currentPlayer == player1) ? player2 : player1;
                infoLabel.setText(currentPlayer.getName() + "'s turn");
            }
            updateScores();
            boxCounter -= boxCompleted;
            if(boxCounter < 1){                
                String winMsg = "It was a tie.";
                if (player1.getScore()>player2.getScore()) {
                    winMsg = "Player 1 Wins!!";
                }
                if (player1.getScore()<player2.getScore()) {
                    winMsg = "Player 2 Wins!!";
                }
                
                Alert alert = new Alert(AlertType.INFORMATION, winMsg, new ButtonType("Back to Main Menu"));
                alert.setHeaderText(null);
                alert.setContentText("Game Over");
                alert.showAndWait();
                primaryStage.setScene(mainMenuScene);
            }
        });
    }

    private void setLabelStyle(Label player1Label, Label player1ScoreLabel, Label player2Label, Label player2ScoreLabel) {
        player1Label.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        player2Label.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        player1ScoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        player2ScoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        player1Label.setTextFill(player1.getColor());
        player2Label.setTextFill(player2.getColor());
        player1ScoreLabel.setTextFill(player1.getColor());
        player2ScoreLabel.setTextFill(player2.getColor());
    }

    private int checkForCompletedBox(Line line, boolean isLineUp) {
        int boxCompleted = 0;
        int row = GridPane.getRowIndex(line.getParent());
        int col = GridPane.getColumnIndex(line.getParent());

        if(isLineUp) {
            if(row > 0) {
                if (lineDrawn.get(vLines[row-1][col]) && lineDrawn.get(hLines[row-1][col]) && lineDrawn.get(vLines[row-1][col+1])) {
                    boxes[row-1][col].setFill(currentPlayer.getColor());
                    boxOwner.put(boxes[row-1][col], currentPlayer);
                    currentPlayer.increaseScore(1);
                    boxCompleted++;
                }
            }

            if(row < GRID_SIZE-1) {
                if (lineDrawn.get(vLines[row][col]) && lineDrawn.get(hLines[row+1][col]) && lineDrawn.get(vLines[row][col+1])) {
                    boxes[row][col].setFill(currentPlayer.getColor());
                    boxOwner.put(boxes[row][col], currentPlayer);
                    currentPlayer.increaseScore(1);
                    boxCompleted++;
                }
            }
        } else {
            if(col > 0) {
                if (lineDrawn.get(hLines[row][col-1]) && lineDrawn.get(vLines[row][col-1]) && lineDrawn.get(hLines[row+1][col-1])) {
                    boxes[row][col-1].setFill(currentPlayer.getColor());
                    boxOwner.put(boxes[row][col-1], currentPlayer);
                    currentPlayer.increaseScore(1);
                    boxCompleted++;
                }
            }

            if(col < GRID_SIZE-1) {
                if (lineDrawn.get(hLines[row][col]) && lineDrawn.get(vLines[row][col+1]) && lineDrawn.get(hLines[row+1][col])) {
                    boxes[row][col].setFill(currentPlayer.getColor());
                    boxOwner.put(boxes[row][col], currentPlayer);
                    currentPlayer.increaseScore(1);
                    boxCompleted++;
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
