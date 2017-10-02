package utils;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by Vasilii_Pozdeev on 2/10/2017.
 */
public class Pairs {
    private java.util.List<Pair<Integer, Integer>> pairs;
    private int length = 255;

    public Pairs() {
        pairs = new ArrayList<Pair<Integer, Integer>>();
    }

    public Pairs(int length) {
        pairs = new ArrayList<Pair<Integer, Integer>>();
        this.length = length;
    }

    public void add(int i1, int i2) {
        pairs.add(new Pair(i1, i2));
    }

    public void addWithShift(int i1, int i2) {
        if (pairs.size() == length) {
            for (int i = 0; i < pairs.size() - 1; i++) {
                pairs.set(i, pairs.get(i + 1));
            }
            pairs.remove(pairs.size() - 1);
        }
        add(i1, i2);
    }

    public int[] get(int index) {
        int[] pair = new int[2];
        pair[0] = pairs.get(index).getKey();
        pair[1] = pairs.get(index).getValue();
        return pair;
    }

    public int getSize() {
        return pairs.size();
    }

    @Override
    public String toString() {
        return "Pairs{" +
                "pairs=" + pairs +
                ", length=" + length +
                '}';
    }

    public void add(int[] pair) {
        add(pair[0], pair[1]);
    }

    public void flush() {
        pairs = new ArrayList<Pair<Integer, Integer>>();
    }

    public boolean contains(int[] baseToCompare) {
        for (Pair<Integer, Integer> pair : pairs) {
            if (pair.getKey() == baseToCompare[0] && pair.getValue() == baseToCompare[1])
                return true;
        }
        return false;

    }
}
