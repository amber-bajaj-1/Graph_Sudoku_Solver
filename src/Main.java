import java.util.*;

public class Main {
    public static void main(String[] args) {
        //input the puzzle to-be-solved in place of "getHardSudokuProblem()"
        Graph sudokuGraph = new Graph(getHardSudokuProblem()); //turns the 2D grid into a graph datatype

        sudokuGraph.buildSudokuEdges(); //builds edges for the Sudoku graph within boxes, rows & columns
        sudokuGraph.printSudokuGraph(); //prints the initial Sudoku problem (for reference)

        //input the desired solving method in place of "OptimizedSolver"
        Solver solution = new OptimizedSolver(sudokuGraph);
        solution.solve();

        sudokuGraph.printSudokuGraph(); //prints the solution
        if (sudokuGraph.isSolution()){
            System.out.println("Solution found & displayed above");
        }
        else{
            System.out.println("No solution possible");
        }

        //---- FOR EMPIRICALLY TESTING EFFICIENCY: SOLVING MULTIPLE SUDOKU BOARDS ----
        /*
        int numBoards = 1000;
        int numRepetitions = 1;
        double runningSum = 0;
        for (int i = 0; i < numRepetitions; i++) {
            //replace "OptimizedSolver(null)" below to test the efficiency of a different solver method
            runningSum += testOnNSudokuProblems(new OptimizedSolver(null), numBoards, "25clueSudokus");
        }
        System.out.println("avg time to solve " + numBoards + " Sudokus: " + runningSum/numRepetitions);
        */
    }

    public static double testOnNSudokuProblems(Solver solver, int numBoards, String boardFileName) {
        List<int[][]> sudokuBoards = SudokuParser.parseSudokuBoards(boardFileName);

        //starts the timer
        long startTime = System.nanoTime();

        for (int i = 0; i < numBoards; i++) {
            int[][] board = sudokuBoards.get(i);

            //converts the board into a graph, and solves using the given solver
            Graph currentSudokuGraph = new Graph(board);
            currentSudokuGraph.buildSudokuEdges();
            Solver solution = createSolverInstance(solver, currentSudokuGraph);

            //(optional: may be modified to better test runtime) prints the original & solution Sudoku boards
            System.out.println("Solving Sudoku #" + (i + 1));
            currentSudokuGraph.printSudokuGraph();
            if (solution.solve()) {
                System.out.println("Solution for Sudoku #" + (i + 1) + " is found:");
                currentSudokuGraph.printSudokuGraph();
            } else {
                System.out.println("No solution found for Sudoku #" + (i + 1));
            }
        }

        //ends the timer
        long endTime = System.nanoTime();

        //calculates the elapsed time, in seconds
        double elapsedTimeSeconds = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("Time taken to solve " + numBoards + " Sudoku boards is: " + elapsedTimeSeconds + " seconds");
        return elapsedTimeSeconds;
    }

    private static Solver createSolverInstance(Solver solverPrototype, Graph graph) {
        if (solverPrototype instanceof DFSSolver) {
            return new DFSSolver(graph);
        }
        else if (solverPrototype instanceof BFSSolver) {
            return new BFSSolver(graph);
        }
        else if (solverPrototype instanceof OptimizedSolver) {
            return new OptimizedSolver(graph);
        }
        else {
            throw new IllegalArgumentException("Unsupported solver type: " + solverPrototype.getClass().getSimpleName());
        }
    }

    //---- BUNCH OF PRE-BUILT SUDOKU PUZZLES ----
    //*any non-zero values will become fixed "hints" within the puzzle
    public static int[][] getSuperEasySudokuProblem() {
        int[][] superEasySudokuProblem = {
                {4, 1, 5, 7, 3, 8, 0, 6, 9},
                {0, 6, 0, 2, 4, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 1, 0, 7, 3},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 5, 0, 3, 8, 2, 0, 0, 0},
                {0, 8, 4, 9, 0, 6, 0, 5, 0},
                {0, 3, 1, 0, 2, 0, 9, 0, 6},
                {0, 7, 2, 8, 9, 4, 0, 3, 5},
                {0, 0, 0, 0, 6, 0, 7, 0, 8}};

        return superEasySudokuProblem;
    }

    public static int[][] getEasySudokuProblem() {
        int[][] easySudokuProblem = {
                {0, 0, 5, 0, 0, 8, 0, 6, 9},
                {0, 6, 0, 2, 4, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 1, 0, 7, 3},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 5, 0, 3, 8, 2, 0, 0, 0},
                {0, 8, 4, 9, 0, 6, 0, 5, 0},
                {0, 0, 1, 0, 2, 0, 9, 0, 6},
                {0, 7, 0, 8, 0, 4, 0, 3, 0},
                {0, 0, 0, 0, 0, 0, 7, 0, 0}};

        return easySudokuProblem;
    }

    public static int[][] getMediumSudokuProblem() {
        int[][] mediumSudokuProblem = {
                {0, 0, 5, 0, 0, 8, 0, 6, 9},
                {0, 6, 0, 2, 4, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 1, 0, 7, 3},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 5, 0, 3, 8, 2, 0, 0, 0},
                {0, 8, 4, 9, 0, 6, 0, 5, 0},
                {0, 0, 1, 0, 2, 0, 9, 0, 6},
                {0, 7, 0, 8, 0, 4, 0, 3, 0},
                {0, 0, 0, 0, 0, 0, 7, 0, 0}};

        return mediumSudokuProblem;
    }

    public static int[][] getHardSudokuProblem() {
        int[][] hardSudokuProblem = {
                {0, 0, 0, 0, 0, 3, 2, 0, 0},
                {0, 4, 0, 0, 1, 0, 0, 0, 6},
                {8, 3, 0, 4, 9, 0, 0, 5, 7},
                {9, 8, 0, 5, 0, 0, 0, 7, 0},
                {0, 0, 0, 6, 0, 0, 0, 3, 0},
                {0, 0, 3, 0, 2, 0, 0, 4, 9},
                {0, 6, 0, 0, 7, 0, 0, 2, 8},
                {0, 0, 0, 1, 0, 5, 0, 0, 0},
                {0, 0, 0, 9, 6, 0, 0, 1, 0}};

        return hardSudokuProblem;
    }

    public static int[][] getVeryHardSudokuProblem() {
        int[][] veryHardSudokuProblem = {
                {5, 6, 0, 7, 2, 4, 0, 0, 0},
                {0, 9, 0, 0, 0, 6, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 8, 0, 0},
                {0, 0, 4, 8, 0, 0, 6, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 9},
                {0, 0, 0, 0, 6, 0, 0, 5, 0},
                {0, 0, 0, 1, 7, 8, 4, 0, 0},
                {0, 5, 0, 0, 4, 0, 0, 7, 0},
                {4, 0, 3, 0, 0, 0, 0, 0, 0}};

        return veryHardSudokuProblem;
    }

    public static int[][] getNoSolutionSudokuProblem(){
        int[][] noSolutionSudokuProblem = {
                {0, 0, 2, 0, 0, 8, 0, 0, 9},
                {0, 6, 0, 2, 4, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 1, 9, 7, 3},
                {0, 0, 0, 0, 4, 0, 0, 0, 0},
                {1, 5, 0, 3, 8, 2, 0, 0, 0},
                {0, 8, 4, 9, 0, 6, 0, 5, 0},
                {0, 0, 1, 0, 2, 5, 9, 0, 6},
                {0, 7, 0, 8, 0, 4, 0, 3, 0},
                {0, 0, 0, 0, 0, 0, 7, 0, 0}};

        return noSolutionSudokuProblem;
    }

    public static int[][] getEmptySudokuProblem() {
        int[][] emptySudokuProblem = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}};

        return emptySudokuProblem;
    }


}