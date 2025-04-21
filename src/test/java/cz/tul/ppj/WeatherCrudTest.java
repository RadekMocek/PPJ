package cz.tul.ppj;

import cz.tul.ppj.model.City;
import cz.tul.ppj.model.State;
import cz.tul.ppj.model.Weather;
import cz.tul.ppj.service.jpa.CityService;
import cz.tul.ppj.service.jpa.StateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static cz.tul.ppj.Helper.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {PpjApplication.class})
@ActiveProfiles({"test"})
public class WeatherCrudTest {

    @Autowired
    private StateService stateService;

    @Autowired
    private CityService cityService;

    @Before
    public void init() {
        stateService.deleteAll();
    }

    private final State state1 = initializeState("CZ", "Czechia");
    private final State state2 = initializeState("GE", "Georgia");
    private final City city11 = initializeCity(3067696, state1, "Prague");
    private final City city12 = initializeCity(3071961, state1, "Liberec");
    private final City city21 = initializeCity(611717, state2, "Tbilisi");
    private final City city22 = initializeCity(615532, state2, "Batumi");
    private final Weather weather111 = initializeWeather(1745157600, city11, 295, 295, 10006, 53, "scattered clouds");

}
