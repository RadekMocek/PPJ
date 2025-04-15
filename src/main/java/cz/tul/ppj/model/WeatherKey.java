package cz.tul.ppj.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class WeatherKey implements Serializable {
    @Column(name = "timestamp")
    private long timestamp;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cityid")
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

    public int getCityId() {
        return city.getCityId();
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
}
