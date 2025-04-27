package cz.tul.ppj.service.jpa;

import cz.tul.ppj.model.Weather;
import cz.tul.ppj.model.WeatherKey;
import cz.tul.ppj.model.dto.WeatherAverageDTO;
import cz.tul.ppj.service.repository.WeatherRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static cz.tul.ppj.util.Conv.separateCityNameCommaStateId;

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

    public Optional<Weather> getByStateIdAndCityNameAndTimestamp(String stateId, String cityName, long timestamp) {
        return weatherRepository.findByStateIdAndCityNameAndTimestamp(stateId, cityName, timestamp);
    }

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

    @Transactional
    public void deleteByStateIdAndCityNameAndTimestamp(String stateId, String cityName, long timestamp) {
        weatherRepository.deleteByStateIdAndCityNameAndTimestamp(stateId, cityName, timestamp);
    }

    public WeatherAverageDTO getWeatherAverages(String citySelect) {
        var parts = separateCityNameCommaStateId(citySelect);
        if (parts == null) {
            return null;
        }
        var cityName = parts[0];
        var stateId = parts[1];
        var top14 = weatherRepository.findTop14(stateId, cityName);
        if (top14.isEmpty()) {
            return null;
        }
        var lastWeather = top14.getFirst();
        var nEvaluatedDays = top14.size();
        if (nEvaluatedDays == 1) {
            return new WeatherAverageDTO(stateId, cityName, nEvaluatedDays, lastWeather.getTimestamp(), -1, -1,
                    lastWeather.getTemperature(), lastWeather.getFeelsLike(), lastWeather.getPressure(), lastWeather.getHumidity(),
                    -1, -1, -1, -1,
                    -1, -1, -1, -1);
        } else if (nEvaluatedDays <= 7) {
            var avgTemperature = top14.stream().mapToDouble(Weather::getTemperature).average().orElse(-1);
            var avgFeelsLike = top14.stream().mapToDouble(Weather::getFeelsLike).average().orElse(-1);
            var avgPressure = top14.stream().mapToInt(Weather::getPressure).average().orElse(-1);
            var avgHumidity = top14.stream().mapToInt(Weather::getHumidity).average().orElse(-1);
            return new WeatherAverageDTO(stateId, cityName, nEvaluatedDays, lastWeather.getTimestamp(), top14.getLast().getTimestamp(), -1,
                    lastWeather.getTemperature(), lastWeather.getFeelsLike(), lastWeather.getPressure(), lastWeather.getHumidity(),
                    avgTemperature, avgFeelsLike, avgPressure, avgHumidity,
                    -1, -1, -1, -1);
        } else {
            var avg1Temperature = top14.stream().limit(7).mapToDouble(Weather::getTemperature).average().orElse(-1);
            var avg1FeelsLike = top14.stream().limit(7).mapToDouble(Weather::getFeelsLike).average().orElse(-1);
            var avg1Pressure = top14.stream().limit(7).mapToInt(Weather::getPressure).average().orElse(-1);
            var avg1Humidity = top14.stream().limit(7).mapToInt(Weather::getHumidity).average().orElse(-1);

            var avg2Temperature = top14.stream().mapToDouble(Weather::getTemperature).average().orElse(-1);
            var avg2FeelsLike = top14.stream().mapToDouble(Weather::getFeelsLike).average().orElse(-1);
            var avg2Pressure = top14.stream().mapToInt(Weather::getPressure).average().orElse(-1);
            var avg2Humidity = top14.stream().mapToInt(Weather::getHumidity).average().orElse(-1);

            return new WeatherAverageDTO(stateId, cityName, nEvaluatedDays, lastWeather.getTimestamp(), top14.get(6).getTimestamp(), top14.getLast().getTimestamp(),
                    lastWeather.getTemperature(), lastWeather.getFeelsLike(), lastWeather.getPressure(), lastWeather.getHumidity(),
                    avg1Temperature, avg1FeelsLike, avg1Pressure, avg1Humidity,
                    avg2Temperature, avg2FeelsLike, avg2Pressure, avg2Humidity);
        }
    }
}
