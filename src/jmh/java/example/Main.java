package example;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@State(Scope.Thread)
public class Main {

    private static int LENGTH = 10_000_000;

    ArrayList<Long> orig = initList();
    ArrayList<Long> sort = sortList();
    ArrayList<Long> shuffle = shuffleList();

    @Benchmark
    public void listOrig() {
        benchmark(orig);
    }

    @Benchmark
    public void listSh() {
        benchmark(shuffle);
    }

    @Benchmark
    public void listSo() {
        benchmark(sort);
    }

    private static ArrayList<Long> sortList() {
        var list = initList();
        Collections.sort(list);
        return list;
    }

    private static ArrayList<Long> shuffleList() {
        var list = initList();
        Collections.shuffle(list);

        int ij = 0;
        for (int i = 0; i < list.size(); i++) {
            // Просто "прикоснитесь" к элементу, чтобы прогреть кэш
            ij += list.get(i);
        }
        System.out.println(ij);
        return list;
    }

    private static ArrayList<Long> initRList() {
        ArrayList<Long> arrayList = new ArrayList<>(LENGTH);
        Random random = new Random(LENGTH);
        for (long i = LENGTH - 1; i >= 0; i--) {
            arrayList.add(random.nextLong(0, 1000));
        }
        return arrayList;
    }

    private static ArrayList<Long> initList() {
        ArrayList<Long> arrayList = new ArrayList<>(LENGTH);
        for (long i = 0; i < LENGTH; i++) {
            arrayList.add(i);
        }
        return arrayList;
    }

    public long benchmark(ArrayList<Long> collection) {
        long sum = 0L;
        for (var i : collection) {
            sum += i;
        }
        return sum;
    }

    private void move(Long data) {

    }
}
