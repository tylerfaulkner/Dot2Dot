/*
 * Course: CS 2852 - 71
 * Spring 2020
 * Lab 2: Dot 2 Dot
 * Name: Tyler Faulkner
 * Created: 03/24/2020
 */
package faulknert;

/**
 * Instance class used to store position for a dot on a canvas.
 */
public class Dot {
    private final double x;
    private final double y;

    /**
     * Initializes dot instance.
     * @param x the x pixel ratio (0-1)
     * @param y the y pixel ratio (0-1)
     */
    public Dot(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Calculate the critical value of the dot (cv2=d12+d23−d13)
     * @param previous the dot previous to this dot
     * @param next the dot after this dot
     * @return critical value
     */
    public double calculateCriticalValue(Dot previous, Dot next){
        double distancePrevNext = Math.sqrt(Math.pow(next.getX() - previous.getX(), 2)
                + Math.pow(next.getY() - previous.getY(), 2));
        double distanceThisNext = Math.sqrt(Math.pow(next.getX() - this.getX(), 2)
                + Math.pow(next.getY() - this.getY(), 2));
        double distancePrevThis = Math.sqrt(Math.pow(this.getX() - previous.getX(), 2)
                + Math.pow(this.getY() - previous.getY(), 2));
        return (distancePrevThis + distanceThisNext) - distancePrevNext;
    }
}
