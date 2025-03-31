package cz.tul.ppj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

public class WeatherFetcher {

    @Autowired
    private NamedParameterJdbcOperations namedParameterJdbcOperations;


}
