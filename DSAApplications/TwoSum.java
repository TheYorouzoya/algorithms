/*
 * Program to solve the two sums problem where the sums to be computed lie
 * within a given interval T.
 * The integers are well above the int limit, so the program uses long. The total
 * number of integers in the file are 1 million. The range for the sums lies in the
 * interval [-10000, 10000] including both ends.
 * 
 * Since the problem differs from the classic TwoSums where only a single sum needs to be
 * computed, the original HashTable solution would not yield an optimal solution as
 * we'd have to perform N lookups for every sum in the given range. If the total number of
 * sums is T, then the running time for the HashTable solution would be O(T * N).
 * 
 * The balanced search tree solution here performs better for the given range as the
 * O(logn) lookups works out better for an O(n logn) running time where logn << T.
 * In this case, T is 20001 while logn is 6, so the running time sees a substantial
 * improvement compared to the usual HashTable solution.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class TwoSum {

    // Load a given filename into a list on memory
    public static List<Long> loadFile (String filename) {
        List<Long> array = new ArrayList<Long>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                array.add(Long.parseLong(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return array;
    }

    // Returns a java TreeMap instance of a given list of integers discarding any duplicates
    // Here, a key corresponds to the integer stored and the boolean value keeps track of
    // whether or not all possible two sums have been computed for this integer
    public static TreeMap<Long, Boolean> returnTreeMap (List<Long> array) {
        TreeMap<Long, Boolean> tree = new TreeMap<>();
        for (long number : array) {
            tree.put(number, false);
        }
        return tree;
    }

    // Count the number of sums (T), within a given interval, which can be computed
    // using the integers x and y from a given array
    public static int countTwoSumsInRange (List<Long> array,  
                                            int leftRange, 
                                            int rightRange) {
        
        // A HashSet to keep track of unique sums
        HashSet<Integer> TSet = new HashSet<>();
        // A Search tree to perform lookups
        TreeMap<Long, Boolean> tree = returnTreeMap(array);

        for (long number : array) { // For every number in list
            if (!tree.get(number)) { // If twosum hasn't been computed
                long left, right;

                // compute the bounds for the all possible sums within the range
                left = leftRange - number;
                right = rightRange - number;

                // Get a subtree for the computed range
                Map<Long, Boolean> subTree = tree.subMap(left, true, right, true);

                // Add all subtree elements to the HashSet
                if (subTree.size() > 0) {
                    for (Long key : subTree.keySet()) {
                        int T = (int) (number + key);
                        TSet.add(T);
                    }
                }
                // Update the number's boolean instance in the tree
                tree.put(number, true);
            }
        }
        return TSet.size();
    }

    public static void main(String[] args) {
        String filename = args[0];
        int leftRange = -10000, rightRange = 10000;

        List<Long> array = loadFile(filename);
        int twoSums = countTwoSumsInRange(array, leftRange, rightRange);

        System.out.println("Count is: " + twoSums);
    }
}
