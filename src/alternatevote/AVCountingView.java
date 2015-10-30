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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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
    private final JTable candidatesTable = new JTable();
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
        ArrayList<Candidate> candidates = new ArrayList<>(model.getCandidates());
        String[] columnNames = {"Name", "Count"};
        Object[][] rowData = new Object[candidates.size()][2];
        
        // Sorts candidates.
        candidates.sort((candidate1, candidate2) -> {
            return Integer.compare(candidate2.getCount(), candidate1.getCount());
        });
        
        // Adds rows to candidates table.
        for (int index = 0; index < candidates.size(); index++) {
            if (!candidates.get(index).isEliminated()) {
                rowData[index][0] = candidates.get(index).getName();
                rowData[index][1] = candidates.get(index).getCount();
            }
        }
        
        // Updates the model for the candidates table.
        candidatesTable.setModel(new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false; // Stops cells being edited.
            }
        });
    }

    /**
     * Creates all of the controls for the view (buttons, panels, etc).
     */
    private void createControls() {
        JPanel buttonsPanel = new JPanel();
        JLabel candidatesLabel = new JLabel("<html><div style='text-align: center; width: " + PANEL_SIZE.width + ";'><h3>Counts</h3></html>");
        JScrollPane candidatesScroller = new JScrollPane(candidatesTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        // Enables/disables buttons.
        enableStart();
        
        // Hooks up the controller on the action listeners.
        startButton.addActionListener((e) -> controller.startCounting());
        redistributeButton.addActionListener((e) -> controller.redistribute());
        
        // Lays out the components.
        panel.setLayout(new BorderLayout());
        panel.add(candidatesLabel, BorderLayout.PAGE_START);
        panel.add(candidatesScroller, BorderLayout.CENTER);
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
