/*
 * A job scheduler which computes the completion times for a given list of jobs
 * Jobs can be pre-sorted according to the greedy criteria and then fed into the
 * computeComplitionTime function to get the total sum of all completion times.
 * 
 * Completion Time is as follows:
 *     (Job1 Weight) * (Job2 Length) + (Job2 Weight) * (Job1 + Job2 Length)...
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScheduleJobs {

    // Function to load Jobs from a file
    public static List<Job> loadJobsFromFile (String filename) {
        // File structure is:
        // [Number of Jobs]
        // [Job1 Weight] [Job1 Length]
        // [Job2 Weight] [Job2 Length]

        List<Job> jobArray = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            line = reader.readLine();   // Skip the first line
            while((line = reader.readLine()) != null) {
                // Read and parse the rest of the file
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

    // Function that computes the total completion time for a given job list in order
    public static long computeCompletionTime(List<Job> jobList) {
        long currentLength = 0;
        long completionTime = 0;
        for (Job j : jobList) {
            completionTime += (j.getWeight() * (currentLength + j.getLength()));
            currentLength += j.getLength();
        }
        return completionTime;
    }

    // Function that primts all jobs in a job list
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
