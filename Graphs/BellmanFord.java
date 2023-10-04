package Graphs;

import java.util.Arrays;

import DataStructures.WeightedDirectedGraph;
import DataStructures.WeightedDirectedGraph.Edge;

public class BellmanFord {
    private WeightedDirectedGraph graph;
    private int[] paths;
    public record Result(boolean cycle, int[] paths) {};

    public BellmanFord(String filename) {
        graph = new WeightedDirectedGraph(filename);
    }

    public BellmanFord(WeightedDirectedGraph graph) {
        this.graph = graph;
    }

    public Result computeShortestPath(int source) {
        int nodes = graph.nodes();
        paths = new int[nodes + 1];
        
        Arrays.fill(paths, Integer.MAX_VALUE);
        paths[source] = 0;

        for(int i = 1; i <= nodes; i++) {
            boolean stable = true;
            int[] nextPaths = paths.clone();

            for(int j = 1; j <= nodes; j++) {
                for(Edge edge : graph.getEdgeList(j)) {
                    int tail = j;
                    int head = edge.node();

                    if(paths[tail] == Integer.MAX_VALUE) continue;
                    nextPaths[head] = Math.min(nextPaths[head], paths[tail] + edge.weight());
                    if(nextPaths[head] != paths[head]) {
                        stable = false;
                    }
                }
            }
            if(stable) { 
                return new Result(false, nextPaths);
            }

            paths = nextPaths;
        }

        return new Result(true, null);
    }

    public static void main(String[] args) {
        String filename = args[0];
        BellmanFord obj = new BellmanFord(filename);
        Result result = obj.computeShortestPath(1);

        if(result.cycle()) {
            System.out.println("Graph contains a negative cycle.");
            return;
        }
        
        int shortest = Integer.MAX_VALUE;
        for(int path : result.paths()) {
            if (shortest > path) shortest = path;
        }
        System.out.println(Arrays.toString(obj.paths));
        System.out.println("Shortest path is: " + shortest);
    }

}