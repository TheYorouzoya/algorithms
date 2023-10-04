package DataStructures;

import java.util.ArrayList;

public class GenericMinHeap<Key extends Comparable<Key>> {
    private ArrayList<Key> heap;
    private int size = 0;

    // Initialize heap of given size
    public GenericMinHeap(int size) {
        heap = new ArrayList<Key>(size + 1);
        heap.add(null);
    }

    // Return true if heap is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Return heap size
    public int size() {
        return size;
    }

    // Insert Key into heap and restructure
    public void insert(Key k) {
        heap.add(k);
        size++;
        heapifyUp(size);
    }

    // Recursively swap Key with parent until key > parent
    public void heapifyUp(int index) {
        while (index > 1 && less(index, index / 2)) {
            swap(index, index / 2);
            index = index / 2;
        }
    }

    // Recursively swap parent with child until parent < child
    public void heapifyDown(int index) {
        while(2 * index <= size) {
            int childIndex = index * 2;
            // set childIndex to be the smaller of two children
            if (childIndex < size && less(childIndex + 1, childIndex)) childIndex++;
            // stop if parent < child
            if (less(index, childIndex)) break;
            // otherwise, swap and continue
            swap(index, childIndex);
            index = childIndex;
        }
    }
    
    // Remove the minimum key and return it
    public Key extractMin() {
        // store minimum key
        Key min = heap.get(1);
        // swap with the last index and set it to null
        swap(1, size);
        heap.remove(size--);
        // restructure heap
        heapifyDown(1);
        return min;
    }

    // Return true if Key at index1 is smaller than that at index2
    public boolean less(int index1, int index2) {
        Key first = heap.get(index1);
        Key second = heap.get(index2);
        return first.compareTo(second) < 0;
    }

    // Swap keys ate two given indices
    public void swap(int index1, int index2) {
        Key temp = heap.get(index1);
        heap.set(index1, heap.get(index2));
        heap.set(index2, temp);
    }

    public void printHeap() {
        System.out.print("Heap is: ");
        for(int i = 1; i <= size; i++) System.out.print(heap.get(i) + " ");
        System.out.println();
    }

    public static void main(String[] args) {
        GenericMinHeap<Integer> heap = new GenericMinHeap<Integer>(5);
        int[] array = { 4, 9, 12, 11, 20 };
        for(int number : array) {
            heap.insert(number);
        }

        heap.printHeap();

        for(int i = 0; i < array.length; i++) {
            System.out.println("Minimum is: " + heap.extractMin());
            // heap.printHeap();
        }
    }
}
