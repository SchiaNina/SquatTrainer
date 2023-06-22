package com.example.squattrainer.utils;

public class Position {
    private float x;
    private float y;
    private float z;

    private static final float TOLERANCE = 2F;

    public Position(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position(float[] arr) {
        this.x = arr[0];
        this.y = arr[1];
        this.z = arr[2];
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float[] toArray() {
        return new float[]{x,y,z};
    }

    public boolean isEqualWithTolerance(float x, float y, float z) {
        return (Math.abs(this.x - x)<TOLERANCE) && (Math.abs(this.y - y)<TOLERANCE) && (Math.abs(this.z - z)<TOLERANCE);
    }
}
