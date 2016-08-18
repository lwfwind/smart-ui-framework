package com.qa.serializable;

import java.io.Serializable;


public class Point implements Serializable {
    private static final long serialVersionUID = 5950169519310163575L;
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "x=" + x + ",y=" + y;
    }
}
