/*
 * Program to implement the Dijkstra's Shortest Path Algorithm which computes the shortest
 * paths for all the nodes reachable from a given source node.
 * Running Time: O((m + n)log n)
 * 
 * Since the nodes are one-indexed, the program keeps the zeroth position empty for simplicity.
 */

import Literals.WeightedDirectedGraph;
import Literals.WeightedDirectedGraph.Edge;
import Literals.CustomHeap;

public class Dijkstra {
    private WeightedDirectedGraph graph;
    private CustomHeap heap;
    private boolean[] marked;   // array to track explored nodes
    private int[] paths;    // array to store shortest path from source node

    // Constructor
    public Dijkstra(int nodes, String filename) {
        graph = new WeightedDirectedGraph(nodes, filename);
        heap = new CustomHeap(nodes);
        marked = new boolean[nodes + 1];
        paths = new int[nodes + 1];

        // Initialize default path length as -1
        for (int i = 0; i <= nodes; i++) {
            paths[i] = -1;
        }
    }

    // Ouputs an array which contains positive shortest paths for all nodes
    // Source node is labelled as 0
    // If a path to a node does not exist, path length is set to be -1
    public int[] computeShortestPaths(int sourceNode) {
        // Set source's value as 0 in the heap
        heap.decreaseKey(sourceNode, 0);

        while(!heap.isEmpty()){
            // Extract minimum from the heap
            int node = heap.peekMinNode();
            int currentDistance = heap.extractMinKey();

            // Note the mininum Distance and mark node
            paths[node] = currentDistance;
            marked[node] = true;

            for (Edge edge : graph.getEdgeList(node)){
                if (!marked[edge.node()]) {
                    // Update all the adjacent nodes with the new current shortest path
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
