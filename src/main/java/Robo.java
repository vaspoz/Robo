import utils.*;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Robo {
    public static final double IMAGE_COMPARE_DELTA = 0.06;
    public static final double POINTERS_DISTANCE_TO_TRIGGER_ACTION = 30.0;
    public static final int DELAY_IMAGE_CAPTURING = 50;

    public static final int POINTERS_BUFFER_DEPTH = 4;
    public static final int DELAY_MOUSE_CAPTURING = 200;
    //delay between capturing mouse position. Both this vars identify how long the cursor should keep unmoved to being captured

    public static Robot robot;
    public static Rectangle screenRect;
    public static Scanner sc = new Scanner(System.in);
    public static Keyboard keyboard;

    public static void main(String[] args) throws IOException, InterruptedException, AWTException {
        robot = new Robot();
        screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        keyboard = new Keyboard(robot);

//        while (true) {
//            waitScreenUpdated(robot.createScreenCapture(screenRect));
//        }
//        robotRecord(5);

        while (true) {
            System.out.print("> ");
            String command = sc.next();
            if (!command.equals("robo")) {
                continue;
            }

            String subcom = sc.next();
            switch (subcom) {
                case "macro":
                    int stepsNum = sc.nextInt();
                    sc.nextLine();
                    robotRecord(stepsNum);
                    break;
                case "play":
                    String macroName = sc.next();
                    robotPlayback(macroName);
                    break;
                case "stop":
                    System.out.println("See you soon, human");
                    TimeUnit.SECONDS.sleep(1);
                    System.exit(0);

            }
        }

    }

    private static void robotRecord(int pointNum) throws InterruptedException {
        int xMax = ((Double) screenRect.getMaxX()).intValue();
        int yMax = ((Double) screenRect.getMaxY()).intValue();
        Point mousePointBuf = new Point(xMax, yMax);

        Point mousePoint;
        UserActions userActions = new UserActions();

        Pairs pointBuffer = new Pairs(POINTERS_BUFFER_DEPTH);
        boolean eventTriggered = false;

        System.out.println("You're too slow human. I'll wait for 2 seconds before start.");
        TimeUnit.SECONDS.sleep(2);
        System.out.println("Do it!");

        while (userActions.capturedPointsNumber() != pointNum) {
            TimeUnit.MILLISECONDS.sleep(DELAY_MOUSE_CAPTURING);

            mousePoint = MouseInfo.getPointerInfo().getLocation();
            pointBuffer.addWithShift(mousePoint);

            //Detect whether the point should be captured
            boolean isShot = elementsAreTheSame(pointBuffer);
            isShot &= !userActions.containPointer(mousePoint);

            if (isShot) {
                double dist = distance(mousePoint, mousePointBuf);
                if ((dist < POINTERS_DISTANCE_TO_TRIGGER_ACTION) && (!eventTriggered)) {
                    System.out.println("EVENT!");
                    String pointAction = sc.next();
                    userActions.setActionForPoint(mousePointBuf, pointAction);
                    eventTriggered = true;
                } else if (dist >= POINTERS_DISTANCE_TO_TRIGGER_ACTION) {
                    mousePointBuf = mousePoint;
                    userActions.addMousePointer(mousePoint);
                    System.out.println(mousePoint);
                    pointBuffer.flush();
                    eventTriggered = false;
                }
            }
        }

        System.out.println("I became more intelligent. Soon you'll fall down on your knees!");
        System.out.print("But now, please, give it a name: ");
        String macroName = sc.next();

        FileManager.saveMacro(macroName, userActions);
    }

    private static void robotPlayback(String macroName) throws InterruptedException, AWTException {
        BufferedImage captureBefore;
        UserActions userActions = FileManager.loadMacro(macroName);
        if (userActions == null) {
            throw new NullPointerException("Cannot find the macro, human. You've made mistake. Again");
        }

        for (Point mousePoint:userActions.getMousePointers()) {

//        }
//        for (int i = 0; i < userActions.capturedPointsNumber(); i++) {

            String action = userActions.getActionForPoint(mousePoint);
            System.out.println("x: " + mousePoint.getX() + ", y: " + mousePoint.getY() + ", Action: " + (action.equals("n") ? "none" : action));

            captureBefore = robot.createScreenCapture(screenRect);
            moveAndClick(mousePoint);
            switch (action) {
                case "wait":
                    waitScreenUpdated(captureBefore);
                    break;
                case "n":
                    break;
                default:
                    keyboard.type(action);
                    break;
            }
            TimeUnit.MILLISECONDS.sleep(300);
        }

        System.out.println("Just in time. As usual");
    }

    private static boolean elementsAreTheSame(Pairs pointBuffer) {
        boolean isEqual = true;
        double[] baseToCompare = pointBuffer.get(0);
        for (int i = 1; i < pointBuffer.getSize(); i++) {
            isEqual &= Arrays.equals(baseToCompare, pointBuffer.get(i));
        }
        return isEqual;
    }

    private static void waitScreenUpdated(BufferedImage captureBefore) throws InterruptedException {
        double delta;
        int bufferDepth = 10;
        Singles deltasBuffer = new Singles(bufferDepth);
        int[] pixelsBefore = ((DataBufferInt) captureBefore.getRaster().getDataBuffer()).getData();

        boolean calmedDown;
        int pickIndex = -1;
        while (true) {
            TimeUnit.MILLISECONDS.sleep(DELAY_IMAGE_CAPTURING);

            BufferedImage captureAfter = robot.createScreenCapture(screenRect);
            int[] pixelsAfter = ((DataBufferInt) captureAfter.getRaster().getDataBuffer()).getData();
            delta = getImagesDelta(pixelsBefore, pixelsAfter);
            pixelsBefore = pixelsAfter;
            deltasBuffer.addWithShift(delta);

            if (lastIsBiggest(deltasBuffer)) {
                pickIndex = deltasBuffer.getSize() - 1;
                continue;
            }

            calmedDown = last5closeToZeroDelta(deltasBuffer);

            if (--pickIndex == 0 && calmedDown) {
                System.out.println("Updated!");
                return;
            }

            if (pickIndex < 0) {
                pickIndex = 0;
            }
        }
    }

    private static boolean last5closeToZeroDelta(Singles deltasBuffer) {
        boolean isResultTrue = true;
        for (int i = deltasBuffer.getSize() - 6; i < deltasBuffer.getSize() - 1; i++) {
            isResultTrue &= 0 <= deltasBuffer.get(i);
            isResultTrue &= deltasBuffer.get(i) < IMAGE_COMPARE_DELTA;
        }
        return isResultTrue;
    }

    private static boolean lastIsBiggest(Singles deltasBuffer) {
        double last = deltasBuffer.getLast();
        if (last < IMAGE_COMPARE_DELTA) return false;
        for (int i = 0; i < deltasBuffer.getSize() - 1; i++) {
            if (deltasBuffer.get(i) > last) return false;
        }
        return true;
    }

    private static double getImagesDelta(int[] pixelsBefore, int[] pixelsAfter) {
        int length;
        if (pixelsBefore.length < pixelsAfter.length) {
            length = pixelsBefore.length;
        } else {
            length = pixelsAfter.length;
        }

        int deltaCount = 0;
        for (int i = 0; i < length; i++) {
            if (pixelsBefore[i] != pixelsAfter[i]) {
                deltaCount++;
            }
        }

        return (double) deltaCount / (double) length;
    }

    private static void moveAndClick(Point mousePoint) throws AWTException, InterruptedException {
        robot.mouseMove(mousePoint.x, mousePoint.y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    private static double distance(Point a, Point b) {
        int x1 = a.x;
        int x2 = b.x;
        int y1 = a.y;
        int y2 = b.y;
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }
}

