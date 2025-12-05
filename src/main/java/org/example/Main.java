package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        long totalFreshFood = 0;
        HashSet<Long> freshIds = new HashSet<>();
        ArrayList<Long> ids = new ArrayList<>();
        boolean startOfIds = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/Food.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    startOfIds = true;
                    continue;  // prevent empty line being parsed
                }
                if (!startOfIds) {
                    // Add ranges to freshIds set
                    String[] parts = line.split("-");
                    long start = Long.parseLong(parts[0]);
                    long end = Long.parseLong(parts[1]);
                    for (long i = start; i <= end; i++) { // inclusive!
                        freshIds.add(i);
                    }
                } else {
                    // Available ingredient IDs
                    ids.add(Long.parseLong(line));
                }
            }
            // Count fresh
            for (long id : ids) {
                if (freshIds.contains(id)) {
                    totalFreshFood++;
                }
            }
            System.out.println("Total Amount of Fresh Food: " + totalFreshFood);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
