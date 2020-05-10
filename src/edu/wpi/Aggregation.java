package edu.wpi;

public class Aggregation {
    public static double getSumRandomV(Bucket bucket) {
        double sum = 0;
        for(Record rec :  bucket) {
            sum += rec.getRandomV();
        }
        return sum;
    }

    public static double getAvgRandomV(Bucket bucket) {
        double sum = 0;
        for(Record rec : bucket) {
            sum += rec.getRandomV();
        }
        double avg = sum / bucket.size();
        return avg;
    }
}
