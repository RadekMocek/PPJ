package cz.tul.ppj.repository;

import cz.tul.ppj.model.City;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends ListCrudRepository<City, Integer> {

    //public List<City> findByStateId();

}
