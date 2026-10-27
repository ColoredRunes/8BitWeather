package weather;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {
    public static ForecastResponse toForecastResponse(WeatherResponse weatherResponse) {
        ForecastResponse forecastResponse = new ForecastResponse();
        ForecastResponse.ForecastItem forecastItem = new ForecastResponse.ForecastItem();
        forecastItem.dt = weatherResponse.dt;
        forecastItem.dt_txt = ""; // Not available in WeatherResponse
        for (Weather weather : weatherResponse.weather) {
            ForecastResponse.Weather forecastWeather = new ForecastResponse.Weather();
            forecastWeather.id = weather.id;
            forecastWeather.main = weather.main;
            forecastWeather.description = weather.description;
            forecastWeather.icon = weather.icon;
            forecastItem.weather.add(forecastWeather);
            
        }
        ForecastResponse.Main forecastMain = new ForecastResponse.Main();
        forecastMain.temp = weatherResponse.main.temp;
        forecastItem.main = forecastMain;

        forecastResponse.list = List.of(forecastItem);
        
        return forecastResponse;
    }
    @SerializedName("coord")
    public Coord coord;

    @SerializedName("weather")
    public List<Weather> weather;

    @SerializedName("base")
    public String base;

    @SerializedName("main")
    public Main main;

    @SerializedName("visibility")
    public int visibility;

    @SerializedName("wind")
    public Wind wind;

    @SerializedName("clouds")
    public Clouds clouds;

    @SerializedName("dt")
    public long dt;

    @SerializedName("sys")
    public Sys sys;

    @SerializedName("timezone")
    public int timezone;

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("cod")
    public int cod;

    // ---------- Nested Classes ----------

    public static class Coord {
        @SerializedName("lon")
        public double lon;

        @SerializedName("lat")
        public double lat;
    }

    public static class Weather {
        @SerializedName("id")
        public int id;

        @SerializedName("main")
        public String main;

        @SerializedName("description")
        public String description;

        @SerializedName("icon")
        public String icon;
    }

    public static class Main {
        @SerializedName("temp")
        public double temp;

        @SerializedName("feels_like")
        public double feelsLike;

        @SerializedName("temp_min")
        public double tempMin;

        @SerializedName("temp_max")
        public double tempMax;

        @SerializedName("pressure")
        public int pressure;

        @SerializedName("humidity")
        public int humidity;

        @SerializedName("sea_level")
        public int seaLevel;

        @SerializedName("grnd_level")
        public int grndLevel;
    }

    public static class Wind {
        @SerializedName("speed")
        public double speed;

        @SerializedName("deg")
        public int deg;
    }

    public static class Clouds {
        @SerializedName("all")
        public int all;
    }

    public static class Sys {
        @SerializedName("type")
        public int type;

        @SerializedName("id")
        public int sysId;

        @SerializedName("country")
        public String country;

        @SerializedName("sunrise")
        public long sunrise;

        @SerializedName("sunset")
        public long sunset;
    }

    // Optional: Pretty-print automatically when you print the object
    @Override
    public String toString() {
        return new com.google.gson.GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
