package game.mob;

import java.util.ArrayList;

import game.Bullet;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class Player extends Mob{
    public static final Color PLAYER_COLOR = Color.RED;
    ArrayList<Bullet> bullets = new ArrayList<>();
    public Player(Point2D position){
        super(position, PLAYER_COLOR);
    }
    public void shoot(double x, double y){
        shoot(new Point2D(x, y));
    }
    public void shoot(Point2D point){
        this.bullets.add(new Bullet(this.getPosition(), point));
    }
    @Override
    public void update(){
        super.update();
        bullets.forEach(bullet -> bullet.update());
    }
    @Override
    public Node getNode(){
        Group group = new Group(super.getNode());
        bullets.forEach(bullet -> group.getChildren().add(bullet.getNode()));
        return group;
    }
}
