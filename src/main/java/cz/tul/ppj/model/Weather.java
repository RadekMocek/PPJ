package cz.tul.ppj.model;

public class Weather {

    private long timestamp;

    private City city;

    private int temperature;

    private int feelsLike;

    private float pressure;

    private float humidity;

    private String description;

    //

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public City getCity() {
        return city;
    }

    public int getCityId() {
        return city.getCityId();
    }

    public String getCityName() {
        return city.getName();
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(int feelsLike) {
        this.feelsLike = feelsLike;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //


    @Override
    public String toString() {
        return "Weather{" + "timestamp=" + timestamp + ", city=" + getCityName() + ", temperature=" + temperature + ", feelsLike=" + feelsLike + ", pressure=" + pressure + ", humidity=" + humidity + ", description='" + description + '\'' + '}' + '\n';
    }
}
