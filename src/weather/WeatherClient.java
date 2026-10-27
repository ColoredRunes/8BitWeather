package weather;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import java.net.URI;
import com.google.gson.*;

public class WeatherClient {
    public WeatherClient(){}
    private static final String API_KEY = ApiKey.API_KEY;
    private boolean isNight;

    private String getResponse(String url) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public boolean isNight() {
        return isNight;
    }

    public WeatherData[] callData(String city) throws Exception {
        
        Gson gson = new Gson();

        // STEP 1 — CURRENT WEATHER
        
        String weathersURL  = "https://api.openweathermap.org/data/2.5/weather?q="+ city + "&units=imperial&appid=" + API_KEY;
        String forecastURL = "https://api.openweathermap.org/data/2.5/forecast?q="+ city + "&units=imperial&appid=" + API_KEY;

        String jsonCurrent = getResponse(weathersURL);
        String jsonForecast = getResponse(forecastURL);

        WeatherResponse current = gson.fromJson(jsonCurrent, WeatherResponse.class);
        if (current == null || current.main == null || current.sys == null || current.weather == null || current.weather.isEmpty()) {
            throw new IllegalArgumentException("Invalid city or API response for: " + city);
        }

        long now = current.dt;
        String thisDay = Instant.ofEpochSecond(1791249222).toString().substring(0,10);

        // String day = current.
        long sunrise = current.sys.sunrise;
        long sunset = current.sys.sunset;

        // Set night/day flag for outside use
        isNight = !(now >= sunrise && now < sunset);

         
        ForecastResponse forecast = gson.fromJson(jsonForecast, ForecastResponse.class);
        if (forecast == null || forecast.list == null || forecast.list.isEmpty()) {
            throw new IllegalArgumentException("Forecast lookup failed for city: " + city);
        }
        Map<String, List<ForecastResponse.ForecastItem>> days = new LinkedHashMap<>();
        
        
        
        String lastDay = "";
        Map<String, Double[]> temps = new LinkedHashMap<>();
        Double[] min_max_avg = new Double[] {999.0,-999.0,0.0};
        double avgTemp = 0.0;
        
        // 8 iterations per date //

        //_________________________________________
        for (ForecastResponse.ForecastItem item : forecast.list) {
            String day = item.dt_txt.substring(0, 10); // "YYYY-MM-DD"
            days.computeIfAbsent(day, k -> new ArrayList<>()).add(item);
            if(lastDay.equals("")){ lastDay = day; }
            if(!day.equals(lastDay)){
                min_max_avg[2]=avgTemp /8;
                min_max_avg[2] =(double) Math.round( min_max_avg[2]*100)/100;
                temps.put(lastDay, min_max_avg.clone());
                min_max_avg = new Double[] {999.0,-999.0,0.0 };
                lastDay = day;

                avgTemp = 0.0;
                // continue;
            }
            avgTemp += item.main.temp;
            min_max_avg[0] =(double) Math.round(Math.min(item.main.temp_min, min_max_avg[0])*100)/100;
            min_max_avg[1] =(double) Math.round(Math.max(item.main.temp_max, min_max_avg[1])*100)/100;
            // min_max_avg[2] =(double) Math.round( min_max_avg[2]*100)/100;
        }
        //post loop flush
        min_max_avg[2] =(double) Math.round( min_max_avg[2]*100)/100;
        temps.put(lastDay, min_max_avg.clone());
        //_________________________________________
        // int idx = 1;

        // boolean skipToday = true;
        WeatherData[] daysOfData = new WeatherData[5];
        daysOfData[0] = new WeatherData(current.weather.get(0).id, current.main.temp, current.main.tempMax, current.main.tempMin, current.main.feelsLike); 
        List<String> keys = new ArrayList<>(days.keySet());
        for (int idx = 1; idx <= 4; idx++) {
            
            String day = keys.get(idx);

            
            List<ForecastResponse.ForecastItem> items = days.get(day);
            ForecastResponse.ForecastItem mid = items.get(items.size() / 2);

            daysOfData[idx] = new WeatherData(mid.weather.get(0).id, temps.get(day)[2], temps.get(day)[1], temps.get(day)[0], mid.main.feels_like);

        }
        return daysOfData;
    }
}
