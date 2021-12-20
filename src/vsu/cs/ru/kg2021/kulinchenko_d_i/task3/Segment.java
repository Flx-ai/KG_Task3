package vsu.cs.ru.kg2021.kulinchenko_d_i.task3;

public class Segment {
    private RealPoint firstPoint;
    private RealPoint secondPoint;
    private int radius;

    public Segment(RealPoint firstPoint, RealPoint secondPoint, int radius) {
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
        this.radius = radius;
    }

    public RealPoint getFirstPoint() {
        return firstPoint;
    }

    public void setFirstPoint(RealPoint firstPoint) {
        this.firstPoint = firstPoint;
    }

    public RealPoint getSecondPoint() {
        return secondPoint;
    }

    public void setSecondPoint(RealPoint secondPoint) {
        this.secondPoint = secondPoint;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
