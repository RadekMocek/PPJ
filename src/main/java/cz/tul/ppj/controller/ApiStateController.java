package cz.tul.ppj.controller;

import cz.tul.ppj.model.State;
import cz.tul.ppj.service.jpa.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiStateController {

    private StateService stateService;

    @Autowired
    public void setStateService(StateService stateService) {
        this.stateService = stateService;
    }

    @GetMapping("/states")
    public ResponseEntity<List<State>> getAllStates() {
        var states = stateService.getAll();
        return new ResponseEntity<>(states, HttpStatus.OK);
    }

    @PutMapping("/states")
    public ResponseEntity<State> createState(@RequestBody State state) {
        if (stateService.exists(state.getStateId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            stateService.create(state);
            return new ResponseEntity<>(state, HttpStatus.CREATED);
        }
    }

}
