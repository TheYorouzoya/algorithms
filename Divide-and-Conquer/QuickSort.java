import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * Program to implement the Quick Sort routine
 * [Average] Running Time: O(n log n)
 * 
 * ==================
 * The Algorithm:
 * ==================
 * 
 * INPUT: An unsorted array of integers, A, of length n. Left
 *        and right endpoints l, r within the array.
 * POSTCONDITION: The same input array but sorted in ascending order within the
 *                range [l, r].
 * STEPS: 1. If l >= r, return. Else,
 *        2. "Choose a pivot element" P
 *        3. "Partition A" around P
 *        4. Recursively sort first part of A
 *        5. Recursively sort second part of A
 *        
 * 
 * 
 * ==============================
 * Pivot Selection Sub-Routine:
 * ==============================
 * 
 * - In randomized Quick Sort, the pivot is chosen at random as that gives us an average 
 *   running time of O(n log n).
 * - The ideal pivot would be the mathematical median of the given input array.
 * - A middle ground between completely random and median is generally used where one 
 *   takes a small random sample from the input array and selects a pivot from that sample.
 * 
 * This implementation uses THREE different selection criteria as specified in the
 * assignment.
 *  1. Always the first element of the array.
 *  2. Always the last element of the array.
 *  3. Median-of-three between the first, last, and the middle element of the array.
 * 
 * ========================
 * Partition Sub-Routine:
 * ========================
 * 
 * INPUT: Unsorted array, A, with ranges "left" and "right".
 * OUTPUT: Array elements partitioned around the given pivot where all the elements
 *         to the left of the array are smaller than the pivot and all the elements
 *         to the right of the array are bigger than the pivot.
 * STEPS: 1. Place the Pivot at the "left" location.
 *        2. Initialize variables "boundary" and "scanner" to "left".
 *        3. Until the scanner reaches "right", repeat the following steps
 *              - Compare the element at the scanner's index with the pivot
 *              - If the element is less than the pivot
 *                  - swap it with the element at the "boundary" index
 *                  - increment boundary by 1
 *              - Else, do nothing.
 *              - increment scanner
 *        4. Swap the pivot with the element at (boundary - 1) index.
 *        5. Return the pivot's index.
 * 
 */

public class QuickSort {
    public int comparisons = 0;

    private int partitionArray(List<Integer> inputArray, int left, int right) {
        int pivot = inputArray.get(left);
        int boundary = left + 1;

        for (int scanner = left + 1; scanner <= right; scanner++) {
            if (inputArray.get(scanner) < pivot) {
                int temp = inputArray.get(boundary);
                inputArray.set(boundary, inputArray.get(scanner));
                inputArray.set(scanner, temp);
                boundary++;
            }
        }
        inputArray.set(left, inputArray.get(boundary - 1));
        inputArray.set((boundary - 1), pivot);
        return (boundary - 1);
    }

    public void sortArray(List<Integer> inputArray, int left, int right) {
        if (left >= right) { // Base case
            return;
        } else {
            int pivot = partitionArray(inputArray, left, right);
            comparisons += right - left;

            sortArray(inputArray, left, pivot - 1);
            sortArray(inputArray, pivot + 1, right);
        }
    }

    private List<Integer> loadIntegerArrayFromFile(String inputFileName) {
        // Load a given array from file into a list object and return it
        List<Integer> inputArray = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(inputFileName))) {
            while (scanner.hasNext()) {
                inputArray.add(scanner.nextInt());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputArray;
    }

    public static void main(String[] args) {
        QuickSort obj = new QuickSort();
        List<Integer> inputArray = new ArrayList<>();
        inputArray = obj.loadIntegerArrayFromFile("quickSortTestArray.txt");
        obj.sortArray(inputArray, 0, inputArray.size() - 1);
        System.out.println("Comparisons: " + obj.comparisons);
    }
}