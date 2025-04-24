package cz.tul.ppj.service.repository;

import cz.tul.ppj.model.State;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends ListCrudRepository<State, String> {

}
