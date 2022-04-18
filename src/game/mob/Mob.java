package game.mob;

import java.util.Objects;

import game.Geometry;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Mob {
    public static class Direction{
        public boolean forward;
        public boolean right;
        public boolean left;
        public boolean backward;
        @Override
        public String toString(){
            return String.format("[Forward: %s, Right: %s, Backward: %s, Left: %s]", forward, right, left, backward);
        }
    }
    private Point2D position;
    private double angle;
    protected double speed;
    private float angle_speed;
    public static char width = 25;
    public static char height = 75;
    private Color color;
    // forward, right, backward, left
    public Direction direction;
    public Mob(Point2D position, Color color){
        Objects.requireNonNull(position);
        this.position = position;
        this.direction = new Direction();
        this.color = color;
        this.speed = 2;
        this.angle_speed = 2;
    }
    public Point2D getPosition(){
        return position;
    }
    public double getAngle(){
        return angle;
    }
    // Movement (animate?)
    public void update(){
        if(this.direction.forward)
            this.forward(this.speed);
        if(this.direction.backward)
            this.forward(-this.speed);
        if(this.direction.right)
            this.angle += this.angle_speed;
        if(this.direction.left)
            this.angle -= this.angle_speed;
    }
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
