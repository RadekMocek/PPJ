package cz.tul.ppj;

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

import static cz.tul.ppj.Helper.initializeState;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {PpjApplication.class})
@ActiveProfiles({"test"})
public class StateCrudTest {

    @Autowired
    private StateService stateService;

    @Before
    public void init() {
        stateService.deleteAll();
    }

    @Test
    public void testCreateAndGetAll() {
        var stateToAdd = initializeState("CZ", "Czechia");
        stateService.create(stateToAdd);
        var states = stateService.getAll();
        var nStates = states.size();
        assertEquals("One state should have been created.", 1, nStates);
        assertEquals("Inserted state should match the retrieved one.", stateToAdd, states.getFirst());
    }

    @Test
    public void testCreateBulkAndGet() {
        var stateToAdd1 = initializeState("CZ", "Czechia");
        var stateToAdd2 = initializeState("GE", "Georgia");
        stateService.createBulk(new ArrayList<>(Arrays.asList(stateToAdd1, stateToAdd2, stateToAdd1 /*intentional duplicate*/)));
        var states = stateService.getAll();
        var nStates = states.size();
        assertEquals("Two state should have been created.", 2, nStates);
        var retrieved1 = stateService.get(stateToAdd1.getStateId());
        assertTrue("Inserted state retrieval should not return null.", retrieved1.isPresent());
        assertEquals("Inserted state should match the retrieved one.", stateToAdd1, retrieved1.get());
        var retrieved2 = stateService.get(stateToAdd2.getStateId());
        assertTrue("Inserted state retrieval should not return null.", retrieved2.isPresent());
        assertEquals("Inserted state should match the retrieved one.", stateToAdd2, retrieved2.get());
        var retrievedNull = stateService.get("123456789");
        assertFalse("Made up state retrieval should return null.", retrievedNull.isPresent());
    }

    @Test
    public void testExists() {
        var stateToAdd = initializeState("CZ", "Czechia");
        stateService.create(stateToAdd);
        assertTrue("Inserted state should exist.", stateService.exists(stateToAdd.getStateId()));
        assertFalse("Made up state should not exist.", stateService.exists("123456789"));
    }

    @Test
    public void testUpdate() {
        var stateToAdd = initializeState("CZ", "Czechia");
        stateService.create(stateToAdd);
        stateToAdd.setName("Bruhia");
        stateService.updateOrCreate(stateToAdd);
        var retrieved = stateService.get(stateToAdd.getStateId());
        assertTrue("Updated state should not be null.", retrieved.isPresent());
        assertEquals("Retrieved state should be updated.", retrieved.get(), stateToAdd);
    }

    @Test
    public void testDelete() {
        final var id = "CZ";
        var stateToAdd = initializeState(id, "Czechia");
        stateService.create(stateToAdd);
        var retrieved = stateService.get(id);
        assertTrue("Inserted state retrieval should not return null.", retrieved.isPresent());
        stateService.delete(id);
        retrieved = stateService.get(id);
        assertFalse("Deleted state retrieval should return null.", retrieved.isPresent());
    }

}
