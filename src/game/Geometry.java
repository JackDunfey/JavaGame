package game;

import javafx.geometry.Point2D;

public class Geometry{
    public static double getAngle(Point2D position, Point2D point){
        double dx = point.getX() - position.getX();
        double dy = position.getY() - point.getY();
        double ref = Math.atan(dy/dx) * 180 / Math.PI;
        return dx > 0 ? 90 - ref : 360 - (90 + ref);
    }
}
