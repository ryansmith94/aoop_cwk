package alternatevote;

/**
 * A program for the alternate vote.
 * @author ryansmith
 */
public class AVCounter {

    /**
     * Starts the program.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    /**
     * Creates a shows the views in a frame.
     */
    private static void createAndShowGUI() {
        AVModel model = new AVModel();
        AVController controller = new AVController(model);
        AVVotesView ballotView = new AVVotesView(model, controller);
        AVCountingView countView = new AVCountingView(model, controller);
        AVFrame frame = new AVFrame(ballotView, countView);
    }
    
}
