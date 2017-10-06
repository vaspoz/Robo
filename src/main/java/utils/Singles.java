package utils;

import javafx.util.Pair;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Vasilii_Pozdeev on 5/10/2017.
 */
public class Singles {
    private Pairs pairs;

    public Singles() {
        pairs = new Pairs();
    }

    public Singles(int depth) {
        pairs = new Pairs(depth);
    }

    public void addWithShift(double elt) {
        pairs.addWithShift(0, elt);
    }

    public double get(int index) {
        double[] pair = pairs.get(index);
        return pair[1];
    }

    public int getSize() {
        return pairs.getSize();
    }

    public void add(double elt) {
        pairs.add(new double[]{0, elt});
    }

    public void flush() {
        pairs.flush();
    }

    public boolean contains(double elt) {
        return pairs.contains(new double[]{0, elt});
    }

    @Override
    public String toString() {
        int pairLastIndex = pairs.getSize() - 1;
        StringBuilder sb = new StringBuilder();
        sb.append("Singles{");
        for (int i = 0; i <= pairLastIndex; i++) {
            sb.append(new DecimalFormat("#.00").format(pairs.get(i)[1]));
            if (i != pairLastIndex) sb.append(" ");
        }
        sb.append("}");
        return sb.toString();
    }

    public double getLast() {
        return get(getSize() - 1);
    }
}
