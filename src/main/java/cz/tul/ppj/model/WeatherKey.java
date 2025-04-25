package cz.tul.ppj.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class WeatherKey implements Serializable {

    @Column(name = "timestamp")
    private long timestamp;

    @ManyToOne//(cascade = CascadeType.REMOVE)
    @JoinColumns(
            value = {
                    @JoinColumn(name = "state"),
                    @JoinColumn(name = "name")
            },
            foreignKey = @ForeignKey(name = "FK_WEATHER_CITY")
    )
    private City city;

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

    public String getCityName() {
        return city.getName();
    }

    public void setCity(City city) {
        this.city = city;
    }

    //

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WeatherKey that = (WeatherKey) o;
        return timestamp == that.timestamp && Objects.equals(city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, city);
    }

    //

    public WeatherKey() {
    }

    public WeatherKey(long timestamp, City city) {
        this.timestamp = timestamp;
        this.city = city;
    }

}
