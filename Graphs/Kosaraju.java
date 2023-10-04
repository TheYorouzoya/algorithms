/*
 * Program that implements the Kosaraju Two Pass Algorithm to compute
 * the 5 largest strongly-connected components in a given directed graph.
 * 
 * Running Time: O(m + n) where m and n denote the number of edges and vertices
 * respectively
 * 
 * For large graphs (~1 million+ nodes), the major overhead will be loading the
 * graph into memory to operate on. This approach to load the entire graph into
 * memory will not work for gigantic graphs.
 */


package Graphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.PriorityQueue;

import DataStructures.DirectedGraph;

public class Kosaraju {
    // First pass computes the ordering on the reversed input graph
    public int[] firstPass(DirectedGraph inputGraph) {
        // Assume that the graph is already reversed
        int N = inputGraph.nodes();
        int currentTime = 1;
        int[] finishingTimes = new int[N];  // Array that keeps track of finishing times ordering
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        boolean[] explored = new boolean[N];

        // Run iterative Depth-First Search from the last node in the graph
        for (int i = N - 1; i > 0; i--) {
            if (!explored[i]) { // If node isn't explored

                // Stack to store the ordering since iterative DFS reversed the order
                ArrayDeque<Integer> orderingStack = new ArrayDeque<>();
                
                // Push node and start DFS
                stack.push(i);
                while(!stack.isEmpty()) {
                    int node = stack.pop();
                    if(!explored[node]) {
                        explored[node] = true;
                        // Push newly explored node into the ordering stack
                        orderingStack.push(node);
                        // Push all the edges into main stack for DFS
                        pushEdgesIntoStack(stack, inputGraph.getEdges(node));
                    }
                }
                // Once DFS is finished, empty the ordering stack nodes into the finishing times array
                while(!orderingStack.isEmpty()) {
                    int node = orderingStack.pop();
                    finishingTimes[currentTime++] = node;
                }
            }
        }
        return finishingTimes;
    }

    // Second pass consumes the finishing time array returned above to run DFS on the original graph
    public PriorityQueue<Integer> secondPass(int[] finishingTimes, DirectedGraph inputGraph) {
        int topSCCNum = 5;  // number of SCCs to compute
        int nodes = inputGraph.nodes();
        ArrayDeque<Integer> stack = new ArrayDeque<Integer>();
        boolean[] explored = new boolean[nodes];
        PriorityQueue<Integer> leaders = new PriorityQueue<>(topSCCNum);    // Heap to store top 5 SCCs

        // Run DFS from the end of the finishing times array
        for (int i = finishingTimes.length - 1; i > 0; i--) {
            if(!explored[finishingTimes[i]]) { // If node is unexplored
                int currentSize = 0;    // Reset current SCC size

                // Push into stack and start DFS
                stack.push(finishingTimes[i]);
                while(!stack.isEmpty()) {
                    int node = stack.pop();
                    if(!explored[node]) {   // Unexplored node
                        explored[node] = true;
                        currentSize++;
                        pushEdgesIntoStack(stack, inputGraph.getEdges(node));  
                    }
                }
                if(leaders.size() > topSCCNum) {
                    // Poll the queue if size overflows
                    leaders.poll();
                }
                // Add new size to queue
                leaders.offer(currentSize);
            }
        }

        return leaders;
    }

    // Function that pushes all outgoing edge nodes into the given stack
    private void pushEdgesIntoStack(ArrayDeque<Integer> stack, ArrayList<Integer> edges) {
        for(int node : edges) {
            stack.push(node);
        }
    }
    
    public static void main(String[] args) {
        String filename = "scc.txt";
        int nodes = 875715;
        Kosaraju obj = new Kosaraju();
        
        DirectedGraph inputGraph = new DirectedGraph(filename, nodes);
        DirectedGraph reversedGraph = inputGraph.reverseGraph();
        int[] finishingTimes = obj.firstPass(reversedGraph);
        PriorityQueue<Integer> leaders = obj.secondPass(finishingTimes, inputGraph);
        System.out.println(leaders);
    }
}
