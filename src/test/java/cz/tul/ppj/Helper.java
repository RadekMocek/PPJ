package cz.tul.ppj;

import cz.tul.ppj.model.City;
import cz.tul.ppj.model.State;
import cz.tul.ppj.model.Weather;
import cz.tul.ppj.model.WeatherKey;

public final class Helper {
    private Helper() {
    }

    public static State initializeState(String id, String name) {
        var result = new State();
        result.setStateId(id);
        result.setName(name);
        return result;
    }

    public static City initializeCity(int id, State state, String name) {
        var result = new City();
        result.setCityId(id);
        result.setState(state);
        result.setName(name);
        return result;
    }

    public static Weather initializeWeather(long timestamp, City city, float temperature, float feelsLike, int pressure, int humidity, String description) {
        var result = new Weather();
        var weatherKey = new WeatherKey();
        weatherKey.setTimestamp(timestamp);
        weatherKey.setCity(city);
        result.setWeatherKey(weatherKey);
        result.setTemperature(temperature);
        result.setFeelsLike(feelsLike);
        result.setPressure(pressure);
        result.setHumidity(humidity);
        result.setDescription(description);
        return result;
    }

}
