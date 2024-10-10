import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class TweakerUI extends HBox {

    private boolean isLineUp, isLine=false;
    public Node selectedItem;

    private ComboBox<BlendMode> cmbBox;
    private ColorPicker inColorPicker, outColorPicker;
    private Slider sldInRadius, sldInChoke, sldInDistance, sldOutRadius, sldOutChoke, sldOutDistance;

    TweakerUI(AnchorPane root) {
    
        root.setOnMouseClicked(e -> {
            //System.out.println("Target: " + e.getTarget().toString());
            //System.out.println("Class Type: " + e.getTarget().getClass().getSimpleName());

            selectedItem = (Node)e.getTarget();
            if(selectedItem.getClass().getSimpleName().equals("Line")) {
                isLine = true;
                Line line = (Line)selectedItem;
                if(line.getEndY() == line.getStartY()) isLineUp=true;
                else if(line.getEndX() == line.getStartX()) isLineUp=false;
                else isLine = false;
            }
            else {
                isLine = false;
            }
            updateEffect();
        });

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(30, 10, 0, 10));

        TextField txtBoardWidth = new TextField();
        txtBoardWidth.setPromptText("Board Width");
        TextField txtBoardHeight = new TextField();
        txtBoardHeight.setPromptText("Board Height");

        TextField txtBoxGridX = new TextField();
        txtBoxGridX.setPromptText("Grid X");
        TextField txtBoxGridY = new TextField();
        txtBoxGridY.setPromptText("Grid Y");

        Button btnNewBoard = new Button("Make New Board");
        btnNewBoard.setOnAction(e -> {
            try {
                int boardWidth = Integer.parseInt(txtBoardWidth.getText());
                int boardHeight = Integer.parseInt(txtBoardHeight.getText());

                int gridSizeX = Integer.parseInt(txtBoxGridX.getText());
                int gridSizeY = Integer.parseInt(txtBoxGridY.getText());

                App.Instance.updateDabBoard(gridSizeX, gridSizeY, 0, 0, boardWidth, boardHeight);
            }
            catch(Exception excp) {
                System.out.println("Wring Grid Size." + excp.getMessage());
            }
        });

        vbox.getChildren().addAll(txtBoardWidth, txtBoardHeight, txtBoxGridX, txtBoxGridY, btnNewBoard);

        cmbBox = new ComboBox<>();
        cmbBox.getItems().addAll(
            BlendMode.SRC_OVER,
            BlendMode.SRC_ATOP,
            BlendMode.ADD,
            BlendMode.MULTIPLY,
            BlendMode.SCREEN,
            BlendMode.OVERLAY,
            BlendMode.DARKEN,
            BlendMode.LIGHTEN,
            BlendMode.COLOR_DODGE,
            BlendMode.COLOR_BURN,
            BlendMode.HARD_LIGHT,
            BlendMode.SOFT_LIGHT,
            BlendMode.DIFFERENCE,
            BlendMode.EXCLUSION,
            BlendMode.RED,
            BlendMode.GREEN,
            BlendMode.BLUE
        );
        cmbBox.setValue(BlendMode.OVERLAY);
        cmbBox.setOnAction(e -> updateEffect());

        inColorPicker = new ColorPicker(Color.WHITE);
        inColorPicker.setOnAction(e -> updateEffect());

        sldInRadius = new Slider(0, 20, 10);
        Label lblInRadius = new Label("Inner Radius: " + sldInRadius.getValue());
        sldInRadius.setOnMouseDragged(e -> {
            lblInRadius.setText("Inner Radius: " + (String.format("%.2f",sldInRadius.getValue())));
            updateEffect();
        });

        sldInChoke = new Slider(0, 1, 0.2);
        Label lblInChoke = new Label("Inner Choke: " + sldInChoke.getValue());
        sldInChoke.setOnMouseDragged(e -> {
            lblInChoke.setText("Inner Choke: " + (String.format("%.2f",sldInChoke.getValue())));
            updateEffect();
        });

        sldInDistance = new Slider(0, 5, 3);
        Label lblInDistance = new Label("Inner Distance: " + sldInDistance.getValue());
        sldInDistance.setOnMouseDragged(e -> {
            lblInDistance.setText("Inner Distance: " + (String.format("%.2f",sldInDistance.getValue())));
            updateEffect();
        });

        vbox.getChildren().addAll(cmbBox,
            inColorPicker, 
            lblInRadius, sldInRadius, 
            lblInChoke, sldInChoke, 
            lblInDistance, sldInDistance
        );


        outColorPicker = new ColorPicker(Color.BLACK);
        outColorPicker.setOnAction(e -> updateEffect());

        sldOutRadius = new Slider(0, 20, 8);
        Label lblOutRadius = new Label("Outer Radius: " + sldOutRadius.getValue());
        sldOutRadius.setOnMouseDragged(e -> {
            lblOutRadius.setText("Outer Radius: " + (String.format("%.2f",sldOutRadius.getValue())));
            updateEffect();
        });

        sldOutChoke = new Slider(0, 1, 0.2);
        Label lblOutChoke = new Label("Outer Spread: " + sldOutChoke.getValue());
        sldOutChoke.setOnMouseDragged(e -> {
            lblOutChoke.setText("Outer Choke: " + (String.format("%.2f",sldOutChoke.getValue())));
            updateEffect();
        });

        sldOutDistance = new Slider(0, 5, 3);
        Label lblOutDistance = new Label("Outer Distance: " + sldOutDistance.getValue());
        sldOutDistance.setOnMouseDragged(e -> {
            lblOutDistance.setText("Outer Distance: " + (String.format("%.2f",sldOutDistance.getValue())));
            updateEffect();
        });

        vbox.getChildren().addAll(
            outColorPicker, 
            lblOutRadius, sldOutRadius, 
            lblOutChoke, sldOutChoke, 
            lblOutDistance, sldOutDistance
        );

        this.getChildren().addAll(vbox, root);
    }

    public void updateEffect() {
        double innerOffsetX = (isLine && isLineUp) ? 0 : sldInDistance.getValue();
        double innerOffsetY = (isLine && !isLineUp) ? 0 : sldInDistance.getValue();
        double outerOffsetX = (isLine && isLineUp) ? 0 : sldOutDistance.getValue();
        double outerOffsetY = (isLine && !isLineUp) ? 0 : sldOutDistance.getValue();

        Blend bgBlend = new Blend(cmbBox.getValue());
        bgBlend.setTopInput(new InnerShadow(BlurType.GAUSSIAN, inColorPicker.getValue(), sldInRadius.getValue(), sldInChoke.getValue(), innerOffsetX, innerOffsetY));
        bgBlend.setBottomInput(new DropShadow(BlurType.GAUSSIAN, outColorPicker.getValue(), sldOutRadius.getValue(), sldOutChoke.getValue(), outerOffsetX, outerOffsetY));
        selectedItem.setEffect(bgBlend);
    }
}

// txtBoardWidth.setTextFormatter(new TextFormatter<>(c -> {
//     if (!c.getControlNewText().matches("\\d*")) 
//         return null;
//     else
//         return c;
//     }
// ));