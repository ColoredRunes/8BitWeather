package animator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;

import debug.Debuggable;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;

/**
 * A regular animation Manager meant to hold whatever contents are sent, and initiate them (ContentsAnim)
 * AnimationManager
 */
public class AnimationManager extends Debuggable {
        private ArrayList<ContentsAnim> drawListInternal = new ArrayList<>();

    private boolean shouldUpdate = true;

    /** a timer used for update */
    public void setshouldUpdate(boolean shouldUpdate) {
        this.shouldUpdate = shouldUpdate;
    }

    public AnimationManager(ArrayList<ContentsAnim> contents) {
        super(AnimationManager.class.getSimpleName());
        drawListInternal = contents;
        startAnimation();
    }

    public void setShouldUpdate(boolean shouldUpdate) {
        this.shouldUpdate = shouldUpdate;
    }

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

    public Image[] procureRemoteImages(int length) throws MalformedURLException, IOException {
        Image[] r = new Image[length];
        for (int i = 0; i < length; i++) {
            URI uri = URI.create("https://bit-weather-b7bb3.web.app/images/blurb" + (i + 1) + ".png");
            Image image = new Image(uri.toString());
            r[i] = image;

        }
        return r;
    }


    @Override
    public void printDebug() {
    }


}
