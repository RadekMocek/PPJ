package cz.tul.ppj.service.jpa;

import cz.tul.ppj.model.City;
import cz.tul.ppj.model.CityKey;
import cz.tul.ppj.service.repository.CityRepository;
import jakarta.transaction.Transactional;
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

    public boolean exists(CityKey cityKey) {
        return cityRepository.existsById(cityKey);
    }

    public Optional<City> get(CityKey cityKey) {
        return cityRepository.findById(cityKey);
    }

    public List<City> getAll() {
        return cityRepository.findAll();
    }

    public List<City> getAllSorted() {
        return cityRepository.findAllSorted();
    }

    public void updateOrCreate(City city) {
        cityRepository.save(city);
    }

    public void delete(CityKey cityKey) {
        cityRepository.deleteById(cityKey);
    }

    public void deleteAll() {
        cityRepository.deleteAll();
    }

    //

    public List<City> getByStateId(String stateId) {
        if (stateId == null) return new ArrayList<>();
        return cityRepository.findByStateId(stateId);
    }

    @Transactional
    public void deleteByStateIdAndCityName(String stateId, String cityName) {
        cityRepository.deleteByCityKey_State_StateIdAndCityKey_Name(stateId, cityName);
    }
}
