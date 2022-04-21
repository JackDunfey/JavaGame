package game.mob;

import java.util.Arrays;
import java.util.Objects;

import game.Geometry;
import game.Sound;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

// Extend Pane?
public class Mob {
    public static class Movement{
        public boolean forward;
        public boolean right;
        public boolean left;
        public boolean backward;
        public boolean cw; // Clockwise
        public boolean ccw; // Counter-clockwise
        @Override
        public String toString(){
            return String.format("[Forward: %s, Right: %s, Backward: %s, Left: %s]", forward, right, left, backward);
        }
    }
    private Point2D position;
    private double angle;
    protected double speed = 2;
    public static char width = 25;
    public static char height = 75;
    private Color color;
    private int health;
    private boolean alive = true;
    private int killCount;
    // forward, right, backward, left
    public Movement direction;
    public Mob(Point2D position, Color color, int health){
        Objects.requireNonNull(position);
        this.position = position;
        this.direction = new Movement();
        this.color = color;
        this.health = health;
    }
    // Getters

    public Point2D getPosition(){
        return position;
    }

    public double getAngle(){
        return angle;
    }

    public int getHealth(){
        return health;
    }

    public int getKillCount(){
        return killCount;
    }

    public boolean isDead(){
        return !alive;
    }

    // Setters

    public void setPosition(Point2D pos){
        this.position = pos;
    }

    public void setAngle(double angle){
        this.angle = angle;
    }

    protected void setKills(int kills){
        this.killCount = kills;
    }

    public void kill(Mob killer){
        if(killer != null){
            killer.killCount++;
            try{
                new Sound("resources/sounds/explode.wav").play();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        this.alive = false;
    }

    // Movement (animate?)
    public void update(){
        if(this.direction.forward)
            this.move("forward", this.speed);
        if(this.direction.backward)
            this.move("backward", this.speed);
        if(this.direction.right)
            this.move("right", this.speed);
        if(this.direction.left)
            this.move("left", this.speed);
        if(this.direction.cw)
            this.angle += 1;
        if(this.direction.ccw)
            this.angle -= 1;
    }

    public void facePoint(Point2D point){
        // Change this to PID
        this.angle = Geometry.getAngle(position, point);
    }

    public Point2D getDirectionalVector(String direction){
        String[] directions = new String[]{"forward", "right", "backward", "left"};
        int multiplier = Arrays.asList(directions).indexOf(direction) - 1;
        return new Point2D(Math.cos((this.angle+90*multiplier)*Math.PI/180), Math.sin((this.angle+90*multiplier)*Math.PI/180));
    }

    public void move(String direction, double distance){
        this.position = position.add(getDirectionalVector(direction).multiply(distance));
    }

    public void damage(double damage, Mob source){
        this.health -= damage;
        if(this.health <= 0)
            this.kill(source);
    }

    public boolean isIntersecting(Mob other){
        return Mob.isIntersecting(this, other);
    }
    
    public static boolean isIntersecting(Mob a, Mob b){
        return ((Path) Rectangle.intersect(a.getBodyRectangle(), b.getBodyRectangle())).getElements().size() > 0;
    }

    public Node getNode(){
        Group mob = new Group();

        var text = new Text(position.getX(), position.getY(), Integer.toString(this.health));

        mob.getChildren().addAll(getBodyRectangle(), text);

        return mob;
    }

    public Rectangle getBodyRectangle(){
        Rectangle rect = new Rectangle(position.getX(), position.getY(), width, height);
        rect.setRotate(this.angle);
        rect.setTranslateX(-width/2);
        rect.setTranslateY(-height/2);
        rect.setFill(this.color);
        return rect;
    }

    @Override
    public String toString(){
        String s = getClass().getName();
        return String.format("%s @ %s", s.substring(s.lastIndexOf(".")+1), position);
    }
}
