package edu.wpi;

import java.util.HashMap;
import java.util.Map;

public class DatasetFactory {
    // Only difference from Project 2 is the 1st letter in the record (it is “A” instead of “F”)
    // Index “j” resets and starts from “001” in each file
    public static final String PROJECT_DATASET_A = "Project3Dataset-A";
    public static final String PROJECT_DATASET_B = "Project3Dataset-B";
    public static final Map<String, String> DATASET_LETTER_MAP = new HashMap<>() {{
       put(PROJECT_DATASET_A, "A");
       put(PROJECT_DATASET_B, "B");
    }};

    public static final int TOTAL_FILES_IN_DATASET = 99;
    public static final int TOTAL_RECORDS_PER_FILE = 100;
}
