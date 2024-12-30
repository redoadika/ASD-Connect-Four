/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #15
 * 1 - 5026231225 - Harbima Razan
 * 2 - 5026231134 - Muhammad Artha Maulana S.
 * 3 - 5026231171 - Redo Adika Dharmawan
 */
import java.awt.event.ActionListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;

public class ConnectFour extends JFrame {
    private final int ROWS = 6;
    private final int COLS = 7;
    private final int TIME_LIMIT = 20; // Waktu maksimum per giliran (detik)
    private JButton[][] buttons = new JButton[ROWS][COLS];
    private Board board = new Board(ROWS, COLS);
    private AI ai = new AI();
    private boolean playerTurn = true; // True: Player, False: Computer
    private Timer timer;
    private int remainingTime;
    private JLabel timerLabel;
    private JPanel leaderboardPanel;
    private HashMap<String, Integer> leaderboard = new HashMap<>();
    private String playerName;
    private int playerMoves;
    private Clip dropSound;
    private Clip winSound;


    public ConnectFour() {
        setTitle("Connect Four - Human vs Computer");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        // Load sounds
        loadSounds();
    
        // Timer
        timerLabel = new JLabel("Time Remaining: " + TIME_LIMIT + "s", JLabel.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(timerLabel, BorderLayout.NORTH);
    
        // Leaderboard
        leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
        leaderboardPanel.add(new JLabel("Leaderboard:"));
        updateLeaderboard();
    
        add(leaderboardPanel, BorderLayout.EAST);
    
        // Board
        JPanel boardPanel = new JPanel(new GridLayout(ROWS, COLS));
        initializeBoard(boardPanel);
        add(boardPanel, BorderLayout.CENTER);
    
        pack();
        setSize(800, 600);
        setVisible(false); // Awalnya tidak terlihat, akan ditampilkan setelah dialog selesai
    
        // Launch Welcome Screen
        showWelcomeScreen();
    }
    

    private void showWelcomeScreen() {
        JPanel welcomePanel = new JPanel(new BorderLayout());

        // Add description text
        JLabel textLabel = new JLabel("<html><div style='text-align: center;'>"
                + "Welcome to Connect Four!<br><br>"
                + "Drop your discs into the columns.<br>"
                + "Be the first to connect four in a row, column, or diagonal!<br><br>"
                + "Good luck and have fun!</div></html>", JLabel.CENTER);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomePanel.add(textLabel, BorderLayout.CENTER);

        // Add input field for username
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel("Enter Your Username (max 16 characters):");
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField nameField = new JTextField(16);
        nameField.setMaximumSize(new Dimension(200, 30));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new JButton("Start Game");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components to input panel
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(nameLabel);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(nameField);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(startButton);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        welcomePanel.add(inputPanel, BorderLayout.SOUTH);

        // Create and show dialog
        JDialog dialog = new JDialog(this, "Welcome", true);
        dialog.getContentPane().add(welcomePanel);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        startButton.addActionListener(e -> {
            playerName = nameField.getText();
            if (playerName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Invalid Input! Please enter your Username.");
                return;
            }
            if (playerName.length() > 16) {
                JOptionPane.showMessageDialog(dialog, "Name must be 16 characters or less!");
                return;
            }
            JOptionPane.showMessageDialog(dialog, "Welcome, " + playerName + "! Let's start the game.");
            dialog.dispose();
            this.setVisible(true);
            resetTimer();
        });

        dialog.setVisible(true);
    }

    private void initializeBoard(JPanel boardPanel) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                buttons[row][col] = new JButton();
                buttons[row][col].setBackground(Color.WHITE);
                buttons[row][col].setEnabled(true);
                final int currentCol = col;
                buttons[row][col].addActionListener(e -> {
                    if (playerTurn) {
                        makeMove(currentCol, 1);
                        resetTimer();
                        if (board.checkWin(1)) {
                            updateScore();
                            JOptionPane.showMessageDialog(this, "You win!");
                            resetGame();
                        } else {
                            playerTurn = false;
                            computerMove();
                        }
                    }
                });
                boardPanel.add(buttons[row][col]);
            }
        }
    }

    private void makeMove(int col, int player) {
        int row = board.makeMove(col, player);
        if (row != -1) {
            buttons[row][col].setIcon(player == 1 ? new ImageIcon("resources/player_piece.png") : new ImageIcon("resources/computer_piece.png"));
    
            if (dropSound != null) {
                dropSound.setFramePosition(0); // Reset sound ke awal
                dropSound.start();
            }
    
            // Cek apakah pemain menang
            if (board.checkWin(player)) {
                if (winSound != null) {
                    winSound.setFramePosition(0); // Reset sound ke awal
                    winSound.start();
                }
                if (player == 1) {
                    updateScore(); // Perbarui skor pemain
                    JOptionPane.showMessageDialog(this, "You win!");
                } else {
                    JOptionPane.showMessageDialog(this, "Computer wins!");
                }
                resetGame();
            }
        }
    }
    
    
    

    private void computerMove() {
        int bestCol = ai.findBestMove(board);
        makeMove(bestCol, 2);
        resetTimer();
        if (board.checkWin(2)) {
            // Putar suara kemenangan sebelum dialog
            if (winSound != null) {
                winSound.setFramePosition(0); // Reset sound ke awal
                winSound.start();
            }
            JOptionPane.showMessageDialog(this, "Computer wins!"); // Tampilkan dialog
            resetGame();
        } else {
            playerTurn = true;
        }
    }
    

    private void resetTimer() {
        if (timer != null) {
            timer.stop();
        }
        remainingTime = TIME_LIMIT; // Atur waktu awal
        timerLabel.setText("Time Remaining: " + remainingTime + "s");

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remainingTime--;
                timerLabel.setText("Time Remaining: " + remainingTime + "s");
                if (remainingTime <= 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(ConnectFour.this, "Time's up! You lose your turn.");
                    playerTurn = false;
                    computerMove();
                }
            }
        });
        timer.start();
    }

    private void updateScore() {
        int initialScore = 1000;
        int score = Math.max(0, initialScore - (playerMoves * 10)); // Kurangi 10 poin per langkah
        leaderboard.put(playerName, leaderboard.getOrDefault(playerName, 0) + score);
        updateLeaderboard();
    }

    private void updateLeaderboard() {
        leaderboardPanel.removeAll();
        leaderboardPanel.add(new JLabel("Leaderboard:"));
        leaderboard.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .forEach(entry -> leaderboardPanel.add(new JLabel(entry.getKey() + " : " + entry.getValue())));
        leaderboardPanel.revalidate();
        leaderboardPanel.repaint();
    }

    private void resetGame() {
        board = new Board(ROWS, COLS);
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                buttons[row][col].setIcon(null);
            }
        }
        playerMoves = 0; // Reset langkah
        playerTurn = true;
        resetTimer();
    }
    
    

    private void loadSounds() {
    try {
        // Load sound for dropping pieces
        AudioInputStream dropAudio = AudioSystem.getAudioInputStream(getClass().getResource("/resources/drop_piece.wav"));
        dropSound = AudioSystem.getClip();
        dropSound.open(dropAudio);

        // Load sound for winning
        AudioInputStream winAudio = AudioSystem.getAudioInputStream(getClass().getResource("/resources/win_sound.wav"));
        winSound = AudioSystem.getClip();
        winSound.open(winAudio);
    } catch (Exception e) {
        System.err.println("Error loading sounds: " + e.getMessage());
    }
}



    public static void main(String[] args) {
        new ConnectFour();
    }
}
