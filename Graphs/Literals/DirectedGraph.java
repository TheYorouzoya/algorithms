package Graphs.Literals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DirectedGraph {
    private int nodes, edges;
    private ArrayList<Integer>[] adjList;

    public DirectedGraph(int N) {
        this.nodes = N;
        this.edges = 0;
        adjList = (ArrayList<Integer>[]) new ArrayList[N];
        for (int i = 0; i < N; i++) {
            adjList[i] = new ArrayList<>();
        }
    }

    public DirectedGraph(String filename, int N) {
        this.nodes = N;
        this.edges = 0;
        adjList = (ArrayList<Integer>[]) new ArrayList[N];
        for (int i = 0; i < N; i++) {
            adjList[i] = new ArrayList<>();
        }
        try (BufferedReader scanner = new BufferedReader(new FileReader(filename))) {
            String line;
            while((line = scanner.readLine()) != null) {
                String[] fileNodes = line.split("\\s");
                addEdge(Integer.parseInt(fileNodes[0]), Integer.parseInt(fileNodes[1]));
                edges++;
            }
            // while(scanner.hasNext()) {
            //     int node1 = scanner.nextInt();
            //     int node2 = scanner.nextInt();
            //     addEdge(node1, node2);
            //     edges++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int nodes() { return this.nodes; }
    public int edges() { return this.edges; }

    public ArrayList<Integer> getEdges(int node) {
        return this.adjList[node];
    }

    public void addEdge(int node1, int node2) {
        adjList[node1].add(node2);
        edges++;
    }

    public DirectedGraph reverseGraph() {
        DirectedGraph reverse = new DirectedGraph(nodes);
        for (int i = 0; i < nodes; i++) {
            for(int node : adjList[i]) {
                reverse.addEdge(node, i);
            }
        }
        return reverse;
    }

    public static void main(String[] args) {
        return;
    }
}
