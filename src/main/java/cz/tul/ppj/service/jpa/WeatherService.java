package cz.tul.ppj.service.jpa;

import cz.tul.ppj.model.Weather;
import cz.tul.ppj.model.WeatherKey;
import cz.tul.ppj.service.repository.WeatherRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    public void create(Weather weather) {
        weatherRepository.save(weather);
    }

    public void createBulk(List<Weather> weathers) {
        weatherRepository.saveAll(weathers);
    }

    public boolean exists(WeatherKey weatherKey) {
        return weatherRepository.existsById(weatherKey);
    }

    public Optional<Weather> get(WeatherKey weatherKey) {
        return weatherRepository.findById(weatherKey);
    }

    public List<Weather> getAll() {
        return weatherRepository.findAll();
    }

    public void updateOrCreate(Weather weather) {
        weatherRepository.save(weather);
    }

    public void delete(WeatherKey weatherKey) {
        weatherRepository.deleteById(weatherKey);
    }

    public void deleteAll() {
        weatherRepository.deleteAll();
    }

    //

    public List<Weather> getByStateIdAndCityName(String stateId, String cityName) {
        return weatherRepository.findByStateIdAndCityName(stateId, cityName);
    }

    public List<WeatherRepository.CityWeatherSummary> countWeathersByEachCity() {
        return weatherRepository.countWeathersByEachCity();
    }

    @Transactional
    public void deleteByStateIdAndCityName(String stateId, String cityName) {
        weatherRepository.deleteByStateIdAndCityName(stateId, cityName);
    }

}
