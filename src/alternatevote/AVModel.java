package alternatevote;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.Scanner;

/**
 * A model for alternate voting.
 * @author ryansmith
 */
public class AVModel extends Observable {
    private final ArrayList<Vote> votes = new ArrayList<>();
    private final ArrayList<Candidate> candidates = new ArrayList<>();
    
    public AVModel() {
        addCandidate("Cameron");
        addCandidate("Corbyn");
        addCandidate("Farron");
        addCandidate("Sturgeon");
    }
    
    /**
     * Starts counting using first preferences.
     * @pre Counting has not started.
     * @pre candidates is not null.
     * @post Votes have been counted for first preferences.
     */
    public void startCounting() {
        assert hasStarted() == false;
        assert candidates != null;
        candidates.forEach((candidate) -> candidate.resetElimination());
        countVotes();
    }
    
    /**
     * Redistributes votes from eliminated candidates.
     * @pre Counting has started.
     * @post Votes have been counted for highest available preferences.
     */
    public void redistribute() {
        assert hasStarted() == true;
        updateCandidates();
        countVotes();
    }

    /**
     * Loads votes from a CSV file.
     * @param absolutePath The absolute path the file to load votes from.
     * @throws FileNotFoundException
     * @throws Exception 
     * @post Votes in the CSV file have been added to the existing votes.
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
     * @pre votes is not null.
     * @pre candidates is not null.
     * @pre preferenceIds is not null.
     * @post The new vote is added to the votes.
     */
    public void addVote(ArrayList<Integer> preferenceIds) throws Exception {
        assert votes != null;
        assert candidates != null;
        assert preferenceIds != null;
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
     * Determines if counting has started.
     * @return True if counting has started.
     * @pre candidates is not null.
     */
    public boolean hasStarted() {
        assert candidates != null;
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
        return !(totalCount < 1 || highestCount * 2 > totalCount);
    }
    
    /**
     * Counts votes for candidates.
     * @pre candidates is not null.
     * @pre votes is not null.
     */
    private void countVotes() {
        assert candidates != null;
        assert votes != null;
        candidates.forEach((candidate) -> candidate.resetCount());
        votes.forEach((vote) -> vote.count());
        emitChange();
    }
    
    /**
     * Updates the candidates preparing them for the next round of counting.
     * @pre candidates is not null.
     * @pre votes is not null.
     * @post One of the lowest scorers is eliminated (if there were any low scorers).
     */
    private void updateCandidates() {
        assert candidates != null;
        assert votes != null;
        int lowestCount = votes.size();
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
     * @post Observers are updated.
     */
    private void emitChange() {
        setChanged();
        notifyObservers();
    }
    
    /**
     * Adds a candidate to the ballot.
     * @param name The name of the candidate.
     * @pre candidates is not null.
     * @post The new candidate is added to candidates.
     */
    private void addCandidate(String name) {
        assert candidates != null;
        candidates.add(new Candidate(name));
        assert candidates.get(candidates.size() - 1).getName().equals(name);
    }
}
