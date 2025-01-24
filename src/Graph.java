import java.util.*;

public class Graph {
    //attributes
    private int numVertices;
    private int numEdges;
    private Set<Node> allNodes = new HashSet<>();

    //constructors
    public Graph(){} //blank constructor to create an empty graph

    public Graph(int[][] sudokuArr) { //constructs a graph from a 2D array of integers
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int nodeVal = sudokuArr[row][col];
                Position nodePosn = new Position(row, col);
                Node newNode = new Node(nodeVal, nodePosn);
                if (nodeVal == 0){
                    newNode.fix();
                }
                addNode(newNode);
            }
        }
    }

    //methods
    public void addNode(Node otherNode){
        allNodes.add(otherNode);
        numVertices++;
    }

    public void addEdge(Node node1, Node node2){
        node1.addNeighbour(node2);
        node2.addNeighbour(node1);
        numEdges++;
    }

    public boolean isFilled(){
        for(Node node : this.getAllNodes()){
            if(node.getValue() == 0){
                return false;
            }
        }
        return true;
    }

    //getters for single or multiple nodes in the graph
    public Optional<Node> getNode(Position posn){
        for (Node node : allNodes){ //
            if (node.getPosn().isEqual(posn)){
                return Optional.of(node);
            }
        }
        return Optional.empty();
    }

    public Set<Node> getAllNodes(){
        return allNodes;
    }

    //getters for the size of the graph
    public int getNumVertices() {
        return numVertices;
    }

    public int getNumEdges() {
        return numEdges;
    }

    //creates a copy of a graph (including copies of all individual nodes)
    public Graph copy() {
        Graph newGraph = new Graph();
        Map<Node, Node> nodeMap = new HashMap<>(); //maps old nodes to copied nodes

        //copies all old nodes to newGraph
        for (Node node : this.getAllNodes()) {
            Node newNode = new Node(node.getValue(), node.getPosn());
            if (node.isFixed()) {
                newNode.fix();
            }
            newGraph.addNode(newNode);
            nodeMap.put(node, newNode);
        }

        //copies the neighbours of all nodes to the new nodes
        for (Node node : this.getAllNodes()) {
            Node newNode = nodeMap.get(node);
            for (Node neighbour : node.getAllNeighbours()) {
                Node newNeighbour = nodeMap.get(neighbour);
                newGraph.addEdge(newNode, newNeighbour);
            }
        }

        return newGraph;
    }

    //displays a 9x9 Sudoku Graph in the shell
    public void printSudokuGraph(){
        int[][] arrayGraph = new int[9][9];
        for (Node node : allNodes){
            int row = node.getPosn().getRow();
            int col = node.getPosn().getCol();
            int val = node.getValue();
            arrayGraph[row][col] = val;
        }

        //prints out the 2D array in a Sudoku-like format
        System.out.println();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (arrayGraph[row][col] == 0){ //prints out an "X" to represent unassigned nodes
                    System.out.print("X ");
                }
                else{
                    System.out.print(arrayGraph[row][col] + " ");
                }
                if ((col + 1) % 3 == 0 && col != 8) {
                    System.out.print("| "); //adds a vertical divider to represent a box
                }
            }
            System.out.println();
            if ((row + 1) % 3 == 0 && row != 8) {
                System.out.println("---------------------"); //adds a horizontal divider to represent a box
            }
        }

        System.out.println();
    }

    //checks whether the graph represents a valid Sudoku solution based on 2 conditions
    public boolean isSolution(){
        Set<String> seen = new HashSet<>();

        for (Node node : getAllNodes()) {
            int value = node.getValue();
            int row = node.getPosn().getRow();
            int col = node.getPosn().getCol();
            int boxIndex = node.getBoxNumber();

            //condition 1: nodes only have valid integer values 1 through 9
            if(value > 9 || value < 1){
                return false;
            }

            //creates a key for a number in each row, column, and box
            String rowKey = value + " in row " + row;
            String colKey = value + " in col " + col;
            String boxKey = value + " in box " + boxIndex;

            //condition 2: no duplicates can be found in any row, column, or box
            if (!seen.add(rowKey) || !seen.add(colKey) || !seen.add(boxKey)) {
                return false;
            }
        }
        return true;
    }

    //helper method to build Sudoku edges in a graph
    private void addEdgesWithinGroup(Set<Node> group) {
        List<Node> nodes = new ArrayList<>(group); //converts nodes into a list for indexing

        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                Node node1 = nodes.get(i);
                Node node2 = nodes.get(j);
                addEdge(node1, node2); //adds edge between the two nodes
            }
        }
    }

    //builds edges for Sudoku graph between nodes in the same column, row, or box
    public void buildSudokuEdges() {
        //creates sets to represent all rows, columns, and boxes
        Set<Node>[] rows = new HashSet[9];
        Set<Node>[] columns = new HashSet[9];
        Set<Node>[] boxes = new HashSet[9];

        //initializes the sets for each group
        for (int i = 0; i < 9; i++) {
            rows[i] = new HashSet<>();
            columns[i] = new HashSet<>();
            boxes[i] = new HashSet<>();
        }

        //adds nodes to their row, column, and box groups
        for (Node node : allNodes) {
            int row = node.getPosn().getRow();
            int col = node.getPosn().getCol();
            int box = node.getBoxNumber();

            rows[row].add(node);
            columns[col].add(node);
            boxes[box].add(node);
        }

        //creates an edge between every pair of nodes within each group
        for (int i = 0; i < 9; i++) {
            addEdgesWithinGroup(rows[i]);
            addEdgesWithinGroup(columns[i]);
            addEdgesWithinGroup(boxes[i]);
        }
    }

}
