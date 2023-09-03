import java.util.LinkedList;
import java.util.List;
import java.awt.Color;

/**
 * A class responsible for printing visual representations of the maze and solutions.
 * This class provides methods to print visual representations of the maze and solutions
 * using the provided openings, removed walls, and maze size.
 */
public class MazePrinter {
    private static List<List<Integer>> E; // Linked list of openings
    private static List<List<Integer>> RandomlyRemovedWalls; // Linked list of the randomly removed walls
    private static int MAZE_SIZE;
    public static int start;
    public static int end;

    /**
     * Initializes the MazePrinter with provided data.
     *
     * @param size The size of the maze.
     * @param openings The list of openings in the maze.
     * @param removedWalls The list of randomly removed walls.
     */
    public static void init(int size, List<List<Integer>> openings, List<List<Integer>> removedWalls){
        MAZE_SIZE = size;
        E = openings;
        RandomlyRemovedWalls = removedWalls;
    }

    /**
     * Prints the maze using StdDraw.
     * This function displays a visual representation of the maze, showing cells and walls.
     * It can be used for debugging and visualization purposes.
     */
    public static void printMaze(int canvasWidth, int canvasHeight) {
        StdDraw.setCanvasSize(canvasWidth,canvasHeight);
		StdDraw.setXscale(0, MAZE_SIZE);
		StdDraw.setYscale(MAZE_SIZE, 0);
        StdDraw.line(0, 0, 0, MAZE_SIZE);
        StdDraw.line(0, 0, MAZE_SIZE, 0);
        StdDraw.line(0, MAZE_SIZE, MAZE_SIZE, MAZE_SIZE);
        StdDraw.line(MAZE_SIZE, 0, MAZE_SIZE, MAZE_SIZE);
        
        int completedSteps = 0;
        long startTime = System.currentTimeMillis();

        for(List<Integer> wall : E){
            int u = wall.get(0);
            int v = wall.get(1);

            if (v - u == 1){
                StdDraw.line(v%MAZE_SIZE, u/MAZE_SIZE, v%MAZE_SIZE, u/MAZE_SIZE + 1);
            }else{
                StdDraw.line(u%MAZE_SIZE, v/MAZE_SIZE, u%MAZE_SIZE + 1, v/MAZE_SIZE);
            }

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            double progress = (double) completedSteps / E.size();
            int percentage = (int) (progress * 100);
            long estimatedTimeRemaining = 0;

            if (completedSteps > 0){
                estimatedTimeRemaining = (long) ((elapsedTime / completedSteps) * (E.size() - completedSteps));
            }
            
            System.out.print("Printing walls: " + percentage + "% it will take " +  formatTime(estimatedTimeRemaining) + "\r");
            completedSteps++;
        }

        System.out.println("Printing walls: 100% it will take 00:00:00");
        completedSteps = 0;
        startTime = System.currentTimeMillis();

        for(List<Integer> wall : RandomlyRemovedWalls){
            StdDraw.setPenColor(Color.GREEN);
            int u = wall.get(0);
            int v = wall.get(1);

            if (v - u == 1){
                StdDraw.line(v%MAZE_SIZE, u/MAZE_SIZE, v%MAZE_SIZE, u/MAZE_SIZE + 1);
            }else{
                StdDraw.line(u%MAZE_SIZE, v/MAZE_SIZE, u%MAZE_SIZE + 1, v/MAZE_SIZE);
            }
        
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            double progress = (double) completedSteps / RandomlyRemovedWalls.size();
            int percentage = (int) (progress * 100);
            long estimatedTimeRemaining = 0;

            if (completedSteps > 0){
                estimatedTimeRemaining = (long) ((elapsedTime / completedSteps) * (RandomlyRemovedWalls.size() - completedSteps));
            }

            System.out.print("Printing randomly removed walls: " + percentage + "% it will take " +  formatTime(estimatedTimeRemaining) + "\r");
            completedSteps++;
        }

        System.out.println("Printing randomly removed walls: 100% it will take 00:00:00");
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.show();
    }

    /**
     * Formats a given time duration in milliseconds into a human-readable string.
     * This function converts a time duration from milliseconds to a formatted string
     * that represents the duration in hours, minutes, seconds, and milliseconds.
     *
     * @param milliseconds The time duration in milliseconds to be formatted.
     * @return A formatted string representing the time duration in hours, minutes, seconds, and milliseconds.
     */
    public static String formatTime(long milliseconds) {
        long seconds = (milliseconds + 1000) / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds %= 60;
        minutes %= 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Prints a solution path using StdDraw.
     * This function displays a visual representation of the solution path on the maze.
     * It can be used for debugging, visualization, and educational purposes.
     *
     * @param sol The solution path to print, represented as a linked list of cell indices.
     * @param color The desired color of the printed solution path.
     * @throws InterruptedException If the printing process is interrupted.
     */
    public static void printSolution(LinkedList<Integer> sol, Color color) throws InterruptedException {
        Integer previous;
        previous = sol.getFirst();

        paint(start, previous, color, false);
        for (Integer index : sol){
            paint(index, previous, color, false);
            previous = index;
            // TimeUnit.MILLISECONDS.sleep(50);
        }
        paint(end, previous, color, false);
        
        System.out.println("Solution's length: " + (sol.size() + 1) + "\n");
        StdDraw.show();
    }

    /**
     * Paints a path between two cells on the maze using StdDraw.
     * This function visually represents a path between the specified previous cell and the current cell
     * using the specified color. It can be used for visualization and debugging purposes.
     *
     * @param previous The index of the previous cell.
     * @param index The index of the current cell.
     * @param color The color used for painting the path.
     * @throws InterruptedException If the painting process is interrupted.
     */
    public static void paint(int previous, int index, Color color, boolean toShow) throws InterruptedException{
        StdDraw.setPenRadius(Math.max(0.005, 0.5/MAZE_SIZE));
        StdDraw.setPenColor(color);
        StdDraw.line(index % MAZE_SIZE + 0.5, index / MAZE_SIZE + 0.5, previous % MAZE_SIZE + 0.5, previous / MAZE_SIZE + 0.5);
        StdDraw.setPenRadius();

        if (toShow){
            StdDraw.show();
        }
    }
}
