package animator.weatheranimator;

import java.util.ArrayList;

import engine.ApplicationContext;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;










/**
 * Admittedly this singular object could inherit from its more general class, 'AnimationController'. Instead this is copy pasted and mutated for singular use
 */
/**
 * A controller who holds Conceyors and Buttons.
 * AnimationControllerW
 */
public class AnimationControllerW {
    /*
     * Stage
     * └── Scene
     * └── Pane (root)
     * ├── Canvas
     * └── Button(s)
     */
    Pane root;

    public Stage stage;

    public final AnimationConveyorW todayAnimationUnit;
    public final AnimationConveyorW[] forecastAnimations = new AnimationConveyorW[4];

    WeatherDrawingState[] DOWState;

    private Button closeButton;
    private Button pinButton;
    private Button minimizeButton;
    private Button forecastButton;

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

    private int sprWidth;

    private int sprHeight;
    private boolean showForcast = false;
    private ArrayList<Runnable> shutdownL = new ArrayList<>();

    /**
     * Creates a weather animation controller and initializes all animation
     * conveyors using weather state information contained within the provided
     * application context.
     *
     * @param app        the parent JavaFX application
     * @param appContext shared application state containing weather data,
     *                   forecast data, and screen dimensions
     * @param stage      the JavaFX stage that will display this weather widget
     */
    public AnimationControllerW(
            Application app,
            ApplicationContext appContext,
            Stage stage) {
        this.stage = stage;

        WeatherDrawingState ws = appContext.weatherDrawingState;
        WeatherDrawingState[] DOWState = appContext.DOWDrawingStates;

        this.DOWState = DOWState;

        double screenWidth = appContext.screenWidth;
        double screenHeight = appContext.screenHeight;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        sprWidth = 128;
        sprHeight = 128;

        todayAnimationUnit = new AnimationConveyorW(0, 0, sprWidth, sprHeight, ws);
        // cultivate them based on initalization

        for (int i = 0; i < DOWState.length; i++) {
            forecastAnimations[i] = new AnimationConveyorW(0, sprHeight * (i + 1), sprWidth, sprHeight, DOWState[i]);
        }

        // Canvas canvas = new Canvas(400, 300);
        // GraphicsContext gc = canvas.getGraphicsContext2D();

    }

    public void start() {
        // applicationContext.

        Pane root = new Pane();

        root.setBackground(Background.EMPTY); // or null

        Scene scene = new Scene(root, 128, sprHeight * 6);

        scene.setFill(Color.TRANSPARENT);

        // Text t = new Text();
        todayAnimationUnit.addToRoot(root);

        for (int i = 0; i < DOWState.length; i++) {
            forecastAnimations[i].addToRoot(root);
        }
        initButtons(root);

        installDragBehavior();

        stage.setScene(scene);
        // Add listener during SHOW FORECAST
        stage.setWidth(sprWidth);
        stage.setHeight(sprHeight * 6);
        stage.setOpacity(1.0);

        stage.setX(screenWidth - sprWidth);
        stage.setY(pinnedY - 5);

        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.show();

    }

    public boolean isShowForcast() {
        return showForcast;
    }

    public void initButtons(Pane root) {
        this.minimizeButton = new Button("-");
        this.closeButton = new Button("x");
        this.forecastButton = new Button("📅");
        this.pinButton = new Button("🖈");

        // ..________________ positioning of elements _______________..//
        closeButton.setLayoutX(sprWidth - 50);
        forecastButton.setLayoutX(sprWidth - 85);
        minimizeButton.setLayoutX(sprWidth - 25);
        pinButton.setLayoutX(5);

        closeButton.setLayoutY(5);
        forecastButton.setLayoutY(5);
        pinButton.setLayoutY(5);
        minimizeButton.setLayoutY(5);

        // ..________________ styling and styling events _______________..//
        String baseCol = "#8e09d1a7;";
        String hovrCol = "#7b070776;";
        String clikCol = "#05c33b76;";

        installButtonStyling(closeButton);
        installButtonStyling(forecastButton);
        installButtonStyling(pinButton);
        installButtonStyling(minimizeButton, baseCol, hovrCol, clikCol);

        // ..________________ visibility events _______________..//
        root.setOnMouseEntered(e -> {
            minimizeButton.setVisible(true);
            closeButton.setVisible(true);
            forecastButton.setVisible(true);
            pinButton.setVisible(true);
        });

        root.setOnMouseExited(e -> {
            if (todayAnimationUnit.isVisible()) {
                minimizeButton.setVisible(false);
                closeButton.setVisible(false);
                forecastButton.setVisible(false);
                pinButton.setVisible(false);
            }
        });

        closeButton.setVisible(false);
        forecastButton.setVisible(false);
        pinButton.setVisible(false);
        minimizeButton.setVisible(false);

        // ..________________ click action events _______________..//
        closeButton.setOnMouseClicked(event -> {
            Platform.exit();// Temp (later i may add system tray functionality)
            for (Runnable r : this.shutdownL) {
                r.run();
            }
        });
        pinButton.setOnMouseClicked(event -> {
            if (isPinned) {
                pinButton.setText("📌");
                isPinned = false;
            } else if (!isPinned) {
                pinButton.setText("🖈");
                stage.setX(screenWidth - sprWidth);
                stage.setY(pinnedY);
                isPinned = true;

            }
        });
        minimizeButton.setOnMouseClicked(event -> {

            if (todayAnimationUnit.isVisible()) {
                todayAnimationUnit.setVisible(false);

                for (AnimationConveyorW animationUnit : forecastAnimations) {
                    animationUnit.setVisible(false);
                    closeButton.setVisible(false);
                    pinButton.setVisible(false);
                    forecastButton.setVisible(false);
                }
            } else {
                todayAnimationUnit.setVisible(true);
                closeButton.setVisible(true);
                pinButton.setVisible(true);
                forecastButton.setVisible(true);
            }
            todayAnimationUnit.manager.setshouldUpdate(todayAnimationUnit.isVisible());
            for (AnimationConveyorW animationUnit : forecastAnimations) {
                animationUnit.manager.setshouldUpdate(animationUnit.isVisible());
            }
        });
        minimizeButton.setOnMouseEntered(event -> {
            if (!todayAnimationUnit.isVisible()) {
                todayAnimationUnit.setVisible(true);
            }
        });
        forecastButton.setOnMouseClicked(event -> {
            // showForcast=true;
            if (showForcast) {
                for (AnimationConveyorW animationUnit : forecastAnimations) {
                    animationUnit.setVisible(false);
                }
                showForcast = false;
            } else {
                for (AnimationConveyorW animationUnit : forecastAnimations) {
                    animationUnit.setVisible(true);
                }
                showForcast = true;
            }
            todayAnimationUnit.manager.setshouldUpdate(todayAnimationUnit.isVisible());
            for (AnimationConveyorW animationUnit : forecastAnimations) {
                animationUnit.manager.setshouldUpdate(animationUnit.isVisible());
            }
        });
        stage.iconifiedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                stage.setIconified(false); // block minimize
            }
        });

        // ..________________ adding to containers _______________..//
        root.getChildren().add(closeButton);
        root.getChildren().add(pinButton);
        root.getChildren().add(minimizeButton);
        root.getChildren().add(forecastButton);

    }

    private void installButtonStyling(Button button) {
        String baseCol = "#098bd1a7;";
        String hovrCol = "#7b070776;";
        String clikCol = "#05c33b76;";
        installButtonStyling(button, baseCol, hovrCol, clikCol);
    }

    private void installButtonStyling(Button button, String baseCol, String hovrCol, String clikCol) {
        button.setOnMousePressed(e -> button.setStyle("-fx-background-color: " + clikCol
                + "-fx-text-fill: white;" + "-fx-background-radius: 4;" + "-fx-padding: 4 8;"));

        button.setOnMouseReleased(e -> button.setStyle("-fx-background-color: " + baseCol + "-fx-text-fill: white;"
                + "-fx-background-radius: 4;" + "-fx-padding: 4 8;"));

        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + hovrCol + "-fx-text-fill: white;"
                + "-fx-background-radius: 4;" + "-fx-padding: 4 8;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + baseCol + "-fx-text-fill: white;"
                + "-fx-background-radius: 4;" + "-fx-padding: 4 8;"));

        button.setStyle("-fx-background-color: " + baseCol + "-fx-text-fill: white;" + "-fx-background-radius: 4;"
                + "-fx-padding: 4 8;");
    }

    // public ManagerAnimation getAnimationManager() {
    // return animationManager;
    // }

    private void installDragBehavior() {
        Runnable dragHandler = () -> {
            stage.setX(stageStartX + (stage.getX() - stageStartX));
            stage.setY(stageStartY + (stage.getY() - stageStartY));
        };

        todayAnimationUnit.getCanvas().setOnMousePressed(event -> {
            if (!isPinned) {

                dragStartX = event.getScreenX();
                dragStartY = event.getScreenY();
                stageStartX = stage.getX();
                stageStartY = stage.getY();
            }
        });

        todayAnimationUnit.getCanvas().setOnMouseDragged(event -> {
            if (!isPinned) {
                double deltaX = event.getScreenX() - dragStartX;
                double deltaY = event.getScreenY() - dragStartY;
                stage.setX(stageStartX + deltaX);
                stage.setY(stageStartY + deltaY);
            }
        });

        todayAnimationUnit.getCanvas().setOnMouseReleased(event -> {
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

}
