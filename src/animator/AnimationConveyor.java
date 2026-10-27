package animator;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

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
     * a constructor for the AnimationConveyor class.
     * 
     * @param x       the x-coordinate of the canvas
     * @param y       the y-coordinate of the canvas
     * @param spriteW the width of the sprite
     * @param spriteH the height of the sprite
     * @param ws      the weather drawing state
     */
    public AnimationConveyor(ArrayList<ContentsAnim> items,double x, double y, int spriteW, int spriteH) {
        // shouldDraw = ws.badDraw;
        // if(ws.badDraw ==) shouldDraw =false;
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
        // int length = (int) (fontStyling.getSize() * dayString.length() * .5);
        // gc.fillRoundRect(textPos[0], textPos[1] - 12, length, 12, 10, 10);
        // gc.setFill(Color.WHITE); // text color
        // gc.fillText("" + dayString, textPos[0], textPos[1]);

        // gc.fillText("" + ws.weatherData.temp_min, textPos[0], textPos[1] + canvas.getHeight() * .8);
        // gc.fillText("" + ws.weatherData.temp, textPos[0] + canvas.getWidth() / 3, textPos[1] + canvas.getHeight() * .8);
        // gc.fillText("" + ws.weatherData.temp_max, textPos[0] + (canvas.getWidth() / 3)*2,textPos[1] + canvas.getHeight() * .8);

    }

    public void addToRoot(Pane root) {
        root.getChildren().add(canvas);
    }
}
