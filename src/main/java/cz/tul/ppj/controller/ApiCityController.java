package cz.tul.ppj.controller;

import cz.tul.ppj.model.City;
import cz.tul.ppj.model.State;
import cz.tul.ppj.service.jpa.CityService;
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
public class ApiCityController {

    private CityService cityService;
    private StateService stateService;

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    @Autowired
    public void setStateService(StateService stateService) {
        this.stateService = stateService;
    }

    @PutMapping("/cities")
    public ResponseEntity<?> createCity(@RequestBody City city) {
        if (StringUtils.isBlank(city.getStateId()) || StringUtils.isBlank(city.getName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("City's stateID and name cannot be blank.");
        } else if (!stateService.exists(city.getStateId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot create city for non-existent state.");
        } else if (cityService.exists(city.getCityKey())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("City '" + city + "' already exists.");
        } else {
            cityService.create(city);
            return ResponseEntity.status(HttpStatus.CREATED).body(city);
        }
    }

    @GetMapping("/cities")
    public ResponseEntity<List<City>> getAllCities() {
        var cities = cityService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(cities);
    }

    //

    @PutMapping("/cities/testing")
    public ResponseEntity<?> createTestingData() {
        // TODO
        return ResponseEntity.status(HttpStatus.CREATED).body("Added countries for testing.");
    }

}
