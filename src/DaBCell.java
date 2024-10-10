import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class DaBCell extends Rectangle {

    private int x, y;
    private boolean marked = false;
    private Color cellColor;

    public DaBCell(int x, int y, double size, double startX, double startY) {
        super(size, size);
        this.x = x;
        this.y = y;
        setFill(Color.WHITE);
        setStroke(Color.BLACK);
        setStrokeWidth(2);
        setX(startX);
        setY(startY);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isMarked() {
        return marked;
    }

    public void markCell(Color color) {
        marked = true;
        cellColor = color;
        setFill(cellColor);
    }
}