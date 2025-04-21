package cz.tul.ppj.repository;

import cz.tul.ppj.model.City;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends ListCrudRepository<City, Integer> {

    @Query("SELECT c FROM City AS c WHERE c.state.stateId = :stateId")
    List<City> findByStateId(@Param("stateId") String stateId);

}
