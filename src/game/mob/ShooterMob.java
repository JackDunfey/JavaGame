package game.mob;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import game.App;
import game.Bullet;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

abstract public class ShooterMob extends Mob {
    private boolean canShoot = true;
    private long shootingDelay = 250L; // (ms)
    private ArrayList<Bullet> bullets = new ArrayList<>();
    public ShooterMob(Point2D position, Color color, int health){
        super(position, color, health);
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

    public ArrayList<Bullet> getBullets(){
        return this.bullets;
    }

    protected void justShot(){
        this.canShoot = false;
        new Timer().schedule(new TimerTask() {
            public void run(){
                canShoot = true;
            }
        }, shootingDelay);
    }

    public void updateBullets(){
        bullets.forEach(bullet -> bullet.update());
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
    public void update(){
        super.update();
        updateBullets();
    }
}
