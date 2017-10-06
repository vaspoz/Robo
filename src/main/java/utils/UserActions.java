package utils;

import javafx.util.Pair;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * Created by Vasilii_Pozdeev on 2/10/2017.
 */
public class UserActions implements Serializable {
    public static final long serialVersionUID = 2L;

    private Map<Point, String> pointerAction;
    private Pairs mousePointers;
    private List<String> actions;

    public UserActions() {
        pointerAction = new HashMap<>();

        mousePointers = new Pairs();
        actions = new ArrayList<>();
    }

    public void setActionForPoint(Point point, String action) {
        pointerAction.put(point, action);
    }

    public String getActionForPoint(Point point) {
        return pointerAction.get(point);
    }

    public int capturedPointsNumber() {
        return pointerAction.size();
    }

    public boolean containPointer(Point point) {
        return pointerAction.containsKey(point);

    }

//    public double[] getMousePointer(int i) {
//        return mousePointers.get(i);
//    }

    public void assignActions(Scanner sc) {
        for (int i = 0; i < mousePointers.getSize(); i++) {
            double[] point = mousePointers.get(i);
            System.out.print("Enter action for point [" + point[0] + ", " + point[1] + "]: ");
            String action = sc.nextLine();

            actions.add(i, action);
        }
    }

    public void addMousePointer(Point mousePoint) {
        pointerAction.put(mousePoint, "");
    }

    public Iterable<? extends Point> getMousePointers() {
        return pointerAction.keySet();

    }
}
