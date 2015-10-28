package alternatevote;

/**
 * A class for representing and manipulating a candidate.
 * @author ryansmith
 */
public class Candidate {
    private final String name;
    private int count;
    private boolean eliminated;
    
    public Candidate(String name) {
        this.name = name;
        resetCount();
        resetElimination();
    }
    
    /**
     * Resets the candidate.
     * @post Candidate is not eliminated.
     */
    public void resetElimination() {
        eliminated = false;
    }

    /**
     * Gets the name of the candidate.
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the count for the candidate.
     * @return The count.
     */
    public int getCount() {
        return count;
    }
    
    /**
     * Increments the count.
     * @post Vote count for the candidate has been incremented by 1.
     */
    public void incrementCount() {
        count += 1;
    }
    
    /**
     * Resets the count.
     * @post Vote count is 0.
     */
    public void resetCount() {
        count = 0;
    }

    /**
     * Eliminates the candidate.
     * @pre Candidate is not eliminated.
     * @post Candidate is eliminated.
     */
    public void eliminate() {
        assert eliminated == false;
        eliminated = true;
    }
    
    /**
     * Determines if the candidate has been eliminated.
     * @return True if the candidate was eliminated.
     */
    public boolean isEliminated() {
        return eliminated;
    }
}
