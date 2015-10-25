/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alternatevote;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ryansmith
 */
public class AVModelTest {
    
    public AVModelTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of startCounting method, of class AVModel.
     */
    @Test
    public void testStartCounting() {
        System.out.println("startCounting");
        AVModel instance = new AVModel();
        instance.startCounting();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of redistribute method, of class AVModel.
     */
    @Test
    public void testRedistribute() {
        System.out.println("redistribute");
        AVModel instance = new AVModel();
        instance.redistribute();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadVotes method, of class AVModel.
     */
    @Test
    public void testLoadVotes() throws Exception {
        System.out.println("loadVotes");
        String absolutePath = "";
        AVModel instance = new AVModel();
        instance.loadVotes(absolutePath);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addVote method, of class AVModel.
     */
    @Test
    public void testAddVote() throws Exception {
        System.out.println("addVote");
        ArrayList<Integer> preferenceIds = null;
        AVModel instance = new AVModel();
        instance.addVote(preferenceIds);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getVotes method, of class AVModel.
     */
    @Test
    public void testGetVotes() {
        System.out.println("getVotes");
        AVModel instance = new AVModel();
        ArrayList<Vote> expResult = null;
        ArrayList<Vote> result = instance.getVotes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCandidates method, of class AVModel.
     */
    @Test
    public void testGetCandidates() {
        System.out.println("getCandidates");
        AVModel instance = new AVModel();
        ArrayList<Candidate> expResult = null;
        ArrayList<Candidate> result = instance.getCandidates();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRound method, of class AVModel.
     */
    @Test
    public void testGetRound() {
        System.out.println("getRound");
        AVModel instance = new AVModel();
        int expResult = 0;
        int result = instance.getRound();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
