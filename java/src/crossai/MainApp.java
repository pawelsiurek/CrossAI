package crossai;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import crossai.controller.AppController;
import crossai.model.Genre;
import crossai.model.Item;

/**
 * Main application entry point with Swing GUI.
 * Provides a graphical interface for the CrossAI Movie Recommender System.
 */
public class MainApp extends JFrame {
    
    private AppController controller;
    
    // UI Components
    private JTextField nameField;
    private JSpinner ageSpinner;
    private Map<Genre, JCheckBox> genreCheckboxes;
    private JRadioButton mockRadio;
    private JRadioButton hybridRadio;
    private JTextArea resultsArea;
    private JButton getRecommendationsButton;
    private JButton clearButton;
    private JLabel statusLabel;
    
    /**
     * Constructor - sets up the GUI.
     */
    public MainApp() {
        super("CrossAI Movie Recommender");
        controller = new AppController(true); // Start with Mock service
        genreCheckboxes = new HashMap<>();
        
        initializeUI();
        setupEventHandlers();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 800);
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }
    
    /**
     * Initialize all UI components.
     */
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Top panel - User info
        JPanel topPanel = createUserInfoPanel();
        
        // Center panel - Genre selection
        JPanel centerPanel = createGenrePanel();
        
        // Bottom panel - Service selection and buttons
        JPanel bottomPanel = createBottomPanel();
        
        // Results panel
        JPanel resultsPanel = createResultsPanel();
        
        // Add all panels
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        add(resultsPanel, BorderLayout.EAST);
        
        // Status bar at the very bottom
        statusLabel = new JLabel("Ready - Using Mock Service");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);
    }
    
    /**
     * Create user information input panel.
     */
    private JPanel createUserInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("User Information"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        nameField = new JTextField(20);
        panel.add(nameField, gbc);
        
        // Age field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Age:"), gbc);
        
        gbc.gridx = 1;
        SpinnerModel ageModel = new SpinnerNumberModel(25, 1, 120, 1);
        ageSpinner = new JSpinner(ageModel);
        panel.add(ageSpinner, gbc);
        
        return panel;
    }
    
    /**
     * Create genre selection panel with checkboxes.
     */
    private JPanel createGenrePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Preferred Genres (Select at least one)"));
        
        // Create scrollable panel for genres
        JPanel genresGrid = new JPanel(new GridLayout(0, 3, 10, 5));
        genresGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add checkbox for each genre
        for (Genre genre : Genre.values()) {
            JCheckBox checkbox = new JCheckBox(genre.getDisplayName());
            genreCheckboxes.put(genre, checkbox);
            genresGrid.add(checkbox);
        }
        
        JScrollPane scrollPane = new JScrollPane(genresGrid);
        scrollPane.setPreferredSize(new Dimension(600, 250));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create bottom panel with service selection and action buttons.
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Service selection
        JPanel servicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        servicePanel.setBorder(BorderFactory.createTitledBorder("Recommendation Service"));
        
        mockRadio = new JRadioButton("Mock Service (Test Data)", true);
        hybridRadio = new JRadioButton("Hybrid Service (Real C++/Python)");
        
        ButtonGroup serviceGroup = new ButtonGroup();
        serviceGroup.add(mockRadio);
        serviceGroup.add(hybridRadio);
        
        servicePanel.add(mockRadio);
        servicePanel.add(hybridRadio);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        getRecommendationsButton = new JButton("Get Recommendations");
        getRecommendationsButton.setFont(new Font("Arial", Font.BOLD, 14));
        getRecommendationsButton.setPreferredSize(new Dimension(200, 40));
        
        clearButton = new JButton("Clear");
        clearButton.setPreferredSize(new Dimension(100, 40));
        
        buttonsPanel.add(getRecommendationsButton);
        buttonsPanel.add(clearButton);
        
        panel.add(servicePanel, BorderLayout.NORTH);
        panel.add(buttonsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Create results display panel.
     */
    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Recommendations"));
        panel.setPreferredSize(new Dimension(300, 0));
        
        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Setup event handlers for all interactive components.
     */
    private void setupEventHandlers() {
        // Get Recommendations button
        getRecommendationsButton.addActionListener(e -> handleGetRecommendations());
        
        // Clear button
        clearButton.addActionListener(e -> handleClear());
        
        // Radio button changes
        mockRadio.addActionListener(e -> {
            controller.setUseMockService(true);
            statusLabel.setText("Using Mock Service");
        });
        
        hybridRadio.addActionListener(e -> {
            controller.setUseMockService(false);
            statusLabel.setText("Using Hybrid Service (C++/Python)");
        });
    }
    
    /**
     * Handle Get Recommendations button click.
     */
    private void handleGetRecommendations() {
        try {
            // Validate input
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                showError("Please enter your name.");
                return;
            }
            
            int age = (Integer) ageSpinner.getValue();
            
            // Get selected genres
            List<Genre> selectedGenres = new ArrayList<>();
            for (Map.Entry<Genre, JCheckBox> entry : genreCheckboxes.entrySet()) {
                if (entry.getValue().isSelected()) {
                    selectedGenres.add(entry.getKey());
                }
            }
            
            if (selectedGenres.isEmpty()) {
                showError("Please select at least one genre.");
                return;
            }
            
            // Create user
            controller.createUser(name, age);
            controller.addGenresToCurrentUser(selectedGenres);
            
            // Update status
            statusLabel.setText("Getting recommendations...");
            getRecommendationsButton.setEnabled(false);
            
            // Get recommendations (in background to keep UI responsive)
            SwingWorker<List<Item>, Void> worker = new SwingWorker<>() {
                @Override
                protected List<Item> doInBackground() throws Exception {
                    return controller.getRecommendationsForCurrentUser();
                }
                
                @Override
                protected void done() {
                    try {
                        List<Item> recommendations = get();
                        displayRecommendations(recommendations);
                        statusLabel.setText("Found " + recommendations.size() + " recommendations!");
                    } catch (Exception ex) {
                        showError("Failed to get recommendations: " + ex.getMessage());
                        statusLabel.setText("Error occurred");
                    } finally {
                        getRecommendationsButton.setEnabled(true);
                    }
                }
            };
            worker.execute();
            
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
            getRecommendationsButton.setEnabled(true);
        }
    }
    
    /**
     * Display recommendations in the results area.
     */
    private void displayRecommendations(List<Item> recommendations) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== YOUR RECOMMENDATIONS ===\n\n");
        
        int count = 1;
        for (Item item : recommendations) {
            sb.append(count++).append(". ").append(item.getTitle()).append("\n");
            
            String desc = item.getDescription();
            if (!desc.isEmpty()) {
                sb.append("   ").append(desc).append("\n");
            }
            sb.append("\n");
        }
        
        resultsArea.setText(sb.toString());
        resultsArea.setCaretPosition(0); // Scroll to top
    }
    
    /**
     * Handle Clear button click.
     */
    private void handleClear() {
        nameField.setText("");
        ageSpinner.setValue(25);
        
        for (JCheckBox checkbox : genreCheckboxes.values()) {
            checkbox.setSelected(false);
        }
        
        resultsArea.setText("");
        controller.clearCurrentUser();
        statusLabel.setText("Ready - " + 
            (mockRadio.isSelected() ? "Using Mock Service" : "Using Hybrid Service"));
    }
    
    /**
     * Show error message dialog.
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Main entry point.
     */
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // If it fails, use default Java look and feel
        }
        
        // Create GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new MainApp());
    }
}