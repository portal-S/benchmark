package org.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class StringGroup {

    private static final List<String> dataList = List.of(
        "1,2,3,4",
        "1,4,5,6",
        "4,5,23,6",

        "6,3,1",
        "8,3,15");


    public static void main(String[] args) throws Exception {
        // map: key(ind) -> val(map: val -> group)
        Map<Integer, Map<Long, String>> groupedData = new HashMap<>();
        Map<String, Set<String>> groupWithValues = new HashMap<>();
        AtomicInteger groupCount = new AtomicInteger(0);

        AtomicInteger maxGroupLen = new AtomicInteger(0);

        try (Stream<String> stream = Files.lines(Paths.get("/Users/aligguseynov/PersonalProjects/experimental-map/src/main/resources/data.txt"))) {
            stream
                .forEach(data -> {
                try {
                    var values = parseData(data);
                    var found = false;
                    int index = 0;
                    var groupName = "";

                    while (found == false && index < values.length) {
                        var value = values[index];
                        if (value == 54) {
                            System.out.println("test");
                        }
                        if (value == -1) {
                            index++;
                            continue;
                        }
                        groupName = tryFindGroup(index, value, groupedData);
                        found = !groupName.equals("");
                        index++;
                    }

                    groupName = found ? groupName : String.format("group-%s", groupCount.getAndIncrement());
                    groupWithValues.computeIfAbsent(groupName, (key) -> new HashSet<>());

                    mergeInGroup(values, groupName, groupedData);
                    var groupData = groupWithValues.get(groupName);
                    groupData.add(data);
                    maxGroupLen.set(Math.max(maxGroupLen.get(), groupData.size()));
                } catch (NumberFormatException e) {
                    System.out.println("not valid string " + data);
                }
            });
        }
        // 29 sec
        long start = System.nanoTime();
        var sorted = groupWithValues.entrySet().stream()
            .sorted((el1, el2) -> el1.getValue().size() - el2.getValue().size())
            .map(Map.Entry::getValue).toList();

        long end = System.nanoTime();


       // var sorted = sortData(groupWithValues, maxGroupLen.get()).stream().filter(Objects::nonNull).toList();

        System.out.println(sorted.get(1));
        System.out.println(end - start);
    }

    private static List<List<String>> sortData(Map<String, Set<String>> groupedData, int maxLen) {

        List<String>[] bucketSortedData = new List[maxLen];

        for (Map.Entry<String, Set<String>> group : groupedData.entrySet()) {
            var groupSize = group.getValue().size() - 1;
            if (groupSize < 1) continue;
            var bucket = bucketSortedData[groupSize];
            if (bucket == null) {
                bucketSortedData[groupSize] = new ArrayList<>();
                bucket = bucketSortedData[groupSize];
            }
            bucket.add(group.getKey());
        }
        return Arrays.asList(bucketSortedData);
    }

    private static void mergeInGroup(
        long[] dataValues,
        String groupName,
        Map<Integer, Map<Long, String>> groupedData
    ) {
        for (int i = 0; i < dataValues.length; i++) {
            groupedData.computeIfAbsent(i, (key) -> new HashMap<>());

            var val = dataValues[i];
            groupedData.get(i).computeIfAbsent(val, (key) -> groupName);
        }
    }

    private static String tryFindGroup(int index, long value, Map<Integer, Map<Long, String>> groupedData) {
        if (!groupedData.containsKey(index) || !groupedData.get(index).containsKey(value)) {
            return "";
        }
        return groupedData.get(index).get(value);
    }


    private static long[] parseData(String data) {
        //TODO: optimize
        return Arrays.stream(data.replace("\"", "").split(";")).mapToLong(dataStr -> {
            if (dataStr.equals("")) return -1;
            return Long.parseLong(dataStr);
        }).toArray();
    }

    // если групп мало и длины строк большие, то пытаемся смержиться
    // с каждой группой проходясь по всем ее ключам

    /**
     * если групп много, то итерируемся по всем отдельным индексом
     */
    /**
     *
     * 4 -> 300 -> g1
     * 5 -> 443 -> g1
     * 6 -> 340 -> g1
     * 8 -> 390 -> g1
     * 7 -> 500 -> g2
     * 7 -> 800 -> g1
     *
     * map: key(ind) -> val(map: val -> group)
     *
     * bucket sort for final sorting
     */
}
