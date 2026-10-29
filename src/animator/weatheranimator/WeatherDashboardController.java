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
public class WeatherDashboardController {
    /*
     * Stage
     * └── Scene
     * └── Pane (root)
     * ├── Canvas
     * └── Button(s)
     */
    // Pane root;

    // public Stage stage;

    public final WeatherCanvasView todayAnimationUnit;
    public final WeatherCanvasView[] forecastAnimations = new WeatherCanvasView[4];

    WeatherDrawingState[] DOWState;

    private int sprWidth;
    private int sprHeight;


    /**
     * Creates a weather animation controller and initializes all animation
     * conveyors using weather state information contained within the provided
     * application context.
     *
     * @param app        the parent JavaFX application
     * @param appContext shared application state containing weather data,
     *                   forecast data, and screen dimensions
     */
    public WeatherDashboardController(
            Application app,
            ApplicationContext appContext) {

        // Primary WeatherState for today
        WeatherDrawingState ws = appContext.weatherDrawingState;
        // WeatherStates for forecasted week
        WeatherDrawingState[] DOWState = appContext.DOWDrawingStates;

        this.DOWState = DOWState;

        sprWidth = 128;
        sprHeight = 128;

        todayAnimationUnit = new WeatherCanvasView(0, 0, sprWidth, sprHeight, ws);

        for (int i = 0; i < DOWState.length; i++) {
            forecastAnimations[i] = new WeatherCanvasView(0, sprHeight * (i + 1), sprWidth, sprHeight, DOWState[i]);
        }

    }

    public void start(Pane root) {
        

        // Text t = new Text();
        todayAnimationUnit.addToRoot(root);

        for (int i = 0; i < DOWState.length; i++) {
            forecastAnimations[i].addToRoot(root);
        }
    }


    public void onMinimizePressed() {
            if (todayAnimationUnit.isVisible()) {
                todayAnimationUnit.setVisible(false);

                for (WeatherCanvasView animationUnit : forecastAnimations) {
                    animationUnit.setVisible(false);
                }
                
            } else {
                todayAnimationUnit.setVisible(true);
                
            }
            todayAnimationUnit.setShouldUpdate(todayAnimationUnit.isVisible());
            for (WeatherCanvasView animationUnit : forecastAnimations) {
                animationUnit.setShouldUpdate(animationUnit.isVisible());
            }
    }

    public void onMinimizeMouseEntered() {
        if (!todayAnimationUnit.isVisible()) {
            todayAnimationUnit.setVisible(true);
        }
    }
    
    public void onForecastPressed() {
       
        todayAnimationUnit.setShouldUpdate(todayAnimationUnit.isVisible());
        for (WeatherCanvasView animationUnit : forecastAnimations) {
            animationUnit.setShouldUpdate(animationUnit.isVisible());
        }
    }

    public boolean isMinimized() {
    return !todayAnimationUnit.isVisible();
    }
    public boolean isExpanded() {
        return todayAnimationUnit.isVisible();
    }
    public boolean isForecastExpanded() {
        return forecastAnimations[0].isVisible();
    }
    public void setForecastExpanded(boolean expanded){
        for (WeatherCanvasView animationUnit : forecastAnimations) {
            animationUnit.setVisible(expanded);
            animationUnit.setShouldUpdate(animationUnit.isVisible());
        }
    }
}

