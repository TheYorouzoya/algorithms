package DataStructures;

import java.util.ArrayList;

public class MaxHeap {
    private ArrayList<Integer> keys;

    public MaxHeap() {
        keys = new ArrayList<Integer>();
        keys.add(0);
    }

    public MaxHeap(int N) {
        // Initialize a heap of given node capacity.
        keys = new ArrayList<Integer>(N + 1);
        keys.add(0);
    }

    public int size() {
        return keys.size() - 1;
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
        // bubble up a heap violation (parent < child) until it is resolved
        while (index > 1) {
            // repeat until violation reaches root

            int child = keys.get(index);
            int parentIndex = parent(index);
            int parent = keys.get(parentIndex);

            if (parent >= child)
                return; // stop if resolved
            else {
                // continue swapping upward
                swap(index, parentIndex);
                index /= 2;
            }
        }
    }

    public void heapifyDown(int index) {
        // bubble-down a heap violation (parent < child) until it is resolved
        while (((index * 2) + 1) < keys.size()) {
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
            if (parent >= leftChild && parent >= rightChild)
                return;
            // else swap with larger child and continue downward
            else if (leftChild > rightChild) {
                swap(index, leftChildIndex);
                index = index * 2;
            } else {
                swap(index, rightChildIndex);
                index = (index * 2) + 1;
            }
        }
    }

    public void swap(int indexA, int indexB) {
        // swap the keys
        int temp = keys.get(indexA);
        keys.set(indexA, keys.get(indexB));
        keys.set(indexB, temp);
    }

    public int peekMax() {
        // Maximum key is always at index 1
        return keys.get(1);
    }

    public int extractMaxKey() {
        // Swap maximum key with last element in the heap
        int lastIndex = keys.size() - 1;
        int maxKey = keys.get(1);
        swap(1, lastIndex);

        // Remove the last element from heap and restructure
        keys.remove(lastIndex);
        heapifyDown(1);

        return maxKey;
    }

    public void insert(int key) {
        // Add key to heap
        keys.add(key);
        int nodeIndex = keys.size() - 1;

        // Move node to the proper place
        heapifyUp(nodeIndex);
    }

    public static void main(String[] args) {

    }
}
