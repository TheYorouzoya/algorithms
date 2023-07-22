import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class KruskalClustering {
    private UnionFind edgeSet;
    private ArrayList<Edge> edgeList;
    private int nodes, clusters;
    
    public KruskalClustering(String filename) {
        // File structure is as follows:
        // [number of nodes]
        // [edge 1 node 1] [edge 1 node 2] [edge 1 cost]
        // [edge 2 node 1] [edge 2 node 2] [edge 2 cost]

        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            nodes = Integer.parseInt(line);
            clusters = nodes;
            edgeSet = new UnionFind(nodes);
            edgeList = new ArrayList<Edge>(nodes);

            while((line = reader.readLine()) != null) {
                String[] lineSplit = line.split(" ");
                int nodeA, nodeB, cost;
                nodeA = Integer.parseInt(lineSplit[0]);
                nodeB = Integer.parseInt(lineSplit[1]);
                cost = Integer.parseInt(lineSplit[2]);
                Edge edge = new Edge(nodeA, nodeB, cost);
                edgeList.add(edge);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int computeMaxClusterDistance(ArrayList<Edge> list, int clusterSize) {
        int spacing = -1;
        for(Edge edge : list) {
            if(edgeSet.union(edge.getNodeA(), edge.getNodeB())) {
                if (clusters <= clusterSize) {
                    spacing = edge.getCost();
                    break;
                }
                clusters--;
            }
        }
        return spacing;
    }

    public static void main(String[] args) {
        String filename = args[0];
        int clusterSize = 4;
        KruskalClustering obj = new KruskalClustering(filename);
        Collections.sort(obj.edgeList, new EdgeComparator());
        int spacing = obj.computeMaxClusterDistance(obj.edgeList, clusterSize);
        System.out.println("Spacing is: " + spacing);
    }
}
