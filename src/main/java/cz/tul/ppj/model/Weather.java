package cz.tul.ppj.model;

public class Weather {

    private long timestamp;

    //private City city;
    private int cityId;

    private float temperature;

    private float feelsLike;

    private int pressure;

    private int humidity;

    private String description;

    //

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /*
    public City getCity() {
        return city;
    }
    /**/
    public int getCityId() {
        return cityId;
    }

    /*
    public void setCity(City city) {
        this.city = city;
    }
    /**/
    public void setCityId(int cityId) {
        this.cityId = cityId;
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
}
