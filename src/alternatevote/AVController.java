package alternatevote;

import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * A controller for the interactions of a votes view and counting view with a model.
 * @author ryansmith
 */
public class AVController {
    private final AVModel model;
    private AVVotesView votesView;
    private AVCountingView countingView;
    
    public AVController(AVModel model) {
        this.model = model;
    }
    
    /**
     * Starts counting votes from first preferences.
     */
    public void startCounting() {
        model.startCounting();
        checkRound();
    }
    
    /**
     * Redistributes votes.
     */
    public void redistribute() {
        model.redistribute();
        checkRound();
    }
    
    /**
     * Loads votes into the ballot from a CSV file.
     */
    public void loadVotes() {
        // Displays a file chooser filtered by CSV files.
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "CSV files", "csv"
        );
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        
        // Attempts to load votes if a file was selected in the chooser.
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                model.loadVotes(chooser.getSelectedFile().getAbsolutePath());
            } catch (Exception ex) {
                votesView.showError(ex.getMessage());
            }
        }
    }

    /**
     * Adds votes to the ballot.
     * @param candidateIds The candidate IDs of the preferences selected in the ballot view.
     */
    public void addVote(ArrayList<Integer> candidateIds) {
        // Attempts add a vote to the model.
        try {
            boolean foundBlankPreference = false; // Ensures that there are no gaps in preferences.
            ArrayList<Integer> preferenceIds = new ArrayList<>();
            
            // Removes blank preferences to ensure that the model receives a valid request.
            for (Integer candidateId : candidateIds) {
                if (candidateId == -1) {
                    foundBlankPreference = true;
                } else if (foundBlankPreference == true) {
                    throw new Exception("Cannot select preferences after a blank preference");
                } else {
                    preferenceIds.add(candidateId);
                }
            }
            
            model.addVote(preferenceIds);
        } catch (Exception ex) {
            votesView.showError(ex.getMessage());
        }
    }

    /**
     * Sets the votes view so that the controller can communicate with it.
     * @param view The votes view.
     */
    public void setVotesView(AVVotesView view) {
        votesView = view;
    }
    
    /**
     * Sets the counting view so that the controller can communicate with it.
     * @param view The counting view.
     */
    public void setCountingView(AVCountingView view) {
        countingView = view;
    }
    
    /**
     * Checks the round to update the countingView.
     */
    private void checkRound() {
        if (model.getRound() == 0) {
            countingView.enableStart();
        } else {
            countingView.disableStart();
        }
    }
}
