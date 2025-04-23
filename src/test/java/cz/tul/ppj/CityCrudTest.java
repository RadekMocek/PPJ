package cz.tul.ppj;

import cz.tul.ppj.model.City;
import cz.tul.ppj.model.CityKey;
import cz.tul.ppj.model.State;
import cz.tul.ppj.service.jpa.CityService;
import cz.tul.ppj.service.jpa.StateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PpjApplication.class})
@ActiveProfiles({"test"})
public class CityCrudTest {

    @Autowired
    private StateService stateService;

    @Autowired
    private CityService cityService;

    @Before
    public void init() {
        stateService.deleteAll();
    }

    private final State state1 = new State("CZ", "Czechia");
    private final State state2 = new State("GE", "Georgia");
    private final City city11 = new City(state1, "Prague");
    private final City city12 = new City(state1, "Liberec");
    private final City city21 = new City(state2, "Tbilisi");
    private final City city22 = new City(state2, "Batumi");
    private final City fakeCity = new City(new State("BL", "Bajookieland"), "Brongulus");

    @Test
    public void testCreateAndGetAll() {
        stateService.create(state1);
        cityService.create(city11);
        var cities = cityService.getAll();
        var nCities = cities.size();
        assertEquals("One city should have been created.", 1, nCities);
        assertEquals("Inserted city should match the retrieved one.", city11, cities.getFirst());
    }

    @Test
    public void testCreateBulkAndGet() {
        stateService.createBulk(new ArrayList<>(Arrays.asList(state1, state2)));
        cityService.createBulk(new ArrayList<>(Arrays.asList(city11, city12, city21, city22, city11 /*intentional duplicate*/)));
        var cities = cityService.getAll();
        var nCities = cities.size();
        assertEquals("Four cities should have been created.", 4, nCities);
        var retrieved11 = cityService.get(city11.getCityKey());
        assertTrue("Inserted city retrieval should not return null.", retrieved11.isPresent());
        assertEquals("Inserted city should match the retrieved one.", city11, retrieved11.get());
        var retrieved22 = cityService.get(city22.getCityKey());
        assertTrue("Inserted city retrieval should not return null.", retrieved22.isPresent());
        assertEquals("Inserted city should match the retrieved one.", city22, retrieved22.get());
        var retrievedNull = cityService.get(fakeCity.getCityKey());
        assertFalse("Made up city retrieval should return null.", retrievedNull.isPresent());
    }

    @Test
    public void testExists() {
        stateService.create(state1);
        cityService.create(city11);
        assertTrue("Inserted city should exist.", cityService.exists(city11.getCityKey()));
        assertFalse("Made up city should not exist.", cityService.exists(fakeCity.getCityKey()));
    }

    @Test
    public void testUpdate() {
        stateService.create(state1);
        cityService.create(city11);
        city11.setName("Braha");
        cityService.updateOrCreate(city11);
        var retrieved = cityService.get(city11.getCityKey());
        assertTrue("Updated city should not be null.", retrieved.isPresent());
        assertEquals("Retrieved city should be updated.", retrieved.get(), city11);
    }

    @Test
    public void testDelete() {
        stateService.create(state1);
        cityService.create(city11);
        var retrieved = cityService.get(city11.getCityKey());
        assertTrue("Inserted city retrieval should not be null.", retrieved.isPresent());
        cityService.delete(city11.getCityKey());
        retrieved = cityService.get(city11.getCityKey());
        assertFalse("Deleted city retrieval should be null.", retrieved.isPresent());
    }

    @Test
    public void testDeleteCascade() {
        stateService.create(state1);
        cityService.create(city11);
        var retrieved = cityService.get(city11.getCityKey());
        assertTrue("Inserted city retrieval should not be null.", retrieved.isPresent());
        stateService.delete(state1.getStateId());
        retrieved = cityService.get(city11.getCityKey());
        assertFalse("Deleted city retrieval should be null.", retrieved.isPresent());
    }

    @Test
    public void testGetByStateId() {
        stateService.create(state1);
        stateService.create(state2);
        cityService.create(city11);
        cityService.create(city12);
        cityService.create(city21);
        cityService.create(city22);
        var result1 = cityService.getByStateId(state1.getStateId());
        assertEquals("There should be two cities for this stateId.", 2, result1.size());
        var result2 = cityService.getByStateId(state2.getStateId());
        assertEquals("There should be two cities for this stateId.", 2, result2.size());
        var result0 = cityService.getByStateId("123456789");
        assertTrue("There should be no cities for made up stateId.", result0.isEmpty());
    }

    @Test(expected = Exception.class)
    public void testReferentialIntegrity() {
        cityService.create(city11);
    }

    /*
    @Test(expected = Exception.class)
    public void testNotBlank() {
        stateService.create(state1);
        cityService.create(new City(state1, " "));
    }
    /**/

}
