package weather;

public class WeatherData {
    public int ID = -9999;
    public double temp = -9999;
    public double temp_min = -9999;
    public double temp_max = -9999;
    public double feels_like = -9999;
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return 
        "ID: "+ID+"\n"+
        "temp: "+temp+"\n"+
        "temp_min: "+temp_min+"\n"+
        "temp_max: "+temp_max+"\n"+
        "feels_like: "+feels_like+"\n";
    }
    public WeatherData(
            int ID,
            double temp,
            double temp_max, 
            double temp_min,
            double feels_like
        ) {
        
        this.ID          = ID;
        this.temp        = temp;
        this.temp_max    = temp_max;
        this.temp_min    = temp_min;
    }
    public static class WeatherDataToday extends WeatherData{
        public WeatherDataToday(int ID, double temp, double temp_max, double temp_min, double feels_like,long sunrise, long sunset) {
            super(ID, temp, temp_max, temp_min, feels_like);
            this.sunrise=sunrise; this.sunset=sunset;
        }
        long sunrise; long sunset;
    
    }
    public void copyFrom(WeatherData w){
        this.ID          = w.ID;
        this.temp        = w.temp;
        this.temp_max    = w.temp_max;
        this.temp_min    = w.temp_min;
    }
}
