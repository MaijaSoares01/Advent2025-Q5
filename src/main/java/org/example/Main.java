package org.example;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<long[]> ranges = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        boolean readingIds = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/Food.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    readingIds = true;
                    continue;
                }
                if (!readingIds) {
                    String[] parts = line.split("-");
                    long start = Long.parseLong(parts[0]);
                    long end = Long.parseLong(parts[1]);
                    ranges.add(new long[]{start, end}); // store interval
                } else {
                    ids.add(Long.parseLong(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Step 1: sort ranges
        ranges.sort(Comparator.comparingLong(a -> a[0]));

        // Step 2: merge overlapping or contiguous ranges
        List<long[]> merged = new ArrayList<>();
        long[] current = ranges.get(0);
        for (int i = 1; i < ranges.size(); i++) {
            long[] next = ranges.get(i);
            if (next[0] <= current[1] + 1) {
                // overlap → merge
                current[1] = Math.max(current[1], next[1]);
            } else {
                // no overlap → push previous
                merged.add(current);
                current = next;
            }
        }
        merged.add(current);

        // Step 3: check each ID via binary search
        long totalFresh = 0;
        for (long id : ids) {
            if (isFresh(id, merged)) {
                totalFresh++;
            }
        }

        //Step 4: each date is a fresh id
        long totalFresh2 = countDates(merged);
        //Part 1
        System.out.println("Total Fresh IDs: " + totalFresh);
        //Part 2
        System.out.println("Total Fresh IDs: " + totalFresh2);
    }

    private static long countDates(List<long[]> ranges) {
        long fresh = 0;
        for (long[] range : ranges) {
            fresh += (range[1] - range[0] + 1); // inclusive range size
        }
        return fresh;
    }

    // Binary search over merged ranges
    private static boolean isFresh(long id, List<long[]> ranges) {
        int left = 0;
        int right = ranges.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            long[] range = ranges.get(mid);
            if (id < range[0]) {
                right = mid - 1;
            } else if (id > range[1]) {
                left = mid + 1;
            } else {
                return true; // id is within range
            }
        }
        return false;
    }
}
