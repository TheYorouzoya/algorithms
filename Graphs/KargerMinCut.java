import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

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
        private ArrayList<Integer> nodesReference;
        private ArrayList<LinkedList<Integer>> edges;

        public AdjacencyList() {
            this.nodes = new ArrayList<Integer>();
            this.nodesReference = new ArrayList<Integer>();
            this.edges = new ArrayList<LinkedList<Integer>>();
        }

        public ArrayList<Integer> getNodes() {
            return this.nodes;
        }

        public ArrayList<Integer> getNodesReference() {
            return this.nodesReference;
        }

        public ArrayList<LinkedList<Integer>> getEdges() {
            return this.edges;
        }

        public int lookupNodeReference(int node) {
            if (nodesReference.get(node - 1) == node) {
                return node;
            } else {
                return lookupNodeReference(nodesReference.get(node - 1));
            }
        }

        public long graphSize() {
            long size = 0;
            for(int i = 0; i < edges.size(); i++) {
                size += edges.get(i).size();
            }
            return size;
        }

        public void loadGraphFromFile(String filename) {
            try (Scanner scanner = new Scanner(new File(filename))) {
                int j = 0;
                while (scanner.hasNext()) {
                    edges.add(new LinkedList<Integer>());
                    String line = scanner.nextLine();
                    String[] lineNodes = line.split("\\s");
                    nodes.add(j, Integer.parseInt(lineNodes[0]));   // Add first element of line to nodes array
                    nodesReference.add(j, Integer.parseInt(lineNodes[0]));
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

    private static void contractRandomEdge(AdjacencyList adjList) {

        // Select the initial node at random
        int indexA = ThreadLocalRandom.current().nextInt(adjList.nodes.size());
        int nodeA = adjList.nodes.get(indexA);
        int nodeAIndex = nodeA - 1;

        // Select the edge node in the previously selected node's linked list at random
        int indexB = ThreadLocalRandom.current().nextInt(1, adjList.edges.get(nodeAIndex).size());
        int nodeB = adjList.edges.get(nodeAIndex).get(indexB);
        nodeB = adjList.lookupNodeReference(nodeB);
        int nodeBIndex = nodeB - 1;

        // Replace exchange node in the reference list
        adjList.nodesReference.set(nodeA - 1, nodeB);

        // Append the first node's linked list to this second edge node
        adjList.edges.get(nodeBIndex).addAll(adjList.edges.get(nodeAIndex));

        // Clean up all the self loops
        for (int i = 1; i < adjList.edges.get(nodeBIndex).size(); i++) {
            int current = adjList.lookupNodeReference(adjList.edges.get(nodeBIndex).get(i));
            if (current == nodeA || current == nodeB) {
                adjList.edges.get(nodeBIndex).remove(i);
                i--;
            }
        }

        // Alias the first node to point to the second node
        adjList.edges.set(nodeAIndex, adjList.edges.get(nodeBIndex));

        // Remove the first node from the list of nodes
        adjList.nodes.remove(indexA);
    }

    private static int computeMinCut(AdjacencyList adjList) {
        while (adjList.nodes.size() > 2) {
            contractRandomEdge(adjList);
        }
        int finalNode = adjList.nodes.get(0);
        return adjList.edges.get(finalNode - 1).size() - 1;
    }

    public static void main(String[] args) {
        String filename = "kargerTest.txt";
        int minCut = Integer.MAX_VALUE;
        
        for(int i = 0; i < 200; i++) {
            AdjacencyList adjList = new AdjacencyList();
            adjList.loadGraphFromFile(filename);
            minCut = Math.min(minCut, computeMinCut(adjList));
        }
        System.out.println("Minimum cut after 200 trials is: " + minCut);
        
    }
}
