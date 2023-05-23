import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/*
 * Program to implement the Randomized Selection Algorithm to find the i'th order statistic
 * of a given input array of integers.
 * [Average] Running Time: O(n)
 * 
 * =================
 * The Algorithm:
 * =================
 * 
 * INPUT: An unsorted array (A) of integers of length n. Left, right, and equal
 *        (left, right, equal) positions within the array and the ordered statistic
 *        i to be computed.
 * OUTPUT: The ith order statistic of the given input array
 * STEPS:   1. If n = 1, return A[1]
 *          2. Choose pivot from A uniformly at random
 *          3. Partition A around p
 *              let j = new index of p
 *          4. If j = i, return p
 *          5. If j > i, return Rselect(1st part of A, j - 1, i)
 *          6. If j < i, return Rselect(2nd part of A, n-j, i-j)
 * 
 */

public class RSelect {

    private int partitionArray(ArrayList<Integer> inputArray, int left, int right) {
        // Generate random pivot index
        int pivotIndex = ThreadLocalRandom.current().nextInt(left, right + 1);
        
        // Swap pivot to the left
        int pivot = inputArray.get(pivotIndex);
        int temp = inputArray.get(left);
        inputArray.set(left, pivot);
        inputArray.set(pivotIndex, temp);
        
        int boundary = left + 1;

        for(int scanner = left + 1; scanner <= right; scanner++) {
            if(inputArray.get(scanner) < pivot) {
                // Swap element if it is less than the pivot
                temp = inputArray.get(boundary);
                inputArray.set(boundary, inputArray.get(scanner));
                inputArray.set(scanner, temp);
                boundary++;
            }
        }
        // Swap pivot to proper location
        inputArray.set(left, inputArray.get(boundary - 1));
        inputArray.set((boundary - 1), pivot);
        
        // Return final pivot position
        return boundary - 1;
    }

    public int findOrderedStatistic(ArrayList<Integer> inputArray, int left, int right, int statistic) {
        if (left >= right) {
            return left;
        } else {
            int pivot = partitionArray(inputArray, left, right);

            if (pivot == statistic) {
                return pivot;
            } else if (pivot > statistic) {
                // Look in the left half of the partitioned array
                return findOrderedStatistic(inputArray, left, pivot - 1, statistic);
            } else {
                // Look in the right half of the partitioned array
                return findOrderedStatistic(inputArray, pivot + 1, right, statistic);
            }
        }
    }

    private ArrayList<Integer> loadIntegerArrayFromFile(String inputFileName) {
        // Load a given array from file into a list object and return it
        ArrayList<Integer> inputArray = new ArrayList<>();
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
        RSelect obj = new RSelect();
        int statistic = 6;
        ArrayList<Integer> inputArray = new ArrayList<>();
        inputArray = obj.loadIntegerArrayFromFile("testArray.txt");
        int index = obj.findOrderedStatistic(inputArray, 0, inputArray.size() - 1, statistic - 1);
        
        System.out.println(statistic + "th Order Statistic for the given input array is: " + inputArray.get(index));
    }   
}