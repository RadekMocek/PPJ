package cz.tul.ppj.dao;

import cz.tul.ppj.model.City;
import cz.tul.ppj.model.State;
import cz.tul.ppj.model.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.List;

public class WeatherDAO {

    @Autowired
    private NamedParameterJdbcOperations jdbc;

    @Transactional
    public boolean create(Weather weather) {

        var params = new BeanPropertySqlParameterSource(weather);

        return jdbc.update("INSERT INTO WEATHER (timestamp, cityId, temperature, feelsLike, pressure, humidity, description)" + "VALUES (:timestamp, :cityId, :temperature, :feelsLike, :pressure, :humidity, :description)", params) == 1;
    }

    public boolean exists(long timestamp, int cityId) {
        return jdbc.queryForObject("SELECT COUNT(*) FROM WEATHER WHERE timestamp = :timestamp AND cityId = :cityId", new MapSqlParameterSource("timestamp", timestamp).addValue("cityId", cityId), Integer.class) > 0;
    }

    public List<Weather> getAll() {
        return jdbc.query("SELECT * FROM WEATHER", BeanPropertyRowMapper.newInstance(Weather.class));
    }

    public void deleteAll() {
        jdbc.getJdbcOperations().execute("TRUNCATE TABLE WEATHER");
    }

    public List<Weather> getAllViaJoin() {
        return jdbc.query("SELECT * FROM WEATHER as W, CITY as C, STATE as S WHERE W.cityId = C.id AND C.stateId = S.id", (ResultSet rs, int _) -> {
            var state = new State();
            state.setStateId(rs.getString("STATE.id"));
            state.setName(rs.getString("STATE.name"));
            var city = new City();
            city.setCityId(rs.getInt("CITY.id"));
            city.setState(state);
            city.setName(rs.getString("CITY.name"));
            var weather = new Weather();
            weather.setTimestamp(rs.getLong("WEATHER.timestamp"));
            weather.setCity(city);
            weather.setTemperature(rs.getInt("WEATHER.temperature"));
            weather.setFeelsLike(rs.getInt("WEATHER.feelsLike"));
            weather.setPressure(rs.getFloat("WEATHER.pressure"));
            weather.setHumidity(rs.getFloat("WEATHER.humidity"));
            weather.setDescription(rs.getString("WEATHER.description"));
            return weather;
        });
    }
}
