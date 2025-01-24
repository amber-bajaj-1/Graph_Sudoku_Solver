import java.util.*;

public class OptimizedSolver extends Solver{
    /*
    This solver implements a most-constrained-value heuristic via a priority queue, first searching nodes with the least
    number of possible values. Then, a least-constraining-value heuristic is implemented: when guessing between possible
    values for a given node, the algorithm prioritizes values which have the "least impact" on a given node's neighbours
     */
    public OptimizedSolver(Graph sudokuGraph) {
        super(sudokuGraph);
    }

    @Override
    public boolean solve() {
        //adds all unassigned nodes to a priority queue
        PriorityQueue<Node> nextNodesPQ = new PriorityQueue<>();
        for (Node node : sudokuGraph.getAllNodes()) {
            if (node.getValue() == 0) {
                nextNodesPQ.add(node);
            }
        }

        return solveHelper(nextNodesPQ);
    }

    //recursive helper function to solve the Sudoku puzzle
    private boolean solveHelper(PriorityQueue<Node> pq) {
        if (pq.isEmpty()) {
            return true; //all nodes have been correctly assigned without any conflicts
        }

        Node currentNode = pq.poll(); //gets the node with the least number of possible values
        Set<Integer> possibleValues = currentNode.getPossibleVals();

        if (possibleValues.isEmpty()) {
            return false; //a cell with no possible values means there is a conflict
        }

        //sorts the possible values in order of those that are the least constraining
        List<Integer> sortedValues = sortByLeastConstrainingValue(currentNode, possibleValues);

        //assigns a value to the given node which has the "least impact" on its neighbours
        for (int value : sortedValues) {
            currentNode.changeValue(value);

            //updates the priority queue
            updateNeighbours(currentNode, pq);

            //recursive step
            if (solveHelper(pq)) {
                return true; //puzzle can be recursively solved
            }

            //unassigns a value and re-updates neighbours if there is a conflict
            currentNode.changeValue(0);
            updateNeighbours(currentNode, pq);
        }

        return false; //no possible values for the given node: triggers backtracking
    }

    //---- helper methods ----
    private void updateNeighbours(Node node, PriorityQueue<Node> pq) {
        for (Node neighbor : node.getAllNeighbours()) {
            if (neighbor.getValue() == 0) { //only updates neighbours whose values have not yet been determined
                //re-inserts the neighbour to recalculate priority
                pq.remove(neighbor);
                pq.add(neighbor);
            }
        }
    }

    //counts the number of possible values removed from neighbours if a value is assigned — a measure of the "impact" on a Node's neighbours
    private int countConstraints(Node node, int value) {
        int constraints = 0;

        for (Node neighbor : node.getAllNeighbours()) {
            if (neighbor.getValue() == 0) { //only considers non-assigned neighbours
                Set<Integer> neighborPossibleValues = neighbor.getPossibleVals();
                if (neighborPossibleValues.contains(value)) {
                    constraints++; //the number of constraints a value introduces increases if its neighbours can possibly take on that value
                }
            }
        }

        return constraints;
    }

    private List<Integer> sortByLeastConstrainingValue(Node node, Set<Integer> possibleValues) {
        List<Integer> sortedValues = new ArrayList<>(possibleValues);

        sortedValues.sort((val1, val2) -> {
            int constraintsVal1 = countConstraints(node, val1);
            int constraintsVal2 = countConstraints(node, val2);
            return Integer.compare(constraintsVal1, constraintsVal2); //sorts the least constraining node first
        });

        return sortedValues;
    }


}
