package cz.tul.ppj.dao;

import cz.tul.ppj.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class CityDAO {

    @Autowired
    private NamedParameterJdbcOperations jdbc;

    @Transactional
    public boolean create(City city) {

        var params = new BeanPropertySqlParameterSource(city);

        return jdbc.update("INSERT INTO CITY (id, stateId, name) VALUES (:cityId, :stateId, :name)", params) == 1;
    }

    public boolean exists(int id) {
        return jdbc.queryForObject("SELECT COUNT(*) FROM CITY WHERE id=:id", new MapSqlParameterSource("id", id), Integer.class) > 0;
    }

    public List<City> getAll() {
        return jdbc.query("SELECT * FROM CITY", BeanPropertyRowMapper.newInstance(City.class));
    }

    public void deleteAll() {
        jdbc.getJdbcOperations().execute("TRUNCATE CITY");
    }

}
