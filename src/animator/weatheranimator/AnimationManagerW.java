package animator.weatheranimator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;


import animator.ContentsAnim;
import debug.Debuggable;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
//  *  * Admittedly this singular object could inherit from its more general class, 'AnimationManager'. Instead this is copy pasted and mutated for singular use

public class AnimationManagerW extends Debuggable {

    private ContentsAnim tree;
    private ContentsAnim details;

    private ContentsAnim cloud0;
    private ContentsAnim cloud1; // May keep and use this to blit concurrently? // THIS MAY BE WASTE OF MEM
    private ContentsAnim cloud2; // May keep and use this to blit concurrently?
    private ContentsAnim cloud3; // May keep and use this to blit concurrently?
    private ContentsAnim cloud4; // May keep and use this to blit concurrently?

    private ContentsAnim error;
    private ContentsAnim connectionError;
    private ContentsAnim night;

    private ContentsAnim fog;
    private ContentsAnim lightning;
    private ContentsAnim wind;
    private ContentsAnim rain1;
    private ContentsAnim rain2;
    private ContentsAnim rain3;
    private ContentsAnim rain4;
    private ContentsAnim[] allRain;

    private ContentsAnim ground_snow;

    private ContentsAnim snow1;
    private ContentsAnim snow2;
    private ContentsAnim snow3;
    private ContentsAnim snow4;
    private ContentsAnim[] allSnow;

    private ArrayList<ContentsAnim> drawListInternal = new ArrayList<>();

    private boolean shouldUpdate = true;

    /** a timer used for update */
    public void setshouldUpdate(boolean shouldUpdate) {
        this.shouldUpdate = shouldUpdate;
    }

    AnimationManagerW() {
        super(AnimationManagerW.class.getSimpleName());
        initAnimations();
        startAnimation();
    }

    private void initAnimations() {
        tree = new ContentsAnim(
                "tree",
                10, 0, 120_000_000L);

        details = new ContentsAnim(
                "details",
                10, 0, 120_000_000L); // TODO handle freezing len = 1 arrays loops
        cloud0 = new ContentsAnim(
                "clouds_0_",
                1,
                0, 180_000_000L);
        cloud1 = new ContentsAnim(
                "clouds_1_",
                11,
                0, 120_000_000L);
        cloud2 = new ContentsAnim(
                "clouds_2_",
                11,
                0, 120_000_000L);
        cloud3 = new ContentsAnim(
                "clouds_3_",
                11,
                0, 120_000_000L);
        cloud4 = new ContentsAnim(
                "clouds_4_",
                11,
                0, 120_000_000L);

        night = new ContentsAnim(
                "night",
                1,
                0, 0);
        ground_snow = new ContentsAnim(
                "ground_snow",
                1,
                0, 0);
        rain1 = new ContentsAnim(
                "rain",
                10,
                0, 20_000_000L);
        lightning = new ContentsAnim(
                "lightning",
                11,
                0, 120_000_000L);
        wind = new ContentsAnim(
                "wind",
                8,
                0, 120_000_000L);
        fog = new ContentsAnim(
                "fog",
                1,
                0, 20_000_000L);
        snow1 = new ContentsAnim(
                "snow",
                10,
                0, 120_000_000L);
        snow2 = snow1.copy(2);
        snow3 = snow1.copy(5);
        snow4 = snow1.copy(8);

        rain2 = rain1.copy(1);
        rain3 = rain1.copy(2);
        rain4 = rain1.copy(3);
        allSnow = new ContentsAnim[] {
                snow1,
                snow2,
                snow3,
                snow4
        };

        allRain = new ContentsAnim[] {
                rain1,
                rain2,
                rain3,
                rain4
        };

        error = new ContentsAnim(
                "ground_error",
                1,
                0, 0);
        connectionError = new ContentsAnim(
                "connecterror",
                1,
                0, 0);
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
    public int getRemoteAnimationSlidesLen() throws IOException, InterruptedException{
        URI numUri = URI.create("https://bit-weather-b7bb3.web.app/images/len.txt");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(numUri).build();

        HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());

        int length = Integer.parseInt(response.body().trim());

        // System.out.println(length); // 
        // 5
        return length;
    }
    public void procureAnim_Contents(WeatherDrawingState weatherState) {
        ContentsAnim remoteAnim = null;
        ArrayList<ContentsAnim> drawList = new ArrayList<>();

        boolean err = weatherState.error;
        boolean conErr = weatherState.connectError;
        try {
            int num = getRemoteAnimationSlidesLen();
            Image[] rem_im = procureRemoteImages(num);
            remoteAnim = new ContentsAnim(rem_im, num, 0, 240_000_000L);
        } catch (IOException e) {
            e.printStackTrace();
            err = true; // REMOTE_ERROR
        } catch (InterruptedException e) {
            e.printStackTrace();
            err = true;
        }

        int drawRain = weatherState.drawRain;
        int drawSnow = weatherState.drawSnow;
        int drawWind = weatherState.drawWind;
        int drawClouds = weatherState.drawClouds;
        int drawLightning = weatherState.drawLightning;
        int drawFog = weatherState.drawFog;
        int drawSnowOnTheGround = weatherState.drawSnowOnTheGround;
        int drawNight = weatherState.drawNight;

        switch (drawClouds) {
            case 0:
                drawList.add(cloud0);
                break;
            case 1:
                drawList.add(cloud1);
                break;
            case 2:
                drawList.add(cloud2);
                break;
            case 3:
                drawList.add(cloud3);
                break;
            case 4:
                drawList.add(cloud4);
                break;

            default:
                break;
        }
        if (drawLightning >= 0) {
            drawList.add(lightning);
        }
        drawList.add(tree);
        drawList.add(details);

        if (err) {
            drawList.add(error);
        }
        if (conErr) {
            drawList.add(connectionError);
        }
        if (drawSnow >= 0)
            for (int i = 0; i < drawSnow; i++) {
                drawList.add(allSnow[i]);
            }

        if (drawRain >= 0)

            for (int i = 0; i < drawRain; i++) {
                drawList.add(allRain[i]);
            }

        if (drawWind >= 0) {
            drawList.add(wind);
        }
        // drawWind(drawWind, index, spriteX, spriteY);
        if (drawFog >= 0) {
        }
        // drawFog(drawFog, index, spriteX, spriteY);
        if (drawSnowOnTheGround >= 0) {
            drawList.add(ground_snow);
        }
        if (drawNight == 1) {
            drawList.add(night);
        }
        if (remoteAnim != null) {
            drawList.add(remoteAnim);
        }
        if(drawFog>0){
            drawList.add(fog);
        }
        this.drawListInternal = new ArrayList<>(drawList);
        // return drawList;

    }

    @Override
    public void printDebug() {
        // System.out.println(AnimationController.class.getSimpleName()+" Images: loaded
        // " + errorImage.getUrl() + " error=" + errorImage.isError());
        // System.out.println(AnimationController.class.getSimpleName()+" Images: loaded
        // " + night.getUrl() + " error=" + night.isError());
    }

}
