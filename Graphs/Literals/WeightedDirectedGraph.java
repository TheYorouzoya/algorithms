package Literals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeightedDirectedGraph {
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
    private List<List<Edge>> adjList;

    public WeightedDirectedGraph(int N) {
        this.nodes = N;
        this.edges = 0;
        adjList = new ArrayList<List<Edge>>(N);
    }

    public WeightedDirectedGraph(int N, String filename) {
        // Line Structure in file: [Node WhiteSpace [Node,Length WhiteSpace]... repeat]

        this.nodes = N;
        this.edges = 0;
        adjList = new ArrayList<List<Edge>>(N);

        for(int i = 0; i <= N; i++) {
            ArrayList<Edge> blank = new ArrayList<>();
            adjList.add(blank);
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] nodePairs = line.split("\t");
                int baseNode = Integer.parseInt(nodePairs[0]);
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

    public int nodes() { return this.nodes; }
    public int edges() { return this.edges; }

    public List<Edge> getEdgeList(int node) {
        return adjList.get(node);
    }

    public void printGraph() {
        for (int i = 1; i <= this.nodes; i++) {
            System.out.print(i + " -> ");
            for(Edge edge : adjList.get(i)) {
                System.out.print(edge.node + " ");
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
