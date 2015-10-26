package alternatevote;

import java.util.ArrayList;

/**
 *
 * @author ryansmith
 */
public class Vote {
    private final ArrayList<Candidate> preferences;
    
    public Vote(ArrayList<Candidate> candidates) {
        this.preferences = candidates;
    }

    /**
     * Gets the preferences.
     * @return The preferences.
     */
    public ArrayList<Candidate> getPreferences() {
        return preferences;
    }
    
    /**
     * Counts the vote.
     */
    public void count() {
        Candidate candidate = getChoice();
        if (candidate != null) {
            candidate.incrementCount();
        }
    }

    /**
     * Gets the current choice.
     * @return The current choice.
     */
    public Candidate getChoice() {
        assert preferences != null;
        Candidate candidate = null;
        
        for (int choice = preferences.size() - 1; choice >= 0; choice--) {
            if (!preferences.get(choice).isEliminated()) {
                candidate = preferences.get(choice);
            }
        }
        
        return candidate;
    }
}
