package edu.wpi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Disk {

    public static Record readRecord(String file, int recordInFile) {
        int start = 40 * (recordInFile - 1);
        int end = start + 40; // the first 40 bytes
        String record = file.substring(start, end);
        String dataset = record.substring(0, 1);
        String blockID = record.substring(1, 3);
        String name = record.substring(16, 19);
        String address = record.substring(28, 31);
        String randomV = record.substring(33, 37);
//        System.out.println("file = " + file + ", recordInFile = " + recordInFile);
//        System.out.println("dataset = " + dataset);
//        System.out.println("recordID = " + blockID);
//        System.out.println("name = " + name);
//        System.out.println("address = " + address);
//        System.out.println("randomV = " + randomV);
        return new Record(dataset, Integer.parseInt(blockID), recordInFile, Integer.parseInt(randomV), name, address);
    }

    public static String readBlock(String tableName, String blockId) {
        String fileName = blockId + ".txt";
        File block = new File(tableName + File.separator + fileName);
        try (BufferedReader br = new BufferedReader(new FileReader(block))) {
            String line;
            while ((line = br.readLine()) != null) {
                // The line represents one Frame
                return line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // In case we haven't returned anything
        return null;
    }

}