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

    public List<City> getAll() {
        return cityRepository.findAll();
    }

}
