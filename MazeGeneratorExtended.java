import java.awt.Color;
import java.util.*;
// import java.util.concurrent.TimeUnit;
import java.io.IOException;

public class MazeGeneratorExtended {
    
    public static int MAZE_SIZE, MAZE_COLUMNS, MAZE_ROWS; // size of maze
    private static List<List<Integer>> E = new LinkedList<>();
    private static List<List<Integer>> FullWalls = new LinkedList<>();
    private static List<List<Integer>> RandomlyRemovedWalls = new LinkedList<>();
    private static UnionFind unionFind;
    private static int start = -1;
    private static int end = -1;
    public static Graph Gmaze;

    public static void generateMaze() {
        unionFind = new UnionFind(MAZE_SIZE);

        for (int i = 0; i < MAZE_ROWS; i++){
            for (int j = 0; j < MAZE_COLUMNS; j++){
                List<Integer> newList = new LinkedList<>();
                if (!(j == MAZE_COLUMNS - 1)){
                    newList.add(i*MAZE_COLUMNS + j);
                    newList.add(i*MAZE_COLUMNS + j + 1);
                    E.add(newList);
                }
                if (!(i == MAZE_ROWS - 1)){
                    newList = new LinkedList<>();
                    newList.add(i*MAZE_COLUMNS + j);
                    newList.add((i + 1)*MAZE_COLUMNS + j);
                    E.add(newList);
                }
            }
        }
        // System.out.println(E.toString());

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

        // System.out.println(E.toString());
    }

    private static void constructGraph() {
        List<List<Integer>> mazeCopy = new ArrayList<>(FullWalls);
        Gmaze = new Graph(mazeCopy, MAZE_SIZE);
    }

    public static void printMaze() {
        if (MAZE_COLUMNS > MAZE_ROWS){
            double ratio = Math.max(((double) MAZE_ROWS) /(double) (MAZE_COLUMNS + MAZE_ROWS) , 1.0/3.0);
            System.out.println((int)(1000 * (1 - ratio))+ " " +(int)(1000 * (ratio)));
            StdDraw.setCanvasSize((int)(1000 * (1 - ratio)) + 1000,(int)(1000 * (ratio)));
        }else if (MAZE_ROWS > MAZE_COLUMNS){
            double ratio = ((double) MAZE_COLUMNS) /((double)  MAZE_COLUMNS + MAZE_ROWS);
            System.out.println(ratio);
            StdDraw.setCanvasSize((int)(1000 * (ratio)),(int)(1000 * (1 - ratio)) + 1000);
        }else{
            StdDraw.setCanvasSize(1000,1000);
        }
        
		StdDraw.setXscale(0, MAZE_COLUMNS);
		StdDraw.setYscale(MAZE_ROWS, 0);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.filledRectangle( MAZE_COLUMNS / 2,  MAZE_ROWS / 2, MAZE_COLUMNS / 2, MAZE_ROWS / 2);
        StdDraw.setPenColor(Color.BLACK);
        // StdDraw.setPenRadius(0.01);
        StdDraw.line(0, 0, 0, MAZE_ROWS);
        StdDraw.line(0, 0, MAZE_COLUMNS, 0);
        StdDraw.line(0, MAZE_ROWS, MAZE_COLUMNS, MAZE_ROWS);
        StdDraw.line(MAZE_COLUMNS, 0, MAZE_COLUMNS, MAZE_ROWS);
        
        int completedSteps = 0;
        long startTime = System.currentTimeMillis();
        // System.out.print(E.toString());
        for(List<Integer> wall : E){
            
            int u = wall.get(0);
            int v = wall.get(1);

            if (v - u == 1){
                StdDraw.line(v%MAZE_COLUMNS, v/MAZE_COLUMNS, v%MAZE_COLUMNS, v/MAZE_COLUMNS + 1);
            }else{
                StdDraw.line(u%MAZE_COLUMNS, u/MAZE_COLUMNS + 1, u%MAZE_COLUMNS + 1, u/MAZE_COLUMNS + 1);
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
                StdDraw.line(v%MAZE_COLUMNS, u/MAZE_COLUMNS, v%MAZE_COLUMNS, u/MAZE_COLUMNS + 1);
            }else{
                StdDraw.line(u%MAZE_COLUMNS, v/MAZE_COLUMNS, u%MAZE_COLUMNS + 1, v/MAZE_COLUMNS);
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
        StdDraw.save("MazeUnsolved.jpg");
    }

    public static void printSolution(LinkedList<Integer> sol, Color color) throws InterruptedException {
        StdDraw.setPenRadius(0.5/Math.max(MAZE_ROWS, MAZE_COLUMNS));
        StdDraw.setPenColor(color);
        Integer previous;
        previous = sol.getFirst();

        StdDraw.line(start % MAZE_COLUMNS + 0.5, start / MAZE_COLUMNS + 0.5, previous % MAZE_COLUMNS + 0.5, previous / MAZE_COLUMNS + 0.5);
        for (Integer index : sol){
            StdDraw.line(index % MAZE_COLUMNS + 0.5, index / MAZE_COLUMNS + 0.5, previous % MAZE_COLUMNS + 0.5, previous / MAZE_COLUMNS + 0.5);
            previous = index;
            //StdDraw.filledCircle(index % MAZE_SIZE + 0.5, index / MAZE_SIZE + 0.5, 0.3);
            // StdDraw.filledSquare(index % MAZE_SIZE + 0.5, index / MAZE_SIZE + 0.5, 0.3);
            // TimeUnit.MILLISECONDS.sleep(50);
        }
        StdDraw.line(end % MAZE_COLUMNS + 0.5, end / MAZE_COLUMNS + 0.5, sol.getLast() % MAZE_COLUMNS + 0.5, sol.getLast() / MAZE_COLUMNS + 0.5);
        
        System.out.println("Solution's length: " + (sol.size() + 1) + "\n");
        StdDraw.setPenRadius();
    }

    public static String formatTime(long milliseconds) {
        long seconds = (milliseconds + 1000) / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds %= 60;
        minutes %= 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    

    public static void removeWalls(){
        Set<List<Integer>> fullWallsSet = new HashSet<>(FullWalls);
        Set<List<Integer>> eSet = new HashSet<>(E);
        fullWallsSet.removeAll(eSet);
        FullWalls = new LinkedList<>(fullWallsSet);
    }

    public static void removeRandomWalls(int numberOfWallsToRemove){
        Collections.shuffle(E);
        System.out.print("Removing walls...");
        for (int i = 0; i < numberOfWallsToRemove; i++){
            if(!E.isEmpty()){
                // System.out.println(E.get(0).toString());
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

    public static void addBackRandomWalls(){
        E.addAll(0, RandomlyRemovedWalls);
        Set<List<Integer>> fullWallsSet = new HashSet<>(FullWalls);
        Set<List<Integer>> eSet = new HashSet<>(RandomlyRemovedWalls);
        fullWallsSet.removeAll(eSet);
        FullWalls = new LinkedList<>(fullWallsSet);
    }

    public static void restart(){
        StdDraw.clear();
        E = new LinkedList<>();
        FullWalls = new LinkedList<>();
        RandomlyRemovedWalls = new LinkedList<>();
        start = -1;
        end = -1;
    }

    public static void enterPress(){
        try {
            System.in.read();
            System.out.println();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Welcome to the Maze Solver Program!\n");
        Scanner reader = new Scanner(System.in);
        MazeSolverExtended solver;
        boolean continueLoop = true;
        while(continueLoop){

            System.out.print("\u001B[31mPlease enter the desired number of columns (enter a positive integer that is bigger than 1): \u001B[0m");

            do{
                MAZE_COLUMNS = reader.nextInt();
                if (MAZE_COLUMNS <= 1) System.out.print("Invalid input, please try again: ");
            }while(MAZE_COLUMNS <= 1);

            System.out.print("\u001B[31mPlease enter the desired number of rows (enter a positive integer that is bigger than 1): \u001B[0m");

            do{
                MAZE_ROWS = reader.nextInt();
                if (MAZE_ROWS <= 1) System.out.print("Invalid input, please try again: ");
            }while(MAZE_ROWS <= 1);

            MAZE_SIZE = MAZE_COLUMNS * MAZE_ROWS;
            
            System.out.print("Generating maze...");
            generateMaze();
            System.out.println(" Done!\n");
            
            removeWalls();
            // System.out.println(" Done!");
            System.out.println("Current number of walls: " + E.size());
            System.out.print("\u001B[31mEnter the number of random walls to remove: \u001B[0m");
            removeRandomWalls(reader.nextInt());
            
            printMaze();
            System.out.print("Constructing graph...");
            constructGraph();
            // System.out.println(Gmaze.toString());
            System.out.println(" Done!\n");

            
            System.out.print("\u001B[31mInsert start index (0 - " + (MAZE_SIZE - 1) + "): \u001B[0m");
            
            while (start < 0 || start >  MAZE_SIZE - 1){
                try{
                    start = reader.nextInt();
                }catch(InputMismatchException e){
                    reader.next(); // Scans the next token of the input as an int.
                    start = -1;
                }
                if (start < 0 || start >  MAZE_SIZE*MAZE_SIZE - 1){
                    System.out.print("\u001B[31mInvalid input, please try again: \u001B[0m");
                }
            }
            StdDraw.setPenColor(Color.RED);
            StdDraw.filledSquare(start % MAZE_COLUMNS + 0.5, start / MAZE_COLUMNS + 0.5 , 0.5);
            System.out.print("\u001B[31mInsert end index (0 - " + (MAZE_SIZE - 1) + "): \u001B[0m");
            while (end < 0 || end >  MAZE_SIZE - 1){
                try{
                    end = reader.nextInt();
                }catch(InputMismatchException e){
                    reader.next(); // Scans the next token of the input as an int.
                    end = -1;
                }
                if (end < 0 || end >  MAZE_SIZE - 1){
                    System.out.print("\u001B[31mInvalid input, please try again: \u001B[0m");
                }
            }
            System.out.println();
            StdDraw.filledSquare(end % MAZE_COLUMNS + 0.5, end / MAZE_COLUMNS + 0.5 , 0.5);
            solver = new MazeSolverExtended(MAZE_SIZE);

            System.out.println("Solving the maze...\n");
            
            long startTime = System.currentTimeMillis();
            solver.solveDFS(start,end);
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            
            LinkedList<Integer> pathDFS = solver.eval();

            System.out.println("\u001B[32mSolved using DFS in " + elapsedTime  + " milliseconds");
            printSolution(pathDFS, Color.MAGENTA);
            System.out.println("\u001B[31mPress Enter to continue to BFS.\u001B[32m");
            enterPress();


            solver.reset();

            startTime = System.currentTimeMillis();
            solver.solveBFS(start, end);
            endTime = System.currentTimeMillis();
            elapsedTime = endTime - startTime;
            
            LinkedList<Integer> pathBFS = solver.eval();
            System.out.println("Solved using BFS in " + elapsedTime  + " milliseconds");
            printSolution(pathBFS, Color.BLUE);
            System.out.println("\u001B[31mPress Enter to continue to DFS in the original maze.\u001B[32m");
            enterPress();

            //print the single path of the maze if we havent removed walls
            addBackRandomWalls();
            constructGraph();

            solver.reset();
            solver.solveDFS(start, end);
            
            LinkedList<Integer> pathSingle = solver.eval();
            System.out.println("Solved using DFS in the original maze");
            printSolution(pathSingle, Color.RED);
            
            StdDraw.save("Maze.jpg");
            System.out.println("\u001B[31mPress Enter to continue or Esc to exit.\u001B[32m");
            enterPress();
            System.out.println("\u001B[34m--------------------------------------------\u001B[0m");
            System.out.println();
            solver.reset();
            restart();
        }
        reader.close();
    }
}

