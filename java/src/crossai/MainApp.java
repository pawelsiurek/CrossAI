package crossai;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main application entry point.
 * Launches the modern CrossAI Movie Recommender GUI.
 */
public class MainApp {
    
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default if system L&F fails
        }
        
        // Launch the modern UI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new ModernMainView());
    }
}