package parsort;

import java.util.*;

public class ParallelSort<T> {
    List<T> temp;
    List<T> vals;
    void sortRange() {

    }

    void mergeRange(int start, int mid, int end) {
        for (int i = start; i < end; i++) {
            temp.set(i, vals.get(i));
        }


    }

    public static void main(String[] args) {

    }
}
