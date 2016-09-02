package com.qa.serializable;

import java.io.Serializable;


/**
 * The type Point.
 */
public class Point implements Serializable {
    private static final long serialVersionUID = 5950169519310163575L;
    private int x;
    private int y;

    /**
     * Instantiates a new Point.
     *
     * @param x the x
     * @param y the y
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * Sets y.
     *
     * @param y the y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * Sets x.
     *
     * @param x the x
     */
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "x=" + x + ",y=" + y;
    }
}
