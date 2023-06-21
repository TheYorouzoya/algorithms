package Literals;
/*
 * A custom min-heap data structure to be used in conjunction with Dijkstra's shortest
 * path algorithm. This heap is one-indexed (rather than zero-indexed) for simplicity
 * of operations. The keys store the current Dijkstra's greedy score for a given vertex.
 * Additionally, it keeps track of the mapping between keys and their respective nodes
 * to support fast lookups for deletion operations.
 */

import java.util.ArrayList;
import java.util.HashMap;


public class CustomHeap {
    private ArrayList<Integer> keys;
    private HashMap<Integer, Integer> indexToNodeMapping;
    private HashMap<Integer, Integer> nodeToIndexMapping;

    public CustomHeap(int N) {
        // Initialize a heap of given node capacity.
        keys = new ArrayList<Integer>();
        indexToNodeMapping = new HashMap<>(N);
        nodeToIndexMapping = new HashMap<>(N);

        for(int i = 0; i <= N; i++) {
            keys.add(Integer.MAX_VALUE);
            indexToNodeMapping.put(i, i);
            nodeToIndexMapping.put(i, i);
        }
    }

    public int size() {
        return keys.size();
    }

    public boolean isEmpty() {
        return keys.size() == 1;
    }

    public int parent(int pos) {
        // return the parent's index of a given node
        if (pos <= 1) {
            throw new ArithmeticException("Invalid child position");
        } else {
            return pos / 2;
        }
    }

    public int leftChild(int pos) {
        // return the left child's index of a given parent
        if (pos < 1 || (pos * 2) > keys.size()) {
            throw new ArithmeticException("Invalid parent position");
        } else {
            return pos * 2;
        }
    }

    public int rightChild(int pos) {
        // return the right child's index of a given parent
        if (pos < 1 || ((pos * 2) + 1) > keys.size()) {
            throw new ArithmeticException("Invalid parent position");
        } else {
            return (pos * 2) + 1;
        }
    }

    public void heapifyUp(int index) {
        // bubble up a heap violation (child < parent) until it is resolved
        while(index > 1) {
            // repeat until violation reaches root
            
            int child = keys.get(index);
            int parentIndex = parent(index);
            int parent = keys.get(parentIndex);

            if (parent <= child) return; // stop if resolved
            else {
                // continue swapping upward
                swap(index, parentIndex);
                index /= 2;
            }
        }        
    }

    public void heapifyDown(int index) {
        // bubble-down a heap violation (parent > child) until it is resolved
        while(((index * 2) + 1) < keys.size()) {
            int parent = keys.get(index);
            int leftChildIndex, rightChildIndex;

            try {
                leftChildIndex = leftChild(index);
            } catch (ArithmeticException e) {
                // left child does not exist, stop
                return;
            }
            int leftChild = keys.get(leftChildIndex);
            
            try {
                rightChildIndex = rightChild(index);
            } catch (ArithmeticException e) {
                // right child does not exist, swap with left child and stop
                swap(index, leftChildIndex);
                return;
            }
            int rightChild = keys.get(rightChildIndex);
            
            // stop if heap violation is restored
            if (parent <= leftChild && parent <= rightChild) return;
            // else swap with smaller child and continue downward
            else if (leftChild < rightChild) {
                swap(index, leftChildIndex);
                index = index * 2;
            } else {
                swap(index, rightChildIndex);
                index = (index * 2) + 1;
            }
        }
    }

    public void swap(int indexA, int indexB) {
        // swap to nodes in the heap and update the mappings
        int nodeA = indexToNodeMapping.get(indexA);
        int nodeB = indexToNodeMapping.get(indexB);

        // swap the keys
        int temp = keys.get(indexA);
        keys.set(indexA, keys.get(indexB));
        keys.set(indexB, temp);
        
        // swap node mapping
        nodeToIndexMapping.put(nodeA, indexB);
        nodeToIndexMapping.put(nodeB, indexA);

        //swap index mapping
        indexToNodeMapping.put(indexA, nodeB);
        indexToNodeMapping.put(indexB, nodeA);
    }

    public int peekMinNode() {
        // Minimum key is always at index 1
        return indexToNodeMapping.get(1);
    }

    public int extractMinKey() {
        // Swap minimum key with last element in the heap
        int lastIndex = keys.size() - 1;
        int minKey = keys.get(1);
        swap(1, lastIndex);

        // Remove the last element from heap and restructure
        keys.remove(lastIndex);
        heapifyDown(1);

        return minKey;
    }

    public void insertNode(int node, int key) {
        // Add key to heap
        keys.add(key);
        int nodeIndex = keys.size() - 1;

        // Add new entry to mappings
        nodeToIndexMapping.put(node, nodeIndex);
        indexToNodeMapping.put(nodeIndex, node);

        // Move node to the proper place
        heapifyUp(nodeIndex);
    }

    public int peekNode(int node) {
        // return the key of a particular node
        return keys.get(nodeToIndexMapping.get(node));
    }

    public void decreaseKey(int node, int key) {
        // change the value of a node's key in the heap to a smaller one
        int nodeIndex = nodeToIndexMapping.get(node);
        if (key < keys.get(nodeIndex)) { // only proceed if key is smaller
            keys.set(nodeIndex, key);
            heapifyUp(nodeIndex);
        }
    }

    public void deleteNode(int node) {
        int index = nodeToIndexMapping.get(node);
        int lastIndex = keys.size() - 1;
        swap(index, lastIndex);
        keys.remove(lastIndex);
    }

    public static void main(String[] args) {
        
    }

}
