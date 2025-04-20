package cz.tul.ppj;

import cz.tul.ppj.service.jpa.StateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
    public void testCreateReceive() {

        var stateToAdd = initializeState("CZ", "Czechia");
        stateService.create(stateToAdd);
        var states = stateService.getAll();
        var nStates = states.size();
        assertEquals("One state should have been created.", 1, nStates);
        assertEquals("Inserted state should match the retrieved one.", stateToAdd, states.getFirst());

    }

    @Test
    public void testExists() {

        var stateToAdd = initializeState("CZ", "Czechia");
        stateService.create(stateToAdd);
        assertTrue("State should exist.", stateService.exists(stateToAdd.getStateId()));
        assertFalse("State should not exist.", stateService.exists("123456789"));

    }

}
