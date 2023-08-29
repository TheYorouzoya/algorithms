/*
 * Program to implement the Maximum Weight Independent Set Graph algorithm.
 * Running Time: O(n)
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MWISGraph {
    int nodes;
    ArrayList<Integer> pathGraph;

    // Load graph from given file
    public MWISGraph(String filename) {
        pathGraph = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            if((line = reader.readLine()) != null) {
                nodes = Integer.parseInt(line);
            } else throw new IOException("Given file " + filename + "is empty.");

            while((line = reader.readLine()) != null) {
                pathGraph.add(Integer.parseInt(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Generate the solutions based on whether the last two vertices are part of the maximum
    // weight independent set or not
    public int[] processGraph() {
        int[] solutions = new int[nodes + 1];

        // First two positions correspond to the empty set and the first vertex itself
        solutions[0] = 0;
        solutions[1] = pathGraph.get(0);

        // For the rest of the graph, store the maximum value between Array[i -1] and
        // Array[i - 2] + vertex[i]
        for(int i = 2; i <= nodes; i++) {
            solutions[i] = Math.max(solutions[i - 1], solutions[i - 2] + pathGraph.get(i - 1));
        }
        return solutions;
    }

    // Return a boolean array with indices corresponding to graph vertex where each entry denotes
    // whether the vertex is a part of the maximum weight independent set
    public boolean[] reconstructSolutions(int[] solutions) {
        boolean[] vertices = new boolean[nodes + 1];
        int i = nodes;
        while(i >= 2) {
            if(solutions[i - 1] < solutions[i - 2] + pathGraph.get(i - 1)) {
                vertices[i] = true;
                i -= 2;
            } else i--;
        }
        if(i == 1) vertices[i] = true;
        return vertices;
    }

    public static void main(String[] args) {
        String filename = args[0];
        MWISGraph obj = new MWISGraph(filename);
        int[] solutions = obj.processGraph();
        boolean[] table = obj.reconstructSolutions(solutions);
        int[] targets = {1, 2, 3, 4, 17, 117, 517, 997};
        char[] output = new char[8];
        int index = 0;
        
        for(int target : targets) {
            if(target < obj.nodes && table[target]) output[index++] = '1';
            else output[index++] = '0';
        }

        System.out.println("Output string is: " + new String(output));
    }
}
