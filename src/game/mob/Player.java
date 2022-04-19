package game.mob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import game.App;
import game.Bullet;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;

// Make ShootingMob an abstract class

public class Player extends Mob{
    public static final Color PLAYER_COLOR = Color.RED;
    ArrayList<Bullet> bullets = new ArrayList<>();
    private boolean canShoot = true;
    private long shootingDelay = 250L; // (ms)
    public Player(Point2D position){
        super(position, PLAYER_COLOR, 10);
        this.speed = 2.5D;
    }

    public void shoot(double x, double y){
        shoot(new Point2D(x, y));
    }

    public void shoot(Point2D point){
        if(this.canShoot)
            addBullet(new Bullet(getPosition(), point));
    }

    public void shoot(){
        if(this.canShoot)
            addBullet(new Bullet(getPosition(), getAngle()));
    }

    public void addBullet(Bullet bullet){
        for(int i = 0; i < 3; i++)
            bullet.update();
        this.bullets.add(bullet);
        this.justShot();
    }

    protected void justShot(){
        this.canShoot = false;
        new Timer().schedule(new TimerTask() {
            public void run(){
                canShoot = true;
            }
        }, shootingDelay);
    }
    
    public ArrayList<Bullet> getBullets(){
        return this.bullets;
    }

    public void processKeys(HashMap<String, Boolean> keys){
        direction.forward = keys.containsKey("W");
        direction.left = keys.containsKey("A");
        direction.backward = keys.containsKey("S");
        direction.right = keys.containsKey("D");
        direction.ccw = keys.containsKey("LEFT");
        direction.cw = keys.containsKey("RIGHT");
    }

    @Override
    public void update(){
        super.update();
        bullets.forEach(bullet -> bullet.update());
    }
    
    public void updateBullets(){
        for(int i = this.bullets.size()-1; i >= 0; i--){
            if(this.bullets.get(i).isDead()){
                this.bullets.remove(i);
                continue;
            }
            var pos = this.bullets.get(i).getPosition();
            if(pos.getX() < 0 || pos.getX() >= App.WIDTH || pos.getY() < 0 || pos.getY() >= App.HEIGHT)
                this.bullets.remove(i);
        }
    }

    @Override
    public Node getNode(){
        Group group = new Group(super.getNode());
        bullets.forEach(bullet -> group.getChildren().add(bullet.getNode()));
        return group;
    }
}
