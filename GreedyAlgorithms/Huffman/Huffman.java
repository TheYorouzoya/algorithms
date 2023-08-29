/*
 * Program to implement the Huffman's encoding algorithm.
 * We are provided with symbols and their weights (rather than frequencies) as input.
 * Running Time: O(n log n)
 */

import java.util.List;
import java.util.PriorityQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Huffman {
    private List<Tree> symbols;
    private int symbolCount;
    
    // Constructor to load symbols from file
    public Huffman(String filename) {
        // File Structure is as follows:
        // [number_of_symbols]
        // [weight_of_symbol_#1]
        // [weight_of_symbol_#2]

        symbols = new ArrayList<Tree>();

        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int counter = 1;

            if((line = reader.readLine()) == null) {
                throw new IOException("Empty file");
            } else {
                symbolCount = Integer.parseInt(line);
            }
            while((line = reader.readLine()) != null) {
                symbols.add(new Tree(counter++, Integer.parseInt(line)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Tree processSymbols() {
        // Initialize and populate heap
        PriorityQueue<Tree> heap = new PriorityQueue<>();
        for(Tree symbol : symbols) { heap.add(symbol); }

        // While heap has more than one symbol
        while(heap.size() > 1) {
            // Extract top two minimum symbols/trees
            Tree symbolA = heap.poll();
            Tree symbolB = heap.poll();
            // Merge the two into a tree
            Tree merged = Tree.Merge(symbolA, symbolB);
            // Insert back into heap
            heap.add(merged);
        }

        // Return the last remaining tree
        return heap.poll();
    }

    public static void main(String[] args) {
        String filename = args[0];
        Huffman obj = new Huffman(filename);
        Tree symbolTree = obj.processSymbols();
        List<String> symbols = symbolTree.getEncodingList(obj.symbolCount);

        int min = Integer.MAX_VALUE, max = 0;
        for(int i = 1; i < symbols.size(); i++) {
            String bitString = symbols.get(i);
            if(bitString.length() < min) min = bitString.length();
            if(bitString.length() > max) max = bitString.length();
        }

        System.out.println("Min: " + min);
        System.out.println("Max: " + max);
    }

}
