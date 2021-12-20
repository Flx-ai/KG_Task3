package vsu.cs.ru.kg2021.kulinchenko_d_i.task3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    private ScreenConverter screenConverter;
    private Line axisX, axisY;
    private List<Line> lines = new ArrayList<>();
    private Line currentLine = null;
    private Line editingLine = null;

    private List<Circle> circles = new ArrayList<>();
    private Circle currentCircle = null;
    private Circle editingCircle = null;

    private List<Segment> segments = new ArrayList<>();
    private Segment currentSegment = null;
    private Segment editingSegment = null;

    public DrawPanel() {
        screenConverter = new ScreenConverter(-2, 2, 4, 4, 800, 600);
        axisX = new Line(new RealPoint(-1, 0), new RealPoint(1, 0));
        axisY = new Line(new RealPoint(0, -1), new RealPoint(0, 1));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    @Override
    protected void paintComponent(Graphics originalGraphics) {
        screenConverter.setScreenWidth(getWidth());
        screenConverter.setScreenHeight(getHeight());

        BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();

        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(0, 0 , getWidth(), getHeight());

        graphics2D.setColor(Color.BLUE);
        drawLine(graphics2D, screenConverter, axisX);
        drawLine(graphics2D, screenConverter, axisY);

        graphics2D.setColor(Color.BLACK);

        for (Line line: lines) {
            drawLine(graphics2D, screenConverter, line);
        }
        if (currentLine != null) {
            graphics2D.setColor(Color.RED);
            drawLine(graphics2D, screenConverter, currentLine);
        }
        if (editingLine != null) {
            graphics2D.setColor(Color.GREEN);
            drawLine(graphics2D, screenConverter, editingLine);
        }

        for (Circle circle: circles) {
            drawCircle(graphics2D, screenConverter, circle);
        }

        if (currentCircle != null) {
            graphics2D.setColor(Color.RED);
            drawCircle(graphics2D, screenConverter, currentCircle);
        }

        if (editingCircle != null) {
            graphics2D.setColor(Color.GREEN);
            drawCircle(graphics2D, screenConverter, editingCircle);
        }

        originalGraphics.drawImage(bufferedImage, 0, 0, null);
        graphics2D.dispose();
    }

    public void applyValuesForCircle(double firstValue, double secondValue, double thirdValue) {
        if (editingCircle == null) {
            return;
        }
        editingCircle.getFirstPoint().setRealX(firstValue);
        editingCircle.getFirstPoint().setRealY(secondValue);

        repaint();
    }

    public interface Editor {
        void toEditCircle(Circle circle);
        void cancel();
    }

    private Editor editor;

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    protected void onEditCircle(Circle circle) {
        if (editor != null) {
            editor.toEditCircle(circle);
        }
    }

    protected void onCancel() {
        if (editor != null) {
            editor.cancel();
        }
    }

    private static void drawArc(Graphics2D g, ScreenConverter screenConverter, Circle circle) {
        ScreenPoint firstPoint = screenConverter.convertRealToScreen(circle.getFirstPoint());
        ScreenPoint secondPoint = screenConverter.convertRealToScreen(circle.getSecondPoint());
    }

    private static void drawLine(Graphics2D g, ScreenConverter screenConverter, Line line) {
        ScreenPoint firstPoint = screenConverter.convertRealToScreen(line.getFirstPoint());
        ScreenPoint secondPoint = screenConverter.convertRealToScreen(line.getSecondPoint());
        g.drawLine(firstPoint.getScreenX(), firstPoint.getScreenY(), secondPoint.getScreenX(), secondPoint.getScreenY());
    }

    private static boolean isNearWithLine(ScreenPoint firstPoint, ScreenPoint secondPoint, double epsilon) {
        int deltaX = firstPoint.getScreenX() - secondPoint.getScreenX();
        int deltaY = firstPoint.getScreenY() - secondPoint.getScreenY();
        int distanceInSquare = deltaX * deltaX + deltaY * deltaY;
        return distanceInSquare < epsilon;
    }

    private static double calculateDistanceToLine(ScreenPoint firstPointLine, ScreenPoint secondPointLine, ScreenPoint checkPoint) {
        double cofA = secondPointLine.getScreenY() - firstPointLine.getScreenY();
        double cofB = - (secondPointLine.getScreenX() - firstPointLine.getScreenX());
        double e = - checkPoint.getScreenX() * cofB + checkPoint.getScreenY() * cofA;
        double f = cofA * firstPointLine.getScreenX() - cofB * firstPointLine.getScreenY();
        double y = (cofA * e - cofB * f) / (cofA * cofA + cofB * cofB);
        double x = (cofA * y - e) / cofB;
        double r = Math.sqrt((checkPoint.getScreenX() - x) * (checkPoint.getScreenX() - x) + (checkPoint.getScreenY() - y) * (checkPoint.getScreenY() - y));
        if (!Double.isNaN(r)) {
            System.out.println(r);
        }
        return r;
    }

    private static boolean closeToLine(ScreenConverter screenConverter, Line line, ScreenPoint searchPoint, int epsilon) {
        ScreenPoint firstScreenPoint = screenConverter.convertRealToScreen(line.getFirstPoint());
        ScreenPoint secondScreenPoint = screenConverter.convertRealToScreen(line.getSecondPoint());
        return isNearWithLine(firstScreenPoint, searchPoint, epsilon) || isNearWithLine(secondScreenPoint, searchPoint, epsilon) ||
                (calculateDistanceToLine(firstScreenPoint, secondScreenPoint, searchPoint) < epsilon && isPointInRectangle(firstScreenPoint, secondScreenPoint, searchPoint));
    }

    private static Line findTheNearestLine(ScreenConverter screenConverter, List<Line> lines, ScreenPoint searchPoint, int epsilon) {
        for (Line line : lines) {
            if (closeToLine(screenConverter, line, searchPoint, epsilon)) {
                return line;
            }
        }
        return null;
    }

    private static void drawCircle(Graphics2D g, ScreenConverter screenConverter, Circle circle) {
        ScreenPoint firstPoint = screenConverter.convertRealToScreen(circle.getFirstPoint());
        ScreenPoint secondPoint = screenConverter.convertRealToScreen(circle.getSecondPoint());
        double radius = calculateCircleRadius(firstPoint, secondPoint);
        g.drawOval((int) (firstPoint.getScreenX() - radius), (int) (firstPoint.getScreenY() - radius), (int) (radius * 2), (int) (radius * 2));
    }

    public static double calculateCircleRadius(ScreenPoint firstPoint, ScreenPoint secondPoint) {
        return Math.sqrt((secondPoint.getScreenX() - firstPoint.getScreenX()) * (secondPoint.getScreenX() - firstPoint.getScreenX()) +
                (secondPoint.getScreenY() - firstPoint.getScreenY()) * (secondPoint.getScreenY() - firstPoint.getScreenY()));
    }

    private static boolean isNearWithCircle(ScreenPoint firstPointCircle, ScreenPoint secondPointCircle, ScreenPoint checkPoint, double epsilon) {
        double radius = calculateCircleRadius(firstPointCircle, secondPointCircle);
        double distanceBetweenTwoPoints = Math.sqrt((checkPoint.getScreenX() - firstPointCircle.getScreenX()) * (checkPoint.getScreenX() - firstPointCircle.getScreenX()) +
                (checkPoint.getScreenY() - firstPointCircle.getScreenY()) * (checkPoint.getScreenY() - firstPointCircle.getScreenY()));
        return distanceBetweenTwoPoints - radius <= epsilon;
    }

    private static boolean isPointInRectangle(ScreenPoint firstScreenPoint, ScreenPoint secondScreenPoint, ScreenPoint checkPoint) {
        return checkPoint.getScreenX() >= Math.min(firstScreenPoint.getScreenX(), secondScreenPoint.getScreenX()) &&
                checkPoint.getScreenX() <= Math.max(firstScreenPoint.getScreenX(), secondScreenPoint.getScreenX()) &&
                checkPoint.getScreenY() >= Math.min(firstScreenPoint.getScreenY(), secondScreenPoint.getScreenY()) &&
                checkPoint.getScreenY() <= Math.max(firstScreenPoint.getScreenY(), secondScreenPoint.getScreenY());
    }

    private static boolean isPointInCircle(ScreenPoint firstScreenPoint, ScreenPoint secondScreenPoint, ScreenPoint checkPoint) {
        double radius = calculateCircleRadius(firstScreenPoint, secondScreenPoint);
        return ((checkPoint.getScreenX() - firstScreenPoint.getScreenX()) * (checkPoint.getScreenX() - firstScreenPoint.getScreenX()) +
                (checkPoint.getScreenY() - firstScreenPoint.getScreenY()) * (checkPoint.getScreenY() - firstScreenPoint.getScreenY()) <= radius * radius);
    }

    private static boolean closeToCircle(ScreenConverter screenConverter, Circle circle, ScreenPoint searchPoint, int epsilon) {
        ScreenPoint firstScreenPoint = screenConverter.convertRealToScreen(circle.getFirstPoint());
        ScreenPoint secondScreenPoint = screenConverter.convertRealToScreen(circle.getSecondPoint());
            return isPointInCircle(firstScreenPoint, secondScreenPoint, searchPoint) || isNearWithCircle(firstScreenPoint, secondScreenPoint, searchPoint, epsilon);
    }


    private static Circle findTheNearestCircle(ScreenConverter screenConverter, List<Circle> circles, ScreenPoint searchPoint, int epsilon) {
        for (Circle circle : circles) {
            if (closeToCircle(screenConverter, circle, searchPoint, epsilon)) {
                return circle;
            }
        }
        return null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    private ScreenPoint previousPoint = null;

    private void cancelEdit() {
        editingCircle = null;
        editingLine = null;
        onCancel();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            previousPoint = new ScreenPoint(e.getX(), e.getY());
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            if (editingCircle == null) {
                Circle circle = findTheNearestCircle(screenConverter, circles, new ScreenPoint(e.getX(), e.getY()), 3);
                Line x = findTheNearestLine(screenConverter, lines, new ScreenPoint(e.getX(), e.getY()), 3);
                if (circle != null) {
                    editingCircle = circle;
                    onEditCircle(circle);
                    editingLine = x;

                } else {
                    RealPoint point1 = screenConverter.convertScreenToReal(new ScreenPoint(e.getX(), e.getY()));
                    currentLine = new Line(point1, point1);
                    currentCircle = new Circle(point1, point1);
                }
            }
//            if (editingLine == null) {
//                Line x = findTheNearestLine(screenConverter, lines, new ScreenPoint(e.getX(), e.getY()), 3);
//                if (x != null) {
//                    editingLine = x;
//                } else {
//                    RealPoint point = screenConverter.convertScreenToReal(new ScreenPoint(e.getX(), e.getY()));
//                    currentLine = new Line(point, point);
//                }
//            }
            else {

//                if (editingCircle != null && editingLine != null) {
//                    RealPoint point2 = screenConverter.convertScreenToReal(new ScreenPoint(e.getX(), e.getY()));
//                    editingCircle.setSecondPoint(point2);                                                             Альтернативу написал в mouseDragged и MouseReleased,
//                                                                                                                         чтобы было плавное изменение радиуса
//                    editingLine.setSecondPoint(point2);
//                }

                if (editingLine != null) {
                    //lines.remove(editingLine);
//                    RealPoint point3 = screenConverter.convertScreenToReal(new ScreenPoint(e.getX(), e.getY()));         Для рисования хорды
//                    currentLine = new Line(point3, point3);
                }
                cancelEdit();
            }
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            previousPoint = null;
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            if (currentCircle != null) {
                currentCircle.setSecondPoint(screenConverter.convertScreenToReal(new ScreenPoint(e.getX(), e.getY())));
                circles.add(currentCircle);
                currentCircle = null;
            }
            if (currentLine != null) {
                currentLine.setSecondPoint(screenConverter.convertScreenToReal(new ScreenPoint(e.getX(), e.getY())));
                lines.add(currentLine);
                currentLine = null;
            }
//            if (editingCircle != null && editingLine != null) {
//                editingLine.setSecondPoint(screenConverter.convertScreenToReal(new ScreenPoint(e.getX(), e.getY())));
//                editingCircle.setSecondPoint(screenConverter.convertScreenToReal(new ScreenPoint(e.getX(), e.getY())));        Когда все выделено, могли зажимать и тянуть, изменяя радиус окружности
//            }
            if (editingLine != null) {
                editingLine.setSecondPoint(screenConverter.convertScreenToReal(new ScreenPoint(e.getX(), e.getY())));
                editingCircle.setSecondPoint(screenConverter.convertScreenToReal(new ScreenPoint(e.getX(), e.getY())));
            }

        } else if (SwingUtilities.isMiddleMouseButton(e)) {
            if (editingCircle != null && editingLine != null) {
                if (closeToCircle(screenConverter, editingCircle, new ScreenPoint(e.getX(), e.getY()), 4)) {
                    circles.remove(editingCircle);
                    lines.remove(editingLine);
                    cancelEdit();
                }
            }
            if (editingCircle != null) {
                if (closeToCircle(screenConverter, editingCircle, new ScreenPoint(e.getX(), e.getY()), 4)) {
                    circles.remove(editingCircle);
                    lines.remove(editingLine);
                    cancelEdit();
                }
            }
//            if (editingLine != null) {
//                if (closeToLine(screenConverter, editingLine, new ScreenPoint(e.getX(), e.getY()), 4)) {
//                    lines.remove(editingLine);
//                    editingLine = null;
//                }
//            }
        }
        repaint();


    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            ScreenPoint currentPoint = new ScreenPoint(e.getX(), e.getY());
            RealPoint firstPoint = screenConverter.convertScreenToReal(currentPoint);
            RealPoint secondPoint = screenConverter.convertScreenToReal(previousPoint);
            RealPoint delta = secondPoint.findDelta(firstPoint);
            screenConverter.moveStartPoint(delta);
            previousPoint = currentPoint;
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            if (currentCircle != null) {
                currentCircle.setSecondPoint(screenConverter.convertScreenToReal(new ScreenPoint(e.getX(), e.getY())));
            }
        } else if (SwingUtilities.isLeftMouseButton(e)) {

//            if (editingCircle != null) {
//                editingLine.setSecondPoint(screenConverter.convertScreenToReal(new ScreenPoint(e.getX(), e.getY())));
//                editingCircle.setSecondPoint(screenConverter.convertScreenToReal(new ScreenPoint(e.getX(), e.getY())));   ?????
//            }

//            if (currentLine != null) {
//                currentLine.setSecondPoint(screenConverter.convertScreenToReal(new ScreenPoint(e.getX(), e.getY())));
//            } else
                if (editingLine != null) {
                editingLine.setSecondPoint(screenConverter.convertScreenToReal(new ScreenPoint(e.getX(), e.getY())));
                editingCircle.setSecondPoint(screenConverter.convertScreenToReal(new ScreenPoint(e.getX(), e.getY())));
            }
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private static final double SCALE_STEP = 0.05;

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int clicksNumber = e.getWheelRotation();
        double coefficient = 1 + SCALE_STEP * (clicksNumber < 0 ? -1 : 1);
        double scale = 1;
        for (int i = Math.abs(clicksNumber); i > 0; i--) {
            scale *= coefficient;
        }
        screenConverter.changeScale(scale);
        repaint();
    }
}
