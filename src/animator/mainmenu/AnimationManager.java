package animator.mainmenu;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;

import animator.ContentsAnim;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;

/**
 * This class is only used in MainMenuController.java, and is not used in the WeatherDashboardController.java. The WeatherDashboardController uses a different conveyor class, AnimationConveyorW, which is specifically designed for weather animations.
 * its paralell in weather is AnimationHandler.java, which is a copy of this class, but with some modifications to suit the needs of the weather animations.
 * 
 * A manager class that handles the updating of animation indices for multiple
 * ContentsAnim objects. It provides methods to start the animation timer,
 * manage the list of ContentsAnim objects, and retrieve remote images for the
 * animations.
 * The AnimationManager class uses an AnimationTimer to continuously update the
 * animation indices of the ContentsAnim objects in its draw list. It allows for
 * control over whether the animation updates should occur, providing
 * flexibility in managing the animation state.
 */
public class AnimationManager {
    /**
     * @param drawListInternal an ArrayList of ContentsAnim objects that the AnimationManager will manage and update.
     * @param shouldUpdate     a boolean flag indicating whether the animation updates should occur.
     */
    private ArrayList<ContentsAnim> drawListInternal;
    private boolean shouldUpdate = true;

    public void setshouldUpdate(boolean shouldUpdate) {
        this.shouldUpdate = shouldUpdate;
    }

    /**
     * a constructor for the AnimationManager class that takes in a list of
     * ContentsAnim objects. It initializes the draw list and starts the animation. 
     * @param contents
     */
    public AnimationManager(ArrayList<ContentsAnim> contents) {
        drawListInternal = contents;
        startAnimation();
    }

    public void setShouldUpdate(boolean shouldUpdate) {
        this.shouldUpdate = shouldUpdate;
    }
    /**
     * Starts the animation timer that continuously updates the animation indices of the ContentsAnim objects in the draw list. The timer checks if updates should occur and iterates through the draw list to update each ContentsAnim object accordingly.
     */
    private void startAnimation() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (shouldUpdate)
                    for (ContentsAnim animationContent : drawListInternal) {
                        animationContent.updateIndex(now);
                    }
            }
        }.start();
    }

    public ArrayList<ContentsAnim> getDrawList() {
        return drawListInternal;
    }
    /**
     * Procures remote images from a specified URL and returns them as an array of Image objects. The method takes in the length of the desired image array and retrieves images from a predefined URL pattern. It handles potential exceptions related to malformed URLs and IO issues.
     * @param length the number of images to retrieve
     * @return an array of Image objects containing the retrieved images
     * @throws MalformedURLException if the constructed URL is malformed
     * @throws IOException if an I/O error occurs while retrieving the images
     */
    public Image[] procureRemoteImages(int length) throws MalformedURLException, IOException {
        Image[] r = new Image[length];
        for (int i = 0; i < length; i++) {
            URI uri = URI.create("https://bit-weather-b7bb3.web.app/images/blurb" + (i + 1) + ".png");
            Image image = new Image(uri.toString());
            r[i] = image;

        }
        return r;
    }
}
