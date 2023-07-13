import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScheduleJobs {

    public static List<Job> loadJobsFromFile (String filename) {
        List<Job> jobArray = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            line = reader.readLine();
            while((line = reader.readLine()) != null) {
                String[] jobDetails = line.split(" ");
                int weight = Integer.parseInt(jobDetails[0]);
                int length = Integer.parseInt(jobDetails[1]);
                jobArray.add(new Job(weight, length));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jobArray;
    }

    public static long computeCompletionTime(List<Job> jobList) {
        long currentLength = 0;
        long completionTime = 0;
        for (Job j : jobList) {
            completionTime += (j.getWeight() * (currentLength + j.getLength()));
            currentLength += j.getLength();
        }
        return completionTime;
    }

    public static void printJobs(List<Job> jobList) {
        for(Job j : jobList) {
            System.out.println(j.toString());
        }
    }

    public static void main(String[] args) {
        String filename = args[0];

        List<Job> jobList = loadJobsFromFile(filename);
        Collections.sort(jobList, new JobDifferenceComparator());
        long completionTime = computeCompletionTime(jobList);
        System.out.println("Difference Completion Time: " + completionTime);

        Collections.sort(jobList, new JobRatioComparator());
        completionTime = computeCompletionTime(jobList);
        System.out.println("Ratio Completion Time: " + completionTime);

    }
}
