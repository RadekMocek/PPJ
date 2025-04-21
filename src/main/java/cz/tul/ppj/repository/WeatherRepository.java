package cz.tul.ppj.repository;

import cz.tul.ppj.model.Weather;
import cz.tul.ppj.model.WeatherKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherRepository extends ListCrudRepository<Weather, WeatherKey> {

    @Query("SELECT w FROM Weather AS w WHERE w.weatherKey.city.cityId = :cityId")
    List<Weather> findByCityId(@Param("cityId") int cityId);

}
