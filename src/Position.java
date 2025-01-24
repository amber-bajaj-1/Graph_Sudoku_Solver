public class Position {
    //attributes
    private int row;
    private int col;

    public Position(int row, int col){
        this.row = row;
        this.col = col;
    }

    //methods
    public boolean isEqual(Position other){
        return (this.row == other.row) && (this.col == other.col);
    }

    //getters of the row and column coordinates
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
