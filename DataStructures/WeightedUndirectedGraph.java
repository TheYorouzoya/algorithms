/*
 * An Undirected Graph with edge weights.
 * Edges are represented as an adjacency matrix.
 */

package DataStructures;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeightedUndirectedGraph {
    private int nodes, edges;
    private List<List<Edge>> adjList;

    public WeightedUndirectedGraph (int N) {
        this.nodes = N;
        this.edges = 0;
        adjList = new ArrayList<List<Edge>>(N);

        for (int i = 0; i <= N; i++) {
            List<Edge> blank = new ArrayList<Edge>();
            adjList.add(blank);
        }
    }

    public WeightedUndirectedGraph (String filename) {
        // File structure is as follows:
        // [number of nodes] [number of edges]
        // [one node of edge] [other node of edge] [edge cost]

        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            
            String line = reader.readLine();
            String[] pairs = line.split(" ");

            // Get number of nodes and edges from line 1
            this.nodes = Integer.parseInt(pairs[0]);
            this.edges = Integer.parseInt(pairs[0]);

            // Initialize adjacency list
            adjList = new ArrayList<List<Edge>>(nodes);
            for(int i = 0; i <= nodes; i++) {
                List<Edge> blank = new ArrayList<Edge>();
                adjList.add(blank);
            }

            // Read and parse the rest of the file
            while((line = reader.readLine()) != null) {
                pairs = line.split(" ");
                int node1, node2, weight;

                // Get edge parameters
                node1 = Integer.parseInt(pairs[0]);
                node2 = Integer.parseInt(pairs[1]);
                weight = Integer.parseInt(pairs[2]);

                // Add edge as Node1 -> Node2
                Edge edge = new Edge(node2, weight);
                adjList.get(node1).add(edge);

                // Add edge as Node2 -> Node1
                edge = new Edge(node1, weight);
                adjList.get(node2).add(edge);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNodes() {
        return this.nodes;
    }

    public int getEdges() {
        return this.edges;
    }

    public List<Edge> getEdgeList(int node) {
        return adjList.get(node);
    }

    public void printGraph() {
        for (int i = 1; i <= this.nodes; i++) {
            System.out.print(i + " -> ");
            for (Edge edge : adjList.get(i)) {
                System.out.print(edge.getNode() + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        String filename = args[0];
        long start, stop;
        start = System.currentTimeMillis();
        WeightedUndirectedGraph graph = new WeightedUndirectedGraph(filename);
        stop = System.currentTimeMillis();
        System.out.println("Time taken to load graph: " + (stop - start) + "ms");
        graph.printGraph();
    }
}
