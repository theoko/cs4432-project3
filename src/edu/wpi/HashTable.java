package edu.wpi;

import java.util.HashMap;

public class HashTable extends HashMap<String, Bucket> {

    int BUCKETS = 500;
    String dataset;

    public HashTable(String dataset) {
        this.dataset = dataset;

        this.initialize();
    }

    private void initialize() {
        for(int i=1; i<=DatasetFactory.TOTAL_FILES_IN_DATASET; i++) {
            String content = Disk.readBlock(dataset, DatasetFactory.DATASET_LETTER_MAP.get(dataset) + i);
            for(int j=1; j<=DatasetFactory.TOTAL_RECORDS_PER_FILE; j++) {
                assert content != null;
                Record record = Disk.readRecord(content, j);
                int randomValue = record.getRandomV();
                String key = Integer.toBinaryString(randomValue);
                Bucket bucket = super.get(key);
                if(bucket == null) bucket = new Bucket();
                bucket.add(record);
                this.put(key, bucket);
            }
        }
    }

    public Bucket bucket(Record record) {
        int randomValue = record.getRandomV();
        String key = Integer.toBinaryString(randomValue);
        return this.get(key);
    }
}
