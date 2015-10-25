package alternatevote;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A view for listing and calculating the vote counts of candidates.
 * @author ryansmith
 */
public class AVCountingView implements Observer {
    private final AVController controller;
    private final AVModel model;

    // Defines components to be used throughout the view.
    private final JButton startButton = new JButton("Start counting");
    private final JButton redistributeButton = new JButton("Redistribute");
    private final JLabel candidatesLabel = new JLabel("");
    private final JPanel panel = new JPanel();
    private static final Dimension PANEL_SIZE = new Dimension(500, 500);
    
    AVCountingView(AVModel model, AVController controller) {
        // Links MVC elements.
        this.model = model;
        this.controller = controller;
        model.addObserver(this);
        controller.setCountingView(this);
        
        // Constructs and updates the controls.
        createControls();
        update(model, null);
    }

    /**
     * Updates the view when the model changes.
     */
    @Override
    public void update(Observable o, Object arg) {
        ArrayList<Candidate> candidates = model.getCandidates();
        String counts = "<html><div style='text-align: center; width: " + PANEL_SIZE.width + ";'><h3>Counts</h3>";
        
        // Displays helpful message when there aren't any candidates.
        if (candidates.isEmpty()) {
            counts += "There are no candidates right now.";
        }
        
        // Displays all candidates except those that have been eliminated.
        for (Candidate candidate : candidates) {
            if (!candidate.isEliminated()) {
                counts += candidate.getName() + " - " + candidate.getCount() + "<br>";
            }
        }
        
        // Updates the candidates label.
        counts += "</div></html>";
        candidatesLabel.setText(counts);
    }

    /**
     * Creates all of the controls for the view (buttons, panels, etc).
     */
    private void createControls() {
        // Enables/disables buttons.
        enableStart();
        
        // Hooks up the controller on the action listeners.
        startButton.addActionListener((e) -> controller.startCounting());
        redistributeButton.addActionListener((e) -> controller.redistribute());
        
        // Defines components not needed as instance attributes.
        JPanel buttonsPanel = new JPanel();
        
        // Lays out the components.
        panel.setLayout(new BorderLayout());
        panel.add(candidatesLabel, BorderLayout.PAGE_START);
        panel.add(buttonsPanel, BorderLayout.PAGE_END);
        
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(startButton);
        buttonsPanel.add(redistributeButton);
        
        panel.setPreferredSize(PANEL_SIZE);
    }

    /**
     * Gets the panel so that it can be added to a content pane externally.
     * @return The view's panel.
     */
    public JPanel getPanel() {
        return panel;
    }
    
    /**
     * Disables starting.
     */
    public void disableStart() {
        startButton.setEnabled(false);
        redistributeButton.setEnabled(true);
    }
    
    /**
     * Enables starting.
     */
    public void enableStart() {
        startButton.setEnabled(true);
        redistributeButton.setEnabled(false);
    }
    
}
