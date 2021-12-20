package vsu.cs.ru.kg2021.kulinchenko_d_i.task3;

import java.awt.*;

public class Line {
    private RealPoint firstPoint, secondPoint;

    public Line(RealPoint firstPoint, RealPoint secondPoint) {
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
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
}