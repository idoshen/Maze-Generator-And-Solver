import java.util.*;
// import java.util.concurrent.TimeUnit;
import java.awt.Color;

public class MazeSolver {

    
    
    private static int MAZE_SIZE; // size of maze
    public static LinkedList<Integer> path = new LinkedList<>();
    private static int [] mark;
    // private static boolean isFound = false;
    
    public MazeSolver(int size){
        MazeSolver.MAZE_SIZE = size;
        mark = new int[MAZE_SIZE*MAZE_SIZE];
    }

    public void reset(){
        path = new LinkedList<>();
        mark = new int[MAZE_SIZE*MAZE_SIZE];
        // isFound = false;
    }

    public void solveDFS(int start, int end) throws InterruptedException {
        DFS(start,end);   
    }

    public void solveBFS(int start, int end) throws InterruptedException {
        BFS(start, end);
    }

    // public static void DFS(int source, int target) throws InterruptedException{ //using recursive DFS
    //     if (mark[source] == 1 && mark[source] != target){
    //         path.removeLast();
    //         return;
    //     }

    //     mark[source] = 1;
    //     // if (MazeGenerator.Gmaze.mainArr[source].neighbors.isEmpty()) return;
    //     // Collections.sort(MazeGenerator.Gmaze.mainArr[source].neighbors);
    //     // System.out.println(source + " -> " + MazeGenerator.Gmaze.mainArr[source].neighbors.toString());
    //     for (int neighbor : MazeGenerator.Gmaze.mainArr[source].neighbors){
    //         if (mark[neighbor] == 1){
    //             continue;
    //         }
    //         if(neighbor == target){
    //             path.add(neighbor);
    //             isFound = true;
    //             return;
    //         }
    //         else if (mark[neighbor] == 0) {
    //             path.add(neighbor);
    //             // paint(neighbor, Color.BLUE,0.25);
    //             DFS(neighbor,target);
    //             if (isFound){
    //                 break;
    //             }
    //         }
    //     }

    //     if (path.getLast() != target){
    //         // paint(path.removeLast(), Color.WHITE,0.3);
    //         path.removeLast();
    //     } 
        
    // }

    public static void DFS(int source, int target) throws InterruptedException { //using stack
        Stack<Integer> stack = new Stack<>();
        stack.push(source);
    
        while (!stack.isEmpty()) {
            int currentVertex = stack.peek();
    
            mark[currentVertex] = 1;
    
            boolean foundNeighbor = false;
            for (int neighbor : MazeGenerator.Gmaze.mainArr[currentVertex].neighbors) {
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
            for (int neighbor : MazeGenerator.Gmaze.mainArr[current].neighbors) {
                if (mark[neighbor] == 0) {
                    if (neighbor != target){
                        // paint(current, neighbor, Color.green);
                    }
                    queue.add(neighbor);
                    mark[neighbor] = 1;
                    MazeGenerator.Gmaze.mainArr[neighbor].parent = current;  // Set parent for backtracking
                }
            }
        }
    }

    private static void reconstructPath(int source, int target) throws InterruptedException {
        int current = MazeGenerator.Gmaze.mainArr[target].parent;
        while (current != source) {
            path.addFirst(current);
            current = MazeGenerator.Gmaze.mainArr[current].parent;
        }
    }


    public static void paint(int previous, int index, Color color) throws InterruptedException{
        StdDraw.setPenRadius(1.0/MAZE_SIZE);
        StdDraw.setPenColor(color);
        //StdDraw.filledCircle(index % MAZE_SIZE + 0.5, index / MAZE_SIZE + 0.5, 0.3);
        // StdDraw.filledSquare(index % MAZE_SIZE + 0.5, index / MAZE_SIZE + 0.5, R);
        StdDraw.line(index % MAZE_SIZE + 0.5, index / MAZE_SIZE + 0.5, previous % MAZE_SIZE + 0.5, previous / MAZE_SIZE + 0.5);
        StdDraw.setPenRadius();

    }

    public LinkedList<Integer> eval(){
        // System.out.println(path);
        return path;
    }
}

