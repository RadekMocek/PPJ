package cz.tul.ppj.service.jpa;

import cz.tul.ppj.model.Weather;
import cz.tul.ppj.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    public List<Weather> getAll() {
        return weatherRepository.findAll();
    }

}
