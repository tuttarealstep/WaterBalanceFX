
public class WeatherStation {
    
    private double minTemp;
    private double maxTemp;
    private double avgTemp;
    private double rain;
    private double rs;
    private double rhMin;
    private double rhMax;
    private double wind;
    private double altitudine;
    private double latitudine;
    private String date;
    private int julianDay;
    
    public WeatherStation() {
        
        this.minTemp = 0;
        this.maxTemp = 0;
        this.avgTemp = 0;
        this.rs = 0;
        this.rain = 0;
        this.rhMin = 0;
        this.rhMax = 0;
        this.wind = 0;
        this.altitudine = 0;
        this.latitudine = 0;
        this.date = "0";
        this.julianDay = 0;
        
    }
    
    public double getMinTemp() {
        return minTemp;
    }
    public double getMaxTemp() {
        return maxTemp;
    }
    public double getAvgTemp() {
        return avgTemp;
    }
    public double getRain() {
        return rain;
    }
    public double getRs() {
        return rs;
    }
    public double getRhMin() {
        return rhMin;
    }
    public double getRhMax() {
        return rhMax;
    }
    public double getWind() {
        return wind;
    }
    public double getAltitudine() {
        return altitudine;
    }
    public double getLatitudine() {
        return latitudine;
    }
    public String getDate() {
        return date;
    }
    public int getJulianDay() {
        return julianDay;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }
    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }
    public void setAvgTemp(double avgTemp) {
        this.avgTemp = avgTemp;
    }
    public void setRain(double rain) {
        this.rain = rain;
    }
    public void setRs(double rs) {
        this.rs = rs;
    }
    public void setRhMin(double rhMin) {
        this.rhMin = rhMin;
    }
    public void setRhMax(double rhMax) {
        this.rhMax = rhMax;
    }
    public void setWind(double wind) {
        this.wind = wind;
    }
    public void setAltitudine(double altitudine) {
        this.altitudine = altitudine;
    }
    public void setLatitudine(double latitudine) {
        this.latitudine = latitudine;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setJulianDay(int julianDay) {
        this.julianDay = julianDay;
    }    
}