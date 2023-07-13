/*
 * Custom comparator to sort Jobs according to the difference (Weight - Length)
 */
import java.util.Comparator;

class JobDifferenceComparator implements Comparator<Job> {
    
    public int compare(Job j1, Job j2) {
        int job1 = j1.getWeight() - j1.getLength();
        int job2 = j2.getWeight() - j2.getLength();

        if (job1 < job2) { return +1; }
        if (job2 < job1) { return -1; }
        if (j1.getWeight() < j2.getWeight()) { return +1; }
        if (j2.getWeight() < j1.getWeight()) {return -1; }
        return 0;
    }
}