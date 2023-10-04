package Graphs;

import java.util.LinkedList;
import java.util.List;

import DataStructures.WeightedDirectedGraph;
import DataStructures.WeightedDirectedGraph.Edge;
import Graphs.BellmanFord.Result;

public class JohnsonAPSP {
    
    private BellmanFord firstPass;
    private Dijkstra dijkstra;
    private WeightedDirectedGraph graph;
    private int[] weights;
    private int[][] paths;
    private int shortest, nodes;

    public JohnsonAPSP(String filename) {

        graph = new WeightedDirectedGraph(filename);

        shortest = Integer.MAX_VALUE;
        nodes = graph.nodes();
        paths = new int[nodes + 1][nodes + 1];
        weights = new int[nodes + 1];
    }

    private Result computeWeights() {
        WeightedDirectedGraph newGraph = new WeightedDirectedGraph(nodes + 1);
        List<Edge> newSource = new LinkedList<Edge>();
        for(int i = 1; i <= nodes; i++) {
            newSource.add(new Edge(i, 0));
            newGraph.addEdgeList(graph.getEdgeList(i), i);
        }
        newGraph.addEdgeList(newSource, nodes + 1);

        firstPass = new BellmanFord(newGraph);
        Result result = firstPass.computeShortestPath(newGraph.nodes());
        return result;
    }

    private WeightedDirectedGraph generateReweightedGraph(int[] weights) {
        WeightedDirectedGraph newGraph = new WeightedDirectedGraph(graph.nodes());
        for(int i = 1; i <= graph.nodes(); i++) {
            List<Edge> newEdges = new LinkedList<Edge>();
            for(Edge edge : graph.getEdgeList(i)) {
                int weight = edge.weight() + weights[i] - weights[edge.node()];
                newEdges.add(new Edge(edge.node(), weight));
            }
            newGraph.addEdgeList(newEdges, i);
        }
        return newGraph;
    }

    public boolean computeAllPaths() {
        Result firstPass = computeWeights();
        if(firstPass.cycle()) {
            return false;
        }

        WeightedDirectedGraph newGraph = generateReweightedGraph(weights);

        for(int i = 1; i <= nodes; i++) {
            dijkstra = new Dijkstra(newGraph);
            paths[i] = dijkstra.computeShortestPaths(i);
        }
        return true;
    }

    public void processPaths() {
        int nodes = graph.nodes();
        for(int source = 1; source <= nodes; source++) {
            for(int dest = 1; dest <= nodes; dest++) {
                if(paths[source][dest] != Integer.MAX_VALUE) {
                    paths[source][dest] = paths[source][dest] - weights[source] + weights[dest];
                }
                if (paths[source][dest] < shortest) shortest = paths[source][dest];
            }
        }
    }

    public static void main(String[] args) {
        String filename = args[0];
        JohnsonAPSP obj = new JohnsonAPSP(filename);
        if(!obj.computeAllPaths()) {
            System.out.println("Given graph has a negative cycle.");
            return;
        }
        obj.processPaths();
        System.out.println("Smallest shortest path in given graph: " + obj.shortest);
    }
}
