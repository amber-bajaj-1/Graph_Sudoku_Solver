import java.util.*;

public class DFSSolver extends Solver{
    //solves the Sudoku puzzle using depth-first search
    public DFSSolver(Graph sudokuGraph) {
        super(sudokuGraph);
    }

    @Override
    public boolean solve() {
        //finds the next unassigned node
        Optional<Node> unassignedNodeOpt = findUnassignedNode(sudokuGraph);
        if (unassignedNodeOpt.isEmpty()) {
            return true; //true indicates a solution is found
        }
        Node currentNode = unassignedNodeOpt.get();

        //assigns a valid value from 1 to 9
        for (int guessVal = 1; guessVal <= 9; guessVal++) {
            if (currentNode.isValidAssignment(guessVal)) {
                currentNode.changeValue(guessVal);

                //recursive step:
                if (solve()) {
                    return true; //puzzle is solved (true indicates a solution is found)
                }

                //backtracking: unassigns the value if there is a conflict
                currentNode.changeValue(0);
            }
        }
        return false; //false indicates a solution is not possible
    }
}
