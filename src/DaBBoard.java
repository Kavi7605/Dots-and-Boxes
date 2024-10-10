import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class DaBBoard extends Pane {

    private int gridCountX, gridCountY;
    private double cellSize;
    private Line[][] horizontalLines, verticalLines;
    private DaBCell[][] cells;
    private Color[] playerColors;
    private int playerTurn = 0;
    private boolean switchPlayer = false;  // Flag to indicate a line completion

    public DaBBoard(int gridCountX, int gridCountY, double startX, double startY, double width, double height) {
        this.gridCountX = gridCountX;
        this.gridCountY = gridCountY;
        cellSize = width / (double) gridCountX;

        // Define player colors
        playerColors = new Color[]{Color.BLUE, Color.RED};

        // Initialize lines and cells
        horizontalLines = new Line[gridCountY][gridCountX - 1];
        verticalLines = new Line[gridCountY - 1][gridCountX];
        cells = new DaBCell[gridCountY][gridCountX];

        for (int y = 0; y < gridCountY; y++) {
            for (int x = 0; x < gridCountX; x++) {
                cells[y][x] = new DaBCell(x, y, cellSize, startX + x * cellSize, startY + y * cellSize);
                cells[y][x].setOnMouseClicked(e -> handleClick(x, y));
                getChildren().add(cells[y][x]);

                // Create horizontal lines
                if (x < gridCountX - 1) {
                    horizontalLines[y][x] = new Line(startX + x * cellSize, startY + y * cellSize, startX + (x + 1) * cellSize, startY + y * cellSize);
                    horizontalLines[y][x].setStrokeWidth(2);
                    getChildren().add(horizontalLines[y][x]);
                }

                // Create vertical lines
                if (y < gridCountY - 1) {
                    verticalLines[y][x] = new Line(startX + x * cellSize, startY + y * cellSize, startX + x * cellSize, startY + (y + 1) * cellSize);
                    verticalLines[y][x].setStrokeWidth(2);
                    getChildren().add(verticalLines[y][x]);
                }
            }
        }
    }

    private void handleClick(int x, int y) {
        if (!cells[y][x].isMarked()) {
            cells[y][x].markCell(playerColors[playerTurn]);
            checkCellMarked(cells[y][x], true);  // Check horizontal line completion
            checkCellMarked(cells[y][x], false); // Check vertical line completion

            // Check if a player has completed a box
            if (switchPlayer) {
                //App.Instance.updatePlayerScore(playerTurn);
                switchPlayer = false;  // Reset the flag
            }
        }
    }

    private void checkCellMarked(DaBCell dabCell, boolean isLineUp) {
        int x = dabCell.getX();
        int y = dabCell.getY();

        // Check horizontal line completion
        if (isLineUp) {
            if (x > 0 && cells[y][x - 1].isMarked() && !horizontalLines[y][x - 1].isVisible()) {
                horizontalLines[y][x - 1].setVisible(true);
                switchPlayer = true;  // Mark for player turn change
            }
            if (x < gridCountX - 1 && cells[y][x + 1].isMarked() && !horizontalLines[y][x].isVisible()) {
                horizontalLines[y][x].setVisible(true);
                switchPlayer = true;
            }
        } else {  // Check vertical line completion
            if (y > 0 && cells[y - 1][x].isMarked() && !verticalLines[y - 1][x].isVisible()) {
                verticalLines[y - 1][x].setVisible(true);
                switchPlayer = true;
            }
            if (y < gridCountY - 1 && cells[y + 1][x].isMarked() && !verticalLines[y][x].isVisible()) {
                verticalLines[y][x].setVisible(true);
                switchPlayer = true;
            }
        }
    }
}