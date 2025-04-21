package cz.tul.ppj.service.jpa;

import cz.tul.ppj.model.City;
import cz.tul.ppj.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public boolean exists(int cityId) {
        return cityRepository.existsById(cityId);
    }

    public Optional<City> get(int cityId) {
        return cityRepository.findById(cityId);
    }

    public List<City> getAll() {
        return cityRepository.findAll();
    }

    public void updateOrCreate(City city) {
        cityRepository.save(city);
    }

    public void delete(int cityId) {
        cityRepository.deleteById(cityId);
    }

    public void deleteAll() {
        cityRepository.deleteAll();
    }

    //

    public List<City> getByStateId(String stateId) {
        if (stateId == null) return new ArrayList<>();
        return cityRepository.findByStateId(stateId);
    }
}
