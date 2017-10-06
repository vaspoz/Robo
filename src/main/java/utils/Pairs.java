package utils;

import javafx.util.Pair;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Vasilii_Pozdeev on 2/10/2017.
 */
public class Pairs implements Serializable {
    public static final long serialVersionUID = 1L;

    private List<Pair<Double, Double>> pairs;
    private int length = 255;

    public Pairs() {
        pairs = new ArrayList<>();
    }

    public Pairs(int length) {
        pairs = new ArrayList<>();
        this.length = length;
        for (int i = 0; i < length; i++) {
            pairs.add(new Pair<>(-1.0, -1.0));
        }
    }

    public void add(double i1, double i2) {
        pairs.add(new Pair(i1, i2));
    }

    public void addWithShift(double i1, double i2) {
        if (pairs.size() == length) {
            for (int i = 0; i < pairs.size() - 1; i++) {
                pairs.set(i, pairs.get(i + 1));
            }
            pairs.remove(pairs.size() - 1);
        }
        add(i1, i2);
    }

    public double[] get(int index) {
        double[] pair = new double[2];
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

    public void add(double[] pair) {
        add(pair[0], pair[1]);
    }

    public void flush() {
        pairs = new ArrayList<>();
    }

    public boolean contains(double[] baseToCompare) {
        for (Pair<Double, Double> pair : pairs) {
            if (pair.getKey() == baseToCompare[0] && pair.getValue() == baseToCompare[1])
                return true;
        }
        return false;

    }

    public Pair getPair(int index) {
        return pairs.get(index);
    }

    public void addWithShift(Point mousePoint) {
        addWithShift(mousePoint.x, mousePoint.y);
    }
}
