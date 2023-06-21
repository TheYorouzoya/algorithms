
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.PriorityQueue;

import Literals.DirectedGraph;

/*
 * Program that implements the Kosaraju Two Pass Algorithm to compute
 * the 5 largest strongly-connected components in a given directed graph.
 * 
 * Running Time: O(m + n) where m and n denote the number of edges and vertices
 * respectively
 * 
 * For large graphs (~1million+ nodes), the major overhead will be loading the
 * graph into memory to operate on. This approach to load the entire graph into
 * memory will not work for gigantic graphs.
 * 
 * ================
 * The Algorithm:
 * ================
 * 
 * 
 * 
 */

public class Kosaraju {
    public int[] firstPass(DirectedGraph inputGraph) {
        // Assume that the graph is already reversed
        int N = inputGraph.nodes();
        int currentTime = 1;
        int[] finishingTimes = new int[N];
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        boolean[] explored = new boolean[N];

        for (int i = N - 1; i > 0; i--) {
            if (!explored[i]) {
                ArrayDeque<Integer> finStack = new ArrayDeque<>();
                stack.push(i);
                while(!stack.isEmpty()) {
                    int node = stack.pop();
                    if(!explored[node]) {
                        explored[node] = true;
                        finStack.push(node);
                        pushEdgesIntoStack(stack, inputGraph.getEdges(node));
                    }
                }
                while(!finStack.isEmpty()) {
                    int node = finStack.pop();
                    finishingTimes[currentTime++] = node;
                }
            }
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
            if(!explored[finishingTimes[i]]) {
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
                if(leaders.size() > topSCCNum) {
                    leaders.poll();
                }
                leaders.offer(currentSize);
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
