package engine;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import animator.weatheranimator.WeatherWindowController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mainmenu.MainMenuController;
import weather.WeatherPoller;

public class App extends Application {
    // File dataDir = new File(System.getenv("LOCALAPPDATA"), "8BitWeather");
    // dataDir.mkdirs();
    public ApplicationContext applicationContext;
    public WeatherPoller weatherPoller;
    // public AnimationControllerW animationController;
    public WeatherWindowController weatherWindowController;
    public MainMenuController mainMenuController  ;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.getIcons().add(new Image(new File("images/icon.png").toURI().toString()));        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setResizable(false);

        Image icon = new Image(new File("resources/images/icon.png").toURI().toString());
        primaryStage.getIcons().add(icon);

        applicationContext = new ApplicationContext();

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        applicationContext.screenWidth = screenWidth;
        applicationContext.screenHeight = screenHeight;

        weatherWindowController = new WeatherWindowController(this, applicationContext, primaryStage,getCouldUpdate("1.2.0"));
        mainMenuController  = new MainMenuController(this,applicationContext,primaryStage);

        weatherPoller = new WeatherPoller(weatherWindowController.getWeatherDashboardController(), applicationContext.weatherDrawingState, applicationContext.DOWDrawingStates);
        // Do not show the weather stage until the user has entered a valid city.
        Runnable r = () -> weatherPoller.stop();
        weatherWindowController.addShutDownListener(r);

        primaryStage.setOnCloseRequest(event -> {
            if (weatherPoller != null) {
                weatherPoller.stop();
            }
            Platform.exit();
        });

    }
    /**
     * 
     * @param version This argument should be modified on every update
     * @return boolean determining if the app should show that the app needs to be updated.
     */
    private boolean getCouldUpdate(String version){
        URI numUri = URI.create("https://bit-weather-b7bb3.web.app/version.txt");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(numUri).build();

        try {
            HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200 && !response.body().trim().equals(version)) {
                    util.Debug.print(version + "?=" + response.body().trim());
                    return true;
                }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return false;
        // if ()
    }
    @Override
    public void stop() {
        if (weatherPoller != null) {
            weatherPoller.stop();
        }
    }

}

/**
 * 
 * @Override
 *           public void start(Stage stage) {
 *           WeatherAnimator animator = new WeatherAnimator();
 * 
 *           AnimationPane animPane = new AnimationPane(animator);
 *           ControlPane ctrlPane = new ControlPane(animator);
 * 
 *           StackPane root = new StackPane(animPane, ctrlPane);
 * 
 *           Scene scene = new Scene(root, screenW, screenH);
 *           scene.setFill(Color.TRANSPARENT);
 *           stage.setScene(scene);
 *           stage.show();
 *           }
 */