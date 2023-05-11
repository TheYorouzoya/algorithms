import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/*
 * Program to implement the Randomized Contraction algorithm for a given undirected graph
 * Running Time: O(n^2m) (n = nodes, m = edges)
 * 
 * This is a rather slow and inefficient implementation of computing graph cuts using
 * the contraction method. An algorithm with more informed trial selection would achieve
 * a much better performance in comparison (close to O(n^2)).
 * 
 * =================
 * The Algorithm:
 * =================
 * 
 * 
 * 
 */

public class KargerMinCut {

    public static class AdjacencyList {
        private ArrayList<Integer> nodes;
        private ArrayList<LinkedList<Integer>> edges;

        public AdjacencyList() {
            this.nodes = new ArrayList<Integer>();
            this.edges = new ArrayList<LinkedList<Integer>>();
        }

        public ArrayList<Integer> getNodes() {
            return this.nodes;
        }

        public ArrayList<LinkedList<Integer>> getEdges() {
            return this.edges;
        }

        public void loadGraphFromFile(String filename) {
            try (Scanner scanner = new Scanner(new File(filename))) {
                int j = 0;
                while (scanner.hasNext()) {
                    edges.add(new LinkedList<Integer>());
                    String line = scanner.nextLine();
                    String[] lineNodes = line.split("\\s");
                    nodes.add(j, Integer.parseInt(lineNodes[0]));   // Add first element of line to nodes array
                    for (int i = 0; i < lineNodes.length; i++) {
                        // Add the line of nodes to the linked list
                        edges.get(j).add(Integer.parseInt(lineNodes[i]));
                    }
                    j++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }

    private static void contractRandomEdge(ArrayList<LinkedList<Integer>> adjList) {
        // Select the node at random
        // Select the edge node in the node's linked list at random
        // Append this latter node's edge linked list to the end of the first node's linked list
        // clear the latter node from the array
    }

    public static void main(String[] args) {
        String filename = "kargerTest.txt";
        AdjacencyList adjList = new AdjacencyList();
        adjList.loadGraphFromFile(filename);
        System.out.println(adjList.edges.get(0));
    }
}
