package game.mob;

import java.util.Objects;

import game.Geometry;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Mob {
    private Point2D position;
    private double angle;
    public static char width = 25;
    public static char height = 75;
    private Color color;
    public Mob(Point2D position, Color color){
        Objects.requireNonNull(position);
        this.position = position;
        this.color = color;
    }
    public Point2D getPosition(){
        return position;
    }
    public double getAngle(){
        return angle;
    }
    // Movement (animate?)
    public void facePoint(Point2D point){
        // Change this to PID
        this.angle = Geometry.getAngle(position, point);
    }
    public void forward(double distance){
        var velocity = new Point2D(Math.cos((this.angle-90)*Math.PI/180), Math.sin((this.angle-90)*Math.PI/180));
        this.position = this.position.add(velocity.multiply(distance));
    }
    public void setPosition(Point2D pos){
        this.position = pos;
    }
    public Node getNode(){
        Rectangle rect = new Rectangle(position.getX(), position.getY(), width, height);
        rect.setRotate(this.angle);
        rect.setTranslateX(-width/2);
        rect.setTranslateY(-height/2);
        rect.setFill(this.color);
        return rect;
    }
}
