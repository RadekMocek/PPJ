package cz.tul.ppj.service.jpa;

import cz.tul.ppj.model.State;
import cz.tul.ppj.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StateService {

    @Autowired
    private StateRepository stateRepository;

    public void create(State state) {
        stateRepository.save(state);
    }

    public void createBulk(List<State> states) {
        stateRepository.saveAll(states);
    }

    public boolean exists(String stateId) {
        return stateRepository.existsById(stateId);
    }

    public Optional<State> get(String stateId) {
        return stateRepository.findById(stateId);
    }

    public List<State> getAll() {
        return stateRepository.findAll();
    }

    public void deleteAll() {
        stateRepository.deleteAll();
    }
}
