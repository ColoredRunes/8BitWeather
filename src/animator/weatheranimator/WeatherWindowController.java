package animator.weatheranimator;

import java.net.URI;
import java.util.ArrayList;

import engine.ApplicationContext;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import util.ImageGrabber;

import java.awt.Canvas;
import java.awt.Desktop;
import java.awt.Taskbar;

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

        weatherDashboardController = new WeatherDashboardController(app, appContext, CustomTaskBar.height);

        double screenWidth = appContext.screenWidth;
        double screenHeight = appContext.screenHeight;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

    }


    private class CustomTaskBar {
        private Button closeButton;
        private Button minimizeButton;
        // private Button forecastButton;
        /*** @param updateButton a button that allows the user to check for updates to the application. It is only visible if the application can be updated.*/
        private Button invertButton;
        private Button updateButton;

        ImageView taskbarView;
        Group container;
        CustomTaskBar(Pane root, ArrayList<Runnable> shutdownL) {
            taskbarView = new ImageView(taskbarImg);

            taskbarView.setLayoutX(0);
            taskbarView.setLayoutY(0);
            taskbarView.setFitWidth(animWidth);
            taskbarView.setFitHeight(CustomTaskBar.height);

            container = new Group(taskbarView);   // field: Group container
            root.getChildren().add(container);
            initButtons(container, shutdownL);    // was: root
            
        }

        public static int height = 25;
        
        @SuppressWarnings("unused")
        public int getHeight() {
            return CustomTaskBar.height;
        }

        @SuppressWarnings("unused")
        public int getWidth() {
            return animWidth;
        }
        public double getLayoutX() {
            return container.getLayoutX();
        }
        public double getLayoutY() {
            return container.getLayoutY();
        }
        public void setLayoutX(double v) {
            container.setLayoutX(v);
        }
        public void setLayoutY(double v) {
            container.setLayoutY(v);
        }

        public void initButtons(Group container, ArrayList<Runnable> shutdownL) {
            this.minimizeButton = new Button("-");
            this.closeButton = new Button("x");
            // this.forecastButton = new Button("📅");
            this.updateButton = getUpdateButton();
            this.invertButton = new Button("⇅");

            
            
            double buttonSize = height - 6;

            for (Button button : new Button[] {
                    minimizeButton,
                    closeButton,
                    // forecastButton,
                    updateButton,
                    invertButton
            }) {
                button.setMinSize(buttonSize, buttonSize);
                button.setPrefSize(buttonSize, buttonSize);
                button.setMaxSize(buttonSize, buttonSize);
            }

            // ..________________ positioning of elements _______________..//
            closeButton.setLayoutX(animWidth - 22);
            minimizeButton.setLayoutX(animWidth - 42);
            invertButton.setLayoutX(3);
            // forecastButton.setLayoutX(3);
            updateButton.setLayoutX(23);

            updateButton.setLayoutY(3);
            invertButton.setLayoutY(3);
            closeButton.setLayoutY(3);
            // forecastButton.setLayoutY(3);
            minimizeButton.setLayoutY(3);

            // ..________________________ Tool tip  ________________________..//

            updateButton.setTooltip(new Tooltip("New Update Available"));
            invertButton.setTooltip(new Tooltip("Flip forecast view"));
            // ..________________ styling and styling events _______________..//

            installButtonStyling(closeButton);
            installButtonStyling(updateButton);
            installButtonStyling(invertButton);
            // installButtonStyling(forecastButton);
            installButtonStyling(minimizeButton);

            // ..________________ click action events _______________..//
            closeButton.setOnMouseClicked(event -> {
                Platform.exit();// Temp (later i may add system tray functionality)
                for (Runnable r : shutdownL) {
                    r.run();
                }
            });
            invertButton.setOnMouseClicked(event->{
                invertLayout();
            });
            minimizeButton.setOnMouseClicked(event -> {
                weatherDashboardController.onMinimizePressed();
                
            });

            // forecastButton.setOnMouseClicked(event -> {
            //     weatherDashboardController.onForecastPressed();
            //     if (showForcast) {
            //         weatherDashboardController.setForecastExpanded(false);
            //         showForcast = false;
            //     } else {
            //         weatherDashboardController.setForecastExpanded(true);
            //         showForcast = true;
            //     }
            // });
            updateButton.setOnMouseClicked(e->{
                try {
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(URI.create("https://8-bit-weather.com/update.html"));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            stage.iconifiedProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue) {
                    stage.setIconified(false); // block minimize
                }
            });
            
            // ..________________ adding to containers _______________..//
            container.getChildren().add(closeButton);
            container.getChildren().add(minimizeButton);
            // container.getChildren().add(forecastButton);
            container.getChildren().add(invertButton);
            if(couldUpdateApp)
                container.getChildren().add(updateButton);

        }
    }
    /**
     * @param taskBar                  a custom taskbar component that provides window control buttons and other UI elements.
     */
    CustomTaskBar taskBar;

    int fullHeight;
    public void start() {
        Pane root = new Pane();

        root.setBackground(Background.EMPTY); // or null

        taskBar = new CustomTaskBar(root, this.shutdownL);

        fullHeight = animHeight * 5 + CustomTaskBar.height;
        Scene scene = new Scene(root, 128, fullHeight); // +50 is offset of the taskbar.

                                      // should make this better

        scene.setFill(Color.TRANSPARENT);

        // Text t = new Text();
        weatherDashboardController.start(root);
        weatherDashboardController.setForecastExpanded(false);

        installDragBehavior(root);

        stage.setScene(scene);
        // Add listener during SHOW FORECAST
        stage.setWidth(animWidth);
        // stage.setHeight(animHeight * 5 + Taskbarhe);
        stage.setOpacity(1.0);

        stage.setX(screenWidth - animWidth);
        stage.setY(pinnedY - 5);

        invertLayout();
        stage.setY(screenHeight-fullHeight);
        stage.setX(screenWidth-450);

        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.show();

    }
    boolean invert = false;
    public void invertLayout() {
        invert = !invert;
        int taskY = invert ? fullHeight - CustomTaskBar.height : 0;
        taskBar.setLayoutY(taskY);
        weatherDashboardController.invertLayout(CustomTaskBar.height, invert);
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

    private void installButtonStyling(Button button) {
        String baseCol = "#098bd151;";
        String hovrCol = "#7b070776;";
        String clikCol = "#05c33b76;";
        installButtonStyling(button, baseCol, hovrCol, clikCol);
    }

    private void installButtonStyling(
            Button button,
            String baseCol,
            String hovrCol,
            String clikCol) {

        String baseStyle = "-fx-background-color: " + baseCol +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 4;" +
                "-fx-padding: 0;" +
                "-fx-font-size: 12px;" +
                "-fx-alignment: center;";

        String hoverStyle = "-fx-background-color: " + hovrCol +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 4;" +
                "-fx-padding: 0;" +
                "-fx-font-size: 12px;" +
                "-fx-alignment: center;";

        String clickStyle = "-fx-background-color: " + clikCol +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 4;" +
                "-fx-padding: 0;" +
                "-fx-font-size: 12px;" +
                "-fx-alignment: center;";

        button.setStyle(baseStyle);

        button.setOnMousePressed(e -> button.setStyle(clickStyle));
        button.setOnMouseReleased(e -> button.setStyle(baseStyle));
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }


    private void installDragBehavior(Pane root) {
        Runnable dragHandler = () -> {
            stage.setX(stageStartX + (stage.getX() - stageStartX));
            stage.setY(stageStartY + (stage.getY() - stageStartY));
            // if (stage.getY() + stage.getHeight() > screenHeight) stage.setY(screenHeight - stage.getHeight());{
            //     // root.setLayoutY(screenHeight-root.getBoundsInParent().getHeight());
            //     // verticallyFlipElements();
            // }
        };

        this.taskBar.taskbarView.setOnMousePressed(event -> {

            dragStartX = event.getScreenX();
            dragStartY = event.getScreenY();
            stageStartX = stage.getX();
            stageStartY = stage.getY();
            // }
        });

        this.taskBar.taskbarView.setOnMouseDragged(event -> {
            double deltaX = event.getScreenX() - dragStartX;
            double deltaY = event.getScreenY() - dragStartY;
            stage.setX(stageStartX + deltaX);
            stage.setY(stageStartY + deltaY);
            // }
        });

        this.taskBar.taskbarView.setOnMouseReleased(event -> {
            dragHandler.run();

        });

    }

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
    private Button getUpdateButton(){

        Image image = ImageGrabber.getImage("/images/menu", "updatebutton", ".png");
        ImageView imageView = new ImageView(image);

        // Optionally resize the image
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        imageView.setPreserveRatio(true);

        // Create the button and set the graphic
        Button button = new Button();
        button.setGraphic(imageView);

        return button;

    }
}