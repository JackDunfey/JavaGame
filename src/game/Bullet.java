package game;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Bullet {
    public static char WIDTH = 10;
    public static char HEIGHT = 20;
    private Point2D position;
    private Point2D directionVector;
    private double angle;
    private double speed = 3;
    private boolean alive = true;

    private double damage = 1d; 

    public Bullet(Point2D start, Point2D target){
        this.angle = Geometry.getAngle(start, target);
        this.directionVector = target.subtract(start).normalize();
        this.position = start;
    }

    public Bullet(Point2D start, double angle){
        this.angle = angle;
        this.directionVector = new Point2D(Math.cos((angle-90)*Math.PI/180), Math.sin((angle-90)*Math.PI/180));
        this.position = start;
    }

    public Point2D getPosition(){
        return this.position;
    }

    public double getDamage(){
        return this.damage;
    }

    public boolean isDead(){
        return !alive;
    }

    public void kill(){
        this.alive = false;
    }

    public void update(){
        this.position = this.position.add(this.directionVector.multiply(this.speed));
    }

    public Node getNode(){
        var rect = new Rectangle(position.getX(), position.getY(), WIDTH, HEIGHT);
        rect.setRotate(this.angle);
        rect.setTranslateX(-WIDTH/2);
        rect.setTranslateY(-HEIGHT/2);
        rect.setFill(Color.BLACK);
        return rect;
    }
}
