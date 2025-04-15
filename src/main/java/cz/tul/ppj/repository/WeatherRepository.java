package cz.tul.ppj.repository;

import cz.tul.ppj.model.Weather;
import cz.tul.ppj.model.WeatherKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends CrudRepository<Weather, WeatherKey> {
    
}
