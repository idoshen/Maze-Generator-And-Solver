import java.util.*;
import java.io.IOException;
// import java.util.concurrent.TimeUnit;

   

public class MazeGenerator {
    
    public static int MAZE_SIZE; // Size of maze
    public static List<List<Integer>> E = new LinkedList<>(); // Linked list of openings
    public static List<List<Integer>> FullWalls = new LinkedList<>(); // Linked list of the walls of the maze
    public static List<List<Integer>> RandomlyRemovedWalls = new LinkedList<>(); // Linked list of the randomly removed walls
    public static UnionFind unionFind; // Union-find data structure for maze generation
    public static int start = -1; // Index of the start cell
    public static int end = -1; // Index of the end cell
    public static Graph Gmaze; // Graph representation of the maze

    /**
     * Generates a maze using Kruskal's algorithm.
     * Kruskal's algorithm creates a maze by randomly connecting cells together
     * while avoiding cycles to ensure a unique path between any two cells.
     */
    public static void generateMaze() {
        unionFind = new UnionFind(MAZE_SIZE*MAZE_SIZE);

        for (int i = 0; i < MAZE_SIZE; i++){
            for (int j = 0; j < MAZE_SIZE; j++){
                List<Integer> newList = new LinkedList<>();
                if (!(j == MAZE_SIZE - 1)){
                    newList.add(i*MAZE_SIZE + j);
                    newList.add(i*MAZE_SIZE + j + 1);
                    E.add(newList);
                }
                if (!(i == MAZE_SIZE - 1)){
                    newList = new LinkedList<>();
                    newList.add(i*MAZE_SIZE + j);
                    newList.add((i + 1)*MAZE_SIZE + j);
                    E.add(newList);
                }
            }
        }

        FullWalls.addAll(E);
        Collections.shuffle(E);
        ListIterator<List<Integer>> itr = E.listIterator();
        List<Integer> newList = itr.next();

        while (unionFind.numSets > 1){
            int u = unionFind.find(newList.get(0));
            int v = unionFind.find(newList.get(1));
            if (u != v){
                unionFind.union(u, v);
                itr.remove();
            }else{
                newList = itr.next();
            }
        }
    }

    /**
     * Constructs a graph representation of the maze using the given list of walls.
     * This function prepares the graph for further processing, such as applying DFS/BFS.
     */
    public static void constructGraph() {
        List<List<Integer>> mazeCopy = new ArrayList<>(FullWalls);
        Gmaze = new Graph(mazeCopy, MAZE_SIZE * MAZE_SIZE);
    }

    /**
     * Removes walls from the maze based on a predefined set of openings.
     * This function updates the list of full walls by removing walls that correspond
     * to openings in the maze. The openings are defined by the set E.
     */
    public static void removeWalls(){
        Set<List<Integer>> fullWallsSet = new HashSet<>(FullWalls);
        Set<List<Integer>> eSet = new HashSet<>(E);
        fullWallsSet.removeAll(eSet);
        FullWalls = new LinkedList<>(fullWallsSet);
    }

    /**
     * Removes a specified number of random walls from the maze.
     * This function randomly selects walls from the predefined set of openings (E),
     * removes them from the openings set, and adds them to the list of full walls (FullWalls).
     *
     * @param numberOfWallsToRemove The number of random walls to remove from the maze.
     */
    public static void removeRandomWalls(int numberOfWallsToRemove){
        Collections.shuffle(E);
        System.out.print("Removing walls...");

        for (int i = 0; i < numberOfWallsToRemove; i++){
            if(!E.isEmpty()){
                List<Integer> wall = E.get(0);
                E.remove(0);
                FullWalls.add(wall);
                RandomlyRemovedWalls.add(wall);
            }
            else{
                break;
            }
        }

        System.out.println("Done!\n");
    }

    /**
     * Adds back the randomly removed walls to the maze.
     * This function restores the walls that were previously removed using the
     * removeRandomWalls function. It adds the walls back to the openings set (E)
     * and updates the list of full walls (FullWalls).
     */
    public static void addBackRandomWalls(){
        E.addAll(0, RandomlyRemovedWalls);
        Set<List<Integer>> fullWallsSet = new HashSet<>(FullWalls);
        Set<List<Integer>> eSet = new HashSet<>(RandomlyRemovedWalls);
        fullWallsSet.removeAll(eSet);
        FullWalls = new LinkedList<>(fullWallsSet);
    }


    /**
     * Restarts the maze generation process by resetting various data structures and variables.
     * This function clears the visual display, initializes the lists of openings (E), full walls (FullWalls),
     * and randomly removed walls (RandomlyRemovedWalls), and resets the start and end cell indices.
     */
    public static void restart(){
        StdDraw.clear();
        E = new LinkedList<>();
        FullWalls = new LinkedList<>();
        RandomlyRemovedWalls = new LinkedList<>();
        start = -1;
        end = -1;
    }

    /**
     * Pauses program execution until the Enter key is pressed.
     * This function waits for the user to press the Enter key before continuing.
     * It can be used to control the flow of the program during debugging or visualization.
     */
    public static void enterPress(){
        try {
            System.in.read();
            System.out.println();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

