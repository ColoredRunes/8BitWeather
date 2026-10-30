package animator.weatheranimator;

import java.util.ArrayList;

import engine.ApplicationContext;
import engine.UpdateButton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import util.ImageGrabber;

public class WeatherWindowController {

    WeatherDashboardController weatherDashboardController;

    /**
     */
    /**
     * A controller who holds Conceyors and Buttons.
     * weatherDashboardControllerW
     */

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
    private ArrayList<Runnable> shutdownL = new ArrayList<>();

    Image taskbarImg = ImageGrabber.getImage("/images/menu", "taskbar", ".png");

    class CustomTaskBar {
        private Button closeButton;
        private Button minimizeButton;
        private Button forecastButton;
        private UpdateButton updateButton;
        ImageView taskbarView;

        CustomTaskBar(Pane root, ArrayList<Runnable> shutdownL) {

            taskbarView = new ImageView(taskbarImg);

            taskbarView.setLayoutX(0);
            taskbarView.setLayoutY(0);
            taskbarView.setFitWidth(animWidth);
            taskbarView.setFitHeight(CustomTaskBar.height);

            root.getChildren().add(taskbarView);
            initButtons(root, shutdownL);
        }

        public static int height = 25;

        public int getHeight() {
            return CustomTaskBar.height;
        }

        public int getWidth() {
            return animWidth;
        }

        public void initButtons(Pane root, ArrayList<Runnable> shutdownL) {
            this.minimizeButton = new Button("-");
            this.closeButton = new Button("x");
            this.forecastButton = new Button("📅");
            this.updateButton = new UpdateButton("up");

            double buttonSize = height - 6;

            for (Button button : new Button[] {
                    minimizeButton,
                    closeButton,
                    forecastButton,
                    updateButton
            }) {
                button.setMinSize(buttonSize, buttonSize);
                button.setPrefSize(buttonSize, buttonSize);
                button.setMaxSize(buttonSize, buttonSize);
            }

            // ..________________ positioning of elements _______________..//
            closeButton.setLayoutX(animWidth - 22);
            forecastButton.setLayoutX(3);
            forecastButton.setLayoutX(23);
            minimizeButton.setLayoutX(animWidth - 42);

            updateButton.setLayoutY(3);
            closeButton.setLayoutY(3);
            forecastButton.setLayoutY(3);
            minimizeButton.setLayoutY(3);

            // ..________________ styling and styling events _______________..//

            installButtonStyling(closeButton);
            installButtonStyling(updateButton);
            installButtonStyling(forecastButton);
            installButtonStyling(minimizeButton);

            // ..________________ visibility events _______________..//
            root.setOnMouseEntered(e -> {
                minimizeButton.setVisible(true);
                closeButton.setVisible(true);
                updateButton.setVisible(true);
                forecastButton.setVisible(true);
            });

            // ..________________ click action events _______________..//
            closeButton.setOnMouseClicked(event -> {
                Platform.exit();// Temp (later i may add system tray functionality)
                for (Runnable r : shutdownL) {
                    r.run();
                }
            });

            minimizeButton.setOnMouseClicked(event -> {
                weatherDashboardController.onMinimizePressed();

            });

            forecastButton.setOnMouseClicked(event -> {
                weatherDashboardController.onForecastPressed();
                // showForcast=true;
                if (showForcast) {
                    weatherDashboardController.setForecastExpanded(false);
                    showForcast = false;
                } else {
                    weatherDashboardController.setForecastExpanded(true);
                    showForcast = true;
                }
            });
            stage.iconifiedProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue) {
                    stage.setIconified(false); // block minimize
                }
            });

            // ..________________ adding to containers _______________..//
            root.getChildren().add(closeButton);
            root.getChildren().add(minimizeButton);
            root.getChildren().add(forecastButton);
            root.getChildren().add(updateButton);

        }
    }

    CustomTaskBar taskBar;

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
            Stage stage) {
        this.stage = stage;

        weatherDashboardController = new WeatherDashboardController(app, appContext, CustomTaskBar.height);

        double screenWidth = appContext.screenWidth;
        double screenHeight = appContext.screenHeight;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

    }

    public void start() {
        // applicationContext.
        Pane root = new Pane();

        root.setBackground(Background.EMPTY); // or null

        taskBar = new CustomTaskBar(root, this.shutdownL);

        Scene scene = new Scene(root, 128, animHeight * 6 + CustomTaskBar.height); // +50 is offset of the taskbar.
                                                                                   // should make this better

        scene.setFill(Color.TRANSPARENT);

        // Text t = new Text();
        weatherDashboardController.start(root);
        weatherDashboardController.setForecastExpanded(false);

        installDragBehavior();

        stage.setScene(scene);
        // Add listener during SHOW FORECAST
        stage.setWidth(animWidth);
        stage.setHeight(animHeight * 6);
        stage.setOpacity(1.0);

        stage.setX(screenWidth - animWidth);
        stage.setY(pinnedY - 5);

        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.show();

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

    // public ManagerAnimation getAnimationManager() {
    // return animationManager;
    // }

    private void installDragBehavior() {
        Runnable dragHandler = () -> {
            stage.setX(stageStartX + (stage.getX() - stageStartX));
            stage.setY(stageStartY + (stage.getY() - stageStartY));
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