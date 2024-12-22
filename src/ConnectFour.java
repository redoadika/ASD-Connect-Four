/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #15
 * 1 - 5026231225 - Harbima Razan
 * 2 - 5026231134 - Muhammad Artha Maulana S.
 * 3 - 5026231171 - Redo Adika Dharmawan
 */

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class ConnectFour extends JFrame {
    private final int ROWS = 6;
    private final int COLS = 7;
    private JButton[][] buttons = new JButton[ROWS][COLS];
    private Board board = new Board(ROWS, COLS);
    private AI ai = new AI();
    private boolean playerTurn = true; // True: Player, False: Computer
    private ImageIcon playerIcon = new ImageIcon("resources/player_piece.png");
    private ImageIcon computerIcon = new ImageIcon("resources/computer_piece.png");
    private String dropSound = "resources/drop_piece.mp3";
    private String winSound = "resources/win_sound.mp3";

    public ConnectFour() {
        setTitle("Connect Four - Human vs Computer");
        setLayout(new GridLayout(ROWS, COLS));
        initializeBoard();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setVisible(true);
    }

    private void initializeBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                buttons[row][col] = new JButton();
                buttons[row][col].setBackground(Color.WHITE);
                buttons[row][col].setEnabled(true);
                final int currentCol = col;
                buttons[row][col].addActionListener(e -> {
                    if (playerTurn) {
                        makeMove(currentCol, 1);
                        playSound(dropSound);
                        if (board.checkWin(1)) {
                            playSound(winSound);
                            JOptionPane.showMessageDialog(this, "You win!");
                            resetGame();
                        } else {
                            playerTurn = false;
                            computerMove();
                        }
                    }
                });
                add(buttons[row][col]);
            }
        }
    }

    private void makeMove(int col, int player) {
        int row = board.makeMove(col, player);
        if (row != -1) {
            buttons[row][col].setIcon(player == 1 ? playerIcon : computerIcon);
        }
    }

    private void computerMove() {
        int bestCol = ai.findBestMove(board);
        makeMove(bestCol, 2);
        playSound(dropSound);
        if (board.checkWin(2)) {
            playSound(winSound);
            JOptionPane.showMessageDialog(this, "Computer wins!");
            resetGame();
        } else {
            playerTurn = true;
        }
    }

    private void resetGame() {
        dispose();
        new ConnectFour();
    }

    private void playSound(String soundFile) {
        try {
            File file = new File(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ConnectFour();
    }
}