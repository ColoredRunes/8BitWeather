package weather;

import java.time.Instant;
import java.time.LocalDate;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import animator.weatheranimator.AnimationControllerW;
import animator.weatheranimator.AnimationConveyorW;
import animator.weatheranimator.AnimationHandler;
import animator.weatheranimator.WeatherDrawingState;
import javafx.application.Platform;

public class WeatherPoller {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // private WeatherClient WeatherClient = new WeatherClient();
    private String city;

    public WeatherDrawingState weatherDrawingState;
    private WeatherDrawingState[] DOWDrawingState;

    // Pulled seperately for same cause; could be more homogenous.
    AnimationHandler primaryAnimationManager;
    private AnimationConveyorW[] forecastAnimations;

    private int day = LocalDate.now().getDayOfWeek().getValue();

        
    /**
     * Should probably split this into two methods?
     * efficiency probably negligible in this context.
     * @param city
     * @return
     * @throws Exception
     */
    private boolean fetchOk;
    private WeatherDrawingState[] procureWeatherDrawingState(String city) {
        WeatherData[] data = null;
        fetchOk = true;
        try {
            data = WeatherClient.callData(city);
            if (data == null || data.length < 5) {
                throw new IllegalStateException("Weather data incomplete for city: " + city);
            }
        } catch (Exception e) {
            fetchOk = false;
            return  null;
        }
        // Conditional nighttime logic
        Instant now = Instant.now();
        WeatherData.WeatherDataToday t =
            (WeatherData.WeatherDataToday) data[0];
        long sunset = t.sunset;
        long sunrise = t.sunrise;
        boolean isNight =
                now.isBefore(Instant.ofEpochSecond(sunrise))
            || now.isAfter(Instant.ofEpochSecond(sunset));

        WeatherDrawingState today = new WeatherDrawingState(data[0], isNight ? 1 : 0,day);

        // NEXT 4 DAYS
        WeatherDrawingState[] days = new WeatherDrawingState[5];
        days[0] = today;

        for (int i = 1; i < 5; i++) {
            days[i] = new WeatherDrawingState(data[i],0,((day+(i-1))%7)+1); // no day/night for forecast
        }

        return days;
    }

    
    public WeatherPoller(AnimationControllerW controller, WeatherDrawingState weatherDrawingState, WeatherDrawingState[] DOWDrawingStates) {
        // this.city = city;
        this.weatherDrawingState = weatherDrawingState;
        this.primaryAnimationManager = controller.todayAnimationUnit.manager;
        this.forecastAnimations = controller.forecastAnimations;
        this.DOWDrawingState = DOWDrawingStates;
    }

    public void start(String city, Consumer<Boolean> onFirstFetch) {
        this.city = city;
        AtomicBoolean first = new AtomicBoolean(true);
        scheduler.scheduleAtFixedRate(() -> { boolean ok = poll(); if(first.getAndSet(false)) onFirstFetch.accept(ok); }, 0, 15, TimeUnit.MINUTES);
    }
    public void stop() {
        scheduler.shutdownNow();
    }

    boolean poll() {
        try {
            //NO BUILT IN LIMIT ERRORS
            WeatherDrawingState[] states = procureWeatherDrawingState(city);
            WeatherDrawingState newState = states[0];
            
            Platform.runLater(() -> {

                weatherDrawingState.copyFrom(newState);
                primaryAnimationManager.procureAnim_Contents(newState);
                for (int i = 0; i < forecastAnimations.length; i++) {
                    WeatherDrawingState state = states[i + 1]; // shift by 1
                    DOWDrawingState[i].copyFrom(state);
                    forecastAnimations[i].manager.procureAnim_Contents(state);
                }
            });
            return fetchOk;
        } catch (Exception ex) {
            ex.printStackTrace();
            
            // primaryAnimationManager.procureAnim_Contents(new WeatherDrawingState(new WeatherData(800, -999.0, -999.0, -999.0, -999.0),-1,-1));
            return false;
        }
    }
    private ScheduledFuture<?> task;

public void cancel() { if (task != null) task.cancel(false); }

}
