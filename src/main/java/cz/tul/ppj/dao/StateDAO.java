package cz.tul.ppj.dao;

import cz.tul.ppj.model.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class StateDAO {

    @Autowired
    private NamedParameterJdbcOperations jdbc;

    @Transactional
    public boolean create(State state) {

        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("id", state.getStateId());
        params.addValue("name", state.getName());

        return jdbc.update("INSERT INTO STATE (id, name) VALUES (:id, :name)", params) == 1;
    }

    public boolean exists(String id) {
        return jdbc.queryForObject("SELECT COUNT(*) FROM STATE WHERE id=:id", new MapSqlParameterSource("id", id), Integer.class) > 0;
    }

    public List<State> getAll() {
        return jdbc.query("SELECT * FROM STATE", BeanPropertyRowMapper.newInstance(State.class));
    }

    public void deleteAll() {
        jdbc.getJdbcOperations().execute("TRUNCATE STATE");
    }

}
