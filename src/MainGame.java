import javax.swing.*;
import java.awt.*;

public class MainGame extends JFrame {
    public MainGame() {
        setTitle("Choose Your Game");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        showWelcomeScreen();
    }

    private void showWelcomeScreen() {
        JPanel welcomePanel = new JPanel(new BorderLayout());

        // Add description text
        JLabel textLabel = new JLabel("<html><div style='text-align: center;'>"
                + "Welcome to Group 15 Tic Tac Toe and Connect Four!<br><br>"
                + "Choose the game you want to play:<br><br>"
                + "Good luck and have fun!</div></html>", JLabel.CENTER);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomePanel.add(textLabel, BorderLayout.CENTER);

        // Buttons for game selection
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding to the panel

        JButton connectFourButton = new JButton("Play Connect Four");
        connectFourButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        connectFourButton.setMaximumSize(new Dimension(200, 30)); // Set size for uniformity

        JButton ticTacToeButton = new JButton("Play Tic-Tac-Toe");
        ticTacToeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticTacToeButton.setMaximumSize(new Dimension(200, 30)); // Set size for uniformity

        // Add spacing between buttons
        buttonPanel.add(connectFourButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        buttonPanel.add(ticTacToeButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Extra spacer at the bottom

        welcomePanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add welcome panel to frame
        this.add(welcomePanel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // Action listeners for buttons
        connectFourButton.addActionListener(e -> {
            this.setVisible(false); // Hide the main window
            new ConnectFour(); // Launch Connect Four
        });

        ticTacToeButton.addActionListener(e -> {
            this.setVisible(false); // Hide the main window
            launchTicTacToe(); // Launch Tic-Tac-Toe
        });
    }

    private void launchTicTacToe() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(Tictactoe.TITLE);
            frame.setContentPane(new Tictactoe()); // Tic-Tac-Toe panel
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null); // Center the application window
            frame.setVisible(true); // Show it
        });
    }

    public static void main(String[] args) {
        new MainGame();
    }
}
