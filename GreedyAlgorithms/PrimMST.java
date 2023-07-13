import Literals.WeightedUndirectedGraph;
import Literals.Edge;
import Literals.CustomHeap;

public class PrimMST {
    private WeightedUndirectedGraph graph;
    private CustomHeap heap;
    private boolean[] marked; // array to track explored nodes
    
    // Constructor
    public PrimMST(String filename) {
        graph = new WeightedUndirectedGraph(filename);
        int nodes = graph.getNodes();
        heap = new CustomHeap(nodes);
        marked = new boolean[nodes + 1];
    }

    // Ouputs an array which contains positive shortest paths for all nodes
    // Source node is labelled as 0
    // If a path to a node does not exist, path length is set to be -1
    public int computeMSTCost(int sourceNode) {
        // Set source's value as 0 in the heap
        heap.decreaseKey(sourceNode, 0);
        int treeCost = 0;

        while (!heap.isEmpty()) {
            // Extract minimum from the heap
            int node = heap.peekMinNode();
            treeCost += heap.extractMinKey();

            marked[node] = true;

            for (Edge edge : graph.getEdgeList(node)) {
                if (!marked[edge.getNode()]) {
                    // Update all the adjacent nodes with the new current shortest path
                    heap.decreaseKey(edge.getNode(), edge.getWeight());
                }
            }
        }
        return treeCost;
    }

    public static void main(String[] args) {
        String filename = args[0];
        int sourceNode = 1;
        PrimMST obj = new PrimMST(filename);
        int treeCost = obj.computeMSTCost(sourceNode);

        System.out.println("The MST Cost is: " + treeCost);
    }
}
