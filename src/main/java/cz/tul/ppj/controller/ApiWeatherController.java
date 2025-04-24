package cz.tul.ppj.controller;

import cz.tul.ppj.model.CityKey;
import cz.tul.ppj.model.dto.WeatherFetchDTO;
import cz.tul.ppj.service.WeatherFetcher;
import cz.tul.ppj.service.jpa.CityService;
import cz.tul.ppj.service.jpa.StateService;
import cz.tul.ppj.service.jpa.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static cz.tul.ppj.util.Conv.iso8601ToTimestampUTCMidday;

@RestController
@RequestMapping("/api")
public class ApiWeatherController {

    private WeatherFetcher weatherFetcher;
    private WeatherService weatherService;
    private CityService cityService;
    private StateService stateService;

    @Autowired
    public void setWeatherFetcher(WeatherFetcher weatherFetcher) {
        this.weatherFetcher = weatherFetcher;
    }

    @Autowired
    public void setWeatherService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    @Autowired
    public void setStateService(StateService stateService) {
        this.stateService = stateService;
    }

    @PutMapping("/weathers")
    public ResponseEntity<?> createWeather(@RequestBody WeatherFetchDTO weatherFetchDTO) {
        var isValid = weatherFetchDTO.isValid();
        if (!isValid.bool()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(isValid.message());
        }
        var timestamp = iso8601ToTimestampUTCMidday(weatherFetchDTO.getDatestamp());
        if (timestamp < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Date must be in 'YYYY-MM-DD' format.");
        }
        var cityNameAndStateId = weatherFetchDTO.getCityNameAndStateId();
        if (cityNameAndStateId == null || cityNameAndStateId.length != 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("City must be in 'CityName,CC' format.");
        }
        var cityName = cityNameAndStateId[0];
        var stateId = cityNameAndStateId[1].toUpperCase();
        var state = stateService.get(stateId);
        if (state.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot fetch weather reports for non-existent state.");
        }
        var city = cityService.get(new CityKey(state.get(), cityName));
        if (city.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot fetch weather reports for non-existent city.");
        }
        var result = weatherFetcher.fetchWeatherDataAndStoreToDatabase(weatherFetchDTO.getCitySelect(), timestamp, weatherFetchDTO.getnDays(), city.get());
        if (!result.bool()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.message());
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(result.message());
        }
    }

    @GetMapping("/weathers/summary")
    public ResponseEntity<?> getWeathers() {
        var weatherSummary = weatherService.countWeathersByEachCity();
        return ResponseEntity.status(HttpStatus.OK).body(weatherSummary);
    }

    @DeleteMapping("/weathers/{stateid}/{cityname}")
    public ResponseEntity<?> deleteCity(@PathVariable("stateid") String stateIdRaw, @PathVariable("cityname") String cityName) {
        var stateId = stateIdRaw.toUpperCase();
        weatherService.deleteByStateIdAndCityName(stateId, cityName);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted all weather reports for '" + stateId + ", " + cityName + "'.");
    }

    @DeleteMapping("/weathers")
    public ResponseEntity<?> deleteAll() {
        weatherService.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body("Deleted all weather reports.");
    }

}
