package engine;

import java.io.File;

import animator.weatheranimator.AnimationControllerW;
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
    public AnimationControllerW animationController;
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

        animationController = new AnimationControllerW(this,applicationContext,primaryStage);
        mainMenuController  = new MainMenuController(this,applicationContext,primaryStage);
        // animationController.show();
        // Will need to send each one to the poller
        weatherPoller = new WeatherPoller(animationController, applicationContext.weatherDrawingState,
                applicationContext.DOWDrawingStates);
        // Do not show the weather stage until the user has entered a valid city.
        Runnable r = () -> weatherPoller.stop();
        animationController.addShutDownListener(r);

        primaryStage.setOnCloseRequest(event -> {
            if (weatherPoller != null) {
                weatherPoller.stop();
            }
            Platform.exit();
        });

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