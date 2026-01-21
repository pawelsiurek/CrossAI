package crossai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.CompoundBorder;

import crossai.controller.AppController;
import crossai.model.Genre;
import crossai.model.Item;

/**
 * Modern UI for CrossAI Movie Recommender with Dark/Light theme support.
 */
public class ModernMainView extends JFrame {
    
    private AppController controller;
    
    // Theme colors
    private boolean isDarkMode = false;
    private Color bgPrimary, bgSecondary, bgCard, textPrimary, textSecondary;
    private Color accentColor, accentHover, borderColor, successColor;
    
    // UI Components
    private JTextField nameField;
    private JSpinner ageSpinner;
    private Map<Genre, JCheckBox> genreCheckboxes;
    private JPanel resultsPanel;
    private JPanel headerPanel;
    private JPanel mainContentPanel;
    private JPanel leftPanel;
    private JButton getRecsButton, clearButton, themeToggle;
    private JLabel statusLabel;
    
    // Panels that need theme updates
    private List<JPanel> themePanels = new ArrayList<>();
    private List<JComponent> themeComponents = new ArrayList<>();
    
    public ModernMainView() {
        super("CrossAI Movie Recommender");
        controller = new AppController(); 
        genreCheckboxes = new HashMap<>();
        
        initializeTheme();
        initializeUI();
        setupEventHandlers();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void initializeTheme() {
        updateThemeColors();
    }
    
    private void updateThemeColors() {
        if (isDarkMode) {
            // Dark mode colors
            bgPrimary = new Color(31, 41, 55);      // gray-800
            bgSecondary = new Color(17, 24, 39);    // gray-900
            bgCard = new Color(55, 65, 81);         // gray-700
            textPrimary = new Color(249, 250, 251); // gray-50
            textSecondary = new Color(156, 163, 175); // gray-400
            accentColor = new Color(59, 130, 246);  // blue-500
            accentHover = new Color(37, 99, 235);   // blue-600
            borderColor = new Color(75, 85, 99);    // gray-600
            successColor = new Color(34, 197, 94);  // green-500
        } else {
            // Light mode colors
            bgPrimary = new Color(249, 250, 251);   // gray-50
            bgSecondary = new Color(255, 255, 255); // white
            bgCard = new Color(255, 255, 255);      // white
            textPrimary = new Color(17, 24, 39);    // gray-900
            textSecondary = new Color(107, 114, 128); // gray-500
            accentColor = new Color(37, 99, 235);   // blue-600
            accentHover = new Color(29, 78, 216);   // blue-700
            borderColor = new Color(229, 231, 235); // gray-200
            successColor = new Color(22, 163, 74);  // green-600
        }
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(bgPrimary);
        
        // Header
        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);
        
        // Main content
        mainContentPanel = new JPanel(new BorderLayout(20, 20));
        mainContentPanel.setBackground(bgPrimary);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Left side
        leftPanel = createLeftPanel();
        
        // Right side
        JPanel rightPanel = createRightPanel();
        
        mainContentPanel.add(leftPanel, BorderLayout.CENTER);
        mainContentPanel.add(rightPanel, BorderLayout.EAST);
        
        add(mainContentPanel, BorderLayout.CENTER);
        
        // Status bar
        statusLabel = new JLabel(" Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setOpaque(true);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        themeComponents.add(statusLabel);
        add(statusLabel, BorderLayout.SOUTH);
        
        applyTheme();
    }
    
    private JPanel createHeader() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, borderColor),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel title = new JLabel("CrossAI Movie Recommender");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        themeComponents.add(title);
        headerPanel.add(title, BorderLayout.WEST);
        
        themeToggle = new JButton();
        updateThemeToggleIcon();
        themeToggle.setFocusPainted(false);
        themeToggle.setBorderPainted(false);
        themeToggle.setContentAreaFilled(false);
        themeToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        themeToggle.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        themeComponents.add(themeToggle);
        headerPanel.add(themeToggle, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createLeftPanel() {
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(bgPrimary);
        
        left.add(createUserInfoCard());
        left.add(Box.createRigidArea(new Dimension(0, 20)));
        left.add(createGenreCard());
        left.add(Box.createRigidArea(new Dimension(0, 20)));
        left.add(createActionButtons());
        
        return left;
    }
    
    private JPanel createUserInfoCard() {
        JPanel container = new JPanel(new BorderLayout(0, 10));
        container.setOpaque(false);
        
        JLabel sectionTitle = new JLabel("User Information");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        themeComponents.add(sectionTitle);
        container.add(sectionTitle, BorderLayout.NORTH);
        
        JPanel card = new JPanel(new GridBagLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, borderColor),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        themePanels.add(card);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        themeComponents.add(nameLabel);
        card.add(nameLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        nameField = createStyledTextField();
        card.add(nameField, gbc);
        
        // Age
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        themeComponents.add(ageLabel);
        card.add(ageLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0;
        SpinnerModel ageModel = new SpinnerNumberModel(25, 1, 120, 1);
        ageSpinner = new JSpinner(ageModel);
        ageSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ((JSpinner.DefaultEditor) ageSpinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
        
        // We add this to themeComponents so borders update, but applyTheme handles inner text color
        themeComponents.add(ageSpinner);
        card.add(ageSpinner, gbc);
        
        container.add(card, BorderLayout.CENTER);
        return container;
    }
    
    private JPanel createGenreCard() {
        JPanel container = new JPanel(new BorderLayout(0, 10));
        container.setOpaque(false);
        
        JLabel sectionTitle = new JLabel("Preferred Genres (Select at least one)");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        themeComponents.add(sectionTitle);
        container.add(sectionTitle, BorderLayout.NORTH);
        
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, borderColor),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        themePanels.add(card);
        
        JPanel genresGrid = new JPanel(new GridLayout(0, 3, 10, 10));
        genresGrid.setOpaque(false);
        
        for (Genre genre : Genre.values()) {
            JCheckBox checkbox = createStyledCheckbox(genre.getDisplayName());
            genreCheckboxes.put(genre, checkbox);
            genresGrid.add(checkbox);
        }
        
        JScrollPane scroll = new JScrollPane(genresGrid);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setPreferredSize(new Dimension(0, 180));
        
        card.add(scroll, BorderLayout.CENTER);
        container.add(card, BorderLayout.CENTER);
        
        return container;
    }
    
    private JPanel createActionButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setOpaque(false);
        
        getRecsButton = createPrimaryButton("Get Recommendations");
        clearButton = createSecondaryBlueButton("Clear");
        
        panel.add(getRecsButton);
        panel.add(clearButton);
        
        return panel;
    }
    
    private JPanel createRightPanel() {
        JPanel right = new JPanel(new BorderLayout(0, 10));
        right.setPreferredSize(new Dimension(400, 0));
        right.setBackground(bgPrimary);
        
        JLabel sectionTitle = new JLabel("Recommendations");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        // FIX: Force to Black and DO NOT add to themeComponents
        sectionTitle.setForeground(Color.BLACK);
        // themeComponents.add(sectionTitle); // Removed!
        
        right.add(sectionTitle, BorderLayout.NORTH);
        
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, borderColor),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setPreferredSize(new Dimension(400, 0));
        themePanels.add(card);
        
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setOpaque(false);
        
        JScrollPane scroll = new JScrollPane(resultsPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        
        card.add(scroll, BorderLayout.CENTER);
        right.add(card, BorderLayout.CENTER);
        
        return right;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(8, borderColor),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        themeComponents.add(field);
        return field;
    }
    
    private JCheckBox createStyledCheckbox(String text) {
        JCheckBox cb = new JCheckBox(text);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setFocusPainted(false);
        cb.setOpaque(false);
        cb.setCursor(new Cursor(Cursor.HAND_CURSOR));
        themeComponents.add(cb);
        return cb;
    }
    
    private JRadioButton createStyledRadioButton(String text, boolean selected) {
        JRadioButton rb = new JRadioButton(text, selected);
        rb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rb.setFocusPainted(false);
        rb.setOpaque(false);
        rb.setCursor(new Cursor(Cursor.HAND_CURSOR));
        themeComponents.add(rb);
        return rb;
    }
    
    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(accentColor);
        btn.setForeground(Color.WHITE);
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(accentHover);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(accentColor);
            }
        });
        
        return btn;
    }
    
    private JButton createSecondaryBlueButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(120, 45));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        Color secondaryBlue = new Color(59, 130, 246);
        Color secondaryBlueHover = new Color(37, 99, 235);
        
        btn.setBackground(secondaryBlue);
        btn.setForeground(Color.WHITE);
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(secondaryBlueHover);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(secondaryBlue);
            }
        });
        
        return btn;
    }
    
    private void setupEventHandlers() {
        themeToggle.addActionListener(e -> toggleTheme());
        getRecsButton.addActionListener(e -> handleGetRecommendations());
        clearButton.addActionListener(e -> handleClear());
    }
    
    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        updateThemeColors();
        updateThemeToggleIcon();
        applyTheme();
    }
    
    private void updateThemeToggleIcon() {
        themeToggle.setText(isDarkMode ? "Light" : "Dark");
        themeToggle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        themeToggle.setToolTipText(isDarkMode ? "Switch to Light Mode" : "Switch to Dark Mode");
    }
    
    private void applyTheme() {
        getContentPane().setBackground(bgPrimary);
        
        if (mainContentPanel != null) mainContentPanel.setBackground(bgPrimary);
        if (leftPanel != null) leftPanel.setBackground(bgPrimary);
        
        if (headerPanel != null) {
            headerPanel.setBackground(bgSecondary);
            headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, borderColor),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
            ));
        }
        
        resultsPanel.setBackground(bgPrimary);
        
        for (JPanel panel : themePanels) {
            panel.setBackground(bgCard);
            panel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(12, borderColor),
                panel.getBorder() instanceof CompoundBorder ? 
                    ((CompoundBorder) panel.getBorder()).getInsideBorder() : 
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
        }
        
        for (JComponent comp : themeComponents) {
            comp.setForeground(textPrimary);
            comp.setBackground(bgSecondary);
            
            if (comp instanceof JTextField || comp instanceof JSpinner) {
                comp.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(8, borderColor),
                    comp.getBorder() instanceof CompoundBorder ?
                        ((CompoundBorder) comp.getBorder()).getInsideBorder() :
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
            
            // FIX for Age Spinner
            if (comp instanceof JSpinner) {
                 JComponent editor = ((JSpinner.DefaultEditor)((JSpinner)comp).getEditor()).getTextField();
                 editor.setBackground(bgSecondary);
                 editor.setForeground(textPrimary);
            }
        }
        
        statusLabel.setBackground(bgSecondary);
        statusLabel.setForeground(textSecondary);
        
        for (Component comp : resultsPanel.getComponents()) {
            if (comp instanceof JPanel) {
                updateMovieCardTheme((JPanel) comp);
            }
        }
        
        repaint();
    }
    
    private void updateMovieCardTheme(JPanel card) {
        card.setBackground(bgCard);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, borderColor),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        
        for (Component comp : card.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getFont().getSize() == 20) {
                    label.setForeground(accentColor);
                }
            } else if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                panel.setBackground(bgCard);
                for (Component subComp : panel.getComponents()) {
                    if (subComp instanceof JLabel) {
                        JLabel label = (JLabel) subComp;
                        String text = label.getText();
                        
                        if (text.startsWith("Rating:")) {
                            label.setForeground(successColor);
                        } else if (text.startsWith("Genres:")) {
                            label.setForeground(textSecondary);
                        } else {
                            label.setForeground(textPrimary);
                        }
                    }
                }
            }
        }
    }
    
    private void handleGetRecommendations() {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                showError("Please enter your name.");
                return;
            }
            
            int age = (Integer) ageSpinner.getValue();
            
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
            
            controller.createUser(name, age);
            controller.addGenresToCurrentUser(selectedGenres);
            
            updateStatus("Getting recommendations...", false);
            getRecsButton.setEnabled(false);
            resultsPanel.removeAll();
            resultsPanel.revalidate();
            resultsPanel.repaint();
            
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
                        updateStatus("Found " + recommendations.size() + " recommendations!", true);
                    } catch (Exception ex) {
                        showError("Failed to get recommendations: " + ex.getMessage());
                        updateStatus("Error occurred", false);
                    } finally {
                        getRecsButton.setEnabled(true);
                    }
                }
            };
            worker.execute();
            
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
            getRecsButton.setEnabled(true);
        }
    }
    
    private void displayRecommendations(List<Item> recommendations) {
        resultsPanel.removeAll();
        
        if (recommendations.isEmpty()) {
            JLabel emptyLabel = new JLabel("No recommendations found.");
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            emptyLabel.setForeground(textSecondary);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultsPanel.add(emptyLabel);
        } else {
            for (int i = 0; i < recommendations.size(); i++) {
                Item item = recommendations.get(i);
                resultsPanel.add(createMovieCard(item, i + 1));
                resultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }
    
    private JPanel createMovieCard(Item item, int rank) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, borderColor),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        card.setBackground(bgCard);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        JLabel rankLabel = new JLabel(String.valueOf(rank));
        rankLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        rankLabel.setForeground(accentColor);
        rankLabel.setPreferredSize(new Dimension(35, 35));
        rankLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(rankLabel, BorderLayout.WEST);
        
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        
        JLabel title = new JLabel(item.getTitle());
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(textPrimary);
        info.add(title);
        
        info.add(Box.createRigidArea(new Dimension(0, 5)));
        
        if (!item.getGenres().isEmpty()) {
            JLabel genres = new JLabel("Genres: " + item.getGenresAsString());
            genres.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            genres.setForeground(textSecondary);
            info.add(genres);
        }
        
        if (item.getRating() > 0) {
            JLabel rating = new JLabel(String.format("Rating: %.1f/10", item.getRating()));
            rating.setFont(new Font("Segoe UI", Font.BOLD, 12));
            rating.setForeground(successColor);
            info.add(rating);
        }
        
        card.add(info, BorderLayout.CENTER);
        
        return card;
    }
    
    private void handleClear() {
        nameField.setText("");
        ageSpinner.setValue(25);
        
        for (JCheckBox cb : genreCheckboxes.values()) {
            cb.setSelected(false);
        }
        
        resultsPanel.removeAll();
        resultsPanel.revalidate();
        resultsPanel.repaint();
        
        controller.clearCurrentUser();
        updateStatus("Ready", false);
    }
    
    private void updateStatus(String message, boolean isSuccess) {
        statusLabel.setText("  " + message);
        statusLabel.setForeground(isSuccess ? successColor : textSecondary);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;
        
        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(2, 2, 2, 2);
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default
        }
        
        SwingUtilities.invokeLater(() -> new ModernMainView());
    }
}