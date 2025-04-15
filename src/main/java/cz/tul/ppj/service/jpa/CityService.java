package cz.tul.ppj.service.jpa;

import cz.tul.ppj.model.City;
import cz.tul.ppj.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public void create(City city) {
        cityRepository.save(city);
    }

    public void createBulk(List<City> cities) {
        cityRepository.saveAll(cities);
    }

    public List<City> getAll() {
        return cityRepository.findAll();
    }

    public void deleteAll() {
        cityRepository.deleteAll();
    }

}
