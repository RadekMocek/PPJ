package cz.tul.ppj.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "Weather")
public class Weather {

    @EmbeddedId
    private WeatherKey weatherKey;

    @Column(name = "temperature")
    private float temperature;

    @Column(name = "feelslike")
    private float feelsLike;

    @Column(name = "pressure")
    private int pressure;

    @Column(name = "humidity")
    private int humidity;

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

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(float feelsLike) {
        this.feelsLike = feelsLike;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
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
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Weather weather = (Weather) o;
        return temperature == weather.temperature && feelsLike == weather.feelsLike && Float.compare(pressure, weather.pressure) == 0 && Float.compare(humidity, weather.humidity) == 0 && Objects.equals(weatherKey, weather.weatherKey) && Objects.equals(description, weather.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weatherKey, temperature, feelsLike, pressure, humidity, description);
    }

    //

    @Override
    public String toString() {
        return "Weather{" + "timestamp=" + getTimestamp() + ", city=" + getCityName() + ", temperature=" + temperature + ", feelsLike=" + feelsLike + ", pressure=" + pressure + ", humidity=" + humidity + ", description='" + description + '\'' + '}' + '\n';
    }

    //

    public Weather() {
    }

    public Weather(long timestamp, City city, float temperature, float feelsLike, int pressure, int humidity, String description) {
        this.weatherKey = new WeatherKey(timestamp, city);
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.pressure = pressure;
        this.humidity = humidity;
        this.description = description;
    }

}
