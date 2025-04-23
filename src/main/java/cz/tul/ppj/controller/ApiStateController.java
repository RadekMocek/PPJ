package cz.tul.ppj.controller;

import cz.tul.ppj.model.State;
import cz.tul.ppj.service.jpa.StateService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiStateController {

    private StateService stateService;

    @Autowired
    public void setStateService(StateService stateService) {
        this.stateService = stateService;
    }

    @PutMapping("/states")
    public ResponseEntity<?> createState(@RequestBody State state) {
        if (StringUtils.isBlank(state.getStateId()) || StringUtils.isBlank(state.getName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("State's ID and name cannot be blank.");
        } else if (stateService.exists(state.getStateId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("State '" + state.getStateId() + "' already exists.");
        } else {
            state.setStateId(state.getStateId().toUpperCase());
            stateService.create(state);
            return ResponseEntity.status(HttpStatus.CREATED).body(state);
        }
    }

    @GetMapping("/states")
    public ResponseEntity<List<State>> getAllStates() {
        var states = stateService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(states);
    }

    @PatchMapping("/states")
    public ResponseEntity<?> updateState(@RequestBody State state) {
        if (stateService.exists(state.getStateId())) {
            stateService.updateOrCreate(state);
            return ResponseEntity.status(HttpStatus.OK).body(state);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("State '" + state.getStateId() + "' does not exist.");
        }
    }

    @DeleteMapping("/states/{stateid}")
    public ResponseEntity<?> deleteState(@PathVariable("stateid") String stateId) {
        if (stateService.exists(stateId)) {
            stateService.delete(stateId);
            return ResponseEntity.status(HttpStatus.OK).body("State '" + stateId + "' deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("State '" + stateId + "' does not exist.");
        }
    }

    @DeleteMapping("/states")
    public ResponseEntity<?> deleteAll() {
        stateService.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body("Deleted everything.");
    }

    //

    @PutMapping("/states/testing")
    public ResponseEntity<?> createTestingData() {
        State state1 = new State("CZ", "Czechia");
        State state2 = new State("GE", "Georgia");
        State state3 = new State("ES", "Spain");
        stateService.createBulk(new ArrayList<>(Arrays.asList(state1, state2, state3)));
        return ResponseEntity.status(HttpStatus.CREATED).body("Added countries for testing.");
    }

}
