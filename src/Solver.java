import java.util.*;

public abstract class Solver {
    protected Graph sudokuGraph;

    //constructor
    public Solver(Graph graph) {
        this.sudokuGraph = graph;
    }

    public abstract boolean solve();

    //---- shared methods used in various solvers ----
    //finds an unassigned node (whose value is 0)
    protected Optional<Node> findUnassignedNode(Graph graph) {
        Set<Node> allNodes = graph.getAllNodes();
        for (Node node : allNodes){
            if(node.getValue() == 0){
                return Optional.of(node);
            }
        }
        return Optional.empty();
    }
}
