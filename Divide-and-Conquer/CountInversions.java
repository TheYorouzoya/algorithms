import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/*
 * Program to count the number of inversions in a given array of size n.
 * Running Time: O(n log n)
 *
 * ================
 * The Algorithm:
 * ================
 * INPUT: array A of n distinct integers.
 * OUTPUT: sorted array B with the same integers, and the number of 
 *         inversions of A.
 * STEPS:
 *  - if n = 0 or n = 1, then return (A, 0), else
 *  - Divide the array into two halves C and D
 *  - Recursively sort and count inversions in the two halves
 *  - Merge and Count the split inversions in the two halves and,
 *  - Return the total number of inversions (two halves + split) along with the sorted 
 *    array as (inversions, B).
 *
 * ===============================================
 * Merge and Count Split Inversions Subroutine:
 * ===============================================
 * INPUT: sorted arrays C and D (length n/2 each).
 * OUTPUT: sorted array B (length n) and the number of split inversions.
 * STEPS:
 *  - start merging the two halves as you would in a merge sort
 *  - for each inverted pair found in the left half,
 *  - add the number of remaining elements in the left half to the number of split
 *    inversions so far
 *  - repeat until both halves have been merged into a sorted array B
 *  - return (B, number of inversions)
 */

public class CountInversions {

    private static class InversionPair {
        // custom pair object to hold the number of inversions and the sorted array together
        private final long inversions;
        private final List<Integer> sortedArray;

        public InversionPair(long inversions, List<Integer> sortedArray) {
            this.inversions = inversions;
            this.sortedArray = sortedArray;
        }

        public long getInversions() { return inversions; }
        public List<Integer> getSortedArray() { return sortedArray; }

    }

    private InversionPair countSplitInversions(List<Integer> firstHalf, List<Integer> secondHalf) {
        List<Integer> sorted = new ArrayList<>();
        int first, second, length;
        long splitInversions = 0;
        first = firstHalf.size();
        second = secondHalf.size();
        length = first + second;

        for (int k = 0, i = 0, j = 0; k < length; k++) {
            if (i >= first) { // If first half runs out
                sorted.add(secondHalf.get(j));
                j++;
            } else if (j >= second) { // If second half runs out
                sorted.add(firstHalf.get(i));
                i++;
            } else {
                if (firstHalf.get(i) > secondHalf.get(j)) {
                    // An inversion is detected while merging
                    sorted.add(secondHalf.get(j));
                    splitInversions += first - i;   // Add the number of elements left in the first half to inversions
                    j++;
                } else {
                    sorted.add(firstHalf.get(i));
                    i++;
                }
            }
        }

        return new InversionPair(splitInversions, sorted);
    }
    
    public InversionPair countArrayInversions(List<Integer> inputArray) {
        long totalInversions = 0;
        int len = inputArray.size();
        if (len <= 1) { // base case
            return new InversionPair(totalInversions, inputArray);
        } else {
            InversionPair firstHalf = countArrayInversions(inputArray.subList(0, len / 2));  // inversions in first half
            InversionPair secondHalf = countArrayInversions(inputArray.subList(len / 2, len));  // inversions in second half
            InversionPair splitInversions = countSplitInversions(firstHalf.getSortedArray(), secondHalf.getSortedArray());
            
            totalInversions = firstHalf.getInversions() + secondHalf.getInversions() + splitInversions.getInversions();

            return new InversionPair(totalInversions, splitInversions.getSortedArray());
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
        String inputFileName = "IntegerArray.txt";
        CountInversions objCountInversions = new CountInversions();

        List<Integer> inputArray = objCountInversions.loadIntegerArrayFromFile(inputFileName);
        InversionPair test = objCountInversions.countArrayInversions(inputArray); 
        long inversions = test.getInversions();

        System.out.println("The number of inversions in the given array is " + inversions);
    }
}
