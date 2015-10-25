package alternatevote;

import java.util.ArrayList;

/**
 *
 * @author ryansmith
 */
public class Vote {
    private final ArrayList<Candidate> preferences;
    private Integer choice = 0;
    
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
     * Starts counting using first preferences.
     */
    public void startCount() {
        assert preferences != null;
        assert preferences.size() > 0;
        choice = 0;
        preferences.get(choice).incrementCount();
    }
    
    /**
     * Redistributes vote if a new preference is found.
     */
    public void redistribute() {
        assert preferences != null;
        assert preferences.size() > 0;
        assert choice >= 0;
        Candidate newChoice = null;
        
        // Finds a new choice if required.
        while (hasNewChoice()) {
            newChoice = preferences.get(++choice);
        }
        
        // Redistributes the count if a new choice is found.
        if (newChoice != null) {
            newChoice.incrementCount();
        }
    }

    /**
     * Determines if there is a new choice.
     * @return True if there is a new choice.
     */
    private boolean hasNewChoice() {
        assert preferences != null;
        assert choice >= 0;
        assert choice < preferences.size();
        boolean isEliminated = preferences.get(choice).isEliminated();
        boolean hasNextPreference = choice + 1 < preferences.size();
        return isEliminated && hasNextPreference;
    }

    /**
     * Gets the current choice.
     * @return The current choice.
     */
    public int getChoice() {
        return choice;
    }
}
