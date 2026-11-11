package animator.weatheranimator;

import engine.ApplicationContext;
import javafx.application.Application;
import javafx.scene.layout.Pane;

/**
 * a controller class that manages the weather dashboard, which includes the
 * primary weather animation for today and the forecast animations for the
 * upcoming days. It initializes the weather animation units based on the
 * provided weather data and handles user interactions such as minimizing and
 * expanding the dashboard.
 * The WeatherDashboardController class is responsible for creating and managing
 * the weather animation units, which are instances of the WeatherCanvasView
 * class. It provides methods to start the dashboard, handle user interactions,
 * and manage the visibility and update state of the animation units.
 * 
 * WeatherDashboardController
 */
public class WeatherDashboardController {

    public WeatherCanvasView todayAnimationUnit;
    public WeatherCanvasView[] forecastAnimations = new WeatherCanvasView[4];

    WeatherDrawingState[] daysOfWeekStates;

    private int sprWidth;
    private int sprHeight;

    /**
     * Constructs a WeatherDashboardController with the specified application,
     * application context, and taskbar offset height. It initializes the weather
     * animation units for today and the forecasted days based on the provided
     * weather data.
     * 
     * @param app        the parent JavaFX application
     * @param appContext shared application state containing weather data,
     *                   forecast data, and screen dimensions
     */
    public WeatherDashboardController(
            Application app,
            ApplicationContext appContext,
            int taskbarOffsetH) {

        // Primary WeatherState for today
        WeatherDrawingState todayState = appContext.weatherDrawingState;
        // WeatherStates for forecasted week
        WeatherDrawingState[] daysOfWeekStates = appContext.DOWDrawingStates;

        this.daysOfWeekStates = daysOfWeekStates;

        sprWidth = 128;
        sprHeight = 128;

        this.todayAnimationUnit = new WeatherCanvasView(0, taskbarOffsetH, sprWidth, sprHeight, todayState);
        for (int i = 0; i < daysOfWeekStates.length; i++) {
            forecastAnimations[i] = new WeatherCanvasView(0, taskbarOffsetH + sprHeight * (i + 1), sprWidth, sprHeight,
                    daysOfWeekStates[i]);
        }
        // setWeatherLayout(true, taskbarOffsetH);

    }

    /**
     * Method should only be called from the WeatherWindowController
     * 
     * @param invert
     * @param taskbarOffsetH
     */
    public void invertLayout(int taskbarOffsetH, boolean invert) {
        int panelCount = 1 + daysOfWeekStates.length;
        int totalStackHeight = panelCount * sprHeight;

        for (int i = 0; i < panelCount; i++) {
            double y;
            if (invert) {
                y = (totalStackHeight - sprHeight) - (i * sprHeight);
            } else {
                y = taskbarOffsetH + (i * sprHeight);
            }

            if (i == 0) {
                todayAnimationUnit.setY(y);
            } else {
                forecastAnimations[i - 1].setY(y);
            }
        }
    }

    public void start(Pane root) {

        // Text t = new Text();
        todayAnimationUnit.addToRoot(root);

        for (int i = 0; i < daysOfWeekStates.length; i++) {
            forecastAnimations[i].addToRoot(root);
        }
    }

    int v = 2; // start at mode 1 open

    public void onMinimizePressed() {
        v++;
        switch (v % 4) {
            case 0:
                todayAnimationUnit.setVisible(false);
                todayAnimationUnit.setShouldUpdate(todayAnimationUnit.isVisible());
                for (WeatherCanvasView animationUnit : forecastAnimations) {
                    animationUnit.setVisible(false);
                    animationUnit.setShouldUpdate(animationUnit.isVisible());
                }
                break;
            case 1:
                todayAnimationUnit.setVisible(true);
                todayAnimationUnit.setShouldUpdate(todayAnimationUnit.isVisible());

                break;
            case 2:
                for (WeatherCanvasView animationUnit : forecastAnimations) {
                    animationUnit.setVisible(true);
                    animationUnit.setShouldUpdate(animationUnit.isVisible());
                }

                break;
            case 3:
                todayAnimationUnit.setVisible(true);
                todayAnimationUnit.setShouldUpdate(todayAnimationUnit.isVisible());
                for (WeatherCanvasView animationUnit : forecastAnimations) {
                    animationUnit.setVisible(false);
                    animationUnit.setShouldUpdate(animationUnit.isVisible());
                }
                break;

            default:
                break;
        }
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

    public void setForecastExpanded(boolean expanded) {
        for (WeatherCanvasView animationUnit : forecastAnimations) {
            animationUnit.setVisible(expanded);
            animationUnit.setShouldUpdate(animationUnit.isVisible());
        }
    }
}
