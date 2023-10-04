import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

public class TwoSAT {
    private ArrayList<ArrayList<Integer>> outgoingList;
    private ArrayList<ArrayList<Integer>> incomingList;
    private int nodes;
    private int edges;

    public TwoSAT(String filename) {
        // File structure is as follows:
        // [number_of_symbols]
        // [symbolA_number] [symbolA_number]
        // A negative sign in front of a symbol's number denotes the negation

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            nodes = Integer.parseInt(line);
            // Each node has a negation counterpart. + 2 is to leave space for 0th node
            nodes = (nodes * 2) + 2;
            edges = 0;

            outgoingList = new ArrayList<>();
            incomingList = new ArrayList<>();

            // Initialize outgoing and incoming lists
            for(int i = 0; i <= nodes; i++) {
                outgoingList.add(new ArrayList<>());
                incomingList.add(new ArrayList<>());
            }

            while((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                int symbolA, symbolB;

                // Each symbol i maps to index i * 2 in the array
                symbolA = Integer.parseInt(data[0]) * 2;
                symbolB = Integer.parseInt(data[1]) * 2;

                if(symbolA < 0) symbolA = Math.abs(symbolA) + 1;
                if(symbolB < 0) symbolB = Math.abs(symbolB) + 1;

                outgoingList.get(negate(symbolA)).add(symbolB);
                outgoingList.get(negate(symbolB)).add(symbolA);

                incomingList.get(symbolB).add(negate(symbolA));
                incomingList.get(symbolA).add(negate(symbolB));

                edges +=2;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int negate(int symbol) {
        if(symbol % 2 == 0) return symbol + 1;
        else return symbol - 1;
    }

    public int[] firstPass() {
        int currentTime = 2;
        int[] finishingTime = new int[nodes];
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        boolean[] explored = new boolean[nodes];

        for (int i = nodes - 1; i > 1; i--) {
            if (!explored[i]) {
                ArrayDeque<Integer> ordering = new ArrayDeque<>();

                stack.push(i);
                while(!stack.isEmpty()) {
                    int node = stack.pop();
                    if(!explored[node]) {
                        explored[node] = true;
                        ordering.push(node);
                        pushEdgesIntoStack(stack, incomingList.get(node));
                    }
                }

                while(!ordering.isEmpty()) {
                    int node = ordering.pop();
                    finishingTime[currentTime++] = node;
                }
            }
        }
        return finishingTime;
    }

    public boolean secondPass(int[] finishingTime) {
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        boolean[] explored = new boolean[nodes];
        int[] leaders = new int[nodes];
        int currentLeader;

        for(int i = finishingTime.length - 1; i > 1; i--) {
            if(!explored[finishingTime[i]]) {
                currentLeader = finishingTime[i];
                stack.push(finishingTime[i]);
                while(!stack.isEmpty()) {
                    int node = stack.pop();
                    if(!explored[node]) {
                        explored[node] = true;
                        leaders[node] = currentLeader;
                        pushEdgesIntoStack(stack, outgoingList.get(node));
                    }
                }
            }
        }

        // System.out.println("Leaders: " + Arrays.toString(leaders));

        for(int i = 2; i < nodes; i +=2) {
            if(leaders[i] == leaders[i + 1])
                return false;
        }
        return true;
    }

    private void pushEdgesIntoStack(ArrayDeque<Integer> stack, ArrayList<Integer> list) {
        for(int node : list) {
            stack.push(node);
        }
    }

    public static void main(String[] args) {
        String filename = args[0];
        TwoSAT obj = new TwoSAT(filename);
        int[] finishingTime = obj.firstPass();
        boolean feasibility = obj.secondPass(finishingTime);
        // System.out.println("Finishing times: " + Arrays.toString(finishingTime));
        System.out.println("Given 2-SAT instance is feasible: " + feasibility);
    }
}
