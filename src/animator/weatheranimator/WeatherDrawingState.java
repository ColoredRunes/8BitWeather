package animator.weatheranimator;

import java.util.Date;

import weather.WeatherData;

/**
 * A drawing state argued to 
 */
public class WeatherDrawingState {

    public int drawRain = -1;// 0-4
    public int drawSnow = -1;// 0-4
    public int drawWind = -1;// 0-4
    public int drawClouds = -1;// 0-4
    public int drawFog = -1; // 0-4

    public int drawLightning = -1;// (0|1)
    public int drawSnowOnTheGround = -1;// (0|1)

    public int drawNight = -1; // (0|1)
    public int drawAlert = -1; // (0|1)

    public int weatherID = -1;// Intercepted ID from OpenWeather

    public boolean error = false;
    private int[] allDraws = new int[] {
            drawRain,
            drawSnow,
            drawWind,
            drawClouds,
            drawLightning,
            drawFog,
            drawSnowOnTheGround,

            drawNight,
    };
    public String dayString;
    public WeatherData weatherData;
    public boolean connectError;
    public boolean limitError;

    // public String c = Calendar.get(Calendar.DAY_OF_WEEK);
    public WeatherData getWeatherData() {
        return weatherData;
    }

    @Override
    public String toString() {
        return "WEATHERID = " + weatherID + "\n" +
                "drawRain = " + drawRain + "\n" +
                "drawSnow = " + drawSnow + "\n" +
                "drawWind = " + drawWind + "\n" +
                "drawClouds = " + drawClouds + "\n" +
                "drawLightning = " + drawLightning + "\n" +
                "drawFog = " + drawFog + "\n" +
                "drawSnowOnTheGround = " + drawSnowOnTheGround + "\n" +
                "drawNight = " + drawNight + "\n" +
                "DAY = " + dayString + "\n" +
                "time" + "TIME = " + new Date().toString();
    }

    public int[] getAll() {
        return allDraws;
    }

    /**
     * WeatherDrawState which can become configured by simply arguing the
     * weathercode. This state determines draw logic for the Icon unit.
     * 
     * @param weatherID OpenWeather API call.ID
     */

    public WeatherDrawingState(WeatherData weatherData, int isNight, int day) {
        int weatherID = weatherData.ID;
        // int weatherID = 202;
        this.weatherData = weatherData;
        
        switch (day) {
            case 1:
                dayString = "Monday";
                break;
            case 2:
                dayString = "Tuesday";
                break;
            case 3:
                dayString = "Wednesday";
                break;
            case 4:
                dayString = "Thursday";
                break;
            case 5:
                dayString = "Friday";
                break;
            case 6:
                dayString = "Saturday";
                break;
            case 7:
                dayString = "Sunday";
                break;

            default:
                dayString = "UnknownDay ID: " + day;
                break;
        }

        this.weatherID = weatherID;
        if (weatherID == -1) {
            error = true;
            return;
        }
        if (weatherID == -2) {
            connectError = true;
            return;
        }

        // this.drawNight = isNight;
        this.drawNight = isNight;
        if (weatherID < 0) {
            error = true;
        }
        if (weatherID < 300) { // 2xx: Thunderstorm
            drawClouds = 4;
            drawLightning = 2;
            drawWind = 1;
           
            switch (weatherID) {
                case 200:
                    drawRain = 1;
                    break; // thunderstorm with light rain
                case 201:
                    drawRain = 2;
                    break; // thunderstorm with rain
                case 202:
                    drawRain = 3;
                    drawWind = 2;
                    break; // thunderstorm with heavy rain
                case 210:
                    break; // light thunderstorm
                case 211:
                    break; // thunderstorm
                case 212:
                    drawWind = 2;
                    break; // heavy thunderstorm
                case 221:
                    drawWind = 2;
                    break; // ragged thunderstorm
                case 230:
                    drawRain = 1;
                    break; // thunderstorm with light drizzle
                case 231:
                    drawRain = 1;
                    break; // thunderstorm with drizzle
                case 232:
                    drawRain = 2;
                    break; // thunderstorm with heavy drizzle
            }

        } else if (weatherID < 400) {
            // * 3xx: Drizzle // DRAW DRIZZLE
            drawClouds = 4;
            drawRain = 1;

        } else if (weatherID < 600) {
            // * 5xx: Rain // DRAW RAIN
            drawClouds = 4;
            handleRainSwitches(weatherID);
        } else if (weatherID < 700) {
            // * 6xx: Snow // DRAW SNOW
            drawClouds = 4;
            handleSnowSwitches(weatherID);
            if (drawSnow >= 3) {
                drawSnowOnTheGround = 1;
            }
        } else if (weatherID < 800) {
            // * 7xx: Atmosphere (fog, haze, dust etc.) // DRAW FOG
            handleAtmosphereSwitches(weatherID);
            drawFog = 4;

        } else if (weatherID == 800) {
            drawClouds = 0;
            // * 800: Clear sky // [DONT DRAW OVERLAY]
        } else if (weatherID == 801) {
            drawClouds = 1;
            // * 801=few, // DRAW FEW
        } else if (weatherID == 802) {
            drawClouds = 2;
            // * 802=scattered, // DRAW SCATTERED
        } else if (weatherID == 803) {
            drawClouds = 3;
            // * 803=broken, // DRAW BROKEN
        } else if (weatherID == 804) {
            drawClouds = 4;
            // * 804=overcast // DRAW CLOUDS
        }
    }

    // 701 → Mist
    // 711 → Smoke
    // 721 → Haze
    // 731 → Dust/Sand whirls
    // 741 → Fog
    // 751 → Sand
    // 761 → Dust
    // 771 → Squall
    // 781 → Tornado
    private void handleAtmosphereSwitches(int weatherID2) {
        drawFog = 1;
        switch (weatherID2) {
            case 701: // Mist
                drawClouds = 2;
                break;
            case 711: // Smoke
                // drawClouds = 2;
                break;
            case 721: // Haze
                drawClouds = 3;
                break;
            case 731: // Dust/Sand whirls
                drawWind = 2;
                drawClouds = 3;
                break;
            case 741: // Fog
                drawClouds = 4;
                break;
            case 751: // Sand
                drawClouds = 2;
                drawWind = 3;
                break;
            case 761: // Dust
                drawClouds = 2;
                drawWind = 3;
                break;
            case 771: // Squall
                drawWind = 4;
                drawClouds = 6;
                break;
            case 781: // Tornado
                drawWind = 4;
                drawClouds = 8;
                // drawAlert = 1;
                break;
            default:
                drawWind = -1;
                drawClouds = -1;
        }
        
    }

    private void handleRainSwitches(int weatherID) {
        switch (weatherID) {
            case 500:
                // 500 — light rain
                drawRain = 1;

                break;
            case 501:
                drawRain = 2;

                // 501 — moderate rain

                break;
            case 502:
                drawRain = 3;
                // 502 — heavy intensity rain

                break;
            case 503:
                drawRain = 4;
                // 503 — very heavy rain

                break;
            case 504:
                drawRain = 4;
                drawWind = 3;
                // 504 — extreme rain

                break;
            case 511:
                drawRain = 3;
                drawSnow = 3;
                // 511 — freezing rain (ice pellets)

                break;
            case 520:
                // 520 — light intensity shower rain
                drawRain = 1;
                break;
            case 521:
                // 521 — shower rain
                drawRain = 2;
                break;
            case 522:
                // 522 — heavy intensity shower rain
                drawRain = 4;
                break;
            case 531:
                // 531: Ragged shower rain
                drawRain = 4;
                drawWind = 4;
                break;

            default:
                drawRain = -1;
        }

    }

    private void handleSnowSwitches(int weatherID) {
        switch (weatherID) {

            case 600:
                // 600 — Light snow (small flakes, gentle fall)
                drawSnow = 1;
                break;

            case 601:
                // 601 — Snow (steady, moderate snowfall)
                drawSnow = 2;
                break;

            case 602:
                // 602 — Heavy snow (large flakes, intense fall)
                drawSnow = 3;
                break;

            case 611:
                // 611 — Sleet (wet snow + rain mix)
                drawSnow = 2; // sleet overlay
                drawRain = 2;
                break;

            case 612:
                // 612 — Light shower sleet (intermittent sleet bursts)
                drawSnow = 3;
                drawSnow = 3;
                break;

            case 613:
                // 613 — Shower sleet (moderate sleet bursts)
                drawSnow = 3;
                drawSnow = 3;
                break;

            case 615:
                // 615 — Light rain and snow (mixed precipitation)
                drawSnow = 2; // light snow + rain overlay
                break;

            case 616:
                // 616 — Rain and snow (balanced mix)
                drawSnow = 2; // sleet overlay
                drawRain = 2;
                break;

            case 620:
                // 620 — Light shower snow (intermittent light snow showers)
                drawSnow = 3; // sleet overlay
                // drawRain = 2;
                break;

            case 621:
                // 621 — Shower snow (moderate snow showers)
                drawSnow = 6;
                break;

            case 622:
                // 622 — Heavy shower snow (intense, chaotic snow bursts)
                drawSnow = 8;
                break;

            default:
                // Any other 6xx fallback (shouldn't happen)
                drawSnow = 2;
        }
    }

    public void copyFrom(WeatherDrawingState newState) {

        this.weatherData.copyFrom(newState.weatherData);

        this.dayString = newState.dayString;
        this.weatherID = newState.weatherID;

        this.drawRain = newState.drawRain;
        this.drawSnow = newState.drawSnow;
        this.drawWind = newState.drawWind;
        this.drawClouds = newState.drawClouds;
        this.drawFog = newState.drawFog;

        this.drawLightning = newState.drawLightning;
        this.drawSnowOnTheGround = newState.drawSnowOnTheGround;

        this.drawNight = newState.drawNight;
        this.drawAlert = newState.drawAlert;
        this.allDraws = new int[] {
                drawRain,
                drawSnow,
                drawWind,
                drawClouds,
                drawLightning,
                drawFog,
                drawSnowOnTheGround,

                drawNight
        };

        // weatherID = -1;// Intercepted ID from OpenWeather

    }

}
