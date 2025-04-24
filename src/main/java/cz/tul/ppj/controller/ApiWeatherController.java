package cz.tul.ppj.controller;

import cz.tul.ppj.model.dto.WeatherFetchDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiWeatherController {

    @PutMapping("/weathers")
    public ResponseEntity<?> createWeather(@RequestBody WeatherFetchDTO weatherFetchDTO) {
        //
        return ResponseEntity.status(HttpStatus.OK).body(weatherFetchDTO);
    }

    @GetMapping("/weathers")
    public ResponseEntity<?> getWeathers() {
        //
        return ResponseEntity.status(HttpStatus.OK).body("WIP");
    }

}
