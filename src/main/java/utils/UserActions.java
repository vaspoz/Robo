package utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Vasilii_Pozdeev on 2/10/2017.
 */
public class UserActions implements Serializable {
    public static final long serialVersionUID = 2L;

    private Pairs mousePointers;
    private List<String> actions;

    public UserActions() {
        mousePointers = new Pairs();
        actions = new ArrayList<>();
    }

    public UserActions(Pairs mousePointers) {
        this.mousePointers = mousePointers;
        actions = new ArrayList<>();
    }

    public void addMousePointer(int[] pair) {
        mousePointers.add(pair);
    }

    public void addMousePointer(int i, int j) {
        mousePointers.add(i, j);
    }

    public void setActionForPoint(int index, String action) {
        actions.add(index, action);
    }

    public String getActionForPoint(int index) {
        return actions.get(index);
    }

    public List<String> getActions() {
        return actions;
    }

    public Pairs getMousePointers() {
        return mousePointers;
    }

    public int capturedPointsNumber() {
        return mousePointers.getSize();
    }

    public boolean containPointer(int[] baseToCompare) {
        return mousePointers.contains(baseToCompare);

    }

    public int[] getMousePointer(int i) {
        return mousePointers.get(i);
    }

    public void assignActions(Scanner sc) {
        for (int i = 0; i < mousePointers.getSize(); i++) {
            int[] point = mousePointers.get(i);
            System.out.print("Enter action for point [" + point[0] + ", " + point[1] + "]: ");
            String action = sc.nextLine();

            actions.add(i, action);
        }
    }
}
