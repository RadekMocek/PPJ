package cz.tul.ppj;

import cz.tul.ppj.model.State;
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
public class StateCrudTest {

    @Autowired
    private StateService stateService;

    @Before
    public void init() {
        stateService.deleteAll();
    }

    private final State state1 = new State("CZ", "Czechia");
    private final State state2 = new State("GE", "Georgia");

    @Test
    public void testCreateAndGetAll() {
        stateService.create(state1);
        var states = stateService.getAll();
        var nStates = states.size();
        assertEquals("One state should have been created.", 1, nStates);
        assertEquals("Inserted state should match the retrieved one.", state1, states.getFirst());
    }

    @Test
    public void testCreateBulkAndGet() {
        stateService.createBulk(new ArrayList<>(Arrays.asList(state1, state2, state1 /*intentional duplicate*/)));
        var states = stateService.getAll();
        var nStates = states.size();
        assertEquals("Two state should have been created.", 2, nStates);
        var retrieved1 = stateService.get(state1.getStateId());
        assertTrue("Inserted state retrieval should not return null.", retrieved1.isPresent());
        assertEquals("Inserted state should match the retrieved one.", state1, retrieved1.get());
        var retrieved2 = stateService.get(state2.getStateId());
        assertTrue("Inserted state retrieval should not return null.", retrieved2.isPresent());
        assertEquals("Inserted state should match the retrieved one.", state2, retrieved2.get());
        var retrievedNull = stateService.get("123456789");
        assertFalse("Made up state retrieval should return null.", retrievedNull.isPresent());
    }

    @Test
    public void testExists() {
        stateService.create(state1);
        assertTrue("Inserted state should exist.", stateService.exists(state1.getStateId()));
        assertFalse("Made up state should not exist.", stateService.exists("123456789"));
    }

    @Test
    public void testUpdate() {
        stateService.create(state1);
        state1.setName("Bruhia");
        stateService.updateOrCreate(state1);
        var retrieved = stateService.get(state1.getStateId());
        assertTrue("Updated state should not be null.", retrieved.isPresent());
        assertEquals("Retrieved state should be updated.", retrieved.get(), state1);
    }

    @Test
    public void testDelete() {
        stateService.create(state1);
        var retrieved = stateService.get(state1.getStateId());
        assertTrue("Inserted state retrieval should not return null.", retrieved.isPresent());
        stateService.delete(state1.getStateId());
        retrieved = stateService.get(state1.getStateId());
        assertFalse("Deleted state retrieval should return null.", retrieved.isPresent());
    }

}
