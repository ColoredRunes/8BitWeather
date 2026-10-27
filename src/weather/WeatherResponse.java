package weather;

import java.util.List;

public class WeatherResponse {
    public Coord coord;
    public List<Weather> weather;
    public String base;
    public Main main;
    public int visibility;
    public Wind wind;
    public Clouds clouds;
    public long dt;
    public Sys sys;
    public int timezone;
    public int id;
    public String name;
    public int cod;
    // ---------- Nested Classes ----------

    public static class Coord {
        public double lon;
        public double lat;    }

    public static class Weather {
        public int id;
        public String main;
        public String description;
        public String icon;    }

    public static class Main {
        public double temp;
        public double feelsLike;
        public double tempMin;
        public double tempMax;
        public int pressure;
        public int humidity;
        public int seaLevel;
        public int grndLevel;    }

    public static class Wind {
        public double speed;
        public int deg;    }

    public static class Clouds {
        public int all;    }

    public static class Sys {
        public int type;
        public int sysId;
        public String country;
        public long sunrise;
        public long sunset;    }

    // Optional: Pretty-print automatically when you print the object
    @Override
    public String toString() {
        return new com.google.gson.GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
