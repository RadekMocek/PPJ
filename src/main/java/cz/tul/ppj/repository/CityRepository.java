package cz.tul.ppj.repository;

import cz.tul.ppj.model.City;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends ListCrudRepository<City, Integer> {

}
