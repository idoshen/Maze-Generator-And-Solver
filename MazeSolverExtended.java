import java.util.*;
// import java.util.concurrent.TimeUnit;
import java.awt.Color;

public class MazeSolverExtended {

    
    
    private static int MAZE_SIZE; // size of maze
    public static LinkedList<Integer> path = new LinkedList<>();
    private static int [] mark;
    // private static boolean isFound = false;
    
    public MazeSolverExtended(int size){
        MazeSolverExtended.MAZE_SIZE = size;
        // mark = new int[MAZE_SIZE*MAZE_SIZE];
        mark = new int[MAZE_SIZE];
    }

    public void reset(){
        path = new LinkedList<>();
        // mark = new int[MAZE_SIZE*MAZE_SIZE];
        mark = new int[MAZE_SIZE];
        // isFound = false;
    }

    public void solveDFS(int start, int end) throws InterruptedException {
        DFS(start,end);   
    }

    public void solveBFS(int start, int end) throws InterruptedException {
        BFS(start, end);
    }


    public static void DFS(int source, int target) throws InterruptedException { //using stack
        Stack<Integer> stack = new Stack<>();
        stack.push(source);
    
        while (!stack.isEmpty()) {
            int currentVertex = stack.peek();
    
            mark[currentVertex] = 1;
    
            boolean foundNeighbor = false;
            for (int neighbor : MazeGeneratorExtended.Gmaze.mainArr[currentVertex].neighbors) {
                // System.out.println(neighbor);
                if (mark[neighbor] == 1) {
                    continue;
                }
                if (neighbor == target) {
                    // isFound = true;
                    return;
                } else if (mark[neighbor] == 0) {
                    stack.push(neighbor);
                    path.add(neighbor);
                    foundNeighbor = true;
                    // paint(currentVertex, neighbor, Color.BLUE);
                    break;
                }
            }
    
            if (!foundNeighbor) {
                stack.pop();
                if (path.getLast() != target) {
                    // int currentPoped = 
                    path.removeLast();
                    // paint(path.getLast(), currentPoped, Color.WHITE);
                    // path.removeLast();
                }
            }
        }
    }

    public static void BFS(int source, int target) throws InterruptedException {
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(source);
        mark[source] = 1;

        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            if (current == target) {
                reconstructPath(source, target);
                // isFound = true;
                return;
            }
            for (int neighbor : MazeGeneratorExtended.Gmaze.mainArr[current].neighbors) {
                if (mark[neighbor] == 0) {
                    if (neighbor != target){
                        // paint(current, neighbor, Color.green);
                    }
                    queue.add(neighbor);
                    mark[neighbor] = 1;
                    MazeGeneratorExtended.Gmaze.mainArr[neighbor].parent = current;  // Set parent for backtracking
                }
            }
        }
    }

    private static void reconstructPath(int source, int target) throws InterruptedException {
        int current = MazeGeneratorExtended.Gmaze.mainArr[target].parent;
        while (current != source) {
            path.addFirst(current);
            current = MazeGeneratorExtended.Gmaze.mainArr[current].parent;
        }
    }


    public static void paint(int previous, int index, Color color) throws InterruptedException{
        StdDraw.setPenRadius(1.0/MAZE_SIZE);
        StdDraw.setPenColor(color);
        //StdDraw.filledCircle(index % MAZE_SIZE + 0.5, index / MAZE_SIZE + 0.5, 0.3);
        // StdDraw.filledSquare(index % MAZE_SIZE + 0.5, index / MAZE_SIZE + 0.5, R);
        StdDraw.line(index % MazeGeneratorExtended.MAZE_COLUMNS + 0.5, index / MazeGeneratorExtended.MAZE_COLUMNS + 0.5, previous % MazeGeneratorExtended.MAZE_COLUMNS + 0.5, previous / MazeGeneratorExtended.MAZE_COLUMNS + 0.5);
        StdDraw.setPenRadius();
    }

    public LinkedList<Integer> eval(){
        // System.out.println(path);
        return path;
    }
}

