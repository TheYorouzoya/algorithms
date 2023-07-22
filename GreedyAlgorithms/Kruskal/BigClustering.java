import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

public class BigClustering {
    private String[] labels;
    private int[] table;
    private int nodes, bits, clusters;
    private UnionFind set;

    public BigClustering(String filename) {
        // File Structure is as follows:
        // [number of nodes] [number of bits for each label]
        // [first bit of node 1][space]...[last bit of node 1][space]

        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            String[] data = line.split(" ");

            nodes = Integer.parseInt(data[0]);
            bits = Integer.parseInt(data[1]);

            int tableSize = (int) Math.pow(2, bits);
            table = new int[tableSize + 1];
            Arrays.fill(table, -1);
            labels = new String[nodes];
            clusters = nodes;
            set = new UnionFind(nodes);

            int index = 0;
            Pattern p = Pattern.compile(" ");

            while((line = reader.readLine()) != null) {
                labels[index++] = p.matcher(line).replaceAll("");
            }

        } catch(IOException e) {
            e.printStackTrace();
        }

        for (int i = 0, n = labels.length; i < n; i++) {
            int index = Integer.parseInt(labels[i], 2);
            if(table[index] >= 0) {
                set.union(table[index], i);
                clusters--;
            }
            table[index] = i;
        }
    }

    public String[] generateLabelVariants(String label) {
        int combinations = bits + (bits * (bits - 1)) / 2;
        String[] variants = new String[combinations];
        char[] charArray = label.toCharArray();
        int counter = 0;

        for(int i = 0, n = charArray.length; i < n; i++) {
            char[] dummy = charArray.clone();
            dummy[i] = (dummy[i] == '0') ? '1' : '0';
            variants[counter++] = new String(dummy);
            for(int j = i + 1; j < n; j++) {
                char[] dummy2 = dummy.clone();
                dummy2[j] = (dummy2[j] == '0') ? '1' : '0';
                variants[counter++] = new String(dummy2);
            }
        }

        return variants;
    }

    public void bruteForce() {
        for(int i = 0; i < nodes; i++) {
            for(int j = i + 1; j < nodes; j++) {
                char[] s1 = labels[i].toCharArray();
                char[] s2 = labels[j].toCharArray();
                int counter = 0;
                for(int k = 0; k < s1.length; k++) {
                    if((s1[k] ^ s2[k]) == 1) counter++;
                }
                if(counter <= 2) {
                    System.out.println(i + ": " + new String(s1) + "\n" + j + ": " + new String(s2));
                }
            }
        }
    }

    public void processLabels() {
        for(int i = 0; i < labels.length; i++) {
            String[] variants = generateLabelVariants(labels[i]);
            for(String label : variants) {
                int index = table[Integer.parseInt(label, 2)];
                if(index >= 0 && set.union(index, i)) clusters--;
            }
        }
    }

    public static void main(String[] args) {
        String filename = args[0];
        long start, stop;
        start = System.currentTimeMillis();
        BigClustering obj = new BigClustering(filename);
        stop = System.currentTimeMillis();
        System.out.println("Loaded clusters in: " + (stop - start) + "ms");

        start = System.currentTimeMillis();
        obj.processLabels();
        stop = System.currentTimeMillis();
        System.out.println("Processed clusters in: " + (stop - start) + "ms");

        System.out.println("Max clustering: " + obj.clusters);     
    }
}
