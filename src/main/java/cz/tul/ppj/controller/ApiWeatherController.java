package cz.tul.ppj.controller;

import cz.tul.ppj.model.dto.WeatherFetchDTO;
import cz.tul.ppj.service.WeatherFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static cz.tul.ppj.util.Conv.iso8601ToTimestampUTCMidday;

@RestController
@RequestMapping("/api")
public class ApiWeatherController {

    private WeatherFetcher weatherFetcher;

    @Autowired
    public void setWeatherFetcher(WeatherFetcher weatherFetcher) {
        this.weatherFetcher = weatherFetcher;
    }

    @PutMapping("/weathers")
    public ResponseEntity<?> createWeather(@RequestBody WeatherFetchDTO weatherFetchDTO) {
        var errorMessage = weatherFetchDTO.getErrorMessage();
        if (errorMessage != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        var timestamp = iso8601ToTimestampUTCMidday(weatherFetchDTO.getDatestamp());
        if (timestamp < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Date must be in YYYY-MM-DD format.");
        }

        var response = weatherFetcher.fetchWeatherDataAndStoreToDatabase(weatherFetchDTO.getCitySelect(), timestamp, weatherFetchDTO.getnDays());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/weathers")
    public ResponseEntity<?> getWeathers() {
        //
        return ResponseEntity.status(HttpStatus.OK).body("WIP");
    }

}
