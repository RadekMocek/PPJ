package cz.tul.ppj.service.repository;

import cz.tul.ppj.model.City;
import cz.tul.ppj.model.CityKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends ListCrudRepository<City, CityKey> {

    Optional<City> findByCityKey_State_StateIdAndCityKey_Name(String stateId, String cityName);

    @Query("SELECT c FROM City AS c WHERE c.cityKey.state.stateId = :stateId")
    List<City> findByStateId(@Param("stateId") String stateId);

    @Query("SELECT c FROM City AS c ORDER BY c.cityKey.state.stateId ASC, c.cityKey.name ASC")
    List<City> findAllSorted();

    void deleteByCityKey_State_StateIdAndCityKey_Name(String stateId, String cityName);

}
