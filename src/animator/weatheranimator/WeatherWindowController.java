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

public class WeatherWindowController {





    WeatherDashboardController animationController;



/**
 */
/**
 * A controller who holds Conceyors and Buttons.
 * AnimationControllerW
 */
  
    Pane root;

    public Stage stage;

    int animWidth;
    int animHeight;

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

    
    private boolean showForcast = false;
    private ArrayList<Runnable> shutdownL = new ArrayList<>();
    class CustomTaskBar{
        CustomTaskBar(){
            
        }
        public int getHeight(){return 10;}
        public int getWidth(){return 100;}
    }
    CustomTaskBar taskBar = new CustomTaskBar();
    /**
     * A window controller meant to keep track of higher level items at the window level.
     * subcomponents include buttons and the WeatherDashBoardController which is embedded inside of this unit controller.
     * @param app
     * @param appContext
     * @param stage
     */
    public WeatherWindowController(
            Application app,
            ApplicationContext appContext,
            Stage stage) {
        this.stage = stage;
        
        animationController = new WeatherDashboardController(app, appContext);


        double screenWidth = appContext.screenWidth;
        double screenHeight = appContext.screenHeight;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        animWidth = 128;
        animHeight = 128;

    }

    public void start() {
        // applicationContext.
        Pane root = new Pane();
        
        root.setBackground(Background.EMPTY); // or null
        
        Scene scene = new Scene(root, 128, animHeight * 6 + taskBar.getHeight());
        
        scene.setFill(Color.TRANSPARENT);
        
        // Text t = new Text();
        animationController.start(root);
        animationController.setForecastExpanded(false);
        initButtons(root);
        
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

    public void initButtons(Pane root) {
        this.minimizeButton = new Button("-");
        this.closeButton = new Button("x");
        this.forecastButton = new Button("📅");
        this.pinButton = new Button("🖈");

        // ..________________ positioning of elements _______________..//
        closeButton.setLayoutX(animWidth - 50);
        forecastButton.setLayoutX(animWidth - 85);
        minimizeButton.setLayoutX(animWidth - 25);
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
                stage.setX(screenWidth - animWidth);
                stage.setY(pinnedY);
                isPinned = true;
            }
        });

        minimizeButton.setOnMouseClicked(event -> {
            animationController.onMinimizePressed();
            if (animationController.isExpanded()){
                getPinButton().setVisible(false);
                getCloseButton().setVisible(false);
                getForecastButton().setVisible(false);
            }
            else{
                getPinButton().setVisible(true);
                getCloseButton().setVisible(true);
                getForecastButton().setVisible(true);
            }
        });

        minimizeButton.setOnMouseEntered(event -> {animationController.onMinimizeMouseEntered();});
        forecastButton.setOnMouseClicked(event -> {animationController.onForecastPressed();
             // showForcast=true;
        if (showForcast) {
            animationController.setForecastExpanded(false);
            showForcast = false;
        } else {
            animationController.setForecastExpanded(true);
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

        animationController.todayAnimationUnit.getCanvas().setOnMousePressed(event -> {
            if (!isPinned) {

                dragStartX = event.getScreenX();
                dragStartY = event.getScreenY();
                stageStartX = stage.getX();
                stageStartY = stage.getY();
            }
        });

        animationController.todayAnimationUnit.getCanvas().setOnMouseDragged(event -> {
            if (!isPinned) {
                double deltaX = event.getScreenX() - dragStartX;
                double deltaY = event.getScreenY() - dragStartY;
                stage.setX(stageStartX + deltaX);
                stage.setY(stageStartY + deltaY);
            }
        });

        animationController.todayAnimationUnit.getCanvas().setOnMouseReleased(event -> {
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


    public Button getPinButton() {
        return pinButton;
    }
    public Button getMinimizeButton() {
        return minimizeButton;
    }
    public Button getCloseButton() {
        return closeButton;
    }
    public Button getForecastButton() {
        return forecastButton;
    }
    public WeatherDashboardController getAnimationController() {
        return animationController;
    }
}
