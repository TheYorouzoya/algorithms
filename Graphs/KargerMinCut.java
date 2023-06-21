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
 * INPUT: An adjacency list containing an undirected graph G with vertices V and edges E.
 *        Parallel edges are allowed.
 * 
 * OUTPUT: Compute a cut with the fewest number of crossing edges (i.e., a min cut).
 * 
 * STEPS: While there are more than 2 vertices in the graph, repeat the following--
 *          1. Pick a remaining edge (u, v) uniformly at random
 *          2. Contract the edge into a single vertex
 *          3. Remove any self-loops
 *        Return the cut represented by the final 2 vertices.
 * 
 * 
 * ======================================
 * About The Randomized Edge Selection:
 * ======================================
 * 
 * Since we need to select an edge uniformly at random, after an edge contracts, the two nodes
 * need to be represented by a single parent node. To ensure this, we make two copies of the
 * node array-- one, to select the initial vertex from, after which, we contract this array by
 * removing the selected node; two, to keep track of the parent node once the two nodes have been
 * merged together after contraction.
 * 
 * The second copy is useful when we need to clean up the self loops after an edge contraction.
 * 
 * NOTE: The implementation below uses a recursive algorithm to "look-up" a node's parent.
 *       Instead of this "look-up", a parent node (supernode) replacement would be much more efficient
 *       since we do multiple look-ups for every edge contraction.
 * 
 * 
 * =============================
 * Edge Contraction Subroutine:
 * =============================
 * 
 * INPUT: Two nodes (u, v) and all the edges they point to respectively.
 * OUTPUT: A single parent node with the edges of (u, v) combined except any edges between u and v
 *         (i.e., self-loops) are removed.
 * STEPS: After an edge (u, v) has been selected uniformly at random (here, v would become the parent),
 *          - Append all the edges in u to the end of v
 *          - Clean up the resulting edge list by removing self-loops
 *          - Have u point to v (i.e., alias).
 *          - Remove u from the first copy of the list of nodes.
 * 
 * 
 * ===============================
 * Self-loop Clean-Up Subroutine:
 * ===============================
 * 
 * INPUT: A list of edges originating from a given node n.
 * OUTPUT: The same list but with all the self loops removed.
 * 
 * STEPS: Until the edge list is empty-
 *          - Read an edge node
 *          - See if this shares a parent with the node n
 *          - If yes, then remove this node.
 *        Return the updated list.
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

        // Assign parent node in the reference list
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
