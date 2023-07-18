/*
 * A simple edge class for a graph.
 * The edge only contains the other end along with the weight.
 */

package DataStructures;

public class Edge {
    private int node, weight;

    public Edge(int node, int weight) {
        this.node = node;
        this.weight = weight;
    }

    public int getNode() { return this.node; }
    public int getWeight() { return this.weight; }

    public void setNode(int node) {
        this.node = node;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
