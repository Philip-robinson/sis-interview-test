/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.rpl.sisTest;

import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author philip
 */
public class TeamStoreTest {
    
    TeamStore inst;
    public TeamStoreTest() {
    }
    
    @Before
    public void setUp() {
        inst=new TeamStore();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of get method, of class TeamStore.
     */
    @Test
    public void testGetNothing() {
        System.out.println("getNothing");
        String name = "XXX";
        assertNull(inst.get(name));
    }

    @Test
    public void testGetAllNothing() {
        System.out.println("getAllNothing");
        assertNotNull(inst.getAll());
        assertEquals(0, inst.getAll().size());
    }


    public void testCreateGet(){
        System.out.println("testCreateGet");
        String CITY="A city";
        String OWNER="An OWner";
        String NAME="A FOOTBALL team";
        String COMP="Some sort of competition";

        Team t = inst.create(NAME, CITY, OWNER, COMP);
        assertNotNull(t);
        assertEquals(NAME, t.getName());
        assertEquals(OWNER, t.getOwner());
        assertEquals(COMP, t.getCompetition());
        assertEquals(CITY, t.getCity());
        String id = t.getId();

        Team tt = inst.get(id);
        assertNotNull(tt);
        assertEquals(NAME, tt.getName());
        assertEquals(OWNER, tt.getOwner());
        assertEquals(COMP, tt.getCompetition());
        assertEquals(CITY, tt.getCity());
    }

    public void testCreateGetOther(){
        System.out.println("testCreateGetOther");
        String CITY="A city";
        String OWNER="An OWner";
        String NAME="A FOOTBALL team";
        String COMP="Some sort of competition";

        Team t = inst.create(NAME, CITY, OWNER, COMP);
        Team tt = inst.get("X"+t.getId());
        assertNull(tt);
    }
    /**
     * Test of exists method, of class TeamStore.
     */
    @Test
    public void testExists() {
        System.out.println("exists");
        String CITY="A city";
        String OWNER="An OWner";
        String NAME="A FOOTBALL team";
        String COMP="Some sort of competition";

        Team t = inst.create(NAME, CITY, OWNER, COMP);
        assertFalse(inst.exists("F"+t.getId()));
        assertTrue(inst.exists(t.getId()));
    }

    /**
     * Test of delete method, of class TeamStore.
     */
    @Test
    public void testDelete() {
        System.out.println("delete");
        String CITY="A city";
        String OWNER="An OWner";
        String NAME="A FOOTBALL team";
        String COMP="Some sort of competition";

        Team t = inst.create(NAME, CITY, OWNER, COMP);
        assertTrue(inst.exists(t.getId()));
        inst.delete(t.getId());
        assertFalse(inst.exists(t.getId()));

    }
    
}
