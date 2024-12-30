import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.sound.sampled.*;
/**
 * Tic-Tac-Toe: Two-player Graphic version with better OO design.
 * The Board and Cell classes are separated in their own classes.
 */
public class Tictactoe extends JPanel {
   private static final long serialVersionUID = 1L; // to prevent serializable warning

   // Define named constants for the drawing graphics
   public static final String TITLE = "Tic Tac Toe";
   public static final Color COLOR_BG = Color.WHITE;
   public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
   public static final Color COLOR_CROSS = new Color(239, 105, 80);  // Red #EF6950
   public static final Color COLOR_NOUGHT = new Color(64, 154, 225); // Blue #409AE1
   public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

   // Define game objects
   private BoardTTT board;         // the game board
   private State currentState;  // the current state of the game
   private Seed currentPlayer;  // the current player
   private JLabel statusBar;    // for displaying status message

   private BufferedImage playerImage, computerImage;
   private Clip dropSound, winSound;

   /** Constructor to setup the UI and game components */
   public Tictactoe() {
    loadSounds();

     // Load gambar dan audio
     try {
        playerImage = ImageIO.read(new File("resources/player_piece.png"));
        computerImage = ImageIO.read(new File("resources/computer_piece.png"));

        AudioInputStream dropStream = AudioSystem.getAudioInputStream(new File("drop_piece.wav"));
        dropSound = AudioSystem.getClip();
        dropSound.open(dropStream);

        AudioInputStream winStream = AudioSystem.getAudioInputStream(new File("win_sound.wav"));
        winSound = AudioSystem.getClip();
        winSound.open(winStream);
    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
        ex.printStackTrace();
    }


      // This JPanel fires MouseEvent
      super.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            int row = mouseY / Cell.SIZE;
            int col = mouseX / Cell.SIZE;
        
            if (currentState == State.PLAYING) {
                if (row >= 0 && row < BoardTTT.ROWS && col >= 0 && col < BoardTTT.COLS
                        && board.cells[row][col].content == Seed.NO_SEED) {
                    currentState = board.stepGame(currentPlayer, row, col);
                    dropSound.start();  // Play drop sound
                    dropSound.setFramePosition(0);  // Reset audio
                    currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                }
            } else {
                newGame();
            }
            if (currentState == State.CROSS_WON || currentState == State.NOUGHT_WON) {
                winSound.start();  // Play win sound
                winSound.setFramePosition(0);  // Reset audio
            }
            repaint();
        }
        
      });

      // Setup the status bar (JLabel) to display status message
      statusBar = new JLabel();
      statusBar.setFont(FONT_STATUS);
      statusBar.setBackground(COLOR_BG_STATUS);
      statusBar.setOpaque(true);
      statusBar.setPreferredSize(new Dimension(300, 30));
      statusBar.setHorizontalAlignment(JLabel.LEFT);
      statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

      super.setLayout(new BorderLayout());
      super.add(statusBar, BorderLayout.PAGE_END); // same as SOUTH
      super.setPreferredSize(new Dimension(BoardTTT.CANVAS_WIDTH, BoardTTT.CANVAS_HEIGHT + 30));
            // account for statusBar in height
      super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

      // Set up Game
      initGame();
      newGame();
   }

   /** Initialize the game (run once) */
   public void initGame() {
      board = new BoardTTT();  // allocate the game-board
   }

   /** Reset the game-board contents and the current-state, ready for new game */
   public void newGame() {
      for (int row = 0; row < BoardTTT.ROWS; ++row) {
         for (int col = 0; col < BoardTTT.COLS; ++col) {
            board.cells[row][col].content = Seed.NO_SEED; // all cells empty
         }
      }
      currentPlayer = Seed.CROSS;    // cross plays first
      currentState = State.PLAYING;  // ready to play
   }

   /** Custom painting codes on this JPanel */
   @Override
   public void paintComponent(Graphics g) {
       super.paintComponent(g);
       setBackground(COLOR_BG);
       board.paint(g, playerImage, computerImage); // Pass images here
       if (currentState == State.PLAYING) {
           statusBar.setForeground(Color.BLACK);
           statusBar.setText((currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn");
       } else if (currentState == State.DRAW) {
           statusBar.setForeground(Color.RED);
           statusBar.setText("It's a Draw! Click to play again.");
       } else if (currentState == State.CROSS_WON) {
           statusBar.setForeground(Color.RED);
           statusBar.setText("'X' Won! Click to play again.");
       } else if (currentState == State.NOUGHT_WON) {
           statusBar.setForeground(Color.RED);
           statusBar.setText("'O' Won! Click to play again.");
       }
   }

   private void loadSounds() {
    try {
        AudioInputStream dropAudio = AudioSystem.getAudioInputStream(getClass().getResource("/resources/drop_piece.wav"));
        dropSound = AudioSystem.getClip();
        dropSound.open(dropAudio);

        AudioInputStream winAudio = AudioSystem.getAudioInputStream(getClass().getResource("/resources/win_sound.wav"));
        winSound = AudioSystem.getClip();
        winSound.open(winAudio);
    } catch (Exception e) {
        System.err.println("Error loading sounds: " + e.getMessage());
    }
}

   
   

   /** The entry "main" method */
   public static void main(String[] args) {
      // Run GUI construction codes in Event-Dispatching thread for thread safety
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            JFrame frame = new JFrame(TITLE);
            // Set the content-pane of the JFrame to an instance of main JPanel
            frame.setContentPane(new Tictactoe());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null); // center the application window
            frame.setVisible(true);            // show it
         }
      });
   }
}