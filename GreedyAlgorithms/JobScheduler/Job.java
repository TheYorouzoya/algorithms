
public class Job {
    private int weight;
    private int length;

    public Job(int weight, int length) {
        this.weight = weight;
        this.length = length;
    }

    public int getWeight() { return this.weight; }
    public int getLength() { return this.length; }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String toString() {
        return ("Weight: " + this.weight + " Length: " + this.length);
    }
}
