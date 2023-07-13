/*
 * Custom comparator to sort jobs according to the ratio (Weight/Length)
 */
import java.util.Comparator;

class JobRatioComparator implements Comparator<Job> {

    public int compare(Job j1, Job j2) {
        float job1 = (float) j1.getWeight() / (float) j1.getLength();
        float job2 = (float) j2.getWeight() / (float) j2.getLength();

        if (job1 < job2) { return +1; }
        if (job2 < job1) { return -1; }
        return 0;
    }
}