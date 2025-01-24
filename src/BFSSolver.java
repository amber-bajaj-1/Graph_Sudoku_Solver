import java.util.*;

public class BFSSolver extends Solver{
    /*
    Solves the Sudoku puzzle using breadth-first search
    This naive solver is not optimized for hard Sudoku with many empty cells
    A memory error may occur if the search space is too large.
    */

    public BFSSolver(Graph sudokuGraph) {
        super(sudokuGraph);
    }

    @Override
    public boolean solve() {
        Queue<Graph> queue = new ArrayDeque<>();
        Set<Integer> seen = new HashSet<>(); //tracks visited states (to avoid repetition)

        //stores the initial graph state in the queue
        queue.add(sudokuGraph.copy());
        seen.add(getGraphHash(sudokuGraph));

        while (!queue.isEmpty()) {
            Graph currentGraph = queue.poll(); //expands the current state of the graph

            //finds the next unassigned node
            Optional<Node> unassignedNodeOpt = findUnassignedNode(currentGraph);

            if (unassignedNodeOpt.isEmpty()) {
                //if all nodes are assigned, check if the graph is a solution, and update the sudokuGraph accordingly
                if (currentGraph.isSolution()) {
                    //uses info from currentGraph to update nodes in SudokuGraph
                    Optional<Node> optUnassignedSudokuNode = findUnassignedNode(sudokuGraph);
                    while(optUnassignedSudokuNode.isPresent()) {
                        Node unassignedNode = optUnassignedSudokuNode.get();
                        Position sudokuNodePosn = unassignedNode.getPosn();
                        int newVal = currentGraph.getNode(sudokuNodePosn).get().getValue();
                        unassignedNode.changeValue(newVal);
                        optUnassignedSudokuNode = findUnassignedNode(sudokuGraph);
                    }
                    return true;
                }
            }
            else {
                //generates next level of graph states
                Node currentNode = unassignedNodeOpt.get();
                for (int newVal = 1; newVal <= 9; newVal++) {
                    if (currentNode.isValidAssignment(newVal)) {
                        //creates a new graph state with a valid value from 1 to 9 assigned to an unassigned node
                        Graph newGraph = currentGraph.copy();
                        Position nodePosn = currentNode.getPosn();
                        Node newNode = newGraph.getNode(nodePosn).get();
                        newNode.changeValue(newVal);

                        //adds the new graph state to the queue if it has not already been enqueued
                        int graphHash = getGraphHash(newGraph);
                        if (!seen.contains(graphHash)) {
                            queue.add(newGraph);
                            seen.add(graphHash);
                        }
                    }
                }
            }
        }
        return false;
    }

    //generates an integer representation of each graph, to avoid repetitions in the search space
    private int getGraphHash(Graph graph) {
        int hash = 0;
        int multiplier = 1;

        for (Node node : graph.getAllNodes()) {
            hash += node.getValue() * multiplier;
            multiplier *= 10;
        }
        return hash;
    }
}
