package edu.wpi.utils;

import java.util.Scanner;

public enum  Input {
    INSTANCE;
    private Scanner scanner = new Scanner(System.in);
    public String readQuery() {
        return scanner.nextLine();
    }
}
