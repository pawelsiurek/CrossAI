package crossai;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main application entry point.
 * Launches the modern CrossAI Movie Recommender GUI.
 */
public class MainApp {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(() -> new ModernMainView());
    }
}