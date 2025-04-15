package cz.tul.ppj.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Weather")
public class Weather {

    @EmbeddedId
    private WeatherKey weatherKey;

    @Column(name = "temperature")
    private int temperature;

    @Column(name = "feelslike")
    private int feelsLike;

    @Column(name = "pressure")
    private float pressure;

    @Column(name = "humidity")
    private float humidity;

    @Column(name = "description")
    private String description;

    //

    public WeatherKey getWeatherKey() {
        return weatherKey;
    }

    public void setWeatherKey(WeatherKey weatherKey) {
        this.weatherKey = weatherKey;
    }

    public long getTimestamp() {
        return weatherKey.getTimestamp();
    }

    public void setTimestamp(long timestamp) {
        weatherKey.setTimestamp(timestamp);
    }

    public City getCity() {
        return weatherKey.getCity();
    }

    public int getCityId() {
        return weatherKey.getCityId();
    }

    public String getCityName() {
        return weatherKey.getCityName();
    }

    public void setCity(City city) {
        weatherKey.setCity(city);
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
        return "Weather{" + "timestamp=" + getTimestamp() + ", city=" + getCityName() + ", temperature=" + temperature + ", feelsLike=" + feelsLike + ", pressure=" + pressure + ", humidity=" + humidity + ", description='" + description + '\'' + '}' + '\n';
    }
}
