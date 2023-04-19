import java.util.ArrayList;
import java.util.List;

/*
 * Program to implement the Merge Sort routine
 */

 public class mergeSort {
    
    private List<Integer> merge (List<Integer> firstHalf, List<Integer> secondHalf) {
        List<Integer> sorted = new ArrayList<>();
        int i = 0, j = 0, first, second, n;
        first = firstHalf.size();
        second = secondHalf.size();
        n = first + second;

        for (int k = 0; k < n; k++) {
            if (i >= first) {
                sorted.add(secondHalf.get(j));
                j++;
            }else if (j >= second) {
                sorted.add(firstHalf.get(i));
                i++;
            } else {
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

    private List<Integer> mSort (List<Integer> inputList) {
        /*
         * Merge Sort algorithm :
         *  - if array size is one, return
         *  - else, split the array in two parts
         *  - sort both halves
         *  - merge the sorted halves
         * 
         * Merge subroutine:
         *  - comparre the first elements of the two sub-arrays
         *  - depending on the order (ascending or descending), slot the appropriate element in the final array
         *  - continue until either sub-array runs out of elements
         *  - once a sub-array runs out of elements, slot the rest of the other sub-array into the final arary
         *  - done.
         */
        int len = inputList.size();
        if (len <= 1) { // base case
            return inputList;
        } else {
            List<Integer> firstHalf = mSort(inputList.subList(0, len / 2)); // sort the first half
            List<Integer> secondHalf = mSort(inputList.subList(len / 2, len)); // sort the second half
            List<Integer> finalOutput = merge(firstHalf, secondHalf); // merge
            return finalOutput;
        }
    }

    public static void main (String[] args) {
        List<Integer> test = List.of(1, 2, 5, 9, 3, 10, 4, 11, 6, 7, 8, 20,
                                    21, 50, 56, 33, 11, 23, 45, 32, 124, 26);
        mergeSort objMergeSort = new mergeSort();
        System.out.println(objMergeSort.mSort(test));
        System.out.println(test);
    }
 }