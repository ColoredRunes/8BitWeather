package weather;

import java.util.List;

public class ForecastResponse {
    public List<ForecastItem> list;

    public static class ForecastItem {
        public long dt;
        public String dt_txt;
        public List<Weather> weather;
        public Main main;
    }

    public static class Weather {
        public int id;
        public String main;
        public String description;
        public String icon;
    }

    public static class Main {
        public double temp;
        public double feels_like;
        public double temp_min;
        public double temp_max;
    }

    @Override
    public String toString() {
        String ret = "";
        for (ForecastItem f : list) {
            ret += ("DATE:" + f.dt_txt + "\n");
            for (Weather w : f.weather) {
                ret += ("--WEATHERID: " + w.id + "\n");
                ret += ("-- --DESCRIPTION: " + w.description + "\n");
            }
        }
        return ret;
        // return "temp: "+tempa
        // +"feels_like: "+feels_like
        // +"temp_min: "+temp_min
        // +"temp_max: "+temp_max;
    }
}
