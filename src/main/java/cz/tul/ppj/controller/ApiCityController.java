package cz.tul.ppj.controller;

import cz.tul.ppj.model.City;
import cz.tul.ppj.model.State;
import cz.tul.ppj.model.dto.CityDTO;
import cz.tul.ppj.service.jpa.CityService;
import cz.tul.ppj.service.jpa.StateService;
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
    public ResponseEntity<?> createCity(@RequestBody CityDTO cityDTO) {
        if (cityDTO.isAnyMemberBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("City's stateID and name cannot be blank.");
        }

        cityDTO.setStateId(cityDTO.getStateId().toUpperCase());

        var state = stateService.get(cityDTO.getStateId());
        if (state.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot create city for non-existent state.");
        }

        var city = new City(state.get(), cityDTO.getCityName());
        if (cityService.exists(city.getCityKey())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("City '" + city + "' already exists.");
        }

        cityService.create(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(city);
    }

    @GetMapping("/cities/{stateid}/{cityname}")
    public ResponseEntity<?> getCity(@PathVariable("stateid") String stateIdRaw, @PathVariable("cityname") String cityName) {
        var stateId = stateIdRaw.toUpperCase();
        var city = cityService.getByStateIdAndCityName(stateId, cityName);
        if (city.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("City '" + stateId + ", " + cityName + "' does not exist.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(city.get());
    }

    @GetMapping("/cities")
    public ResponseEntity<List<City>> getAllCities() {
        var cities = cityService.getAllSorted();
        return ResponseEntity.status(HttpStatus.OK).body(cities);
    }

    @DeleteMapping("/cities/{stateid}/{cityname}")
    public ResponseEntity<?> deleteCity(@PathVariable("stateid") String stateIdRaw, @PathVariable("cityname") String cityName) {
        var stateId = stateIdRaw.toUpperCase();
        /*
        var state = stateService.get(stateId);
        if (state.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Non-existent state shouldn't have any cities :/.");
        }
        var cityKey = new CityKey(state.get(), cityName);
        if (cityService.exists(cityKey)) {
            cityService.delete(cityKey);
            return ResponseEntity.status(HttpStatus.OK).body("City '" + stateId + ", " + cityName + "' deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("City '" + stateId + ", " + cityName + "' does not exist.");
        }
        */
        cityService.deleteByStateIdAndCityName(stateId, cityName);
        return ResponseEntity.status(HttpStatus.OK).body("City '" + stateId + ", " + cityName + "' deleted.");
    }

    @DeleteMapping("/cities")
    public ResponseEntity<?> deleteAll() {
        cityService.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body("Deleted all cities.");
    }

    //

    @PutMapping("/cities/testing")
    public ResponseEntity<?> createTestingData() {
        var state1 = new State("CZ", "Czechia");
        var state2 = new State("ES", "Spain");
        var state3 = new State("GE", "Georgia");
        if (!stateService.exists(state1.getStateId()) || !stateService.exists(state2.getStateId()) || !stateService.exists(state3.getStateId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Add testing countries first.");
        }
        var city1 = new City(state1, "Liberec");
        var city2 = new City(state2, "Madrid");
        var city3 = new City(state3, "Tbilisi");
        cityService.createBulk(new ArrayList<>(Arrays.asList(city1, city2, city3)));
        return ResponseEntity.status(HttpStatus.CREATED).body("Added cities for testing.");
    }

}
