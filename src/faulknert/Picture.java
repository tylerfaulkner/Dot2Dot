/*
 * Course: CS 2852 - 71
 * Spring 2020
 * Lab 2: Dot 2 Dot
 * Name: Tyler Faulkner
 * Created: 03/24/2020
 */
package faulknert;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collection;
import java.util.Iterator;


/**
 * Instance class used to load and draw .dot files onto a canvas.
 */
public class Picture {
    private List<Dot> dots;
    private boolean loaded = false;
    private static final int DOT_SIZE = 8;
    private static final int DOT_OFFSET = 4;
    private long removalTime = 0;

    /**
     * Instantiates the picture class without a original to copy
     *
     * @param emptyList list type determined by strategy
     */
    public Picture(List<Dot> emptyList) {
        this(null, emptyList);
    }

    /**
     * Instantiates the picture class with an original to copy
     *
     * @param original  the original picture instance to copy
     * @param emptyList the new list to add the original's dots to
     */
    public Picture(Picture original, List<Dot> emptyList) {
        if (original != null) {
            for (Dot dot : original.dots) {
                emptyList.add(dot);
            }
        }
        loaded = true;
        this.dots = emptyList;
    }

    /**
     * Used to load the .dot file. Must be ran first before other methods.
     *
     * @param path Path object to file.
     * @throws FileNotFoundException    If the scanner cannot find the file
     * @throws IllegalArgumentException when the values for x or y are not between 0 and 1
     */
    public void load(Path path) throws FileNotFoundException, IllegalArgumentException {
        int i = 1;
        Scanner in = new Scanner(new File(path.toString()));
        dots = new ArrayList<>();
        in.useDelimiter(", |\\n|,");
        while (in.hasNext()) {
            double x = Double.parseDouble(in.next());
            double y = Double.parseDouble(in.next());
            if (x >= 0 && x <= 1 && y <= 1 && y >= 0) {
                dots.add(new Dot(x, y));
                ++i;
            } else {
                throw new IllegalArgumentException();
            }
        }
        loaded = true;
    }

    /**
     * Saves the current image to a file
     *
     * @param path the path to save the image
     */
    public void save(Path path) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path.toFile()))) {
            for (Dot dot : dots) {
                pw.print(dot.getX());
                pw.print(", ");
                pw.println(dot.getY());
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "A fatal error has occurred.");
            alert.setHeaderText("Critical Error");
            alert.showAndWait();
        }
    }

    /**
     * Draws each dot loaded from the file. Load method must be ran first.
     *
     * @param canvas the canvas object in the application
     * @throws IllegalStateException thrown if a file has not been loaded yet upon method call
     */
    public void drawDots(Canvas canvas) throws IllegalStateException {
        if (loaded) {
            for (Dot dot : dots) {
                canvas.getGraphicsContext2D().fillOval(
                        (1 - dot.getX()) * canvas.getWidth() - DOT_OFFSET,
                        (1 - dot.getY()) * canvas.getHeight() - DOT_OFFSET, DOT_SIZE, DOT_SIZE);
            }
        } else {
            throw new IllegalStateException("A file has not been loaded.");
        }
    }

    /**
     * Removes dots from the image based on their critical values
     *
     * @param numberDesired the desired amount of dots left after removal
     * @param strategy      the selected removal method to be used
     * @throws IllegalStateException    thrown if a file has not been loaded yet upon method call
     * @throws IllegalArgumentException thrown if the numberDesired is less than 3
     */
    public void removeDots(int numberDesired, String strategy)
            throws IllegalArgumentException, IllegalStateException {
        if (loaded) {
            if (numberDesired >= 3) {
                if (strategy.equals("index")) {
                    removalTime = removeDotsIndex(dots, numberDesired);
                } else if (strategy.equals("iterator")) {
                    removalTime = removeDotsIterator(dots, numberDesired);
                }
            } else {
                throw new IllegalArgumentException("A minimum value of 3 dots is required.");
            }
        } else {
            throw new IllegalStateException("A file has not been loaded.");
        }
    }

    /**
     * Draws a line path connecting all dot locations. Load method must be ran first.
     *
     * @param canvas the canvas object in the application
     * @throws IllegalStateException thrown if a file has not been loaded yet upon method call
     */
    public void drawLines(Canvas canvas) throws IllegalStateException {
        if (loaded) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.beginPath();
            Dot first = dots.get(0);
            double firstX = (1 - first.getX()) * canvas.getWidth();
            double firstY = (1 - first.getY()) * canvas.getHeight();
            gc.moveTo(firstX, firstY);
            for (int i = 1; i < dots.size(); ++i) {
                Dot dot = dots.get(i);
                gc.lineTo((1 - dot.getX()) * canvas.getWidth(),
                        (1 - dot.getY()) * canvas.getHeight());
            }
            gc.lineTo(firstX, firstY);
            gc.closePath();
            gc.stroke();
        } else {
            throw new IllegalStateException("A file has not been loaded.");
        }
    }

    public int getDotCount() {
        return dots.size();
    }

    public long getRemovalTime() {
        return removalTime;
    }

    //O(n^2)
    private static long removeDotsIndex(List<Dot> dots, int numberDesired) {
        long start = System.nanoTime();
        while (dots.size() > numberDesired) {
            double lowestValue = Double.MAX_VALUE;
            int lowestIndex = 0;
            for (int i = 1; i < dots.size() - 1; ++i) {
                double criticalValue = dots.get(i).calculateCriticalValue(dots.get(i - 1),
                        dots.get(i + 1));
                if (criticalValue < lowestValue) {
                    lowestValue = criticalValue;
                    lowestIndex = i;
                }
            }
            dots.remove(lowestIndex);
        }
        return System.nanoTime() - start;
    }

    //O(n)
    private static long removeDotsIterator(Collection<Dot> dots, int numberDesired) {
        long start = System.nanoTime();
        while (dots.size() > numberDesired) {
            Iterator<Dot> iterator = dots.iterator();
            Dot before = iterator.next();
            Dot current = iterator.next();
            Dot after = iterator.next();
            double lowestValue = Double.MAX_VALUE;
            Dot lowestDot = current;
            while(iterator.hasNext()) {
                double criticalValue = current.calculateCriticalValue(before,
                        after);
                if (criticalValue < lowestValue) {
                    lowestValue = criticalValue;
                    lowestDot = current;
                }
                before = current;
                current = after;
                after = iterator.next();
            }
            dots.remove(lowestDot);
        }
        return System.nanoTime() - start;
    }
}
