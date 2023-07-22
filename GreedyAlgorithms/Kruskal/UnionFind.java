
public class UnionFind {
    private int[] parent;
    private int[] rank;
    
    public UnionFind(int N) {
        this.parent = new int[N + 1];
        this.rank = new int[N + 1];

        for(int i = 0; i <= N; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    // Return true if the union is successful, otherwise false
    public boolean union(int nodeA, int nodeB) {
        int parentA, parentB;
        // Get parents for both nodes
        parentA = find(nodeA);
        parentB = find(nodeB);
        
        // If both nodes have the same parent
        if (parentA == parentB) { return false; }

        // Attach the smaller tree to the bigger one
        if (rank[parentA] < rank[parentB]) {
            parent[parentA] = parentB;
        } else if (rank[parentB] < rank[parentA]) {
            parent[parentB] = parentA;
        } else { // If both trees are equal, merge first into second and update rank
            parent[parentA] = parentB;
            rank[parentB] += 1;
        }
        return true;
    }

    // Function to find the parent of a given node
    public int find(int node) {
        // If node itself is the parent
        if (parent[node] == node) { return node; }

        // Recursively find the parent and do path compression
        return parent[node] = find(parent[node]);
    }

    public void printSet() {
        for(int i : parent) {
            System.out.print(i + ", ");
        }
        System.out.println();
    }

    public void printRanks() {
        for(int i : rank) {
            System.out.print(i + ", ");
        }
        System.out.println();
    }

    public void printParents() {
        int counter = 0;
        for (int i = 1; i < parent.length; i++) {
            if(i == parent[i]) {
                // System.out.print(i + ", ");
                counter++;
            }
        }
        // System.out.println();
        System.out.println("Number of leaders: " + counter);
    }
}
