package engine;

import animator.weatheranimator.WeatherDrawingState;
import weather.WeatherData;
import weather.WeatherPoller;

/**
 * Application layer currently only possessing state contexts for animations.
 * ApplicationContext
 */
public class ApplicationContext {
    public String city = "Los%20Angeles";
    public boolean offline = false;
    public WeatherPoller weatherPoller;

    public WeatherDrawingState weatherDrawingState = new WeatherDrawingState(new WeatherData(800, -999.0, -999.0, -999.0, -999.0),-1,-1); //start sunny
    public WeatherDrawingState[] DOWDrawingStates = new WeatherDrawingState[]{
        new WeatherDrawingState(new WeatherData(800, -999.0, -999.0, -999.0, -999.0),-1,-5),
        new WeatherDrawingState(new WeatherData(800, -999.0, -999.0, -999.0, -999.0),-1,-5),
        new WeatherDrawingState(new WeatherData(800, -999.0, -999.0, -999.0, -999.0),-1,-5),
        new WeatherDrawingState(new WeatherData(800, -999.0, -999.0, -999.0, -999.0),-1,-5)
        
    } ; //start sunny
public double screenWidth;
    public double screenHeight;

}
