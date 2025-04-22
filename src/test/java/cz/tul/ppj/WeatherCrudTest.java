package cz.tul.ppj;

import cz.tul.ppj.model.City;
import cz.tul.ppj.model.State;
import cz.tul.ppj.model.Weather;
import cz.tul.ppj.service.jpa.CityService;
import cz.tul.ppj.service.jpa.StateService;
import cz.tul.ppj.service.jpa.WeatherService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static cz.tul.ppj.Helper.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PpjApplication.class})
@ActiveProfiles({"test"})
public class WeatherCrudTest {

    @Autowired
    private StateService stateService;

    @Autowired
    private CityService cityService;

    @Autowired
    private WeatherService weatherService;

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
    private final Weather weather111 = initializeWeather(1745157600, city11, 294.96f, 294.58f, 1006, 53, "scattered clouds");
    private final Weather weather112 = initializeWeather(1745240400, city11, 291.38f, 290.85f, 1011, 61, "broken clouds");
    private final Weather weather121 = initializeWeather(1745157600, city12, 293.57f, 292.89f, 1007, 47, "clear sky");
    private final Weather weather122 = initializeWeather(1745240400, city12, 287.56f, 287.07f, 1011, 77, "overcast clouds");
    private final Weather weather211 = initializeWeather(1745157600, city21, 288.99f, 287.96f, 1016, 51, "scattered clouds");
    private final Weather weather212 = initializeWeather(1745240400, city21, 290.99f, 290.27f, 1014, 55, "scattered clouds");
    private final Weather weather221 = initializeWeather(1745157600, city22, 284.14f, 283.72f, 1015, 93, "mist");
    private final Weather weather222 = initializeWeather(1745240400, city22, 287.14f, 286.74f, 1015, 82, "clear sky");

    @Test
    public void testCreateAndGetAll() {
        stateService.create(state1);
        cityService.create(city11);
        weatherService.create(weather111);
        var weathers = weatherService.getAll();
        var nWeathers = weathers.size();
        assertEquals("One weather should have been created.", 1, nWeathers);
        assertEquals("Inserted weather should match the retrieved one.", weather111, weathers.getFirst());
    }

    @Test
    public void testCreateBulkAndGet() {
        stateService.createBulk(new ArrayList<>(Arrays.asList(state1, state2)));
        cityService.createBulk(new ArrayList<>(Arrays.asList(city11, city12, city21, city22)));
        weatherService.createBulk(new ArrayList<>(Arrays.asList(weather111, weather112, weather121, weather122, weather211, weather212, weather221, weather222, weather222 /*intentional duplicate*/)));
        var weathers = weatherService.getAll();
        var nWeathers = weathers.size();
        assertEquals("Eight weathers should have been created.", 8, nWeathers);
        var retrieved111 = weatherService.get(weather111.getWeatherKey());
        assertTrue("Inserted weather retrieval should not return null.", retrieved111.isPresent());
        assertEquals("Inserted weather should match the retrieved one.", weather111, retrieved111.get());
        var retrieved222 = weatherService.get(weather222.getWeatherKey());
        assertTrue("Inserted weather retrieval should not return null.", retrieved222.isPresent());
        assertEquals("Inserted weather should match the retrieved one.", weather222, retrieved222.get());
        var retrievedNull = weatherService.get(initializeWeatherKey(-1, city11));
        assertFalse("Made up weather retrieval should return null.", retrievedNull.isPresent());
    }

    @Test
    public void testExists() {
        stateService.create(state1);
        cityService.create(city11);
        weatherService.create(weather111);
        assertTrue("Inserted weather should exist.", weatherService.exists(weather111.getWeatherKey()));
        assertFalse("Made up weather should not exist.", weatherService.exists(initializeWeatherKey(-1, city11)));
    }

    @Test
    public void testUpdate() {
        stateService.create(state1);
        stateService.create(state2);
        cityService.create(city11);
        cityService.create(city22);
        weatherService.create(weather111);
        weather111.setCity(city22);
        weatherService.updateOrCreate(weather111);
        var retrieved = weatherService.get(weather111.getWeatherKey());
        assertTrue("Updated weather should not be null.", retrieved.isPresent());
        assertEquals("Retrieved weather should be updated.", retrieved.get(), weather111);
    }

    @Test
    public void testDelete() {
        stateService.create(state1);
        cityService.create(city11);
        weatherService.create(weather111);
        var retrieved = weatherService.get(weather111.getWeatherKey());
        assertTrue("Inserted weather retrieval should not be null.", retrieved.isPresent());
        weatherService.delete(weather111.getWeatherKey());
        retrieved = weatherService.get(weather111.getWeatherKey());
        assertFalse("Deleted weather retrieval should be null.", retrieved.isPresent());
    }

    @Test
    public void testDeleteCascade() {
        stateService.create(state1);
        cityService.create(city11);
        weatherService.create(weather111);
        var retrieved = weatherService.get(weather111.getWeatherKey());
        assertTrue("Inserted weather retrieval should not be null.", retrieved.isPresent());
        stateService.delete(state1.getStateId());
        retrieved = weatherService.get(weather111.getWeatherKey());
        assertFalse("Deleted weather retrieval should be null.", retrieved.isPresent());
    }

    @Test
    public void testGetByStateId() {
        stateService.create(state1);
        stateService.create(state2);
        cityService.create(city11);
        cityService.create(city21);
        weatherService.create(weather111);
        weatherService.create(weather211);
        weatherService.create(weather212);
        var result1 = weatherService.getByCityId(city11.getCityId());
        assertEquals("There should be one weather for this cityId.", 1, result1.size());
        var result2 = weatherService.getByCityId(city21.getCityId());
        assertEquals("There should be two weathers for this cityId.", 2, result2.size());
        var result0 = weatherService.getByCityId(-1);
        assertTrue("There should be no weathers for made up cityId.", result0.isEmpty());
    }
}
