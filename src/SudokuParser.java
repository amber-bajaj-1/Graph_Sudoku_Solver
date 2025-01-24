import java.io.*;
import java.util.*;

public class SudokuParser {
    //extracts all one-line Sudoku boards from the file
    public static List<int[][]> parseSudokuBoards(String fileName) {
        List<int[][]> sudokuBoards = new ArrayList<>();

        try (InputStream inputStream = SudokuParser.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) { //skips empty lines
                    int[][] board = parseSudokuLine(line.trim());
                    sudokuBoards.add(board);
                }
            }
        }
        catch (IOException | NullPointerException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        return sudokuBoards;
    }

    //turns a single line of text file into a 9x9 Sudoku board
    private static int[][] parseSudokuLine(String line) {
        int[][] board = new int[9][9];
        for (int i = 0; i < line.length(); i++) {
            int row = i / 9; //calculates the row index
            int col = i % 9; //calculates the column index
            board[row][col] = Character.getNumericValue(line.charAt(i));
        }
        return board;
    }
}