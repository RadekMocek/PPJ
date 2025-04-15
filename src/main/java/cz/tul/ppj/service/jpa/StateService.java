package cz.tul.ppj.service.jpa;

import cz.tul.ppj.model.State;
import cz.tul.ppj.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateService {

    @Autowired
    private StateRepository stateRepository;

    public List<State> getAll() {
        return stateRepository.findAll();
    }

    public void deleteAll() {
        stateRepository.deleteAll();
    }
}
