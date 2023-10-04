/*
 * A custom Directed Graph supporting edge weights represented as an adjacency matrix.
 */

package DataStructures;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeightedDirectedGraph {
    // Custom edge class to store the endpoint of an edge along with the weight
    public static class Edge {
        private int node, weight;

        public Edge(int node, int weight) {
            this.node = node;
            this.weight = weight;
        }

        public int node() { return this.node; }
        public int weight() { return this.weight; }
    }

    private int nodes, edges;
    private ArrayList<List<Edge>> adjList;

    public WeightedDirectedGraph(int N) {
        // Initialize a graph with N nodes
        this.nodes = N;
        this.edges = 0;
        adjList = new ArrayList<List<Edge>>(N);

        for (int i = 0; i <= N; i++) {
            ArrayList<Edge> blank = new ArrayList<>();
            adjList.add(blank);
        }
    }

    public WeightedDirectedGraph(int N, String filename) {
        // Read a graph from a given file
        // Line Structure in file: [Node WhiteSpace [Node,Length WhiteSpace]... repeat]

        this.nodes = N;
        this.edges = 0;
        adjList = new ArrayList<List<Edge>>(N);

        for(int i = 0; i <= N; i++) {
            ArrayList<Edge> blank = new ArrayList<>();
            adjList.add(blank);
        }

        // Read data from file into buffer
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            // Until reader reaches end of file
            while((line = reader.readLine()) != null) {
                // Split line into (node, weight) pairs
                String[] nodePairs = line.split("\t");

                // First element is the base node
                int baseNode = Integer.parseInt(nodePairs[0]);

                // Parse all the outgoing edges
                for(int i = 1; i < nodePairs.length; i++) {
                    String[] edgeLength = nodePairs[i].split(",");
                    Edge edge = new Edge(Integer.parseInt(edgeLength[0]), Integer.parseInt(edgeLength[1]));
                    adjList.get(baseNode).add(edge);
                    this.edges++;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public WeightedDirectedGraph(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            String[] data;

            if ((line = reader.readLine()) == null) {
                throw new IOException("Given file " + filename + " is empty.");
            }

            data = line.split(" ");
            this.nodes = Integer.parseInt(data[0]);
            this.edges = Integer.parseInt(data[1]);

            adjList = new ArrayList<List<Edge>>(nodes);
            for (int i = 0; i <= nodes; i++) {
                ArrayList<Edge> blank = new ArrayList<>();
                adjList.add(blank);
            }

            while ((line = reader.readLine()) != null) {
                data = line.split(" ");
                int tail = Integer.parseInt(data[0]);
                int head = Integer.parseInt(data[1]);
                int weight = Integer.parseInt(data[2]);
                adjList.get(tail).add(new Edge(head, weight));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int nodes() { return this.nodes; }
    public int edges() { return this.edges; }

    public List<Edge> getEdgeList(int node) {
        return adjList.get(node);
    }

    public void addEdgeList(List<Edge> list, int index) {
        adjList.set(index, list);
    }

    public void printGraph() {
        for (int i = 1; i <= this.nodes; i++) {
            System.out.print(i + " -> ");
            for(Edge edge : adjList.get(i)) {
                System.out.print(edge.node + "(" + edge.weight + ") ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        long startTime, stopTime;
        int nodes = 200;
        String filename = "dijkstradata.txt";
        startTime = System.currentTimeMillis();
        WeightedDirectedGraph testGraph = new WeightedDirectedGraph(nodes, filename);
        stopTime = System.currentTimeMillis();

        System.out.println("Loading the graph took: " + (stopTime - startTime) + " ms");
        testGraph.printGraph();
    }
}
