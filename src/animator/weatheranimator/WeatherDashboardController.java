package animator.weatheranimator;


import engine.ApplicationContext;
import javafx.application.Application;
import javafx.scene.layout.Pane;









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
            ApplicationContext appContext,
            int taskbarOffsetH
        ) {

        // Primary WeatherState for today
        WeatherDrawingState ws = appContext.weatherDrawingState;
        // WeatherStates for forecasted week
        WeatherDrawingState[] DOWState = appContext.DOWDrawingStates;

        this.DOWState = DOWState;

        sprWidth = 128;
        sprHeight = 128;

        todayAnimationUnit = new WeatherCanvasView(0, taskbarOffsetH, sprWidth, sprHeight, ws);

        for (int i = 0; i < DOWState.length; i++) {
            forecastAnimations[i] = new WeatherCanvasView(0, taskbarOffsetH+sprHeight * (i + 1), sprWidth, sprHeight, DOWState[i]);
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
                todayAnimationUnit.setShouldUpdate(false);
                for (WeatherCanvasView animationUnit : forecastAnimations) {
                    animationUnit.setVisible(false);
                    animationUnit.setShouldUpdate(animationUnit.isVisible());
                }
                
            } 
            else {
                todayAnimationUnit.setVisible(true); 
            }
            todayAnimationUnit.setShouldUpdate(todayAnimationUnit.isVisible());
    }

    
    
    public void onForecastPressed() {
        if (isMinimized()) {
            onMinimizePressed();
        }
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

