package edu.wpi;

import edu.wpi.utils.Benchmark;
import edu.wpi.utils.Input;

import java.util.Set;

public class Main {
    private static void hashJoin() {
        /**
         * 1) Build hash table on Dataset-A. It should have 500 buckets. The buckets will store the entire record content.
         * 2) Use hashing of each record to determine the corresponding bucket should be based on the join column.
         * 3) Then, read Dataset-B file-by-file and record-by-record. For each record apply the same hash function on the join column to know which bucket you should check from Dataset-A (say bucket# K).
         * 4) Apply the join condition between r and each records in bucket K. If the join condition is met, i.e., A.RandomV = B.RandomV, then you need to produce an output record with the needed columns.
         * - Print out the execution time taken to execute the command (in millisecond)
         * - Print out the qualifying records (only the columns specified in the query)
         */

        // Create new benchmark instance
        Benchmark benchmark = new Benchmark();
        // Create a new hash table
        HashTable hashTable = new HashTable(DatasetFactory.PROJECT_DATASET_A);
        Bucket res = new Bucket();
        for (int i=1; i<=DatasetFactory.TOTAL_FILES_IN_DATASET; i++) {
            // Read ith file
            String content = Disk.readBlock(DatasetFactory.PROJECT_DATASET_B, "B" + i);
            for (int j=1; j<=DatasetFactory.TOTAL_RECORDS_PER_FILE; j++) {
                Record record = Disk.readRecord(content, j);
                Bucket bucket = hashTable.bucket(record);
                res.addAll(bucket);
                for (Record jrecord : bucket) {
                    System.out.println(jrecord.getRecID() + ", " + jrecord.getName() + ", "
                            + record.getRecID() + ", " + record.getName());
                }
            }
        }
        long timeElapsed = benchmark.getTimeElapsed();
        System.out.println("Time elapsed in MS: " + timeElapsed);
    }

    private static void nestedLoopJoin() {
        /**
         * 1) Loops over each file in Dataset-A, and for each file do:
         *      - Store the records of that file in memory
         *      - Read the entire Dataset-B, file-by-file and record-by-record, and compare each record with the
         *        records you have in memory from Dataset-A
         *      - Maintain the count of the records matching the join condition
         * 2) Retrieve the next file from Dataset-A and repeat the process
         */
        Benchmark benchmark = new Benchmark();
        Bucket res = new Bucket();
        Bucket bucket = new Bucket();
        for(int i=1; i<=DatasetFactory.TOTAL_FILES_IN_DATASET; i++) {
            String content = Disk.readBlock(DatasetFactory.PROJECT_DATASET_A, "A" + i);
            Bucket allRecs = new Bucket();
            for (int j=1; j<=DatasetFactory.TOTAL_RECORDS_PER_FILE; j++) {
                Record record = Disk.readRecord(content, j);
                allRecs.add(record);
            }
            bucket.addAll(allRecs);
        }
        for(int i=1; i<=DatasetFactory.TOTAL_FILES_IN_DATASET; i++) {
            String content = Disk.readBlock(DatasetFactory.PROJECT_DATASET_B, "B" + i);
            for(int j=1; j<=DatasetFactory.TOTAL_RECORDS_PER_FILE; j++) {
                Record record = Disk.readRecord(content, j);
                Bucket sub = bucket.getBucketsBasedOnCondition(Condition.GREATER_THAN, record);
                res.addAll(sub);
            }
        }
        long timeElapsed = benchmark.getTimeElapsed();
        System.out.println("Results total: " + res.size());
        System.out.println("Time elapsed in MS: " + timeElapsed);
    }

    private static void hashBasedAggregation(String dataset, AggregationType type) {
        /**
         * 1) Maintain a hash table, where each distinct group should have an entry in this table.
         * 2) As you scan the dataset, you need to keep updating the aggregation value maintained in the hash table
         * 3) After scanning the entire dataset, you start printing the content of the hash table
         */
        Benchmark benchmark = new Benchmark();
        HashTable hashTable = new HashTable(dataset);
        // Using a set and eliminating duplicates
        Set<String> keys = hashTable.keySet();
        for(String key : keys) {
            Bucket g = hashTable.get(key);
            double res;
            if (type == AggregationType.RANDOMV_SUM) {
                res = Aggregation.getSumRandomV(g);
            } else if (type == AggregationType.RANDOMV_AVG) {
                res = Aggregation.getAvgRandomV(g);
            } else {
                throw new UnsupportedOperationException();
            }
            System.out.println(key + ", " + res);
        }
        long timeElapsed = benchmark.getTimeElapsed();
        System.out.println("Time elapsed in MS: " + timeElapsed);
    }

    public static void main(String[] args) {
        /**
         * Queries supported:
         * 1) “SELECT A.Col1, A.Col2, B.Col1, B.Col2 FROM A, B WHERE A.RandomV = B.RandomV”:
         *      -- A refers to Dataset-A and B refers to Dataset-B
         * 2) “SELECT count(*) FROM A, B WHERE A.RandomV > B.RandomV”:
         * 3) “SELECT Col2, <AggregationFunction(ColumnID)> FROM <Dataset> GROUP BY Col2”:
         *      -- AggregationFunction(ColumnID) can be SUM(RandomV) or AVG(RandomV)
         *      -- <Dataset> can be A or B (referring to Dataset-A and Dataset-B respectively)
         *      -- Col2 will NOT change, it is referring to "name"
         */

        while (true) {
            System.out.println("Please enter a query: ");
            String query = Input.INSTANCE.readQuery();
            QueryType parse = QueryParser.parse(query);
            if (parse == QueryType.HASH_JOIN) {
                hashJoin();
            } else if (parse == QueryType.NESTED_LOOP_JOIN) {
                nestedLoopJoin();
            } else if (parse == QueryType.HASH_BASED_AGGREGATION_SUM_A) {
                hashBasedAggregation(DatasetFactory.PROJECT_DATASET_A, AggregationType.RANDOMV_SUM);
            } else if (parse == QueryType.HASH_BASED_AGGREGATION_SUM_B) {
                hashBasedAggregation(DatasetFactory.PROJECT_DATASET_B, AggregationType.RANDOMV_SUM);
            } else if (parse == QueryType.HASH_BASED_AGGREGATION_AVG_A) {
                hashBasedAggregation(DatasetFactory.PROJECT_DATASET_A, AggregationType.RANDOMV_AVG);
            } else if (parse == QueryType.HASH_BASED_AGGREGATION_AVG_B) {
                hashBasedAggregation(DatasetFactory.PROJECT_DATASET_B, AggregationType.RANDOMV_AVG);
            } else {
                throw new UnsupportedOperationException();
            }
        }

    }
}
