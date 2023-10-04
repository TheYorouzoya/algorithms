import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Knapsack {
    private Item[] itemList;
    private int items;
    private int capacity;
    
    private class Item {
        int weight;
        int value;

        public Item(int weight, int value) {
            this.weight = weight;
            this.value = value;
        }
    }

    public Knapsack(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            String[] pair;
            
            if ((line = reader.readLine()) == null) {
                throw new IOException("Given file " + filename + " is empty");
            }

            pair = line.split(" ");
            capacity = Integer.parseInt(pair[0]);
            items = Integer.parseInt(pair[1]);

            itemList = new Item[items + 1];
            int index = 1;

            while((line = reader.readLine()) != null) {
                pair = line.split(" ");
                itemList[index++] = new Item(Integer.parseInt(pair[1]), Integer.parseInt(pair[0]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int processSmallKnapsack() {
        int[][] knapsack = new int[items + 1][capacity + 1];

        for(int i = 0; i <= capacity; i++) knapsack[0][i] = 0;

        for(int i = 1; i <= items; i++) {
            for(int c = 0; c <= capacity; c++) {
                if(itemList[i].weight > c) {
                    knapsack[i][c] = knapsack[i - 1][c];
                } else {
                    knapsack[i][c] = Math.max(knapsack[i - 1][c], 
                                              knapsack[i - 1][c - itemList[i].weight] + itemList[i].value);
                }
            }
        }

        return knapsack[items][capacity];
    }

    public int processBigKnapsack() {
        int[] cache = new int[capacity + 1];

        for(int i = 1; i <= items; i++) {
            int[] current = new int[capacity + 1];
            for(int c = 0; c <= capacity; c++) {
                if(itemList[i].weight > c) {
                    current[c] = cache[c];
                } else {
                    current[c] = Math.max(cache[c], cache[c - itemList[i].weight] + itemList[i].value);
                }
            }
            cache = current;
        }
        return cache[capacity];
    }

    public static void main(String[] args) {
        String filename = args[0];
        Knapsack obj = new Knapsack(filename);
        // System.out.println("Optimal solutions for small knapsack is: " + obj.processSmallKnapsack());
        long start, stop;
        start = System.currentTimeMillis();
        System.out.println("Optimal solution for big knapsack is: " + obj.processBigKnapsack());
        stop = System.currentTimeMillis();

        System.out.println("Processing took: " + (stop - start) + "ms");
    }
}
