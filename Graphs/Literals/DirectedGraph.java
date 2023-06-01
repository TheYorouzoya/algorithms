/*
 * A Directed Graph that is represented by an adjacency matrix.
 * Graph contains nodes and integers and edges are stores as an array
 * of ArrayList.
 * Only stores the outgoing adjacency list and not the incoming one.
 */

package Literals;

import java.io.BufferedReader;
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
        // Assume the file contains a list of edges as [Integer-Space-Integer-Space] as each
        // edge per line, i.e., "2 4 "

        this.nodes = N;
        this.edges = 0;

        // Load raw string data into StringBuilder
        final int MAX_BUFFER_SIZE = 1024 * 4;
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[MAX_BUFFER_SIZE];
        int readChars;

        try (BufferedReader scanner = new BufferedReader(new FileReader(filename))) {
            while ((readChars = scanner.read(buffer, 0, MAX_BUFFER_SIZE)) > 0) {
                builder.append(buffer, 0, readChars);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize Adjacency list
        adjList = (ArrayList<Integer>[]) new ArrayList[nodes];
        for (int i = 0; i < nodes; i++) {
            adjList[i] = new ArrayList<>();
        }

        // Parse the StringBuilder to construct the adjacency list
        int node[] = new int[2];
        int i = 0, j = 0, k = 0;
        for (; i < builder.length(); i++) {
            if (builder.charAt(i) == ' ') {
                node[k++] = Integer.parseInt(builder.substring(j, i));
                j = i + 1;
            } else if (builder.charAt(i) == '\n') {
                k = 0;
                adjList[node[0]].add(node[1]);
                j = i + 1;
            }
        }
        adjList[node[0]].add(node[1]);
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

    public void printGraph() {
        for(int i = 0; i < nodes; i++) {
            System.out.println(i + " -> " + adjList[i]);
        }
    }

    public DirectedGraph reverseGraph() {
        DirectedGraph reverse = new DirectedGraph(nodes);
        for (int i = 0; i < nodes; i++) {
            for(int node : adjList[i]) {
                reverse.adjList[node].add(i);
                reverse.edges++;
            }
        }
        return reverse;
    }

    public static void main(String[] args) {
        return;
    }
}
