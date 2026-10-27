package weather;

import java.util.List;

public class OneCallResponse {
    public Current current;
    public List<Daily> daily;

    public static class Current {
        public long dt;
        public long sunrise;
        public long sunset;
        public List<Weather> weather;
    }

    public static class Daily {
        public long dt;
        public long sunrise;
        public long sunset;
        public List<Weather> weather;
        public Temp temp;
    }

    public static class Temp {
        public double day;
        public double min;
        public double max;
    }

    public static class Weather {
        public int id;
        public String main;
        public String description;
        public String icon;
    }
}
