/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #15
 * 1 - 5026231225 - Harbima Razan
 * 2 - 5026231134 - Muhammad Artha Maulana S.
 * 3 - 5026231171 - Redo Adika Dharmawan
 */

public class AI {
    public int findBestMove(Board board) {
        int bestScore = Integer.MIN_VALUE;
        int bestCol = 0;

        for (int col = 0; col < 7; col++) {
            if (board.isValidMove(col)) {
                board.makeMove(col, 2);
                int score = minimax(board, 0, false);
                board.undoMove(col);
                if (score > bestScore) {
                    bestScore = score;
                    bestCol = col;
                }
            }
        }
        return bestCol;
    }

    private int minimax(Board board, int depth, boolean isMaximizing) {
        if (board.checkWin(1)) return -10;
        if (board.checkWin(2)) return 10;
        if (board.isFull() || depth >= 5) return 0;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int col = 0; col < 7; col++) {
                if (board.isValidMove(col)) {
                    board.makeMove(col, 2);
                    int score = minimax(board, depth + 1, false);
                    board.undoMove(col);
                    bestScore = Math.max(score, bestScore);
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int col = 0; col < 7; col++) {
                if (board.isValidMove(col)) {
                    board.makeMove(col, 1);
                    int score = minimax(board, depth + 1, true);
                    board.undoMove(col);
                    bestScore = Math.min(score, bestScore);
                }
            }
            return bestScore;
        }
    }
}