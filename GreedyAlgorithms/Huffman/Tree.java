/*
 * A binary tree to be used in conjunction with the Huffman class for encoding labels.
 * The inner class "Node" class encapsulates a symbol/label as a graph node with
 * ID as symbol's index on file and Weight as the symbol's weight.
 * The symbols are always on leaves while the nodes connect other trees together.
 */

import java.util.ArrayList;
import java.util.List;

public class Tree implements Comparable<Tree> {

    private Node root;

    // Inner class
    private class Node {
        int ID;
        int weight;
        Node left, right;

        // Constructor for symbol/labels at leaves
        public Node(int ID, int weight, Node left, Node right) {
            this.ID = ID;
            this.weight = weight;
            this.left = left;
            this.right = right;
        }

        // Constructor for tree nodes
        public Node(int weight, Node left, Node right) {
            this.ID = -1;
            this.weight = weight;
            this.left = left;
            this.right = right;
        }

    }

    // Constructor for merging two trees
    private Tree(int weight, Node left, Node right) {
        this.root = new Node(weight, left, right);
    }

    // Constuctor for a symbol/label as standalone tree
    public Tree(int ID, int weight) {
        this.root = new Node(ID, weight, null, null);
    }

    // Static function that merges two trees
    public static Tree Merge(Tree treeA, Tree treeB) {
        int weight = treeA.root.weight + treeB.root.weight;
        return new Tree(weight, treeA.root, treeB.root);
    }

    // Return weight of a tree
    public int Weight() { return this.root.weight; }

    // Compare trees based on their weight
    @Override
    public int compareTo(Tree other) {
        if(this.root.weight < other.root.weight) return -1;
        if(this.root.weight > other.root.weight) return +1;
        return 0;
    }

    // Recursive function that populates given string array with encoded strings
    public void generateEncodes(Node node, List<String> encodes, String currentCode) {
        if(node.left == null || node.right == null) {
            // If leave is reached, append current encoded string to array
            encodes.set(node.ID, currentCode);
            return;
        }
        // Append 0 to the current encoded string if traversing left
        generateEncodes(node.left, encodes, currentCode + "0");
        // Append 1 to the current encoded string if traversing right
        generateEncodes(node.right, encodes, currentCode + "1");
    }

    // Function that populates encoding Strings corresponding to the symbol indices
    public List<String> getEncodingList(int nodes) {
        // Initialize array with empty strings
        List<String> encodes = new ArrayList<String>();
        for(int i = 0; i <= nodes; i++) {
            encodes.add("");
        }

        // Populate entries
        generateEncodes(root, encodes, "");

        return encodes;
    }
}
