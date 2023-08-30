import java.util.*;

class GraphNode {
    public int value;
    public List<Integer> neighbors;
    public int parent;

    public GraphNode(int value) {
        this.value = value;
        this.neighbors = new LinkedList<>();
        this.parent = -1;
    }
    
}
