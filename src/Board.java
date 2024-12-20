/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #15
 * 1 - 5026231225 - Harbima Razan
 * 2 - 5026231134 - Muhammad Artha Maulana S.
 * 3 - 5026231171 - Redo Adika Dharmawan
 */

public class Board {
    private int[][] board;
    private int rows, cols;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        board = new int[rows][cols];
    }

    public int makeMove(int col, int player) {
        for (int row = rows - 1; row >= 0; row--) {
            if (board[row][col] == 0) {
                board[row][col] = player;
                return row;
            }
        }
        return -1;
    }

    public void undoMove(int col) {
        for (int row = 0; row < rows; row++) {
            if (board[row][col] != 0) {
                board[row][col] = 0;
                break;
            }
        }
    }

    public boolean isValidMove(int col) {
        return board[0][col] == 0;
    }

    public boolean isFull() {
        for (int col = 0; col < cols; col++) {
            if (isValidMove(col)) return false;
        }
        return true;
    }

    public boolean checkWin(int player) {
        // Horizontal, vertical, diagonal checks
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (checkDirection(row, col, player, 1, 0) ||  // Horizontal
                        checkDirection(row, col, player, 0, 1) ||  // Vertical
                        checkDirection(row, col, player, 1, 1) ||  // Diagonal Right
                        checkDirection(row, col, player, 1, -1)) { // Diagonal Left
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDirection(int row, int col, int player, int dRow, int dCol) {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            int newRow = row + i * dRow;
            int newCol = col + i * dCol;
            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && board[newRow][newCol] == player) {
                count++;
            } else {
                break;
            }
        }
        return count == 4;
    }

    public int[][] getBoard() {
        return board;
    }
}