/*
 * Program to solve the median maintenance problem.
 * The program maintains a running mediam as integers are fed into it via a stream (filename)
 * and returns the sum of all medians modulo number of integers at the end.
 * So, if medians for n integers are m1, m2, m3,..., mn respectively, as the integers are being
 * fed from the stream, the program will return the value of:
 * (m1 + m2 + m3 + ... + mn) modulo n
 * 
 * =================
 * The Algorithm:
 * =================
 * 
 * INPUT: A stream of integers in the form of a file
 * 
 * OUTPUT: The sum of the running medians: (m1 + m2 + m3 + ... + mn) modulo n
 * 
 * STEPS: - We maintain two heaps: a max-heap on the left and a min-heap on the right
 *        - As the integers are pulled from the stream, we insert them into the left heap if
 *          they're less than the heap's max value, otherwise we insert them into the right heap.
 *        - After every insertion, we check if the heaps require balancing as follows:
 *              - If the size difference between the two heaps exceeds 1,
 *                  - ExtractMin or ExtractMax depending on whichever heap is larger
 *                  - Insert the extracted integer into the other heap
 *              - Else do nothing
 *        - Once the heaps are balanced, return the median as follows,
 *              - If the right heap is larger, return the minimum integer from it
 *              - Otherwise, return the maximum integer from the left heap
 *        - Keep a running total of and a counter for these medians and once the stream stops,
 *          return (sum modulo counter).
 */

import Literals.MaxHeap;
import Literals.MinHeap;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class MedianMaintenance {
    private MaxHeap leftHeap;
    private MinHeap rightHeap;

    public MedianMaintenance() {
        // Intialize heaps with a dummy intial value
        rightHeap = new MinHeap();
        leftHeap = new MaxHeap();

        rightHeap.insert(Integer.MAX_VALUE);
        leftHeap.insert(Integer.MIN_VALUE);
    }

    // Function to balance the heap if the size difference between the two heaps is > 2
    public void balanceHeaps() {
        int rightSize = rightHeap.size();
        int leftSize = leftHeap.size();

        if (Math.abs(rightSize - leftSize) <= 1) { // Return early if no need to balance
            return;
        } else if (rightSize > leftSize) { // If right heap is bigger
            // Extract from right and insert into left heap
            leftHeap.insert(rightHeap.extractMinKey());
        } else { // If left heap is bigger
            // Extract from left and insert into right heap
            rightHeap.insert(leftHeap.extractMaxKey());
        }
    }

    // Function that returns the current median
    public int currentMedian() {
        int rightSize = rightHeap.size();
        int leftSize = leftHeap.size();

        if (leftSize < rightSize) { // If right heap is bigger
            return rightHeap.peekMin();
        } else { // If left heap is bigger or the two heaps are equal in size
            return leftHeap.peekMax();
        }
    }

    // Function that returns the median after inserting and integer
    public int insertAndReturnMedian(int number) {
        if(number < rightHeap.peekMin()) { // Insert in left if number < right heap
            leftHeap.insert(number);
        } else { // Insert in right if number > left heap
            rightHeap.insert(number);
        }
        balanceHeaps();
        return currentMedian();
    }

    public static void main(String[] args) {
        String filename = args[0];
        MedianMaintenance obj = new MedianMaintenance();
        long medianSum = 0;
        int currentMedian;
        int count = 0;

        try (Scanner scanner = new Scanner(new FileReader(filename))) {
            while (scanner.hasNext()) {
                currentMedian = obj.insertAndReturnMedian(scanner.nextInt());
                medianSum += currentMedian;
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(medianSum % count);
    }
}
