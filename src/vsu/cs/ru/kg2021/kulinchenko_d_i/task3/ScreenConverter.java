package vsu.cs.ru.kg2021.kulinchenko_d_i.task3;

public class ScreenConverter {
    private double startX, startY, realWidth, realHeight;
    private int screenWidth;
    private int screenHeight;

    public ScreenConverter(double startX, double startY, double realWidth, double realHeight, int screenWidth, int screenHeight) {
        this.startX = startX;
        this.startY = startY;
        this.realWidth = realWidth;
        this.realHeight = realHeight;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public ScreenPoint convertRealToScreen(RealPoint realPoint) {
        double x = (realPoint.getRealX() - startX) / realWidth * screenWidth;
        double y = (startY - realPoint.getRealY()) / realHeight * screenHeight;
        return new ScreenPoint((int)x, (int)y);
    }

    public RealPoint convertScreenToReal(ScreenPoint screenPoint) {
        double x = startX + screenPoint.getScreenX() * realWidth / screenWidth;
        double y = startY - screenPoint.getScreenY() * realHeight / screenHeight;
        return new RealPoint(x, y);
    }

    public void moveStartPoint(RealPoint delta) {
        startX += delta.getRealX();
        startY += delta.getRealY();
    }

    public void changeScale(double scale) {
        double deltaX = (realWidth - realWidth * scale) / 2;
        double deltaY = (realHeight - realHeight * scale) / 2;
        startX += deltaX;
        startY -= deltaY;
        realWidth *= scale;
        realHeight *= scale;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getRealWidth() {
        return realWidth;
    }

    public void setRealWidth(double realWidth) {
        this.realWidth = realWidth;
    }

    public double getRealHeight() {
        return realHeight;
    }

    public void setRealHeight(double realHeight) {
        this.realHeight = realHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }
}
