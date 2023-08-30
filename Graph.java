import java.util.*;

public class Graph {

    public GraphNode[] mainArr;

    public Graph(List<List<Integer>> maze, int size){
        this.mainArr = new GraphNode[size];
        for (int i = 0; i < size; i++) {
            this.mainArr[i] = new GraphNode(i);
        }

        for (List<Integer> edge : maze){
            int u = edge.get(0), v = edge.get(1);
            mainArr[u].neighbors.add(v);
            mainArr[v].neighbors.add(u);
        }
    }

    public GraphNode[] getGraph(){
        return mainArr;
    }

    public String toString(){
        String str ="";
        for (GraphNode edge : mainArr){
            str += edge.value + " -> " + edge.neighbors.toString() + "\n";
        }
        return str;
    }
}
