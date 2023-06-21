import Literals.WeightedDirectedGraph;
import Literals.WeightedDirectedGraph.Edge;
import Literals.CustomHeap;

public class Dijkstra {
    private WeightedDirectedGraph graph;
    private CustomHeap heap;
    private boolean[] marked;
    private int[] paths;

    public Dijkstra(int nodes, String filename) {
        graph = new WeightedDirectedGraph(nodes, filename);
        heap = new CustomHeap(nodes);
        marked = new boolean[nodes + 1];
        paths = new int[nodes + 1];
    }

    public int[] computeShortestPaths(int sourceNode) {
        heap.decreaseKey(sourceNode, 0);
        while(!heap.isEmpty()){
            int node = heap.peekMinNode();
            int currentDistance = heap.extractMinKey();
            paths[node] = currentDistance;
            marked[node] = true;
            for (Edge edge : graph.getEdgeList(node)){
                if (!marked[edge.node()]) {
                    heap.decreaseKey(edge.node(), edge.weight() + currentDistance);
                }
            }
        }
        return paths;
    }

    public static void main(String[] args) {
        int nodes = 200;
        int sourceNode = 1;
        String filename = "dijkstradata.txt";
        Dijkstra obj = new Dijkstra(nodes, filename);
        System.out.println("Heap size is: " + obj.heap.size());

        int[] paths = obj.computeShortestPaths(sourceNode);

        int[] required = {7, 37, 59, 82, 99, 115, 133, 165, 188, 197};
        System.out.println("The required paths are: ");
        for(int index : required) {
            System.out.println(index + ": " + paths[index]);
        }


    }

}
