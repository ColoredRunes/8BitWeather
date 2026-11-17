package animator.weatheranimator;

import java.net.URI;
import java.util.ArrayList;

import engine.ApplicationContext;
import engine.Position;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mainmenu.MainMenuController;
import util.ImageGrabber;

import java.awt.Desktop;

/**
 * A window controller meant to keep track of higher level items at the window
 * level.
 * subcomponents include buttons and the WeatherDashBoardController which is
 * embedded inside of this unit controller. 
 * 
 */

public class WeatherWindowController {

    /**
     * @param weatherDashboardController the WeatherDashboardController instance that manages the weather dashboard UI and functionality.
     */
    WeatherDashboardController weatherDashboardController;

    Pane root;

    public Stage stage;

    int animWidth = 128;
    int animHeight = 128;

    HBox hBox = new HBox();

    private double dragStartX;
    private double dragStartY;
    private double stageStartX;
    private double stageStartY;

    boolean isPinned = true;
    private int pinnedY = 35;

    private double screenWidth;
    @SuppressWarnings("unused")
    private double screenHeight;

    private boolean showForcast = false;

    /**
     * @param shutdownL a list of Runnable tasks that will be executed when the application is shutting down.
     */
    private ArrayList<Runnable> shutdownL = new ArrayList<>();

    Image taskbarImg = ImageGrabber.getImage("/images/menu", "taskbar", ".png");
    /**
     * @param couldUpdateApp a boolean flag indicating whether the application can be updated, which affects the visibility of the update button in the taskbar.
     */
    private boolean couldUpdateApp;

    /**
     * A window controller meant to keep track of higher level items at the window
     * level.
     * subcomponents include buttons and the WeatherDashBoardController which is
     * embedded inside of this unit controller.
     * 
     * @param app
     * @param appContext
     * @param stage
     */
    public WeatherWindowController(
            Application app,
            ApplicationContext appContext,
            Stage stage,
            boolean couldUpdateApp
        ) {
        this.stage = stage;
        this.couldUpdateApp = couldUpdateApp;

        weatherDashboardController = new WeatherDashboardController(app, appContext, 0);

        double screenWidth = appContext.screenWidth;
        double screenHeight = appContext.screenHeight;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

    }


    
    // /**
    //  * @param taskBar                  a custom taskbar component that provides window control buttons and other UI elements.
    //  */
    // CustomTaskBar taskBar;

    int fullHeight;
    public void start() {
        Pane root = new Pane();

        root.setBackground(Background.EMPTY); // or null

        // taskBar = new CustomTaskBar(root, this.shutdownL);

        fullHeight = animHeight * 5 ;//+ CustomTaskBar.height;
        Scene scene = new Scene(root, 128, fullHeight); // +50 is offset of the taskbar.

                                      // should make this better

        scene.setFill(Color.TRANSPARENT);

        // Text t = new Text();
        weatherDashboardController.start(root);
        // weatherDashboardController.setForecastExpanded(false);

        // installDragBehavior(root);

        stage.setScene(scene);
        // Add listener during SHOW FORECAST
        stage.setWidth(animWidth);
        stage.setHeight(fullHeight);
        stage.setOpacity(1.0);


        invertLayout();

        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        Position.setPos(stage);
        stage.show();
    }
    boolean invert = false;
    public void invertLayout() {
        invert = !invert;
        int taskY = invert ? fullHeight : 0;
        // taskBar.setLayoutY(taskY);
        weatherDashboardController.invertLayout(0, invert);
        if (stage.getY()+fullHeight>screenHeight){
            stage.setY(screenHeight-fullHeight);
        }
        if (stage.getY()<screenHeight){
            stage.setY(0);
        }
    }
    public boolean isShowForcast() {
        return showForcast;
    }

    


    

        // this.taskBar.taskbarView.setOnMousePressed(event -> {

        //     dragStartX = event.getScreenX();
        //     dragStartY = event.getScreenY();
        //     stageStartX = stage.getX();
        //     stageStartY = stage.getY();
        //     // }
        // });

        // this.taskBar.taskbarView.setOnMouseDragged(event -> {
        //     double deltaX = event.getScreenX() - dragStartX;
        //     double deltaY = event.getScreenY() - dragStartY;
        //     stage.setX(stageStartX + deltaX);
        //     stage.setY(stageStartY + deltaY);
        //     // }
        // });

        // this.taskBar.taskbarView.setOnMouseReleased(event -> {
        //     dragHandler.run();

        // });

    // }

    private void verticallyFlipElements() {
        // set new taskpbarpos
        // set new weatherdashboardlayout
    }

    public void show() {
        stage.show();
    }

    public void hide() {
        stage.hide();
    }

    public void setPosition(double x, double y) {
        stage.setX(x);
        stage.setY(y);
    }

    public void addShutDownListener(Runnable r) {
        shutdownL.add(r);
    }

    public Stage getStage() {
        return stage;
    }

    public WeatherDashboardController getWeatherDashboardController() {
        return weatherDashboardController;
    }
    
}