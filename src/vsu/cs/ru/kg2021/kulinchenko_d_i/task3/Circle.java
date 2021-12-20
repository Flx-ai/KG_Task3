package vsu.cs.ru.kg2021.kulinchenko_d_i.task3;

public class Circle {
    private RealPoint firstPoint;
    private RealPoint secondPoint;
    private double radius;

    public Circle(RealPoint startPoint, RealPoint nextPoint) {
        this.firstPoint = startPoint;
        this.secondPoint = nextPoint;
    }

    public RealPoint getSecondPoint() {
        return secondPoint;
    }

    public void setSecondPoint(RealPoint secondPoint) {
        this.secondPoint = secondPoint;
    }

    public RealPoint getFirstPoint() {
        return firstPoint;
    }

    public void setFirstPoint(RealPoint firstPoint) {
        this.firstPoint = firstPoint;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

}
