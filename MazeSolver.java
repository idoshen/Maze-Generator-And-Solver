import java.util.*;
import java.awt.Color;

public class MazeSolver {

    private static int MAZE_SIZE; // size of maze
    public static LinkedList<Integer> path = new LinkedList<>();
    private static int [] mark;

    /**
     * Constructs a MazeSolver object with the specified maze size.
     * This constructor initializes a MazeSolver instance with the provided maze size.
     * It also initializes the 'mark' array used for marking visited cells during solving.
     *
     * @param size The size of the maze (number of rows = size ; number of columns = size).
     */
    public MazeSolver(int size){
        MazeSolver.MAZE_SIZE = size;
        mark = new int[MAZE_SIZE*MAZE_SIZE];
    }

    /**
     * Resets the MazeSolver to its initial state.
     * This method resets the internal state of the MazeSolver, clearing any previous
     * markings or data to prepare for solving a new maze.
     */
    public void reset(){
        path = new LinkedList<>();
        mark = new int[MAZE_SIZE*MAZE_SIZE];
    }

    /**
     * Solves the maze using Depth-First Search (DFS) algorithm.
     * This method uses the Depth-First Search algorithm to find a path
     * from the specified source cell to the specified target cell in the maze.
     *
     * @param source The index of the source cell.
     * @param target The index of the target cell.
     * @throws InterruptedException If the solving process is interrupted.
     */
    public static void solveDFS(int source, int target) throws InterruptedException { //using stack
        Stack<Integer> stack = new Stack<>();
        stack.push(source);
    
        while (!stack.isEmpty()) {
            int currentVertex = stack.peek();
            mark[currentVertex] = 1;
            boolean foundNeighbor = false;

            for (int neighbor : MazeGenerator.Gmaze.mainArr[currentVertex].neighbors) {

                if (mark[neighbor] == 1) {
                    continue;
                }
                if (neighbor == target) {
                    return;
                } else if (mark[neighbor] == 0) {
                    stack.push(neighbor);
                    path.add(neighbor);
                    foundNeighbor = true;
                    // MazePrinter.paint(currentVertex, neighbor, Color.BLUE, true);
                    break;
                }
            }
    
            if (!foundNeighbor) {
                stack.pop();
                if (path.getLast() != target) {
                    path.removeLast();
                    // int currentPoped = path.removeLast();
                    // MazePrinter.paint(path.getLast(), currentPoped, Color.WHITE, true);
                }
            }
        }
    }

    /**
     * Solves the maze using Breadth-First Search (BFS) algorithm.
     * This method uses the Breadth-First Search algorithm to find a path
     * from the specified source cell to the specified target cell in the maze.
     *
     * @param source The index of the source cell.
     * @param target The index of the target cell.
     * @throws InterruptedException If the solving process is interrupted.
     */
    public static void solveBFS(int source, int target) throws InterruptedException {
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(source);
        mark[source] = 1;

        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            if (current == target) {
                reconstructPath(source, target);
                return;
            }

            for (int neighbor : MazeGenerator.Gmaze.mainArr[current].neighbors) {
                if (mark[neighbor] == 0) {
                    if (neighbor != target){
                        // MazePrinter.paint(current, neighbor, Color.green, true);

                    }
                    queue.add(neighbor);
                    mark[neighbor] = 1;
                    MazeGenerator.Gmaze.mainArr[neighbor].parent = current;  // Set parent for backtracking
                }
            }
        }
    }

    /**
     * Reconstructs the path from the target cell to the source cell.
     * This method reconstructs the path from the specified target cell to the specified source cell
     * using the parent information stored in the maze graph.
     *
     * @param source The index of the source cell.
     * @param target The index of the target cell.
     * @throws InterruptedException If the process is interrupted.
     */
    private static void reconstructPath(int source, int target) throws InterruptedException {
        int current = MazeGenerator.Gmaze.mainArr[target].parent;
        while (current != source) {
            path.addFirst(current);
            current = MazeGenerator.Gmaze.mainArr[current].parent;
        }
    }

    /**
     * Retrieves the evaluated path generated during maze solving.
     * This method returns the list of cell indices that represent the path
     * generated during maze solving using the solver algorithms.
     *
     * @return A linked list of cell indices representing the evaluated path.
     */
    public LinkedList<Integer> eval(){
        return path;
    }
}

