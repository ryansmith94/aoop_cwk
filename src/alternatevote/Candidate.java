package alternatevote;

/**
 *
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
     */
    public void incrementCount() {
        count += 1;
    }
    
    /**
     * Resets the count.
     */
    public void resetCount() {
        count = 0;
    }

    /**
     * Eliminates the candidate.
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
