package alternatevote;

import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JFrame;

/**
 * A frame used to display views.
 * @author ryansmith
 */
@SuppressWarnings("serial")
public class AVFrame extends JFrame {
    public AVFrame(AVVotesView ballotView, AVCountingView countView) {
        this.setTitle("Alternative Vote Counter");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
        
        // Adds the views to the content pane.
        contentPane.add(ballotView.getPanel());
        contentPane.add(countView.getPanel());
        
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }
}
