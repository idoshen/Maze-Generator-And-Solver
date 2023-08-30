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
        while(continueLoop){
            System.out.print("\u001B[31mPlease enter the desired size of the maze (enter a positive integer that is bigger than 1): \u001B[0m");

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
            System.out.print("\u001B[31mEnter the number of random walls to remove: \u001B[0m");
            MazeGenerator.removeRandomWalls(reader.nextInt());
            
            MazePrinter.init(MAZE_SIZE, MazeGenerator.E, MazeGenerator.RandomlyRemovedWalls);
            MazePrinter.printMaze();
            System.out.print("Constructing graph...");
            MazeGenerator.constructGraph();
            System.out.println(" Done!\n");

            
            System.out.print("\u001B[31mInsert start index (0 - " + (MAZE_SIZE*MAZE_SIZE - 1) + "): \u001B[0m");
            
            while (MazeGenerator.start < 0 || MazeGenerator.start >  MAZE_SIZE*MAZE_SIZE - 1){
                try{
                    MazeGenerator.start = reader.nextInt();
                }catch(InputMismatchException e){
                    reader.next(); // Scans the next token of the input as an int.
                    MazeGenerator.start = -1;
                }
                if (MazeGenerator.start < 0 || MazeGenerator.start >  MAZE_SIZE*MAZE_SIZE - 1){
                    System.out.print("\u001B[31mInvalid input, please try again: \u001B[0m");
                }
            }

            StdDraw.setPenColor(Color.RED);
            StdDraw.filledSquare(MazeGenerator.start % MAZE_SIZE + 0.5, MazeGenerator.start / MAZE_SIZE + 0.5 , 0.5);
            StdDraw.show();
            System.out.print("\u001B[31mInsert end index (0 - " + (MAZE_SIZE*MAZE_SIZE - 1) + "): \u001B[0m");

            while (MazeGenerator.end < 0 || MazeGenerator.end >  MAZE_SIZE*MAZE_SIZE - 1){
                try{
                    MazeGenerator.end = reader.nextInt();
                }catch(InputMismatchException e){
                    reader.next(); // Scans the next token of the input as an int.
                    MazeGenerator.end = -1;
                }
                if (MazeGenerator.end < 0 || MazeGenerator.end >  MAZE_SIZE*MAZE_SIZE - 1){
                    System.out.print("\u001B[31mInvalid input, please try again: \u001B[0m");
                }
            }

            System.out.println();
            StdDraw.filledSquare(MazeGenerator.end % MAZE_SIZE + 0.5, MazeGenerator.end / MAZE_SIZE + 0.5 , 0.5);
            StdDraw.show();

            MazePrinter.start = MazeGenerator.start;
            MazePrinter.end = MazeGenerator.end;
            
            MazeSolver solver = new MazeSolver(MAZE_SIZE);
            solver.reset();

            System.out.println("Solving the maze...\n");
            
            long startTime = System.currentTimeMillis();
            MazeSolver.solveDFS(MazeGenerator.start,MazeGenerator.end);
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            LinkedList<Integer> pathDFS = solver.eval();

            System.out.println("\u001B[32mSolved using DFS in " + elapsedTime  + " milliseconds");
            MazePrinter.printSolution(pathDFS, Color.MAGENTA);
            System.out.println("\u001B[31mPress Enter to continue to BFS.\u001B[32m");
            enterPress();

            solver.reset();

            startTime = System.currentTimeMillis();
            MazeSolver.solveBFS(MazeGenerator.start, MazeGenerator.end);
            endTime = System.currentTimeMillis();
            elapsedTime = endTime - startTime;
            
            LinkedList<Integer> pathBFS = solver.eval();
            System.out.println("Solved using BFS in " + elapsedTime  + " milliseconds");
            MazePrinter.printSolution(pathBFS, Color.BLUE);
            System.out.println("\u001B[31mPress Enter to continue to DFS in the original maze.\u001B[32m");
            enterPress();

            MazeGenerator.addBackRandomWalls();
            MazeGenerator.constructGraph();

            solver.reset();
            MazeSolver.solveDFS(MazeGenerator.start, MazeGenerator.end);
            
            LinkedList<Integer> pathSingle = solver.eval();
            System.out.println("Solved using DFS in the original maze");
            MazePrinter.printSolution(pathSingle, Color.RED);

            StdDraw.save("Maze.jpg");
            System.out.println("\u001B[31mPress Enter to continue or Esc to exit.\u001B[32m");
            enterPress();
            System.out.println("\u001B[34m--------------------------------------------\u001B[0m");
            System.out.println();
            MazeGenerator.restart();
        }
        reader.close();
    }
}
