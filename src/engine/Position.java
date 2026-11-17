package engine;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Position {
    public static void setPos(Stage stage){
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double width = stage.getWidth() > 0 ? stage.getWidth() : stage.getScene().getWidth();
        double height = stage.getHeight() > 0 ? stage.getHeight() : stage.getScene().getHeight();
        double padding = 10;

        stage.setX(visualBounds.getMaxX() - width - padding);
        stage.setY(visualBounds.getMaxY() - height - padding);
        stage.show();
        stage.toFront();
    }
}
