package Graphs;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

import Graphs.Literals.DirectedGraph;

public class Kosaraju {
    public int[] firstPass(DirectedGraph inputGraph) {
        // Assume that the graph is already reversed
        int N = inputGraph.nodes();
        int currentTime = 1;
        int[] finishingTimes = new int[N];
        ArrayDeque<Integer> stack = new ArrayDeque<Integer>();
        boolean[] explored = new boolean[N];

        for (int i = N - 1; i > 0; i--) {
            // System.out.println("i is: " + i);
            if (!explored[i]) {
                stack.push(i);
                pushEdgesIntoStack(stack, inputGraph.getEdges(i));
                explored[i] = true;
                while(!stack.isEmpty()) {
                    int node = stack.pop();
                    if(!explored[node]) {
                        // System.out.println("Explored: " + node);
                        explored[node] = true;
                        finishingTimes[currentTime++] = node;
                        pushEdgesIntoStack(stack, inputGraph.getEdges(node));  
                    }
                }
                finishingTimes[currentTime++] = i;
            }
            // System.out.println("===============");
        }
        return finishingTimes;
    }

    public PriorityQueue<Integer> secondPass(int[] finishingTimes, DirectedGraph inputGraph) {
        int topSCCNum = 5;
        int N = inputGraph.nodes();
        ArrayDeque<Integer> stack = new ArrayDeque<Integer>();
        boolean[] explored = new boolean[N];
        PriorityQueue<Integer> leaders = new PriorityQueue<>(topSCCNum);

        for (int i = finishingTimes.length - 1; i > 0; i--) {
            if(!explored[i]) {
                int currentSize = 0;
                stack.push(finishingTimes[i]);
                while(!stack.isEmpty()) {
                    int node = stack.pop();
                    if(!explored[node]) {
                    explored[node] = true;
                    currentSize++;
                    pushEdgesIntoStack(stack, inputGraph.getEdges(node));  
                    }
                }
                leaders.add(currentSize);
            }
        }

        return leaders;
    }

    private void pushEdgesIntoStack(ArrayDeque<Integer> stack, ArrayList<Integer> edges) {
        for(int node : edges) {
            stack.push(node);
        }
    }
    
    public static void main(String[] args) {
        String filename = "Graphs/scc.txt";
        int nodes = 875715;
        
        long startTime = System.currentTimeMillis();
        DirectedGraph inputGraph = new DirectedGraph(filename, nodes);
        long stopTime = System.currentTimeMillis();
        
        System.out.println("Done loading graph in: " + (stopTime - startTime) + "ms");
        
        Kosaraju obj = new Kosaraju();

        startTime = System.currentTimeMillis();
        DirectedGraph reversedGraph = inputGraph.reverseGraph();
        stopTime = System.currentTimeMillis();
        System.out.println("Reversed graph in: " + (stopTime - startTime) + "ms");
        
        startTime = System.currentTimeMillis();
        int[] finishingTimes = obj.firstPass(reversedGraph);
        stopTime = System.currentTimeMillis();
        System.out.println("Computed first pass in: " + (stopTime - startTime) + "ms");
        // System.out.println(Arrays.toString(finishingTimes));

        startTime = System.currentTimeMillis();
        PriorityQueue<Integer> leaders = obj.secondPass(finishingTimes, inputGraph);
        stopTime = System.currentTimeMillis();
        System.out.println("Computed second pass in: " + (stopTime - startTime) + "ms");
        // System.out.println(leaders);
    }
}
