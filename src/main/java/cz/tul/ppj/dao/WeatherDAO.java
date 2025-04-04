package cz.tul.ppj.dao;

import cz.tul.ppj.model.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class WeatherDAO {

    @Autowired
    private NamedParameterJdbcOperations jdbc;

    @Transactional
    public boolean create(Weather weather) {

        var params = new BeanPropertySqlParameterSource(weather);

        return jdbc.update(
                "INSERT INTO WEATHER (timestamp, cityId, temperature, feelsLike, pressure, humidity, description)" +
                        "VALUES (:timestamp, :cityId, :temperature, :feelsLike, :pressure, :humidity, :description)"
                , params) == 1;
    }

    public boolean exists(long timestamp, int cityId) {
        return jdbc.queryForObject("SELECT COUNT(*) FROM WEATHER WHERE timestamp = :timestamp AND cityId = :cityId", new MapSqlParameterSource("timestamp", timestamp).addValue("cityId", cityId), Integer.class) > 0;
    }

    public List<Weather> getAll() {
        return jdbc.query("SELECT * FROM WEATHER", BeanPropertyRowMapper.newInstance(Weather.class));
    }

    public void deleteAll() {
        jdbc.getJdbcOperations().execute("TRUNCATE WEATHER");
    }

}
