public class Edge {
    private int nodeA, nodeB, cost;

    public Edge(int nodeA, int nodeB, int cost) {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.cost = cost;
    }

    public int getNodeA() { return nodeA; }
    public int getNodeB() { return nodeB; }
    public int getCost() {return cost; }
    
}
