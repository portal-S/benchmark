package main;


import java.util.ArrayList;

public class TheMainTest {
    private final static int LENGTH = 10_000_000;

    ArrayList<Long> c1 = initList();
    ArrayList<Long> c2 = initList();
    ArrayList<Long> c3 = initList();

    private static ArrayList<Long> initList() {
        ArrayList<Long> arrayList = new ArrayList<>(LENGTH);
        for (long i = 0; i < LENGTH; i++) {
            arrayList.add(i);
        }
        return arrayList;
    }


    public long benchmark1() {
        long sum = 0L;
        for (var item : c1) {
            sum += item;
        }
        return sum;
    }

    public long benchmark2() {
        long sum = 0L;
        for (var item : c2) {
            sum += item;
        }
        return sum;
    }

    public long benchmark3() {
        long sum = 0L;
        for (var item : c3) {
            sum += item;
        }
        return sum;
    }




}
