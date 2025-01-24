Creates custom graph-theoretic classes to impose a graph structure on standard 9x9 Sudokus, transforming Sudoku-solving into a node-colouring problem (where numerical values assigned to each node represent different colours)

Initially, naive depth-first and breadth-first solving algorithms were implemented, as a basis to test optimized-solvers (and to practice the techniques I studied in my introductory computer science course).
Then, various methods were explored to create a more optimized solver. The current version implements: (1) a most-constrained-value heuristic, wherein unassigned nodes with the least number of possible values are assigned first; and (2) a least-constraining-value heuristic — when a guess needs to be made, values that have the lowest-impact on unassigned neighbours are chosen first.  

The Main contains a method to empirically test the runtime of various solving algorithms on 9x9 Sudokus with 25 or 30 clues, retrieved from https://printable-sudoku-puzzles.com/wfiles/. 
Once optimization (1) was implemented, the algorithm took an average of 2.35712 seconds to solve 1000 25-clue Sudokus. The current algorithm with both optimizations (1) and (2) takes an average of 2.2841 seconds to run on 1000 25-clue Sudokus, which is about 0.00228 seconds per Sudoku. 

Future updates may explore visualizations of Sudoku graphs, or implement further optimizations through methods like arc consistency or forward-checking. 
