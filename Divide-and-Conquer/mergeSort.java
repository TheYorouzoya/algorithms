import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/*
 * Program to implement the Merge Sort routine
 * Running Time: O(n log n)
 * 
 * =================
 * The Algorithm:
 * =================
 * INPUT: array A of n distinct integers.
 * OUTPUT: sorted array with the same integers, sorted in ascending order
 * STEPS:
 *  1 if input array is of size <= 1, return the array as is
 *  2 else, recursively sort the first half of A
 *  3 recursively sort the second half of A
 *  4 "Merge" the two halves and return the result
 * 
 * ====================
 * Merge Subroutine:
 * ====================
 * INPUT: sorted arrays C and D
 * OUTPUT: sorted array B
 * STEPS:
 *  1 set pointers (i, j) and k for the two halves (C, D) and outpur array B
 *  2 if either array runs of of elements, append the rest of the remaining array
 *    to the result and return
 *  3 compare the ith element of C with jth element of D
 *      - if C[i] < D[j] then
 *          B[k] := C[i]
 *          increment i
 *      - else
 *          B[k] := D[j]
 *          increment j
 *  4 repeat until you run out of elements in either arrays
 *  5 return the sorted array B
 */

 public class mergeSort {
    
    private List<Integer> merge (List<Integer> firstHalf, List<Integer> secondHalf) {
        List<Integer> sorted = new ArrayList<>();
        int first, second, length;
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
            } else { // Merge in ascending order
                if (firstHalf.get(i) > secondHalf.get(j)) {
                    sorted.add(secondHalf.get(j));
                    j++;
                } else {
                    sorted.add(firstHalf.get(i));
                    i++;
                }
            }
        }
        return sorted;
    }

    private List<Integer> sortArray (List<Integer> inputList) {
        int len = inputList.size();
        if (len <= 1) { // base case
            return inputList;
        } else {
            List<Integer> firstHalf = sortArray(inputList.subList(0, len / 2)); // sort the first half
            List<Integer> secondHalf = sortArray(inputList.subList(len / 2, len)); // sort the second half
            List<Integer> finalOutput = merge(firstHalf, secondHalf); // merge
            return finalOutput;
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

    private void writeIntegerArrayToFile(String outputFileName, List<Integer> integerArray) {
        // Write the sorted array to a given file
        try (PrintWriter outputFile = new PrintWriter(new FileWriter(outputFileName))) {
            integerArray.stream().forEach(listInteger -> outputFile.println(listInteger));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args) {
        String inputFileName = "IntegerArray.txt";
        String outputFileName = "testArraySorted.txt";
        mergeSort objMergeSort = new mergeSort();

        List<Integer> inputArray = objMergeSort.loadIntegerArrayFromFile(inputFileName);
        List<Integer> outputData = objMergeSort.sortArray(inputArray);

        objMergeSort.writeIntegerArrayToFile(outputFileName, outputData);
    }
 }