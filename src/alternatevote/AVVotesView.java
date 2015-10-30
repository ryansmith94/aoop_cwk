package alternatevote;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * A view for listing votes and adding more.
 * @author ryansmith
 */
public class AVVotesView implements Observer {
    private final AVController controller;
    private final AVModel model;
    
    // Defines components to be used throughout the view.
    private final JButton addButton = new JButton("Add vote");
    private final JButton loadButton = new JButton("Load votes");
    private final JPanel panel = new JPanel();
    private final JTable votesTable = new JTable();
    private final ArrayList<JComboBox<String>> comboBoxes = new ArrayList<>();
    private static final Dimension PANEL_SIZE = new Dimension(500, 500);
    
    AVVotesView(AVModel model, AVController controller) {
        // Links MVC elements.
        this.model = model;
        this.controller = controller;
        model.addObserver(this);
        controller.setVotesView(this);
        
        // Constructs and updates the controls.
        createControls();
        update(model, null);
    }

    /**
     * Updates the view when the model changes.
     */
    @Override
    public void update(Observable o, Object arg) {
        ArrayList<Vote> votes = model.getVotes();
        ArrayList<Candidate> candidates = model.getCandidates();
        Object[][] rowData = new Object[votes.size()][candidates.size()];
        Object[] columnNames = new Object[candidates.size()];
        
        // Adds columns to votes table.
        for (int index = 0; index < candidates.size(); index++) {
            columnNames[index] = "Preference " + (index + 1);
        }
        
        // Displays the preferences for all votes.
        for (int index = 0; index < votes.size(); index++) {
            ArrayList<Candidate> preferences = votes.get(index).getPreferences();
            for (int preference = 0; preference < preferences.size(); preference++) {
                String prefText = "<html><span ";
                
                // Displays the current preference/choice in a different colour.
                if (preferences.get(preference) == votes.get(index).getChoice()) {
                    prefText += "style='color:red'";
                }
                
                prefText += ">"+preferences.get(preference).getName()+"</span></html>";
                rowData[index][preference] = prefText;
            }
        }
        
        // Updates the model for the votes table.
        votesTable.setModel(new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false; // Stops cells being edited.
            }
        });
    }
    
    /**
     * Gets an array of the candidate names for displaying in combo boxes.
     * @return The array of candidate names.
     */
    private String[] getCandidateNames() {
        int index = 1;
        ArrayList<Candidate> candidates = model.getCandidates();
        String[] names = new String[candidates.size()+1];
        
        // Adds the names of the candidate to an array.
        names[0] = ""; // Blank candidate (used when you don't want to select more candidates).
        for (Candidate candidate : candidates) {
            names[index++] = candidate.getName();
        }
        
        return names;
    }
    
    /**
     * Creates combo boxes for picking candidates for a new vote.
     * @return The panel containing the combo boxes so that it can be added to another panel.
     */
    private JPanel createComboBoxes() {
        JPanel container = new JPanel();
        JPanel comboContainer = new JPanel();
        JLabel preferenceLabel = new JLabel();
        String[] names = getCandidateNames();
        
        // Sets the text of the preference (new vote) label.
        String preferenceLabelText = "<html><div style='text-align: center; width: " + PANEL_SIZE.width + ";'><h3>New Vote</h3>";
        if (names.length < 2) {
            preferenceLabelText += "There are no candidates to vote for right now.<br><br>";
        }
        preferenceLabelText += "</div></html>";
        preferenceLabel.setText(preferenceLabelText);
        
        // Lays out the containers.
        container.setLayout(new BorderLayout());
        container.add(preferenceLabel, BorderLayout.PAGE_START);
        container.add(comboContainer, BorderLayout.PAGE_END);
        
        comboContainer.setLayout(new BoxLayout(comboContainer, BoxLayout.Y_AXIS));
        
        // Allows the user to select each of their preferences for a new vote.
        for (int index = 1; index < names.length; index++) {
            JPanel comboPanel = new JPanel();
            JLabel comboLabel = new JLabel("Preference " + index);
            JComboBox<String> comboBox = new JComboBox<>(names);
            comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.X_AXIS));
            comboPanel.add(comboLabel);
            comboPanel.add(comboBox);
            comboContainer.add(comboPanel);
            comboBoxes.add(comboBox);
        }
        
        // Returns the container so that it can be added to a panel.
        return container;
    }

    /**
     * Creates all of the controls for the view (buttons, panels, etc).
     */
    private void createControls() {
        JScrollPane votesScroller = new JScrollPane(votesTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel bottomPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();
        JLabel votesLabel = new JLabel("<html><div style='text-align: center; width: " + PANEL_SIZE.width + ";'><h3>Votes</h3></html>");
        
        // Enables/disables buttons.
        enableAdding();
        
        // Hooks up the controller on the action listeners.
        loadButton.addActionListener((e) -> controller.loadVotes());
        addButton.addActionListener((e) -> {
            // Uses the selected indexes of the combo boxes to determine the candidate IDs.
            ArrayList<Integer> candidateIds = new ArrayList<>();
            comboBoxes.forEach((comboBox) -> candidateIds.add(comboBox.getSelectedIndex() - 1));
            
            // Requests that the controller adds a vote with the given candidate IDs.
            controller.addVote(candidateIds);
        });
        
        // Lays out the components.
        panel.setLayout(new BorderLayout());
        panel.add(votesLabel, BorderLayout.PAGE_START);
        panel.add(votesScroller, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.PAGE_END);
        
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(createComboBoxes(), BorderLayout.PAGE_START);
        bottomPanel.add(buttonsPanel, BorderLayout.PAGE_END);
        
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(addButton);
        buttonsPanel.add(loadButton);
        
        
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
     * Displays an error message for the user.
     * @param message The message to be displayed as the error.
     */
    public void showError(String message) {
        showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Disables adding.
     */
    public void disableAdding() {
        loadButton.setEnabled(false);
        addButton.setEnabled(false);
    }
    
    /**
     * Enables adding.
     */
    public void enableAdding() {
        loadButton.setEnabled(true);
        addButton.setEnabled(true);
    }
}
