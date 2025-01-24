import java.util.*;

public class Node implements Comparable<Node> {
    //attributes
    private int value = 0; //the value (represented here as a number) can be imagined as a colour assignment
    private Position posn;
    private boolean isFixed = false;
    private Set<Node> neighbours = new HashSet<>();

    //constructor
    public Node(int val, Position posn){
        this.value = val;
        this.posn = posn;
    }

    //getters
    public Set<Node> getAllNeighbours(){
        return this.neighbours;
    }

    public Set<Integer> getNeighbourVals(){
        Set<Integer> neighbourVals = new HashSet<>();
        for(Node n : this.neighbours){
            neighbourVals.add(n.value);
        }
        return neighbourVals;
    }

    public Set<Integer> getPossibleVals(){
        Set<Integer> allValues = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        allValues.removeAll(getNeighbourVals());
        return allValues;
    }

    public boolean isFixed(){
        return this.isFixed;
    }

    public Position getPosn(){
        return this.posn;
    }

    public int getValue(){
        return this.value;
    }

    //assigns a box number 0 through 8, with 0 in the top left and 8 in the bottom right, counting horizontally first
    public int getBoxNumber() {
        return (this.posn.getRow() / 3) * 3 + (this.posn.getCol() / 3);
    }

    //methods
    public void changeValue(int n){
        this.value = n;
    }

    public void fix(){
        this.isFixed = true;
    }

    public void addNeighbour(Node otherNode){
        this.neighbours.add(otherNode);
    }

    //checks if assigning a given value to a given node will result in any conflicts
    public boolean isValidAssignment(int value) {
        Set<Integer> invalidVals = getNeighbourVals();
        //the current node's value cannot be the same as any of its neighbours
        return !invalidVals.contains(value);
    }

    //comparator: helps sort Nodes from the least to greatest number of possible values
    @Override
    public int compareTo(Node other) {
        int otherNumInvalids = other.getNeighbourVals().size();
        int thisNumInvalids = this.getNeighbourVals().size();

        if(otherNumInvalids > thisNumInvalids){
            return 1;
        }
        else{
            return -1;
        }
    }
}