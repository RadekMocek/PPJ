package cz.tul.ppj.service.repository;

import cz.tul.ppj.model.Weather;
import cz.tul.ppj.model.WeatherKey;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherRepository extends ListCrudRepository<Weather, WeatherKey> {

    @Query("SELECT w FROM Weather AS w WHERE w.weatherKey.city.cityKey.state.stateId = :stateId AND w.weatherKey.city.cityKey.name = :cityName")
    List<Weather> findByStateIdAndCityName(@Param("stateId") String stateId, @Param("cityName") String cityName);

    @Query("SELECT w.weatherKey.city.cityKey.state.stateId as stateId, " +
            "w.weatherKey.city.cityKey.name AS cityName, " +
            "COUNT(w) AS weatherCount " +
            "FROM Weather AS w " +
            "GROUP BY stateId, cityName")
    List<CityWeatherSummary> countWeathersByEachCity();

    @Modifying
    @Query("DELETE FROM Weather as w WHERE w.weatherKey.city.cityKey.state.stateId = :stateId AND w.weatherKey.city.cityKey.name = :cityName")
    void deleteByStateIdAndCityName(@Param("stateId") String stateId, @Param("cityName") String cityName);

    @Modifying
    @Query("DELETE FROM Weather as w " +
            "WHERE w.weatherKey.city.cityKey.state.stateId = :stateId " +
            "AND w.weatherKey.city.cityKey.name = :cityName " +
            "AND w.weatherKey.timestamp = :timestamp")
    void deleteByStateIdAndCityNameAndTimestamp(@Param("stateId") String stateId, @Param("cityName") String cityName, @Param("timestamp") long timestamp);

    //

    interface CityWeatherSummary {
        String getStateId();

        String getCityName();

        Long getWeatherCount();
    }

}
