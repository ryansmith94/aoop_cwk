package alternatevote;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A set of tests for the AVModel.
 * @author ryansmith
 */
public class AVModelTest {
    private static final String USER_DIR = System.getProperty("user.dir");
    private AVModel instance;
    private ArrayList<Candidate> candidates;
    
    @Before
    public void setUp() {
        instance = new AVModel();
        candidates = instance.getCandidates();
    }
    
    @Test
    public void testStartCounting() throws Exception {
        instance.loadVotes(USER_DIR+"/fixtures/test1.csv");
        assertFalse(instance.hasStarted());
        
        instance.startCounting();
        assertCount(0, 4);
        assertCount(1, 1);
        assertCount(2, 1);
        assertCount(3, 1);
        assertFalse(instance.hasStarted());
    }
    
    @Test
    public void testRedistributeTie() throws Exception {
        instance.loadVotes(USER_DIR+"/fixtures/test2.csv");
        assertFalse(instance.hasStarted());
        
        // Completes round 1.
        instance.startCounting();
        assertCount(0, 2);
        assertCount(1, 2);
        assertCount(2, 2);
        assertCount(3, 2);
        assertTrue(instance.hasStarted());
        
        eliminateAdditionalCandidates();
        
        // Completes round 2.
        instance.redistribute();
        assertTrue(getCount(0) + getCount(1) + getCount(2) + getCount(3) == 8);
        assertEliminations(1);
        assertTrue(instance.hasStarted());
        
        // Completes round 3.
        instance.redistribute();
        assertTrue(getCount(0) + getCount(1) + getCount(2) + getCount(3) == 8);
        assertEliminations(2);
        assertTrue(instance.hasStarted());
        
        // Completes round 4.
        instance.redistribute();
        assertTrue(getCount(0) + getCount(1) + getCount(2) + getCount(3) == 8);
        assertEliminations(3);
        assertFalse(instance.hasStarted());
    }
    
    @Test
    public void testRedistribute() throws Exception {
        instance.loadVotes(USER_DIR+"/fixtures/test3.csv");
        assertFalse(instance.hasStarted());
        
        // Completes round 1.
        instance.startCounting();
        assertCount(0, 4);
        assertCount(1, 3);
        assertCount(2, 2);
        assertCount(3, 1);
        assertTrue(instance.hasStarted());
        
        eliminateAdditionalCandidates();
        
        // Completes round 2.
        instance.redistribute();
        assertCount(0, 4);
        assertCount(1, 3);
        assertCount(2, 2);
        assertEliminated(3);
        assertEliminations(1);
        assertTrue(instance.hasStarted());
        
        // Completes round 3.
        instance.redistribute();
        assertCount(0, 4);
        assertCount(1, 3);
        assertEliminated(2);
        assertEliminated(3);
        assertEliminations(2);
        assertFalse(instance.hasStarted());
    }
    
    /**
     * Eliminates additional candidates. The tests were only designed for 4 candidates.
     */
    private void eliminateAdditionalCandidates() {
        int additionalCandidates = candidates.size() - 4;
        
        for (int index = 0; index < additionalCandidates; index++) {
            instance.redistribute();
        }
    }
    
    /**
     * Gets the vote count for a given candidate ID.
     * @param candidateId The ID of the candidate to find.
     * @return The number of votes counted for the candidate.
     */
    private int getCount(int candidateId) {
        return candidates.get(candidateId).getCount();
    }
    
    /**
     * Asserts that the vote count a given candidate matches an expected count.
     * @param candidateId The ID of the candidate to find.
     * @param expectedCount Expected vote count.
     */
    private void assertCount(int candidateId, int expectedCount) {
        int actualCount = getCount(candidateId);
        assertTrue(actualCount == expectedCount);
    }
    
    /**
     * Asserts that the given candidate has been eliminated.
     * @param candidateId The ID of the candidate to find.
     */
    private void assertEliminated(int candidateId) {
        assertTrue(candidates.get(candidateId).isEliminated());
    }
    
    /**
     * Asserts that the number of eliminated candidates matches the expected eliminations.
     * @param expectedEliminations The number of candidates expected to be eliminated.
     */
    private void assertEliminations(int expectedEliminations) {
        int actualEliminations = 0;
        
        for (Candidate candidate : candidates) {
            actualEliminations += candidate.isEliminated() ? 1 : 0;
        }
        
        // Allows more candidates to be added without invaliding tests.
        expectedEliminations += candidates.size() - 4;
        
        assertTrue(actualEliminations == expectedEliminations);
    }
}
