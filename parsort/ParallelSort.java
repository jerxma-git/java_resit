package parsort;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ParallelSort<T> {
    static boolean log = true;

    public ParallelSort(List<T> unsorted) {
        vals = new ArrayList<>(unsorted);
        temp = new ArrayList<>(unsorted);
        comparator = null;
    }

    public ParallelSort(List<T> unsorted, Comparator<T> comparator) {
        this(unsorted);
        this.comparator = comparator;
    }

    Comparator<T> comparator;
    List<T> temp;
    List<T> vals;

    List<T> sort() {
        for (int step = 1; step < vals.size(); step *= 2) {
            for (int i = 0; (i + 1) * step < vals.size(); i += 2) {
                // TODO: parallelize
                mergeRange(i * step, (i + 1) * step, (i + 2) * step);
            }
        }
        return vals;
    }

    // List<T> sort() {
    //     for (int step = 1; step < vals.size(); step *= 2) {
    //             List<Thread> threads = new ArrayList<>();
    //         for (int i = 0; (i + 1) * step < vals.size(); i += 2) {
    //             final int start = i * step;
    //             final int mid = (i+1) * step;
    //             final int end = (i+2) * step;
    //             Thread th = new Thread( () -> mergeRange(start, mid, end) );
    //             threads.add(th);
    //             th.run();
    //             // mergeRange(i * step, (i + 1) * step, (i + 2) * step);
    //         }
    //         for (Thread th : threads) {
    //             try {
    //                 th.join();
    //             } catch (InterruptedException e) {
    //                 //TODO: handle exception
    //             }
    //         }

    //     }
    //     return vals;
    // }

    void mergeRange(int start, int mid, int end) {
        if (end > vals.size()) {
            end = vals.size();
        }
        for (int i = start; i < end; i++) {
            temp.set(i, vals.get(i));
        }
        int pos1 = start;
        int pos2 = mid;
        int pos = start;
        while (pos < end) {
            while (pos1 < mid && (pos2 == end || compare(temp.get(pos1), temp.get(pos2)) <= 0)) {
                vals.set(pos++, temp.get(pos1++));
            }
            while (pos2 < end && (pos1 == mid || compare(temp.get(pos2), temp.get(pos1)) < 0)) {
                vals.set(pos++, temp.get(pos2++));
            }
        }
    }

    public String toString() {
        return vals.stream().map(Object::toString).collect(Collectors.joining(", "));
    }

    public static boolean test(int arraySize) {
        Random rd = new Random();
        List<Integer> vals = rd.ints().limit(arraySize).boxed().collect(Collectors.toList());
        log(vals);
        ParallelSort<Integer> ps = new ParallelSort<>(vals, Comparator.reverseOrder());
        List<Integer> psSorted = ps.sort();
        vals.sort(Comparator.reverseOrder());
        log(psSorted);
        log(vals);
        return vals.equals(psSorted);
    }

    public static boolean testMul() {
        List<Boolean> results = new ArrayList<>();
        for (int size = 0; size < 10; size++) {
            results.add(test(size));
        }
        return results.stream().allMatch(Predicate.isEqual(true));
    }

    public static void setLog(boolean log) {
        ParallelSort.log = log;
    }

    static void log(Object msg) {
        if (log) {
            System.out.println(msg);
        }
    }

    @SuppressWarnings("unchecked")
    private int compare(T o1, T o2) {
        return comparator == null ? ((Comparable<T>) o1).compareTo(o2) : comparator.compare(o1, o2);
    }

    public static void main(String[] args) {
        setLog(true);
        if (testMul()) {
            System.out.println("passed");
        } else {
            System.out.println("failed");
        }

    }
}
