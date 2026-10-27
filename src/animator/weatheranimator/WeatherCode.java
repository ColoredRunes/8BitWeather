package animator.weatheranimator;
public enum WeatherCode {





    THUNDERSTORM(200, "Thunderstorm"),
    DRIZZLE(300, "Drizzle"),
    LIGHT_RAIN(500, "Light Rain"),
    MODERATE_RAIN(501, "Moderate Rain"),
    HEAVY_RAIN(502, "Heavy Rain"),
    FREEZING_RAIN(511, "Freezing Rain"),
    SHOWER_RAIN(520, "Shower Rain"),
    SNOW(600, "Snow"),
    MIST(701, "Mist"),
    SMOKE(711, "Smoke"),
    HAZE(721, "Haze"),
    DUST_WHIRLS(731,  "Dust/Sand Whirls"),
    FOG(741, "Fog"),
    SAND(751, "Sand"),
    DUST(761, "Dust"),
    SQUALL(771, "Squall"),
    TORNADO(781, "Tornado"),
    CLEAR_SKY(800,  "Clear Sky"),
    FEW_CLOUDS(801,  "Few Clouds (11-25%)"),
    SCATTERED_CLOUDS(8802, "Scattered Clouds (25-50%)"),
    BROKEN_CLOUDS(803, "Broken Clouds (51-84%)"),
    OVERCAST(804, "Overcast (85-100%)");




    private final int code;
    private final String description;
    // private WeatherDrawingState copyableState;

    WeatherCode(int code, String description) {
        this.code = code;
        // this.copyableState = state;
        this.description = description;
    }
    // public WeatherDrawingState getWeatherDrawingState() {
    //     return copyableState;
    // }
    public static WeatherCode fromId(int id) {
        for (WeatherCode wc : values()) {
            if (id == wc.code) {
                return wc;
            }
        }
        return null; // or throw IllegalArgumentException
    }

    public String getDescription() {
        return description;
    }
}
