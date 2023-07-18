/*
 * Program to implement Prim's Minimum Spanning Trees algorithm using a
 * custom heap data structure to optimize running time.
 */

import DataStructures.WeightedUndirectedGraph;
import DataStructures.Edge;
import DataStructures.CustomHeap;

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

    // Output the total cost of the minimum spanning tree for a given graph
    public int computeMSTCost(int sourceNode) {
        // Set source's value as 0 in the heap
        heap.decreaseKey(sourceNode, 0);
        int treeCost = 0;

        while (!heap.isEmpty()) {
            // Extract minimum from the heap
            int node = heap.peekMinNode();
            // Add heap's key to running tree cost
            treeCost += heap.extractMinKey();
            // Mark node as explored
            marked[node] = true;

            for (Edge edge : graph.getEdgeList(node)) {
                if (!marked[edge.getNode()]) { // If edge isn't already explored
                    // Update all the adjacent nodes with the new current lowest cost
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
