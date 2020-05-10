package edu.wpi;

public class Record {
    String dataset;
    int blockID;
    int recID;
    int randomV;
    String name;
    String address;

    public Record(String dataset, int blockID, int recID, int randomV, String name, String address) {
        this.dataset = dataset;
        this.blockID = blockID;
        this.recID = recID;
        this.randomV = randomV;
        this.name = name;
        this.address = address;
    }

    public String getDataset() {
        return dataset;
    }

    public int getBlockID() {
        return blockID;
    }

    public int getRecID() {
        return recID;
    }

    public int getRandomV() {
        return randomV;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
