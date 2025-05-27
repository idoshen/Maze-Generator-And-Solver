import java.io.IOException;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;
import java.awt.Color;

public class MazeGeneratorAndSolver {

    public static int MAZE_SIZE; // Size of maze

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

    public static void main(String[] args) throws InterruptedException {
        StdDraw.enableDoubleBuffering();
        System.out.println("Welcome to the Maze Solver Program!\n");
        Scanner reader = new Scanner(System.in);
        boolean continueLoop = true;
        System.out.print("Please enter the canvas width (enter a positive integer that is bigger than 1): ");

        int canvasWidth, canvasHeight;
        do{
            canvasWidth = reader.nextInt();
            if (canvasWidth <= 1) System.out.print("Invalid input, please try again: ");
        }while(canvasWidth <= 1);

        System.out.print("Please enter the desired canvas height (enter a positive integer that is bigger than 1): ");

        do{
            canvasHeight = reader.nextInt();
            if (canvasHeight <= 1) System.out.print("Invalid input, please try again: ");
        }while(canvasHeight <= 1);

        while(continueLoop){
            System.out.print("Please enter the desired size of the maze (enter a positive integer that is bigger than 1): ");

            do{
                MAZE_SIZE = reader.nextInt();
                if (MAZE_SIZE <= 1) System.out.print("Invalid input, please try again: ");
            }while(MAZE_SIZE <= 1);

            MazeGenerator.MAZE_SIZE = MAZE_SIZE;
            
            System.out.print("Generating maze...");
            MazeGenerator.generateMaze();
            MazeGenerator.removeWalls();
            System.out.println(" Done!\n");
            
            
            System.out.println("Current number of walls: " + MazeGenerator.E.size());
            System.out.print("Enter the number of random walls to remove: ");
            MazeGenerator.removeRandomWalls(reader.nextInt());
            
            MazePrinter.init(MAZE_SIZE, MazeGenerator.E, MazeGenerator.RandomlyRemovedWalls);
            MazePrinter.printMaze(canvasWidth, canvasHeight);
            System.out.print("Constructing graph...");
            MazeGenerator.constructGraph();
            System.out.println(" Done!\n");

            
            System.out.print("Insert start index (0 - " + (MAZE_SIZE*MAZE_SIZE - 1) + "): ");
            
            while (MazeGenerator.start < 0 || MazeGenerator.start >  MAZE_SIZE*MAZE_SIZE - 1){
                try{
                    MazeGenerator.start = reader.nextInt();
                }catch(InputMismatchException e){
                    reader.next(); // Scans the next token of the input as an int.
                    MazeGenerator.start = -1;
                }
                if (MazeGenerator.start < 0 || MazeGenerator.start >  MAZE_SIZE*MAZE_SIZE - 1){
                    System.out.print("Invalid input, please try again: ");
                }
            }

            StdDraw.setPenColor(Color.RED);
            StdDraw.filledSquare(MazeGenerator.start % MAZE_SIZE + 0.5, MazeGenerator.start / MAZE_SIZE + 0.5 , 0.3);
            StdDraw.show();
            System.out.print("Insert end index (0 - " + (MAZE_SIZE*MAZE_SIZE - 1) + "): ");

            while (MazeGenerator.end < 0 || MazeGenerator.end >  MAZE_SIZE*MAZE_SIZE - 1){
                try{
                    MazeGenerator.end = reader.nextInt();
                }catch(InputMismatchException e){
                    reader.next(); // Scans the next token of the input as an int.
                    MazeGenerator.end = -1;
                }
                if (MazeGenerator.end < 0 || MazeGenerator.end >  MAZE_SIZE*MAZE_SIZE - 1){
                    System.out.print("Invalid input, please try again: ");
                }
            }

            System.out.println();
            StdDraw.filledSquare(MazeGenerator.end % MAZE_SIZE + 0.5, MazeGenerator.end / MAZE_SIZE + 0.5 , 0.3);
            StdDraw.show();

            MazePrinter.start = MazeGenerator.start;
            MazePrinter.end = MazeGenerator.end;
            
            MazeSolver solver = new MazeSolver(MAZE_SIZE);
            solver.reset();

            // DFS START
            // System.out.print("Visualize the solving process of DFS? (Y/N): ");
            // boolean printProcessDFS = reader.next().equalsIgnoreCase("Y");

            // System.out.println("Solving the maze...\n");
            
            // long startTime = System.currentTimeMillis();
            // MazeSolver.solveDFS(MazeGenerator.start,MazeGenerator.end, Color.blue, printProcessDFS);
            // long endTime = System.currentTimeMillis();
            // long elapsedTime = endTime - startTime;

            // LinkedList<Integer> pathDFS = solver.eval();

            // System.out.println("Solved using DFS in " + elapsedTime  + " milliseconds");
            // MazePrinter.printSolution(pathDFS, Color.MAGENTA);

            // DFS END

            // solver.reset();
            
            //BFS START
            System.out.print("Visualize the solving process of BFS? (Y/N): ");
            boolean printProcessBFS = reader.next().equalsIgnoreCase("Y");

            // startTime = System.currentTimeMillis();
            MazeSolver.solveBFS(MazeGenerator.start, MazeGenerator.end, printProcessBFS);
            // endTime = System.currentTimeMillis();
            // elapsedTime = endTime - startTime;
            
            LinkedList<Integer> pathBFS = solver.eval();
            // System.out.println("Solved using BFS in " + elapsedTime  + " milliseconds");
            MazePrinter.printSolution(pathBFS, Color.BLUE);
            // BFS END
            
            // System.out.println("Press Enter to continue to A*.");
            // enterPress();

            solver.reset();
            

            // A* START
            System.out.print("Visualize the solving process of A*? (Y/N): ");
            boolean printProcessAStar = reader.next().equalsIgnoreCase("Y");

            // startTime = System.currentTimeMillis();
            MazeSolver.solveAStar(MazeGenerator.start, MazeGenerator.end, printProcessAStar);
            // endTime = System.currentTimeMillis();
            // elapsedTime = endTime - startTime;

            LinkedList<Integer> pathAStar = solver.eval();
            // System.out.println("Solved using A* in " + elapsedTime  + " milliseconds");
            MazePrinter.printSolution(pathAStar, Color.GREEN);
            // A* END
            
            System.out.println("Press Enter to continue to DFS in the original maze.");
            enterPress();

            MazeGenerator.addBackRandomWalls();
            MazeGenerator.constructGraph();

            solver.reset();
            

            // DFS ORIGINAL MAZE START
            System.out.print("Visualize the solving process of the original maze DFS? (Y/N): ");
            boolean printProcessDFSOriginal = reader.next().equalsIgnoreCase("Y");
            MazeSolver.solveDFS(MazeGenerator.start, MazeGenerator.end, Color.RED, printProcessDFSOriginal);
            
            LinkedList<Integer> pathSingle = solver.eval();
            System.out.println("Solved using DFS in the original maze");
            MazePrinter.printSolution(pathSingle, Color.RED);
            // DFS ORIGINAL MAZE END

            StdDraw.save("Maze.jpg");
            System.out.println("Press Enter to continue or Esc to exit.");
            enterPress();
            System.out.println("--------------------------------------------");
            System.out.println();
            MazeGenerator.restart();
        }
        reader.close();
    }
}
