import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class TSP {
    private int cities;
    private float[][] distance, subProblems;
    private record City(float x, float y) {};
    long empty = 0;

    public TSP(String filename) {
        // File Structure is as follows
        // [Number_of_cities]
        // [City1's_x_coordinate] [City2's_y_coordinate]
        // [City2's x-coordinate] [City2's_y_coordinate]

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            cities = Integer.parseInt(line);
            distance = new float[cities][cities];

            // number of subproblems is (2^(n - 1) - 1) * (n - 1)
            int size = (1 << (cities - 1)) - 1;
            subProblems = new float[size][cities - 1];

            for (float[] subProblem : subProblems)
                Arrays.fill(subProblem, Float.POSITIVE_INFINITY);

            City[] cityList = new City[cities];
            int count = 0;
            while((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                City city = new City(Float.parseFloat(data[0]), Float.parseFloat(data[1]));
                cityList[count++] = city;
            }

            computeDistances(cityList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int shortestTour() {
        int[] currentSubsets;
        for(int j = 0 ; j < cities - 1; j++) {
            subProblems[0][j] = distance[0][j + 1];
        }

        long start, stop;
        start = System.currentTimeMillis();
        for(int i = 0; i < cities - 1; i++) {
            currentSubsets = gosperHack(i);
            for(int subset : currentSubsets) {
                for(int j = 0; j < cities - 1; j++) {
                    if(subProblems[subset][j] == Float.POSITIVE_INFINITY) continue;
                    computeNextSubProblems(subset, j);
                }
            }
        }
        

        // Compute the final hops
        currentSubsets = gosperHack(cities - 2);
        float min = Float.POSITIVE_INFINITY;
        for(int subset : currentSubsets) {
            for(int i = 0; i < cities - 1; i++) {
                if(subProblems[subset][i] == Float.POSITIVE_INFINITY) continue;
                float lastHop = subProblems[subset][i] + distance[0][i + 1];
                if(lastHop < min) min = lastHop;
            }
        }

        stop = System.currentTimeMillis();

        System.out.println("Computing all " + ((1 << (cities - 1)) * (cities - 1) * (cities - 1)) + " subproblems took: "
                + (stop - start) + "ms");

        return (int) Math.floor(min);
    }

    private void computeNextSubProblems(int subset, int index) {
        int nextSubset = setBitAt(subset, index);
        for(int i = 0; i < cities - 1; i++) {
            if(!bitIsSet(nextSubset, i)) {
                subProblems[nextSubset][i] = Math.min(subProblems[nextSubset][i],
                                                       subProblems[subset][index] + distance[index + 1][i + 1]);
            }
        }
    }

    // Look up Gosper's Hack to generate/traverse subsets of size K in a set with N elements
    // Here, make sure that the cities does not reach above the number of bits in an int
    private int[] gosperHack(int subsetSize) {
        if(subsetSize <= 0) return new int[1];
        int bits = cities - 1;
        int subsetCount = computeNumberOfSubsets(bits, subsetSize);
        int[] subset = new int[subsetCount];
        int set = (1 << subsetSize) - 1;
        int index = 0;

        while(set < (1 << bits)) {
            subset[index++] = set;
            int c = set & - set;
            int r = set + c;
            set = (((r ^ set) >> 2) / c) | r;
        }

        return subset;
    }

    // Function that basically computes the binomial N choose K where
    // N is number of cities (excluding source) in the input and K is the subset size
    // Returns the total number of subsets of a given size
    private int computeNumberOfSubsets(int N, int subsetSize) {
        int stop = Math.min(subsetSize, N - subsetSize);    // nCk is symmetric around n - k
        int subsets = 1;
        for(int i = 0; i < stop; i++) {
            subsets = (subsets * (N - i)) / (i + 1);
        }
        return subsets;
    }

    private void computeDistances(City[] cityList) {
        for(int i = 0; i < cities; i++) {
            for(int j = 0; j < cities; j++) {
                if(i == j) distance[i][j] = 0;
                else distance[i][j] = distance(cityList[i], cityList[j]);
            }
        }
    }

    private float distance(City city1, City city2) {
        return (float) Math.hypot(city1.x - city2.x, 
                                  city1.y - city2.y);
    }
    
    private boolean bitIsSet(int mask, int index) {
        return ((mask >> index) & 1) == 1;
    }

    private int setBitAt(int mask, int index) {
        return mask | (1 << index);
    }

    private void printSubproblems() {
        for(int i = 0; i < cities - 1; i++) {
            // System.out.println("Size " + i + ": ");
            int[] subsets = gosperHack(i);
            for(int subset : subsets) {
                // System.out.print("   " + subset + " [");
                for(int j = 0; j < cities - 1; j++) {
                    // System.out.print(subProblems[subset][j] + " ");
                    if(subProblems[subset][j] == Float.POSITIVE_INFINITY) empty++;
                }
                // System.out.println("]");
            }
            
        }
    }

    public static void main(String[] args) {
        String filename = args[0];
        TSP obj = new TSP(filename);
        System.out.println("Shortest tour is of length: " + obj.shortestTour());
        obj.printSubproblems();
        System.out.println("Empty cells: " + obj.empty);
    }
}
