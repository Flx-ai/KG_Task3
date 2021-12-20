package vsu.cs.ru.kg2021.kulinchenko_d_i.task3;

public class RealPoint {
    private double realX;
    private double realY;

    public RealPoint(double realX, double realY) {
        this.realX = realX;
        this.realY = realY;
    }

    public double getRealX() {
        return realX;
    }

    public void setRealX(double realX) {
        this.realX = realX;
    }

    public double getRealY() {
        return realY;
    }

    public void setRealY(double realY) {
        this.realY = realY;
    }

    public RealPoint findDelta(RealPoint realPoint) {
        return new RealPoint(getRealX() - realPoint.getRealX(), getRealY() - realPoint.getRealY());
    }
}