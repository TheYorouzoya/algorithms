import java.util.Comparator;

public class EdgeComparator implements Comparator<Edge> {
    public int compare(Edge A, Edge B) {
        if (A.getCost() > B.getCost()) { return +1; }
        if (B.getCost() > A.getCost()) { return -1; }
        return 0;
    }
}
