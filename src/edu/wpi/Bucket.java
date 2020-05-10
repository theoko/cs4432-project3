package edu.wpi;

import java.util.ArrayList;

public class Bucket extends ArrayList<Record> {
    public Bucket() {
        super();
    }

    public Bucket getBucketsBasedOnCondition(Condition condition, Record test) {
        Bucket bucket = new Bucket();
        for (Record rec : this) {
            if (condition == Condition.GREATER_THAN) {
                if (rec.getRandomV() > test.getRandomV()) {
                    bucket.add(rec);
                }
            }
        }
        return bucket;
    }
}
