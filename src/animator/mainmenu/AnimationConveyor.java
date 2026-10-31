package animator.mainmenu;

import java.util.ArrayList;

import animator.ContentsAnim;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This particular conveyor is only used in MainMenuController.java, and is not used in the WeatherDashboardController.java. Its parallel in weather is WeatherCanvasView.java, which is a copy of this class, but with some modifications to suit the needs of the weather animations.
 * A conveyor class that manages the animation of multiple ContentsAnim objects on a canvas. It provides methods to draw the animations, manage visibility, and add the canvas to a parent pane.
 * Unlike the AnimationManager, this class handles the JavaFX canvas and the drawing of the images, while the AnimationManager is responsible for updating the animation indices of the ContentsAnim objects.
 * The AnimationConveyor class uses an AnimationTimer to continuously update and draw the animations on the canvas. It also allows for customization of font styling for any text that may be drawn on the canvas.
 * The class provides methods to set the visibility of the canvas, retrieve the canvas, and add the canvas to a parent pane. It also includes an enumeration for local font formats, allowing for easy customization of text appearance.
 */
public class AnimationConveyor {
    
    private final Canvas canvas;
    public final AnimationManager manager;
    private final AnimationTimer timer;
    private GraphicsContext gc;
    private Font fontStyling;
    // private int[] textPos = new int[] { 10, 25 };

    public enum FontFormatLocal {
        Djuwara("font/Djuwara/Djuwara-1jgBe.ttf", 12),
        Museno("font/Museno/Museno-3l4yp.ttf", 16);
        private String url;
        private int size;
        FontFormatLocal(String url, int size) {
            this.url = url;
            this.size = size;
        }
        public String getUrl() {return url;}
        public int getSize() {return size;}
    }

    FontFormatLocal font;
    private boolean shouldDraw=true;

    /**
     * This particular conveyor is only used in MainMenuController.java, and is not used in the WeatherDashboardController.java. Its parallel in weather is WeatherCanvasView.java, which is a copy of this class, but with some modifications to suit the needs of the weather animations.
     * Constructs an AnimationConveyor with the specified list of ContentsAnim items, position, sprite dimensions, and weather drawing state. The conveyor manages the animation of the provided items on a canvas, allowing for customization of font styling and visibility.
     * The WeatherDashboardController uses a different conveyor class, AnimationConveyorW, which is specifically designed for weather animations.
     * 
     * @param x       the x-coordinate of the canvas
     * @param y       the y-coordinate of the canvas
     * @param spriteW the width of the sprite
     * @param spriteH the height of the sprite
     * @param ws      the weather drawing state
     */
    public AnimationConveyor(ArrayList<ContentsAnim> items,double x, double y, int spriteW, int spriteH) {
        // _____________________________________________________________________
        // _____________________________________________________________________
        // I can port this over later to have user options for text types/fonts.
        font = FontFormatLocal.Museno;
        fontStyling = Font.loadFont(
            getClass().getResourceAsStream("/" + font.getUrl()),
            font.getSize());


        // _____________________________________________________________________
        // _____________________________________________________________________

        canvas = new Canvas(spriteW, spriteH);

        canvas.setLayoutX(x);
        canvas.setLayoutY(y);

        
        manager = new AnimationManager(items);

        gc = canvas.getGraphicsContext2D();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw();
            }
        };
        timer.start();
        startAnimation();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    private void startAnimation() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw();
            }
        }.start();
    }

    public void setVisible(boolean isVisable) {
        canvas.setVisible(isVisable);
    }

    public boolean isVisible() {
        return canvas.isVisible();
    }

    public void draw() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (!canvas.isVisible()|| !shouldDraw )
            return;

        ArrayList<ContentsAnim> list = manager.getDrawList();
        // int i = 0;
        for (ContentsAnim animationContents : list) {

            Image image = animationContents.getCurrentImage();
            if (image == null) {
                continue;
            }
            double scale = Math.min(canvas.getWidth() / image.getWidth(), canvas.getHeight() / image.getHeight());

            double w = image.getWidth() * scale;
            double h = image.getHeight() * scale;
            double x;
            double y;


                x = (canvas.getWidth() - w) / 2.0;
                y = (canvas.getHeight() - h) / 2.0;

            gc.drawImage(image, x, y, w, h);

            // i++;
        }

        gc.setFill(new Color(0, 0, 0, .5)); // text color

        gc.setFont(fontStyling);

    }

    public void addToRoot(Pane root) {
        root.getChildren().add(canvas);
    }
}
