package cz.tul.ppj.repository;

import cz.tul.ppj.model.Weather;
import cz.tul.ppj.model.WeatherKey;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends ListCrudRepository<Weather, WeatherKey> {

}
