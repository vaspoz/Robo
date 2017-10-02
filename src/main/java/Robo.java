import utils.Pairs;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Robo {
    public static Robot robot;
    public static Rectangle screenRect;
    public static Pairs mousePoints = new Pairs();

    public static void main(String[] args) throws IOException, InterruptedException, AWTException {
        robot = new Robot();
        screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

        robotRecord(5);
        System.out.println(mousePoints);
        TimeUnit.SECONDS.sleep(5);
        robotRepeat();

    }

    private static void robotRecord(int pointNum) throws InterruptedException {
        Point mousePoint;
        int bufferDepth = 3;
        int delay = 100;
        Pairs pointBuffer = new Pairs(bufferDepth);

        while (mousePoints.getSize() != pointNum) {
            TimeUnit.MILLISECONDS.sleep(delay);
            mousePoint = MouseInfo.getPointerInfo().getLocation();
            pointBuffer.addWithShift(mousePoint.x, mousePoint.y);

            if (pointBuffer.getSize() < bufferDepth) continue;

            boolean isShot = true;
            int[] baseToCompare = pointBuffer.get(0);
            for (int i = 1; i < bufferDepth; i++) {
                isShot &= Arrays.equals(baseToCompare, pointBuffer.get(i));
            }
            if (isShot && !mousePoints.contains(baseToCompare)) {
                mousePoints.add(baseToCompare);
                System.out.println(Arrays.toString(baseToCompare));
                pointBuffer.flush();
            }


        }
    }

    private static void robotRepeat() throws InterruptedException, AWTException {
        BufferedImage captureBefore;

        for (int i = 0; i < mousePoints.getSize(); i++) {
            int[] pair = mousePoints.get(i);
            captureBefore = robot.createScreenCapture(screenRect);
            moveAndClick(pair[0], pair[1]);
//            waitScreenUpdated(captureBefore);
            TimeUnit.SECONDS.sleep(1);
        }
    }

    private static void waitScreenUpdated(BufferedImage captureBefore) throws InterruptedException {
        double delta = 0.0;

        int[] pixelsBefore = ((DataBufferInt) captureBefore.getRaster().getDataBuffer()).getData();

        while (delta < 0.25) {
            TimeUnit.MILLISECONDS.sleep(100);
            BufferedImage captureAfter = robot.createScreenCapture(screenRect);
            int[] pixelsAfter = ((DataBufferInt) captureAfter.getRaster().getDataBuffer()).getData();
            delta = getImagesDelta(pixelsBefore, pixelsAfter);
            System.out.println("Delta: " + delta);
        }
        TimeUnit.MILLISECONDS.sleep(500);

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

    private static void moveAndClick(int x, int y) throws AWTException, InterruptedException {
        robot.mouseMove(x, y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
}

