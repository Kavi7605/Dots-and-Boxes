import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.effect.Glow;
import javafx.scene.effect.BlurType;

public class App extends Application {  
    private Stage primaryStage;
    private Scene mainMenuScene;
    private static int GRID_SIZE = 6;
    private static String player1Name = "Player 1";
    private static String player2Name = "Player 2";
    private static final int SPACING = 50;
    private static final int LINE_THICKNESS = 5;
    public Label infoLabel = new Label();

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
        primaryStage.getIcons().add(new Image("icon.png"));
    }

    private Scene createMainMenuScene() {
        BorderPane root = new BorderPane();
        VBox center = new VBox(30);
        center.setAlignment(Pos.CENTER);
        center.setStyle("-fx-background-color: linear-gradient(to bottom, #1a237e, #0d47a1);");

        Label titleLabel = new Label("Dots and Boxes");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.5)));
        
        Label subtitleLabel = new Label("A Classic Strategy Game");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        subtitleLabel.setTextFill(Color.rgb(255, 255, 255, 0.8));
        subtitleLabel.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.3)));

        Button playButton = new Button("Play Game");
        styleButton(playButton, "#4CAF50", "#388E3C");
        
        Button quitButton = new Button("Exit");
        styleButton(quitButton, "#F44336", "#D32F2F");
        
        playButton.setOnAction(event -> primaryStage.setScene(optionScene()));
        quitButton.setOnAction(event -> primaryStage.close());

        center.getChildren().addAll(titleLabel, subtitleLabel, playButton, quitButton);
        root.setCenter(center);

        return new Scene(root, screenSize.getWidth(), screenSize.getHeight());
    } 

    private Scene optionScene() {
        BorderPane root = new BorderPane();
        VBox vBox = new VBox(30);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-background-color: linear-gradient(to bottom, #1a237e, #0d47a1);");
        vBox.setPadding(new Insets(40));

        Label titleLabel = new Label("Game Settings");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.5)));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(30));

        Label player1Label = new Label("Player 1:");
        Label player2Label = new Label("Player 2:");
        Label gridSize = new Label("Grid Size:");
        
        styleLabel(player1Label);
        styleLabel(player2Label);
        styleLabel(gridSize);

        TextField p1Name = new TextField("");
        TextField p2Name = new TextField("");
        styleTextField(p1Name);
        styleTextField(p2Name);
        
        p1Name.setPromptText("Enter Player 1 Name");
        p2Name.setPromptText("Enter Player 2 Name");
        p1Name.textProperty().addListener((observable, oldValue, newValue) -> {
            player1Name = newValue;
        });
        p2Name.textProperty().addListener((observable, oldValue, newValue) -> {
            player2Name = newValue;
        });

        ComboBox<String> gridField = new ComboBox<>();
        gridField.getItems().addAll("4x4","5x5","6x6", "7x7", "8x8");
        gridField.setValue("6x6");
        styleComboBox(gridField);

        Button confirmButton = new Button("Start Game");
        styleButton(confirmButton, "#4CAF50", "#388E3C");
        confirmButton.setOnAction(event -> primaryStage.setScene(createGameBoardScene()));

        Button backButton = new Button("Back to Menu");
        styleButton(backButton, "#2196F3", "#1976D2");
        backButton.setOnAction(event -> primaryStage.setScene(mainMenuScene));

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(backButton, confirmButton);

        grid.add(player1Label, 0, 0);
        grid.add(player2Label, 0, 1);
        grid.add(gridSize, 0, 2);
        grid.add(p1Name, 1, 0);
        grid.add(p2Name, 1, 1);
        grid.add(gridField, 1, 2);

        vBox.getChildren().addAll(titleLabel, grid, buttonBox);
        root.setCenter(vBox);

        return new Scene(root, screenSize.getWidth(), screenSize.getHeight());
    }

    private void styleLabel(Label label) {
        label.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        label.setTextFill(Color.WHITE);
        label.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.3)));
    }

    private void styleTextField(TextField textField) {
        textField.setMaxWidth(250);
        textField.setAlignment(Pos.CENTER);
        textField.setStyle("-fx-font-size: 16px; -fx-padding: 12px; -fx-background-color: rgba(255, 255, 255, 0.9); " +
                         "-fx-text-fill: #1a237e; -fx-background-radius: 10; -fx-border-radius: 10;");
        textField.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.2)));
    }

    private void styleComboBox(ComboBox<String> comboBox) {
        comboBox.setMaxWidth(250);
        comboBox.setStyle("-fx-font-size: 16px; -fx-padding: 12px; -fx-background-color: rgba(255, 255, 255, 0.9); " +
                         "-fx-text-fill: #1a237e; -fx-background-radius: 10; -fx-border-radius: 10;");
        comboBox.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.2)));
    }

    private void styleButton(Button button, String color, String hoverColor) {
        button.setStyle("-fx-font-size: 18px; -fx-padding: 15px 30px; -fx-background-color: " + color + "; " +
                       "-fx-text-fill: white; -fx-background-radius: 25; -fx-font-weight: bold;");
        button.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.3)));
        
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: white; " +
                          "-fx-background-radius: 25; -fx-font-size: 18px; -fx-padding: 15px 30px; -fx-font-weight: bold;");
            button.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.4)));
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                          "-fx-background-radius: 25; -fx-font-size: 18px; -fx-padding: 15px 30px; -fx-font-weight: bold;");
            button.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.3)));
        });
    }

    private Scene createGameBoardScene() {
        hLines = new Line[GRID_SIZE][GRID_SIZE-1];
        vLines = new Line[GRID_SIZE-1][GRID_SIZE];
        boxes = new Rectangle[GRID_SIZE][GRID_SIZE];

        player1 = new Player(player1Name, Color.rgb(255, 85, 85));
        player2 = new Player(player2Name, Color.rgb(85, 170, 255));
        currentPlayer = player1;
        infoLabel.setText(currentPlayer.getName() + "'s turn");

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
        borderPane.setStyle("-fx-background-color: linear-gradient(to bottom, #1a237e, #0d47a1);");
        
        VBox topBar = new VBox(10);
        topBar.setPadding(new Insets(20));
        topBar.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2);");

        HBox playerInfo = new HBox(40);
        playerInfo.setAlignment(Pos.CENTER);

        VBox player1Box = new VBox(5);
        VBox player2Box = new VBox(5);
        
        Label player1Label = new Label(player1Name);
        Label player2Label = new Label(player2Name);
        styleLabel(player1Label);
        styleLabel(player2Label);
        
        player1ScoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        player2ScoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        player1ScoreLabel.setTextFill(player1.getColor());
        player2ScoreLabel.setTextFill(player2.getColor());
        
        player1Box.getChildren().addAll(player1Label, player1ScoreLabel);
        player2Box.getChildren().addAll(player2Label, player2ScoreLabel);
        
        infoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        infoLabel.setTextFill(Color.WHITE);
        infoLabel.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.3)));

        Button backButton = new Button("←");
        styleButton(backButton, "#2196F3", "#1976D2");
        backButton.setOnAction(event -> primaryStage.setScene(mainMenuScene));

        playerInfo.getChildren().addAll(backButton, player1Box, infoLabel, player2Box);
        topBar.getChildren().add(playerInfo);

        HBox gameBoard = new HBox();
        gameBoard.setAlignment(Pos.CENTER);
        gameBoard.getChildren().add(gridPane);

        borderPane.setTop(topBar);
        borderPane.setCenter(gameBoard);

        updateScores();

        return new Scene(borderPane, screenSize.getWidth(), screenSize.getHeight());
    }

    private void styleLine(Line line, boolean isLineUp) {
        line.setStroke(Color.TRANSPARENT);
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        line.setStrokeWidth(LINE_THICKNESS * 2);

        line.setOnMouseEntered(event -> {
            if (!lineDrawn.get(line)) {
                line.setStroke(currentPlayer.getColor());
                line.setEffect(new Glow(10));
            }
        });

        line.setOnMouseExited(event -> {
            if (!lineDrawn.get(line)) {
                line.setStroke(Color.TRANSPARENT);
                line.setEffect(null);
            }
        });

        line.setOnMouseClicked(event -> {
            if (lineDrawn.get(line)) {
                return;
            }

            line.setStroke(currentPlayer.getColor());
            line.setEffect(new Glow(5));
            lineDrawn.put(line, true);

            int boxCompleted = checkForCompletedBox(line, isLineUp);
            if (boxCompleted == 0) {
                currentPlayer = (currentPlayer == player1) ? player2 : player1;
                infoLabel.setText(currentPlayer.getName() + "'s turn");
                
                // Add animation for turn change
                FadeTransition fade = new FadeTransition(Duration.millis(300), infoLabel);
                fade.setFromValue(0.5);
                fade.setToValue(1.0);
                fade.play();
            }
            updateScores();
            boxCounter -= boxCompleted;
            
            if(boxCounter < 1) {
                String winMsg = "It was a tie.";
                if (player1.getScore() > player2.getScore()) {
                    winMsg = player1Name + " Wins!!";
                }
                if (player1.getScore() < player2.getScore()) {
                    winMsg = player2Name + " Wins!!";
                }
                
                Alert alert = new Alert(AlertType.INFORMATION, winMsg, new ButtonType("Back to Main Menu"));
                alert.setTitle("Game Over");
                alert.setHeaderText(null);
                alert.setContentText(winMsg);
                alert.getDialogPane().setStyle("-fx-background-color: #1a237e;");
                alert.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
                alert.showAndWait();
                primaryStage.setScene(mainMenuScene);
            }
        });
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