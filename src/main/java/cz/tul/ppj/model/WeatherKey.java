package cz.tul.ppj.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Embeddable
public class WeatherKey implements Serializable {
    @Column(name = "timestamp")
    private long timestamp;

    @ManyToOne
    @JoinColumn(name = "cityId")
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
}
