package alternatevote;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author ryansmith
 */
public class AVModel extends Observable {
    private final ArrayList<Vote> votes;
    private final ArrayList<Candidate> candidates;
    private int round = 0;
    
    public AVModel() {
        this.candidates = new ArrayList<>();
        this.votes = new ArrayList<>();
        addCandidate("Cameron");
        addCandidate("Corbyn");
        addCandidate("Farron");
        addCandidate("Sturgeon");
    }

    /**
     * Starts counting votes from first preferences.
     */
    public void startCounting() {
        assert candidates != null;
        assert votes != null;
        assert round == 0;
        candidates.forEach((candidate) -> candidate.reset());
        votes.forEach((vote) -> vote.startCount());
        updateRound();
        emitChange();
    }

    /**
     * Redistributes votes.
     */
    public void redistribute() {
        assert candidates != null;
        assert votes != null;
        assert round > 0;
        updateCandidates();
        votes.forEach((vote) -> vote.redistribute());
        updateRound();
        emitChange();
    }

    /**
     * Loads votes from a CSV file.
     * @param absolutePath The absolute path the file to load votes from.
     * @throws FileNotFoundException
     * @throws Exception 
     */
    public void loadVotes(String absolutePath) throws FileNotFoundException, Exception {
        try (Scanner scanner = new Scanner(new File(absolutePath))) {
            scanner.useDelimiter("\n"); // Each vote is separated by a new line.
            while (scanner.hasNext()) {
                String vote = scanner.next();
                String[] ids = vote.split(","); // Each preference is separated by a comma.
                
                // Converts the candidate IDs from strings to integers.
                ArrayList<Integer> candidateIds = new ArrayList<>();
                for (String id : ids) {
                    candidateIds.add(Integer.parseInt(id));
                }
                addVote(candidateIds);
            }
        }
    }

    /**
     * Adds a vote.
     * @param preferenceIds The candidate IDs of the preferences.
     * @throws Exception 
     */
    public void addVote(ArrayList<Integer> preferenceIds) throws Exception {
        assert votes != null;
        assert candidates != null;
        ArrayList<Candidate> preferences = new ArrayList<>();
        
        // Validates the number of preferences.
        if (preferenceIds.size() > candidates.size()) {
            throw new Exception("Too many preferences selected.");
        } else if (preferenceIds.size() < 1) {
            throw new Exception("No preferences selected.");
        }
        
        // Gets the candidate matching each of the preference IDs.
        for (Integer id : preferenceIds) {
            // Validates preference before adding it.
            if (id < 0 || id >= candidates.size()) {
                throw new Exception("Candidate could not be found.");
            } else if (preferenceIds.indexOf(id) != preferences.size()) {
                throw new Exception("Candidate cannot be selected twice.");
            }
            preferences.add(candidates.get(id));
        }
        
        // Adds the vote to the existing votes.
        votes.add(new Vote(preferences));
        emitChange();
    }

    /**
     * Gets the votes.
     * @return the votes.
     */
    public ArrayList<Vote> getVotes() {
        return votes;
    }

    /**
     * Gets the candidates.
     * @return the candidates.
     */
    public ArrayList<Candidate> getCandidates() {
        return candidates;
    }

    /**
     * Gets the current round.
     * @return the round.
     */
    public int getRound() {
        return round;
    }
    
    /**
     * Updates the round based on the vote counts of the candidates and the current round.
     */
    private void updateRound() {
        assert candidates != null;
        assert round >= 0;
        assert round < candidates.size();
        int highestCount = 0;
        int totalCount = 0;
        
        // Determines the total startCount and the candidate with the highest startCount.
        for (Candidate candidate : candidates) {
            if (!candidate.isEliminated()) {
                int candidateCount = candidate.getCount();
                totalCount += candidateCount;
                if (candidateCount > highestCount) {
                    highestCount = candidateCount;
                }
            }
        }
        
        // Determines if the round should be reset or incremented.
        if (totalCount < 1 || highestCount * 2 > totalCount || round == candidates.size() - 1) {
            round = 0;
        } else {
            round += 1;
        }
    }
    
    /**
     * Updates the candidates preparing them for the next round of counting.
     */
    private void updateCandidates() {
        assert candidates != null;
        assert candidates.size() > 0;
        int lowestCount = candidates.get(0).getCount();
        ArrayList<Candidate> lowScorers = new ArrayList<>();
        
        // Finds the lowest scorers.
        for (Candidate candidate : candidates) {
            if (!candidate.isEliminated()) {
                int candidateCount = candidate.getCount();
                if (candidateCount < lowestCount) {
                    lowScorers.clear();
                    lowScorers.add(candidate);
                    lowestCount = candidateCount;
                } else if (candidateCount == lowestCount) {
                    lowScorers.add(candidate);
                }
            }
        }
        
        // Randomly determines which of the low scorers to eliminate.
        if (lowScorers.size() > 0) {
            lowScorers.get((new Random()).nextInt(lowScorers.size())).eliminate();
        }
    }
    
    /**
     * Emits a change to the subscribed observers.
     */
    private void emitChange() {
        setChanged();
        notifyObservers();
    }
    
    /**
     * Adds a candidate to the ballot.
     * @param name The name of the candidate.
     */
    private void addCandidate(String name) {
        candidates.add(new Candidate(name));
    }
}
